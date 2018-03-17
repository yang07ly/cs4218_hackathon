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
import sg.edu.nus.comp.cs4218.impl.ShellStub;
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
	
	private static final String FOLDER1_FILE1 = FOLDER1 + File.separator + FILE1;
	private static final String FOLDER1_FILE2 = FOLDER1 + File.separator + FILE2;
	private static final String FOLDER2_FILE1 = FOLDER2 + File.separator + FILE1;
	private static final String FOLDER2_FILE2 = FOLDER2 + File.separator + FILE2;
	private static final String ABS_FILE_W_SPACES = TEST_DIR + File.separator + FILE_W_SPACES;
	private static final String ABS_FILE1 = TEST_DIR + File.separator + FILE1;
	private static final String ABS_FILE2 = TEST_DIR + File.separator + FILE2;
	private static final String ABS_FOLDER1 = TEST_DIR + File.separator + FOLDER1;
	private static final String ABS_FOLDER2 = TEST_DIR + File.separator + FOLDER2;	

	private GlobOperator globOptr;
	private String[] input, output, expected;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		globOptr = new GlobOperator(new ShellStub());
		input = output = expected = new String[0];
	}

	@Test
	public void testGlobEmpty() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_EMPTY};
		input = new String[] {FILE_EMPTY};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobMutipleSpaces() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_SPACES};
		input = new String[] {FILE_SPACES};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobExistingFile() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE1};
		input = new String[] {FILE1};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobNonExistingFile() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_NONEXISTENT};
		input = new String[] {FILE_NONEXISTENT};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobEverythingRelative() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2};
		input = new String[] {"*"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobEverythingAbsolute() throws AbstractApplicationException, ShellException {
		expected = new String[] {ABS_FILE_W_SPACES, ABS_FILE1, ABS_FILE2, ABS_FOLDER1, ABS_FOLDER2};
		input = new String[] {Environment.currentDirectory + File.separator + "*"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobEverythingWithEmptyParent() throws AbstractApplicationException, ShellException {
		expected = new String[] {ABS_FILE_W_SPACES, ABS_FILE1, ABS_FILE2, ABS_FOLDER1, ABS_FOLDER2};
		input = new String[] {Environment.currentDirectory + File.separator + File.separator + "*"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobAllFolders() throws AbstractApplicationException, ShellException {
		expected = new String[] {FOLDER1, FOLDER2};
		input = new String[] {"*/"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobAtFront() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {"*.txt"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobAtEnd() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2};
		input = new String[] {"f*"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobAtMiddle() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, };
		input = new String[] {"f*.txt"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobParentDirectory() throws AbstractApplicationException, ShellException {
		expected = new String[] {FOLDER1_FILE1, FOLDER2_FILE1};
		input = new String[] {"*" + File.separator + FILE1};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobMultipleInSingleFolder() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE1, FOLDER1};
		input = new String[] {"*1*"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobMultipleInMutipleFolder() throws AbstractApplicationException, ShellException {
		expected = new String[] {FOLDER1_FILE1, FOLDER1_FILE2, FOLDER2_FILE1, FOLDER2_FILE2};
		input = new String[] {"*" + File.separator + "file*"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobMutiplePath() throws AbstractApplicationException, ShellException {
		expected = new String[] {FOLDER1_FILE1, FOLDER1_FILE2, FOLDER2_FILE1, FOLDER2_FILE2};
		input = new String[] {FOLDER1 + File.separator + "*", FOLDER2 + File.separator + "*"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobExistingAndNonExistingFile() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FILE_NONEXISTENT};
		input = new String[] {"file*", FILE_NONEXISTENT};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobRemoveQuote() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES};
		input = new String[] {"\"file name with\"*"};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testInvalidGlobNull() throws AbstractApplicationException, ShellException {
		input = new String[] {null};
		
		thrown.expect(ShellException.class);
        thrown.expectMessage("shell: Null Pointer Exception");

		globOptr.evaluate(input);
	}
}
