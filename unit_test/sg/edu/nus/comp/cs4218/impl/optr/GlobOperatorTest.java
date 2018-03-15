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

	GlobOperator globCmd;
	String[] expected, result;
	String exceptionMessage;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		globCmd = new GlobOperator(new ShellImplStub());
	}

	@Test
	public void testGlobEmpty() {
		expected = new String[] {FILE_EMPTY};
		try {
			result = globCmd.evaluate(FILE_EMPTY);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobMutipleSpaces() {
		expected = new String[] {FILE_SPACES};
		try {
			result = globCmd.evaluate(FILE_SPACES);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobExistingFile() {
		expected = new String[] {FILE1};
		try {
			result = globCmd.evaluate(FILE1);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobNonExistingFile() {
		expected = new String[] {FILE_NONEXISTENT};
		try {
			result = globCmd.evaluate(FILE_NONEXISTENT);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobEverythingRelative() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2};
		try {
			result = globCmd.evaluate("*");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobEverythingAbsolute() {
		expected = new String[] {ABS_FILE_W_SPACES, ABS_FILE1, ABS_FILE2, ABS_FOLDER1, ABS_FOLDER2};
		try {
			result = globCmd.evaluate(Environment.currentDirectory + File.separator + "*");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobAllFolders() {
		expected = new String[] {FOLDER1, FOLDER2};
		try {
			result = globCmd.evaluate("*/");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobAtFront() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		try {
			result = globCmd.evaluate("*.txt");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobAtEnd() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2};
		try {
			result = globCmd.evaluate("f*");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobAtMiddle() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, };
		try {
			result = globCmd.evaluate("f*.txt");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobParentDirectory() {
		expected = new String[] {FOLDER1_FILE1, FOLDER2_FILE1};
		try {
			result = globCmd.evaluate("*" + File.separator + FILE1);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobMultipleInSingleFolder() {
		expected = new String[] {FILE1, FOLDER1};
		try {
			result = globCmd.evaluate("*1*");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobMultipleInMutipleFolder() {
		expected = new String[] {FOLDER1_FILE1, FOLDER1_FILE2, FOLDER2_FILE1, FOLDER2_FILE2};
		try {
			result = globCmd.evaluate("*" + File.separator + "file*");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}
	
	@Test
	public void testGlobMutiplePath() {
		expected = new String[] {FOLDER1_FILE1, FOLDER1_FILE2, FOLDER2_FILE1, FOLDER2_FILE2};
		try {
			result = globCmd.evaluate(FOLDER1 + File.separator + "*", FOLDER2 + File.separator + "*");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}
	
	@Test
	public void testGlobExistingAndNonExistingFile() {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FILE_NONEXISTENT};
		try {
			result = globCmd.evaluate("file*", FILE_NONEXISTENT);
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}
	
	@Test
	public void testGlobRemoveQuote() {
		expected = new String[] {FILE_W_SPACES};
		try {
			result = globCmd.evaluate("\"file name with\"*");
		} catch (Exception e) {
			Assert.fail(UNEXPECTED + e.getMessage());
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testInvalidGlobNull() throws AbstractApplicationException, ShellException {
		thrown.expect(ShellException.class);
        thrown.expectMessage("shell: Null Pointer Exception");

		result = globCmd.evaluate(new String[] {null});
	}
}
