package com.github.kaiwinter.jxsltunit.jaxb.jxsltunit;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

/**
 * Java representation of the XML test configuration. This can either be a single {@link XsltTestsuite} or a
 * {@link XsltTestsuites} which encloses multiple {@link XsltTestsuite} definitions.
 */
public interface IXsltTest {

	/**
	 * Runs the tests.
	 */
	void process() throws TransformerFactoryConfigurationError, TransformerException, IOException, SAXException;

	/**
	 * Writes the result of this test suite(s) to the passed stream.
	 * 
	 * @param outputStream
	 *            the stream to write the result to
	 */
	void writeResultAsXml(OutputStream outputStream) throws JAXBException;

	/**
	 * Prints the result of this test suite(s) to standard out.
	 * 
	 * @param outputStream
	 *            the stream to write the result to
	 */
	void writeResultAsText(OutputStream outputStream) throws IOException;
}
