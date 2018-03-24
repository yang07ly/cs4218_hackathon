package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class QuoteOperatorTest {
	private static final String NOT_CLOSED_EXP = "shell: Quotes not closed";
	private static final String NULL_PTR_EXP = "shell: Null Pointer Exception";

	private QuoteOperator quoteOptr;
	private CommandString cmd, expected;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		quoteOptr = new QuoteOperator();
		cmd = new CommandString();
		expected = new CommandString();
	}

	@Test
	public void testEvaluateNoQuotes() throws AbstractApplicationException, ShellException {
		expected = new CommandString("message");
		expected.setCharEscaped(0, false, false, false, false, false, false, false);

		cmd = new CommandString("message");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateSQClosed() throws AbstractApplicationException, ShellException {
		expected = new CommandString("SQ message");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("'SQ message'");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateSQMultiple() throws AbstractApplicationException, ShellException {
		expected = new CommandString("IN OUT IN");
		expected.setCharEscaped(0, true, true, false, false, false, false, false, true, true);

		cmd = new CommandString("'IN' OUT 'IN'");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateSQWithinWord() throws AbstractApplicationException, ShellException {
		expected = new CommandString("OI IO");
		expected.setCharEscaped(0, false, true, true, true, false);

		cmd = new CommandString("O'I I'O");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateDQClosed() throws AbstractApplicationException, ShellException {
		expected = new CommandString("DQ message");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("\"DQ message\"");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateDQMultiple() throws AbstractApplicationException, ShellException {
		expected = new CommandString("IN OUT IN");
		expected.setCharEscaped(0, true, true, false, false, false, false, false, true, true);

		cmd = new CommandString("\"IN\" OUT \"IN\"");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateDQWithinWord() throws AbstractApplicationException, ShellException {
		expected = new CommandString("OI IO");
		expected.setCharEscaped(0, false, true, true, true, false);

		cmd = new CommandString("O\"I I\"O");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateBQClosed() throws AbstractApplicationException, ShellException {
		expected = new CommandString("`BQ message`");
		expected.setCharEscaped(0, false, true, true, true, true, true, true, true, true, true, true, false);

		cmd = new CommandString("`BQ message`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateBQMultiple() throws AbstractApplicationException, ShellException {
		expected = new CommandString("`IN` OUT `IN`");
		expected.setCharEscaped(0, false, true, true, false, false, false, false, false, false, false, true, true,
				false);

		cmd = new CommandString("`IN` OUT `IN`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateBQWithinWord() throws AbstractApplicationException, ShellException {
		expected = new CommandString("O`I I`O");
		expected.setCharEscaped(0, false, false, true, true, true, false, false);

		cmd = new CommandString("O`I I`O");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateMultipleQuotes() throws AbstractApplicationException, ShellException {
		expected = new CommandString("SQ DQ `BQ`");
		expected.setCharEscaped(0, true, true, false, true, true, false, false, true, true, false);

		cmd = new CommandString("'SQ' \"DQ\" `BQ`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateDQWithinSQ() throws AbstractApplicationException, ShellException {
		expected = new CommandString("DQ \"IN\" SQ");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("'DQ \"IN\" SQ'");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateBQWithinSQ() throws AbstractApplicationException, ShellException {
		expected = new CommandString("BQ `IN` SQ");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("'BQ `IN` SQ'");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateSQWithinDQ() throws AbstractApplicationException, ShellException {
		expected = new CommandString("SQ 'IN' DQ");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("\"SQ 'IN' DQ\"");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateBQWithinDQ() throws AbstractApplicationException, ShellException {
		expected = new CommandString("BQ `IN` DQ");
		expected.setCharEscaped(0, true, true, true, false, false, false, false, true, true, true);

		cmd = new CommandString("\"BQ `IN` DQ\"");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateSQWithinBQ() throws AbstractApplicationException, ShellException {
		expected = new CommandString("`SQ 'IN' BQ`");
		expected.setCharEscaped(0, false, true, true, true, true, true, true, true, true, true, true, false);

		cmd = new CommandString("`SQ 'IN' BQ`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvaluateDQWithinBQ() throws AbstractApplicationException, ShellException {
		expected = new CommandString("`DQ \"IN\" BQ`");
		expected.setCharEscaped(0, false, true, true, true, true, true, true, true, true, true, true, false);

		cmd = new CommandString("`DQ \"IN\" BQ`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
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
