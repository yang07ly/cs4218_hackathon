package sg.edu.nus.comp.cs4218.impl.commons;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;

public class FileUtilTest {
	
	private static final String TEST_DIR = System.getProperty("user.dir") + OSUtil.SEP + "test_system" + OSUtil.SEP
			+ "fileutil_test_system";
	private String currentDir;
	
	FileUtil testFileUtil;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + OSUtil.SEP + "test_system" + OSUtil.SEP
				+ "fileutil_test_system";
		currentDir = Environment.currentDirectory + OSUtil.SEP;
	
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testisAbsoluteTrue() {
		assertTrue(FileUtil.isAbsolute(TEST_DIR));	
	}

	@Test
	public void testgetFolderContent() throws IOException {
		String[] Expected = {"folder1" , "folder2" , "folder3"};
		String[] Actual = FileUtil.getFolderContent(TEST_DIR);
		assertArrayEquals(Expected,Actual);
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
		
		File Expected = new File(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP +"testfile1.txt");
		File Actual = FileUtil.getFileFromPath("folder1" + OSUtil.SEP + "testfile1.txt");
		
		assertEquals(Expected,Actual);
		
	}
	
	@Test
	public void testGetFileFromPathWithException() throws IOException{
		
		thrown.expect(IOException.class);
		thrown.expectMessage(TEST_DIR + ": Is a directory");
		FileUtil.getFileFromPath(TEST_DIR);
		
		
	}
	
	@Test
	public void testGetFolderFromPath() throws IOException {
		File Expected = new File(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP);
		File Actual = FileUtil.getFolderFromPath("folder1");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testGetFolderFromPathWithException() throws IOException {
		thrown.expect(IOException.class);
		thrown.expectMessage(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP +"testfile1.txt" +": Is a file");
		FileUtil.getFolderFromPath(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP +"testfile1.txt");
		
	}
	
	@Test
	public void testReadAllBytesFound() throws IOException {
		byte[] Actual = FileUtil.readAllBytes(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP +"testfile1.txt")		;
		byte[] Expected = {49,50 ,51,52}; // ascii of 1 2 3 4
		assertArrayEquals(Expected,Actual);		
	}
	
	@Test
	public void testReadAllBytesFileNotFound() throws IOException {
		thrown.expect(IOException.class);
		thrown.expectMessage(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP +"testfile1.olala" +": No such file or directory");
		FileUtil.readAllBytes(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP +"testfile1.olala");
	}
	
	
	@Test
	public void testGetFileOrFolderFromPathWithException() throws IOException {
		thrown.expect(IOException.class);
		thrown.expectMessage(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP +"testfile99.txt" +": No such file or directory");
		FileUtil.getFileOrDirectoryFromPath(TEST_DIR + OSUtil.SEP + "folder1" + OSUtil.SEP +"testfile99.txt");
		
		
	}

}
