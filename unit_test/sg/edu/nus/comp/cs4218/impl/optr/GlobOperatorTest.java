package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImplStub;
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
	
	private static final String UNEXPECTED = "Unexpected Exception: ";

	private GlobOperator globCmd;
	private String[] input, output, expected;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		globCmd = new GlobOperator(new ShellImplStub());
		input = output = expected = new String[0];
	}

	@Test
	public void testGlobEmpty() {
		expected = new String[] {FILE_EMPTY};
		input = new String[] {FILE_EMPTY};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobMutipleSpaces() {
		expected = new String[] {FILE_SPACES};
		input = new String[] {FILE_SPACES};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobExistingFile() {
		expected = new String[] {FILE1};
		input = new String[] {FILE1};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobNonExistingFile() {
		expected = new String[] {FILE_NONEXISTENT};
		input = new String[] {FILE_NONEXISTENT};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobEverythingRelative() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2};
		input = new String[] {"*"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobEverythingAbsolute() {
		expected = new String[] {ABS_FILE_W_SPACES, ABS_FILE1, ABS_FILE2, ABS_FOLDER1, ABS_FOLDER2};
		input = new String[] {Environment.currentDirectory + File.separator + "*"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobAllFolders() {
		expected = new String[] {FOLDER1, FOLDER2};
		input = new String[] {"*/"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobAtFront() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {"*.txt"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobAtEnd() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2};
		input = new String[] {"f*"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobAtMiddle() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, };
		input = new String[] {"f*.txt"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobParentDirectory() {
		expected = new String[] {FOLDER1_FILE1, FOLDER2_FILE1};
		input = new String[] {"*" + File.separator + FILE1};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobMultipleInSingleFolder() {
		expected = new String[] {FILE1, FOLDER1};
		input = new String[] {"*1*"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testGlobMultipleInMutipleFolder() {
		expected = new String[] {FOLDER1_FILE1, FOLDER1_FILE2, FOLDER2_FILE1, FOLDER2_FILE2};
		input = new String[] {"*" + File.separator + "file*"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobMutiplePath() {
		expected = new String[] {FOLDER1_FILE1, FOLDER1_FILE2, FOLDER2_FILE1, FOLDER2_FILE2};
		input = new String[] {FOLDER1 + File.separator + "*", FOLDER2 + File.separator + "*"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobExistingAndNonExistingFile() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FILE_NONEXISTENT};
		input = new String[] {"file*", FILE_NONEXISTENT};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobRemoveQuote() {
		expected = new String[] {FILE_W_SPACES};
		input = new String[] {"\"file name with\"*"};
		try {
			output = globCmd.evaluate(input);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, output);
	}

	@Test
	public void testInvalidGlobNull() throws AbstractApplicationException, ShellException {
		input = new String[] {null};
		
		thrown.expect(ShellException.class);
        thrown.expectMessage("shell: Null Pointer Exception");

		output = globCmd.evaluate(input);
	}
}
