package com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.impl;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.github.kaiwinter.jxsltunit.exception.TestConfigurationException;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.impl.XsltTestsuite;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.impl.XsltTestsuite.XsltTestcase;

public final class XsltTestsuiteTest {

	/**
	 * A successful test with no error.
	 */
	@Test
	public void testProcessValid() {
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
	@Test(expected = TestConfigurationException.class)
	public void testProcessTestcaseNotFound() {
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
	@Test(expected = TestConfigurationException.class)
	public void testProcessPathNotFound() {
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
	@Test(expected = TestConfigurationException.class)
	public void testProcessMissingXml() {
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
	@Test(expected = TestConfigurationException.class)
	public void testProcessMissingXslt() {
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
	public void testProcessDiffInResult() {
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
