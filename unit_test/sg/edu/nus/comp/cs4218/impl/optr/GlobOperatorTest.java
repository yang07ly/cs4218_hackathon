package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class GlobOperatorTest {

	private static final String TEST_DIR = System.getProperty("user.dir") + OSUtil.SEP + "test_system" + OSUtil.SEP
			+ "glob_test_system";

	private static final String FILE1 = "file1.txt";
	private static final String FILE2 = "file2.txt";
	private static final String FILE_W_SPACES = "file name with spaces.txt";
	private static final String FILE_EMPTY = "";
	private static final String FILE_SPACES = "   ";
	private static final String FILE_NONEXISTENT = "nonExistentFile*";
	private static final String FOLDER1 = "folder1";
	private static final String FOLDER2 = "folder2";
	private static final String FOLDER_W_SPACES = "folder name with spaces";

	private static final String FOLDER1_FILE1 = FOLDER1 + OSUtil.SEP + FILE1;
	private static final String FOLDER1_FILE2 = FOLDER1 + OSUtil.SEP + FILE2;
	private static final String FOLDER1_FOLDER_WS = FOLDER1 + OSUtil.SEP + FOLDER_W_SPACES;
	private static final String FOLDER2_FILE1 = FOLDER2 + OSUtil.SEP + FILE1;
	private static final String FOLDER2_FILE2 = FOLDER2 + OSUtil.SEP + FILE2;
	private static final String ABS_FILE_W_SPACES = TEST_DIR + OSUtil.SEP + FILE_W_SPACES;
	private static final String ABS_FILE1 = TEST_DIR + OSUtil.SEP + FILE1;
	private static final String ABS_FILE2 = TEST_DIR + OSUtil.SEP + FILE2;
	private static final String ABS_FOLDER1 = TEST_DIR + OSUtil.SEP + FOLDER1;
	private static final String ABS_FOLDER2 = TEST_DIR + OSUtil.SEP + FOLDER2;

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
	public void testEvalToDoNothingUsingEmptyStr() throws AbstractApplicationException, ShellException {
		expected = new CommandString(FILE_EMPTY);
		cmd = new CommandString(FILE_EMPTY);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToDoNothingUsingSpacesStr() throws AbstractApplicationException, ShellException {
		expected = new CommandString(FILE_SPACES);
		cmd = new CommandString(FILE_SPACES);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToDoNothingUsingExistingFile() throws AbstractApplicationException, ShellException {
		expected = new CommandString(FILE1);
		cmd = new CommandString(FILE1);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToDoNothingUsingNonExistentFile() throws AbstractApplicationException, ShellException {
		expected = new CommandString(FILE_NONEXISTENT);
		cmd = new CommandString(FILE_NONEXISTENT);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobEverythingInCurDirUsingWildcardOnly()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2);
		cmd = new CommandString("*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobEverythingInDirUsingAbsDirWithWildcard()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(ABS_FILE_W_SPACES, ABS_FILE1, ABS_FILE2, ABS_FOLDER1, ABS_FOLDER2);
		cmd = new CommandString(Environment.currentDirectory + OSUtil.SEP + "*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToIgnoreEmptyDirBtwFileSepUsingPathWithEmptyDirBtwFileSep()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(ABS_FILE_W_SPACES, ABS_FILE1, ABS_FILE2, ABS_FOLDER1, ABS_FOLDER2);
		cmd = new CommandString(Environment.currentDirectory + OSUtil.SEP + OSUtil.SEP + "*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobDirOnlySepUsingPathEndWithFileSep() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FOLDER1, FOLDER2);
		cmd = new CommandString("*/");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobFilesUsingWildcardAtFront() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2);
		cmd = new CommandString("*.txt");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobFilesUsingWildcardAtEnd() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2);
		cmd = new CommandString("f*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobFilesUsingWildcardBtwTest() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2);
		cmd = new CommandString("f*.txt");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobFilesUsingMultiWildcardWithTest() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2);
		cmd = new CommandString("file*.*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobFilesInDiffDirsUsingWildcardInParentDir()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FOLDER1_FILE1, FOLDER2_FILE1);
		cmd = new CommandString("*" + OSUtil.SEP + FILE1);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobBothFileAndDirUsingWildCardMatchBothFileAndDirName()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE1, FOLDER1);
		cmd = new CommandString("*1*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobAllFilesInMultiDirUsingWildCardInParentDirAndFileName()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FOLDER1_FILE1, FOLDER1_FILE2, FOLDER2_FILE1, FOLDER2_FILE2);
		cmd = new CommandString("*" + OSUtil.SEP + FILE_WILDCARD);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobMultiPathsUsingWildCardInTwoPath() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FOLDER1_FILE1, FOLDER1_FILE2, FOLDER1_FOLDER_WS, FOLDER2_FILE1, FOLDER2_FILE2);
		cmd = new CommandString(FOLDER1 + OSUtil.SEP + "*" + STR_SPACE + FOLDER2 + OSUtil.SEP + "*");
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobMultiPathsUsingExistingAndNonExistentFile()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES, FILE1, FILE2, FILE_NONEXISTENT);
		expected.setCharEscapedRange(expected.length() - FILE_NONEXISTENT.length(), expected.length(), false);
		cmd = new CommandString(FILE_WILDCARD + STR_SPACE + FILE_NONEXISTENT);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobFileNameWithSpaceUsingWildcardWithEscapedSpaces()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES);
		cmd = new CommandString("file name with*");
		cmd.setCharEscapedRange(0, 13, true);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobFileNameWithSpaceUsingMultiWildcardWithEscapedSpaces()
			throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_W_SPACES);
		cmd = new CommandString("file name* with*");
		cmd.setCharEscapedRange(0, 9, true);
		cmd.setCharEscapedRange(10, 15, true);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToDoNothingUsingEscapedWildcard() throws AbstractApplicationException, ShellException {
		expected = getExpectedCmdStr(FILE_WILDCARD);
		cmd = new CommandString(FILE_WILDCARD);
		cmd.setCharEscapedRange(0, cmd.length(), true);
		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToGlobNothingUsingEscapedAndValidWildcard()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("**");
		expected.setCharEscaped(0, true);

		cmd = new CommandString("**");
		cmd.setCharEscaped(0, true);

		globOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToThrowsShellExpUsingNullStr() throws AbstractApplicationException, ShellException {
		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Null Pointer Exception");
		globOptr.evaluate(null);
	}

	/**
	 * Return the CommandString array by creating new CommandString by appending
	 * each inputed String with its characters state set to true, separated by a
	 * space with a false state.
	 * 
	 * @param strings
	 *            String of the new CommandString to be added to the array.
	 * @return CommandString with the appended inputed Strings.
	 */
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
