package com.github.kaiwinter.jxsltunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

public final class MainTest {

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	/**
	 * Single testsuite, outputs as plain text.
	 */
	@Test
	public void testTestsuiteOutputText() throws IOException, URISyntaxException {
		String filename = new File(MainTest.class.getResource("/testsuite.xml").toURI()).getAbsolutePath();

		Main.main(new String[] { "-config", filename });

		assertEquals("Testsuite 'Testsuite Test'\n- Match number: 1 ... OK\n",
		        systemOutRule.getLogWithNormalizedLineSeparator());
	}

	/**
	 * Multiple testsuites, outputs as plain text.
	 */
	@Test
	public void testTestsuitesOutputText() throws IOException, URISyntaxException {
		String filename = new File(MainTest.class.getResource("/testsuites.xml").toURI()).getAbsolutePath();

		Main.main(new String[] { "-config", filename });

		assertEquals(
		        "Testsuite 'Testsuite Test - 1'\n- Match number: 1 ... OK\nTestsuite 'Testsuite Test - 2'\n- Match number: 0 ... OK\n",
		        systemOutRule.getLogWithNormalizedLineSeparator());
	}

	/**
	 * Single testsuite, outputs as XML.
	 */
	@Test
	public void testTestsuiteOutputXML() throws IOException, URISyntaxException {
		String filename = new File(MainTest.class.getResource("/testsuite.xml").toURI()).getAbsolutePath();

		Main.main(new String[] { "-config", filename, "-format", "xml" });

		Diff myDiff = DiffBuilder //
		        .compare(
		                "<testsuites><testsuite name='Testsuite Test'><testcase name='Match number: 1'/></testsuite></testsuites>") //
		        .withTest(systemOutRule.getLog()) //
		        .checkForIdentical().ignoreWhitespace().build();

		assertFalse(myDiff.toString(), myDiff.hasDifferences());
	}

	/**
	 * Multiple testsuites, outputs as XML
	 */
	@Test
	public void testTestsuitesOutputXML() throws IOException, URISyntaxException {
		String filename = new File(MainTest.class.getResource("/testsuites.xml").toURI()).getAbsolutePath();

		Main.main(new String[] { "-config", filename, "-format", "xml" });

		Diff myDiff = DiffBuilder //
		        .compare(
		                "<testsuites><testsuite name='Testsuite Test - 1'><testcase name='Match number: 1'/></testsuite><testsuite name='Testsuite Test - 2'><testcase name='Match number: 0'/></testsuite></testsuites>") //
		        .withTest(systemOutRule.getLog()) //
		        .checkForIdentical().ignoreWhitespace().build();

		assertFalse(myDiff.toString(), myDiff.hasDifferences());
	}
}
