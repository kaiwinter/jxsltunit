package com.github.kaiwinter.jxsltunit.core;

import com.github.kaiwinter.jxsltunit.exception.ResultWriterException;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;

/**
 * Defines the format of how the test result is written.
 */
public interface ResultWriter {

	/**
	 * Writes the <code>xsltTest</code> to the output stream.
	 *
	 * @param xsltTest
	 *            the {@link IXsltTest} result to write
	 * @throws ResultWriterException
	 *             if the result of a test cannot be written to the output stream.
	 */
	void write(IXsltTest xsltTest) throws ResultWriterException;
}