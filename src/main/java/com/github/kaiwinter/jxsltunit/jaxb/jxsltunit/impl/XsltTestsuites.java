package com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.impl;

import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaiwinter.jxsltunit.exception.ProcessingException;
import com.github.kaiwinter.jxsltunit.exception.ResultWriterException;
import com.github.kaiwinter.jxsltunit.exception.TestConfigurationException;
import com.github.kaiwinter.jxsltunit.jaxb.UnMarshallUtil;
import com.github.kaiwinter.jxsltunit.jaxb.junit.JunitTestsuites;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;

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
	public void process(String workingDirectory) throws TestConfigurationException, ProcessingException {
		LOGGER.info("Running test suite, number of test cases: {}", xsltTestsuite.size());
		for (XsltTestsuite test : xsltTestsuite) {
			test.process(workingDirectory);
		}
	}

	@Override
	public void writeResultAsXml(OutputStream outputStream) throws ResultWriterException {
		JunitTestsuites testsuites = new JunitTestsuites();
		for (XsltTestsuite xsltTest : this.xsltTestsuite) {
			testsuites.junitTestsuite.add(xsltTest.junitTestsuite);
		}

		UnMarshallUtil.marshall(testsuites, outputStream);
	}

	@Override
	public void writeResultAsText(OutputStream outputStream) throws ResultWriterException {
		for (XsltTestsuite xsltTest : this.xsltTestsuite) {
			xsltTest.writeResultAsText(outputStream);
		}
	}
}