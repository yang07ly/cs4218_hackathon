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
	private static final String FILE2_TXT = "file2.txt";
	private static final String FILE_NOT_FOUND = "paste: asdf: No such file or directory";
	private static final String ASDF = "asdf";
	private static final String OUTPUT2 = "output";
	private static final String OUTPUT_DIR = "paste: output: Is a directory";
	private static final String ASDFGH = "asdfgh";
	private static final String FILE1_TXT = "file1.txt";
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
	public void testRun() {
		String path = currentDir + OUTPUT2;
		expected = OUTPUT_DIR;
		String args[] = {path};
		try {
			app.run(args, null, outputStream);
			output = outputStream.toString();
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testInvalidFile() {
		expected = FILE_NOT_FOUND;
		String[] args = {ASDF};
		
		try {
			output = app.mergeFile(args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testDir() {
		String path = currentDir + OUTPUT2;
		expected = OUTPUT_DIR;
		
		try {
			output = app.mergeFile(path);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleWithInvalidFile() {
		String[] args = {FILE1_TXT, ASDF, FILE2_TXT};
		expected = FILE_NOT_FOUND;
		
		try {
			output = app.mergeFile(args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleWithDirectory() {
		String directory = currentDir + OUTPUT2;
		String[] args = {FILE1_TXT, directory, FILE2_TXT};
		expected = OUTPUT_DIR;
		
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
		expected = ASDFGH;
		try {
			output = app.mergeFile(FILE1_TXT);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testOneFileAbsolutePath() {
		String file = currentDir + FILE1_TXT;
		expected = ASDFGH;
		try {
			output = app.mergeFile(file);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleFiles() {
		String file = currentDir + FILE2_TXT;
		String[] args = {FILE1_TXT, file};
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
		expected = FILE_NOT_FOUND;
		String[] args = {ASDF};
		
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
			output = app.mergeFileAndStdin(inputStream, args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamDir() {
		String path = currentDir + OUTPUT2;
		expected = OUTPUT_DIR;
		
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
			output = app.mergeFileAndStdin(inputStream, path);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamMultipleWithInvalidFile() {
		String[] args = {FILE1_TXT, ASDF, FILE2_TXT};
		expected = FILE_NOT_FOUND;
		
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
			output = app.mergeFileAndStdin(inputStream, args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamMultipleWithDirectory() {
		String directory = currentDir + OUTPUT2;
		String[] args = {FILE1_TXT, directory, FILE2_TXT};
		expected = OUTPUT_DIR;
		
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
			output = app.mergeFileAndStdin(inputStream, args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamZeroFiles() {
		expected = ASDFGH;
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
			output = app.mergeFileAndStdin(inputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamOneFile() {
		expected = ASDFGH;
		String path = currentDir + FILE1_TXT;
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
			output = app.mergeFileAndStdin(inputStream, path);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamOneFileAbsolutePath() {
		String file = currentDir + FILE1_TXT;
		expected = ASDFGH;
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
			output = app.mergeFileAndStdin(inputStream, file);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testStreamMultipleFiles() {
		String file = currentDir + FILE2_TXT;
		String[] args = {FILE1_TXT, file};
		expected = "asdfgh qwerty";
		try {
			FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
			output = app.mergeFileAndStdin(inputStream, args);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}
}
