package sg.edu.nus.comp.cs4218.impl;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class GlobTest {
	
	private static ShellImpl TestShell;
	String Result, Expected;
	

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + 
				File.separator + "test_system";
		
		
		
	}

	@Test
	public void testGlobNoPaths() {
		Result=TestShell.globNoPaths("");
		Expected="";
		assertEquals(Result,Expected);
	}

	@Test
	public void testGlobOneFile() {
		Result=TestShell.globOneFile("glob_test_system/*");
		Expected = "glob_test_system/file1.txt  glob_test_system/file2.txt\n";  
		assertEquals(Result,Expected);	 
	}

	@Test
	public void testGlobFilesDirectories() {
		Result=TestShell.globFilesDirectories("glob_test_system/*");
		Expected = "glob_test_system/file1.txt  glob_test_system/file2.txt\n";  
		assertEquals(Result,Expected);	
		
	}

	

}
