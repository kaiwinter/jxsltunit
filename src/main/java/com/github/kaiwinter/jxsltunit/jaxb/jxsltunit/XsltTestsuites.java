package com.github.kaiwinter.jxsltunit.jaxb.jxsltunit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.github.kaiwinter.jxsltunit.jaxb.UnMarshallUtil;
import com.github.kaiwinter.jxsltunit.jaxb.junit.JunitTestsuites;

/**
 * Java representation of the XML test configuration of the outer &lt;xsltTestsuites&gt; which encloses
 * &lt;xsltTestsuite&gt;s.
 */
@XmlRootElement(namespace = "jxsltunit")
public final class XsltTestsuites implements IXsltTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(XsltTestsuites.class.getSimpleName());

	@XmlElement
	public List<XsltTestsuite> xsltTestsuite;

	@Override
	public void process() throws TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
		LOGGER.info("Running test suite, number of test cases: {}", xsltTestsuite.size());
		for (XsltTestsuite test : xsltTestsuite) {
			test.process();
		}
	}

	@Override
	public void writeResultAsXml(OutputStream outputStream) throws JAXBException {
		JunitTestsuites testsuites = new JunitTestsuites();
		for (XsltTestsuite xsltTest : this.xsltTestsuite) {
			testsuites.junitTestsuite.add(xsltTest.junitTestsuite);
		}

		UnMarshallUtil.marshall(testsuites, outputStream);
	}

	@Override
	public void writeResultAsText(OutputStream outputStream) throws IOException {
		for (XsltTestsuite xsltTest : this.xsltTestsuite) {
			xsltTest.writeResultAsText(outputStream);
		}
	}
}