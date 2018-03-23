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
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class PipeCommandIT {
	private PipeCommand pipeCmd;
	private CommandString cmdLine;
	private String expected;
	private ByteArrayOutputStream output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		cmdLine = new CommandString();
		expected = "";
		output = new ByteArrayOutputStream();
	}

	@Test
	public void testInvalidEmptyCmd() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: : Invalid app");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}

	@Test
	public void testNoPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc");
		expected = "abc";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOnePipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo cat | cat");
		expected = "cat";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testMultiplePipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc | cat | sed s/bc/cd/");
		expected = "acd";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testInvalidPipeAtFront() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("| echo abc");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}

	@Test
	public void testInvalidPipeAtBack() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo |");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}

	@Test
	public void testPipeWithinText() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc|cat");
		expected = "abc";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}
	
	@Test
	public void testPipeWithleadingAndTrailingSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("     echo leading and trailing spaces |cat      ");
		expected = "leading and trailing spaces";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}

	@Test
	public void testPipeEscaped() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc |cat");
		cmdLine.setCharEscapedRange(9, 13, true);
		expected = "abc |cat";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
}
