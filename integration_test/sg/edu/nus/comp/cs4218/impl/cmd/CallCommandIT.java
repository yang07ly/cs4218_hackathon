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

public class CallCommandIT {
	private static final String QWERTY = "qwerty";
	private static final String ABC = "abc";
	private static final String FILE1_TXT = "file1.txt";
	private static final String OUTPUT_STREAM = "outputStream";
	private static final String FILEFILE = "filefile";
	private static final String NEWLINE = System.lineSeparator();
	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system";
	private static final String LS_TEST_DIR = TEST_DIR + File.separator + "ls_test_system";
	private static final String CAT_TEST_DIR = TEST_DIR + File.separator + "cat_test_system";
	private static final String MKDIR_TEST_DIR = TEST_DIR + File.separator + "mkdir_test_system";
	private static final String PASTE_TEST_DIR = TEST_DIR + File.separator + "paste_test_system";
	private static final String DIFF_TEST_DIR = TEST_DIR + File.separator + "diff_test_system";
	private static final String CD_TEST_DIR = TEST_DIR + File.separator + "cd_test_system";
	private static final String SPLIT_TEST_DIR = TEST_DIR + File.separator + "split_test_system";
	private static final String CMP_TEST_DIR = TEST_DIR + File.separator + "cmp_test_system";
	private static final String GREP_TEST_DIR = TEST_DIR + File.separator + "grep_test_system";
	private static final String SED_TEST_DIR = TEST_DIR + File.separator + "sed_test_system";
	private static final String GLOB_TEST_DIR = TEST_DIR + File.separator + "glob_test_system";
	private static final String IO_TEST_DIR = TEST_DIR + File.separator + "ioRedir_test_system";

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
	public void testInvalidEmptyCmd() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("");
		callCmd = new CallCommand(new ShellImpl(), cmdLine);

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: : Invalid app");

		callCmd.parse();
		callCmd.evaluate(System.in, output);
	}

	@Test
	public void testAppNameWithOneSpace() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(" echo abc");
		expected = ABC;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testAppNameWithMultipleSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("    echo abc");
		expected = ABC;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgs() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc def");
		expected = "abc def";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsWithMultipleSpace() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo  abc     def");
		expected = "abc def";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateEcho() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc");
		expected = ABC;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCat() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("cat file1.txt");
		expected = "asdf";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateExit() throws ShellException, AbstractApplicationException {
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
	public void testEvaluateLs() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = LS_TEST_DIR;
		cmdLine = new CommandString("ls");
		expected = "'file name with space.txt'  file1.txt  file2.txt  'folder name with space'  folder1  folder2";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateMkDir() throws ShellException, AbstractApplicationException, IOException {
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
	public void testEvaluatePaste() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = PASTE_TEST_DIR;
		cmdLine = new CommandString("paste file1.txt file2.txt");
		expected = "asdfgh\tqwerty";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateDiff() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = DIFF_TEST_DIR;
		cmdLine = new CommandString("diff file1.txt file2.txt");
		expected = "< line2" + System.getProperty("line.separator") + "> line6";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCd() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CD_TEST_DIR;
		cmdLine = new CommandString("cd folder1");
		String directory = CD_TEST_DIR + File.separator + "folder1";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(directory, Environment.currentDirectory);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateSplit() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = SPLIT_TEST_DIR;
		cmdLine = new CommandString("split file1.txt -l 6");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Environment.currentDirectory + File.separator + "xaa")));
		assertEquals("asdfgh", reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		assertEquals("zxcvbn", reader.readLine());
		reader.close();
		Files.delete(Paths.get(Environment.currentDirectory + File.separator + "xaa"));

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCmp() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CMP_TEST_DIR;
		cmdLine = new CommandString("cmp file1.txt file2.txt -csl");

		expected = "Files differ";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateGrep() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = GREP_TEST_DIR;
		cmdLine = new CommandString("grep file file1.txt");

		expected = "line 1: This file is named 'file1'.\n" + "line 2: It is a Text file.";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateSed() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = SED_TEST_DIR;
		cmdLine = new CommandString("sed s/test/replaced/ sedTestFile1.txt");

		expected = "This is Sed Test File 1.\n" + "1. replaced\n" + "2. replaced test\n" + "3. replaced test test\n"
				+ "4. replaced test test test\n" + "5. replacedestestest\n" + "6. replacedestestestestest";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testInvalidApp() throws ShellException, AbstractApplicationException, IOException {
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
	public void testEvaluateWithGlob() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo *");
		expected = "cat_test_system cd_test_system cmd_test_system cmp_test_system "
				+ "diff_test_system glob_test_system grep_test_system ioRedir_test_system "
				+ "ls_test_system mkdir_test_system paste_test_system quote_test_system sed_test_system " + "split_test_system";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateWithCmdSub() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo `echo test command substitution`");
		expected = "test command substitution";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testGlob() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = GLOB_TEST_DIR;
		cmdLine = new CommandString("echo file*");
		expected = "file name with spaces.txt file1.txt file2.txt";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testInputStream() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat < file.txt");
		expected = FILEFILE;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testOutputStream() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("echo abc > file1.txt");
		expected = "";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertTrue(Files.exists(Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT)));
		OutputStream outputStream = (OutputStream) Whitebox.getInternalState(callCmd, OUTPUT_STREAM);
		outputStream.close();
		Path filePath = Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT);

		assertEquals(ABC, new String(Files.readAllBytes(filePath)));
		assertEquals(expected, output.toString());
		Files.delete(filePath);
	}

	@Test
	public void testCmdSub() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat `echo file.txt`");
		expected = FILEFILE;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testInputStreamWithOutputStream() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat < file.txt > file1.txt");
		expected = "";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertTrue(Files.exists(Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT)));
		OutputStream outputStream = (OutputStream) Whitebox.getInternalState(callCmd, OUTPUT_STREAM);
		outputStream.close();
		Path filePath = Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT);

		assertEquals(FILEFILE, new String(Files.readAllBytes(filePath)));
		assertEquals(expected, output.toString());
		Files.delete(filePath);
	}

	@Test
	public void testInputStreamWithGlob() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat < f*");
		expected = FILEFILE;

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testInputStreamWithCmdSub() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat `echo file.txt` < file.txt -");
		expected = "filefile" + NEWLINE + "filefile";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testOutputStreamWithGlob() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat file.txt > glob*");
		expected = "";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		Path filePath = Paths.get(Environment.currentDirectory + File.separator + "globOut.txt");
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
	public void testOutputStreamWithCmdSub() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = IO_TEST_DIR;
		cmdLine = new CommandString("cat file.txt > `echo globOut.txt`");
		cmdLine.setCharEscapedRange(16, 32, true);
		expected = "";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		Path filePath = Paths.get(Environment.currentDirectory + File.separator + "globOut.txt");
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
	public void testCmdSubWithGlob() throws ShellException, AbstractApplicationException, IOException {
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
	public void testEscapedChars() throws ShellException, AbstractApplicationException, IOException {
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
	public void testInvalidCmdSub() throws ShellException, AbstractApplicationException, IOException {
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
	public void testInvalidInputStream() throws ShellException, AbstractApplicationException, IOException {
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
	public void testInvalidOutputStream() throws ShellException, AbstractApplicationException, IOException {
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
	public void testInvalidAppError() throws ShellException, AbstractApplicationException, IOException {
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
