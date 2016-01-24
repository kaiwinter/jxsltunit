package com.github.kaiwinter.jxsltunit.exception;

/**
 * Thrown if an error occurs while transforming the XML file or the temporary file cannot be accessed.
 */
@SuppressWarnings("serial")
public final class ProcessingException extends RuntimeException {

	public ProcessingException(Exception e) {
		super(e);
	}
}
