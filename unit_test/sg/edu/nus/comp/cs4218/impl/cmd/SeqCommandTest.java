package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.util.reflection.Whitebox;

import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellStub;
import sg.edu.nus.comp.cs4218.impl.cmd.SeqCommand;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class SeqCommandTest {
	private final static String EMPTY = "";
	private final static String SPACES = "    ";

	private final static String ARGS_VAR = "argsArray";
	private final static String CMD = "cmd";

	private final static char SEMICOLON = ';';

	private static final String EXP_INVALID_SEQ = "shell: Invalid semicolon operator/s";

	private SeqCommand seqCmd;
	private CommandString cmdLine;
	private CommandString[] expectedArgs;

	private Shell shell;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws AbstractApplicationException, ShellException {
		seqCmd = null;
		cmdLine = new CommandString();
		shell = new ShellStub();
	}

	@Test
	public void testParseEmpty() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(EMPTY);
		expectedArgs = getExpectedCmdStrArr(EMPTY);

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (CommandString[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}

	@Test
	public void testParseNoSeq() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD);
		expectedArgs = getExpectedCmdStrArr(CMD);

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (CommandString[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}

	@Test
	public void testParseOneSeq() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + SEMICOLON + CMD);
		expectedArgs = getExpectedCmdStrArr(CMD, CMD);

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (CommandString[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}

	@Test
	public void testParseMultipleSeq() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + SEMICOLON + CMD + SEMICOLON + CMD);
		expectedArgs = getExpectedCmdStrArr(CMD, CMD, CMD);

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (CommandString[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}

	@Test
	public void testParseSeqAtEnd() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + SEMICOLON + CMD + SEMICOLON);
		expectedArgs = getExpectedCmdStrArr(CMD, CMD);

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (CommandString[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}

	@Test
	public void testParseSeqWithLeadingAndTrailingSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(SPACES + CMD + SEMICOLON + CMD + SPACES);
		expectedArgs = getExpectedCmdStrArr(CMD, CMD);

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (CommandString[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}

	@Test
	public void testParseSeqEscapedSemiColon() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + SEMICOLON + CMD);
		cmdLine.setCharEscaped(3, true);

		expectedArgs = getExpectedCmdStrArr(CMD + SEMICOLON + CMD);
		expectedArgs[0].setCharEscaped(3, true);

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (CommandString[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}

	@Test
	public void testInvalidParseSeqAtFront() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(SEMICOLON + CMD);

		thrown.expect(ShellException.class);
		thrown.expectMessage(EXP_INVALID_SEQ);
		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
	}

	@Test
	public void testInvalidParseEmptyCmdBetweenSeq() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(CMD + SEMICOLON + SPACES + SEMICOLON + CMD);

		thrown.expect(ShellException.class);
		thrown.expectMessage(EXP_INVALID_SEQ);
		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
	}

	/**
	 * Return the CommandString array by creating new CommandString for each inputed
	 * String. Escape states for the new CommandStrings is by default false.
	 * 
	 * @param strings
	 *            String of the new CommandString to be added to the array.
	 * @return CommandString Array List of CommandString with the inputed Strings.
	 */
	private CommandString[] getExpectedCmdStrArr(String... strings) {
		Vector<CommandString> cmdStrs = new Vector<CommandString>(strings.length);
		for (int i = 0; i < strings.length; i++) {
			cmdStrs.add(new CommandString(strings[i]));
		}
		return cmdStrs.toArray(new CommandString[cmdStrs.size()]);
	}
}
