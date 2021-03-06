package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.CmpException;

public class CmpApplicationTest {
	private static final String OS_NAME = "os.name";
	private static final String FILE1_TXT = "file1.txt";
	private static final String FILES_DIFFER = "Files differ";
	private static final String FILE2_TXT = "file2.txt";
	private static final String CMP_INVALID_FLAGS = "cmp: Invalid flags";
	private static final String EMPTY_FILE_TXT = "emptyFile.txt";
	private static final String FILE3_TXT = "file3.txt";
	private static final String NEWLINE = System.lineSeparator();
	CmpApplication app;
	OutputStream outputStream;
	String expected, output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "cmp_test_system";
		app = new CmpApplication();
		outputStream = new ByteArrayOutputStream();
		output = "";
		expected = "";
	}

	@Test
	public void testCmpFilesToThrowExpUsingMoreThanTwoFiles() throws CmpException {
		expected = "cmp: requires 2 files to be specified";
		String[] args = { FILE1_TXT, FILE2_TXT, "-cl", "file3.txt" };

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, System.in, outputStream);
	}

	@Test
	public void testCmpFilesToThrowExpUsingInvalidFlags() throws CmpException {
		expected = CMP_INVALID_FLAGS;
		String[] args = { FILE1_TXT, FILE2_TXT, "-a" };

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, System.in, outputStream);
	}

	@Test
	public void testCmpFilesToThrowExpUsingInvalidFlags2() throws CmpException {
		expected = CMP_INVALID_FLAGS;
		String[] args = { FILE1_TXT, FILE2_TXT, "-c-" };

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, System.in, outputStream);
	}

	@Test
	public void testCmpFilesToThrowExpUsingInvalidFlags3() throws CmpException {
		expected = CMP_INVALID_FLAGS;
		String[] args = { FILE1_TXT, FILE2_TXT, "--" };

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, System.in, outputStream);
	}

	@Test
	public void testCmpFilesToThrowExpUsingInvalidFlags4() throws CmpException {
		expected = CMP_INVALID_FLAGS;
		String[] args = { FILE1_TXT, FILE2_TXT, "--s" };

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, System.in, outputStream);
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingMultiFlags() throws CmpException {
		expected = FILES_DIFFER;
		String[] args = { FILE1_TXT, FILE2_TXT, "-csl" };
		app.run(args, System.in, outputStream);
		output = outputStream.toString();
		assertEquals(expected, output);
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingMultiFlags2() throws CmpException {
		expected = FILES_DIFFER;
		String[] args = { FILE1_TXT, FILE2_TXT, "-c-s-l" };
		app.run(args, System.in, outputStream);
		output = outputStream.toString();
		assertEquals(expected, output);
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingMultiFlags3() throws CmpException {
		expected = FILES_DIFFER;
		String[] args = { FILE1_TXT, FILE2_TXT, "-c", "-s", "-l" };
		app.run(args, System.in, outputStream);
		output = outputStream.toString();
		assertEquals(expected, output);
	}

	@Test
	public void testCmpFilesToThrowExpUsingTwoFilesAndInputStream() throws CmpException {
		expected = "cmp: requires 2 files to be specified";
		String[] args = { FILE1_TXT, FILE2_TXT, "-" };

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, System.in, outputStream);
	}

	@Test
	public void testCmpFilesToThrowExpUsingEmptyFileName() throws CmpException {
		expected = "cmp: : No such file or directory";

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		output = app.cmpTwoFiles("", FILE2_TXT, false, false, false);
	}

	@Test
	public void testCmpFilesToThrowExpUsingEmptyFileName2() throws CmpException {
		expected = "cmp: : No such file or directory";

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		output = app.cmpTwoFiles(FILE2_TXT, "", false, false, false);
	}

	@Test
	public void testCmpFilesToThrowExpUsingSpaceAsFileName() throws CmpException {
		expected = "cmp:  : No such file or directory";

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		output = app.cmpTwoFiles(" ", FILE2_TXT, false, false, false);
	}

	@Test
	public void testCmpFilesToThrowExpUsingSpaceAsFileName2() throws CmpException {
		expected = "cmp:  : No such file or directory";

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		output = app.cmpTwoFiles(FILE2_TXT, " ", false, false, false);
	}

	@Test
	public void testCmpFilesToThrowExpUsingDir() throws CmpException {
		Path path = Paths.get(Environment.currentDirectory);
		expected = "cmp: " + Paths.get(Environment.currentDirectory) + ": Is a directory";

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		output = app.cmpTwoFiles(path.toString(), FILE2_TXT, false, false, false);
	}

	@Test
	public void testCmpFilesToThrowExpUsingDir2() throws CmpException {
		Path path = Paths.get(Environment.currentDirectory);
		expected = "cmp: " + Paths.get(Environment.currentDirectory) + ": Is a directory";

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		output = app.cmpTwoFiles(FILE2_TXT, path.toString(), false, false, false);
	}

	@Test
	public void testCmpFilesToThrowExpUsingInvalidFile() throws CmpException {
		expected = "cmp: file3.txt: No such file or directory";
		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		output = app.cmpTwoFiles(FILE3_TXT, FILE1_TXT, false, false, false);
	}

	@Test
	public void testCmpFilesToThrowExpUsingInvalidFile2() throws CmpException {
		expected = "cmp: file4.txt: No such file or directory";

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		output = app.cmpTwoFiles("file4.txt", FILE1_TXT, false, false, false);
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingIdenticalFilesAndFlagsCSL() throws CmpException {
		output = app.cmpTwoFiles(FILE1_TXT, "file1_copy.txt", true, true, true);
		assertEquals("", output);
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingDiffFilesAndFlagsCSL() throws CmpException {
		output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, true, true, true);
		assertEquals(FILES_DIFFER, output);
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingDiffFilesAndFlagsC() throws CmpException {
		output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, true, false, false);
		if (System.getProperty(OS_NAME).length() > 8) {
			assertEquals("file1.txt file2.txt differ: char 10, line 2 is 151 i 154 l", output);
		} else {
			assertEquals("file1.txt file2.txt differ: char 9, line 2 is 151 i 154 l", output);
		}
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingDiffFilesAndFlagsS() throws CmpException {
		output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, false, true, false);
		assertEquals(FILES_DIFFER, output);
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingDiffFilesAndFlagsL() throws CmpException {
		output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, false, false, true);
		if (System.getProperty(OS_NAME).length() > 8) {
			assertEquals("10 151 154" + NEWLINE + "11 154 151", output);
		} else {
			assertEquals("9 151 154" + NEWLINE + "10 154 151", output);
		}
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingEmptyFileAndFlagsL() throws CmpException {
		output = app.cmpTwoFiles(FILE1_TXT, EMPTY_FILE_TXT, false, false, true);
		assertEquals("cmp: EOF on emptyFile.txt", output);
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingDiffFilesAndFlagsCL() throws CmpException {
		output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, true, false, true);
		if (System.getProperty(OS_NAME).length() > 8) {
			assertEquals("10 151 i 154 l" + NEWLINE + "11 154 l 151 i", output);
		} else {
			assertEquals("9 151 i 154 l" + NEWLINE + "10 154 l 151 i", output);
		}
	}

	@Test
	public void testCmpFilesToRunCorrectlyUsingAbsPathAndFlagsL() throws CmpException {
		Path path = Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT);
		output = app.cmpTwoFiles(path.toString(), FILE2_TXT, false, false, true);
		if (System.getProperty(OS_NAME).length() > 8) {
			assertEquals("10 151 154" + NEWLINE + "11 154 151", output);
		} else {
			assertEquals("9 151 154" + NEWLINE + "10 154 151", output);
		}
	}

	/*
	 * Test file and stream
	 */

	@Test
	public void testCmpStreamToThrowExpUsingEmptyFileName() throws CmpException, IOException {
		expected = "cmp: : No such file or directory";

		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin("", inputStream, false, false, false);
	}

	@Test
	public void testCmpStreamToThrowExpUsingSpaceAsFileName() throws CmpException, IOException {
		expected = "cmp:  : No such file or directory";
		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(" ", inputStream, false, false, false);
	}

	@Test
	public void testCmpStreamToThrowExpUsingDir() throws CmpException, IOException {
		Path path = Paths.get(Environment.currentDirectory);
		expected = "cmp: " + path.toString() + ": Is a directory";
		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(path.toString(), inputStream, false, false, false);
	}

	@Test
	public void testCmpStreamToThrowExpUsingInvalidFile() throws CmpException, IOException {
		expected = "cmp: file3.txt: No such file or directory";
		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE3_TXT, inputStream, false, false, false);
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingEmptyInputStream() throws CmpException, IOException {
		expected = "file1.txt - differ: char 1, line 1";
		File file = new File(Environment.currentDirectory + File.separator + EMPTY_FILE_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE1_TXT, inputStream, false, false, false);
		assertEquals(expected, output);
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingIdenticalFilesAndFlagsCSL() throws CmpException, IOException {
		expected = "";
		File file = new File(Environment.currentDirectory + File.separator + FILE1_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE1_TXT, inputStream, true, true, true);
		assertEquals(expected, output);
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingDiffFilesAndFlagsCSL() throws CmpException, IOException {
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE1_TXT, inputStream, true, true, true);
		assertEquals(FILES_DIFFER, output);
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingDiffFilesAndFlagsC() throws CmpException, IOException {
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE1_TXT, inputStream, true, false, false);
		if (System.getProperty(OS_NAME).length() > 8) {
			assertEquals("file1.txt - differ: char 10, line 2 is 151 i 154 l", output);
		} else {
			assertEquals("file1.txt - differ: char 9, line 2 is 151 i 154 l", output);
		}
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingDiffFilesAndFlagsS() throws CmpException, IOException {
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE1_TXT, inputStream, false, true, false);
		assertEquals(FILES_DIFFER, output);
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingDiffFilesAndFlagsL() throws CmpException, IOException {
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE1_TXT, inputStream, false, false, true);
		if (System.getProperty(OS_NAME).length() > 8) {
			assertEquals("10 151 154" + NEWLINE + "11 154 151", output);
		} else {
			assertEquals("9 151 154" + NEWLINE + "10 154 151", output);
		}
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingEmptyStreamAndFlagsL() throws CmpException, IOException {
		File file = new File(Environment.currentDirectory + File.separator + EMPTY_FILE_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE1_TXT, inputStream, false, false, true);
		assertEquals("cmp: EOF on -", output);
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingAbsPathAndFlagsL() throws CmpException, IOException {
		Path path = Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT);
		File file = new File(Environment.currentDirectory + File.separator + EMPTY_FILE_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(path.toString(), inputStream, false, false, true);
		assertEquals("cmp: EOF on -", output);
	}

	@Test
	public void testCmpStreamToRunCorrectlyUsingDiffFilesAndFlagsCL() throws CmpException, IOException {
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		output = app.cmpFileAndStdin(FILE1_TXT, inputStream, true, false, true);
		if (System.getProperty(OS_NAME).length() > 8) {
			assertEquals("10 151 i 154 l" + NEWLINE + "11 154 l 151 i", output);
		} else {
			assertEquals("9 151 i 154 l" + NEWLINE + "10 154 l 151 i", output);
		}
	}

	@Test
	public void testRunToThrowExpUsingNonTextFile() throws CmpException, IOException {
		String[] args = { "doge.jpg", FILE1_TXT };
		expected = "cmp: doge.jpg is not a text file";
		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, null, outputStream);
	}

	@Test
	public void testRunToThrowExpUsingEmptyArgs() throws CmpException, IOException {
		String[] args = {};
		expected = "cmp: requires 2 files to be specified";
		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, null, outputStream);
	}

	@Test
	public void testRunToThrowExpUsingEmptyFileName() throws CmpException, IOException {
		String[] args = { FILE1_TXT, "" };
		expected = "cmp: '': No such file or directory";
		thrown.expect(CmpException.class);
		thrown.expectMessage(expected);
		app.run(args, null, outputStream);
	}

	@Test
	public void testRunToRunCorrectlyUsingFileAndInputStream() throws CmpException, IOException {
		String[] args = { FILE1_TXT, "-", "-cl" };
		File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
		InputStream inputStream = new FileInputStream(file);
		app.run(args, inputStream, outputStream);
		if (System.getProperty(OS_NAME).length() > 8) {
			assertEquals("10 151 i 154 l" + NEWLINE + "11 154 l 151 i", outputStream.toString());
		} else {
			assertEquals("9 151 i 154 l" + NEWLINE + "10 154 l 151 i", outputStream.toString());
		}
	}

}
