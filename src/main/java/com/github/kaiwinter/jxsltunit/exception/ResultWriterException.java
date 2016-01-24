package com.github.kaiwinter.jxsltunit.exception;

/**
 * Thrown if the result of a test cannot be written to the output stream.
 */
@SuppressWarnings("serial")
public final class ResultWriterException extends RuntimeException {

	public ResultWriterException(Throwable e) {
		super(e);
	}
}
