package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;

public class DiffApplicationTest {

	private static final String AND = " and ";
	private static final String FILES = "Files ";
	private static final String FILE2_WITH_BLANKS = "file2_withBlankLines.txt";
	private static final String ASDF_NOT_FOUND = "diff: asdf: No such file or directory";
	private static final String LINE2_LINE6 = "< line2" + System.getProperty("line.separator") + "> line6";
	private static final String EMPTY_ARGS = "diff: : No such file or directory";
	private static final String FILE_NOT_FOUND = "diff:  : No such file or directory";
	private static final String DIR1 = "dir1";
	private static final String FILE3_TXT = "file3.txt";
	private static final String FILE2_TXT = "file2.txt";
	private static final String FILE1_WITH_BLANKS = "file1_withBlankLines.txt";
	private static final String FILE1_COPY_TXT = "file1_copy.txt";
	private static final String FILE1_TXT = "file1.txt";
	private static final String ASDF = "asdf";
	private static final String NEWLINE = System.getProperty("line.separator");
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
	public void testRunExtraOperand() {
		expected = "diff: requires 2 files to be specified";
		String[] args = { FILE1_TXT, FILE2_TXT, FILE3_TXT };
		try {
			app.run(args, null, null);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileEmptyFileA() {
		expected = EMPTY_ARGS;
		try {
			output = app.diffTwoFiles("", FILE2_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileEmptyFileB() {
		expected = EMPTY_ARGS;
		try {
			output = app.diffTwoFiles(FILE2_TXT, "", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileSpaceFileA() {
		expected = FILE_NOT_FOUND;
		try {
			output = app.diffTwoFiles(" ", FILE2_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileSpaceFileB() {
		expected = FILE_NOT_FOUND;
		try {
			output = app.diffTwoFiles(FILE2_TXT, " ", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileInvalidFileA() {
		expected = ASDF_NOT_FOUND;
		try {
			output = app.diffTwoFiles(ASDF, FILE2_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileInvalidFileB() {
		expected = ASDF_NOT_FOUND;
		try {
			output = app.diffTwoFiles(FILE2_TXT, ASDF, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirEmptyFolderA() {
		expected = EMPTY_ARGS;
		try {
			output = app.diffTwoDir("", "dir2", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirEmptyFolderB() {
		expected = EMPTY_ARGS;
		try {
			output = app.diffTwoDir(DIR1, "", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirSpaceFolderA() {
		expected = FILE_NOT_FOUND;
		try {
			output = app.diffTwoDir(" ", DIR1, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirSpaceFolderB() {
		expected = FILE_NOT_FOUND;
		try {
			output = app.diffTwoDir(DIR1, " ", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirInvalidFolderA() {
		expected = ASDF_NOT_FOUND;
		try {
			output = app.diffTwoDir(ASDF, DIR1, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirInvalidFolderB() {
		expected = ASDF_NOT_FOUND;
		try {
			output = app.diffTwoDir(DIR1, ASDF, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamInvalidFile() {
		expected = ASDF_NOT_FOUND;
		try {
			File file = new File(currentDir + FILE1_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.diffFileAndStdin(ASDF, inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamSpaceFile() {
		expected = FILE_NOT_FOUND;
		try {
			File file = new File(currentDir + FILE1_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.diffFileAndStdin(" ", inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamEmptyFileName() {
		expected = EMPTY_ARGS;
		try {
			File file = new File(currentDir + FILE1_TXT);
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
			output = app.diffTwoFiles(FILE1_TXT, FILE1_COPY_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirNoDiff() {
		expected = "";
		try {
			output = app.diffTwoDir(DIR1, "dir1_copy", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamNoDiff() {
		expected = "";
		try {
			File file = new File(currentDir + FILE1_COPY_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.diffFileAndStdin(FILE1_TXT, inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileRelRel() {
		expected = LINE2_LINE6;
		try {
			output = app.diffTwoFiles(FILE1_TXT, FILE2_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileAbsAbs() {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = LINE2_LINE6;
		try {
			output = app.diffTwoFiles(file1, file2, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueFalseFalse() {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = LINE2_LINE6;
		try {
			output = app.diffTwoFiles(file1, file2, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueFalseFalseNoDiff() {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_COPY_TXT;
		expected = FILES + file1 + AND + file2 + " are identical";
		try {
			output = app.diffTwoFiles(file1, file2, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseTrueFalse() {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
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
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = FILES + file1 + AND + file2 + " differ";
		try {
			output = app.diffTwoFiles(file1, file2, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseFalseTrueNoDiff() {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_COPY_TXT;
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
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_WITH_BLANKS;
		expected = FILES + file1 + AND + file2 + " differ";
		try {
			output = app.diffTwoFiles(file1, file2, false, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseTrueTrueNoDiff() {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
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
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_WITH_BLANKS;
		expected = LINE2_LINE6;
		try {
			output = app.diffTwoFiles(file1, file2, true, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueTrueFalseNoDiff() {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
		expected = FILES + file1 + AND + file2 + " are identical";
		try {
			output = app.diffTwoFiles(file1, file2, true, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamRel() {
		expected = LINE2_LINE6;
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamAbs() {
		expected = LINE2_LINE6;
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamEmptyStream() {
		expected = "< line1" + NEWLINE + "< line2" + NEWLINE + "< line3" + NEWLINE + "< line5";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + "emptyFile.txt"));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueFalseFalse() {
		expected = "< line2" + System.getProperty("line.separator") + "> line6";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueFalseFalseNoDiff() {
		expected = "Files file1.txt and - are identical";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_COPY_TXT));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueFalseNoDiff() {
		expected = "";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseFalseTrue() {
		expected = "Files file1.txt and - differ";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseFalseTrueNoDiff() {
		expected = "";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_COPY_TXT));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueTrue() {
		expected = "Files file1.txt and - differ";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_WITH_BLANKS));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueTrueNoDiff() {
		expected = "";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueTrueFalse() {
		expected = LINE2_LINE6;
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_WITH_BLANKS));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueTrueFalseNoDiff() {
		expected = "Files file1.txt and - are identical";
		try {
			InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
			output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirRelRel() {
		expected = "Only in dir1: emptyFile.txt" + NEWLINE + "Only in dir2: file2.txt";
		try {
			output = app.diffTwoDir(DIR1, "dir2", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirAbsAbs() {
		expected = "Only in " + currentDir + DIR1 + ": emptyFile.txt" + NEWLINE + "Only in " + currentDir
				+ "dir2: file2.txt";
		try {
			output = app.diffTwoDir(currentDir + DIR1, currentDir + "dir2", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileLastLineEmpty() {
		expected = "> ";
		try {
			output = app.diffTwoFiles(currentDir + "file3.txt", currentDir + "file3_lastLineEmpty.txt", false, false,
					false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}
}
