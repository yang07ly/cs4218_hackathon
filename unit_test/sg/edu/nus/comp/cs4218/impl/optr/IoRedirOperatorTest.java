package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellStub;

public class IoRedirOperatorTest {

	private static final String OUTPUT_TXT = "output.txt";
	private static final String FILE_TXT = "file.txt";
	private static final String ASD = "asd";
	private IoRedirOperator ioRedirOp;
	private String[] input, output, expected;
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
		input = output = expected = new String[0];
		inputStream = null;
		outputStream = null;
		currentDir = Environment.currentDirectory + File.separator;
	}

	@Test
	public void testNoIO() throws ShellException, AbstractApplicationException {
		expected = new String[] { ASD };
		input = new String[] { ASD };

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertArrayEquals(expected, output);
		assertTrue(inputStream == null);
		assertTrue(outputStream == null);
	}

	@Test
	public void testInputInvalidFile() throws ShellException, AbstractApplicationException {
		expected = new String[] {};
		input = new String[] { "<", ASD };
		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: asd: No such file or directory");

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOneInput() throws ShellException, AbstractApplicationException, IOException {
		expected = new String[] {};
		input = new String[] { "<", FILE_TXT };

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertArrayEquals(expected, output);
		assertTrue(inputStream != null);
		assertTrue(outputStream == null);
		inputStream.close();
	}

	@Test
	public void testMultipleInputs() throws ShellException, AbstractApplicationException {
		expected = new String[] {};
		input = new String[] { "<", FILE_TXT, "<", FILE_TXT };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 input can be specified");

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testInputWithoutArg() throws ShellException, AbstractApplicationException {
		expected = new String[] {};
		input = new String[] { "<" };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: no input file specified");

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputFileNoExists() throws ShellException, AbstractApplicationException, IOException {
		expected = new String[] {};
		input = new String[] { ">", ASD };

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertArrayEquals(expected, output);
		assertTrue(inputStream == null);
		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + ASD)));

		outputStream.close();
		Files.delete(Paths.get(currentDir + ASD));
	}

	@Test
	public void testOutputFileExists() throws ShellException, AbstractApplicationException, IOException {
		expected = new String[] {};
		input = new String[] { ">", OUTPUT_TXT };

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertArrayEquals(expected, output);
		assertTrue(inputStream == null);
		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + OUTPUT_TXT)));

		outputStream.close();
	}

	@Test
	public void testMultipleOutputs() throws ShellException, AbstractApplicationException {
		expected = new String[] {};
		input = new String[] { ">", FILE_TXT, ">", FILE_TXT };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 output can be specified");

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputWithoutArg() throws ShellException, AbstractApplicationException {
		expected = new String[] {};
		input = new String[] { ">" };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: no output file specified");

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOneInputAndOneOutput() throws ShellException, AbstractApplicationException, IOException {
		expected = new String[] { "hi" };
		input = new String[] { "hi", "<", FILE_TXT, ">", ASD };

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertArrayEquals(expected, output);
		assertTrue(inputStream != null);
		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + ASD)));

		outputStream.close();
		inputStream.close();
		Files.delete(Paths.get(currentDir + ASD));
	}

	@Test
	public void testMultipleInputAndOneOutput() throws ShellException, AbstractApplicationException, IOException {
		expected = new String[] { "hi" };
		input = new String[] { "hi", "<", FILE_TXT, ">", ASD, "<", OUTPUT_TXT };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 input can be specified");

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOneInputAndMultipleOutput() throws ShellException, AbstractApplicationException, IOException {
		expected = new String[] { "hi" };
		input = new String[] { "hi", "<", FILE_TXT, ">", ASD, ">", OUTPUT_TXT };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 output can be specified");

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testMultipleInputAndMultipleOutput() throws ShellException, AbstractApplicationException, IOException {
		expected = new String[] { "hi" };
		input = new String[] { "hi", "<", FILE_TXT, ">", ASD, "<", OUTPUT_TXT, ">", "def" };

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 input can be specified");

		output = ioRedirOp.evaluate(input);
		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}
}