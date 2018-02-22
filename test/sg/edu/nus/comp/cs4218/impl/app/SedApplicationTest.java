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
import sg.edu.nus.comp.cs4218.exception.SedException;

public class SedApplicationTest {
	public static final String STRING_TEST = "test";
	public static final String STRING_REPLACE = "replaced";
	public static final String STRING_STREAM = "stream";
	public static final String STRING_COMMAND = "s/test/replaced/";
	
	public static final String TESTFILE1 = "sedTestFile1.txt";
	public static final String TESTFILE2 = "sedTestFile2.txt";
	public static final String TESTFILE1_HEADER = "This is Sed Test File 1.\n";
	public static final String TESTFILE2_HEADER = "This is Sed Test File 2.\n";
	
	public static final String EXP_NULL_POINTER = "sed: Null Pointer Exception";
	
	SedApplication sedApp;
	String expected, result;
	OutputStream stdout;
	InputStream stdin;
		
	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator + "sed_test_system";;
		sedApp = new SedApplication();
		stdin = new ByteArrayInputStream("Text from Inputstream\nstream of texts in stream".getBytes());
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void testReplaceAllRelativeFile() {
		expected = TESTFILE1_HEADER + 
				"1. replaced\n" + 
				"2. replaced replaced\n" + 
				"3. replaced replaced replaced\n" + 
				"4. replaced replaced replaced replaced\n" + 
				"5. replacedesreplacedest\n" + 
				"6. replacedesreplacedesreplacedest\n";
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, TESTFILE1);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testReplaceAllAbsoluteFile() {
		expected = TESTFILE2_HEADER + 
				"1. replacedesreplacedesreplacedest\n" + 
				"2. replacedesreplacedest\n" + 
				"3. replaced replaced replaced replaced\n" + 
				"4. replaced replaced replaced\n" + 
				"5. replaced replaced\n" + 
				"6. replaced\n";
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, Environment.currentDirectory + File.separator + TESTFILE2);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testReplaceThirdOccuranceInFile() {
		expected = TESTFILE2_HEADER + 
				"1. testestestesreplacedest\n" + 
				"2. testestestest\n" + 
				"3. test test replaced test\n" + 
				"4. test test replaced\n" + 
				"5. test test\n" + 
				"6. test\n";
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 3, TESTFILE2);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testRegexMatchAddLeadBracketInFile() {
		expected = "> This is Sed Test File 1.\n" + 
				"> 1. test\n" + 
				"> 2. test test\n" + 
				"> 3. test test test\n" + 
				"> 4. test test test test\n" + 
				"> 5. testestestest\n" + 
				"> 6. testestestestestest\n";
		try {
			result = sedApp.replaceSubstringInFile("^", "> ", -1, TESTFILE1);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testRegexMatchRemoveLeadNumInFile() {
		expected = TESTFILE2_HEADER + 
				"testestestestestest\n" + 
				"testestestest\n" + 
				"test test test test\n" + 
				"test test test\n" + 
				"test test\n" + 
				"test\n";
		try {
			result = sedApp.replaceSubstringInFile("[0-9].\\s+", "", -1, TESTFILE2);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testNoRegexMatchInFile() {
		expected = TESTFILE1_HEADER + 
				"1. test\n" + 
				"2. test test\n" + 
				"3. test test test\n" + 
				"4. test test test test\n" + 
				"5. testestestest\n" + 
				"6. testestestestestest\n";
		try {
			result = sedApp.replaceSubstringInFile("NoMatched", STRING_REPLACE, -1, TESTFILE1);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullPatternInFile() {
		expected = EXP_NULL_POINTER;
		try {
			result = sedApp.replaceSubstringInFile(null, STRING_REPLACE, -1, TESTFILE1);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidEmptyPatternInFile() {
		expected = "sed: regular expression cannot be empty";
		try {
			result = sedApp.replaceSubstringInFile("", STRING_REPLACE, -1, TESTFILE1);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullReplacementInFile() {
		expected = EXP_NULL_POINTER;
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, null, -1, TESTFILE1);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidReplaceIndexZeroInFile() {
		expected = "sed: command may not be zero";
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, 0, TESTFILE1);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullFile() {
		expected = EXP_NULL_POINTER;
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, null);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNonExistentFile() {
		expected = "sed: can't read nonExistentFile: No such file or directory";
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, "nonExistentFile");
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidEmptyFileName() {
		expected = "sed: can't read : No such file or directory";
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, "");
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidSpacesFileName() {
		expected = "sed: can't read    : No such file or directory";
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, "   ");
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidFolderReplace() {
		expected = "sed: read error on folder1: Is a directory";
		try {
			result = sedApp.replaceSubstringInFile(STRING_TEST, STRING_REPLACE, -1, "folder1");
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testReplaceFromInputStream() {
		expected = "Text from Inputreplaced\nreplaced of texts in replaced\n";
		try {
			result = sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, -1, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testReplaceSecondOccuranceInStream() {
		expected = "Text from Inputstream\nstream of texts in replaced\n";
		try {
			result = sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, 2, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testRegexMatchAddLeadBracketInStream() {
		expected = "> Text from Inputstream\n> stream of texts in stream\n";
		try {
			result = sedApp.replaceSubstringInStdin("^", "> ", -1, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testRegexMatchRemoveTextInStream() {
		expected = "Text from Input\nof texts in\n";
		try {
			result = sedApp.replaceSubstringInStdin("\\s*stream\\s*", "", -1, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}

	@Test
	public void testNoRegexMatchInStream() {
		expected = "Text from Inputstream\nstream of texts in stream\n";
		try {
			result = sedApp.replaceSubstringInStdin("noMatched", STRING_REPLACE, -1, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullPatternInStream() {
		expected = EXP_NULL_POINTER;
		try {
			result = sedApp.replaceSubstringInStdin(null, STRING_REPLACE, -1, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidEmptyPatternInStream() {
		expected = "sed: regular expression cannot be empty";
		try {
			result = sedApp.replaceSubstringInStdin("", STRING_REPLACE, -1, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullReplacementInStream() {
		expected = EXP_NULL_POINTER;
		try {
			result = sedApp.replaceSubstringInStdin(STRING_STREAM, null, -1, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidReplaceIndexZeroInStream() {
		expected = "sed: command may not be zero";
		try {
			result = sedApp.replaceSubstringInStdin(STRING_STREAM, STRING_REPLACE, 0, stdin);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullStream() {
		expected = EXP_NULL_POINTER;
		try {
			result = sedApp.replaceSubstringInStdin(null, STRING_REPLACE, -1, null);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testOtherSeparactingChar() {
		expected = TESTFILE1_HEADER + 
				"1. replaced\n" + 
				"2. replaced replaced\n" + 
				"3. replaced replaced replaced\n" + 
				"4. replaced replaced replaced replaced\n" + 
				"5. replacedesreplacedest\n" + 
				"6. replacedesreplacedesreplacedest\n";
		try {
			String[] strArr = {"s1test1replaced1", TESTFILE1};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testMultipleFiles() {
		expected = TESTFILE1_HEADER + 
				"1. replaced\n" + 
				"2. replaced replaced\n" + 
				"3. replaced replaced replaced\n" + 
				"4. replaced replaced replaced replaced\n" + 
				"5. replacedesreplacedest\n" + 
				"6. replacedesreplacedesreplacedest\n" + 
				TESTFILE2_HEADER + 
				"1. replacedesreplacedesreplacedest\n" + 
				"2. replacedesreplacedest\n" + 
				"3. replaced replaced replaced replaced\n" + 
				"4. replaced replaced replaced\n" + 
				"5. replaced replaced\n" + 
				"6. replaced\n";
		try {
			String[] strArr = {STRING_COMMAND, TESTFILE1, TESTFILE2};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidNonExistentFileInValidFiles() {
		expected = "sed: can't read nonExistentFile: No such file or directory";
		try {
			String[] strArr = {STRING_COMMAND, TESTFILE1, "nonExistentFile"};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidEmptyArguments() {
		expected = "sed: REPLACEMENT is not specified";
		try {
			String[] strArr = {};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidCommand() {
		expected = "sed: char 1: unknown command: 'i'";
		try {
			String[] strArr = {"i", TESTFILE1};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidUnterminatedCommand() {
		expected = "sed: char 3: unterminated: 's'";
		try {
			String[] strArr = {"s/s", TESTFILE1};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidEmptyRegExp() {
		expected = "sed: char 0: no previous regular expression";
		try {
			String[] strArr = {"s//replaced/", TESTFILE1};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidReplaceIndexChar() {
		expected = "sed: char 15: unknown options to 's'";
		try {
			String[] strArr = {"s/test/replaced/???", TESTFILE1};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidReplaceIndex() {
		expected = "sed: char 15: number option to 's' command may not be zero";
		try {
			String[] strArr = {"s/test/replaced/0", TESTFILE1};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidCommandPosition() {
		expected = "sed: char 1: unknown command: 'f'";
		try {
			String[] strArr = {TESTFILE1, STRING_COMMAND};
			sedApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidNullArgs() {
		expected = "REPLACEMENT is not specified";
		try {
			sedApp.run(null, null, stdout);
			result = stdout.toString();
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	public void testInvalidNullOutputStream() {
		expected = EXP_NULL_POINTER;
		try {
			String[] strArr = {STRING_COMMAND, TESTFILE1};
			sedApp.run(strArr, null, null);
		} catch (SedException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
}
