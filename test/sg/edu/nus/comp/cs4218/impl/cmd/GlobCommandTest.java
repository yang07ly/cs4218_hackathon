package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;

public class GlobCommandTest {

	public static final String FILE_1 = "file1.txt";
	public static final String FILE_2 = "file2.txt";
	public static final String FILE_EMPTY = "";
	public static final String FILE_SPACES = "   ";
	public static final String FILE_NONEXISTENT = "nonExistentFile";
	public static final String FOLDER_1 = "folder1";
	public static final String FOLDER_2 = "folder2";

	GlobCommand globCmd;
	String[] expected, result;
	String exceptionMessage;

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator + "glob_test_system";
		globCmd = new GlobCommand();
	}

	@Test
	public void testGlobEmpty() {
		expected = new String[] {FILE_EMPTY};
		try {
			result = globCmd.globFileAndDirectory(FILE_EMPTY);
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobMutipleSpaces() {
		expected = new String[] {FILE_SPACES};
		try {
			result = globCmd.globFileAndDirectory(FILE_SPACES);
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobExistingFile() {
		expected = new String[] {FILE_1};
		try {
			result = globCmd.globFileAndDirectory(FILE_1);
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobNonExistingFile() {
		expected = new String[] {FILE_NONEXISTENT};
		try {
			result = globCmd.globFileAndDirectory(FILE_NONEXISTENT);
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobEverythingRelative() {
		expected = new String[] {FOLDER_1, FOLDER_2, FILE_1, FILE_2};
		try {
			result = globCmd.globFileAndDirectory("*");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobEverythingAbsolute() {
		expected = new String[] {Environment.currentDirectory + File.separator + FOLDER_1, 
				Environment.currentDirectory + File.separator + FOLDER_2, 
				Environment.currentDirectory + File.separator + FILE_1, 
				Environment.currentDirectory + File.separator + FILE_2};
		try {
			result = globCmd.globFileAndDirectory(Environment.currentDirectory + File.separator + "*");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobAllFolders() {
		expected = new String[] {FOLDER_1, FOLDER_2};
		try {
			result = globCmd.globFileAndDirectory("*/");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobAtFront() {
		expected = new String[] {FILE_1, FILE_2};
		try {
			result = globCmd.globFileAndDirectory("*.txt");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobAtEnd() {
		expected = new String[] {FOLDER_1, FOLDER_2, FILE_1, FILE_2};
		try {
			result = globCmd.globFileAndDirectory("f*");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobAtMiddle() {
		expected = new String[] {FILE_1, FILE_2};
		try {
			result = globCmd.globFileAndDirectory("f*.txt");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobParentDirectory() {
		expected = new String[] {FOLDER_1 + File.separator + FILE_1, FOLDER_2 + File.separator + FILE_1};
		try {
			result = globCmd.globFileAndDirectory("*" + File.separator + FILE_1);
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobMultipleInSingleFolder() {
		expected = new String[] {FOLDER_1, FILE_1};
		try {
			result = globCmd.globFileAndDirectory("*1*");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testGlobMultipleInMutipleFolder() {
		expected = new String[] {FOLDER_1 + File.separator + FILE_1, 
				FOLDER_1 + File.separator + FILE_2, 
				FOLDER_2 + File.separator + FILE_1, 
				FOLDER_2 + File.separator + FILE_2};
		try {
			result = globCmd.globFileAndDirectory("*" + File.separator + "file*");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertArrayEquals(expected, result);
	}

	@Test
	public void testInvalidGlobNull() {
		try {
			result = globCmd.globFileAndDirectory("   ");
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertEquals("shell: Null Pointer Exception", exceptionMessage);
	}
}
