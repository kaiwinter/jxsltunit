package com.github.kaiwinter.jxsltunit.jaxb.jxsltunit;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.github.kaiwinter.jxsltunit.core.XsltTestError;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.XsltTestsuite;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.XsltTestsuite.XsltTestcase;

public final class XsltTestsuiteTest {

	/**
	 * A successful test with no error.
	 */
	@Test
	public void testProcessValid()
	        throws TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
		XsltTestsuite testsuite = new XsltTestsuite();
		testsuite.xml = getClass().getResource("/min-test.xml").getFile();
		testsuite.xslt = getClass().getResource("/min-test.xslt").getFile();
		testsuite.path = "pa > ch";

		XsltTestcase testcase = new XsltTestcase();
		testcase.matchNumber = 0;
		testcase.content = "<ch>child 1</ch>";

		testsuite.xsltTestcase = new ArrayList<>();
		testsuite.xsltTestcase.add(testcase);
		testsuite.process();

		Assert.assertNull(testsuite.junitTestsuite.junitTestcase.get(0).error);
	}

	/**
	 * Failing test. Defines a third matching element which is not in the result.
	 */
	@Test(expected = XsltTestError.class)
	public void testProcessTestcaseNotFound()
	        throws TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
		XsltTestsuite testsuite = new XsltTestsuite();
		testsuite.xml = getClass().getResource("/min-test.xml").getFile();
		testsuite.xslt = getClass().getResource("/min-test.xslt").getFile();
		testsuite.path = "pa > ch";

		XsltTestcase testcase = new XsltTestcase();
		testcase.matchNumber = 3;
		testcase.content = "<ch>child 3</ch>";

		testsuite.xsltTestcase = new ArrayList<>();
		testsuite.xsltTestcase.add(testcase);
		testsuite.process();

		Assert.fail("XsltTestError not thrown");
	}

	/**
	 * Failing test. Path in the result XML not found.
	 */
	@Test(expected = XsltTestError.class)
	public void testProcessPathNotFound()
	        throws TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
		XsltTestsuite testsuite = new XsltTestsuite();
		testsuite.xml = getClass().getResource("/min-test.xml").getFile();
		testsuite.xslt = getClass().getResource("/min-test.xslt").getFile();
		testsuite.path = "pa > cha";

		XsltTestcase testcase = new XsltTestcase();
		testcase.matchNumber = 0;
		testcase.content = "<ch>child 1</ch>";

		testsuite.xsltTestcase = new ArrayList<>();
		testsuite.xsltTestcase.add(testcase);
		testsuite.process();

		Assert.fail("XsltTestError not thrown");
	}

	/**
	 * Failing test. Invalid configuration, xml missing.
	 */
	@Test(expected = XsltTestError.class)
	public void testProcessMissingXml()
	        throws TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
		XsltTestsuite testsuite = new XsltTestsuite();
		testsuite.xml = "";
		testsuite.xslt = getClass().getResource("/min-test.xslt").getFile();
		testsuite.path = "pa > ch";

		XsltTestcase testcase = new XsltTestcase();
		testsuite.xsltTestcase = new ArrayList<>();
		testsuite.xsltTestcase.add(testcase);
		testsuite.process();

		Assert.fail("XsltTestError not thrown");
	}

	/**
	 * Failing test. Invalid configuration, xslt missing.
	 */
	@Test(expected = XsltTestError.class)
	public void testProcessMissingXslt()
	        throws TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
		XsltTestsuite testsuite = new XsltTestsuite();
		testsuite.xml = getClass().getResource("/min-test.xml").getFile();
		testsuite.xslt = "";
		testsuite.path = "pa > ch";

		XsltTestcase testcase = new XsltTestcase();
		testsuite.xsltTestcase = new ArrayList<>();
		testsuite.xsltTestcase.add(testcase);
		testsuite.process();

		Assert.fail("XsltTestError not thrown");
	}

	/**
	 * Failing test. Difference in transformed XML.
	 */
	@Test
	public void testProcessDiffInResult()
	        throws TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
		XsltTestsuite testsuite = new XsltTestsuite();
		testsuite.xml = getClass().getResource("/min-test.xml").getFile();
		testsuite.xslt = getClass().getResource("/min-test.xslt").getFile();
		testsuite.path = "pa > ch";

		XsltTestcase testcase = new XsltTestcase();
		testcase.matchNumber = 0;
		testcase.content = "<ch>child 2</ch>";

		testsuite.xsltTestcase = new ArrayList<>();
		testsuite.xsltTestcase.add(testcase);
		testsuite.process();

		String expected = "Expected text value 'child 2' but was 'child 1' - comparing <ch ...>child 2</ch> at /ch[1]/text()[1] to <ch ...>child 1</ch> at /ch[1]/text()[1] (DIFFERENT)";
		Assert.assertEquals(expected, testsuite.junitTestsuite.junitTestcase.get(0).error.systemError);
	}
}
