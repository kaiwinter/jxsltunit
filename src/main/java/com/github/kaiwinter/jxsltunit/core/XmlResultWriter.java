package com.github.kaiwinter.jxsltunit.core;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import com.github.kaiwinter.jxsltunit.jaxb.jxsltunit.IXsltTest;

/**
 * Writes the result in Junit´s XML format.
 */
public final class XmlResultWriter implements ResultWriter {

	private final OutputStream outputStream;

	/**
	 * Constructs a new {@link XmlResultWriter}.
	 * 
	 * @param outputStream
	 *            the {@link OutputStream} to {@link #write(IXsltTest)} to
	 */
	public XmlResultWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void write(IXsltTest xsltTest) throws JAXBException, IOException {
		xsltTest.writeResultAsXml(outputStream);
	}

}
