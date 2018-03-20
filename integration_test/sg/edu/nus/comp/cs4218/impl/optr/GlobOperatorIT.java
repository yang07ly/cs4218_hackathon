package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class GlobOperatorIT {

	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system" + File.separator + "glob_test_system";
	
	private static final String FILE1 = "file1.txt";
	private static final String FILE2 = "file2.txt";
	private static final String FILE_W_SPACES = "file name with spaces.txt";
	private static final String FILE_SPACES = "   ";
	private static final String FILE_NONEXISTENT = "nonExistentFile*";
	private static final String FOLDER1 = "folder1";
	private static final String FOLDER2 = "folder2";
	private static final String FOLDER_WS = "folder name with spaces";
	
	private static final String ABS_FOLDER1 = TEST_DIR + File.separator + FOLDER1;
	private static final String ABS_FOLDER_WS = ABS_FOLDER1 + File.separator + FOLDER_WS;
	private static final String ABS_FILE1 = ABS_FOLDER_WS + File.separator + FILE1;
	private static final String ABS_FILE2 = ABS_FOLDER_WS + File.separator + FILE2;
	
	private static final String FOLDER_PARTIAL = "folder";
	private static final String FILE_PARTIAL = "file";
	private static final String FILE_WS_PARTIAL = "file name with";
	private static final String TXT_PARTIAL = ".txt";
	
	private static final String WILDCARD = "*";
	private static final String FILE_WILDCARD = FILE_PARTIAL + WILDCARD;
	
	private static final String BQUOTE = "`";
	private static final String SQUOTE = "'";
	private static final String DQUOTE = "\"";
	
	private static final String EXP_QUOTES = "shell: Quotes not closed";
	
	private GlobOperator globOptr;
	private String[] input, output, expected;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		globOptr = new GlobOperator(new ShellImpl());
		input = output = expected = new String[0];
	}
	
	@Test
	public void testGlobWildCardWithNoQuotes() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {FILE_WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardInDQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {DQUOTE + FILE_WILDCARD + DQUOTE};
		input = new String[] {DQUOTE + FILE_WILDCARD + DQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardInSQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {SQUOTE + FILE_WILDCARD + SQUOTE};
		input = new String[] {SQUOTE + FILE_WILDCARD + SQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardInBQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {BQUOTE + FILE_WILDCARD + BQUOTE};
		input = new String[] {BQUOTE + FILE_WILDCARD + BQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardAfterDQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {DQUOTE + FILE_PARTIAL + DQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardAfterSQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {SQUOTE + FILE_PARTIAL + SQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardAfterBQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {BQUOTE + FILE_PARTIAL + BQUOTE + WILDCARD};
		input = new String[] {BQUOTE + FILE_PARTIAL + BQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardBeforeDQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {WILDCARD + DQUOTE + TXT_PARTIAL + DQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardBeforeSQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {WILDCARD + SQUOTE + TXT_PARTIAL + SQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardBeforeBQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {WILDCARD + BQUOTE + TXT_PARTIAL + BQUOTE};
		input = new String[] {WILDCARD + BQUOTE + TXT_PARTIAL + BQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardBetween2DQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {DQUOTE + FILE_PARTIAL + DQUOTE + WILDCARD + DQUOTE + TXT_PARTIAL + DQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardBetween2SQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2};
		input = new String[] {SQUOTE + FILE_PARTIAL + SQUOTE + WILDCARD + SQUOTE + TXT_PARTIAL + SQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardBetween2BQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {BQUOTE + FILE_PARTIAL + BQUOTE + WILDCARD + BQUOTE + TXT_PARTIAL + BQUOTE};
		input = new String[] {BQUOTE + FILE_PARTIAL + BQUOTE + WILDCARD + BQUOTE + TXT_PARTIAL + BQUOTE};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardAfterDQOfNameWithSpace() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES};
		input = new String[] {DQUOTE + FILE_WS_PARTIAL + DQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardAfterSQOfNameWithSpace() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES};
		input = new String[] {SQUOTE + FILE_WS_PARTIAL + SQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobWildCardAfterBQOfNameWithSpace() throws AbstractApplicationException, ShellException {
		expected = new String[] {BQUOTE + FILE_WS_PARTIAL + BQUOTE + WILDCARD};
		input = new String[] {BQUOTE + FILE_WS_PARTIAL + BQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	@Test
	public void testGlobAllFilesWithDQWithinPath() throws AbstractApplicationException, ShellException {
		expected = new String[] {ABS_FILE1, ABS_FILE2};
		input = new String[] {ABS_FOLDER1 + File.separator + DQUOTE + FOLDER_WS + DQUOTE + File.separator + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobAllFilesWithSQWithinPath() throws AbstractApplicationException, ShellException {
		expected = new String[] {ABS_FILE1, ABS_FILE2};
		input = new String[] {ABS_FOLDER1 + File.separator + SQUOTE + FOLDER_WS + SQUOTE + File.separator + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobAllFilesWithBQWithinPath() throws AbstractApplicationException, ShellException {
		expected = new String[] {ABS_FOLDER1 + File.separator + BQUOTE + FOLDER_WS + BQUOTE + File.separator + WILDCARD};
		input = new String[] {ABS_FOLDER1 + File.separator + BQUOTE + FOLDER_WS + BQUOTE + File.separator + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobEmptyWithinQuotesWithWildCard() throws AbstractApplicationException, ShellException {
		expected = new String[] {FILE_W_SPACES, FILE1, FILE2, FOLDER1, FOLDER2};
		input = new String[] {DQUOTE + DQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobSpacesWithinQuotesWithWildCard() throws AbstractApplicationException, ShellException {
		expected = new String[] {SQUOTE + FILE_SPACES + SQUOTE + WILDCARD};
		input = new String[] {SQUOTE + FILE_SPACES + SQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobNoMatchWithQuotes() throws AbstractApplicationException, ShellException {
		expected = new String[] {DQUOTE + FILE_NONEXISTENT + DQUOTE + WILDCARD};
		input = new String[] {DQUOTE + FILE_NONEXISTENT + DQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testGlobMutipleElementsWithQuotes() throws AbstractApplicationException, ShellException {
		expected = new String[] {FOLDER1, FOLDER2, FILE_W_SPACES, FILE1, FILE2, BQUOTE + FILE_PARTIAL + BQUOTE + WILDCARD};
		input = new String[] {DQUOTE + FOLDER_PARTIAL + DQUOTE + WILDCARD, 
				SQUOTE + FILE_PARTIAL + SQUOTE + WILDCARD + SQUOTE + TXT_PARTIAL + SQUOTE,
				BQUOTE + FILE_PARTIAL + BQUOTE + WILDCARD};
		output = globOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testInvalidUnclosedQuotes() throws AbstractApplicationException, ShellException {
		input = new String[] {DQUOTE + FILE_PARTIAL + WILDCARD};
		thrown.expect(ShellException.class);
        thrown.expectMessage(EXP_QUOTES);
		globOptr.evaluate(input);
	}
	
	@Test
	public void testInvalidUnclosedQuotesInOneElement() throws AbstractApplicationException, ShellException {
		input = new String[] {DQUOTE + FILE_PARTIAL + DQUOTE + WILDCARD, WILDCARD + TXT_PARTIAL + SQUOTE};
		thrown.expect(ShellException.class);
        thrown.expectMessage(EXP_QUOTES);
		globOptr.evaluate(input);
	}
}
