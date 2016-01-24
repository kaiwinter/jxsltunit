package com.github.kaiwinter.jxsltunit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.github.kaiwinter.jxsltunit.core.ResultWriter;
import com.github.kaiwinter.jxsltunit.core.TextResultWriter;
import com.github.kaiwinter.jxsltunit.core.XmlResultWriter;
import com.github.kaiwinter.jxsltunit.core.XsltTestUc;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class.getSimpleName());

	/**
	 * @param args
	 *            the test file specification and output format
	 * @throws IllegalArgumentException
	 *             if no test file was passed or test file cannot be opened
	 */
	public static void main(String[] args) throws JAXBException, SAXException, TransformerFactoryConfigurationError,
	        TransformerException, IOException {

		CommandLineArgs commandLineArgs = parseCommandLineArgs(args);
		if (commandLineArgs != null) {
			start(commandLineArgs);
		}
	}

	private static CommandLineArgs parseCommandLineArgs(String[] args) {
		CommandLineArgs commandLineArgs = new CommandLineArgs();
		CmdLineParser commandLineArgsParser = new CmdLineParser(commandLineArgs);
		try {
			commandLineArgsParser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			commandLineArgsParser.printUsage(System.err);
			return null;
		}

		return commandLineArgs;
	}

	private static void start(CommandLineArgs commandLineArgs) throws JAXBException, SAXException,
	        TransformerFactoryConfigurationError, TransformerException, IOException {

		LOGGER.info("XML file with test specification: '{}'", commandLineArgs.config);
		File file = new File(commandLineArgs.config);
		if (!file.exists()) {
			throw new IllegalArgumentException("Test file cannot be opened: " + file.getAbsolutePath());
		}

		OutputStream outputStream = null;
		try {
			if (commandLineArgs.outfile == null) {
				outputStream = System.out;
			} else {
				outputStream = new FileOutputStream(commandLineArgs.outfile);
			}

			ResultWriter resultWriter;
			if (CommandLineArgs.OutputFormat.XML == commandLineArgs.format) {
				resultWriter = new XmlResultWriter(outputStream);
			} else {
				resultWriter = new TextResultWriter(outputStream);
			}
			new XsltTestUc().runTests(file, resultWriter);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	private static class CommandLineArgs {
		enum OutputFormat {
			TEXT, XML
		};

		@Option(name = "-config", usage = "the xml file containing the test specification", required = true)
		public String config;

		@Option(name = "-format", usage = "the output format, text or junit xml. Defaults to text", required = false)
		public OutputFormat format;

		@Option(name = "-outfile", usage = "file to write the output to. If not set result is written to standart out", required = false)
		public String outfile;
	}
}
