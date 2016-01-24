package com.github.kaiwinter.jxsltunit.jaxb;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.github.kaiwinter.jxsltunit.Main;
import com.github.kaiwinter.jxsltunit.exception.ResultWriterException;
import com.github.kaiwinter.jxsltunit.exception.TestConfigurationException;
import com.github.kaiwinter.jxsltunit.jaxb.junit.JunitTestsuites;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.impl.XsltTestsuite;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.impl.XsltTestsuites;

public final class UnMarshallUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnMarshallUtil.class.getSimpleName());

	private static final String XSD = "jxslttestsuite.xsd";

	/**
	 * Loads the test cases from the passed XML file.
	 *
	 * @param file
	 *            the file with the test specification
	 *
	 * @return the parsed test specification, either of type {@link XsltTestsuite} or {@link XsltTestsuites}
	 * @throws TestConfigurationException
	 *             if the schema or marshaller could not be created
	 */
	public static IXsltTest loadTestDefinition(File file) throws TestConfigurationException {
		try {
			JAXBContext context;
			context = JAXBContext.newInstance(XsltTestsuites.class, XsltTestsuite.class);

			Unmarshaller unmarshaller = context.createUnmarshaller();

			URL resource = Main.class.getResource(XSD);
			if (resource == null) {
				LOGGER.error("Schema {} not found!", XSD);
				System.exit(1);
			}

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(resource);
			unmarshaller.setSchema(schema);
			IXsltTest xsltTest = (IXsltTest) unmarshaller.unmarshal(file);
			return xsltTest;
		} catch (JAXBException | SAXException e) {
			throw new TestConfigurationException(e);
		}
	}

	/**
	 * Writes the passed <code>testsuites</code> in junit XML result format to the passed <code>outputStream</code>.
	 *
	 * @param testsuites
	 *            the test results to output
	 * @param outputStream
	 *            the {@link OutputStream} to write to
	 * @throws ResultWriterException
	 *             if the result of a test cannot be written to the output stream.
	 */
	public static void marshall(JunitTestsuites testsuites, OutputStream outputStream) throws ResultWriterException {
		try {
			JAXBContext context = JAXBContext.newInstance(JunitTestsuites.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.marshal(testsuites, outputStream);
		} catch (JAXBException e) {
			throw new ResultWriterException(e);
		}
	}
}
