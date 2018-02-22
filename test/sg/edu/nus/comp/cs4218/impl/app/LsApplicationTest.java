package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.LsException;

public class LsApplicationTest {
	
	public static final String STRING_COLON = ":";
	public static final String FOLDER1 = "folder1";
	public static final String FOLDER1_1 = "folder1_1";
	public static final String FOLDER2 = "folder2";
	public static final String FILE1 = "file1.txt";
	public static final String FOLDER1_CONTENT = "file1_in_folder1.txt  file2_in_folder1.txt  folder1_1\n";
	public static final String FOLDER2_CONTENT = "file1_in_folder2.txt  file2_in_folder2.txt\n";
	
	LsApplication lsApp;
	String expected, result;
	OutputStream stdout;

	@Before
	public void setup() {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system";
		lsApp = new LsApplication();
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void testListCurrentFolder() {
		expected = "'file name with space.txt'  file1.txt  file2.txt  'folder name with space'  "
				+ FOLDER1 + "  folder2  sedTestFile1.txt  sedTestFile2.txt\n";
		try {
			result = lsApp.listFolderContent(false, false);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testListFile() {
		expected = "file1.txt\n";
		try {
			result = lsApp.listFolderContent(false, false, FILE1);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testListRelativeFolderContent() {
		expected = FOLDER1_CONTENT;
		try {
			result = lsApp.listFolderContent(false, false, FOLDER1);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testListAbsoluteFolderContent() {
		expected = "file1_in_folder1_1.txt  file2_in_folder1_1.txt\n";
		try {
			result = lsApp.listFolderContent(false, false, Environment.currentDirectory + "/folder1/folder1_1");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testDisplayNameContainQuote() {
		expected = "'file name with space.txt'\n";
		try {
			result = lsApp.listFolderContent(false, false, "file name with space.txt");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testNoHiddenFilesInCurrentFolder() {
		try {
			result = lsApp.listFolderContent(false, false);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertFalse(result.contains(".hiddenFile1.txt"));
	}

	@Test
	public void testNoHiddenFilesInOtherFolder() {
		try {
			result = lsApp.listFolderContent(false, false, FOLDER1);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertFalse(result.contains(".hiddenFile2.txt"));
	}

	@Test
	public void testListMutipleFiles() {
		expected = "file1.txt  file2.txt\n";
		try {
			result = lsApp.listFolderContent(false, false, FILE1, "file2.txt");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testListMultipleFoldersContent() {
		expected = FOLDER1 + STRING_COLON + "\n" + 
				FOLDER1_CONTENT + "\n" + 
				FOLDER2 + STRING_COLON + "\n" + 
				FOLDER2_CONTENT;
		try {
			result = lsApp.listFolderContent(false, false, FOLDER1, FOLDER2);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testListMutipleFilesAndFolders() {
		expected = Environment.currentDirectory + File.separator + FILE1 + "  file2.txt\n\n" + 
				Environment.currentDirectory + File.separator + FOLDER1 + STRING_COLON + "\n" + 
				FOLDER1_CONTENT + "\n" + 
				FOLDER2 + STRING_COLON + "\n" + 
				FOLDER2_CONTENT;
		try {
			result = lsApp.listFolderContent(false, false, Environment.currentDirectory + "/file1.txt", "file2.txt",
					Environment.currentDirectory + "/folder1", FOLDER2);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testDirectoryListCurrentFolder() {
		expected = ".\n";
		try {
			result = lsApp.listFolderContent(true, false);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testDirectoryListRelativeFile() {
		expected = FOLDER2 + File.separator + "file1_in_folder2.txt\n";
		try {
			result = lsApp.listFolderContent(true, false, FOLDER2 + "/file1_in_folder2.txt");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testDirectoryListAbsoluteFolder() {
		expected = Environment.currentDirectory + File.separator + FOLDER2 + "\n";
		try {
			result = lsApp.listFolderContent(true, false, Environment.currentDirectory + File.separator + FOLDER2);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testDirectoryListFilesAndFolders() {
		expected = Environment.currentDirectory + File.separator + FOLDER1 + File.separator + "file1_in_folder1.txt  " +
				Environment.currentDirectory + File.separator + FOLDER2 + "  "  +
				FOLDER1 + "  folder2" + File.separator  + "file2_in_folder2.txt\n";
		try {
			result = lsApp.listFolderContent(true, false, Environment.currentDirectory + File.separator + FOLDER2, 
					FOLDER2 + "/file2_in_folder2.txt", FOLDER1, 
					Environment.currentDirectory + "/folder1/file1_in_folder1.txt");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testRecursiveListCurrentFolder() {
		expected = ".:\n" + 
				"'file name with space.txt'  file1.txt  file2.txt  'folder name with space'  "
				+ "folder1  folder2  sedTestFile1.txt  sedTestFile2.txt\n\n" + 
				"'." + File.separator + "folder name with space':\n" + 
				"file1_in_folder_spaces.txt  file2_in_folder_spaces.txt\n\n" + 
				".\\folder1:\n" + 
				FOLDER1_CONTENT + "\n" + 
				"." + File.separator + FOLDER1 + File.separator + FOLDER1_1 + STRING_COLON + "\n" + 
				"file1_in_folder1_1.txt  file2_in_folder1_1.txt\n\n" + 
				"." + File.separator + FOLDER2 + STRING_COLON + "\n" + 
				FOLDER2_CONTENT;
		try {
			result = lsApp.listFolderContent(false, true);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testRecursiveListAbsoluteFolder() {
		expected =  Environment.currentDirectory + File.separator + FOLDER1 + STRING_COLON + "\n" + 
				FOLDER1_CONTENT + "\n" + 
				Environment.currentDirectory + File.separator + FOLDER1 + File.separator + FOLDER1_1 + STRING_COLON + "\n" + 
				"file1_in_folder1_1.txt  file2_in_folder1_1.txt\n";
		try {
			result = lsApp.listFolderContent(false, true, Environment.currentDirectory + "/folder1");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testRecursiveListMultipleFolders() {
		expected =  FOLDER1 + STRING_COLON + "\n" + 
				FOLDER1_CONTENT + "\n" + 
				FOLDER1 + File.separator + FOLDER1_1 + STRING_COLON + "\n" + 
				"file1_in_folder1_1.txt  file2_in_folder1_1.txt\n\n" +
				FOLDER2 + STRING_COLON + "\n" + 
				FOLDER2_CONTENT;
		try {
			result = lsApp.listFolderContent(false, true, FOLDER2, FOLDER1);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testRecursiveListFilesAndFolders() {
		expected =  Environment.currentDirectory + File.separator + FOLDER1 + File.separator + "file1_in_folder1.txt  " + 
				FOLDER2 + File.separator + "file1_in_folder2.txt\n\n" + 
				Environment.currentDirectory + File.separator + FOLDER2 + STRING_COLON + "\n" + 
				"file1_in_folder2.txt  file2_in_folder2.txt\n\n" +
				FOLDER1 + STRING_COLON + "\n" + 
				FOLDER1_CONTENT + "\n" + 
				FOLDER1 + File.separator + FOLDER1_1 + STRING_COLON + "\n" + 
				"file1_in_folder1_1.txt  file2_in_folder1_1.txt\n"; 
		try {
			result = lsApp.listFolderContent(false, true, Environment.currentDirectory + File.separator + FOLDER2, 
					FOLDER2 + "/file1_in_folder2.txt", FOLDER1, 
					Environment.currentDirectory + "/folder1/file1_in_folder1.txt");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testDirectoryAndRecursiveListFilesAndFolders() {
		expected = Environment.currentDirectory + File.separator + FOLDER1 + File.separator + "file1_in_folder1.txt  " +
				Environment.currentDirectory + File.separator + FOLDER2 + "  "  +
				FOLDER1 + "  folder2" + File.separator  + "file2_in_folder2.txt\n";
		try {
			result = lsApp.listFolderContent(true, true, Environment.currentDirectory + File.separator + FOLDER2, 
					FOLDER2 + "/file2_in_folder2.txt", FOLDER1, 
					Environment.currentDirectory + "/folder1/file1_in_folder1.txt");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidFile() {
		expected = "ls: cannot access 'nonExistentFile': No such file or directory";
		try {
			result = lsApp.listFolderContent(true, true, "nonExistentFile");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidFileWithValidFiles() {
		expected = "ls: cannot access 'nonExistentFile': No such file or directory";
		try {
			result = lsApp.listFolderContent(true, true, FILE1, "nonExistentFile", FOLDER1);
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testEmptyFile() {
		expected = "ls: cannot access '': No such file or directory";
		try {
			result = lsApp.listFolderContent(true, true, "");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testSpacesAsFileName() {
		expected = "ls: cannot access '   ': No such file or directory";
		try {
			result = lsApp.listFolderContent(true, true, "   ");
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testOptionCombiSingleDash() {
		expected = Environment.currentDirectory + File.separator + FOLDER1 + "\n";
		try {
			String[] strArr = {"-dR", Environment.currentDirectory + "/folder1"};
			lsApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testOptionCombiDoubleDashNoSpace() {
		expected = Environment.currentDirectory + File.separator + FOLDER1 + File.separator + FOLDER1_1 + 
				File.separator + "file2_in_folder1_1.txt  " + FOLDER2 + "\n";
		try {
			String[] strArr = {"-d-R", Environment.currentDirectory + "/folder1/folder1_1/file2_in_folder1_1.txt", FOLDER2};
			lsApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testOptionCombiDoubleDashWithSpace() {
		expected = FOLDER1 + File.separator + FOLDER1_1 + "\n";
		try {
			String[] strArr = {"-d", "-R", FOLDER1 + "/folder1_1"};
			lsApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testOptionCombiDiffSeq() {
		expected = FOLDER1 + File.separator + FOLDER1_1 + File.separator + "file2_in_folder1_1.txt\n";
		try {
			String[] strArr = {"-R-d", FOLDER1 + "/folder1_1/file2_in_folder1_1.txt"};
			lsApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testOptionPositionAtEnd() {
		expected = FOLDER1 + "  folder2\n";
		try {
			String[] strArr = {FOLDER1, FOLDER2, "-d"};
			lsApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testOptionPositionAtCenter() {
		expected = FOLDER1 + "  folder2\n";
		try {
			String[] strArr = {FOLDER1, "-R", FOLDER2, "-d"};
			lsApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testNullArgs() {
		expected = "'file name with space.txt'  file1.txt  file2.txt  'folder name with space'  "
				+ FOLDER1 + "  folder2  sedTestFile1.txt  sedTestFile2.txt\n";
		try {
			lsApp.run(null, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidOption() {
		expected = "ls: invalid option -- 'f'";
		try {
			String[] strArr = {"-f", FOLDER1};
			lsApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidOptionCombi() {
		expected = "ls: invalid option -- '-'";
		try {
			String[] strArr = {"-d-R-", FOLDER1};
			lsApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullOutputStream() {
		expected = "ls: Null Pointer Exception";
		try {
			lsApp.run(null, null, null);
			result = stdout.toString();
		} catch (LsException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
}
