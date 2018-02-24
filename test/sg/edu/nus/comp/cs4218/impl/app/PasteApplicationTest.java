package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;

public class PasteApplicationTest {
	PasteApplication app;
	OutputStream outputStream;
	String expected, output, currentDir;

	@Before
	public void setUp() {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "paste_test_system";
		currentDir = Environment.currentDirectory + File.separator;
		app = new PasteApplication();
		outputStream = new ByteArrayOutputStream();
		output = "";
		expected = "";
	}

	@Test
	public void testInvalidFile() {
		expected = "paste: asdf: No such file or directory";
		String[] args = {"asdf"};
		
		try {
			output = app.mergeFile(args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDir() {
		String path = currentDir + "output";
		expected = "paste: output: Is a directory";
		
		try {
			output = app.mergeFile(path);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleWithInvalidFile() {
		String[] args = {"file1.txt", "asdf", "file2.txt"};
		expected = "paste: asdf: No such file or directory";
		
		try {
			output = app.mergeFile(args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleWithDirectory() {
		String directory = currentDir + "output";
		String[] args = {"file1.txt", directory, "file2.txt"};
		expected = "paste: output: Is a directory";
		
		try {
			output = app.mergeFile(args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testZeroFiles() {
		expected = "";
		try {
			output = app.mergeFile();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testOneFile() {
		expected = "asdfgh";
		try {
			output = app.mergeFile("file1.txt");
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testOneFileAbsolutePath() {
		String file = currentDir + "file1.txt";
		expected = "asdfgh";
		try {
			output = app.mergeFile(file);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleFiles() {
		String file = currentDir + "file2.txt";
		String[] args = {"file1.txt", file};
		expected = "asdfgh qwerty";
		try {
			output = app.mergeFile(args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}
	/*
	 * Testing stream method
	 */


	@Test
	public void testStreamInvalidFile() {
		expected = "paste: asdf: No such file or directory";
		String[] args = {"asdf"};
		
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + "file1.txt");
			output = app.mergeFileAndStdin(inputStream, args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamDir() {
		String path = currentDir + "output";
		expected = "paste: output: Is a directory";
		
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + "file1.txt");
			output = app.mergeFileAndStdin(inputStream, path);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamMultipleWithInvalidFile() {
		String[] args = {"file1.txt", "asdf", "file2.txt"};
		expected = "paste: asdf: No such file or directory";
		
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + "file1.txt");
			output = app.mergeFileAndStdin(inputStream, args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamMultipleWithDirectory() {
		String directory = currentDir + "output";
		String[] args = {"file1.txt", directory, "file2.txt"};
		expected = "paste: output: Is a directory";
		
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + "file1.txt");
			output = app.mergeFileAndStdin(inputStream, args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamZeroFiles() {
		expected = "asdfgh";
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + "file1.txt");
			output = app.mergeFileAndStdin(inputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamOneFile() {
		expected = "asdfgh";
		String path = currentDir + "file1.txt";
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + "file1.txt");
			output = app.mergeFileAndStdin(inputStream, path);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamOneFileAbsolutePath() {
		String file = currentDir + "file1.txt";
		expected = "asdfgh";
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + "file1.txt");
			output = app.mergeFileAndStdin(inputStream, file);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamMultipleFiles() {
		String file = currentDir + "file2.txt";
		String[] args = {"file1.txt", file};
		expected = "asdfgh qwerty";
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + "file1.txt");
			output = app.mergeFileAndStdin(inputStream, args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}
}
