package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class CallCommandTest {

	private static final String ARGS_ARRAY = "argsArray";
	private static final String APP = "app";
	private static final String ECHO = "echo";
	private static final String DEF = "def";
	private static final String ABC = "abc";
	private String expected, cmdLine;

	@Before
	public void setUp() throws Exception {
		cmdLine = expected = "";
	}

	@Test
	public void testParseToRunCorrectlyWhenParsingApp() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), new CommandString(cmdLine));
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseToRunCorrectlyUsingSpaceBeforeAppName() throws ShellException, AbstractApplicationException {
		cmdLine = " echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), new CommandString(cmdLine));
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseToRunCorrectlyUsingMultiSpacesBeforeAppName()
			throws ShellException, AbstractApplicationException {
		cmdLine = "    echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), new CommandString(cmdLine));
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseToRunCorrectlyWhenParsingArgs() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc def";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), new CommandString(cmdLine));
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, ARGS_ARRAY));
	}

	@Test
	public void testParseToRunCorrectlyWhenMultiSpaceBtwArgs() throws ShellException, AbstractApplicationException {
		cmdLine = "echo  abc     def";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), new CommandString(cmdLine));
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, ARGS_ARRAY));
	}

}