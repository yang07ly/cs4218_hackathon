package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.PasteException;

public class PasteApplicationTest {
	private static final String PASTE = "paste: ";
	private static final String FILE2_TXT = "file2.txt";
	private static final String FILE_NOT_FOUND = "paste: asdf: No such file or directory";
	private static final String ASDF = "asdf";
	private static final String OUTPUT2 = "output";
	private static final String OUTPUT_DIR = "output: Is a directory";
	private static final String ASDFGH = "asdfgh";
	private static final String FILE1_TXT = "file1.txt";
	PasteApplication app;
	OutputStream outputStream;
	String expected, output, currentDir;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

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
	public void testRun() throws PasteException {
		String path = currentDir + OUTPUT2;
		expected = PASTE + currentDir + OUTPUT_DIR;
		String args[] = { path };
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);
		app.run(args, null, outputStream);
	}

	@Test
	public void testInvalidFile() throws PasteException {
		expected = FILE_NOT_FOUND;
		String[] args = { ASDF };
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);
		output = app.mergeFile(args);
	}

	@Test
	public void testDir() throws PasteException {
		String path = currentDir + OUTPUT2;
		expected = PASTE + currentDir + OUTPUT_DIR;
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);
		output = app.mergeFile(path);
	}

	@Test
	public void testMultipleWithInvalidFile() throws PasteException {
		String[] args = { FILE1_TXT, ASDF, FILE2_TXT };
		expected = FILE_NOT_FOUND;
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);
		output = app.mergeFile(args);
	}

	@Test
	public void testMultipleWithDirectory() throws PasteException {
		String directory = currentDir + OUTPUT2;
		String[] args = { FILE1_TXT, directory, FILE2_TXT };
		expected = PASTE + currentDir + OUTPUT_DIR;
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);
		output = app.mergeFile(args);
	}

	@Test
	public void testZeroFiles() throws PasteException {
		expected = "";
		output = app.mergeFile();
		assertEquals(expected, output);
	}

	@Test
	public void testOneFile() throws PasteException {
		expected = ASDFGH;
		output = app.mergeFile(FILE1_TXT);
		assertEquals(expected, output);
	}

	@Test
	public void testOneFileAbsolutePath() throws PasteException {
		String file = currentDir + FILE1_TXT;
		expected = ASDFGH;
		output = app.mergeFile(file);
		assertEquals(expected, output);
	}

	@Test
	public void testMultipleFiles() throws PasteException {
		String file = currentDir + FILE2_TXT;
		String[] args = { FILE1_TXT, file };
		expected = "asdfgh\tqwerty";
		output = app.mergeFile(args);
		assertEquals(expected, output);
	}
	/*
	 * Testing stream method
	 */

	@Test
	public void testStreamInvalidFile() throws FileNotFoundException, PasteException {
		expected = FILE_NOT_FOUND;
		String[] args = { ASDF };
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);

		FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
		output = app.mergeFileAndStdin(inputStream, args);
	}

	@Test
	public void testStreamDir() throws FileNotFoundException, PasteException {
		String path = currentDir + OUTPUT2;
		expected = PASTE + currentDir + OUTPUT_DIR;
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);

		FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
		output = app.mergeFileAndStdin(inputStream, path);
	}

	@Test
	public void testStreamMultipleWithInvalidFile() throws FileNotFoundException, PasteException {
		String[] args = { FILE1_TXT, ASDF, FILE2_TXT };
		expected = FILE_NOT_FOUND;
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);

		FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
		output = app.mergeFileAndStdin(inputStream, args);
	}

	@Test
	public void testStreamMultipleWithDirectory() throws FileNotFoundException, PasteException {
		String directory = currentDir + OUTPUT2;
		String[] args = { FILE1_TXT, directory, FILE2_TXT };
		expected = PASTE + currentDir + OUTPUT_DIR;
		thrown.expect(PasteException.class);
		thrown.expectMessage(expected);

		FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
		output = app.mergeFileAndStdin(inputStream, args);
		assertEquals(expected, output);
	}

	@Test
	public void testStreamZeroFiles() throws FileNotFoundException, PasteException {
		expected = ASDFGH;
		FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
		output = app.mergeFileAndStdin(inputStream);
		assertEquals(expected, output);
	}

	@Test
	public void testStreamOneFile() throws FileNotFoundException, PasteException {
		expected = "asdfgh\tasdfgh";
		String path = currentDir + FILE1_TXT;
		FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
		output = app.mergeFileAndStdin(inputStream, path);
		assertEquals(expected, output);
	}

	@Test
	public void testStreamOneFileAbsolutePath() throws FileNotFoundException, PasteException {
		String file = currentDir + FILE1_TXT;
		expected = "asdfgh\tasdfgh";
		FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
		output = app.mergeFileAndStdin(inputStream, file);
		assertEquals(expected, output);
	}

	@Test
	public void testStreamMultipleFiles() throws FileNotFoundException, PasteException {
		String file = currentDir + FILE2_TXT;
		String[] args = { FILE1_TXT, file };
		expected = "asdfgh\tqwerty\tasdfgh";
		FileInputStream inputStream = new FileInputStream(currentDir + FILE1_TXT);
		output = app.mergeFileAndStdin(inputStream, args);
		assertEquals(expected, output);
	}
}
