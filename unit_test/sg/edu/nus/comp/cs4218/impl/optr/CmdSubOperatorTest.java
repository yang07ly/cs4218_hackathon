package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.*;

import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.LsException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellStub;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;
import sg.edu.nus.comp.cs4218.impl.optr.CmdSubOperator;

public class CmdSubOperatorTest {
	private static final String CMD_STR = "cmd";
	private static final String CMDSUB_STR = "`" + CMD_STR + "`";
	private static final String NOT_CLOSED_EXP = "shell: Back Quotes not closed";
	private static final String NULL_EXP = "shell: Null Pointer Exception";

	private ShellStub spyShell;
	private CmdSubOperator cmdSubOptr;
	private CommandString cmd, expected;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		spyShell = Mockito.spy(new ShellStub());
		cmdSubOptr = new CmdSubOperator(spyShell);
		cmd = new CommandString();
		expected = new CommandString();
	}

	@Test
	public void testNoCmdSub() throws AbstractApplicationException, ShellException {
		expected = new CommandString(CMD_STR);
		cmd = new CommandString(CMD_STR);
		cmdSubOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testCmdSub() throws AbstractApplicationException, ShellException {
		expected = new CommandString(ShellStub.SHELL_RESULT);
		cmd = new CommandString(CMDSUB_STR);
		cmdSubOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testCmdSubEmpty() throws AbstractApplicationException, ShellException {
		expected = new CommandString();
		cmd = new CommandString("``");
		cmdSubOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testCmdSubWithinChars() throws AbstractApplicationException, ShellException {
		expected = new CommandString("F" + ShellStub.SHELL_RESULT + "E");

		cmd = new CommandString("F" + CMDSUB_STR + "E");
		cmd.setCharEscaped(2, true, true, true);

		cmdSubOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testMutipleCmdSub() throws AbstractApplicationException, ShellException {
		expected = new CommandString(ShellStub.SHELL_RESULT + ShellStub.SHELL_RESULT);

		cmd = new CommandString(CMDSUB_STR + CMDSUB_STR);
		cmd.setCharEscaped(1, true, true, true);
		cmd.setCharEscaped(6, true, true, true);

		cmdSubOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testCmdSubEscaped() throws AbstractApplicationException, ShellException {
		expected = new CommandString(CMDSUB_STR);
		expected.setCharEscapedRange(0, CMDSUB_STR.length(), true);

		cmd = new CommandString(CMDSUB_STR);
		cmd.setCharEscapedRange(0, CMDSUB_STR.length(), true);

		cmdSubOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testCmdSubAppReturnMultipleLines() throws AbstractApplicationException, ShellException {
		expected = new CommandString("Rline1. Rline2.");

		Shell mockShell = Mockito.mock(Shell.class);
		Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				OutputStream output = (OutputStream) invocation.getArguments()[1];
				output.write(("Rline1." + OSUtil.NEWLINE + "Rline2.").getBytes());
				return null;
			}
		}).when(mockShell).parseAndEvaluate(Mockito.anyString(), Mockito.any(OutputStream.class));

		cmd = new CommandString(CMDSUB_STR);
		cmd.setCharEscaped(1, true, true, true);

		cmdSubOptr.evaluate(cmd);
		assertEquals(expected, cmd);
	}

	@Test
	public void testInvalidCmdSubNull() throws AbstractApplicationException, ShellException {
		thrown.expect(ShellException.class);
		thrown.expectMessage(NULL_EXP);
		cmdSubOptr.evaluate(null);
	}

	@Test
	public void testInvalidCmdSubQuoteNotClosed() throws AbstractApplicationException, ShellException {
		cmd = new CommandString("`" + CMD_STR);
		cmd.setCharEscaped(0, false, true, true, true);
		thrown.expect(ShellException.class);
		thrown.expectMessage(NOT_CLOSED_EXP);
		cmdSubOptr.evaluate(cmd);
	}

	@Test
	public void testInvalidCmdSubCmd() throws AbstractApplicationException, ShellException {
		Shell mockShell = Mockito.mock(Shell.class);
		Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
		Mockito.doThrow(ShellException.class).when(mockShell).parseAndEvaluate(Mockito.anyString(),
				Mockito.any(OutputStream.class));

		thrown.expect(ShellException.class);
		cmd = new CommandString(CMDSUB_STR);
		cmdSubOptr.evaluate(cmd);
	}

	@Test
	public void testInvalidCmdSubApp() throws AbstractApplicationException, ShellException {
		Shell mockShell = Mockito.mock(Shell.class);
		Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
		Mockito.doThrow(LsException.class).when(mockShell).parseAndEvaluate(Mockito.anyString(),
				Mockito.any(OutputStream.class));

		thrown.expect(LsException.class);
		cmd = new CommandString(CMDSUB_STR);
		cmdSubOptr.evaluate(cmd);
	}
}
