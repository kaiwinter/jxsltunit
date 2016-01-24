package com.github.kaiwinter.jxsltunit.jaxb;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.github.kaiwinter.jxsltunit.jaxb.UnMarshallUtil;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.XsltTestsuite;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.XsltTestsuites;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.XsltTestsuite.XsltTestcase;

public final class UnMarshallUtilTest {

	/**
	 * Asserts that a simple {@link XsltTestsuite} with one {@link XsltTestcase} is read correctly.
	 */
	@Test
	public void testLoadTestsuite() throws JAXBException, SAXException, URISyntaxException {
		URL resource = getClass().getResource("/testsuite.xml");
		File file = new File(resource.toURI());
		IXsltTest xsltTest = UnMarshallUtil.loadTestDefinition(file);
		XsltTestsuite testsuite = (XsltTestsuite) xsltTest;

		assertEquals("Testsuite Test", testsuite.description);
		assertEquals("min-test.xml", testsuite.xml);
		assertEquals("min-test.xslt", testsuite.xslt);
		assertEquals("pa > ch", testsuite.path);
		assertEquals(1, testsuite.xsltTestcase.size());
		assertEquals(1, testsuite.xsltTestcase.iterator().next().matchNumber);
		assertEquals("<ch>child 2</ch>", testsuite.xsltTestcase.iterator().next().content);
	}

	/**
	 * Asserts that a simple {@link XsltTestsuite} with two {@link XsltTestcase}s is read correctly.
	 */
	@Test
	public void testLoadTestsuiteMultipleTestcases() throws JAXBException, SAXException, URISyntaxException {
		URL resource = getClass().getResource("/testsuite-multiple-testcases.xml");
		File file = new File(resource.toURI());
		IXsltTest xsltTest = UnMarshallUtil.loadTestDefinition(file);
		XsltTestsuite testsuite = (XsltTestsuite) xsltTest;

		assertEquals("Testsuite Test", testsuite.description);
		assertEquals("min-test.xml", testsuite.xml);
		assertEquals("min-test.xslt", testsuite.xslt);
		assertEquals("pa > ch", testsuite.path);
		assertEquals(2, testsuite.xsltTestcase.size());

		// Testcase 1
		XsltTestcase testcase = testsuite.xsltTestcase.get(0);
		assertEquals(0, testcase.matchNumber);
		assertEquals("<ch>child 1</ch>", testcase.content);

		// Testcase 2
		testcase = testsuite.xsltTestcase.get(1);
		assertEquals(1, testcase.matchNumber);
		assertEquals("<ch>child 2</ch>", testcase.content);
	}

	/**
	 * Asserts that a {@link XsltTestsuites} which contains two {@link XsltTestsuite}s with one {@link XsltTestcase}
	 * each is read correctly.
	 */
	@Test
	public void testLoadTestsuites() throws JAXBException, SAXException, URISyntaxException {
		URL resource = getClass().getResource("/testsuites.xml");
		File file = new File(resource.toURI());
		IXsltTest xsltTest = UnMarshallUtil.loadTestDefinition(file);
		XsltTestsuites testsuites = (XsltTestsuites) xsltTest;

		assertEquals(2, testsuites.xsltTestsuite.size());

		// Testsuite 1
		XsltTestsuite testsuite = testsuites.xsltTestsuite.get(0);
		assertEquals("Testsuite Test - 1", testsuite.description);
		assertEquals("min-test.xml", testsuite.xml);
		assertEquals("min-test.xslt", testsuite.xslt);
		assertEquals("pa > ch", testsuite.path);
		assertEquals(1, testsuite.xsltTestcase.size());
		assertEquals(1, testsuite.xsltTestcase.iterator().next().matchNumber);
		assertEquals("<ch>child 2</ch>", testsuite.xsltTestcase.iterator().next().content);

		// Testsuite 2
		testsuite = testsuites.xsltTestsuite.get(1);
		assertEquals("Testsuite Test - 2", testsuite.description);
		assertEquals("min-test.xml", testsuite.xml);
		assertEquals("min-test.xslt", testsuite.xslt);
		assertEquals("pa > ch", testsuite.path);
		assertEquals(1, testsuite.xsltTestcase.size());
		assertEquals(0, testsuite.xsltTestcase.iterator().next().matchNumber);
		assertEquals("<ch>child 1</ch>", testsuite.xsltTestcase.iterator().next().content);
	}
}
