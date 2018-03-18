package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class CallCommandTest {

	private static final String ECHO = "echo";
	private static final String DEF = "def";
	private static final String ABC = "abc";
	private String expected, cmdLine;

	@Before
	public void setUp() throws Exception {
		cmdLine = expected = "";
	}

	@Test
	public void testParseAppName() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, "app"));
	}

	@Test
	public void testParseAppNameWithOneSpace() throws ShellException, AbstractApplicationException {
		cmdLine = " echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, "app"));
	}

	@Test
	public void testParseAppNameWithMultipleSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = "    echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, "app"));
	}

	@Test
	public void testParseAppNameWithOneTab() throws ShellException, AbstractApplicationException {
		cmdLine = "\techo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, "app"));
	}

	@Test
	public void testParseAppNameWithMultipleTabs() throws ShellException, AbstractApplicationException {
		cmdLine = "\t\t\techo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, "app"));
	}

	@Test
	public void testParseAppNameWithMultipleTabsSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "  \t\t   \t  echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, "app"));
	}

	@Test
	public void testParseArgs() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc def";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, "argsArray"));
	}

	@Test
	public void testParseArgsWithMultipleSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "echo  abc     def";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, "argsArray"));
	}

	@Test
	public void testParseArgsWithOneTab() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\tabc\tdef";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, "argsArray"));
	}

	@Test
	public void testParseArgsWithMultipleTabs() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\t\tabc\t\t\tdef";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, "argsArray"));
	}

	@Test
	public void testParseArgsWithMultipleTabsSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\t    \tabc   \t\t\t     def";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, "argsArray"));
	}

}