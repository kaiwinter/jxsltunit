package com.github.kaiwinter.jxsltunit.jaxb.jxsltunit;

import java.io.OutputStream;

import com.github.kaiwinter.jxsltunit.exception.ProcessingException;
import com.github.kaiwinter.jxsltunit.exception.ResultWriterException;
import com.github.kaiwinter.jxsltunit.exception.TestConfigurationException;

/**
 * Java representation of the XML test configuration. This can either be a single {@link XsltTestsuite} or a
 * {@link XsltTestsuites} which encloses multiple {@link XsltTestsuite} definitions.
 */
public interface IXsltTest {

	/**
	 * Runs the tests.
	 *
	 * @throws TestConfigurationException
	 *             if the configuration contains errors
	 * @throws ProcessingException
	 *             if an error occurs while transforming the XML
	 */
	void process() throws TestConfigurationException, ProcessingException;

	/**
	 * Writes the result of this test suite(s) to the passed stream.
	 *
	 * @param outputStream
	 *            the stream to write the result to
	 * @throws ResultWriterException
	 *             if the result of a test cannot be written to the output stream.
	 */
	void writeResultAsXml(OutputStream outputStream) throws ResultWriterException;

	/**
	 * Prints the result of this test suite(s) to standard out.
	 *
	 * @param outputStream
	 *            the stream to write the result to
	 * @throws ResultWriterException
	 *             if the result of a test cannot be written to the output stream.
	 */
	void writeResultAsText(OutputStream outputStream) throws ResultWriterException;
}
