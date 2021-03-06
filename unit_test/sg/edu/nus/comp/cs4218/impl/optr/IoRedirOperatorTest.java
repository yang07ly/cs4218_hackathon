package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class IoRedirOperatorTest {

	private static final String OUTPUT_TXT = "output.txt";
	private static final String ASD = "asd";
	private IoRedirOperator ioRedirOp;
	private CommandString input, expected;
	private InputStream inputStream;
	private OutputStream outputStream;
	private String currentDir;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + OSUtil.SEP + "test_system" + OSUtil.SEP
				+ "ioRedir_test_system";
		ioRedirOp = new IoRedirOperator(new ShellStub());
		input = new CommandString();
		expected = new CommandString();
		inputStream = null;
		outputStream = null;
		currentDir = Environment.currentDirectory + OSUtil.SEP;
	}

	@After
	public void tearDown() throws IOException {
		if (outputStream != null) {
			outputStream.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
	}

	@Test
	public void testInputToRunCorrectlyUsingNoInputStream() throws ShellException, AbstractApplicationException {
		expected = new CommandString(ASD);
		input = new CommandString(ASD);

		inputStream = ioRedirOp.getInputStream(input);

		assertTrue(outputStream == null);
	}

	@Test
	public void testOutputToRunCorrectlyUsingNoOutputStream() throws ShellException, AbstractApplicationException {
		expected = new CommandString(ASD);
		input = new CommandString(ASD);
		outputStream = ioRedirOp.getOutputStream(input);
		assertTrue(outputStream == null);
	}

	@Test
	public void testInputToThrowExpUsingUsingInvalidFile() throws ShellException, AbstractApplicationException {
		input = new CommandString("< asd");
		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: asd: No such file or directory");
		inputStream = ioRedirOp.getInputStream(input);
	}

	@Test
	public void testInputToRunCorrectlyUsingUsingValidFile()
			throws ShellException, AbstractApplicationException, IOException {
		input = new CommandString("< file.txt");
		inputStream = ioRedirOp.getInputStream(input);

		assertTrue(inputStream != null);
		inputStream.close();
	}

	@Test
	public void testInputToThrowExpUsingUsingMultiInputStreams() throws ShellException, AbstractApplicationException {
		input = new CommandString("< file.txt < file.txt");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 inputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testInputToThrowExpUsingWhenNoFile() throws ShellException, AbstractApplicationException {
		input = new CommandString("<");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: no input file specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputToThrowExpUsingUsingInvalidFile()
			throws ShellException, AbstractApplicationException, IOException {
		expected = new CommandString("");
		input = new CommandString("> asd");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + ASD)));

		outputStream.close();
		Files.delete(Paths.get(currentDir + ASD));
	}

	@Test
	public void testOutputToRunCorrectlyUsingUsingExistingFile()
			throws ShellException, AbstractApplicationException, IOException {
		expected = new CommandString("");
		input = new CommandString("> output.txt");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);

		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + OUTPUT_TXT)));

		outputStream.close();
	}

	@Test
	public void testOutputToThrowExpUsingUsingMultiOutputStreams() throws ShellException, AbstractApplicationException {
		input = new CommandString("> file.txt > file.txt");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 outputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputToThrowExpUsingWhenNoFile() throws ShellException, AbstractApplicationException {
		input = new CommandString(">");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: no output file specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputRunCorrectlyExpUsingUsingInputAndOutput()
			throws ShellException, AbstractApplicationException, IOException {
		expected = new CommandString("hi  > asd");
		input = new CommandString("hi < file.txt > asd");
		inputStream = ioRedirOp.getInputStream(input);

		assertEquals(expected, input);
		assertTrue(inputStream != null);

		inputStream.close();
	}

	@Test
	public void testOutputToRunCorrectlyUsingUsingInputAndNonExistingOutputFile()
			throws ShellException, AbstractApplicationException, IOException {
		expected = new CommandString("hi < file.txt ");
		input = new CommandString("hi < file.txt > asd");

		outputStream = ioRedirOp.getOutputStream(input);

		assertEquals(expected, input);
		assertTrue(outputStream != null);
		assertTrue(Files.exists(Paths.get(currentDir + ASD)));

		outputStream.close();
		Files.delete(Paths.get(currentDir + ASD));
	}

	@Test
	public void testOutputToThrowExpUsingUsingMultiInputsAndOneOutput()
			throws ShellException, AbstractApplicationException, IOException {
		input = new CommandString("hi < file.txt > asd < output.txt");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 inputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputToThrowExpUsingUsingMultiOutputsAndOneInput()
			throws ShellException, AbstractApplicationException, IOException {
		input = new CommandString("hi < file.txt > asd > output.txt");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 outputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}

	@Test
	public void testOutputToThrowExpUsingUsingMultiOutputsAndMultiInputs()
			throws ShellException, AbstractApplicationException, IOException {
		input = new CommandString("hi < file.txt > asd < output.txt > afe");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: only 1 inputstream can be specified");

		inputStream = ioRedirOp.getInputStream(input);
		outputStream = ioRedirOp.getOutputStream(input);
	}
}