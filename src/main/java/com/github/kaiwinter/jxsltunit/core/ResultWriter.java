package com.github.kaiwinter.jxsltunit.core;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;

/**
 * Defines the format of how the test result is written.
 */
public interface ResultWriter {

    /**
     * Writes the <code>xsltTest</code> to the output stream.
     * 
     * @param xsltTest
     *            the {@link IXsltTest} result to write
     */
    void write(IXsltTest xsltTest) throws JAXBException, IOException;
}