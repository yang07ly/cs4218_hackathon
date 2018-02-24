package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;

public class DiffApplicationTest {

	DiffApplication app;
	OutputStream outputStream;
	String expected, output;
	String currentDir;

	@Before
	public void setUp() {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "diff_test_system";
		currentDir = Environment.currentDirectory + File.separator;
		app = new DiffApplication();
		outputStream = new ByteArrayOutputStream();
		output = "";
		expected = "";
	}

	@Test
	public void testFileFileEmptyFileA() {
		expected = "diff: can't have empty argument";
		try {
			output = app.diffTwoFiles("", "file2.txt", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileEmptyFileB() {
		expected = "diff: can't have empty argument";
		try {
			output = app.diffTwoFiles("file2.txt", "", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileSpaceFileA() {
		expected = "diff: ' ': No such file or directory";
		try {
			output = app.diffTwoFiles(" ", "file2.txt", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileSpaceFileB() {
		expected = "diff: ' ': No such file or directory";
		try {
			output = app.diffTwoFiles("file2.txt", " ", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileInvalidFileA() {
		expected = "diff: 'asdf': No such file or directory";
		try {
			output = app.diffTwoFiles("asdf", "file2.txt", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileInvalidFileB() {
		expected = "diff: 'asdf': No such file or directory";
		try {
			output = app.diffTwoFiles("file2.txt", "asdf", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirEmptyFolderA() {
		expected = "diff: can't have empty argument";
		try {
			output = app.diffTwoDir("", "dir2", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirEmptyFolderB() {
		expected = "diff: can't have empty argument";
		try {
			output = app.diffTwoDir("dir1", "", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirSpaceFolderA() {
		expected = "diff: ' ': No such file or directory";
		try {
			output = app.diffTwoDir(" ", "dir1", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirSpaceFolderB() {
		expected = "diff: ' ': No such file or directory";
		try {
			output = app.diffTwoDir("dir1", " ", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirInvalidFolderA() {
		expected = "diff: 'asdf': No such file or directory";
		try {
			output = app.diffTwoDir("asdf", "dir1", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirInvalidFolderB() {
		expected = "diff: 'asdf': No such file or directory";
		try {
			output = app.diffTwoDir("dir1", "asdf", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamInvalidFile() {
		expected = "diff: 'asdf': No such file or directory";
		try {
			File file = new File(currentDir + "file1.txt");
			InputStream inputStream = new FileInputStream(file);
			output = app.diffFileAndStdin("asdf", inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamSpaceFile() {
		expected = "diff: ' ': No such file or directory";
		try {
			File file = new File(currentDir + "file1.txt");
			InputStream inputStream = new FileInputStream(file);
			output = app.diffFileAndStdin(" ", inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamEmptyFileName() {
		expected = "diff: can't have empty argument";
		try {
			File file = new File(currentDir + "file1.txt");
			InputStream inputStream = new FileInputStream(file);
			output = app.diffFileAndStdin("", inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}
	/*
	 * valid tests:
	 */

	@Test
	public void testFileFileNoDiff() {
		expected = "";
		try {
			output = app.diffTwoFiles("file1.txt", "file1_copy.txt", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirNoDiff() {
		expected = "";
		try {
			output = app.diffTwoDir("dir1", "dir1_copy", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamNoDiff() {
		expected = "";
		try {
			File file = new File(currentDir + "file1_copy.txt");
			InputStream inputStream = new FileInputStream(file);
			output = app.diffFileAndStdin("file1.txt", inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileRelRel() {
		expected = "<line2\n>line6";
		try {
			output = app.diffTwoFiles("file1.txt", "file2.txt", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileAbsAbs() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file2.txt";
		expected = "<line2\n>line6";
		try {
			output = app.diffTwoFiles(file1, file2, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueFalseFalse() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file2.txt";
		expected = "<line2\n>line6";
		try {
			output = app.diffTwoFiles(file1, file2, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueFalseFalseNoDiff() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file1_copy.txt";
		expected = "Files 'file1.txt' and 'file1_copy.txt' are identical";
		try {
			output = app.diffTwoFiles(file1, file2, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseTrueFalse() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file1_withBlankLines.txt";
		expected = "";
		try {
			output = app.diffTwoFiles(file1, file2, false, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseFalseTrue() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file2.txt";
		expected = "Files 'file1.txt' and 'file2.txt' differ";
		try {
			output = app.diffTwoFiles(file1, file2, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseFalseTrueNoDiff() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file1_copy.txt";
		expected = "";
		try {
			output = app.diffTwoFiles(file1, file2, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseTrueTrue() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file2_withBlankLines.txt";
		expected = "Files 'file1.txt' and 'file2_withBlankLines.txt' differ";
		try {
			output = app.diffTwoFiles(file1, file2, false, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseTrueTrueNoDiff() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file1_withBlankLines.txt";
		expected = "";
		try {
			output = app.diffTwoFiles(file1, file2, false, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueTrueFalse() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file2_withBlankLines.txt";
		expected = "<line2\n>line6";
		try {
			output = app.diffTwoFiles(file1, file2, true, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueTrueFalseNoDiff() {
		String file1 = currentDir + "file1.txt", file2 = currentDir + "file1_withBlankLines.txt";
		expected = "Files 'file1.txt' and 'file1_withBlankLines.txt' are identical";
		try {
			output = app.diffTwoFiles(file1, file2, true, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamRel() {
		expected = "<line2\n>line6";
		try {
			InputStream fileStream = new FileInputStream(new File("file2.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamAbs() {
		expected = "<line2\n>line6";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file2.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamEmptyStream() {
		expected = "<line1\n<line2\n<line3\n<line5\n";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "emptyFile.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueFalseFalse() {
		expected = "<line1\n<line2\n<line3\n<line5\n";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file2.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueFalseFalseNoDiff() {
		expected = "Files 'file1.txt' and 'file1_copy.txt' are identical";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file1_copy.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueFalseNoDiff() {
		expected = "";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file1_withBlankLines.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, false, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseFalseTrue() {
		expected = "Files 'file1.txt' and 'file2.txt' differ";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file2.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseFalseTrueNoDiff() {
		expected = "";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file1_copy.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueTrue() {
		expected = "Files 'file1.txt' and 'file2_withBlankLines.txt' differ";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file2_withBlankLines.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, false, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueTrueNoDiff() {
		expected = "";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file1_withBlankLines.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, false, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueTrueFalse() {
		expected = "<line2\n>line6";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file2_withBlankLines.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, true, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueTrueFalseNoDiff() {
		expected = "Files 'file1.txt' and 'file1_withBlankLines.txt' are identical";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "file1_withBlankLines.txt"));
			output = app.diffFileAndStdin("file1.txt", fileStream, true, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

}
