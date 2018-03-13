package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;

public class CatApplicationTest {

	private static final String FILE1_TXT = "file1.txt";
	private static final String FILE1_CONTENT = "asdf";
	CatApplication app;
	OutputStream outputStream;
	String expected, output, currentDir;

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
	public void testInvalidFile() {
		expected = "cat: asdf: No such file or directory";
		String[] args = {"asdf"};
		
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDirectory() {
		expected = "cat: " + Environment.currentDirectory + ": Is a directory";
		String[] args = {Environment.currentDirectory};
		
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testOneFile() {
		expected = FILE1_CONTENT;
		String[] args = {FILE1_TXT};
		
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testAbsolutePath() {
		String path = Environment.currentDirectory + File.separator + FILE1_TXT;
		expected = FILE1_CONTENT;
		String[] args = {path};
		
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleFiles() {
		expected = "asdf\nqwer";
		String[] args = {FILE1_TXT,"file2.txt"};
		
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleFilesWithInvalidFiles() {
		expected = "asdf\ncat: asdf: No such file or directory\nqwer";
		String[] args = {FILE1_TXT, "asdf", "file2.txt"};
		
		try {
			app.run(args, System.in, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStream() {
		expected = FILE1_CONTENT;
		try {
			FileInputStream fileStream = new FileInputStream(new File(currentDir + FILE1_TXT));
			app.run(null, fileStream, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testNullArgsNullStream() {
		expected = "cat: Null Pointer Exception";
		
		try {
			app.run(null, null, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}
}
