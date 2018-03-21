package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class callCommandIT {
	private final String line = "\n";
	private CallCommand callCom;
	private String cmdLine, expected;
	private ByteArrayOutputStream output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		expected = cmdLine = "";
		output = new ByteArrayOutputStream();
	}

	@Test
	public void testEmptyCmd() throws ShellException, AbstractApplicationException {
		cmdLine = "";
		callCom = new CallCommand(new ShellImpl(), cmdLine);
		expected = "shell: : Invalid app";

		thrown.expect(ShellException.class);
		thrown.expectMessage(expected);

		callCom.parse();
		callCom.evaluate(System.in, output);
	}

	@Test
	public void testAppNameWithOneSpace() throws ShellException, AbstractApplicationException {
		cmdLine = " echo abc";
		expected = "abc";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testAppNameWithMultipleSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = "    echo abc";
		expected = "abc";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testAppNameWithOneTab() throws ShellException, AbstractApplicationException {
		cmdLine = "\techo abc";
		expected = "abc";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testAppNameWithMultipleTabs() throws ShellException, AbstractApplicationException {
		cmdLine = "\t\t\techo abc";
		expected = "abc";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testAppNameWithMultipleTabsSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "  \t\t   \t  echo abc";
		expected = "abc";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgs() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc def";
		expected = "abc def";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsWithMultipleSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "echo  abc     def";
		expected = "abc def";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsWithOneTab() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\tabc\tdef";
		expected = "abc def";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsWithMultipleTabs() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\t\tabc\t\t\tdef";
		expected = "abc def";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsWithMultipleTabsSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\t    \tabc   \t\t\t     def";
		expected = "abc def";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateEcho() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc";
		expected = "abc";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCat() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "cat_test_system";
		cmdLine = "cat file1.txt";
		expected = "asdf";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateExit() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "cat_test_system";
		cmdLine = "exit";

		Thread thread1 = new Thread() {
			public void run() {
				callCom = new CallCommand(new ShellImpl(), cmdLine);
				try {
					callCom.parse();
					callCom.evaluate(System.in, output);
				} catch (AbstractApplicationException e) {
				} catch (ShellException e) {
				}
			}
		};
		assertTrue(!thread1.isAlive());
	}

	@Test
	public void testEvaluateLs() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "ls_test_system";
		cmdLine = "ls";
		expected = ".hiddenFile1.txt  'file name with space.txt'  file1.txt  file2.txt  'folder name with space'  folder1  folder2";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateMkDir() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "mkdir_test_system";
		cmdLine = "mkdir folder1";
		expected = "";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		Path dirPath = Paths.get(Environment.currentDirectory).resolve("folder1");
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
		Files.delete(dirPath);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluatePaste() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "paste_test_system";
		cmdLine = "paste file1.txt file2.txt";
		expected = "asdfgh\tqwerty";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateDiff() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "diff_test_system";
		cmdLine = "diff file1.txt file2.txt";
		expected = "< line2" + System.getProperty("line.separator") + "> line6";

		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCd() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "cd_test_system";
		cmdLine = "cd folder1";
		String directory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "cd_test_system" + File.separator + "folder1";
		expected = "";
		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(directory, Environment.currentDirectory);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateSplit() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "split_test_system";
		cmdLine = "split file1.txt -l 6";
		
		expected = "";
		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		BufferedReader reader = new BufferedReader(new FileReader(new File(Environment.currentDirectory + File.separator + "xaa")));
		assertEquals("asdfgh", reader.readLine());
		assertEquals("qwerty", reader.readLine());
		assertEquals("zxcvbn", reader.readLine());
		reader.close();
		Files.delete(Paths.get(Environment.currentDirectory + File.separator + "xaa"));
		
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCmp() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "cmp_test_system";
		cmdLine = "cmp file1.txt file2.txt -csl";
		
		expected = "Files differ";
		callCom = new CallCommand(new ShellImpl(), cmdLine);
		callCom.parse();
		callCom.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}
}
