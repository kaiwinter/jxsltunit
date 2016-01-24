package com.github.kaiwinter.jxsltunit.jaxb.junit;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.impl.XsltTestsuites;

/**
 * Data structure which is filled with the result of a {@link XsltTestsuites} and is used to be marshaled as a junit
 * result XML directly.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "testsuites")
public final class JunitTestsuites {

	@XmlElement(name = "testsuite")
	public List<JunitTestsuite> junitTestsuite = new ArrayList<>();

	public static final class JunitTestsuite {
		@XmlAttribute
		public String name;

		@XmlElement(name = "testcase")
		public List<JunitTestcase> junitTestcase = new ArrayList<>();

		public static final class JunitTestcase {
			@XmlAttribute
			public String name;

			public Error error;

			public static final class Error {
				@XmlElement(name = "system-err")
				public String systemError;
			}
		}
	}

}
