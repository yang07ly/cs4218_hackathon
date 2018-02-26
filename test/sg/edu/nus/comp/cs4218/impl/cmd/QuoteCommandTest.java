package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public class QuoteCommandTest {
	QuoteCommand command;
	String output, expected, cmdString;
	InputStream inputStream;
	OutputStream outputStream;

	@Before
	public void setup() {
		output = "";
		expected = "";
		cmdString = "";
		inputStream = System.in;
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void testNoQuotes() {
		expected = "asd";
		cmdString = "echo asd";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testUnclosedSQ() {
		expected = "Quote: Unclosed quotes";
		cmdString = "echo 'asd";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testClosedSQ() {
		expected = "asd";
		cmdString = "echo 'asd'";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testMultipleSQ() {
		expected = "asd omg";
		cmdString = "echo 'asd' 'omg'";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testUnclosedDQ() {
		expected = "Quote: Unclosed quotes";
		cmdString = "echo \"asd";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testClosedDQ() {
		expected = "asd";
		cmdString = "echo \"asd\"";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testMultipleDQ() {
		expected = "asd omg";
		cmdString = "echo \"asd\" \"omg\"";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testMultipleSQDQ() {
		expected = "DQ1 SQ1 SQ2 DQ2";
		cmdString = "echo \"DQ1\" 'SQ1' 'SQ2' \"DQ2\"";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testDQWithinSQ() {
		expected = "\"DQ within SQ\"";
		cmdString = "echo '\"DQ within SQ\"'";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testSQWithinDQ() {
		expected = "'SQ within DQ'";
		cmdString = "echo \"'SQ within DQ'\"";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testMultipleSQWithUnclosedDQ() {
		expected = "Quote: Unclosed quotes";
		cmdString = "echo 'Invalid' 'Quotes' \"asd";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testMultipleDQWithUnclosedSQ() {
		expected = "Quote: Unclosed quotes";
		cmdString = "echo \"Invalid\" \"Quotes\" 'asd";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testBQWithinSQ() {
		expected = "`echo abc`";
		cmdString = "echo '`echo abc`'";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testBQWithinDQ() {
		expected = "abc";
		cmdString = "echo \"`echo abc`\"";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testSQWithinText() {
		expected = "abcde";
		cmdString = "echo ab'c'de";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}

	@Test
	public void testDQWithinText() {
		expected = "abcde";
		cmdString = "echo ab\"c\"de";
		command = new QuoteCommand(cmdString);
		try {
			output = command.evaluate(inputStream, outputStream);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(output, output);
	}
}
