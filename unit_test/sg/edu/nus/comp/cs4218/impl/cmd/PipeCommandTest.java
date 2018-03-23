package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertArrayEquals;

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
	private static final String ARGS_ARRAY = "argsArray";
	private static final String CAT = " cat";
	private static final String ECHO_ABC = "echo abc ";
	private PipeCommand pipeCom;
	private CommandString[] expected;
	private String cmdLine;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		expected = new CommandString[0];
		cmdLine = "";
	}

	@Test
	public void testNoPipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc";
		pipeCom = new PipeCommand(new ShellStub(), new CommandString(cmdLine));
		expected = new CommandString[] { new CommandString("echo abc") };

		pipeCom.parse();

		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCom, ARGS_ARRAY));
	}

	@Test
	public void testOnePipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc | cat";
		pipeCom = new PipeCommand(new ShellStub(), new CommandString(cmdLine));
		expected = new CommandString[] { new CommandString(ECHO_ABC), new CommandString(CAT) };

		pipeCom.parse();

		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCom, ARGS_ARRAY));
	}

	@Test
	public void testMultiplePipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc | cat | cat";
		pipeCom = new PipeCommand(new ShellStub(), new CommandString(cmdLine));
		expected = new CommandString[] { new CommandString(ECHO_ABC), new CommandString(" cat "), new CommandString(CAT) };

		pipeCom.parse();

		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCom, ARGS_ARRAY));
	}

	@Test
	public void testPipeAtFront() throws ShellException, AbstractApplicationException {
		cmdLine = "| echo abc";
		pipeCom = new PipeCommand(new ShellStub(), new CommandString(cmdLine));
		expected = new CommandString[] { new CommandString(ECHO_ABC), new CommandString(" cat "), new CommandString(CAT) };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		pipeCom.parse();
	}

	@Test
	public void testPipeAtBack() throws ShellException, AbstractApplicationException {
		cmdLine = "echo |";
		pipeCom = new PipeCommand(new ShellStub(), new CommandString(cmdLine));
		expected = new CommandString[] { new CommandString(ECHO_ABC), new CommandString(" cat "), new CommandString(CAT) };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		pipeCom.parse();
	}

	@Test
	public void testPipeWithinText() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc|cat";
		pipeCom = new PipeCommand(new ShellStub(), new CommandString(cmdLine));
		expected = new CommandString[] { new CommandString("echo abc"), new CommandString("cat") };

		pipeCom.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCom, ARGS_ARRAY));
	}

	@Test
	public void testPipeWithinQuotes() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc \"|cat\"";
		pipeCom = new PipeCommand(new ShellStub(), new CommandString(cmdLine));
		expected = new CommandString[] { new CommandString("echo abc \""), new CommandString("cat\"") };

		pipeCom.parse();
		assertArrayEquals(expected, (CommandString[]) Whitebox.getInternalState(pipeCom, ARGS_ARRAY));
	}

}