package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

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

public class SeqCommandTest {
	private final static String EMPTY = "";
	private final static String SPACES = "    ";
	
	private final static String ARGS_VAR = "argsArray";
	private final static String CMD = "cmd";
	
	private final static char SEMICOLON = ';';
	
	private static final String EXP_INVALID_SEQ = "shell: Invalid semicolon operator/s";

	private SeqCommand seqCmd;
	private String cmdLine;
	private String[] expectedArgs;

	private Shell shell;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws AbstractApplicationException, ShellException {
		seqCmd = null;
		cmdLine = "";
		expectedArgs = new String[0];
		shell = new ShellStub();
	}

	@Test
	public void testParseEmpty() throws ShellException, AbstractApplicationException {
		cmdLine = EMPTY;
		expectedArgs = new String[] {EMPTY};

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}
	
	@Test
	public void testParseNoSeq() throws ShellException, AbstractApplicationException {
		cmdLine = CMD;
		expectedArgs = new String[] {CMD};

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}
	
	@Test
	public void testParseOneSeq() throws ShellException, AbstractApplicationException {
		cmdLine = CMD + SEMICOLON + CMD;
		expectedArgs = new String[] {CMD, CMD};

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}
	
	@Test
	public void testParseMultipleSeq() throws ShellException, AbstractApplicationException {
		cmdLine = CMD + SEMICOLON + CMD + SEMICOLON + CMD;
		expectedArgs = new String[] {CMD, CMD, CMD};

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}
	
	@Test
	public void testParseSeqAtEnd() throws ShellException, AbstractApplicationException {
		cmdLine = CMD + SEMICOLON + CMD + SEMICOLON;
		expectedArgs = new String[] {CMD, CMD};

		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(seqCmd, ARGS_VAR));
	}
	
	@Test
	public void testInvalidParseSeqAtFront() throws ShellException, AbstractApplicationException {
		cmdLine = SEMICOLON + CMD;
		
		thrown.expect(ShellException.class);
		thrown.expectMessage(EXP_INVALID_SEQ);
		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
	}
	
	@Test
	public void testInvalidParseEmptyCmdBetweenSeq() throws ShellException, AbstractApplicationException {
		cmdLine = CMD + SEMICOLON + SPACES + SEMICOLON + CMD;
		
		thrown.expect(ShellException.class);
		thrown.expectMessage(EXP_INVALID_SEQ);		
		seqCmd = new SeqCommand(shell, cmdLine);
		seqCmd.parse();
	}
}
