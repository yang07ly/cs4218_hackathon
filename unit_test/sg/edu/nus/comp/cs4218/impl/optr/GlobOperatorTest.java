package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
import sg.edu.nus.comp.cs4218.impl.optr.GlobOperator;

public class GlobOperatorTest {

	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system" + File.separator + "glob_test_system";

	private static final String FILE1 = "file1.txt";
	private static final String FILE2 = "file2.txt";
	private static final String FILE_W_SPACES = "file name with spaces.txt";
	private static final String FILE_EMPTY = "";
	private static final String FILE_SPACES = "   ";
	private static final String FILE_NONEXISTENT = "nonExistentFile*";
	private static final String FOLDER1 = "folder1";
	private static final String FOLDER2 = "folder2";
	private static final String FOLDER_W_SPACES = "folder name with spaces";

	private static final String FOLDER1_FILE1 = FOLDER1 + File.separator + FILE1;
	private static final String FOLDER1_FILE2 = FOLDER1 + File.separator + FILE2;
	private static final String FOLDER1_FOLDER_WS = FOLDER1 + File.separator + FOLDER_W_SPACES;
	private static final String FOLDER2_FILE1 = FOLDER2 + File.separator + FILE1;
	private static final String FOLDER2_FILE2 = FOLDER2 + File.separator + FILE2;
	private static final String ABS_FILE_W_SPACES = TEST_DIR + File.separator + FILE_W_SPACES;
	private static final String ABS_FILE1 = TEST_DIR + File.separator + FILE1;
	private static final String ABS_FILE2 = TEST_DIR + File.separator + FILE2;
	private static final String ABS_FOLDER1 = TEST_DIR + File.separator + FOLDER1;
	private static final String ABS_FOLDER2 = TEST_DIR + File.separator + FOLDER2;	

	private static final String STR_SPACE = " ";
	private static final String FILE_WILDCARD = "file*";

	private GlobOperator globOptr;
	private CommandString cmd, expected;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		globOptr = new GlobOperator();
		cmd = new CommandString();
		expected = new CommandString();
	}

	@Test
	public void testGlobEmpty() throws AbstractApplicationException, ShellException {
		expected = new CommandString(FILE_EMPTY);
		cmd = new CommandString(FILE_EMPTY);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobMutipleSpaces() throws AbstractApplicationException, ShellException {
		expected = new CommandString(FILE_SPACES);
		cmd = new CommandString(FILE_SPACES);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobExistingFile() throws AbstractApplicationException, ShellException {
		expected = new CommandString(FILE1);
		cmd = new CommandString(FILE1);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobNonExistingFile() throws AbstractApplicationException, ShellException {
		expected = new CommandString(FILE_NONEXISTENT);
		cmd = new CommandString(FILE_NONEXISTENT);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobEverythingRelative() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2);
		cmd = new CommandString("*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobEverythingAbsolute() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(ABS_FILE_W_SPACES, ABS_FILE1, ABS_FILE2, ABS_FOLDER1, ABS_FOLDER2);
		cmd = new CommandString(Environment.currentDirectory + File.separator + "*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobEverythingWithEmptyParent() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(ABS_FILE_W_SPACES, ABS_FILE1, ABS_FILE2, ABS_FOLDER1, ABS_FOLDER2);
		cmd = new CommandString(Environment.currentDirectory + File.separator + File.separator + "*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobAllFolders() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FOLDER1, FOLDER2);
		cmd = new CommandString("*/");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobAtFront() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2);
		cmd = new CommandString("*.txt");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobAtEnd() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2);
		cmd = new CommandString("f*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobAtMiddle() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2);
		cmd = new CommandString("f*.txt");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}
	
	@Test
	public void testGlobMutipleWildcard() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2);
		cmd = new CommandString("file*.*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobParentDirectory() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FOLDER1_FILE1, FOLDER2_FILE1);
		cmd = new CommandString("*" + File.separator + FILE1);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobMultipleFileInSingleFolder() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE1, FOLDER1);
		cmd = new CommandString("*1*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobMultipleFileInMutipleFolder() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FOLDER1_FILE1, FOLDER1_FILE2, FOLDER2_FILE1, FOLDER2_FILE2);
		cmd = new CommandString("*" + File.separator + FILE_WILDCARD);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobMutiplePath() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FOLDER1_FILE1, FOLDER1_FILE2, FOLDER1_FOLDER_WS, FOLDER2_FILE1, FOLDER2_FILE2);
		cmd = new CommandString(FOLDER1 + File.separator + "*" + STR_SPACE + FOLDER2 + File.separator + "*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobExistingAndNonExistingFile() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2, FILE_NONEXISTENT);
		expected.setCharEscapedRange(expected.length() - FILE_NONEXISTENT.length(), expected.length(), false);
		cmd = new CommandString(FILE_WILDCARD + STR_SPACE + FILE_NONEXISTENT);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobFileNameWithSpaces() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES);
		cmd = new CommandString("file name with*");
		cmd.setCharEscapedRange(0, 13, true);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobFileNameWithSpacesWithMutipleWildcard() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES);
		cmd = new CommandString("file name* with*");
		cmd.setCharEscapedRange(0, 9, true);
		cmd.setCharEscapedRange(10, 15, true);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testGlobEscapedWildcard() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_WILDCARD);
		cmd = new CommandString(FILE_WILDCARD);
		cmd.setCharEscapedRange(0, cmd.length(), true);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}
	
	@Test
	public void testGlobValidAndEscapedWildcard() throws AbstractApplicationException, ShellException {
		expected = new CommandString("**");
		expected.setCharEscaped(0, true);
		
		cmd = new CommandString("**");
		cmd.setCharEscaped(0, true);
		
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testInvalidGlobNull() throws AbstractApplicationException, ShellException {
		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Null Pointer Exception");
		globOptr.evaluate(null);
	}

	private CommandString getExpectedCmdStr(String... strings) {
		CommandString cmdStr = new CommandString();
		int startIndex = 0;
		for (int i = 0; i < strings.length; i++) {
			cmdStr.insertStringAt(startIndex, strings[i]);
			cmdStr.setCharEscapedRange(startIndex, cmdStr.length(), true);
			startIndex = cmdStr.length();

			if (i < strings.length - 1) {
				cmdStr.insertStringAt(startIndex, " ");
				cmdStr.setCharEscapedRange(startIndex, cmdStr.length(), false);
				startIndex = cmdStr.length();
			}
		}
		return cmdStr;
	}
}
