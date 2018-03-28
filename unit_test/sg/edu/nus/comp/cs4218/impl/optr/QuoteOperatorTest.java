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
	public void testEvalToDoNothingUsingStrWithNoQuote() throws AbstractApplicationException, ShellException {
		expected = new CommandString("message");
		expected.setCharEscaped(0, false, false, false, false, false, false, false);

		cmd = new CommandString("message");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwSingleQuoteUsingStrInSingleQuote()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("SQ message");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("'SQ message'");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwSingleQuoteUsingMultiStrInSingleQuote()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("IN OUT IN");
		expected.setCharEscaped(0, true, true, false, false, false, false, false, true, true);

		cmd = new CommandString("'IN' OUT 'IN'");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwSingleQuoteUsingSingleQuoteBtwText()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("OI IO");
		expected.setCharEscaped(0, false, true, true, true, false);

		cmd = new CommandString("O'I I'O");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwDoubleQuoteUsingStrInDoubleQuote()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("DQ message");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("\"DQ message\"");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwDoubleQuoteUsingMultiStrInDoubleQuote()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("IN OUT IN");
		expected.setCharEscaped(0, true, true, false, false, false, false, false, true, true);

		cmd = new CommandString("\"IN\" OUT \"IN\"");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwDoubleQuoteUsingDoubleQuoteBtwText()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("OI IO");
		expected.setCharEscaped(0, false, true, true, true, false);

		cmd = new CommandString("O\"I I\"O");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwBackQuoteUsingStrInBackQuote()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("`BQ message`");
		expected.setCharEscaped(0, false, true, true, true, true, true, true, true, true, true, true, false);

		cmd = new CommandString("`BQ message`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwBackQuoteUsingMultiStrInBackQuote()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("`IN` OUT `IN`");
		expected.setCharEscaped(0, false, true, true, false, false, false, false, false, false, false, true, true,
				false);

		cmd = new CommandString("`IN` OUT `IN`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwBackQuoteUsingBackQuoteBtwText()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("O`I I`O");
		expected.setCharEscaped(0, false, false, true, true, true, false, false);

		cmd = new CommandString("O`I I`O");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscToCharBtwQuotesUsingMultiStrInQuotes()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("SQ DQ `BQ`");
		expected.setCharEscaped(0, true, true, false, true, true, false, false, true, true, false);

		cmd = new CommandString("'SQ' \"DQ\" `BQ`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscDoubleQuotesBtwSingleQuotesUsingStrWithDoubleQuotesInSingleQuotes()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("DQ \"IN\" SQ");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("'DQ \"IN\" SQ'");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscBackQuotesBtwSingleQuotesUsingStrWithBackQuotesInSingleQuotes()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("BQ `IN` SQ");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("'BQ `IN` SQ'");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscSingleQuotesBtwDoubleQuotesUsingStrWithSingleQuotesInDoubleQuotes()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("SQ 'IN' DQ");
		expected.setCharEscaped(0, true, true, true, true, true, true, true, true, true, true);

		cmd = new CommandString("\"SQ 'IN' DQ\"");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToNotEscBackQuotesBtwDoubleQuotesUsingStrWithBackQuotesInDoubleQuotes()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("BQ `IN` DQ");
		expected.setCharEscaped(0, true, true, true, false, false, false, false, true, true, true);

		cmd = new CommandString("\"BQ `IN` DQ\"");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscSingleQuotesBtwBackQuotesUsingStrWithSingleQuotesInBackQuotes()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("`SQ 'IN' BQ`");
		expected.setCharEscaped(0, false, true, true, true, true, true, true, true, true, true, true, false);

		cmd = new CommandString("`SQ 'IN' BQ`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToSetEscDoubleQuotesBtwBackQuotesUsingStrWithDoubleQuotesInBackuotes()
			throws AbstractApplicationException, ShellException {
		expected = new CommandString("`DQ \"IN\" BQ`");
		expected.setCharEscaped(0, false, true, true, true, true, true, true, true, true, true, true, false);

		cmd = new CommandString("`DQ \"IN\" BQ`");
		quoteOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testEvalToThrowsShellExpUsingUnclosedSingleQuote() throws AbstractApplicationException, ShellException {
		cmd = new CommandString("'Unclosed SQ");
		thrown.expect(ShellException.class);
		thrown.expectMessage(NOT_CLOSED_EXP);
		quoteOptr.evaluate(cmd);
	}

	@Test
	public void testEvalToThrowsShellExpUsingUnclosedDoubleQuote() throws AbstractApplicationException, ShellException {
		cmd = new CommandString("\"Unclosed DQ");
		thrown.expect(ShellException.class);
		thrown.expectMessage(NOT_CLOSED_EXP);
		quoteOptr.evaluate(cmd);
	}

	@Test
	public void testEvalToThrowsShellExpUsingUnclosedBackQuote() throws AbstractApplicationException, ShellException {
		cmd = new CommandString("`Unclosed BQ");
		thrown.expect(ShellException.class);
		thrown.expectMessage(NOT_CLOSED_EXP);
		quoteOptr.evaluate(cmd);
	}

	@Test
	public void testEvalToThrowsShellExpUsingNullStr() throws AbstractApplicationException, ShellException {
		thrown.expect(ShellException.class);
		thrown.expectMessage(NULL_PTR_EXP);
		quoteOptr.evaluate(null);
	}
}
