package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.MkdirException;
import sg.edu.nus.comp.cs4218.impl.commons.OSValidator;

public class MkdirApplicationTest {
	
	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system" + File.separator + "mkdir_test_system";
	
	private static final String DIR_FOLDER1 = "folder1";
	private static final String DIR_FOLDER2 = "folder2";
	private static final String DIR_FOLDER3 = "folder3";
	
	private static final String DIR_FOLDER1_1 = DIR_FOLDER1 +  File.separator + "folder1_1";
	private static final String DIR_FOLDER1_1_1 = DIR_FOLDER1_1 +  File.separator + "folder1_1_1";
	
	private static final String ABS_DIR_FOLDER1 = TEST_DIR + File.separator + DIR_FOLDER1;
	
	private static final String DIR_EXISTING = "existingFolder";
	private static final String DIR_NONEXISTENT = "nonExistent";
	private static final String FILE_EXISTING = "existingFile.txt";
	
	private MkdirApplication mkdirApp;
	private Path dirPath;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		mkdirApp = new MkdirApplication();
		
		Path folderPath = Paths.get(Environment.currentDirectory).resolve(DIR_EXISTING);
		Path filePath = Paths.get(Environment.currentDirectory).resolve(FILE_EXISTING);
		Files.createDirectories(folderPath);
		Files.createFile(filePath);
	}

	@After
	public void tearDown() throws Exception {
		File file = new File(Environment.currentDirectory);
		delete(file);
	}

	@Test
	public void testRelativeMkDir() throws MkdirException {
		mkdirApp.createFolder(DIR_FOLDER1);
		dirPath = Paths.get(Environment.currentDirectory).resolve(DIR_FOLDER1);
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
	}
	
	@Test
	public void testAbsoluteMkDir() throws MkdirException {
		mkdirApp.createFolder(ABS_DIR_FOLDER1);
		dirPath = Paths.get(Environment.currentDirectory).resolve(DIR_FOLDER1);
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
	}
	
	@Test
	public void testMkDirInDir() throws MkdirException {
		mkdirApp.createFolder(DIR_EXISTING + File.separator + DIR_FOLDER1);
		dirPath = Paths.get(Environment.currentDirectory).resolve(DIR_EXISTING + File.separator + DIR_FOLDER1);
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
	}
	
	@Test
	public void testMkMutipleDirs() throws MkdirException {
		String[] folderNames = {DIR_FOLDER1, DIR_FOLDER2, DIR_FOLDER3};
		mkdirApp.createFolder(folderNames);
		for (int i = 0; i < folderNames.length; i++) {
			dirPath = Paths.get(Environment.currentDirectory).resolve(folderNames[i]);
			assertTrue(Files.exists(dirPath));
			assertTrue(Files.isDirectory(dirPath));
		}
	}
	
	@Test
	public void testMkMutipleDirsInSeq() throws MkdirException {
		String[] folderNames = {DIR_FOLDER1, DIR_FOLDER1_1, DIR_FOLDER1_1_1};
		mkdirApp.createFolder(folderNames);
		for (int i = 0; i < folderNames.length; i++) {
			dirPath = Paths.get(Environment.currentDirectory).resolve(folderNames[i]);
			assertTrue(Files.exists(dirPath));
			assertTrue(Files.isDirectory(dirPath));
		}
	}
	
	@Test
	public void testMkDirOfExistingDirWithDiffCaseInUnix() throws MkdirException {
		Assume.assumeTrue(OSValidator.isUnix());
		mkdirApp.createFolder(DIR_EXISTING.toUpperCase(Locale.ENGLISH));
		dirPath = Paths.get(Environment.currentDirectory).resolve(DIR_EXISTING.toUpperCase(Locale.ENGLISH));
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
	}
	
	@Test
	public void testInvalidMkDirOfExistingDirWithDiffCaseInWindows() throws MkdirException {
		Assume.assumeTrue(OSValidator.isWindows());
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: cannot create directory ‘" + DIR_EXISTING.toUpperCase(Locale.ENGLISH) + "’: File exists");
		mkdirApp.createFolder(DIR_EXISTING.toUpperCase(Locale.ENGLISH));
	}
	
	@Test
	public void testMkDirOnlySpacesNameInUnix() throws MkdirException {
		Assume.assumeTrue(OSValidator.isUnix());
		mkdirApp.createFolder("   ");
		dirPath = Paths.get(Environment.currentDirectory).resolve("   ");
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
	}
	
	@Test
	public void testInvalidMkDirOnlySpacesNameInWindows() throws MkdirException {
		Assume.assumeTrue(OSValidator.isWindows());
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: cannot create directory ‘   ’: No such file or directory");
		mkdirApp.createFolder("   ");
	}
	
	@Test
	public void testInvalidMkDirNullName() throws MkdirException {
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: missing operand");
		mkdirApp.createFolder();
	}
	
	@Test
	public void testInvalidMkDirEmptyName() throws MkdirException {
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: cannot create directory ‘’: No such file or directory");
		mkdirApp.createFolder("");
	}
	
	@Test
	public void testInvalidMkDirOfExistingDir() throws MkdirException {
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: cannot create directory ‘existingFolder’: File exists");
		mkdirApp.createFolder(DIR_EXISTING);
	}
	
	@Test
	public void testInvalidMkDirOfExistingFile() throws MkdirException {
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: cannot create directory ‘existingFile.txt’: File exists");
		mkdirApp.createFolder(FILE_EXISTING);
	}
	
	@Test
	public void testInvalidMkDirInNonExistDir() throws MkdirException {
		String newPath = DIR_NONEXISTENT + File.separator + DIR_FOLDER1;
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: cannot create directory ‘" + newPath + "’: No such file or directory");
		mkdirApp.createFolder(newPath);
	}
	
	@Test
	public void testInvalidMkDirWithValidDir() throws MkdirException {
		String newPath = DIR_NONEXISTENT + File.separator + DIR_FOLDER2;
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: cannot create directory ‘" + newPath + "’: No such file or directory");
		mkdirApp.createFolder(DIR_FOLDER1, newPath, DIR_FOLDER3);
	}
	
	@Test
	public void testMkDirRun() throws MkdirException {
		String[] folderNames = {DIR_FOLDER1, DIR_FOLDER1_1, DIR_FOLDER1_1_1};
		mkdirApp.run(folderNames, null, null);
		for (int i = 0; i < folderNames.length; i++) {
			dirPath = Paths.get(Environment.currentDirectory).resolve(folderNames[i]);
			assertTrue(Files.exists(dirPath));
			assertTrue(Files.isDirectory(dirPath));
		}
	}
	
	@Test
	public void testMkDirNullArgs() throws MkdirException {
		thrown.expect(MkdirException.class);
		thrown.expectMessage("mkdir: missing operand");
		mkdirApp.run(null, null, null);
	}
	
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
}
