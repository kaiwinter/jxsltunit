package com.github.kaiwinter.jxsltunit.exception;

/**
 * Exception which is thrown if the test configuration contains errors.
 */
@SuppressWarnings("serial")
public final class TestConfigurationException extends RuntimeException {

	public TestConfigurationException(Exception e) {
		super(e);
	}

	public TestConfigurationException(String message) {
		super(message);
	}
}
