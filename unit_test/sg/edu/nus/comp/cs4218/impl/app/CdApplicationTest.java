package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.CdException;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class CdApplicationTest {

	private static final String DIR_USER = System.getProperty("user.dir");
	private static final String DIR_TEST_SYS = DIR_USER + File.separator + "test_system";
	private static final String DIR_CD_TEST_SYS = DIR_TEST_SYS + File.separator + "cd_test_system";

	private static final String DIR_FOLDER1 = "folder1";
	private static final String DIR_FOLDER2 = "folder2";

	private static final String ABS_DIR_FOLDER1 = DIR_CD_TEST_SYS + File.separator + DIR_FOLDER1;
	private static final String ABS_DIR_FOLDER2 = DIR_CD_TEST_SYS + File.separator + DIR_FOLDER2;

	private CdApplication cdApp;
	private String expected, result;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		Environment.currentDirectory = DIR_CD_TEST_SYS;
		cdApp = new CdApplication();
	}

	@Test
	public void testRelativeDirectoryChange() throws CdException {
		expected = ABS_DIR_FOLDER1;
		cdApp.changeToDirectory(DIR_FOLDER1);
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testAbsoluteDirectoryChange() throws CdException {
		expected = ABS_DIR_FOLDER2;
		cdApp.changeToDirectory(ABS_DIR_FOLDER2);
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testUserDirectoryChange() throws CdException {
		expected = DIR_USER;
		cdApp.changeToDirectory(null);
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testCurrentDirectoryChange() throws CdException {
		expected = DIR_CD_TEST_SYS;
		cdApp.changeToDirectory(".");
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testParentDirectoryChange() throws CdException {
		expected = DIR_TEST_SYS;
		cdApp.changeToDirectory("..");
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testDirChangeMixWithSpecialDir() throws CdException {
		expected = ABS_DIR_FOLDER2;
		cdApp.changeToDirectory("folder1" + OSUtil.SEP + "../folder2");
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testEmptyDirectoryChange() throws CdException {
		expected = DIR_USER;
		cdApp.changeToDirectory("");
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testNullArgs() throws CdException {
		expected = DIR_USER;
		cdApp.run(null, null, null);
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testEmptyArgs() throws CdException {
		expected = DIR_USER;
		cdApp.run(new String[0], null, null);
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testEmptyStringInArgs() throws CdException {
		expected = DIR_USER;
		cdApp.run(new String[] { "" }, null, null);
		result = Environment.currentDirectory;
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidNonExistentChange() throws CdException {
		thrown.expect(CdException.class);
		thrown.expectMessage("cd: unknownDir: No such file or directory");
		cdApp.changeToDirectory("unknownDir");
	}

	@Test
	public void testInvalidFileChange() throws CdException {
		thrown.expect(CdException.class);
		thrown.expectMessage("cd: file1.txt: Not a directory");
		cdApp.changeToDirectory("file1.txt");
	}

	@Test
	public void testInvalidSpacesDirectoryChange() throws CdException {
		thrown.expect(CdException.class);
		thrown.expectMessage("cd:    : No such file or directory");
		cdApp.changeToDirectory("   ");
	}

	@Test
	public void testInvalidMultipleDirectoryChange() throws CdException {
		thrown.expect(CdException.class);
		thrown.expectMessage("cd: too many arguments");
		cdApp.run(new String[] { DIR_FOLDER1, DIR_FOLDER2 }, null, null);
	}
}
