package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class ShellImplIT {
	private static final String TEST_DIR = System.getProperty("user.dir") + OSUtil.SEP + "test_system" + OSUtil.SEP
			+ "quote_test_system";
	private static final String NEW_LINE = OSUtil.NEWLINE;
	private static final String FILE_CONTENT = "This file is named \"file\" in quote_test_system.";
	private static final String NAME_CONTENT = "This file is named \"name\" in quote_test_system.";
	private static final String FILE_NAME_CONTENT = "This file is named \"file name\" in quote_test_system.";
	private static final String BQ_CONTENT = "This file is named \"`back quote`\" in quote_test_system.";

	private ShellImpl shell;
	private String cmdline, expected;
	private ByteArrayOutputStream output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		createFileForTest();

		shell = new ShellImpl();
		cmdline = expected = "";
		output = new ByteArrayOutputStream();
	}

	@Test
	public void testSeqInNoQuote() throws ShellException, AbstractApplicationException {
		expected = "seq1" + NEW_LINE + "seq2";
		cmdline = "echo seq1; echo seq2";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testSeqInDoubleQuote() throws ShellException, AbstractApplicationException {
		expected = "double ; echo quote";
		cmdline = "echo double \"; echo quote\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testSeqInSingleQuote() throws ShellException, AbstractApplicationException {
		expected = "single ; echo quote";
		cmdline = "echo single '; echo quote'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testPipeInNoQuote() throws ShellException, AbstractApplicationException {
		expected = "Pipe";
		cmdline = "echo pipe| sed s/pipe/Pipe/";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testPipeInDoubleQuote() throws ShellException, AbstractApplicationException {
		expected = "double quote| sed s/pipe/Pipe/";
		cmdline = "echo double quote\"| sed s/pipe/Pipe/\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testPipeInSingleQuote() throws ShellException, AbstractApplicationException {
		expected = "single quote| sed s/pipe/Pipe/";
		cmdline = "echo single quote'| sed s/pipe/Pipe/'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsInNoQuote() throws ShellException, AbstractApplicationException {
		expected = FILE_CONTENT + NEW_LINE + NAME_CONTENT;
		cmdline = "cat file name";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsInDoubleQuote() throws ShellException, AbstractApplicationException {
		expected = FILE_NAME_CONTENT;
		cmdline = "cat \"file name\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsInSingleQuote() throws ShellException, AbstractApplicationException {
		expected = FILE_NAME_CONTENT;
		cmdline = "cat 'file name'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testGlobInNoQuote() throws ShellException, AbstractApplicationException {
		expected = "`back quote` file file name name";
		cmdline = "echo *";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testGlobInDoubleQuote() throws ShellException, AbstractApplicationException {
		expected = "*";
		cmdline = "echo \"*\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testGlobInSingleQuote() throws ShellException, AbstractApplicationException {
		expected = "*";
		cmdline = "echo '*'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testCmdSubInNoQuote() throws ShellException, AbstractApplicationException {
		expected = FILE_CONTENT;
		cmdline = "`echo cat` file";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testCmdSubInDoubleQuote() throws ShellException, AbstractApplicationException {
		expected = NAME_CONTENT;
		cmdline = "\"`echo cat`\" name";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testCmdSubInSingleQuote() throws ShellException, AbstractApplicationException {
		expected = BQ_CONTENT;
		cmdline = "cat '`back quote`'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testInputRedirInNoQuote() throws ShellException, AbstractApplicationException {
		expected = "";
		cmdline = "echo < file";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testInputRedirInDoubleQuote() throws ShellException, AbstractApplicationException {
		expected = "< name";
		cmdline = "echo \"< name\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testInputRedirInSingleQuote() throws ShellException, AbstractApplicationException {
		expected = "< file name";
		cmdline = "echo '< file name'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOutputRedirInNoQuote() throws ShellException, AbstractApplicationException {
		expected = "";
		cmdline = "echo text > file";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOutputRedirInDoubleQuote() throws ShellException, AbstractApplicationException {
		expected = "text > name";
		cmdline = "echo text \"> name\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOutputRedirInSingleQuote() throws ShellException, AbstractApplicationException {
		expected = "text > file name";
		cmdline = "echo text '> file name'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testDoubleQuoteInSingleQuote() throws ShellException, AbstractApplicationException {
		expected = "double \"in\" single";
		cmdline = "echo 'double \"in\" single'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testSingleQuoteInDoubleQuote() throws ShellException, AbstractApplicationException {
		expected = "single 'in' double";
		cmdline = "echo \"single 'in' double\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	/**
	 * Creates the files required for this test suite.
	 */
	private void createFileForTest() throws IOException {
		PrintWriter writer;
		writer = new PrintWriter(TEST_DIR + OSUtil.SEP + "file");
		writer.print(FILE_CONTENT);
		writer.close();

		writer = new PrintWriter(TEST_DIR + OSUtil.SEP + "name");
		writer.print(NAME_CONTENT);
		writer.close();

		writer = new PrintWriter(TEST_DIR + OSUtil.SEP + "file name");
		writer.print(FILE_NAME_CONTENT);
		writer.close();

		writer = new PrintWriter(TEST_DIR + OSUtil.SEP + "`back quote`");
		writer.print(BQ_CONTENT);
		writer.close();
	}
}
