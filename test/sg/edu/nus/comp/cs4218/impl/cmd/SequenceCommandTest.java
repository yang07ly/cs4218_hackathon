package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.cmd.SequenceCommand;

public class SequenceCommandTest {

	SequenceCommand sequenceCommand;
	
	@Before
	public void setup() {
		sequenceCommand = new SequenceCommand();
	}
	
	@Test
	public void testSequenceTwoCommandsNoArgs() throws ShellException, AbstractApplicationException {
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("");
		sequenceCommand.parse();
		sequenceCommand.evaluate(System.in, stdout);
		assertEquals("", new String(stdout.toByteArray()));
	}
	
	@Test
	public void testSequenceTwoCommandsWithValidArgs() throws ShellException, AbstractApplicationException {
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo a; echo b");
		sequenceCommand.parse();
		sequenceCommand.evaluate(System.in, stdout);
		
		String expectedResult = "a"  + "\n" + "b" + "\n";
		assertEquals(expectedResult, new String(stdout.toByteArray()));
	}
	
	@Test
	public void testSequenceMultipleCommandsWithValidArgs() throws ShellException, AbstractApplicationException {
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo a; echo b; echo c");
		sequenceCommand.parse();
		sequenceCommand.evaluate(System.in, stdout);
		
		String expectedResult = "a"  + "\n" + "b" + "\n" + "c" + "\n";
		assertEquals(expectedResult, new String(stdout.toByteArray()));
	}
}
