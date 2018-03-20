package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.optr.QuoteOperator;

public class QuoteOperatorTest {
	private static final String NOT_CLOSED_EXP = "shell: Quotes not closed";
	private static final String NULL_PTR_EXP = "shell: Null Pointer Exception";
	
	private QuoteOperator quoteOptr;
	private String[] input, output, expected;
	private String inputStr;
	private char sepChar;
	private Integer[] outputInt, expectedInt;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup() {
		quoteOptr = new QuoteOperator();
		input = output = expected = new String[0];
		inputStr = "";
		sepChar = '\u0000';
		outputInt = expectedInt = new Integer[0];
	}

	@Test
	public void testRemoveNoQuotes() throws AbstractApplicationException, ShellException {
		expected = new String[] {"some message"};
		input = new String[] {"some message"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testRemoveSQClosed() throws AbstractApplicationException, ShellException {
		expected = new String[] {"message within single quotes"};
		input = new String[] {"'message within single quotes'"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testRemoveSQMultiple() throws AbstractApplicationException, ShellException {
		expected = new String[] {"SQ message 1", "SQ message 2"};
		input = new String[] {"'SQ message 1'", "'SQ message 2'"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveSQWithinWord() throws AbstractApplicationException, ShellException {
		expected = new String[] {"SQ out in in out SQ"};
		input = new String[] {"SQ out i'n i'n out SQ"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveDQClosed() throws AbstractApplicationException, ShellException {
		expected = new String[] {"message within double quotes"};
		input = new String[] {"\"message within double quotes\""};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testRemoveDQMultiple() throws AbstractApplicationException, ShellException {
		expected = new String[] {"DQ message 1", "DQ message 2"};
		input = new String[] {"\"DQ message 1\"", "\"DQ message 2\""};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveDQWithinWord() throws AbstractApplicationException, ShellException {
		expected = new String[] {"DQ out in in out DQ"};
		input = new String[] {"DQ out i\"n i\"n out DQ"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveBQClosed() throws AbstractApplicationException, ShellException {
		expected = new String[] {"`message within back quotes`"};
		input = new String[] {"`message within back quotes`"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testRemoveBQMultiple() throws AbstractApplicationException, ShellException {
		expected = new String[] {"`BQ message 1`", "`BQ message 2`"};
		input = new String[] {"`BQ message 1`", "`BQ message 2`"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveBQWithinWord() throws AbstractApplicationException, ShellException {
		expected = new String[] {"BQ out i`n i`n out BQ"};
		input = new String[] {"BQ out i`n i`n out BQ"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testRemoveMultipleQuotes() throws AbstractApplicationException, ShellException {
		expected = new String[] {"SQ DQ `BQ`"};
		input = new String[] {"'SQ' \"DQ\" `BQ`"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testRemoveDQWithinSQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {"\"DQ within SQ\""};
		input = new String[] {"'\"DQ within SQ\"'"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveBQWithinSQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {"`BQ within SQ`"};
		input = new String[] {"'`BQ within SQ`'"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testRemoveSQWithinDQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {"'SQ within DQ'"};
		input = new String[] {"\"'SQ within DQ'\""};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}

	@Test
	public void testRemoveBQWithinDQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {"`BQ within DQ`"};
		input = new String[] {"\"`BQ within DQ`\""};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveSQWithinBQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {"`SQ 'within' BQ`"};
		input = new String[] {"`SQ 'within' BQ`"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveDQWithinBQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {"`DQ \"within\" BQ`"};
		input = new String[] {"`DQ \"within\" BQ`"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testRemoveMultiplElements() throws AbstractApplicationException, ShellException {
		expected = new String[] {"SQ", "DQ", "`BQ`"};
		input = new String[] {"'SQ'", "\"DQ\"", "`BQ`"};
		output = quoteOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testInvalidRemoveSQUnclosed() throws AbstractApplicationException, ShellException {
		input = new String[] {"'Unclosed SQ"};	
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP);     
		quoteOptr.evaluate(input);
	}
	
	@Test
	public void testInvalidDQRemoveUnclosed() throws AbstractApplicationException, ShellException {
		input = new String[] {"\"Unclosed DQ"};
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP);   
		quoteOptr.evaluate(input);
	}
	
	@Test
	public void testInvalidRemoveBQUnclosed() throws AbstractApplicationException, ShellException {
		input = new String[] {"`Unclosed BQ"};
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP);
		quoteOptr.evaluate(input);
	}
	
	@Test
	public void testInvalidRemoveNullInput() throws AbstractApplicationException, ShellException {
		input = new String[] {null};
		thrown.expect(ShellException.class);
        thrown.expectMessage(NULL_PTR_EXP);
		quoteOptr.evaluate(input);
	}
	
	@Test
	public void testGetCharIndicesNoQuotes() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {4, 10};
		inputStr = "cmd1; cmd2; cmd3";
		sepChar = ';';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}

	@Test
	public void testGetCharIndicesSQClosed() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {11, 16};
		inputStr = "'cmd1;cmd2';cmd3;cmd4";
		sepChar = ';';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}

	@Test
	public void testGetCharIndicesSQMultiple() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {5};
		inputStr = "cmd1 ; 'cmd2 ; cmd3''cmd4 ; cmd5'";
		sepChar = ';';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testGetCharIndicesSQWithinWord() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {4, 18};
		inputStr = "cmd1; cmd'2 ;cmd'3; cmd4";
		sepChar = ';';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testGetCharIndicesDQClosed() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {11, 16};
		inputStr = "\"cmd1|cmd2\"|cmd3|cmd4";
		sepChar = '|';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}

	@Test
	public void testGetCharIndicesDQMultiple() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {5};
		inputStr = "cmd1 | \"cmd2 | cmd3\"\"cmd4 | cmd5\"";
		sepChar = '|';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testGetCharIndicesDQWithinWord() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {4, 18};
		inputStr = "cmd1| cmd\"2| cmd\"3| cmd4";
		sepChar = '|';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testGetCharIndicesBQClosed() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {11, 16};
		inputStr = "`cmd1 arg1` arg2 arg3";
		sepChar = ' ';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}

	@Test
	public void testGetCharIndicesBQMultiple() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {4};
		inputStr = "cmd1 `cmd2 arg2``cmd3 arg3`";
		sepChar = ' ';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testGetCharIndicesBQWithinWord() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {4, 20};
		inputStr = "cmd1 arg`cmd2 arg2`1 arg2";
		sepChar = ' ';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}

	@Test
	public void testGetCharIndicesMultipleQuotes() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {5, 11};
		inputStr = "'S Q' \"D Q\" `B Q`";
		sepChar = ' ';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}

	@Test
	public void testGetCharIndicesDQWithinSQ() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {0, 3, 22, 25};
		inputStr = "\"DQ\" '\"DQ within SQ\"' \"DQ\"";
		sepChar = '"';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testGetCharIndicesBQWithinSQ() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {0, 3, 22, 25};
		inputStr = "`BQ` '`BQ within SQ`' `BQ`";
		sepChar = '`';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}

	@Test
	public void testGetCharIndicesSQWithinDQ() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {0, 3, 22, 25};
		inputStr = "'SQ' \"'SQ within DQ'\" 'SQ'";
		sepChar = '\'';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}

	@Test
	public void testGetCharIndicesBQWithinDQ() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {0, 3, 6, 19, 22, 25};
		inputStr = "`BQ` \"`BQ within DQ`\" `BQ`";
		sepChar = '`';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testGetCharIndicesSQWithinBQ() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {0, 3, 22, 25};
		inputStr = "'SQ' `'SQ within BQ'` 'SQ'";
		sepChar = '\'';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testGetCharIndicesDQWithinBQ() throws AbstractApplicationException, ShellException {
		expectedInt = new Integer[] {0, 3, 22, 25};
		inputStr = "\"DQ\" `\"DQ within BQ\"` \"DQ\"";
		sepChar = '"';
		outputInt = quoteOptr.getIndices(inputStr, sepChar);
		assertArrayEquals(expectedInt, outputInt);
	}
	
	@Test
	public void testInvalidGetCharIndicesSQUnclosed() throws AbstractApplicationException, ShellException {
		inputStr = "'cmd1; cmd2; cmd3; cmd4";
		sepChar = ';';
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP); 
		quoteOptr.getIndices(inputStr, sepChar);
	}
	
	@Test
	public void testInvalidDQGetCharIndicesUnclosed() throws AbstractApplicationException, ShellException {
		inputStr = "\"cmd1 | cmd2 | cmd3 | cmd4";
		sepChar = '|';
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP);   
        quoteOptr.getIndices(inputStr, sepChar);
	}
	
	@Test
	public void testInvalidGetCharIndicesBQUnclosed() throws AbstractApplicationException, ShellException {
		inputStr = "`cmd1 arg1 arg3 arg4";
		sepChar = ' ';	
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP);    
        quoteOptr.getIndices(inputStr, sepChar);
	}
	
	@Test
	public void testInvalidGetCharIndicesNullInput() throws AbstractApplicationException, ShellException {
		inputStr = null;
		sepChar = ' ';	
		thrown.expect(ShellException.class);
        thrown.expectMessage(NULL_PTR_EXP);    
        quoteOptr.getIndices(inputStr, sepChar);
	}
}
