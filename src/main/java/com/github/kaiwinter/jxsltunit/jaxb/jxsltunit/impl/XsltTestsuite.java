package com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import com.github.kaiwinter.jxsltunit.exception.ProcessingException;
import com.github.kaiwinter.jxsltunit.exception.ResultWriterException;
import com.github.kaiwinter.jxsltunit.exception.TestConfigurationException;
import com.github.kaiwinter.jxsltunit.jaxb.UnMarshallUtil;
import com.github.kaiwinter.jxsltunit.jaxb.junit.JunitTestsuites;
import com.github.kaiwinter.jxsltunit.jaxb.junit.JunitTestsuites.JunitTestsuite;
import com.github.kaiwinter.jxsltunit.jaxb.junit.JunitTestsuites.JunitTestsuite.JunitTestcase;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;

/**
 * Java representation of the XML test configuration.
 */
@XmlRootElement(namespace = "jxsltunit")
public final class XsltTestsuite implements IXsltTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(XsltTestsuite.class.getSimpleName());

	@XmlElement
	public List<XsltTestcase> xsltTestcase;

	@XmlAttribute
	public String description;

	@XmlAttribute(name = "xml")
	public String xml;

	@XmlAttribute
	public String xslt;

	@XmlAttribute
	public String path;

	// Testresult
	@XmlTransient
	public JunitTestsuite junitTestsuite = new JunitTestsuite();

	@XmlRootElement
	public final static class XsltTestcase {

		@XmlAttribute(name = "match_number")
		public int matchNumber;

		@XmlValue
		@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
		public String content;
	}

	@Override
	public void process(String workingDirectory) throws TestConfigurationException, ProcessingException {
		if (xsltTestcase == null) {
			throw new IllegalArgumentException("Uninitialized xsltTestcase");
		} else if (description == null) {
			LOGGER.info("Parsed test specification, number of matches: {}", xsltTestcase.size());
		} else {
			LOGGER.info("Parsed test specification '{}', number of matches: {}", description, xsltTestcase.size());
		}

		boolean valid = validateTestDefinition(workingDirectory);
		if (!valid) {
			throw new TestConfigurationException(
			        "Invalid test configuration: " + junitTestsuite.junitTestcase.get(0).error.systemError);
		}

		try {
			Path tempFileOutput = Files.createTempFile("xslttest", null);
			LOGGER.trace("Using temp file: '{}'", tempFileOutput);

			xslTransformXml(workingDirectory, tempFileOutput);

			runTests(tempFileOutput);
		} catch (IOException | TransformerException e) {
			throw new ProcessingException(e);
		}
	}

	/**
	 * Validates the passed test specification for sanity.
	 *
	 * @param workingDirectory
	 *            the directory in which the xml and xsl is looked up
	 *
	 * @param xsltTest
	 *            the test specification
	 * @return
	 */
	private boolean validateTestDefinition(String workingDirectory) {
		Collection<String> errors = new ArrayList<>();

		if (!new File(workingDirectory, xml).exists()) {
			String message = "XML not found (xml attribute): '" + xml + "'";
			errors.add(message);
		}
		if (!new File(workingDirectory, xslt).exists()) {
			String message = "XSL not found (xslt attribute): '" + xslt + "'";
			errors.add(message);
		}

		if (!errors.isEmpty()) {
			String collect = errors.stream().map(s -> s.toString()).collect(Collectors.joining(", "));

			JunitTestcase junitTestcase = new JunitTestcase();
			junitTestcase.name = String.valueOf("init");
			junitTestsuite.name = description;
			junitTestsuite.junitTestcase.add(junitTestcase);
			junitTestcase.error = new JunitTestcase.Error();
			junitTestcase.error.systemError = collect;
			return false;
		}
		return true;
	}

	/**
	 * XSL Transforms the XML file.
	 *
	 * @param xsltTest
	 *            the Test specification object
	 * @param tempFileOutput
	 *            the temporary file
	 */
	private void xslTransformXml(String workingDirectory, Path tempFileOutput) throws TransformerException {
		Source xmlInput = new StreamSource(new File(workingDirectory, xml));
		Source xsltInput = new StreamSource(new File(workingDirectory, xslt));
		Result xmlOutput = new StreamResult(tempFileOutput.toFile());

		Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltInput);
		transformer.transform(xmlInput, xmlOutput);
	}

	/**
	 * Checks the specified tests against the xsl'transformed file.
	 *
	 * @param tempFileOutput
	 *            the temporary file which contains the result of the XSL transformation
	 *
	 * @throws IOException
	 *             if the temp file cannot be accessed
	 * @throws TestConfigurationException
	 *             if the test configuration contains errors
	 */
	private void runTests(Path tempFileOutput) throws IOException, TestConfigurationException {
		Objects.requireNonNull(path, "path must not be null");

		// Load transformed file
		Document document = Jsoup.parse(tempFileOutput.toFile(), null);

		// Get JSoup pseudo path to testable XML node in result
		Elements elementsToTest = document.select(path);
		if (elementsToTest.isEmpty()) {
			String message = MessageFormat.format("No data found at path: ''{0}''", path);
			throw new TestConfigurationException(message);
		}

		// Iterate test cases
		for (XsltTestsuite.XsltTestcase element : xsltTestcase) {
			LOGGER.info("Testing match number {}", element.matchNumber);

			if (element.matchNumber > elementsToTest.size() - 1) {
				String message = MessageFormat.format(
				        "Number of matches: {0} (highest index: {1}), test case wants match with index: {2}",
				        elementsToTest.size(), elementsToTest.size() - 1, element.matchNumber);
				throw new TestConfigurationException(message);
			}
			Element elementXslt = elementsToTest.get(element.matchNumber);

			// Assert.assertEquals("Test case " + element.number, element.content.replaceAll("\\s", "").toLowerCase(),
			// elementXslt.outerHtml().replaceAll("\\s", "").toLowerCase());

			JunitTestcase testcase = new JunitTestcase();
			testcase.name = String.valueOf("Match number: " + element.matchNumber);
			junitTestsuite.name = description;
			junitTestsuite.junitTestcase.add(testcase);

			// TODO KW: instead of outerHtml use html. Doing this needs to distinguish between html and text content.
			Diff diff = DiffBuilder.compare(Input.fromString(element.content.toLowerCase())) //
			        .withTest(Input.fromString(elementXslt.outerHtml().toLowerCase())) //
			        .ignoreWhitespace() //
			        .build();
			if (diff.hasDifferences()) {
				Iterable<Difference> differences = diff.getDifferences();
				String message = StreamSupport.stream(differences.spliterator(), false).map(s -> s.toString())
				        .collect(Collectors.joining(", "));
				testcase.error = new JunitTestcase.Error();
				testcase.error.systemError = message;
			}
		}
	}

	@Override
	public void writeResultAsXml(OutputStream outputStream) throws ResultWriterException {
		JunitTestsuites testsuites = new JunitTestsuites();
		testsuites.junitTestsuite.add(this.junitTestsuite);

		UnMarshallUtil.marshall(testsuites, outputStream);
	}

	@Override
	public void writeResultAsText(OutputStream outputStream) throws ResultWriterException {
		if (description == null) {
			writeln(outputStream, "Testsuite 'unnamed'");
		} else {
			writeln(outputStream, "Testsuite '" + description + "'");
		}

		for (JunitTestcase junitTestcase : junitTestsuite.junitTestcase) {
			if (junitTestcase.error == null) {
				writeln(outputStream, "- " + junitTestcase.name + " ... OK");
			} else {
				writeln(outputStream, "- " + junitTestcase.name + " ... FAIL: " + junitTestcase.error.systemError);
			}
		}
	}

	/**
	 * Prints the string with a line break to the given stream.
	 *
	 * @param outputStream
	 *            the stream to write on
	 * @param string
	 *            the string to write followed by a line break
	 * @throws ResultWriterException
	 *             if the result of a test cannot be written to the output stream.
	 */
	private void writeln(OutputStream outputStream, String string) throws ResultWriterException {
		try {
			outputStream.write((string + System.lineSeparator()).getBytes());
		} catch (IOException e) {
			throw new ResultWriterException(e);
		}
	}
}