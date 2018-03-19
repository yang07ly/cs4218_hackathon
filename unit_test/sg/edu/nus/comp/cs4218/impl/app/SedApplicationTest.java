package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.*;

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

public class SedApplicationTest {
	private static final String STRING_TEST = "test";
	private static final String STRING_REPLACE = "replaced";
	private static final String STRING_STREAM = "stream";
	private static final String STRING_COMMAND = "s/test/replaced/";
	
	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system" + File.separator + "sed_test_system";
	private static final String TESTFILE1 = "sedTestFile1.txt";
	private static final String TESTFILE2 = "sedTestFile2.txt";
	private static final String TESTFILE1_HEADER = "This is Sed Test File 1.\n";
	private static final String TESTFILE2_HEADER = "This is Sed Test File 2.\n";
	
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
		stdin = new ByteArrayInputStream("Text from Inputstream\nstream of texts in stream".getBytes());
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void testReplaceRelativeFile() throws SedException {
		expected = TESTFILE1_HEADER + 
				"1. replaced\n" + 
				"2. replaced test\n" + 
				"3. replaced test test\n" + 
				"4. replaced test test test\n" + 
				"5. replacedestestest\n" + 
				"6. replacedestestestestest\n";
		result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, TESTFILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testReplaceAbsoluteFile() throws SedException {
		expected = TESTFILE2_HEADER + 
				"1. replacedestestestestest\n" + 
				"2. replacedestestest\n" + 
				"3. replaced test test test\n" + 
				"4. replaced test test\n" + 
				"5. replaced test\n" + 
				"6. replaced\n";
		result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, Environment.currentDirectory + File.separator + TESTFILE2);
		assertEquals(expected, result);
	}

	@Test
	public void testReplaceThirdOccuranceInFile() throws SedException {
		expected = TESTFILE2_HEADER + 
				"1. testestestesreplacedest\n" + 
				"2. testestestest\n" + 
				"3. test test replaced test\n" + 
				"4. test test replaced\n" + 
				"5. test test\n" + 
				"6. test\n";
		result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 3, TESTFILE2);
		assertEquals(expected, result);
	}

	@Test
	public void testRegexMatchAddLeadBracketInFile() throws SedException {
		expected = "> This is Sed Test File 1.\n" + 
				"> 1. test\n" + 
				"> 2. test test\n" + 
				"> 3. test test test\n" + 
				"> 4. test test test test\n" + 
				"> 5. testestestest\n" + 
				"> 6. testestestestestest\n";
		result = sedApp.replaceSubstringInFile("^", "> ", 1, TESTFILE1);
		assertEquals(expected, result);
	}

	@Test
	public void testRegexMatchRemoveLeadNumInFile() throws SedException {
		expected = TESTFILE2_HEADER + 
				"testestestestestest\n" + 
				"testestestest\n" + 
				"test test test test\n" + 
				"test test test\n" + 
				"test test\n" + 
				"test\n";
		result = sedApp.replaceSubstringInFile("[0-9].\\s+", "", 1, TESTFILE2);
		assertEquals(expected, result);
	}

	@Test
	public void testNoRegexMatchInFile() throws SedException {
		expected = TESTFILE1_HEADER + 
				"1. test\n" + 
				"2. test test\n" + 
				"3. test test test\n" + 
				"4. test test test test\n" + 
				"5. testestestest\n" + 
				"6. testestestestestest\n";
		result = sedApp.replaceSubstringInFile("NoMatched", STRING_REPLACE, 1, TESTFILE1);
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullPatternInFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);	
		sedApp.replaceSubstringInFile(null, STRING_REPLACE, 1, TESTFILE1);
	}
	
	@Test
	public void testInvalidEmptyPatternInFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: regular expression cannot be empty");	
		sedApp.replaceSubstringInFile("", STRING_REPLACE, 1, TESTFILE1);
	}
	
	@Test
	public void testInvalidNullReplacementInFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);	
		sedApp.replaceSubstringInFile(STRING_TEST, null, 1, TESTFILE1);
	}
	
	@Test
	public void testInvalidReplaceIndexZeroInFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: command may not be zero");	
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 0, TESTFILE1);
	}
	
	@Test
	public void testInvalidReplaceIndexNegativeInFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: command may not be negative");	
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, TESTFILE1);
	}
	
	@Test
	public void testInvalidNullFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);	
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, null);
	}
	
	@Test
	public void testInvalidNonExistentFile() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: can't read nonExistentFile: No such file or directory");	
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, "nonExistentFile");
	}
	
	@Test
	public void testInvalidEmptyFileName() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: can't read : No such file or directory");	
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, "");
	}
	
	@Test
	public void testInvalidSpacesFileName() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: can't read    : No such file or directory");	
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, "   ");
	}
	
	@Test
	public void testInvalidFolderReplace() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: read error on folder1: Is a directory");	
		sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 1, "folder1");
	}

	@Test
	public void testReplaceFromInputStream() throws SedException {
		expected = "Text from Inputreplaced\nreplaced of texts in stream\n";
		result = sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, 1, stdin);
		assertEquals(expected, result);
	}
	
	@Test
	public void testReplaceSecondOccuranceInStream() throws SedException {
		expected = "Text from Inputstream\nstream of texts in replaced\n";
		result = sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, 2, stdin);
		assertEquals(expected, result);
	}

	@Test
	public void testRegexMatchAddLeadBracketInStream() throws SedException {
		expected = "> Text from Inputstream\n> stream of texts in stream\n";
		result = sedApp.replaceSubstringInStdin("^", "> ", 1, stdin);
		assertEquals(expected, result);
	}

	@Test
	public void testRegexMatchRemoveTextInStream() throws SedException {
		expected = "Text from Input\nof texts in stream\n";
		result = sedApp.replaceSubstringInStdin("\\s*stream\\s*", "", 1, stdin);
		assertEquals(expected, result);
	}

	@Test
	public void testNoRegexMatchInStream() throws SedException {
		expected = "Text from Inputstream\nstream of texts in stream\n";
		result = sedApp.replaceSubstringInStdin("noMatched", STRING_REPLACE, 1, stdin);
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullPatternInStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);	
		sedApp.replaceSubstringInStdin(null, STRING_REPLACE, 1, stdin);
	}
	
	@Test
	public void testInvalidEmptyPatternInStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: regular expression cannot be empty");	
		sedApp.replaceSubstringInStdin("", STRING_REPLACE, 1, stdin);
	}
	
	@Test
	public void testInvalidNullReplacementInStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);	
		sedApp.replaceSubstringInStdin(STRING_STREAM, null, 1, stdin);
	}
	
	@Test
	public void testInvalidReplaceIndexZeroInStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: command may not be zero");	
		sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, 0, stdin);
	}
	
	@Test
	public void testInvalidReplaceIndexNegativeInStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: command may not be negative");	
		sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, -1, stdin);
	}
	
	@Test
	public void testInvalidNullStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);	
		sedApp.replaceSubstringInStdin(null, STRING_REPLACE, 1, null);
	}
	
	@Test
	public void testOtherSeparatingCharNumeric() throws SedException {
		expected = TESTFILE1_HEADER + 
				"1. replaced\n" + 
				"2. replaced test\n" + 
				"3. replaced test test\n" + 
				"4. replaced test test test\n" + 
				"5. replacedestestest\n" + 
				"6. replacedestestestestest";
		String[] strArr = {"s1test1replaced1", TESTFILE1};
		sedApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}
	
	@Test
	public void testOtherSeparatingCharAlpha() throws SedException {
		expected = "Text from Inputreplaced\nreplaced of texts in stream";
		String[] strArr = {"szstreamzreplacedz"};
		sedApp.run(strArr, stdin, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}
	
	@Test
	public void testMultipleFiles() throws SedException {
		expected = TESTFILE1_HEADER + 
				"1. replaced\n" + 
				"2. replaced test\n" + 
				"3. replaced test test\n" + 
				"4. replaced test test test\n" + 
				"5. replacedestestest\n" + 
				"6. replacedestestestestest\n" + 
				TESTFILE2_HEADER + 
				"1. replacedestestestestest\n" + 
				"2. replacedestestest\n" + 
				"3. replaced test test test\n" + 
				"4. replaced test test\n" + 
				"5. replaced test\n" + 
				"6. replaced";
		String[] strArr = {STRING_COMMAND, TESTFILE1, TESTFILE2};
		sedApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNonExistentFileInValidFiles() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: can't read nonExistentFile: No such file or directory");	
		String[] strArr = {STRING_COMMAND, TESTFILE1, "nonExistentFile"};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidEmptyArguments() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: REPLACEMENT is not specified");	
		String[] strArr = {};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidCommand() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 1: unknown command: 'i'");	
		String[] strArr = {"i", TESTFILE1};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidMissingSeparatingChar() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 1: unterminated: 's'");	
		String[] strArr = {"s", TESTFILE1};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidUnterminatedCommand() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 3: unterminated: 's'");	
		String[] strArr = {"s/s", TESTFILE1};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidEmptyRegExp() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 0: no previous regular expression");	
		String[] strArr = {"s//replaced/", TESTFILE1};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidReplaceIndexChar() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 15: unknown options to 's'");	
		String[] strArr = {"s/test/replaced/???", TESTFILE1};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidReplaceIndex() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 15: number option to 's' command may not be zero");	
		String[] strArr = {"s/test/replaced/0", TESTFILE1};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidCommandPosition() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: char 10: unknown options to 's'");	
		String[] strArr = {TESTFILE1, STRING_COMMAND};
		sedApp.run(strArr, null, stdout);
	}
	
	@Test
	public void testInvalidNullArgs() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage("sed: REPLACEMENT is not specified");	
		sedApp.run(null, null, stdout);
	}
	
	@Test
	public void testInvalidNullOutputStream() throws SedException {
		thrown.expect(SedException.class);
		thrown.expectMessage(EXP_NULL_POINTER);	
		String[] strArr = {STRING_COMMAND, TESTFILE1};
		sedApp.run(strArr, null, null);
	}
}
