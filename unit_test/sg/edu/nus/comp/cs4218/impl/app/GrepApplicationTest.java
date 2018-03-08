package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.GrepException;

public class GrepApplicationTest {

	public static final String FILE1_LINE1 = "line 1: This file is named 'file1'.\n";
	public static final String FILE1_LINE2 = "line 2: It is a Text file.\n";
	public static final String FILE1_LINE3 = "line 3: It has a total of 6 lines.\n";
	public static final String FILE1_LINE4 = "line4:ThisLineContainNoSpaces.\n";
	public static final String FILE1_LINE5 = "line  5:  This  line  always  has  two  spaces.\n";
	public static final String FILE1_LINEA = "line a: This line has no numeric.\n";

	public static final String FILE2_LINEA = "line A: This file is named 'file2'.\n";
	public static final String FILE2_LINEB = "line B: It is a Text file.\n";
	public static final String FILE2_LINEC = "line C: It has a total of six lines.\n";
	public static final String FILE2_LINED = "lineD:ThisLineContainNoSpaces.\n";
	public static final String FILE2_LINEE = "line  E:  This  line  always  has  two  spaces.\n";
	public static final String FILE2_LINE1 = "line 1: This line contain a numeric.\n";

	public static final String STREAM_LINE1 = "streamline 1: This is an Inputstream.\n";
	public static final String STREAM_LINE2 = "streamline 2: It has a total of 5 lines.\n";
	public static final String STREAM_LINE3 = "streamline3:ThisLineContainNoSpaces.\n";
	public static final String STREAM_LINE4 = "streamline  4:  This  line  always  has  two  spaces.\n";
	public static final String STREAM_LINEA = "streamline a: This line has no numeric.\n";

	public static final String PATTERN_FILE = "file";
	public static final String PATTERN_HAS = "has";
	public static final String PATTERN_NOMATCH = "NoMatch";
	public static final String PATTERN_DIGIT = "[0-9]";
	public static final String PATTERN_EMPTY = "";
	public static final String PATTERN_TWOSPACES = "  ";
	public static final String PATTERN_SINGLE_A = "\\sa\\s";
	public static final String PATTERN_SDIGIT = "\\s[0-9]\\s";

	public static final String FILE_1 = "file1.txt";
	public static final String FILE_2 = "file2.txt";
	public static final String FILE_EMPTY = "fileEmpty.txt";
	public static final String FILE_NONEXISTENT = "nonExistentFile";
	public static final String FOLDER_1 = "folder1";
	
	public static final String GREP_OPTION = "-v";

	public static final String STRING_COLON = ":";
	public static final String STRING_GREP = "grep: ";

	GrepApplication grepApp;
	String expected, result;
	OutputStream stdout;
	InputStream stdin;

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator + "grep_test_system";
		grepApp = new GrepApplication();
		stdout = new ByteArrayOutputStream();
		stdin = new ByteArrayInputStream((STREAM_LINE1 + STREAM_LINE2 + STREAM_LINE3 + STREAM_LINE4 + STREAM_LINEA).getBytes());
	}

	@Test
	public void testGrepTextFromRelativeFile() {
		expected = FILE1_LINE1 + FILE1_LINE2;		
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false, FILE_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepTextFromAbsoluteFile() {
		expected = FILE1_LINE3 + FILE1_LINE5 + FILE1_LINEA;		
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false, Environment.currentDirectory + File.separator + FILE_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepRegexMatchNumericFromFile() {
		expected = FILE1_LINE1 + FILE1_LINE2 + FILE1_LINE3 + FILE1_LINE4 + FILE1_LINE5;		
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_DIGIT, false, FILE_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepNoMatchedFromFile() {
		expected = "";		
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_NOMATCH, false, FILE_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepEmptyFromFile() {
		expected = FILE1_LINE1 + FILE1_LINE2 + FILE1_LINE3 + FILE1_LINE4 + FILE1_LINE5 + FILE1_LINEA;		
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_EMPTY, false, FILE_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepTwoSpacesFromFile() {
		expected = FILE1_LINE5;		
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_TWOSPACES, false, FILE_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepCharAFromMutipleFiles() {
		String file2FullPath = Environment.currentDirectory + File.separator + FILE_2;
		expected = FILE_1 + STRING_COLON + FILE1_LINE2 +
				FILE_1 + STRING_COLON + FILE1_LINE3 +
				file2FullPath + STRING_COLON + FILE2_LINEB +
				file2FullPath + STRING_COLON + FILE2_LINEC;
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_SINGLE_A, false, FILE_1, file2FullPath);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepEmptyFromEmptyFile() {
		expected = "\n";
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_EMPTY, false, FILE_EMPTY);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvertGrepRegexMatchFromFile() {	
		expected = FILE1_LINEA;
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_DIGIT, true, FILE_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidGrepNullPatternFromFile() {	
		expected = "grep: Pattern Null Pointer Exception";
		try {
			result = grepApp.grepFromMultipleFiles(null, false, FILE_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidGrepNoFile() {	
		expected = "grep: File Null Pointer Exception";
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidGrepEmptyFileName() {	
		expected = "grep: : No such file or directory";
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false, "");
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidGrepSpacesFileName() {	
		expected = "grep:    : No such file or directory";
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false, "   ");
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidGrepFromNonExistentFile() {	
		expected = STRING_GREP + FILE_NONEXISTENT + ": No such file or directory";
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false, FILE_NONEXISTENT);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidGrepFromFolder() {	
		expected = STRING_GREP + FOLDER_1 + ": Is a directory";
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false, FOLDER_1);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testInvalidGrepFromMutipleFiles() {	
		expected = STRING_GREP + FILE_NONEXISTENT + ": No such file or directory";
		try {
			result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false, FILE_1, FILE_NONEXISTENT);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepTextFromInputStream() {	
		expected = STREAM_LINE2 + STREAM_LINE4 + STREAM_LINEA;
		try {
			result = grepApp.grepFromStdin(PATTERN_HAS, false, stdin);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepRegexMatchNumericFromInputStream() {	
		expected = STREAM_LINE1 + STREAM_LINE2 + STREAM_LINE3 + STREAM_LINE4;
		try {
			result = grepApp.grepFromStdin(PATTERN_DIGIT, false, stdin);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testGrepEmptyFromInputStream() {	
		expected = STREAM_LINE1 + STREAM_LINE2 + STREAM_LINE3 + STREAM_LINE4 + STREAM_LINEA;
		try {
			result = grepApp.grepFromStdin(PATTERN_EMPTY, false, stdin);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepTwoSpacesFromInputStream() {	
		expected = STREAM_LINE4;
		try {
			result = grepApp.grepFromStdin(PATTERN_TWOSPACES, false, stdin);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepNoMatchedFromInputStream() {	
		expected = "";
		try {
			result = grepApp.grepFromStdin(PATTERN_NOMATCH, false, stdin);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepEmptyFromEmptyInputStream() {	
		expected = "\n";
		try {
			result = grepApp.grepFromStdin(PATTERN_EMPTY, false, new ByteArrayInputStream("".getBytes()));
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvertGrepRegexMatchNumericFromInputStream() {	
		expected = STREAM_LINEA;
		try {
			result = grepApp.grepFromStdin(PATTERN_DIGIT, true, stdin);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidGrepNullPatternFromInputStream() {	
		expected = "grep: Pattern Null Pointer Exception";
		try {
			result = grepApp.grepFromStdin(null, false, stdin);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidGrepNullInputStream() {	
		expected = "grep: Stdin Null Pointer Exception";
		try {
			result = grepApp.grepFromStdin(PATTERN_HAS, false, null);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepPatternExtractionFoFile() {	
		expected = FILE1_LINE3;
		try {
			String[] strArr = {PATTERN_SDIGIT, FILE_1};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepPatternExtractionForInputStream() {	
		expected = STREAM_LINE2;
		try {
			String[] strArr = {PATTERN_SDIGIT};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepOptionPositionFront() {	
		expected = FILE1_LINEA;
		try {
			String[] strArr = {GREP_OPTION, PATTERN_DIGIT, FILE_1};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepOptionPositionEndOfStream() {	
		expected = STREAM_LINE1 + STREAM_LINE2 +  STREAM_LINE3 +  STREAM_LINE4;
		try {
			String[] strArr = {PATTERN_DIGIT, GREP_OPTION};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepOptionPositionEndOfFile() {	
		expected = FILE2_LINEB + FILE2_LINEC +  FILE2_LINED +  FILE2_LINEE;
		try {
			String[] strArr = {PATTERN_DIGIT, FILE_2, GREP_OPTION};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepOptionPositionBetPatternAndFile() {	
		expected = FILE1_LINEA;
		try {
			String[] strArr = {PATTERN_DIGIT, GREP_OPTION, FILE_1};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testGrepOptionPositionBetFiles() {	
		expected = FILE_2 + STRING_COLON + FILE2_LINEB +
				FILE_2 + STRING_COLON + FILE2_LINEC +
				FILE_2 + STRING_COLON + FILE2_LINED +
				FILE_2 + STRING_COLON + FILE2_LINEE +
				FILE_1 + STRING_COLON + FILE1_LINEA;
		try {
			String[] strArr = {PATTERN_DIGIT, FILE_2, GREP_OPTION, FILE_1};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidGrepNullArgs() {	
		expected = "Usage: grep [OPTION]... PATTERN [FILE]...";
		try {
			grepApp.run(null, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidGrepEmptyArgs() {	
		expected = "Usage: grep [OPTION]... PATTERN [FILE]...";
		try {
			grepApp.run(new String[0], stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidGrepNoPattern() {	
		expected = "Usage: grep [OPTION]... PATTERN [FILE]...";
		try {
			String[] strArr = {GREP_OPTION};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidGrepOption() {	
		expected = "grep: option requires an argument -- 'i'";
		try {
			String[] strArr = {"-i", PATTERN_FILE, FILE_1};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidPatternPosition() {	
		expected = STRING_GREP + PATTERN_FILE + ": No such file or directory";
		try {
			String[] strArr = {FILE_1, PATTERN_FILE};
			grepApp.run(strArr, stdin, stdout);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidGrepNullOutputStream() {	
		expected = "grep: Stdout Null Pointer Exception";
		try {
			String[] strArr = {PATTERN_FILE, FILE_1};
			grepApp.run(strArr, stdin, null);
		} catch (GrepException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
}
