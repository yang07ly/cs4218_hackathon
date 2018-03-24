package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.SplitException;

public class SplitApplicationTest {
	private static final String FILE1_TXT = "file1.txt";
	private static final String XAA = "xaa";
	private static final String FILE2_TXT = "file2.txt";
	private static final String XAC = "xac";
	private static final String XAB = "xab";
	private static final String OUTPUT2 = "output";
	private static final String QWERTY = "qwerty";
	private static final String ZXCVBN = "zxcvbn";
	private static final String ASDFGH = "asdfgh";
	SplitApplication app;
	InputStream inputStream = System.in;
	OutputStream outputStream;
	String expected, output, currentDir;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "split_test_system";
		currentDir = Environment.currentDirectory + File.separator;
		app = new SplitApplication();
		outputStream = new ByteArrayOutputStream();
		output = "";
		expected = "";
	}

	@Test
	public void testRunToThrowExpUsingNullArgs() throws SplitException {
		expected = "split: Null Pointer Exception";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.run(null, null, outputStream);
	}

	@Test
	public void testRunToThrowExpUsingInvalidFlags() throws SplitException {
		String[] splitArgs = { FILE1_TXT, "-s", "-c" };
		expected = "split: -s: invalid flag";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.run(splitArgs, System.in, outputStream);
	}

	@Test
	public void testRunToThrowExpUsingNegativeLines() throws SplitException {
		String[] splitArgs = { FILE1_TXT, "-l", "-2" };
		expected = "split: -2: invalid number of lines";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.run(splitArgs, System.in, outputStream);
	}

	@Test
	public void testRunToThrowExpWhenSplitMoreThanOneWay() throws SplitException {
		String[] splitArgs = { FILE1_TXT, "-l", "-2", "-b", "3" };
		expected = "split: cannot split in more than one way";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.run(splitArgs, System.in, outputStream);
	}

	@Test
	public void testRunToThrowExpWhenSplitMoreThanOneWay2() throws SplitException {
		String[] splitArgs = { FILE1_TXT, "-b", "3", "-l", "-2" };
		expected = "split: cannot split in more than one way";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.run(splitArgs, System.in, outputStream);
	}

	@Test
	public void testSplitFileLinesToThrowExpUsingEmptyFileName() throws SplitException {
		expected = "split: : No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByLines("", "", 4);
	}

	@Test
	public void testSplitFileLinesToThrowExpUsingSpaceAsFileName() throws SplitException {
		expected = "split:  : No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByLines(" ", "", 4);
	}

	@Test
	public void testSplitFileLinesToThrowExpUsingInvalidFile() throws SplitException {
		expected = "split: asdf: No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByLines("asdf", "", 4);
	}

	@Test
	public void testSplitFileLinesToThrowExpUsingInvalidFile2() throws SplitException {
		expected = "split: asdf.txt: No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByLines("asdf.txt", "", 4);
	}

	@Test
	public void testSplitFileLinesToThrowExpUsingInvalidFilePath() throws SplitException {
		String path = currentDir + "asddfaw" + File.separator;
		expected = "split: '" + path + "': No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByLines(FILE1_TXT, path, 2);
	}

	@Test
	public void testSplitFileLinesToThrowExpUsingInvalidNumberOfLines() throws SplitException {
		expected = "split: 0: invalid number of lines";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByLines(FILE1_TXT, "", 0);
	}

	/*
	 * These tests are all valid test cases for splitFileByLines
	 */
	@Test
	public void testRunToRunCorrectlyUsingDefaultLines() throws IOException, SplitException {
		FileInputStream fileStream = new FileInputStream(new File(currentDir + FILE1_TXT));
		app.run(null, fileStream, outputStream);
		output = outputStream.toString();

		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
		assertEquals(ASDFGH, reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		assertEquals(ZXCVBN, reader.readLine());
		reader.close();
		Files.delete(Paths.get(currentDir + XAA));
	}

	@Test
	public void testRunToRunCorrectlyUsingStreamAndBytes() throws IOException, SplitException {
		String[] myArgs = { "-b", "6" };
		FileInputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
		app.run(myArgs, fileStream, outputStream);
		output = outputStream.toString();

		BufferedReader fileReader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
		char[] buffer = new char[10];
		int count = fileReader.read(buffer, 0, 10);
		assertEquals(ASDFGH, new String(buffer, 0, count));
		fileReader.close();
		Files.delete(Paths.get(currentDir + XAA));
	}

	@Test
	public void testRunToRunCorrectlyUsingFileAndLines() throws IOException, SplitException {
		String[] myArgs = { currentDir + FILE1_TXT, "-l", "6" };
		app.run(myArgs, null, outputStream);
		output = outputStream.toString();

		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
		assertEquals(ASDFGH, reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		assertEquals(ZXCVBN, reader.readLine());
		reader.close();
		Files.delete(Paths.get(currentDir + XAA));
	}

	@Test
	public void testRunToRunCorrectlyUsingFileAndBytes() throws IOException, SplitException {
		String[] myArgs = { currentDir + FILE2_TXT, "-b", "6" };
		app.run(myArgs, null, outputStream);
		output = outputStream.toString();

		BufferedReader fileReader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
		char[] buffer = new char[10];
		int count = fileReader.read(buffer, 0, 10);
		assertEquals(ASDFGH, new String(buffer, 0, count));
		fileReader.close();
		Files.delete(Paths.get(currentDir + XAA));
	}

	@Test
	public void testSplitFileLinesToRunCorrectlyUsingValidLines() throws IOException, SplitException {
		app.splitFileByLines(FILE1_TXT, "", 2);

		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + "aa")));
		assertEquals(ASDFGH, reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(currentDir + "ab")));
		assertEquals(ZXCVBN, reader.readLine());
		reader.close();
		Files.delete(Paths.get(currentDir + "aa"));
		Files.delete(Paths.get(currentDir + "ab"));
	}

	@Test
	public void testSplitFileLinesToRunCorrectlyUsingRelPath() throws IOException, SplitException {
		Path path = Paths.get(Environment.currentDirectory).resolve(OUTPUT2);
		String myPath = path.toString() + File.separator;
		Path outputFile = Paths.get(myPath + "aa");
		Path outputFileB = Paths.get(myPath + "ab");
		app.splitFileByLines(FILE1_TXT, path.toString() + File.separator, 2);

		BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile.toString())));
		assertEquals(ASDFGH, reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(outputFileB.toString())));
		assertEquals(ZXCVBN, reader.readLine());
		reader.close();
		Files.delete(outputFile);
		Files.delete(outputFileB);
	}

	@Test
	public void testSplitFileLinesToRunCorrectlyUsingDefaultPrefix() throws IOException, SplitException {
		app.splitFileByLines(FILE1_TXT, null, 2);

		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
		assertEquals(ASDFGH, reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(currentDir + XAB)));
		assertEquals(ZXCVBN, reader.readLine());
		reader.close();
		Files.delete(Paths.get(currentDir + XAA));
		Files.delete(Paths.get(currentDir + XAB));
	}

	@Test
	public void testSplitFileLinesToRunCorrectlyUsingAbsPath() throws IOException, SplitException {
		Path path = Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT);
		app.splitFileByLines(path.toString(), "", 2);

		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + "aa")));
		assertEquals(ASDFGH, reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(currentDir + "ab")));
		assertEquals(ZXCVBN, reader.readLine());
		reader.close();
		Files.delete(Paths.get(currentDir + "aa"));
		Files.delete(Paths.get(currentDir + "ab"));
	}

	@Test
	public void testSplitFileLinesToRunCorrectlyUsingPathInPrefix() throws IOException, SplitException {
		Path path = Paths.get(Environment.currentDirectory).resolve(OUTPUT2);
		Path source = Paths.get(Environment.currentDirectory);
		String myPath = path.toString() + File.separator;
		Path outputFile = Paths.get(myPath + "aa");
		Path outputFileB = Paths.get(myPath + "ab");
		app.splitFileByLines(source.toString() + File.separator + FILE1_TXT, path.toString() + File.separator, 2);

		BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile.toString())));
		assertEquals(ASDFGH, reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(outputFileB.toString())));
		assertEquals(ZXCVBN, reader.readLine());
		reader.close();
		Files.delete(outputFile);
		Files.delete(outputFileB);
	}

	@Test
	public void testSplitFileLinesToRunCorrectlyUsingDefaultPrefix2() throws IOException, SplitException {
		Path source = Paths.get(Environment.currentDirectory);
		app.splitFileByLines(source.toString() + File.separator + FILE1_TXT, null, 2);

		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
		assertEquals(ASDFGH, reader.readLine());
		assertEquals(QWERTY, reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(currentDir + XAB)));
		assertEquals(ZXCVBN, reader.readLine());
		reader.close();
		Files.delete(Paths.get(currentDir + XAA));
		Files.delete(Paths.get(currentDir + XAB));
	}
	/*
	 * These test cases are for splitFileByBytes
	 */

	@Test
	public void testSplitFileBytesToThrowExpUsingEmptyFileName() throws SplitException {
		expected = "split: : No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes("", "", "1");
	}

	@Test
	public void testSplitFileBytesToThrowExpUsingSpaceAsFileName() throws SplitException {
		expected = "split:  : No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes(" ", "", "1");
	}

	@Test
	public void testSplitFileBytesToThrowExpUsingInvalidFile() throws SplitException {
		expected = "split: asdf: No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes("asdf", "", "1");
	}

	@Test
	public void testSplitFileBytesToThrowExpUsingInvalidFile2() throws SplitException {
		expected = "split: asdf.txt: No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes("asdf.txt", "", "4");
	}

	@Test
	public void testSplitFileBytesToThrowExpUsingInvalidPathInPrefix() throws SplitException {
		String path = currentDir + "asddfaw" + File.separator;
		expected = "split: '" + path + "': No such file or directory";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes(FILE1_TXT, path, "2");
	}

	@Test
	public void testSplitFileBytesToThrowExpUsingInvalidBytes() throws SplitException {
		expected = "split: 2s: invalid number of bytes";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes(FILE1_TXT, "", "2s");
	}

	@Test
	public void testSplitFileBytesToThrowExpUsingEmptyBytesString() throws SplitException {
		expected = "split: : invalid number of bytes";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes(FILE1_TXT, "", "");
	}

	@Test
	public void testSplitFileBytesToThrowExpUsingSpaceAsByteString() throws SplitException {
		expected = "split:  : invalid number of bytes";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes(FILE1_TXT, "", " ");
	}

	@Test
	public void testSplitFileBytesToThrowExpUsingZeroBytes() throws SplitException {
		expected = "split: 0: invalid number of bytes";
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.splitFileByBytes(FILE1_TXT, "", "0");
	}

	@Test
	public void testSplitFileBytesToRunCorrectlyUsingValidBytes() throws IOException, SplitException {
		app.splitFileByBytes(FILE2_TXT, "", "2");
		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + "aa")));
		assertEquals("as", reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(currentDir + "ab")));
		assertEquals("df", reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(currentDir + "ac")));
		assertEquals("gh", reader.readLine());
		reader.close();
		Files.delete(Paths.get(currentDir + "aa"));
		Files.delete(Paths.get(currentDir + "ab"));
		Files.delete(Paths.get(currentDir + "ac"));
	}

	@Test
	public void testSplitFileBytesToRunCorrectlyUsingValidBytesWithB() throws IOException, SplitException {
		app.splitFileByBytes("512bytes.txt", "", "1b");
		char[] buffer = new char[512];
		String expected = "";
		for (int i = 0; i < 512; i++) {
			expected += "a";
		}
		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + "aa")));
		reader.read(buffer, 0, 512);
		assertEquals(expected, new String(buffer));
		reader.close();

		buffer = new char[512];
		reader = new BufferedReader(new FileReader(new File(currentDir + "ab")));
		int count = reader.read(buffer, 0, 512);
		reader.close();
		assertEquals("a", new String(buffer, 0, count));
		Files.delete(Paths.get(currentDir + "aa"));
		Files.delete(Paths.get(currentDir + "ab"));
	}

	@Test
	public void testSplitFileBytesToRunCorrectlyUsingKiloBytes() throws IOException, SplitException {
		Path path = Paths.get(Environment.currentDirectory + File.separator + "1024bytes.txt");
		app.splitFileByBytes(path.toString(), "", "1k");
		char[] buffer = new char[1024];
		String expected = "";
		for (int i = 0; i < 1024; i++) {
			expected += "a";
		}
		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + "aa")));
		reader.read(buffer, 0, 1024);
		assertEquals(expected, new String(buffer));
		reader.close();

		buffer = new char[1024];
		reader = new BufferedReader(new FileReader(new File(currentDir + "ab")));
		int count = reader.read(buffer, 0, 1024);
		reader.close();
		assertEquals("a", new String(buffer, 0, count));
		Files.delete(Paths.get(currentDir + "aa"));
		Files.delete(Paths.get(currentDir + "ab"));
	}

	@Test
	public void testSplitFileBytesToRunCorrectlyUsingMegaBytes() throws IOException, SplitException {
		app.splitFileByBytes("megabytes.txt", "", "1m");
		char[] buffer = new char[1048576];
		StringBuilder stringBuilder = new StringBuilder(1048576);
		for (int i = 0; i < 1048576; i++) {
			stringBuilder.append('a');
		}
		String expected = new String(stringBuilder);
		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + "aa")));
		reader.read(buffer, 0, 1048576);
		assertEquals(expected, new String(buffer));
		reader.close();

		buffer = new char[1];
		reader = new BufferedReader(new FileReader(new File(currentDir + "ab")));
		reader.read(buffer, 0, 1);
		reader.close();
		assertEquals("a", new String(buffer));

		Files.delete(Paths.get(currentDir + "aa"));
		Files.delete(Paths.get(currentDir + "ab"));
	}

	@Test
	public void testSplitFileBytesToRunCorrectlyUsingRelPath() throws IOException, SplitException {
		Path path = Paths.get(Environment.currentDirectory).resolve(OUTPUT2);
		String myPath = path.toString() + File.separator;
		Path outputFile = Paths.get(myPath + "aa");
		Path outputFileB = Paths.get(myPath + "ab");
		Path outputFileC = Paths.get(myPath + "ac");

		app.splitFileByBytes(FILE2_TXT, myPath, "2");
		BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile.toString())));
		assertEquals("as", reader.readLine());
		reader.close();

		reader = new BufferedReader(new FileReader(new File(outputFileB.toString())));
		assertEquals("df", reader.readLine());
		reader.close();

		reader = new BufferedReader(new FileReader(new File(outputFileC.toString())));
		assertEquals("gh", reader.readLine());
		reader.close();

		Files.delete(Paths.get(outputFile.toString()));
		Files.delete(Paths.get(outputFileB.toString()));
		Files.delete(Paths.get(outputFileC.toString()));
	}

	@Test
	public void testSplitFileBytesToRunCorrectlyUsingRelPathWithValidBytes() throws IOException, SplitException {
		Path path = Paths.get(Environment.currentDirectory).resolve(OUTPUT2);
		String myPath = path.toString() + File.separator;
		Path outputFile = Paths.get(myPath + "aa");
		Path outputFileB = Paths.get(myPath + "ab");
		app.splitFileByBytes("512bytes.txt", myPath, "1b");
		char[] buffer = new char[512];
		String expected = "";
		for (int i = 0; i < 512; i++) {
			expected += "a";
		}
		BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile.toString())));
		reader.read(buffer, 0, 512);
		assertEquals(expected, new String(buffer));
		reader.close();

		buffer = new char[1];
		reader = new BufferedReader(new FileReader(new File(outputFileB.toString())));
		reader.read(buffer, 0, 1);
		reader.close();
		Files.delete(Paths.get(outputFile.toString()));
		Files.delete(Paths.get(outputFileB.toString()));
	}

	@Test
	public void testSplitFileBytesToRunCorrectlyUsingDefaultPrefix() throws IOException, SplitException {
		Path path = Paths.get(Environment.currentDirectory + File.separator + FILE2_TXT);
		app.splitFileByBytes(path.toString(), null, "2");
		BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
		assertEquals("as", reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(currentDir + XAB)));
		assertEquals("df", reader.readLine());
		reader.close();
		reader = new BufferedReader(new FileReader(new File(currentDir + XAC)));
		assertEquals("gh", reader.readLine());
		reader.close();
		Files.delete(Paths.get(currentDir + XAA));
		Files.delete(Paths.get(currentDir + XAB));
		Files.delete(Paths.get(currentDir + XAC));
	}

	@Test
	public void testSplitFileBytesToRunCorrectlyUsingAbsPathWithKiloBytes() throws IOException, SplitException {
		Path path = Paths.get(Environment.currentDirectory + File.separator + "1024bytes.txt");
		Path path2 = Paths.get(Environment.currentDirectory + File.separator + OUTPUT2 + File.separator);
		app.splitFileByBytes(path.toString(), path2.toString(), "1k");
		char[] buffer = new char[1024];
		String expected = "";
		for (int i = 0; i < 1024; i++) {
			expected += "a";
		}
		BufferedReader reader = new BufferedReader(new FileReader(new File(path2.toString() + "aa")));
		reader.read(buffer, 0, 1024);
		assertEquals(expected, new String(buffer));
		reader.close();
		buffer = new char[1024];
		reader = new BufferedReader(new FileReader(new File(path2.toString() + "ab")));
		int count = reader.read(buffer, 0, 1024);
		assertEquals("a", new String(buffer, 0, count));
		reader.close();
		Files.delete(Paths.get(path2.toString() + "aa"));
		Files.delete(Paths.get(path2.toString() + "ab"));
	}

	@Test
	public void testRunToThrowExpUsingInvalidLines() throws SplitException {
		expected = "split: 43s: invalid number of lines";
		String[] args = { "file1.txt", "-l", "43s" };
		thrown.expect(SplitException.class);
		thrown.expectMessage(expected);
		app.run(args, null, outputStream);
	}
}
