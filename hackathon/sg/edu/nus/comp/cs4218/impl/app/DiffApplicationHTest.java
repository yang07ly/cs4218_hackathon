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
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.DiffException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class DiffApplicationHTest {

	private static final String DIR2 = "dir2";
	private static final String AND = " and ";
	private static final String FILES = "Files ";
	private static final String FILE2_WITH_BLANKS = "file2_withBlankLines.txt";
	private static final String ASDF_NOT_FOUND = "diff: asdf: No such file or directory";
	private static final String LINE2_LINE6 = "< line2" + System.getProperty("line.separator") + "> line6";
	private static final String EMPTY_ARGS = "diff: : No such file or directory";
	private static final String FILE_NOT_FOUND = "diff:  : No such file or directory";
	private static final String DIR1 = "dir1";
	private static final String FILE3_TXT = "file3.txt";
	private static final String FILE2_TXT = "file2.txt";
	private static final String FILE1_WITH_BLANKS = "file1_withBlankLines.txt";
	private static final String FILE1_COPY_TXT = "file1_copy.txt";
	private static final String FILE1_TXT = "file1.txt";
	private static final String ASDF = "asdf";
	private static final String NEWLINE = System.getProperty("line.separator");
	DiffApplication app;
	OutputStream outputStream;
	String expected, output;
	String currentDir;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "diff_test_system";
		currentDir = Environment.currentDirectory + File.separator;
		app = new DiffApplication();
		outputStream = new ByteArrayOutputStream();
		output = "";
		expected = "";
	}

	/*
	 * Test Case DIFF1
	 */
	@Test
	public void testDiffBlackBox() throws AbstractApplicationException, ShellException {
		ShellImpl shell = new ShellImpl();
		shell.runApp("diff", null, null, null);
	}
	
	/*
	 * Test Case DIFF2
	 */
	@Test
	public void testEOFBlank() throws DiffException {
		Environment.currentDirectory = System.getProperty("user.dir");
		String[] args = {"A.txt", "B.txt"};
		app.run(args, null, outputStream);
		output = outputStream.toString();
		expected = ">";
		assertEquals(expected, output);
	}
}
