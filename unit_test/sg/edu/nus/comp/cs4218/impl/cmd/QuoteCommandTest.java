package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class QuoteCommandTest {
	private static final String ASD = "asd";
	private static final String UNCLOSED_QUOTES = "Quote: Unclosed quotes";
	QuoteCommand command;
	String expected, cmdString;
	String[] output;

	@Before
	public void setup() {
		expected = "";
		cmdString = "";
		output = new String[1];
	}

	@Test
	public void testNoQuotes() {
		expected = ASD;
		cmdString = "echo asd";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testUnclosedSQ() {
		expected = UNCLOSED_QUOTES;
		cmdString = "echo 'asd";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testClosedSQ() {
		expected = ASD;
		cmdString = "echo 'asd'";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testMultipleSQ() {
		cmdString = "echo 'asd' 'omg'";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(2, output.length);
		assertEquals(ASD, output[0]);
		assertEquals("omg", output[1]);
	}

	@Test
	public void testUnclosedDQ() {
		expected = UNCLOSED_QUOTES;
		cmdString = "echo \"asd";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testClosedDQ() {
		expected = ASD;
		cmdString = "echo \"asd\"";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testMultipleDQ() {
		cmdString = "echo \"asd\" \"omg\"";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(2, output.length);
		assertEquals(ASD, output[0]);
		assertEquals("omg", output[1]);
	}

	@Test
	public void testMultipleSQDQ() {
		cmdString = "echo \"DQ1\" 'SQ1' 'SQ2' \"DQ2\"";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(4, output.length);
		assertEquals("DQ1", output[0]);
		assertEquals("SQ1", output[1]);
		assertEquals("SQ2", output[2]);
		assertEquals("DQ2", output[3]);
	}

	@Test
	public void testDQWithinSQ() {
		expected = "\"DQ within SQ\"";
		cmdString = "echo '\"DQ within SQ\"'";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals("\"DQ within SQ\"", output[0]);
	}

	@Test
	public void testSQWithinDQ() {
		cmdString = "echo \"'SQ within DQ'\"";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals("'SQ within DQ'", output[0]);
	}

	@Test
	public void testMultipleSQWithUnclosedDQ() {
		expected = UNCLOSED_QUOTES;
		cmdString = "echo 'Invalid' 'Quotes' \"asd";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testMultipleDQWithUnclosedSQ() {
		expected = UNCLOSED_QUOTES;
		cmdString = "echo \"Invalid\" \"Quotes\" 'asd";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testBQWithinSQ() {
		expected = "`abc`";
		cmdString = "echo '`echo abc`'";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testBQWithinDQ() {
		expected = "abc";
		cmdString = "echo \"`echo abc`\"";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testSQWithinText() {
		expected = "abcde";
		cmdString = "echo ab'c'de";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}

	@Test
	public void testDQWithinText() {
		expected = "abcde";
		cmdString = "echo ab\"c\"de";
		try {
			output = command.evaluate(cmdString);
		} catch (Exception e) {
			output[0] = e.getMessage();
		}
		assertEquals(1, output.length);
		assertEquals(expected, output[0]);
	}
}
