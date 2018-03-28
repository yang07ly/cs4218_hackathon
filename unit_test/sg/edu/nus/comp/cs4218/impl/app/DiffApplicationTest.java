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

	private static final String DIR2 = "dir2";
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
	public void testRunToThrowExpUsingMoreThanTwoFiles() throws DiffException {
		expected = "diff: requires 2 files to be specified";
		String[] args = { FILE1_TXT, FILE2_TXT, FILE3_TXT };
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		app.run(args, null, null);
	}

	@Test
	public void testDiffFilesToThrowExpUsingEmptyFileName() throws DiffException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles("", FILE2_TXT, false, false, false);
	}

	@Test
	public void testDiffFilesToThrowExpUsingEmptyFileName2() throws DiffException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(FILE2_TXT, "", false, false, false);
	}

	@Test
	public void testDiffFilesToThrowExpUsingSpaceAsFileName() throws DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(" ", FILE2_TXT, false, false, false);
	}

	@Test
	public void testDiffFilesToThrowExpUsingSpaceAsFileName2() throws DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(FILE2_TXT, " ", false, false, false);
	}

	@Test
	public void testDiffFilesToThrowExpUsingInvalidFile() throws DiffException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(ASDF, FILE2_TXT, false, false, false);
	}

	@Test
	public void testDiffFilesToThrowExpUsingInvalidFile2() throws DiffException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoFiles(FILE2_TXT, ASDF, false, false, false);
	}

	@Test
	public void testDiffDirsToThrowExpUsingEmptyDirName() throws DiffException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir("", DIR2, false, false, false);
	}

	@Test
	public void testDiffDirsToThrowExpUsingEmptyDirName2() throws DiffException {
		expected = EMPTY_ARGS;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(DIR1, "", false, false, false);
	}

	@Test
	public void testDiffDirsToThrowExpUsingSpaceAsDirName() throws DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(" ", DIR1, false, false, false);
	}

	@Test
	public void testDiffDirsToThrowExpUsingSpaceAsDirName2() throws DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(DIR1, " ", false, false, false);
	}

	@Test
	public void testDiffDirsToThrowExpUsingInvalidDir() throws DiffException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(ASDF, DIR1, false, false, false);
	}

	@Test
	public void testDiffDirsToThrowExpUsingInvalidDir2() throws DiffException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		output = app.diffTwoDir(DIR1, ASDF, false, false, false);
	}

	@Test
	public void testDiffStreamToThrowExpUsingInvalidFile() throws DiffException, FileNotFoundException {
		expected = ASDF_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		File file = new File(currentDir + FILE1_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.diffFileAndStdin(ASDF, inputStream, false, false, false);
	}

	@Test
	public void testDiffStreamToThrowExpUsingSpaceAsFileName() throws FileNotFoundException, DiffException {
		expected = FILE_NOT_FOUND;
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		File file = new File(currentDir + FILE1_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.diffFileAndStdin(" ", inputStream, false, false, false);
	}

	@Test
	public void testDiffStreamToThrowExpUsingEmptyFileName() throws DiffException, FileNotFoundException {
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
	public void testDiffFilesToRunCorrectlyUsingIdenticalFiles() throws DiffException {
		expected = "";
		output = app.diffTwoFiles(FILE1_TXT, FILE1_COPY_TXT, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffDirsToRunCorrectlyUsingIdenticalDirs() throws DiffException {
		expected = "";
		output = app.diffTwoDir(DIR1, "dir1_copy", false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingIdenticalFiles() throws DiffException, FileNotFoundException {
		expected = "";
		File file = new File(currentDir + FILE1_COPY_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.diffFileAndStdin(FILE1_TXT, inputStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingRelPath() throws DiffException {
		expected = LINE2_LINE6;
		output = app.diffTwoFiles(FILE1_TXT, FILE2_TXT, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingAbsPath() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = LINE2_LINE6;
		output = app.diffTwoFiles(file1, file2, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingDiffFilesAndFlagsS() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = LINE2_LINE6;
		output = app.diffTwoFiles(file1, file2, true, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingIdenticalFilesAndFlagsS() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_COPY_TXT;
		expected = FILES + file1 + AND + file2 + " are identical";
		output = app.diffTwoFiles(file1, file2, true, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingIdenticalFilesAndFlagsB() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
		expected = "";
		output = app.diffTwoFiles(file1, file2, false, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingDiffFilesAndFlagsQ() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_TXT;
		expected = FILES + file1 + AND + file2 + " differ";
		output = app.diffTwoFiles(file1, file2, false, false, true);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingIdenticalFilesAndFlagsQ() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_COPY_TXT;
		expected = "";
		output = app.diffTwoFiles(file1, file2, false, false, true);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingDiffFilesAndFlagsBQ() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_WITH_BLANKS;
		expected = FILES + file1 + AND + file2 + " differ";
		output = app.diffTwoFiles(file1, file2, false, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingIdenticalFilesAndFlagsBQ() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
		expected = "";
		output = app.diffTwoFiles(file1, file2, false, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingDiffFilesAndFlagsSB() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE2_WITH_BLANKS;
		expected = LINE2_LINE6;
		output = app.diffTwoFiles(file1, file2, true, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingIdenticalFilesAndFlagsSB() throws DiffException {
		String file1 = currentDir + FILE1_TXT, file2 = currentDir + FILE1_WITH_BLANKS;
		expected = FILES + file1 + AND + file2 + " are identical";
		output = app.diffTwoFiles(file1, file2, true, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingRelPath() throws FileNotFoundException, DiffException {
		expected = LINE2_LINE6;
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingAbsPath() throws FileNotFoundException, DiffException {
		expected = LINE2_LINE6;
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingEmptyStream() throws FileNotFoundException, DiffException {
		expected = "< line1" + NEWLINE + "< line2" + NEWLINE + "< line3" + NEWLINE + "< line5";
		InputStream fileStream = new FileInputStream(new File(currentDir + "emptyFile.txt"));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingDiffFilesAndFlagsS() throws FileNotFoundException, DiffException {
		expected = "< line2" + System.getProperty("line.separator") + "> line6";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingIdenticalFilesAndFlagsS() throws DiffException, FileNotFoundException {
		expected = "Files file1.txt and - are identical";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_COPY_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingIdenticalFilesAndFlagsB() throws FileNotFoundException, DiffException {
		expected = "";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingDiffFilesAndFlagsQ() throws DiffException, FileNotFoundException {
		expected = "Files file1.txt and - differ";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, true);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingIdenticalFilesAndFlagsQ() throws FileNotFoundException, DiffException {
		expected = "";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_COPY_TXT));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, false, true);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingDiffFilesAndFlagsB() throws FileNotFoundException, DiffException {
		expected = "Files file1.txt and - differ";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingIdenticalFilesAndFlagsB2()
			throws FileNotFoundException, DiffException {
		expected = "";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, false, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingDiffFilesAndFlagsSQ() throws FileNotFoundException, DiffException {
		expected = LINE2_LINE6;
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE2_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffStreamToRunCorrectlyUsingIdenticalFilesAndFlagsSB()
			throws DiffException, FileNotFoundException {
		expected = "Files file1.txt and - are identical";
		InputStream fileStream = new FileInputStream(new File(currentDir + FILE1_WITH_BLANKS));
		output = app.diffFileAndStdin(FILE1_TXT, fileStream, true, true, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffDirToRunCorrectlyUsingDiffDir() throws DiffException {
		expected = "Only in dir1: emptyFile.txt" + NEWLINE + "Only in dir2: file2.txt";
		output = app.diffTwoDir(DIR1, DIR2, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffDirToRunCorrectlyUsingAbsPath() throws DiffException {
		expected = "Only in " + currentDir + DIR1 + ": emptyFile.txt" + NEWLINE + "Only in " + currentDir
				+ "dir2: file2.txt";
		output = app.diffTwoDir(currentDir + DIR1, currentDir + DIR2, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testDiffFilesToRunCorrectlyUsingFilesWithLastLineEmpty() throws DiffException {
		expected = "> ";
		output = app.diffTwoFiles(currentDir + "file3.txt", currentDir + "file3_lastLineEmpty.txt", false, false,
				false);
		assertEquals(expected, output);
	}

	@Test
	public void testRunToRunCorrectlyUsingDirAndDir() throws DiffException {
		expected = "Only in dir1: emptyFile.txt" + NEWLINE + "Only in dir2: file2.txt";
		String[] args = { DIR1, DIR2 };
		app.run(args, null, outputStream);
		assertEquals(expected, outputStream.toString());
	}

	@Test
	public void testRunToRunCorrectlyUsingDirAndFile() throws DiffException {
		expected = "";
		String[] args = { DIR1, FILE1_TXT };
		app.run(args, null, outputStream);
		assertEquals(expected, outputStream.toString());
	}

	@Test
	public void testRunToRunCorrectlyUsingFileAndFile() throws DiffException {
		expected = "< line6" + NEWLINE + "> line2";
		String[] args = { FILE2_TXT, FILE1_TXT };
		app.run(args, null, outputStream);
		assertEquals(expected, outputStream.toString());
	}

	@Test
	public void testRunToRunCorrectlyUsingFileAndInputStream() throws DiffException, FileNotFoundException {
		expected = "< line6" + NEWLINE + "> line2";
		String[] args = { FILE2_TXT, "-" };
		app.run(args, new FileInputStream(new File(currentDir + FILE1_TXT)), outputStream);
		assertEquals(expected, outputStream.toString());
	}

	@Test
	public void testRunToThrowExpUsingNonTextFile() throws DiffException, FileNotFoundException {
		expected = "diff: doge.jpg is not a text file";
		String[] args = { FILE2_TXT, "doge.jpg" };
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		app.run(args, null, outputStream);
		assertEquals(expected, outputStream.toString());
	}

	@Test
	public void testRunToThrowExpUsingDirAndInputStream() throws DiffException, FileNotFoundException {
		expected = "diff: cannot compare directory and inputstream";
		String[] args = { DIR1, "-" };
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		app.run(args, new FileInputStream(new File(currentDir + FILE1_TXT)), outputStream);
	}

	@Test
	public void testRunToRunCorrectlyUsingMultiFlags() throws DiffException, FileNotFoundException {
		expected = "Files file1.txt and file1_copy.txt are identical";
		String[] args = { FILE1_TXT, FILE1_COPY_TXT, "-sB-q" };
		app.run(args, new FileInputStream(new File(currentDir + FILE1_TXT)), outputStream);
		assertEquals(expected, outputStream.toString());
	}

	@Test
	public void testRunToThrowExpUsingInvalidFlags() throws DiffException, FileNotFoundException {
		expected = "diff: Invalid flags";
		String[] args = { FILE1_TXT, FILE1_COPY_TXT, "-a" };
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		app.run(args, new FileInputStream(new File(currentDir + FILE1_TXT)), outputStream);
	}

	@Test
	public void testRunToThrowExpUsingInvalidFlags2() throws DiffException, FileNotFoundException {
		expected = "diff: Invalid flags";
		String[] args = { FILE1_TXT, FILE1_COPY_TXT, "--s" };
		thrown.expect(DiffException.class);
		thrown.expectMessage(expected);
		app.run(args, new FileInputStream(new File(currentDir + FILE1_TXT)), outputStream);
	}
}
