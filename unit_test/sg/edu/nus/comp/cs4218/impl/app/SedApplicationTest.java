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
import sg.edu.nus.comp.cs4218.exception.SedException;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class SedApplicationTest {
	private static final String STRING_TEST = "test";
	private static final String STRING_REPLACE = "replaced";
	private static final String STRING_STREAM = "stream";
	private static final String STRING_COMMAND = "s/test/replaced/";

	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system"
			+ File.separator + "sed_test_system";
	private static final String TESTFILE1 = "sedTestFile1.txt";
	private static final String TESTFILE2 = "sedTestFile2.txt";
	private static final String TESTFILE1_HEADER = "This is Sed Test File 1." + OSUtil.NEWLINE;
	private static final String TESTFILE2_HEADER = "This is Sed Test File 2." + OSUtil.NEWLINE;

	private static final String EXP_NULL_POINTER = "sed: Null Pointer Exception";

	private SedApplication sedApp;
	private String expected, result;
	private OutputStream stdout;
	private InputStream stdin;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		Environment.currentDirectory = TEST_DIR;
		sedApp = new SedApplication();
		stdin = new ByteArrayInputStream(
				("Text from Inputstream" + OSUtil.NEWLINE + "stream of texts in stream").getBytes());
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void testReplFileToReplSubStrUsingFirstStrMatchOfRelFile() throws SedException {
		expected = TESTFILE1_HEADER + "1. replaced" + OSUtil.NEWLINE + "2. replaced test" + OSUtil.NEWLINE
				+ "3. replaced test test" + OSUtil.NEWLINE + "4. replaced test test test" + OSUtil.NEWLINE
				+ "5. replacedestestest" + OSUtil.NEWLINE + "6. replacedestestestestest" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, TESTFILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testReplFileToReplSubStrUsingFirstStrMatchOfAbsFile() throws SedException {
		expected = TESTFILE2_HEADER + "1. replacedestestestestest" + OSUtil.NEWLINE + "2. replacedestestest"
				+ OSUtil.NEWLINE + "3. replaced test test test" + OSUtil.NEWLINE + "4. replaced test test"
				+ OSUtil.NEWLINE + "5. replaced test" + OSUtil.NEWLINE + "6. replaced" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1,
				Environment.currentDirectory + File.separator + TESTFILE2);
		assertEquals(expected, result);
	}

	@Test
	public void testReplFileToReplSubStrUsingThirdStrMatch() throws SedException {
		expected = TESTFILE2_HEADER + "1. testestestesreplacedest" + OSUtil.NEWLINE + "2. testestestest"
				+ OSUtil.NEWLINE + "3. test test replaced test" + OSUtil.NEWLINE + "4. test test replaced"
				+ OSUtil.NEWLINE + "5. test test" + OSUtil.NEWLINE + "6. test" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 3, TESTFILE2);
		assertEquals(expected, result);
	}

	@Test
	public void testReplFileToAddLeadingAngleBracketUsingFirstRegexMatch() throws SedException {
		expected = "> This is Sed Test File 1." + OSUtil.NEWLINE + "> 1. test" + OSUtil.NEWLINE + "> 2. test test"
				+ OSUtil.NEWLINE + "> 3. test test test" + OSUtil.NEWLINE + "> 4. test test test test" + OSUtil.NEWLINE
				+ "> 5. testestestest" + OSUtil.NEWLINE + "> 6. testestestestestest" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInFile("^", "> ", 1, TESTFILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testReplFileToRemoveLeadingNumUsingFirstRegexMatch() throws SedException {
		expected = TESTFILE2_HEADER + "testestestestestest" + OSUtil.NEWLINE + "testestestest" + OSUtil.NEWLINE
				+ "test test test test" + OSUtil.NEWLINE + "test test test" + OSUtil.NEWLINE + "test test"
				+ OSUtil.NEWLINE + "test" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInFile("[0-9].\\s+", "", 1, TESTFILE2);
		assertEquals(expected, result);
	}

	@Test
	public void testReplFileToReplNothingUsingNoMatchedRegex() throws SedException {
		expected = TESTFILE1_HEADER + "1. test" + OSUtil.NEWLINE + "2. test test" + OSUtil.NEWLINE + "3. test test test"
				+ OSUtil.NEWLINE + "4. test test test test" + OSUtil.NEWLINE + "5. testestestest" + OSUtil.NEWLINE
				+ "6. testestestestestest" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInFile("NoMatched", STRING_REPLACE, 1, TESTFILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testReplFileToThrowsSedExpUsingNullPattern() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		sedApp.replaceSubstringInFile(null, STRING_REPLACE, 1, TESTFILE1);
	}

	@Test
	public void testReplFileToThrowsSedExpUsingEmptyStrPattern() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: regular expression cannot be empty");
		sedApp.replaceSubstringInFile("", STRING_REPLACE, 1, TESTFILE1);
	}

	@Test
	public void testReplFileToThrowsSedExpUsingNullReplacement() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		sedApp.replaceSubstringInFile(STRING_TEST, null, 1, TESTFILE1);
	}

	@Test
	public void testReplFileToThrowsSedExpUsingZerothOccurance() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: command may not be zero");
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 0, TESTFILE1);
	}

	@Test
	public void testReplFileToThrowsSedExpUsingNegativeOccurance() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: command may not be negative");
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, TESTFILE1);
	}

	@Test
	public void testReplFileToThrowsSedExpUsingNullFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, null);
	}

	@Test
	public void testReplFileToThrowsSedExpUsingNonExistentFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: can't read nonExistentFile: No such file or directory");
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, "nonExistentFile");
	}

	@Test
	public void testReplFileToThrowsSedExpUsingEmptyFileName() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: can't read : No such file or directory");
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, "");
	}

	@Test
	public void testReplFileToThrowsSedExpUsingSpacesFileName() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: can't read    : No such file or directory");
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, "   ");
	}

	@Test
	public void testReplFileToThrowsSedExpUsingDir() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: read error on folder1: Is a directory");
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, "folder1");
	}

	@Test
	public void testReplStdinToReplSubStrUsingFirstStrMatch() throws SedException {
		expected = "Text from Inputreplaced" + OSUtil.NEWLINE + "replaced of texts in stream" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, 1, stdin);
		assertEquals(expected, result);
	}

	@Test
	public void testReplStdinToReplSubStrUsingSecondStrMatch() throws SedException {
		expected = "Text from Inputstream" + OSUtil.NEWLINE + "stream of texts in replaced" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, 2, stdin);
		assertEquals(expected, result);
	}

	@Test
	public void testReplStdinToAddLeadingAngleBracketUsingFirstRegexMatch() throws SedException {
		expected = "> Text from Inputstream" + OSUtil.NEWLINE + "> stream of texts in stream" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInStdin("^", "> ", 1, stdin);
		assertEquals(expected, result);
	}

	@Test
	public void testReplStdinToRemoveSubStrUsingFirstRegexMatch() throws SedException {
		expected = "Text from Input" + OSUtil.NEWLINE + "of texts in stream" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInStdin("\\s*stream\\s*", "", 1, stdin);
		assertEquals(expected, result);
	}

	@Test
	public void testReplStdinToReplNothingUsingNoMatchedRegex() throws SedException {
		expected = "Text from Inputstream" + OSUtil.NEWLINE + "stream of texts in stream" + OSUtil.NEWLINE;
		result = sedApp.replaceSubstringInStdin("noMatched", STRING_REPLACE, 1, stdin);
		assertEquals(expected, result);
	}

	@Test
	public void testReplStdinToThrowsSedExpUsingNullPattern() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		sedApp.replaceSubstringInStdin(null, STRING_REPLACE, 1, stdin);
	}

	@Test
	public void testReplStdinToThrowsSedExpUsingEmptyStrPattern() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: regular expression cannot be empty");
		sedApp.replaceSubstringInStdin("", STRING_REPLACE, 1, stdin);
	}

	@Test
	public void testReplStdinToThrowsSedExpUsingNullReplacement() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		sedApp.replaceSubstringInStdin(STRING_STREAM, null, 1, stdin);
	}

	@Test
	public void testReplStdinToThrowsSedExpUsingZerothMatch() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: command may not be zero");
		sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, 0, stdin);
	}

	@Test
	public void testReplStdinToThrowsSedExpUsingNegativeMatch() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: command may not be negative");
		sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, -1, stdin);
	}

	@Test
	public void testReplStdinToThrowsSedExpUsingNullInputStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		sedApp.replaceSubstringInStdin(null, STRING_REPLACE, 1, null);
	}

	@Test
	public void testRunToReplSubStrUsingNumericSepChar() throws SedException {
		expected = TESTFILE1_HEADER + "1. replaced" + OSUtil.NEWLINE + "2. replaced test" + OSUtil.NEWLINE
				+ "3. replaced test test" + OSUtil.NEWLINE + "4. replaced test test test" + OSUtil.NEWLINE
				+ "5. replacedestestest" + OSUtil.NEWLINE + "6. replacedestestestestest";
		String[] strArr = { "s1test1replaced1", TESTFILE1 };
		sedApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToReplSubStrUsingAlphabetSepChar() throws SedException {
		expected = "Text from Inputreplaced" + OSUtil.NEWLINE + "replaced of texts in stream";
		String[] strArr = { "szstreamzreplacedz" };
		sedApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToReplSubStrInMutiFileUsingMutiFilesInArg() throws SedException {
		expected = TESTFILE1_HEADER + "1. replaced" + OSUtil.NEWLINE + "2. replaced test" + OSUtil.NEWLINE
				+ "3. replaced test test" + OSUtil.NEWLINE + "4. replaced test test test" + OSUtil.NEWLINE
				+ "5. replacedestestest" + OSUtil.NEWLINE + "6. replacedestestestestest" + OSUtil.NEWLINE
				+ TESTFILE2_HEADER + "1. replacedestestestestest" + OSUtil.NEWLINE + "2. replacedestestest"
				+ OSUtil.NEWLINE + "3. replaced test test test" + OSUtil.NEWLINE + "4. replaced test test"
				+ OSUtil.NEWLINE + "5. replaced test" + OSUtil.NEWLINE + "6. replaced";
		String[] strArr = { STRING_COMMAND, TESTFILE1, TESTFILE2 };
		sedApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToThrowsSedExpUsingMutiFilesWithInvalidFileInArg() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: can't read nonExistentFile: No such file or directory");
		String[] strArr = { STRING_COMMAND, TESTFILE1, "nonExistentFile" };
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingEmptyArg() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: REPLACEMENT is not specified");
		String[] strArr = {};
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingInvalidCmd() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 1: unknown command: 'i'");
		String[] strArr = { "i", TESTFILE1 };
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingNoSepChar() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 1: unterminated: 's'");
		String[] strArr = { "s", TESTFILE1 };
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingUnterminatedCmd() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 3: unterminated: 's'");
		String[] strArr = { "s/s", TESTFILE1 };
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingEmptyRegex() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 0: no previous regular expression");
		String[] strArr = { "s//replaced/", TESTFILE1 };
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingNonNumericNthMatch() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 15: unknown options to 's'");
		String[] strArr = { "s/test/replaced/???", TESTFILE1 };
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingZerothMatch() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 15: number option to 's' command may not be zero");
		String[] strArr = { "s/test/replaced/0", TESTFILE1 };
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingCommandAfterFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 10: unknown options to 's'");
		String[] strArr = { TESTFILE1, STRING_COMMAND };
		sedApp.run(strArr, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingNullArg() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: REPLACEMENT is not specified");
		sedApp.run(null, null, stdout);
	}

	@Test
	public void testRunToThrowsSedExpUsingNullOutputStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);
		String[] strArr = { STRING_COMMAND, TESTFILE1 };
		sedApp.run(strArr, null, null);
	}
}
