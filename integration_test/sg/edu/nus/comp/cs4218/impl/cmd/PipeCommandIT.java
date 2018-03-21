package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.cmd.PipeCommand;
import sg.edu.nus.comp.cs4218.impl.cmd.SeqCommand;

public class PipeCommandIT {
	private PipeCommand pipeCom;
	private String cmdLine, expected;
	private ByteArrayOutputStream output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		expected = cmdLine = "";
		output = new ByteArrayOutputStream();
	}

	@Test
	public void testEmptyCmd() throws ShellException, AbstractApplicationException {
		cmdLine = "";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);
		expected = "shell: : Invalid app";

		thrown.expect(ShellException.class);
		thrown.expectMessage(expected);
		
		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
	}

	@Test
	public void testNoPipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);
		expected = "abc";

		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOnePipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc | cat";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);
		expected = "abc";

		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testMultiplePipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc | cat | cat";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);
		expected = "abc";

		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testPipeAtFront() throws ShellException, AbstractApplicationException {
		cmdLine = "| echo abc";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
	}

	@Test
	public void testPipeAtBack() throws ShellException, AbstractApplicationException {
		cmdLine = "echo |";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
	}

	@Test
	public void testPipeWithinText() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc|cat";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);
		expected = "abc";

		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
	}

	@Test
	public void testPipeWithinDoubleQuotes() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc \"|cat\"";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);
		expected = "abc |cat";

		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testPipeWithinSingleQuotes() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc '|cat'";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);
		expected = "abc |cat";

		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testPipeWithinBackQuotes() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc `|cat`";
		pipeCom = new PipeCommand(new ShellImpl(), cmdLine);
		expected = "abc |cat";

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		pipeCom.parse();
		pipeCom.evaluate(System.in, output);
	}
}
