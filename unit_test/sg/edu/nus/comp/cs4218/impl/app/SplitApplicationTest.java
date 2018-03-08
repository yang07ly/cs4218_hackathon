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
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;

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
	private static final String IO_ERROR_IN_TEST = "IO Error in test";
	SplitApplication app;
	InputStream inputStream = System.in;
	OutputStream outputStream;
	String expected, output, currentDir;

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
	public void testRunNullInputs() {
		expected = "split: Null Pointer Exception";
		try {
			app.run(null, null, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testRunExtraOperand() {
		String[] splitArgs = { FILE1_TXT, "-s", "-c" };
		expected = "split: -s: invalid flag";
		try {
			app.run(splitArgs, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testNegativeLines() {
		String[] splitArgs = { FILE1_TXT, "-l", "-2" };
		expected = "split: -2: invalid number of lines";
		try {
			app.run(splitArgs, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testRunSplitMoreThanOneWay() {
		String[] splitArgs = { FILE1_TXT, "-l", "-2", "-b", "3" };
		expected = "split: cannot split in more than one way";
		try {
			app.run(splitArgs, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testRunSplitMoreThanOneWay2() {
		String[] splitArgs = { FILE1_TXT, "-b", "3", "-l", "-2" };
		expected = "split: cannot split in more than one way";
		try {
			app.run(splitArgs, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testRunSplit2Ways() {
		String[] splitArgs = { FILE1_TXT, "-b", "2", "-l", "2" };
		expected = "split: cannot split in more than one way";
		try {
			app.run(splitArgs, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testLinesEmptyFileName() {
		expected = "split: : No such file or directory";
		try {
			app.splitFileByLines("", "", 4);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testLinesSpaceFileName() {
		expected = "split:  : No such file or directory";
		try {
			app.splitFileByLines(" ", "", 4);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testLinesFileNotFound() {
		expected = "split: asdf: No such file or directory";
		try {
			app.splitFileByLines("asdf", "", 4);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testLinesDirectoryFileNotFound() {
		try {
			app.splitFileByLines("asdf.txt", "", 4);
		} catch (Exception e) {
			output = e.getMessage();
		}
		expected = "split: asdf.txt: No such file or directory";
		assertEquals(expected, output);
	}

	@Test
	public void testLinesInvalidOutputPath() {
		try {
			String path = currentDir + "asddfaw" + File.separator;
			if (System.getProperty("os.name").length() > 8) {
				app.splitFileByLines(FILE1_TXT, path, 2);
			}
			{
				app.splitFileByLines(FILE1_TXT, path, 2);
			}
			expected = "split: '" + path + "': No such file or directory";
			assertEquals(expected, output);
		} catch (Exception e) {
			output = e.getMessage();
		}
	}

	@Test
	public void testLinesInvalidNumberOfLines() {
		try {
			app.splitFileByLines(FILE1_TXT, "", 0);
		} catch (Exception e) {
			output = e.getMessage();
		}
		expected = "split: 0: invalid number of lines";
		assertEquals(expected, output);
	}

	/*
	 * These tests are all valid test cases for splitFileByLines
	 */
	@Test
	public void testSplitStreamByDefaultLines() {
		try {
			FileInputStream fileStream = new FileInputStream(new File(currentDir + FILE1_TXT));
			app.run(null, fileStream, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
			assertEquals(ASDFGH, reader.readLine());
			assertEquals(QWERTY, reader.readLine());
			assertEquals(ZXCVBN, reader.readLine());
			reader.close();
			Files.delete(Paths.get(currentDir + XAA));
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testSplitStreamByBytes() {
		String[] myArgs = { "-b", "6" };
		try {
			FileInputStream fileStream = new FileInputStream(new File(currentDir + FILE2_TXT));
			app.run(myArgs, fileStream, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
			char[] buffer = new char[10];
			int count = fileReader.read(buffer, 0, 10);
			assertEquals(ASDFGH, new String(buffer, 0, count));
			fileReader.close();
			Files.delete(Paths.get(currentDir + XAA));
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testSplitFileByLines() {
		String[] myArgs = { currentDir + FILE1_TXT, "-l", "6" };
		try {
			app.run(myArgs, null, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
			assertEquals(ASDFGH, reader.readLine());
			assertEquals(QWERTY, reader.readLine());
			assertEquals(ZXCVBN, reader.readLine());
			reader.close();
			Files.delete(Paths.get(currentDir + XAA));
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testSplitFileByBytes() {
		String[] myArgs = { currentDir + FILE2_TXT, "-b", "6" };
		try {
			app.run(myArgs, null, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
			char[] buffer = new char[10];
			int count = fileReader.read(buffer, 0, 10);
			assertEquals(ASDFGH, new String(buffer, 0, count));
			fileReader.close();
			Files.delete(Paths.get(currentDir + XAA));
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testLinesRelativeAny() {
		try {
			app.splitFileByLines(FILE1_TXT, "", 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + "aa")));
			assertEquals(ASDFGH, reader.readLine());
			assertEquals(QWERTY, reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File(currentDir + "ab")));
			assertEquals(ZXCVBN, reader.readLine());
			reader.close();
			Files.delete(Paths.get(currentDir + "aa"));
			Files.delete(Paths.get(currentDir + "ab"));
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testLinesRelativePath() {
		Path path = Paths.get(Environment.currentDirectory).resolve(OUTPUT2);
		String myPath = path.toString() + File.separator;
		Path outputFile = Paths.get(myPath + "aa");
		Path outputFileB = Paths.get(myPath + "ab");
		try {
			app.splitFileByLines(FILE1_TXT, path.toString() + File.separator, 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile.toString())));
			assertEquals(ASDFGH, reader.readLine());
			assertEquals(QWERTY, reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File(outputFileB.toString())));
			assertEquals(ZXCVBN, reader.readLine());
			reader.close();
			Files.delete(outputFile);
			Files.delete(outputFileB);
		} catch (IOException e) {
			output = e.getMessage();
		}

		assertEquals("", output);
	}

	@Test
	public void testLinesRelativeNull() {
		try {
			app.splitFileByLines(FILE1_TXT, null, 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
			assertEquals(ASDFGH, reader.readLine());
			assertEquals(QWERTY, reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File(currentDir + XAB)));
			assertEquals(ZXCVBN, reader.readLine());
			reader.close();
			Files.delete(Paths.get(currentDir + XAA));
			Files.delete(Paths.get(currentDir + XAB));
		} catch (IOException e) {
			output = e.getMessage();
		}
		assertEquals("", output);
	}

	@Test
	public void testLinesAbsoluteAny() {
		Path path = Paths.get(Environment.currentDirectory + File.separator + FILE1_TXT);
		try {
			app.splitFileByLines(path.toString(), "", 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + "aa")));
			assertEquals(ASDFGH, reader.readLine());
			assertEquals(QWERTY, reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File(currentDir + "ab")));
			assertEquals(ZXCVBN, reader.readLine());
			reader.close();
			Files.delete(Paths.get(currentDir + "aa"));
			Files.delete(Paths.get(currentDir + "ab"));
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}

		assertEquals("", output);
	}

	@Test
	public void testLinesAbsolutePath() {
		Path path = Paths.get(Environment.currentDirectory).resolve(OUTPUT2);
		Path source = Paths.get(Environment.currentDirectory);
		String myPath = path.toString() + File.separator;
		Path outputFile = Paths.get(myPath + "aa");
		Path outputFileB = Paths.get(myPath + "ab");
		try {
			app.splitFileByLines(source.toString() + File.separator + FILE1_TXT, path.toString() + File.separator, 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile.toString())));
			assertEquals(ASDFGH, reader.readLine());
			assertEquals(QWERTY, reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File(outputFileB.toString())));
			assertEquals(ZXCVBN, reader.readLine());
			reader.close();
			Files.delete(outputFile);
			Files.delete(outputFileB);
		} catch (IOException e) {
			output = e.getMessage();
		}

		assertEquals("", output);
	}

	@Test
	public void testLinesAbsoluteNull() {
		Path source = Paths.get(Environment.currentDirectory);
		try {
			app.splitFileByLines(source.toString() + File.separator + FILE1_TXT, null, 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(currentDir + XAA)));
			assertEquals(ASDFGH, reader.readLine());
			assertEquals(QWERTY, reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File(currentDir + XAB)));
			assertEquals(ZXCVBN, reader.readLine());
			reader.close();
			Files.delete(Paths.get(currentDir + XAA));
			Files.delete(Paths.get(currentDir + XAB));
		} catch (IOException e) {
			output = e.getMessage();
		}
		assertEquals("", output);
	}
	/*
	 * These test cases are for splitFileByBytes
	 */

	@Test
	public void testBytesEmptyFileName() {
		expected = "split: : No such file or directory";
		try {
			app.splitFileByBytes("", "", "1");
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testBytesSpaceFileName() {
		expected = "split:  : No such file or directory";
		try {
			app.splitFileByBytes(" ", "", "1");
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testBytesFileNotFound() {
		expected = "split: asdf: No such file or directory";
		try {
			app.splitFileByBytes("asdf", "", "1");
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testBytesDirectoryFileNotFound() {
		try {
			app.splitFileByBytes("asdf.txt", "", "4");
		} catch (Exception e) {
			output = e.getMessage();
		}
		expected = "split: asdf.txt: No such file or directory";
		assertEquals(expected, output);
	}

	@Test
	public void testBytesInvalidOutputPath() {
		try {
			String path = currentDir + "asddfaw" + File.separator;
			if (System.getProperty("os.name").length() > 8) {
				app.splitFileByBytes(FILE1_TXT, path, "2");
			}
			{
				app.splitFileByBytes(FILE1_TXT, path, "2");
			}
			expected = "split: '" + path + "': No such file or directory";
			assertEquals(expected, output);
		} catch (Exception e) {
			output = e.getMessage();
		}
	}

	@Test
	public void testBytesInvalidNumberOfBytes() {
		try {
			app.splitFileByBytes(FILE1_TXT, "", "2s");
		} catch (Exception e) {
			output = e.getMessage();
		}
		expected = "split: 2s: invalid number of bytes";
		assertEquals(expected, output);
	}

	@Test
	public void testBytesEmptyBytesString() {
		try {
			app.splitFileByBytes(FILE1_TXT, "", "");
		} catch (Exception e) {
			output = e.getMessage();
		}
		expected = "split: : invalid number of bytes";
		assertEquals(expected, output);
	}

	@Test
	public void testBytesSpaceBytesString() {
		try {
			app.splitFileByBytes(FILE1_TXT, "", " ");
		} catch (Exception e) {
			output = e.getMessage();
		}
		expected = "split:  : invalid number of bytes";
		assertEquals(expected, output);
	}

	@Test
	public void testBytesZeroBytes() {
		try {
			app.splitFileByBytes(FILE1_TXT, "", "0");
		} catch (Exception e) {
			output = e.getMessage();
		}
		expected = "split: 0: invalid number of bytes";
		assertEquals(expected, output);
	}

	@Test
	public void testBytesRelativeAnyInteger() {
		try {
			app.splitFileByBytes(FILE2_TXT, "", "2");
		} catch (Exception e) {
			output = e.getMessage();
		}
		try {
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
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testBytesRelativeAnyByte() {
		try {
			app.splitFileByBytes("512bytes.txt", "", "1b");
		} catch (Exception e) {
			output = e.getMessage();
		}
		char[] buffer = new char[512];
		String expected = "";
		for (int i = 0; i < 512; i++) {
			expected += "a";
		}
		try {
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
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testBytesAbsoluteAnyKilobyte() {
		Path path = Paths.get(Environment.currentDirectory + File.separator + "1024bytes.txt");
		try {
			app.splitFileByBytes(path.toString(), "", "1k");
		} catch (Exception e) {
			output = e.getMessage();
		}
		char[] buffer = new char[1024];
		String expected = "";
		for (int i = 0; i < 1024; i++) {
			expected += "a";
		}
		try {
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
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testBytesRelativeAnyMegabyte() {
		try {
			app.splitFileByBytes("megabytes.txt", "", "1m");
		} catch (Exception e) {
			output = e.getMessage();
		}
		char[] buffer = new char[1048576];
		StringBuilder stringBuilder = new StringBuilder(1048576);
		for (int i = 0; i < 1048576; i++) {
			stringBuilder.append('a');
		}
		String expected = new String(stringBuilder);
		try {
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
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testBytesRelativePathInteger() {
		Path path = Paths.get(Environment.currentDirectory).resolve(OUTPUT2);
		String myPath = path.toString() + File.separator;
		Path outputFile = Paths.get(myPath + "aa");
		Path outputFileB = Paths.get(myPath + "ab");
		Path outputFileC = Paths.get(myPath + "ac");
		try {
			app.splitFileByBytes(FILE2_TXT, myPath, "2");
		} catch (Exception e) {
			output = e.getMessage();
		}
		try {
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
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testBytesRelativePathByte() {
		Path path = Paths.get(Environment.currentDirectory).resolve(OUTPUT2);
		String myPath = path.toString() + File.separator;
		Path outputFile = Paths.get(myPath + "aa");
		Path outputFileB = Paths.get(myPath + "ab");
		try {
			app.splitFileByBytes("512bytes.txt", myPath, "1b");
		} catch (Exception e) {
			output = e.getMessage();
		}
		char[] buffer = new char[512];
		String expected = "";
		for (int i = 0; i < 512; i++) {
			expected += "a";
		}
		try {
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
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
		assertEquals("", output);
	}

	@Test
	public void testBytesAbsoluteNullInteger() {
		Path path = Paths.get(Environment.currentDirectory + File.separator + FILE2_TXT);
		try {
			app.splitFileByBytes(path.toString(), null, "2");
		} catch (Exception e) {
			output = e.getMessage();
		}
		try {
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
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
	}

	@Test
	public void testBytesAbsolutePathKiloByte() {
		Path path = Paths.get(Environment.currentDirectory + File.separator + "1024bytes.txt");
		Path path2 = Paths.get(Environment.currentDirectory + File.separator + OUTPUT2 + File.separator);
		try {
			app.splitFileByBytes(path.toString(), path2.toString(), "1k");
		} catch (Exception e) {
			output = e.getMessage();
		}
		char[] buffer = new char[1024];
		String expected = "";
		for (int i = 0; i < 1024; i++) {
			expected += "a";
		}
		try {
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
		} catch (IOException e) {
			output = IO_ERROR_IN_TEST;
		}
	}

}
