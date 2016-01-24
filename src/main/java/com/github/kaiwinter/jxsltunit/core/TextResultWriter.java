package com.github.kaiwinter.jxsltunit.core;

import java.io.OutputStream;

import com.github.kaiwinter.jxsltunit.exception.ResultWriterException;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;

/**
 * Writes the result as plain text.
 */
public final class TextResultWriter implements ResultWriter {

	private final OutputStream outputStream;

	/**
	 * Constructs a new {@link TextResultWriter}.
	 *
	 * @param outputStream
	 *            the {@link OutputStream} to {@link #write(IXsltTest)} to
	 */
	public TextResultWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void write(IXsltTest xsltTest) throws ResultWriterException {
		xsltTest.writeResultAsText(outputStream);
	}
}
