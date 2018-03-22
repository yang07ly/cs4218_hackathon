package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
import sg.edu.nus.comp.cs4218.impl.optr.QuoteOperator;

public class QuoteOperatorTest {
	private static final String NOT_CLOSED_EXP = "shell: Quotes not closed";
	private static final String NULL_PTR_EXP = "shell: Null Pointer Exception";
	
	private QuoteOperator quoteOptr;
	private CommandString cmd;
	private String expectedStr;
	private Boolean[] expectedBool;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup() {
		quoteOptr = new QuoteOperator();
		cmd = new CommandString();
		expectedStr = "";
		expectedBool = new Boolean[0];
	}

	@Test
	public void testEvaluateNoQuotes() throws AbstractApplicationException, ShellException {
		expectedStr = "message";
		expectedBool = new Boolean[] {false, false, false, false, false, false, false};
		
		cmd = new CommandString("message");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}

	@Test
	public void testEvaluateSQClosed() throws AbstractApplicationException, ShellException {
		expectedStr = "SQ message";
		expectedBool = new Boolean[] {true, true, true, true, true, true, true, true, true, true};
		
		cmd = new CommandString("'SQ message'");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}

	@Test
	public void testEvaluateSQMultiple() throws AbstractApplicationException, ShellException {
		expectedStr = "IN OUT IN";
		expectedBool = new Boolean[] {true, true, false, false, false, false, false, true, true};
		
		cmd = new CommandString("'IN' OUT 'IN'");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testEvaluateSQWithinWord() throws AbstractApplicationException, ShellException {
		expectedStr = "OI IO";
		expectedBool = new Boolean[] {false, true, true, true, false};
		
		cmd = new CommandString("O'I I'O");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testEvaluateDQClosed() throws AbstractApplicationException, ShellException {
		expectedStr = "DQ message";
		expectedBool = new Boolean[] {true, true, true, true, true, true, true, true, true, true};
		
		cmd = new CommandString("\"DQ message\"");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}

	@Test
	public void testEvaluateDQMultiple() throws AbstractApplicationException, ShellException {
		expectedStr = "IN OUT IN";
		expectedBool = new Boolean[] {true, true, false, false, false, false, false, true, true};
		
		cmd = new CommandString("\"IN\" OUT \"IN\"");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testEvaluateDQWithinWord() throws AbstractApplicationException, ShellException {
		expectedStr = "OI IO";
		expectedBool = new Boolean[] {false, true, true, true, false};
		
		cmd = new CommandString("O\"I I\"O");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testEvaluateBQClosed() throws AbstractApplicationException, ShellException {
		expectedStr = "`BQ message`";
		expectedBool = new Boolean[] {false, true, true, true, true, true, true, true, true, true, true, false};
		
		cmd = new CommandString("`BQ message`");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}

	@Test
	public void testEvaluateBQMultiple() throws AbstractApplicationException, ShellException {
		expectedStr = "`IN` OUT `IN`";
		expectedBool = new Boolean[] {false, true, true, false, false, false, false, false, false, false, true, true, false};
		
		cmd = new CommandString("`IN` OUT `IN`");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testEvaluateBQWithinWord() throws AbstractApplicationException, ShellException {
		expectedStr = "O`I I`O";
		expectedBool = new Boolean[] {false, false, true, true, true, false, false};
		
		cmd = new CommandString("O`I I`O");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}

	@Test
	public void testEvaluateMultipleQuotes() throws AbstractApplicationException, ShellException {
		expectedStr = "SQ DQ `BQ`";
		expectedBool = new Boolean[] {true, true, false, true, true, false, false, true, true, false};
		
		cmd = new CommandString("'SQ' \"DQ\" `BQ`");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}

	@Test
	public void testEvaluateDQWithinSQ() throws AbstractApplicationException, ShellException {
		expectedStr = "DQ \"IN\" SQ";
		expectedBool = new Boolean[] {true, true, true, true, true, true, true, true, true, true};
		
		cmd = new CommandString("'DQ \"IN\" SQ'");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testEvaluateBQWithinSQ() throws AbstractApplicationException, ShellException {
		expectedStr = "BQ `IN` SQ";
		expectedBool = new Boolean[] {true, true, true, true, true, true, true, true, true, true};
		
		cmd = new CommandString("'BQ `IN` SQ'");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}

	@Test
	public void testEvaluateSQWithinDQ() throws AbstractApplicationException, ShellException {
		expectedStr = "SQ 'IN' DQ";
		expectedBool = new Boolean[] {true, true, true, true, true, true, true, true, true, true};
		
		cmd = new CommandString("\"SQ 'IN' DQ\"");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}

	@Test
	public void testEvaluateBQWithinDQ() throws AbstractApplicationException, ShellException {
		expectedStr = "BQ `IN` DQ";
		expectedBool = new Boolean[] {true, true, true, false, false, false, false, true, true, true};
		
		cmd = new CommandString("\"BQ `IN` DQ\"");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testEvaluateSQWithinBQ() throws AbstractApplicationException, ShellException {
		expectedStr = "`SQ 'IN' BQ`";
		expectedBool = new Boolean[] {false, true, true, true, true, true, true, true, true, true, true, false};
		
		cmd = new CommandString("`SQ 'IN' BQ`");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testEvaluateDQWithinBQ() throws AbstractApplicationException, ShellException {
		expectedStr = "`DQ \"IN\" BQ`";
		expectedBool = new Boolean[] {false, true, true, true, true, true, true, true, true, true, true, false};
		
		cmd = new CommandString("`DQ \"IN\" BQ`");
		quoteOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testInvalidRemoveSQUnclosed() throws AbstractApplicationException, ShellException {
		cmd = new CommandString("'Unclosed SQ");
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP);  
		quoteOptr.evaluate(cmd);
	}
	
	@Test
	public void testInvalidDQUnclosed() throws AbstractApplicationException, ShellException {
		cmd = new CommandString("\"Unclosed DQ");
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP);  
		quoteOptr.evaluate(cmd);
	}
	
	@Test
	public void testInvalidBQUnclosed() throws AbstractApplicationException, ShellException {
		cmd = new CommandString("`Unclosed BQ");
		thrown.expect(ShellException.class);
        thrown.expectMessage(NOT_CLOSED_EXP);  
		quoteOptr.evaluate(cmd);
	}
	
	@Test
	public void testInvalidNullInput() throws AbstractApplicationException, ShellException {
		thrown.expect(ShellException.class);
        thrown.expectMessage(NULL_PTR_EXP);  
		quoteOptr.evaluate(null);
	}
}
