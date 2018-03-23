package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellStub;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class IoRedirOperatorTest {

	private static final String OUTPUT_TXT = "output.txt";
	private static final String ASD = "asd";
	private IoRedirOperator ioRedirOp;
	private CommandString input, output, expected;
	private InputStream inputStream;
	private OutputStream outputStream;
	private String currentDir;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system" + File.separator
				+ "ioRedir_test_system";
		ioRedirOp = new IoRedirOperator(new ShellStub());
		input = new CommandString();
		output = new CommandString();
		expected = new CommandString();
		inputStream = null;
		outputStream = null;
		currentDir = Environment.currentDirectory + File.separator;
	}
	
	@After
	public void tearDown() throws IOException {
		if(outputStream != null) {
			outputStream.close();
		}
		if(inputStream != null) {
			inputStream.close();
		}
	}

	@Test
	public void testNoIO() throws ShellException, AbstractApplicationException {
		expected = new CommandString(ASD);
		input = new CommandString(ASD);

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertTrue(inputStream == null);
		assertTrue(outputStream == null);
	}

	@Test
	public void testInputInvalidFile() throws ShellException, AbstractApplicationException {
		input = new CommandString("< asd");
		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: asd: No such file or directory");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOneInput() throws ShellException, AbstractApplicationException, IOException {
		input = new CommandString("< file.txt");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertTrue(inputStream != null);
		assertTrue(outputStream == null);
		inputStream.close();
	}

	@Test
	public void testMultipleInputs() throws ShellException, AbstractApplicationException {
		input = new CommandString("< file.txt < file.txt");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 inputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testInputWithoutArg() throws ShellException, AbstractApplicationException {
		input = new CommandString("<");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: no input file specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputFileNoExists() throws ShellException, AbstractApplicationException, IOException {
		expected = new CommandString("");
		input = new CommandString("> asd");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertEquals(input, output);
		assertTrue(inputStream == null);
		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + ASD)));

		outputStream.close();
		Files.delete(Paths.get(currentDir + ASD));
	}

	@Test
	public void testOutputFileExists() throws ShellException, AbstractApplicationException, IOException {
		expected = new CommandString("");
		input = new CommandString("> output.txt");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertEquals(input, output);
		assertTrue(inputStream == null);
		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + OUTPUT_TXT)));

		outputStream.close();
	}

	@Test
	public void testMultipleOutputs() throws ShellException, AbstractApplicationException {
		input = new CommandString("> file.txt > file.txt");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 outputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputWithoutArg() throws ShellException, AbstractApplicationException {
		input = new CommandString(">");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: no output file specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOneInputAndOneOutput() throws ShellException, AbstractApplicationException, IOException {
		expected = new CommandString("hi  ");
		input = new CommandString("hi < file.txt > asd");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertEquals(expected, input);
		assertTrue(inputStream != null);
		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + ASD)));

		outputStream.close();
		inputStream.close();
		Files.delete(Paths.get(currentDir + ASD));
	}

	@Test
	public void testMultipleInputAndOneOutput() throws ShellException, AbstractApplicationException, IOException {
		input = new CommandString("hi < file.txt > asd < output.txt");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 inputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOneInputAndMultipleOutput() throws ShellException, AbstractApplicationException, IOException {
		input = new CommandString("hi < file.txt > asd > output.txt");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 outputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testMultipleInputAndMultipleOutput() throws ShellException, AbstractApplicationException, IOException {
		input = new CommandString("hi < file.txt > asd < output.txt > afe");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 inputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}
}