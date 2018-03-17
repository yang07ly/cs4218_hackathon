package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellStub;
import sg.edu.nus.comp.cs4218.impl.optr.IoRedirOperator;

public class PipeCommandTest {
	private PipeCommand pipeCom;
	private String[] input, output, expected;
	private String currentDir, cmdLine;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "ioRedir_test_system";
		input = output = expected = new String[0];
		cmdLine = "";
		currentDir = Environment.currentDirectory + File.separator;
	}

	@Test
	public void testNoPipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc";
		pipeCom = new PipeCommand(new ShellStub(), cmdLine);
		expected = new String[] { "echo abc" };

		pipeCom.parse();

		assertArrayEquals(expected, pipeCom.argsArray);
	}

	@Test
	public void testOnePipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc | cat";
		pipeCom = new PipeCommand(new ShellStub(), cmdLine);
		expected = new String[] { "echo abc ", " cat" };

		pipeCom.parse();

		assertArrayEquals(expected, pipeCom.argsArray);
	}

	@Test
	public void testMultiplePipe() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc | cat | cat";
		pipeCom = new PipeCommand(new ShellStub(), cmdLine);
		expected = new String[] { "echo abc ", " cat ", " cat" };

		pipeCom.parse();

		assertArrayEquals(expected, pipeCom.argsArray);
	}

	@Test
	public void testPipeAtFront() throws ShellException, AbstractApplicationException {
		cmdLine = "| echo abc";
		pipeCom = new PipeCommand(new ShellStub(), cmdLine);
		expected = new String[] { "echo abc ", " cat ", " cat" };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		pipeCom.parse();
	}

	@Test
	public void testPipeAtBack() throws ShellException, AbstractApplicationException {
		cmdLine = "echo |";
		pipeCom = new PipeCommand(new ShellStub(), cmdLine);
		expected = new String[] { "echo abc ", " cat ", " cat" };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		pipeCom.parse();
	}

	@Test
	public void testPipeWithinText() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc|cat";
		pipeCom = new PipeCommand(new ShellStub(), cmdLine);
		expected = new String[] { "echo abc", "cat"};

		pipeCom.parse();
		assertArrayEquals(expected, pipeCom.argsArray);
	}

	@Test
	public void testPipeWithinQuotes() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc \"|cat\"";
		pipeCom = new PipeCommand(new ShellStub(), cmdLine);
		expected = new String[] { "echo abc \"", "cat\""};

		pipeCom.parse();
		assertArrayEquals(expected, pipeCom.argsArray);
	}
	
}