package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.MkdirException;

public class MkdirApplicationTest {
	public static final String DIR_FOLDER1 = "folder1";
	public static final String DIR_FOLDER2 = "folder2";
	public static final String DIR_FOLDER3 = "folder3";
	public static final String DIR_EXISTING = "existingFolder";
	public static final String DIR_NONEXISTENT = "nonExistent";
	public static final String FILE_EXISTING = "existingFile.txt";
	
	MkdirApplication mkdirApp;
	String expected, result;
	Path dirPath;
	
	private void delete(File file) throws IOException {	 
		for (File childFile : file.listFiles()) {
			if (childFile.isDirectory()) {
				delete(childFile);
			}
			if (!childFile.delete()) {
				throw new IOException();
			}
		}
	}

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator + "mkdir_test_system";
		mkdirApp = new MkdirApplication();
		
		Path folderPath = Paths.get(Environment.currentDirectory).resolve("existingFolder");
		Path filePath = Paths.get(Environment.currentDirectory).resolve("existingFile.txt");
		Files.createDirectories(folderPath);
		Files.createFile(filePath);
	}

	@After
	public void tearDown() throws Exception {
		File file = new File(Environment.currentDirectory);
		delete(file);
	}

	@Test
	public void testRelativeMkDir() {
		try {
			mkdirApp.createFolder(DIR_FOLDER1);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		dirPath = Paths.get(Environment.currentDirectory).resolve(DIR_FOLDER1);
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
	}
	
	@Test
	public void testAbsoluteMkDir() {
		try {
			mkdirApp.createFolder(Environment.currentDirectory + File.separator + DIR_FOLDER1);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		dirPath = Paths.get(Environment.currentDirectory).resolve(DIR_FOLDER1);
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
	}
	
	@Test
	public void testMkDirInDir() {
		try {
			mkdirApp.createFolder(DIR_EXISTING + File.separator + DIR_FOLDER1);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		dirPath = Paths.get(Environment.currentDirectory).resolve(DIR_EXISTING + File.separator + DIR_FOLDER1);
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
	}
	
	@Test
	public void testMkMutipleDirs() {
		String[] folderNames = {DIR_FOLDER1, DIR_FOLDER2, DIR_FOLDER3};
		try {
			mkdirApp.createFolder(folderNames);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		for (int i = 0; i < folderNames.length; i++) {
			dirPath = Paths.get(Environment.currentDirectory).resolve(folderNames[i]);
			assertTrue(Files.exists(dirPath));
			assertTrue(Files.isDirectory(dirPath));
		}
	}
	
	@Test
	public void testMkMutipleDirsInSeq() {
		String[] folderNames = {DIR_FOLDER1, DIR_FOLDER1 + File.separator + DIR_FOLDER2, 
								DIR_FOLDER1 + File.separator + DIR_FOLDER2 + File.separator + DIR_FOLDER3};
		try {
			mkdirApp.createFolder(folderNames);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		for (int i = 0; i < folderNames.length; i++) {
			dirPath = Paths.get(Environment.currentDirectory).resolve(folderNames[i]);
			assertTrue(Files.exists(dirPath));
			assertTrue(Files.isDirectory(dirPath));
		}
	}
	
	@Test
	public void testInvalidMkDirNullName() {
		expected = "mkdir: missing operand";	
		try {
			mkdirApp.createFolder();
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidMkDirEmptyName() {
		expected = "mkdir: cannot create directory ‘’: No such file or directory";		
		try {
			mkdirApp.createFolder("");
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidMkDirOnlySpacesName() {
		try {
			mkdirApp.createFolder("   ");
			
			dirPath = Paths.get(Environment.currentDirectory).resolve("   ");
			assertTrue(Files.exists(dirPath));
			assertTrue(Files.isDirectory(dirPath));
		} catch (MkdirException e) {
			result = e.getMessage();
			assertEquals("mkdir: cannot create directory ‘   ’: No such file or directory", result);
		}
	}
	
	@Test
	public void testInvalidMkDirOfExistingDir() {
		expected = "mkdir: cannot create directory ‘existingFolder’: File exists";		
		try {
			mkdirApp.createFolder(DIR_EXISTING);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidMkDirOfExistingFile() {
		expected = "mkdir: cannot create directory ‘existingFile.txt’: File exists";		
		try {
			mkdirApp.createFolder(FILE_EXISTING);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidMkDirInNonExistDir() {
		String newPath = DIR_NONEXISTENT + File.separator + DIR_FOLDER1;
		expected = "mkdir: cannot create directory ‘" + newPath + "’: No such file or directory";
		try {
			mkdirApp.createFolder(newPath);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidMkDirWithValidDir() {
		String newPath = DIR_NONEXISTENT + File.separator + DIR_FOLDER2;
		expected = "mkdir: cannot create directory ‘" + newPath + "’: No such file or directory";		
		try {
			mkdirApp.createFolder(DIR_FOLDER1, newPath, DIR_FOLDER3);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testMkDirRun() {
		String[] folderNames = {DIR_FOLDER1, DIR_FOLDER1 + File.separator + DIR_FOLDER2, 
								DIR_FOLDER1 + File.separator + DIR_FOLDER2 + File.separator + DIR_FOLDER3};
		try {
			mkdirApp.run(folderNames, null, null);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		for (int i = 0; i < folderNames.length; i++) {
			dirPath = Paths.get(Environment.currentDirectory).resolve(folderNames[i]);
			assertTrue(Files.exists(dirPath));
			assertTrue(Files.isDirectory(dirPath));
		}
	}
	
	@Test
	public void testMkDirNullArgs() {
		expected = "mkdir: missing operand";
		try {
			mkdirApp.run(null, null, null);
		} catch (MkdirException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
}
