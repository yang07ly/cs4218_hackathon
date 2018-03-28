package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.GrepException;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class GrepApplicationTest {

	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system"
			+ File.separator + "grep_test_system";
	private static final String FILE_1 = "file1.txt";
	private static final String FILE_2 = "file2.txt";
	private static final String ABS_FILE2 = TEST_DIR + File.separator + FILE_2;
	private static final String FILE_EMPTY = "fileEmpty.txt";
	private static final String FILE_NONEXISTENT = "nonExistentFile";
	private static final String FOLDER_1 = "folder1";

	private static final String FILE1_LINE1 = "line 1: This file is named 'file1'." + OSUtil.NEWLINE;
	private static final String FILE1_LINE2 = "line 2: It is a Text file." + OSUtil.NEWLINE;
	private static final String FILE1_LINE3 = "line 3: It has a total of 6 lines." + OSUtil.NEWLINE;
	private static final String FILE1_LINE4 = "line4:ThisLineContainNoSpaces." + OSUtil.NEWLINE;
	private static final String FILE1_LINE5 = "line  5:  This  line  always  has  two  spaces." + OSUtil.NEWLINE;
	private static final String FILE1_LINEA = "line a: This line has no numeric." + OSUtil.NEWLINE;

	private static final String FILE2_LINEA = "line A: This file is named 'file2'." + OSUtil.NEWLINE;
	private static final String FILE2_LINEB = "line B: It is a Text file." + OSUtil.NEWLINE;
	private static final String FILE2_LINEC = "line C: It has a total of six lines." + OSUtil.NEWLINE;
	private static final String FILE2_LINED = "lineD:ThisLineContainNoSpaces." + OSUtil.NEWLINE;
	private static final String FILE2_LINEE = "line  E:  This  line  always  has  two  spaces." + OSUtil.NEWLINE;
	private static final String FILE2_LINE1 = "line 1: This line contain a numeric." + OSUtil.NEWLINE;

	private static final String STREAM_LINE1 = "streamline 1: This is an Inputstream." + OSUtil.NEWLINE;
	private static final String STREAM_LINE2 = "streamline 2: It has a total of 5 lines." + OSUtil.NEWLINE;
	private static final String STREAM_LINE3 = "streamline3:ThisLineContainNoSpaces." + OSUtil.NEWLINE;
	private static final String STREAM_LINE4 = "streamline  4:  This  line  always  has  two  spaces." + OSUtil.NEWLINE;
	private static final String STREAM_LINEA = "streamline a: This line has no numeric." + OSUtil.NEWLINE;

	private static final String PATTERN_FILE = "file";
	private static final String PATTERN_HAS = "has";
	private static final String PATTERN_NOMATCH = "NoMatch";
	private static final String PATTERN_DIGIT = "[0-9]";
	private static final String PATTERN_EMPTY = "";
	private static final String PATTERN_TWOSPACES = "  ";
	private static final String PATTERN_SINGLE_A = "\\sa\\s";
	private static final String PATTERN_SDIGIT = "\\s[0-9]\\s";

	private static final String GREP_OPTION = "-v";

	private static final String STR_COLON = ": ";
	private static final String STR_GREP = "grep: ";
	private static final String STR_EMPTY = "";

	private static final String EXP_NULL_POINTER = "grep: Null Pointer Exception";

	private GrepApplication grepApp;
	private String expected, result;
	private OutputStream stdout;
	private InputStream stdin;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		Environment.currentDirectory = TEST_DIR;
		grepApp = new GrepApplication();
		stdout = new ByteArrayOutputStream();
		stdin = new ByteArrayInputStream(
				(STREAM_LINE1 + STREAM_LINE2 + STREAM_LINE3 + STREAM_LINE4 + STREAM_LINEA).getBytes());
	}

	@Test
	public void testMutiFileToGrepLineUsingRelFile() throws GrepException {
		expected = FILE1_LINE1 + FILE1_LINE2;
		result = grepApp.grepFromMultipleFiles(PATTERN_FILE, false, FILE_1);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepLineUsingAbsFile() throws GrepException {
		expected = FILE1_LINE3 + FILE1_LINE5 + FILE1_LINEA;
		result = grepApp.grepFromMultipleFiles(PATTERN_HAS, false,
				Environment.currentDirectory + File.separator + FILE_1);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepLineUsingRegexPattern() throws GrepException {
		expected = FILE1_LINE1 + FILE1_LINE2 + FILE1_LINE3 + FILE1_LINE4 + FILE1_LINE5;
		result = grepApp.grepFromMultipleFiles(PATTERN_DIGIT, false, FILE_1);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepNothingUsingUnmatchedPattern() throws GrepException {
		expected = "";
		result = grepApp.grepFromMultipleFiles(PATTERN_NOMATCH, false, FILE_1);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepAllLineUsingEmptyPattern() throws GrepException {
		expected = FILE2_LINEA + FILE2_LINEB + FILE2_LINEC + FILE2_LINED + FILE2_LINEE + FILE2_LINE1;
		result = grepApp.grepFromMultipleFiles(PATTERN_EMPTY, false, FILE_2);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepLineWithTwoSpacesUsingTwoSpacesPattern() throws GrepException {
		expected = FILE1_LINE5;
		result = grepApp.grepFromMultipleFiles(PATTERN_TWOSPACES, false, FILE_1);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepLineFromMutiFilesUsingMutiFiles() throws GrepException {
		expected = FILE_1 + STR_COLON + FILE1_LINE2 + FILE_1 + STR_COLON + FILE1_LINE3 + ABS_FILE2 + STR_COLON
				+ FILE2_LINEB + ABS_FILE2 + STR_COLON + FILE2_LINEC + ABS_FILE2 + STR_COLON + FILE2_LINE1;
		result = grepApp.grepFromMultipleFiles(PATTERN_SINGLE_A, false, FILE_1, ABS_FILE2);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepEmptyUsingEmptyPatternAndEmptyFile() throws GrepException {
		expected = STR_EMPTY;
		result = grepApp.grepFromMultipleFiles(PATTERN_EMPTY, false, FILE_EMPTY);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepEmptyFromMutiFilesUsingEmptyPatternAndMutiEmptyFiles() throws GrepException {
		expected = STR_EMPTY;
		result = grepApp.grepFromMultipleFiles(PATTERN_EMPTY, false, FILE_EMPTY, FILE_EMPTY);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToGrepLineUsingInvertMatch() throws GrepException {
		expected = FILE1_LINEA;
		result = grepApp.grepFromMultipleFiles(PATTERN_DIGIT, true, FILE_1);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testMutiFileToThrowsGrepExpUsingNullPattern() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		grepApp.grepFromMultipleFiles(null, false, FILE_1);
	}

	@Test
	public void testMutiFileToThrowsGrepExpUsingNoFile() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		grepApp.grepFromMultipleFiles(PATTERN_FILE, false);
	}

	@Test
	public void testMutiFileToThrowsGrepExpUsingEmptyFileName() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage("grep: : No such file or directory");
		grepApp.grepFromMultipleFiles(PATTERN_FILE, false, "");
	}

	@Test
	public void testMutiFileToThrowsGrepExpUsingSpacesAsFileName() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage("grep:    : No such file or directory");
		grepApp.grepFromMultipleFiles(PATTERN_FILE, false, "   ");
	}

	@Test
	public void testMutiFileToThrowsGrepExpUsingNonExistentFile() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(STR_GREP + FILE_NONEXISTENT + ": No such file or directory");
		grepApp.grepFromMultipleFiles(PATTERN_FILE, false, FILE_NONEXISTENT);
	}

	@Test
	public void testMutiFileToThrowsGrepExpUsingDir() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(STR_GREP + FOLDER_1 + ": Is a directory");
		grepApp.grepFromMultipleFiles(PATTERN_FILE, false, FOLDER_1);
	}

	@Test
	public void testMutiFileToThrowsGrepExpUsingMutiFilesWithNonExistentFile() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(STR_GREP + FILE_NONEXISTENT + ": No such file or directory");
		grepApp.grepFromMultipleFiles(PATTERN_FILE, false, FILE_1, FILE_NONEXISTENT);
	}

	@Test
	public void testStdinToGrepLineUsingInputStream() throws GrepException {
		expected = STREAM_LINE2 + STREAM_LINE4 + STREAM_LINEA;
		result = grepApp.grepFromStdin(PATTERN_HAS, false, stdin);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testStdinToGrepLineUsingRegexPattern() throws GrepException {
		expected = STREAM_LINE1 + STREAM_LINE2 + STREAM_LINE3 + STREAM_LINE4;
		result = grepApp.grepFromStdin(PATTERN_DIGIT, false, stdin);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testStdinToGrepLineUsingEmptyInputStream() throws GrepException {
		expected = STREAM_LINE1 + STREAM_LINE2 + STREAM_LINE3 + STREAM_LINE4 + STREAM_LINEA;
		result = grepApp.grepFromStdin(PATTERN_EMPTY, false, stdin);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testStdinToGrepLineUsingTwoSpacePattern() throws GrepException {
		expected = STREAM_LINE4;
		result = grepApp.grepFromStdin(PATTERN_TWOSPACES, false, stdin);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testStdinToGrepEmptyUsingNomatchedPattern() throws GrepException {
		expected = "";
		result = grepApp.grepFromStdin(PATTERN_NOMATCH, false, stdin);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testStdinToGrepEmptyUsingEmptyPatternAndEmptyInputStream() throws GrepException {
		expected = "";
		result = grepApp.grepFromStdin(PATTERN_EMPTY, false, new ByteArrayInputStream("".getBytes()));
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testStdinToGrepLineUsingInvertMatch() throws GrepException {
		expected = STREAM_LINEA;
		result = grepApp.grepFromStdin(PATTERN_DIGIT, true, stdin);
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testStdinToThrowsGrepExpUsingNullPattern() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		grepApp.grepFromStdin(null, false, stdin);
	}

	@Test
	public void testStdinToThrowsGrepExpUsingNullInputStream() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		grepApp.grepFromStdin(PATTERN_HAS, false, null);
	}

	@Test
	public void testRunToGrepLineUsingFile() throws GrepException {
		expected = FILE1_LINE3;
		String[] strArr = { PATTERN_SDIGIT, FILE_1 };

		grepApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testRunToGrepLineUsingInputStream() throws GrepException {
		expected = STREAM_LINE2;
		String[] strArr = { PATTERN_SDIGIT };

		grepApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testRunToGrepLineUsingOptionAtFront() throws GrepException {
		expected = FILE1_LINEA;
		String[] strArr = { GREP_OPTION, PATTERN_DIGIT, FILE_1 };

		grepApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testRunToGrepLineUsingOptionAfterPattern() throws GrepException {
		expected = STREAM_LINEA;
		String[] strArr = { PATTERN_DIGIT, GREP_OPTION };

		grepApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testRunToGrepLineUsingOptionAfterFile() throws GrepException {
		expected = FILE2_LINEB + FILE2_LINEC + FILE2_LINED + FILE2_LINEE;
		String[] strArr = { PATTERN_DIGIT, FILE_2, GREP_OPTION };

		grepApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testRunToGrepLineUsingOptionBtwPatternAndFile() throws GrepException {
		expected = FILE1_LINEA;
		String[] strArr = { PATTERN_DIGIT, GREP_OPTION, FILE_1 };

		grepApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testRunToGrepLineUsingOptionBtwTwoFiles() throws GrepException {
		expected = FILE_2 + STR_COLON + FILE2_LINEB + FILE_2 + STR_COLON + FILE2_LINEC + FILE_2 + STR_COLON
				+ FILE2_LINED + FILE_2 + STR_COLON + FILE2_LINEE + FILE_1 + STR_COLON + FILE1_LINEA;
		String[] strArr = { PATTERN_DIGIT, FILE_2, GREP_OPTION, FILE_1 };

		grepApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected.trim(), result);
	}

	@Test
	public void testRunToThrowGrepExpUsingNullArg() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		grepApp.run(null, stdin, stdout);
	}

	@Test
	public void testRunToThrowGrepExpUsingEmptyArg() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(STR_GREP + "pattern is not specified");
		grepApp.run(new String[0], stdin, stdout);
	}

	@Test
	public void testRunToThrowGrepExpUsingNoPattern() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(STR_GREP + "pattern is not specified");
		String[] strArr = { GREP_OPTION };
		grepApp.run(strArr, stdin, stdout);
	}

	@Test
	public void testRunToThrowGrepExpUsingInvalidGrepOption() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage("grep: invalid option -- 'i'");
		String[] strArr = { "-i", PATTERN_FILE, FILE_1 };
		grepApp.run(strArr, stdin, stdout);
	}

	@Test
	public void testRunToThrowGrepExpUsingUnspecifiedGrepOption() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage("grep: file: No such file or directory");
		String[] strArr = { "-", PATTERN_FILE, FILE_1 };
		grepApp.run(strArr, stdin, stdout);
	}

	@Test
	public void testRunToThrowGrepExpUsingPatternAfterFile() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(STR_GREP + PATTERN_FILE + ": No such file or directory");
		String[] strArr = { FILE_1, PATTERN_FILE };
		grepApp.run(strArr, stdin, stdout);
	}

	@Test
	public void testRunToThrowGrepExpUsingNullOutputStream() throws GrepException {
		thrown.expect(GrepException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		String[] strArr = { PATTERN_FILE, FILE_1 };
		grepApp.run(strArr, stdin, null);
	}
}
