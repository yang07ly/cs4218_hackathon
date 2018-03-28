package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.util.reflection.Whitebox;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.CatException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class CallCommandIT {
	private static final String QWERTY = "qwerty";
	private static final String ABC = "abc";
	private static final String TAB = "\t";
	private static final String FILE1_TXT = "file1.txt";
	private static final String OUTPUT_STREAM = "outputStream";
	private static final String FILEFILE = "filefile";
	private static final String NEWLINE = System.lineSeparator();
	private static final String TEST_DIR = System.getProperty("user.dir") + OSUtil.SEP + "test_system";
	private static final String LS_TEST_DIR = TEST_DIR + OSUtil.SEP + "ls_test_system";
	private static final String CAT_TEST_DIR = TEST_DIR + OSUtil.SEP + "cat_test_system";
	private static final String MKDIR_TEST_DIR = TEST_DIR + OSUtil.SEP + "mkdir_test_system";
	private static final String PASTE_TEST_DIR = TEST_DIR + OSUtil.SEP + "paste_test_system";
	private static final String DIFF_TEST_DIR = TEST_DIR + OSUtil.SEP + "diff_test_system";
	private static final String CD_TEST_DIR = TEST_DIR + OSUtil.SEP + "cd_test_system";
	private static final String SPLIT_TEST_DIR = TEST_DIR + OSUtil.SEP + "split_test_system";
	private static final String CMP_TEST_DIR = TEST_DIR + OSUtil.SEP + "cmp_test_system";
	private static final String GREP_TEST_DIR = TEST_DIR + OSUtil.SEP + "grep_test_system";
	private static final String SED_TEST_DIR = TEST_DIR + OSUtil.SEP + "sed_test_system";
	private static final String GLOB_TEST_DIR = TEST_DIR + OSUtil.SEP + "glob_test_system";
	private static final String IO_TEST_DIR = TEST_DIR + OSUtil.SEP + "ioRedir_test_system";

	private CallCommand callCmd;
	private CommandString cmdLine;
	private String expected;
	private ByteArrayOutputStream output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		cmdLine = new CommandString();
		expected = "";
		output = new ByteArrayOutputStream();
	}

	@Test
	public void testEvaluateToThrowExpUsingUsingEmptyCmdString() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("");
		callCmd = new CallCommand(new ShellImpl(), cmdLine);

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: : Invalid app");

		callCmd.parse();
		callCmd.evaluate(System.in, output);
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingSpaceBeforeAppName()
			throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(" echo abc");
		expected = ABC;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingMultiSpacesBeforeAppName()
			throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("    echo abc");
		expected = ABC;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingMultiArgs() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc def");
		expected = "abc def";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingMultiSpaceBtwArgs() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo  abc     def");
		expected = "abc def";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingEcho() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc");
		expected = ABC;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingCat() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("cat file1.txt");
		expected = "asdf";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingExit() throws ShellException, AbstractApplicationException {
		Thread thread1 = new Thread() {
			public void run() {
				CallCommand newCallCmd = new CallCommand(new ShellImpl(), new CommandString("exit"));
				try {
					newCallCmd.parse();
					newCallCmd.evaluate(System.in, new ByteArrayOutputStream());
				} catch (AbstractApplicationException | ShellException e) {
				}
			}
		};
		assertTrue(!thread1.isAlive());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingLs() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = LS_TEST_DIR;
		cmdLine = new CommandString("ls");
		expected = "'file name with space.txt'" + TAB + "file1.txt" + TAB + "file2.txt" + TAB
				+ "'folder name with space'" + TAB + "folder1" + TAB + "folder2";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingMkDir()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = MKDIR_TEST_DIR;
		cmdLine = new CommandString("mkdir folder1");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		Path dirPath = Paths.get(Environment.currentDirectory).resolve("folder1");
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
		Files.delete(dirPath);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingPaste()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = PASTE_TEST_DIR;
		cmdLine = new CommandString("paste file1.txt file2.txt");
		expected = "asdfgh\tqwerty";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingDiff() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = DIFF_TEST_DIR;
		cmdLine = new CommandString("diff file1.txt file2.txt");
		expected = "< line2" + System.getProperty("line.separator") + "> line6";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingCd() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CD_TEST_DIR;
		cmdLine = new CommandString("cd folder1");
		String directory = CD_TEST_DIR + OSUtil.SEP + "folder1";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(directory, Environment.currentDirectory);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingSplit()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = SPLIT_TEST_DIR;
		cmdLine = new CommandString("split file1.txt -l 6");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Environment.currentDirectory + OSUtil.SEP + "xaa")));
		assertEquals("asdfgh", reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		assertEquals("zxcvbn", reader.readLine());
		reader.close();
		Files.delete(Paths.get(Environment.currentDirectory + OSUtil.SEP + "xaa"));

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingCmp() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CMP_TEST_DIR;
		cmdLine = new CommandString("cmp file1.txt file2.txt -csl");

		expected = "Files differ";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingGrep() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = GREP_TEST_DIR;
		cmdLine = new CommandString("grep file file1.txt");

		expected = "line 1: This file is named 'file1'." + NEWLINE + "line 2: It is a Text file.";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingSed() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = SED_TEST_DIR;
		cmdLine = new CommandString("sed s/test/replaced/ sedTestFile1.txt");

		expected = "This is Sed Test File 1." + NEWLINE + "1. replaced" + NEWLINE + "2. replaced test" + NEWLINE
				+ "3. replaced test test" + NEWLINE + "4. replaced test test test" + NEWLINE + "5. replacedestestest"
				+ NEWLINE + "6. replacedestestestestest";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToThrowExpUsingInvalidApp()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("eco abc");
		expected = QWERTY;

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: eco: Invalid app");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingGlob() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo *");
		expected = "cat_test_system cd_test_system cmd_test_system cmp_test_system "
				+ "diff_test_system fileutil_test_system glob_test_system grep_test_system ioRedir_test_system "
				+ "ls_test_system mkdir_test_system paste_test_system quote_test_system sed_test_system "
				+ "split_test_system";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingCmdSub() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo `echo test command substitution`");
		expected = "test command substitution";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingGlobWithText() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = GLOB_TEST_DIR;
		cmdLine = new CommandString("echo file*");
		expected = "file name with spaces.txt file1.txt file2.txt";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingInputStream() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat < file.txt");
		expected = FILEFILE;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingOutputStream()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("echo abc > file1.txt");
		expected = "";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertTrue(Files.exists(Paths.get(Environment.currentDirectory + OSUtil.SEP + FILE1_TXT)));
		OutputStream outputStream = (OutputStream) Whitebox.getInternalState(callCmd, OUTPUT_STREAM);
		outputStream.close();
		Path filePath = Paths.get(Environment.currentDirectory + OSUtil.SEP + FILE1_TXT);

		assertEquals(ABC, new String(Files.readAllBytes(filePath)));
		assertEquals(expected, output.toString());
		Files.delete(filePath);
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingCmdSub2()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat `echo file.txt`");
		expected = FILEFILE;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingInputStreamWithOutputStream()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat < file.txt > file1.txt");
		expected = "";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertTrue(Files.exists(Paths.get(Environment.currentDirectory + OSUtil.SEP + FILE1_TXT)));
		OutputStream outputStream = (OutputStream) Whitebox.getInternalState(callCmd, OUTPUT_STREAM);
		outputStream.close();
		Path filePath = Paths.get(Environment.currentDirectory + OSUtil.SEP + FILE1_TXT);

		assertEquals(FILEFILE, new String(Files.readAllBytes(filePath)));
		assertEquals(expected, output.toString());
		Files.delete(filePath);
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingInputStreamWithGlob()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat < f*");
		expected = FILEFILE;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingInputStreamWithCmdSub()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat `echo file.txt` < file.txt -");
		expected = "filefile" + NEWLINE + "filefile";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingOutputStreamWithGlob()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat file.txt > glob*");
		expected = "";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		Path filePath = Paths.get(Environment.currentDirectory + OSUtil.SEP + "globOut.txt");
		assertTrue(Files.exists(filePath));

		assertEquals(FILEFILE, new String(Files.readAllBytes(filePath)));
		assertEquals(expected, output.toString());
		OutputStream outputStream = (OutputStream) Whitebox.getInternalState(callCmd, OUTPUT_STREAM);
		outputStream.close();
		outputStream = new FileOutputStream(filePath.toFile());
		outputStream.write("".getBytes());
		outputStream.close();
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingOutputStreamWithCmdSub()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat file.txt > `echo globOut.txt`");
		cmdLine.setCharEscapedRange(16, 32, true);
		expected = "";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		Path filePath = Paths.get(Environment.currentDirectory + OSUtil.SEP + "globOut.txt");
		assertTrue(Files.exists(filePath));

		assertEquals(FILEFILE, new String(Files.readAllBytes(filePath)));
		assertEquals(expected, output.toString());
		OutputStream outputStream = (OutputStream) Whitebox.getInternalState(callCmd, OUTPUT_STREAM);
		outputStream.close();
		outputStream = new FileOutputStream(filePath.toFile());
		outputStream.write("".getBytes());
		outputStream.close();
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingCmdSubWithGlob()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("echo `cat containsStar.txt`");
		cmdLine.setCharEscapedRange(6, 26, true);
		expected = "file.txt";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToRunCorrectlyUsingEscapedChars()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("cat file with space.txt");
		cmdLine.setCharEscapedRange(4, 23, true);
		expected = QWERTY;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateToThrowExpUsingInvalidCmdSub()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("echo `cat file.txt");
		expected = QWERTY;

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Back Quotes not closed");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
	}

	@Test
	public void testEvaluateToThrowExpUsingInvalidInputStream()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("cat < asdf");
		expected = QWERTY;

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: asdf: No such file or directory");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
	}

	@Test
	public void testEvaluateToThrowExpUsingInvalidOutputStream()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("cat > abc > asdf");
		expected = QWERTY;

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 outputstream can be specified");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
	}

	@Test
	public void testEvaluateToThrowExpUsingInvalidAppError()
			throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("cat  ");
		cmdLine.setCharEscapedRange(4, 5, true);

		thrown.expect(CatException.class);
		thrown.expectMessage("cat:  : No such file or directory");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
	}
}
