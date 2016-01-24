package com.github.kaiwinter.jxsltunit.core;

import java.io.File;

import com.github.kaiwinter.jxsltunit.jaxb.UnMarshallUtil;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.XsltTestsuite;
import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.XsltTestsuites;

/**
 * Use case class which loads an XSLT test definition, runs it and outputs the result.
 */
public final class XsltTestUc {

	/**
	 * Runs the tests defined in <code>testDefinition</code> and outputs the results by using the
	 * <code>resultWriters</code>.
	 *
	 * @param testDefinition
	 *            XML file with the definition of {@link XsltTestsuites} or {@link XsltTestsuite}.
	 * @param resultWriters
	 *            used to output the result, e.g. to console and/or a file
	 */
	public void runTests(File testDefinition, ResultWriter... resultWriters) {
		IXsltTest xsltTest = UnMarshallUtil.loadTestDefinition(testDefinition);

		xsltTest.process();

		for (ResultWriter resultWriter : resultWriters) {
			resultWriter.write(xsltTest);
		}
	}
}
