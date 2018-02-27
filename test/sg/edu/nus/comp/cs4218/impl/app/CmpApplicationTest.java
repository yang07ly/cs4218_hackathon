package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;

public class CmpApplicationTest {
	private static final String FILE1_TXT = "file1.txt";
	private static final String FILES_DIFFER = "Files differ\n";
	private static final String FILE2_TXT = "file2.txt";
	private static final String CMP_INVALID_FLAGS = "cmp: Invalid flags";
	private static final String EMPTY_FILE_TXT = "emptyFile.txt";
	private static final String FILE3_TXT = "file3.txt";
	CmpApplication app;
	OutputStream outputStream;
	String expected, output;

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
	public void testExtraOperand() {
		expected = "cmp: Can't compare more than 2 sources";
		String[] args = { FILE1_TXT, FILE2_TXT, "-cl", "file3.txt" };
		try {
			app.run(args, System.in, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testInvalidFlag() {
		expected = CMP_INVALID_FLAGS;
		String[] args = { FILE1_TXT, FILE2_TXT, "-a" };
		try {
			app.run(args, System.in, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testInvalidFlag2() {
		expected = CMP_INVALID_FLAGS;
		String[] args = { FILE1_TXT, FILE2_TXT, "-c-" };
		try {
			app.run(args, System.in, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testCombineFlags() {
		expected = FILES_DIFFER;
		String[] args = { FILE1_TXT, FILE2_TXT, "-csl" };
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testCombineFlags2() {
		expected = FILES_DIFFER;
		String[] args = { FILE1_TXT, FILE2_TXT, "-c-s-l" };
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testCombineFlags3() {
		expected = FILES_DIFFER;
		String[] args = { FILE1_TXT, FILE2_TXT, "-c", "-s", "-l" };
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testHyphenFlag() {
		expected = "cmp: Can't compare more than 2 sources";
		String[] args = { FILE1_TXT, FILE2_TXT, "-" };
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDoubleHyphen() {
		expected = CMP_INVALID_FLAGS;
		String[] args = { FILE1_TXT, FILE2_TXT, "--" };
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDoubleHyphen2() {
		expected = CMP_INVALID_FLAGS;
		String[] args = { FILE1_TXT, FILE2_TXT, "--s" };
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileEmptyFileA() {
		expected = "cmp: can't have empty argument";
		try {
			output = app.cmpTwoFiles("", FILE2_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileEmptyFileB() {
		expected = "cmp: can't have empty argument";
		try {
			output = app.cmpTwoFiles(FILE2_TXT, "", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileSpaceFileA() {
		expected = "cmp: ' ': No such file or directory";
		try {
			output = app.cmpTwoFiles(" ", FILE2_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileSpaceFileB() {
		expected = "cmp: ' ': No such file or directory";
		try {
			output = app.cmpTwoFiles(FILE2_TXT, " ", false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileDirectoryFileA() {
		Path path = Paths.get(Environment.currentDirectory);
		expected = "cmp: '" + Paths.get(Environment.currentDirectory) + "': this is a directory";
		try {
			output = app.cmpTwoFiles(path.toString(), FILE2_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileDirectoryFileB() {
		Path path = Paths.get(Environment.currentDirectory);
		expected = "cmp: '" + Paths.get(Environment.currentDirectory) + "': this is a directory";
		try {
			output = app.cmpTwoFiles(FILE2_TXT, path.toString(), false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileNonExistentialFileA() {
		expected = "cmp: 'file3.txt': No such file or directory";
		try {
			output = app.cmpTwoFiles(FILE3_TXT, FILE1_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileNonExistentialFileb() {
		expected = "cmp: 'file4.txt': No such file or directory";
		try {
			output = app.cmpTwoFiles("file4.txt", FILE1_TXT, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileFileRelRelTrueTrueTrueNoDiff() {
		try {
			output = app.cmpTwoFiles(FILE1_TXT, "file1_copy.txt", true, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals("", output);
	}

	@Test
	public void testFileFileRelRelTrueTrueTrue() {
		try {
			output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, true, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(FILES_DIFFER, output);
	}

	@Test
	public void testFileFileRelRelTrueFalseFalse() {
		try {
			output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		if (System.getProperty("os.name").length() > 8) {
			assertEquals("file1.txt file2.txt differ: byte 10, line 2 is 151 i 154 l\n", output);
		} else {
			assertEquals("file1.txt file2.txt differ: byte 9, line 2 is 151 i 154 l\n", output);
		}
	}

	@Test
	public void testFileFileRelRelFalseTrueFalse() {
		try {
			output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, false, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(FILES_DIFFER, output);
	}

	@Test
	public void testFileFileRelRelFalseFalseTrue() {
		try {
			output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		if (System.getProperty("os.name").length() > 8) {
			assertEquals("10 151 154\n11 154 151\n", output);
		} else {
			assertEquals("9 151 154\n10 154 151\n", output);
		}
	}

	@Test
	public void testFileEmptyFileRelRelFalseFalseTrue() {
		try {
			output = app.cmpTwoFiles(FILE1_TXT, EMPTY_FILE_TXT, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals("cmp: EOF on emptyFile.txt\n", output);
	}

	@Test
	public void testFileEmptyFileRelRelTrueFalseTrue() {
		try {
			output = app.cmpTwoFiles(FILE1_TXT, FILE2_TXT, true, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		if (System.getProperty("os.name").length() > 8) {
			assertEquals("10 151 i 154 l\n11 154 l 151 i\n", output);
		} else {
			assertEquals("9 151 i 154 l\n10 154 l 151 i\n", output);
		}
	}

	@Test
	public void testFilePathFileRelRelFalseFalseTrue() {
		Path path = Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT);
		try {
			output = app.cmpTwoFiles(path.toString(), FILE2_TXT, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		if (System.getProperty("os.name").length() > 8) {
			assertEquals("10 151 154\n11 154 151\n", output);
		} else {
			assertEquals("9 151 154\n10 154 151\n", output);
		}
	}

	/*
	 * Test file and stream
	 */

	@Test
	public void testFileStreamEmptyFileA() {
		expected = "cmp: can't have empty argument";
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin("", inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamSpaceFileA() {
		expected = "cmp: ' ': No such file or directory";
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(" ", inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamDirectoryFileA() {
		Path path = Paths.get(Environment.currentDirectory);
		expected = "cmp: '" + path.toString() + "': this is a directory";
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(path.toString(), inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamNonExistentialFileA() {
		expected = "cmp: 'file3.txt': No such file or directory";
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE3_TXT, inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamEmptyStreamFalseFalseFalse() {
		expected = "file1.txt - differ: byte 1, line 1\n";
		try {
			File file = new File(Environment.currentDirectory + File.separator + EMPTY_FILE_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE1_TXT, inputStream, false, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamTrueTrueTrueNoDiff() {
		expected = "";
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE1_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE1_TXT, inputStream, true, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testFileStreamRelRelTrueTrueTrue() {
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE1_TXT, inputStream, true, true, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(FILES_DIFFER, output);
	}

	@Test
	public void testFileStreamRelRelTrueFalseFalse() {
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE1_TXT, inputStream, true, false, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		if (System.getProperty("os.name").length() > 8) {
			assertEquals("file1.txt - differ: byte 10, line 2 is 151 i 154 l\n", output);
		} else {
			assertEquals("file1.txt - differ: byte 9, line 2 is 151 i 154 l\n", output);
		}
	}

	@Test
	public void testFileStreamRelRelFalseTrueFalse() {
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE1_TXT, inputStream, false, true, false);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(FILES_DIFFER, output);
	}

	@Test
	public void testFileStreamRelRelFalseFalseTrue() {
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE1_TXT, inputStream, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		if (System.getProperty("os.name").length() > 8) {
			assertEquals("10 151 154\n11 154 151\n", output);
		} else {
			assertEquals("9 151 154\n10 154 151\n", output);
		}
	}

	@Test
	public void testFileStreamFalseFalseTrue() {
		try {
			File file = new File(Environment.currentDirectory + File.separator + EMPTY_FILE_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE1_TXT, inputStream, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals("cmp: EOF on -\n", output);
	}

	@Test
	public void testFilePathStreamFalseFalseTrue() {
		Path path = Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT);
		try {
			File file = new File(Environment.currentDirectory + File.separator + EMPTY_FILE_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(path.toString(), inputStream, false, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals("cmp: EOF on -\n", output);
	}

	@Test
	public void testFileStreamTrueFalseTrue() {
		try {
			File file = new File(Environment.currentDirectory + File.separator + FILE2_TXT);
			InputStream inputStream = new FileInputStream(file);
			output = app.cmpFileAndStdin(FILE1_TXT, inputStream, true, false, true);
		} catch (Exception e) {
			output = e.getMessage();
		}
		if (System.getProperty("os.name").length() > 8) {
			assertEquals("10 151 i 154 l\n11 154 l 151 i\n", output);
		} else {
			assertEquals("9 151 i 154 l\n10 154 l 151 i\n", output);
		}
	}

}
