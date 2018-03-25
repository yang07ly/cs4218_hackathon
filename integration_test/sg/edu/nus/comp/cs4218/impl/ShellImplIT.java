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
	public void testParseAndEvalToEvalSeqOfCmdUsingSeqInNoQuotes() throws ShellException, AbstractApplicationException {
		expected = "seq1" + NEW_LINE + "seq2";
		cmdline = "echo seq1; echo seq2";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalSeqUsingSeqInDoubleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "double ; echo quote";
		cmdline = "echo double \"; echo quote\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalSeqUsingSeqInSingleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "single ; echo quote";
		cmdline = "echo single '; echo quote'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToEvalPipeOfCmdUsingPipeInNoQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "Pipe";
		cmdline = "echo pipe| sed s/pipe/Pipe/";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalPipeUsingPipeInDoubleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "double quote| sed s/pipe/Pipe/";
		cmdline = "echo double quote\"| sed s/pipe/Pipe/\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalPipeUsingPipeInSingleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "single quote| sed s/pipe/Pipe/";
		cmdline = "echo single quote'| sed s/pipe/Pipe/'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToEvalArgsOfCmdUsingArgsInNoQuotes()
			throws ShellException, AbstractApplicationException {
		expected = FILE_CONTENT + NEW_LINE + NAME_CONTENT;
		cmdline = "cat file name";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalArgsUsingArgsInDoubleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = FILE_NAME_CONTENT;
		cmdline = "cat \"file name\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalArgsUsingArgsInSingleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = FILE_NAME_CONTENT;
		cmdline = "cat 'file name'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToEvalGlobOfArgUsingGlobInNoQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "`back quote` file file name name";
		cmdline = "echo *";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalGlobUsingGlobInDoubleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "*";
		cmdline = "echo \"*\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalGlobUsingGlobInSingleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "*";
		cmdline = "echo '*'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToEvalCmdSubUsingCmdSubInNoQuotes()
			throws ShellException, AbstractApplicationException {
		expected = FILE_CONTENT;
		cmdline = "`echo cat` file";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalCmdSubUsingCmdSubInDoubleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = NAME_CONTENT;
		cmdline = "\"`echo cat`\" name";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalCmdSubUsingCmdSubInSingleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = BQ_CONTENT;
		cmdline = "cat '`back quote`'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToEvalInputRedirOfCmdUsingInputRedirInNoQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "";
		cmdline = "echo < file";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalInputRedirUsingInputRedirInDoubleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "< name";
		cmdline = "echo \"< name\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalInputRedirUsingInputRedirInSingleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "< file name";
		cmdline = "echo '< file name'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToEvalOutputRedirOfCmdUsingOutputRedirInNoQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "";
		cmdline = "echo text > file";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalOutputRedirUsingOutputRedirInDoubleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "text > name";
		cmdline = "echo text \"> name\"";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToNotEvalOutputRedirUsingOutputRedirInSingleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "text > file name";
		cmdline = "echo text '> file name'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToPrintDoubleQuotesUsingDoubleQuotesInSingleQuotes()
			throws ShellException, AbstractApplicationException {
		expected = "double \"in\" single";
		cmdline = "echo 'double \"in\" single'";

		shell.parseAndEvaluate(cmdline, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testParseAndEvalToPrintSingleQuotesUsingSingleQuotesInDoubleQuotes()
			throws ShellException, AbstractApplicationException {
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
