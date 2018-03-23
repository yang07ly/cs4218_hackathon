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
	public void testInvalidPipeEmpty() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: : Invalid app");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}

	@Test
	public void testNoPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo no pipe");
		expected = "no pipe";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOnePipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo one pipe | cat");
		expected = "one pipe";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testMultiplePipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo mutiple pipes | cat | sed s/pipes/Pipes/");
		expected = "mutiple Pipes";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testInvalidPipeAtFront() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("| echo pipe front");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}

	@Test
	public void testInvalidPipeAtBack() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo pipe back |");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}

	@Test
	public void testPipeWithinText() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo pipeWithinText|cat");
		expected = "pipeWithinText";
		
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
		cmdLine = new CommandString("echo escaped |pipe");
		cmdLine.setCharEscaped(13, true);
		expected = "escaped |pipe";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testPipeEscapedWithNotEscaped() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo escaped |pipe | sed s/pipe/pipe_with_valid_pipe/");
		cmdLine.setCharEscaped(13, true);
		expected = "escaped |pipe_with_valid_pipe";
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testInvalidPipeAppException() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("cat nonExistentFile");

		thrown.expect(AbstractApplicationException.class);
		thrown.expectMessage("cat: nonExistentFile: No such file or directory");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}
	
	@Test
	public void testInvalidPipeShellException() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("invalidApp");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: invalidApp: Invalid app");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}
	
	@Test
	public void testInvalidMultiPipeWithExceptionAtStart() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("ec pipe1 | cat | sed s/pipe1/sed-replacement/");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: ec: Invalid app");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}
	
	@Test
	public void testInvalidMultiPipeWithExceptionAtMiddle() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo pipe1 | ct | sed s/pipe1/sed-replacement/");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: ct: Invalid app");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}
	
	@Test
	public void testInvalidMultiSeqWithExceptionAtEnd() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo pipe1 | cat | sd s/pipe1/sed-replacement/");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: sd: Invalid app");
		
		pipeCmd = new PipeCommand(new ShellImpl(), cmdLine);
		pipeCmd.parse();
		pipeCmd.evaluate(System.in, output);
	}
}
