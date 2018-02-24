package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.cmd.PipeCommand;

public class PipeCommandTest {

	PipeCommand pipeCommand;
	
	@Before
	public void setup() {
		pipeCommand = new PipeCommand();
	}
	
	@Test
	public void testPipeTwoCommandsNoArgs() throws ShellException, AbstractApplicationException {
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		pipeCommand = new PipeCommand("");
		pipeCommand.parse();
		pipeCommand.evaluate(System.in, stdout);
		assertEquals("", new String(stdout.toByteArray()));
	}
	
	@Test
	public void testPipeTwoCommandsWithValidArgs() throws ShellException, AbstractApplicationException {
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		pipeCommand = new PipeCommand("cat text.txt | sed s/pains/paining/");
		pipeCommand.parse();
		pipeCommand.evaluate(System.in, stdout);
		String expectedResult = "On the other hand,"
				+ " we denounce with righteous indignation and "
				+ "dislike men who are so beguiled and demoralized"
				+ " by the charms of pleasure of the moment, so blinded"
				+ " by desire, that they cannot foresee the pain and trouble "
				+ "that are bound to ensue; and equal blame belongs to those "
				+ "who fail in their duty through weakness of will, which is the "
				+ "same as saying through shrinking from toil and pain. "
				+ "These cases are perfectly simple and easy to distinguish. "
				+ "In a free hour, when our power of choice is untrammelled and when "
				+ "nothing prevents our being able to do what we like best, every"
				+ " pleasure is to be welcomed and every pain avoided. But in certain "
				+ "circumstances and owing to the claims of duty or the obligations of"
				+ " business it will frequently occur that pleasures have to be repudiated "
				+ "and annoyances accepted. The wise man therefore always holds in these "
				+ "matters to this principle of selection: he rejects pleasures to secure other"
				+ " greater pleasures, or else he endures paining to avoid worse paining" + "\n";
		
		assertEquals(expectedResult, new String(stdout.toByteArray()));
	}
	
	@Test
	public void testPipeMultipleCommandsWithValidArgs() throws ShellException, AbstractApplicationException {
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		pipeCommand = new PipeCommand("cat text.txt | sed s/pains/paining/ | sed s/pleasures/pleasuring/");
		pipeCommand.parse();
		pipeCommand.evaluate(System.in, stdout);
		String expectedResult = "On the other hand,"
				+ " we denounce with righteous indignation and "
				+ "dislike men who are so beguiled and demoralized"
				+ " by the charms of pleasure of the moment, so blinded"
				+ " by desire, that they cannot foresee the pain and trouble "
				+ "that are bound to ensue; and equal blame belongs to those "
				+ "who fail in their duty through weakness of will, which is the "
				+ "same as saying through shrinking from toil and pain. "
				+ "These cases are perfectly simple and easy to distinguish. "
				+ "In a free hour, when our power of choice is untrammelled and when "
				+ "nothing prevents our being able to do what we like best, every"
				+ " pleasure is to be welcomed and every pain avoided. But in certain "
				+ "circumstances and owing to the claims of duty or the obligations of"
				+ " business it will frequently occur that pleasuring have to be repudiated "
				+ "and annoyances accepted. The wise man therefore always holds in these "
				+ "matters to this principle of selection: he rejects pleasuring to secure other"
				+ " greater pleasuring, or else he endures paining to avoid worse paining" + "\n";
		
		assertEquals(expectedResult, new String(stdout.toByteArray()));
	}
	
	@Test(expected = ShellException.class)
	public void testPipeTwoCommandsWithInvalidArgs() throws ShellException, AbstractApplicationException {
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		pipeCommand = new PipeCommand("|cat text.txt | sed s/pains/paining/");
		pipeCommand.parse();
		pipeCommand.evaluate(System.in, stdout);
	}
	
	@Test(expected = ShellException.class)
	public void testPipeMultipleCommandsWithInvalidArgs() throws ShellException, AbstractApplicationException {
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		pipeCommand = new PipeCommand("cat text.txt | sed s/pains/paining/ | sed s/pleasures/pleasuring/|");
		pipeCommand.parse();
		pipeCommand.evaluate(System.in, stdout);
	}

	
}

