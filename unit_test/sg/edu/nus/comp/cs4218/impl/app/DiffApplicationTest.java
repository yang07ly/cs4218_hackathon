package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.DiffException;

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

	@Rule
	public ExpectedException thrown = ExpectedException.none();

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
	public void testRunExtraOperand() throws DiffException {
		expected = "diff: requires 2 files to be specified";
		String[] args = { FILE1_TXT, FILE2_TXT, FILE3_TXT };
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		app.run(args, null, null);
	}

	@Test
	public void testFileFileEmptyFileA() throws DiffException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles("", FILE2_TXT, false, false, false);
	}

	@Test
	public void testFileFileEmptyFileB() throws DiffException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(FILE2_TXT, "", false, false, false);
	}

	@Test
	public void testFileFileSpaceFileA() throws DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(" ", FILE2_TXT, false, false, false);
	}

	@Test
	public void testFileFileSpaceFileB() throws DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(FILE2_TXT, " ", false, false, false);
	}

	@Test
	public void testFileFileInvalidFileA() throws DiffException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(ASDF, FILE2_TXT, false, false, false);
	}

	@Test
	public void testFileFileInvalidFileB() throws DiffException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(FILE2_TXT, ASDF, false, false, false);
	}

	@Test
	public void testDirDirEmptyFolderA() throws DiffException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir("", "dir2", false, false, false);
	}

	@Test
	public void testDirDirEmptyFolderB() throws DiffException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(DIR1, "", false, false, false);
	}

	@Test
	public void testDirDirSpaceFolderA() throws DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(" ", DIR1, false, false, false);
	}

	@Test
	public void testDirDirSpaceFolderB() throws DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(DIR1, " ", false, false, false);
	}

	@Test
	public void testDirDirInvalidFolderA() throws DiffException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(ASDF, DIR1, false, false, false);
	}

	@Test
	public void testDirDirInvalidFolderB() throws DiffException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(DIR1, ASDF, false, false, false);
	}

	@Test
	public void testFileStreamInvalidFile() throws DiffException, FileNotFoundException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		File file = new File(currentDir + FILE1_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.diffFileAndStdin(ASDF, inputStream, false, false, false);
	}

	@Test
	public void testFileStreamSpaceFile() throws FileNotFoundException, DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		File file = new File(currentDir + FILE1_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.diffFileAndStdin(" ", inputStream, false, false, false);
	}

	@Test
	public void testFileStreamEmptyFileName() throws DiffException, FileNotFoundException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		File file = new File(currentDir + FILE1_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.diffFileAndStdin("", inputStream, false, false, false);
	}
	/*
	 * valid tests:
	 */

	@Test
	public void testFileFileNoDiff() throws DiffException {
		expected = "";
		output = app.diffTwoFiles(FILE1_TXT, FILE1_COPY_TXT, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirNoDiff() throws DiffException {
		expected = "";
		output = app.diffTwoDir(DIR1, "dir1_copy", false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamNoDiff() throws DiffException, FileNotFoundException {
		expected = "";
		File file = new File(currentDir + FILE1_COPY_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.diffFileAndStdin(FILE1_TXT, inputStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileRelRel() throws DiffException {
		expected = LINE2_LINE6;
		output = app.diffTwoFiles(FILE1_TXT, FILE2_TXT, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileAbsAbs() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = LINE2_LINE6;
		output = app.diffTwoFiles(file1, file2, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueFalseFalse() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = LINE2_LINE6;
		output = app.diffTwoFiles(file1, file2, true, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueFalseFalseNoDiff() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_COPY_TXT;
		expected = FILES + file1 + AND + file2 + " are identical";
		output = app.diffTwoFiles(file1, file2, true, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseTrueFalse() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
		expected = "";
		output = app.diffTwoFiles(file1, file2, false, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseFalseTrue() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = FILES + file1 + AND + file2 + " differ";
		output = app.diffTwoFiles(file1, file2, false, false, true);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseFalseTrueNoDiff() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_COPY_TXT;
		expected = "";
		output = app.diffTwoFiles(file1, file2, false, false, true);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseTrueTrue() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_WITH_BLANKS;
		expected = FILES + file1 + AND + file2 + " differ";
		output = app.diffTwoFiles(file1, file2, false, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileFalseTrueTrueNoDiff() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
		expected = "";
		output = app.diffTwoFiles(file1, file2, false, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueTrueFalse() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_WITH_BLANKS;
		expected = LINE2_LINE6;
		output = app.diffTwoFiles(file1, file2, true, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileTrueTrueFalseNoDiff() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
		expected = FILES + file1 + AND + file2 + " are identical";
		output = app.diffTwoFiles(file1, file2, true, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamRel() throws FileNotFoundException, DiffException {
		expected = LINE2_LINE6;
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamAbs() throws FileNotFoundException, DiffException {
		expected = LINE2_LINE6;
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamEmptyStream() throws FileNotFoundException, DiffException {
		expected = "< line1" + NEWLINE + "< line2" + NEWLINE + "< line3" + NEWLINE + "< line5";
		InputStream fileStream = new FileInputStream(new File(currentDir + "emptyFile.txt"));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueFalseFalse() throws FileNotFoundException, DiffException {
		expected = "< line2" + System.getProperty("line.separator") + "> line6";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueFalseFalseNoDiff() throws DiffException, FileNotFoundException {
		expected = "Files file1.txt and - are identical";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_COPY_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueFalseNoDiff() throws FileNotFoundException, DiffException {
		expected = "";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseFalseTrue() throws DiffException, FileNotFoundException {
		expected = "Files file1.txt and - differ";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, true);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseFalseTrueNoDiff() throws FileNotFoundException, DiffException {
		expected = "";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_COPY_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, true);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueTrue() throws FileNotFoundException, DiffException {
		expected = "Files file1.txt and - differ";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamFalseTrueTrueNoDiff() throws FileNotFoundException, DiffException {
		expected = "";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueTrueFalse() throws FileNotFoundException, DiffException {
		expected = LINE2_LINE6;
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueTrueFalseNoDiff() throws DiffException, FileNotFoundException {
		expected = "Files file1.txt and - are identical";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirRelRel() throws DiffException {
		expected = "Only in dir1: emptyFile.txt" + NEWLINE + "Only in dir2: file2.txt";
		output = app.diffTwoDir(DIR1, "dir2", false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDirDirAbsAbs() throws DiffException {
		expected = "Only in " + currentDir + DIR1 + ": emptyFile.txt" + NEWLINE + "Only in " + currentDir
				+ "dir2: file2.txt";
		output = app.diffTwoDir(currentDir + DIR1, currentDir + "dir2", false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileLastLineEmpty() throws DiffException {
		expected = "> ";
		output = app.diffTwoFiles(currentDir + "file3.txt", currentDir + "file3_lastLineEmpty.txt", false, false,
				false);
		assertEquals(expected, output);
	}
}
