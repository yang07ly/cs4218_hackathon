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
import sg.edu.nus.comp.cs4218.exception.CatException;

public class CatApplicationTest {

	private static final String FILE1_TXT = "file1.txt";
	private static final String FILE1_CONTENT = "asdf";
	private static final String NEWLINE = System.lineSeparator();
	CatApplication app;
	OutputStream outputStream;
	String expected, output, currentDir;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "cat_test_system";
		currentDir = Environment.currentDirectory + File.separator;
		app = new CatApplication();
		outputStream = new ByteArrayOutputStream();
		output = "";
		expected = "";
	}

	@Test
	public void testRunToThrowsExpUsingInvalidFile() throws CatException {
		String[] args = { FILE1_CONTENT };
		expected = "cat: asdf: No such file or directory";

		thrown.expect(CatException.class);
		thrown.expectMessage(expected);
		app.run(args, System.in, outputStream);
	}

	@Test
	public void testRunToThrowsExpUsingDirectory() throws CatException {
		expected = "cat: " + Environment.currentDirectory + ": Is a directory";
		String[] args = { Environment.currentDirectory };
		thrown.expect(CatException.class);
		thrown.expectMessage(expected);
		app.run(args, System.in, outputStream);
	}

	@Test
	public void testRunToRunCorrectlyUsingRelPath() throws CatException {
		expected = FILE1_CONTENT;
		String[] args = { FILE1_TXT };

		app.run(args, System.in, outputStream);
		output = outputStream.toString();
		assertEquals(expected, output);
	}

	@Test
	public void testRunToRunCorrectlyUsingAbsPath() throws CatException {
		String path = Environment.currentDirectory + File.separator + FILE1_TXT;
		expected = FILE1_CONTENT;
		String[] args = { path };

		app.run(args, System.in, outputStream);
		output = outputStream.toString();
		assertEquals(expected, output);
	}

	@Test
	public void testRunToRunCorrectlyUsingMultiFiles() throws CatException {
		expected = FILE1_CONTENT + NEWLINE + "qwer";
		String[] args = { FILE1_TXT, "file2.txt" };

		app.run(args, System.in, outputStream);
		output = outputStream.toString();
		assertEquals(expected, output);
	}

	@Test
	public void testRunToRunCorrectlyUsingValidAndInvalidFiles() throws CatException {
		expected = FILE1_CONTENT + NEWLINE + "cat: asdf: No such file or directory" + NEWLINE + "qwer";
		String[] args = { FILE1_TXT, FILE1_CONTENT, "file2.txt" };

		app.run(args, System.in, outputStream);
		output = outputStream.toString();
		assertEquals(expected, output);
	}

	@Test
	public void testRunToRunCorrectlyUsingInputStream() throws CatException, FileNotFoundException {
		expected = FILE1_CONTENT;
		FileInputStream fileStream = new FileInputStream(new File(currentDir + FILE1_TXT));
		app.run(null, fileStream, outputStream);
		output = outputStream.toString();
		assertEquals(expected, output);
	}

	@Test
	public void testRunToThrowExceptionUsingNullArgs() throws CatException {
		expected = "cat: Null Pointer Exception";
		thrown.expect(CatException.class);
		thrown.expectMessage(expected);
		app.run(null, null, outputStream);
	}

	@Test
	public void testRunToRunCorrectlyUsingFilesAndInputStream() throws CatException, FileNotFoundException {
		expected = "asdf" + System.lineSeparator() + "asdf";
		String[] args = { "file1.txt", "-" };
		InputStream inputStream = new FileInputStream(
				new File(Environment.currentDirectory + File.separator + "file1.txt"));
		app.run(args, inputStream, outputStream);
		assertEquals(expected, outputStream.toString());
	}
}
