package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.CdException;
public class CdApplicationTest {
	public static final String DIR_USER = "user.dir";
	public static final String DIR_TEST_SYSTEM = "test_system";
	public static final String DIR_FOLDER1 = "folder1";
	public static final String DIR_FOLDER2 = "folder2";
	
	CdApplication cdApp;
	String expected, result;

	@Before
	public void setUp() {
		Environment.currentDirectory = System.getProperty(DIR_USER) + File.separator + DIR_TEST_SYSTEM;
		cdApp = new CdApplication();
	}

	@Test
	public void testRelativeDirectoryChange() {
		expected = System.getProperty(DIR_USER) + File.separator + DIR_TEST_SYSTEM + File.separator + DIR_FOLDER1;
		try {
			cdApp.changeToDirectory(DIR_FOLDER1, new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testAbsoluteDirectoryChange() {
		expected = System.getProperty(DIR_USER) + File.separator + DIR_TEST_SYSTEM + File.separator + DIR_FOLDER2;
		try {
			cdApp.changeToDirectory(System.getProperty(DIR_USER) + File.separator + DIR_TEST_SYSTEM + File.separator + DIR_FOLDER2, 
					new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testUserDirectoryChange() {
		expected = System.getProperty(DIR_USER);
		try {
			cdApp.changeToDirectory(null, new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testCurrentDirectoryChange() {
		expected = System.getProperty(DIR_USER) + File.separator + DIR_TEST_SYSTEM;
		try {
			cdApp.changeToDirectory(".", new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testParentDirectoryChange() {
		expected = System.getProperty(DIR_USER);
		try {
			cdApp.changeToDirectory("..", new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testDirChangeMixWithSpecialDir() {
		expected = System.getProperty(DIR_USER) + File.separator + DIR_TEST_SYSTEM + File.separator + DIR_FOLDER2;
		try {
			cdApp.changeToDirectory("folder1/../folder2", new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testEmptyDirectoryChange() {
		expected = System.getProperty(DIR_USER);
		try {
			cdApp.changeToDirectory("", new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidNonExistentChange() {
		expected = "cd: unknownDir: No such file or directory";
		try {
			cdApp.changeToDirectory("unknownDir", new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidFileChange() {
		expected = "cd: file1.txt: Not a directory";
		try {
			cdApp.changeToDirectory("file1.txt", new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidSpacesDirectoryChange() {
		expected = "cd:    : No such file or directory";
		try {
			cdApp.changeToDirectory("   ", new Environment());
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullEnv() {
		expected = "cd: Null Pointer Exception";
		try {
			cdApp.changeToDirectory(null, null);
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testNullArgs() {
		expected = System.getProperty(DIR_USER);
		try {
			cdApp.run(null, null, null);
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testEmptyStringInArgs() {
		expected = System.getProperty(DIR_USER);
		try {
			String[] strArr = {""};
			cdApp.run(strArr, null, null);
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidMultipleDirectoryChange() {
		expected = "cd: too many arguments";
		try {
			String[] strArr = {DIR_FOLDER1, DIR_FOLDER2};
			cdApp.run(strArr, null, null);
			result = Environment.currentDirectory;
		} catch (CdException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
}
