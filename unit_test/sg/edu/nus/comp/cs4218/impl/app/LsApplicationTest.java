package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.LsException;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class LsApplicationTest {

	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system"
			+ File.separator + "ls_test_system";

	private static final String STR_COLON = ":";
	private static final String STR_TABS = "\t";

	private static final String FOLDER1 = "folder1";
	private static final String FOLDER1_1 = "folder1_1";
	private static final String FOLDER2 = "folder2";
	private static final String FOLDER_WITH_SPACE = "folder name with space";

	private static final String ABS_FOLDER1 = TEST_DIR + File.separator + FOLDER1;
	private static final String ABS_FOLDER2 = TEST_DIR + File.separator + FOLDER2;
	private static final String ABS_FOLDER1_1 = ABS_FOLDER1 + File.separator + FOLDER1_1;

	private static final String REL_FOLDER1_1 = FOLDER1 + File.separator + FOLDER1_1;

	private static final String HEAD_FOLDER1 = FOLDER1 + STR_COLON;
	private static final String HEAD_FOLDER2 = FOLDER2 + STR_COLON;
	private static final String HEAD_FOLDER1_1 = FOLDER1 + File.separator + FOLDER1_1 + STR_COLON;
	private static final String HEAD_ABS_FOLDER1 = ABS_FOLDER1 + STR_COLON;
	private static final String HEAD_ABS_FOLDER2 = ABS_FOLDER2 + STR_COLON;
	private static final String HEAD_ABS_FOLDER11 = ABS_FOLDER1_1 + STR_COLON;

	private static final String FILE1 = "file1.txt";
	private static final String FILE2 = "file2.txt";
	private static final String FOLDER1_FILE1 = "file1_in_folder1.txt";
	private static final String FOLDER2_FILE1 = "file1_in_folder2.txt";
	private static final String FOLDER2_FILE2 = "file2_in_folder2.txt";
	private static final String FOLDER1_1_FILE2 = "file2_in_folder1_1.txt";
	private static final String FILE_WITH_SPACE = "file name with space.txt";
	private static final String FILE_HIDDEN = ".hiddenFile1.txt";
	private static final String FILE_NON_EXISTENT = "nonExistentFile";

	private static final String REL_FOLDER1_FILE1 = FOLDER1 + File.separator + FOLDER1_FILE1;
	private static final String REL_FOLDER2_FILE1 = FOLDER2 + File.separator + FOLDER2_FILE1;
	private static final String REL_FOLDER2_FILE2 = FOLDER2 + File.separator + FOLDER2_FILE2;
	private static final String REL_F1_1_FILE2 = FOLDER1 + File.separator + FOLDER1_1 + File.separator
			+ FOLDER1_1_FILE2;

	private static final String ABS_FILE1 = TEST_DIR + File.separator + FILE1;
	private static final String ABS_FOLDER1_FILE1 = TEST_DIR + File.separator + REL_FOLDER1_FILE1;
	private static final String ABS_F1_1_FILE2 = TEST_DIR + File.separator + REL_F1_1_FILE2;

	private static final String CUR_CONTENT = "'file name with space.txt'" + STR_TABS + "file1.txt" + STR_TABS
			+ "file2.txt" + STR_TABS + "'folder name with space'" + STR_TABS + "folder1" + STR_TABS + "folder2";
	private static final String FOLDER1_CONTENT = "file1_in_folder1.txt" + STR_TABS + "file2_in_folder1.txt" + STR_TABS
			+ "folder1_1";
	private static final String FOLDER2_CONTENT = "file1_in_folder2.txt" + STR_TABS + "file2_in_folder2.txt";
	private static final String FOLDER1_1_CONTENT = "file1_in_folder1_1.txt" + STR_TABS + "file2_in_folder1_1.txt";
	private static final String FOLDER_WS_CONTENT = "file1_in_folder_spaces.txt" + STR_TABS
			+ "file2_in_folder_spaces.txt";

	private LsApplication lsApp;
	private String expected, result;
	private OutputStream stdout;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws LsException {
		Environment.currentDirectory = TEST_DIR;
		lsApp = new LsApplication();
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void testListToListCurDirContentUsingNoArg() throws LsException {
		expected = CUR_CONTENT;
		result = lsApp.listFolderContent(false, false);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListFileUsingFileInArg() throws LsException {
		expected = FILE1;
		result = lsApp.listFolderContent(false, false, FILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListDirContentUsingRelDirInArg() throws LsException {
		expected = FOLDER1_CONTENT;
		result = lsApp.listFolderContent(false, false, FOLDER1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListDirContentUsingAbsDirInArg() throws LsException {
		expected = FOLDER1_1_CONTENT;
		result = lsApp.listFolderContent(false, false, ABS_FOLDER1_1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToAppendQuoteToNameUsingFileNameWithSpaceInArg() throws LsException {
		expected = "'" + FILE_WITH_SPACE + "'";
		result = lsApp.listFolderContent(false, false, FILE_WITH_SPACE);
		assertEquals(expected, result);
	}

	@Test
	public void testListToNotListHiddenFileUsingNoArg() throws LsException {
		result = lsApp.listFolderContent(false, false);
		assertFalse(result.contains(FILE_HIDDEN));
	}

	@Test
	public void testListToNotListHiddenFileUsingDirInArg() throws LsException {
		result = lsApp.listFolderContent(false, false, FOLDER1);
		assertFalse(result.contains(FILE_HIDDEN));
	}

	@Test
	public void testListToListFilesUsingMultiFilesInArg() throws LsException {
		expected = FILE1 + STR_TABS + FILE2;
		result = lsApp.listFolderContent(false, false, FILE1, FILE2);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListMultiDirsContentUsingMultiDirsInArg() throws LsException {
		expected = HEAD_FOLDER1 + OSUtil.NEWLINE + FOLDER1_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE + HEAD_FOLDER2
				+ OSUtil.NEWLINE + FOLDER2_CONTENT;
		result = lsApp.listFolderContent(false, false, FOLDER1, FOLDER2);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListMultiFilesAndDirsContentUsingMultiFilesAndDirsInArg() throws LsException {
		expected = ABS_FILE1 + STR_TABS + FILE2 + OSUtil.NEWLINE + OSUtil.NEWLINE + HEAD_ABS_FOLDER1 + OSUtil.NEWLINE
				+ FOLDER1_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE + HEAD_FOLDER2 + OSUtil.NEWLINE + FOLDER2_CONTENT;
		result = lsApp.listFolderContent(false, false, ABS_FILE1, FILE2, ABS_FOLDER1, FOLDER2);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListCurDirOnlyUsingDirOnlyWithNoArg() throws LsException {
		expected = ".";
		result = lsApp.listFolderContent(true, false);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListRelDirUsingDirOnlyWithRelDirInArg() throws LsException {
		expected = REL_FOLDER2_FILE1;
		result = lsApp.listFolderContent(true, false, REL_FOLDER2_FILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListAbsDirUsingDirOnlyWithAbsDirInArg() throws LsException {
		expected = ABS_FOLDER2;
		result = lsApp.listFolderContent(true, false, ABS_FOLDER2);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListFilesAndDirsUsingDirOnlyWithFilesAndDirsInArg() throws LsException {
		expected = ABS_FOLDER1_FILE1 + STR_TABS + ABS_FOLDER2 + STR_TABS + FOLDER1 + STR_TABS + REL_FOLDER2_FILE2;
		result = lsApp.listFolderContent(true, false, ABS_FOLDER2, REL_FOLDER2_FILE2, FOLDER1, ABS_FOLDER1_FILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListAllCurDirContentUsingRecursiveWithNoArg() throws LsException {
		expected = ".:" + OSUtil.NEWLINE + CUR_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE + "'." + File.separator
				+ FOLDER_WITH_SPACE + "':" + OSUtil.NEWLINE + FOLDER_WS_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE + "."
				+ File.separator + FOLDER1 + ":" + OSUtil.NEWLINE + FOLDER1_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE
				+ "." + File.separator + FOLDER1 + File.separator + FOLDER1_1 + ":" + OSUtil.NEWLINE + FOLDER1_1_CONTENT
				+ OSUtil.NEWLINE + OSUtil.NEWLINE + "." + File.separator + FOLDER2 + ":" + OSUtil.NEWLINE
				+ FOLDER2_CONTENT;
		result = lsApp.listFolderContent(false, true);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListAllDirContentUsingRecursiveWithDirInArg() throws LsException {
		expected = HEAD_ABS_FOLDER1 + OSUtil.NEWLINE + FOLDER1_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE
				+ HEAD_ABS_FOLDER11 + OSUtil.NEWLINE + FOLDER1_1_CONTENT;
		result = lsApp.listFolderContent(false, true, ABS_FOLDER1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListAllDirsContentUsingRecursiveWithMultiDirsInArg() throws LsException {
		expected = HEAD_FOLDER1 + OSUtil.NEWLINE + FOLDER1_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE + HEAD_FOLDER1_1
				+ OSUtil.NEWLINE + FOLDER1_1_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE + HEAD_FOLDER2 + OSUtil.NEWLINE
				+ FOLDER2_CONTENT;
		result = lsApp.listFolderContent(false, true, FOLDER2, FOLDER1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToListAllFilesAndDirsContentUsingRecursiveWithMultiFilesAndDirsInArg() throws LsException {
		expected = ABS_FOLDER1_FILE1 + STR_TABS + REL_FOLDER2_FILE1 + OSUtil.NEWLINE + OSUtil.NEWLINE + HEAD_ABS_FOLDER2
				+ OSUtil.NEWLINE + FOLDER2_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE + HEAD_FOLDER1 + OSUtil.NEWLINE
				+ FOLDER1_CONTENT + OSUtil.NEWLINE + OSUtil.NEWLINE + HEAD_FOLDER1_1 + OSUtil.NEWLINE
				+ FOLDER1_1_CONTENT;
		result = lsApp.listFolderContent(false, true, ABS_FOLDER2, REL_FOLDER2_FILE1, FOLDER1, ABS_FOLDER1_FILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToFilesAndDirsUsingDirOnlyAndRecursiveWithMultiFilesAndDirsInArg() throws LsException {
		expected = ABS_FOLDER1_FILE1 + STR_TABS + ABS_FOLDER2 + STR_TABS + FOLDER1 + STR_TABS + REL_FOLDER2_FILE2;
		result = lsApp.listFolderContent(true, true, ABS_FOLDER2, REL_FOLDER2_FILE2, FOLDER1, ABS_FOLDER1_FILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testListToThrowsLsExpUsingNonExistentFile() throws LsException {
		thrown.expect(LsException.class);
		thrown.expectMessage("ls: cannot access 'nonExistentFile': No such file or directory");
		lsApp.listFolderContent(true, true, FILE_NON_EXISTENT);
	}

	@Test
	public void testListToThrowsLsExpUsingMultiFilesWithNonExistentFile() throws LsException {
		thrown.expect(LsException.class);
		thrown.expectMessage("ls: cannot access 'nonExistentFile': No such file or directory");
		lsApp.listFolderContent(true, true, FILE1, FILE_NON_EXISTENT, FOLDER1);
	}

	@Test
	public void testListToThrowsLsExpUsingEmptyFileName() throws LsException {
		thrown.expect(LsException.class);
		thrown.expectMessage("ls: cannot access '': No such file or directory");
		lsApp.listFolderContent(true, true, "");
	}

	@Test
	public void testListToThrowsLsExpUsingSpacesFileName() throws LsException {
		thrown.expect(LsException.class);
		thrown.expectMessage("ls: cannot access '   ': No such file or directory");
		lsApp.listFolderContent(true, true, "   ");
	}

	@Test
	public void testRunToListDirUsingCombiOptionInSingleDash() throws LsException {
		expected = ABS_FOLDER1;
		String[] strArr = { "-dR", ABS_FOLDER1 };

		lsApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToListDirUsingCombiOptionWithDoubleDashWithoutSpace() throws LsException {
		expected = ABS_F1_1_FILE2 + STR_TABS + FOLDER2;
		String[] strArr = { "-d-R", ABS_F1_1_FILE2, FOLDER2 };

		lsApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToListDirUsingCombiOptionWithDoubleDashWithSpace() throws LsException {
		expected = REL_FOLDER1_1;
		String[] strArr = { "-d", "-R", REL_FOLDER1_1 };

		lsApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToListDirUsingCombiOptionWithDiffSeq() throws LsException {
		expected = REL_F1_1_FILE2;
		String[] strArr = { "-R-d", REL_F1_1_FILE2 };

		lsApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToListDirUsingOptionAtEnd() throws LsException {
		expected = FOLDER1 + STR_TABS + FOLDER2;
		String[] strArr = { FOLDER1, FOLDER2, "-d" };

		lsApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToListDirUsingOptionBtwDirs() throws LsException {
		expected = FOLDER1 + STR_TABS + FOLDER2;
		String[] strArr = { FOLDER1, "-R", FOLDER2, "-d" };

		lsApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToThrowsLsExpUsingNullArg() throws LsException {
		expected = CUR_CONTENT;
		lsApp.run(null, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToThrowsLsExpUsingInvalidOption() throws LsException {
		thrown.expect(LsException.class);
		thrown.expectMessage("ls: invalid option -- 'f'");
		String[] strArr = { "-f", FOLDER1 };
		lsApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsLsExpUsingOptionWithDoubleDash() throws LsException {
		thrown.expect(LsException.class);
		thrown.expectMessage("ls: invalid option -- '-'");
		String[] strArr = { "--", FOLDER1 };
		lsApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsLsExpUsingOptionEndWithDash() throws LsException {
		thrown.expect(LsException.class);
		thrown.expectMessage("ls: invalid option -- '-'");
		String[] strArr = { "-d-R-", FOLDER1 };
		lsApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsLsExpUsingNullOutputStream() throws LsException {
		thrown.expect(LsException.class);
		thrown.expectMessage("ls: Null Pointer Exception");
		lsApp.run(null, null, null);
	}
}
