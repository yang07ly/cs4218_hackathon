package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertArrayEquals;

import java.util.Vector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.util.reflection.Whitebox;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellStub;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class PipeCommandTest {
	private final static String EMPTY = "";
	private final static String SPACES = "    ";
	
	private final static String ARGS_VAR = "argsArray";
	private final static String CMD = "cmd";
	
	private final static char PIPE = '|';
	
	private static final String EXP_INVALID_PIPE = "shell: Invalid pipe operator/s";
	
	private PipeCommand pipeCmd;
	private CommandString[] expected;
	private CommandString cmdLine;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		expected = new CommandString[0];
		cmdLine = new CommandString();
	}
	
	@Test
	public void testParseEmpty() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(EMPTY);
		expected = getExpectedCmdStrArr(EMPTY);
		
		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCmd, ARGS_VAR));
	}

	@Test
	public void testParseNoPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD);
		expected = getExpectedCmdStrArr(CMD);

		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCmd, ARGS_VAR));
	}

	@Test
	public void testParseOnePipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + PIPE + CMD);
		expected = getExpectedCmdStrArr(CMD, CMD);
		
		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCmd, ARGS_VAR));
	}

	@Test
	public void testParseMultiplePipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + PIPE + CMD + PIPE + CMD);
		expected = getExpectedCmdStrArr(CMD, CMD, CMD);

		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCmd, ARGS_VAR));
	}

	@Test
	public void testParsePipeWithLeadingAndTrailingSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(SPACES + CMD + PIPE + CMD + SPACES);
		expected = getExpectedCmdStrArr(CMD, CMD);
		
		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCmd, ARGS_VAR));
	}

	@Test
	public void testParseEscapedPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + PIPE + CMD);
		cmdLine.setCharEscaped(3, true);
		
		expected = getExpectedCmdStrArr(CMD + PIPE + CMD);
		expected[0].setCharEscaped(3, true);
		
		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCmd, ARGS_VAR));
	}
	
	@Test
	public void testParseSeqEscapedWithValidPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + PIPE + CMD + PIPE + CMD);
		cmdLine.setCharEscaped(3, true);
		
		expected = getExpectedCmdStrArr(CMD + PIPE + CMD, CMD);
		expected[0].setCharEscaped(3, true);
		
		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCmd, ARGS_VAR));
	}
	
	@Test
	public void testInvalidParsePipeAtFront() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(PIPE + CMD);

		thrown.expect(ShellException.class);
		thrown.expectMessage(EXP_INVALID_PIPE);
		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
	}

	@Test
	public void testInvalidParsePipeAtBack() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + PIPE);

		thrown.expect(ShellException.class);
		thrown.expectMessage(EXP_INVALID_PIPE);
		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
	}
	
	@Test
	public void testInvalidParsePipeEmptyCmdBetweenPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + PIPE + PIPE + CMD);

		thrown.expect(ShellException.class);
		thrown.expectMessage(EXP_INVALID_PIPE);
		pipeCmd = new PipeCommand(new ShellStub(), cmdLine);
		pipeCmd.parse();
	}

	/**
	 * Return the CommandString array by creating new CommandString for each
	 * inputed String. Escape states for the new CommandStrings is by default false.
	 * 
	 * @param strings
	 * 				String of the new CommandString to be added to the array.
	 * @return CommandString Array
	 * 				List of CommandString with the inputed Strings.
	 */
	private CommandString[] getExpectedCmdStrArr(String... strings) {
		Vector<CommandString> cmdStrs = new Vector<CommandString>(strings.length);
		for (int i = 0; i < strings.length; i++) {
			cmdStrs.add(new CommandString(strings[i]));
		}
		return cmdStrs.toArray(new CommandString[cmdStrs.size()]);
	}
}