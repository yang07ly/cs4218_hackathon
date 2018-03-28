package sg.edu.nus.comp.cs4218.impl.commons;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;

public class FileUtilTest {

	private static final String TESTFILE1_TXT = "testfile1.txt";
	private static final String FOLDER3 = "folder3";
	private static final String FOLDER2 = "folder2";
	private static final String FOLDER1 = "folder1";
	private static final String TEST_DIR = System.getProperty("user.dir") + OSUtil.SEP + "test_system" + OSUtil.SEP
			+ "fileutil_test_system";

	FileUtil testFileUtil;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
	}

	@Test
	public void testisAbsoluteTrue() {
		assertTrue(FileUtil.isAbsolute(TEST_DIR));
	}

	@Test
	public void testgetFolderContent() throws IOException {
		String[] expected = { FOLDER1, FOLDER2, FOLDER3 };
		String[] actual = FileUtil.getFolderContent(TEST_DIR);
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testisDirectoryTrue() {
		assertTrue(FileUtil.isDirectory(TEST_DIR));
	}

	@Test
	public void testisDirectoryUnknownFile() {
		assertFalse(FileUtil.isDirectory(TEST_DIR + OSUtil.SEP + "notfound"));
	}

	@Test
	public void testGetFileFromPath() throws IOException {

		File expected = new File(TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP + TESTFILE1_TXT);
		File actual = FileUtil.getFileFromPath(FOLDER1 + OSUtil.SEP + TESTFILE1_TXT);

		assertEquals(expected, actual);

	}

	@Test
	public void testGetFileFromPathWithException() throws IOException {

		thrown.expect(IOException.class);
		thrown.expectMessage(TEST_DIR + ": Is a directory");
		FileUtil.getFileFromPath(TEST_DIR);

	}

	@Test
	public void testGetFolderFromPath() throws IOException {
		File expected = new File(TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP);
		File actual = FileUtil.getFolderFromPath(FOLDER1);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetFolderFromPathWithException() throws IOException {
		thrown.expect(IOException.class);
		thrown.expectMessage(TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP + TESTFILE1_TXT + ": Is a file");
		FileUtil.getFolderFromPath(TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP + TESTFILE1_TXT);

	}

	@Test
	public void testReadAllBytesFound() throws IOException {
		byte[] actual = FileUtil.readAllBytes(TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP + TESTFILE1_TXT);
		byte[] expected = { 49, 50, 51, 52 }; // ascii of 1 2 3 4
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testReadAllBytesFileNotFound() throws IOException {
		thrown.expect(IOException.class);
		thrown.expectMessage(
				TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP + "testfile1.olala" + ": No such file or directory");
		FileUtil.readAllBytes(TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP + "testfile1.olala");
	}

	@Test
	public void testGetFileOrFolderFromPathWithException() throws IOException {
		thrown.expect(IOException.class);
		thrown.expectMessage(
				TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP + "testfile99.txt" + ": No such file or directory");
		FileUtil.getFileOrDirectoryFromPath(TEST_DIR + OSUtil.SEP + FOLDER1 + OSUtil.SEP + "testfile99.txt");
	}
}
