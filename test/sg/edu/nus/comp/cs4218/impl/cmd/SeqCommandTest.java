package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.cmd.SeqCommand;

public class SeqCommandTest {

	SeqCommand sequenceCommand;
	AppStub appStub;
	ShellStub shellStub;
	
	Vector<String> expected,actual;
	
	@Before
	public void setup() {
		sequenceCommand = new SeqCommand();
		appStub = new AppStub();
		shellStub = new ShellStub();
		expected = new Vector<String>();
		actual = new Vector<String>();
	}
	
	@Test
	public void testSeqCommandsWithNoArgs() {
		sequenceCommand = new SeqCommand("");
		try {
			sequenceCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}
				
		assertEquals(sequenceCommand.argsArray.size(), 0);
		assertEquals(sequenceCommand.argsArray, expected);
	}
	
	@Test
	public void testSeqTwoCommandsWithValidArgs() {
		sequenceCommand = new SeqCommand("echo a; echo b");
		try {
			sequenceCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}
		
		expected.add("echo a"); 
		expected.add(" echo b");
		assertEquals(sequenceCommand.argsArray.size(), 2);
		assertEquals(sequenceCommand.argsArray, expected);
	}
	
	@Test
	public void testSeqMultipleCommandsWithValidArgs() {
		sequenceCommand = new SeqCommand("echo a; echo b; echo c");
		try {
			sequenceCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}
		
		expected.add("echo a");
		expected.add(" echo b");
		expected.add(" echo c");
		assertEquals(sequenceCommand.argsArray.size(), 3);
		assertEquals(sequenceCommand.argsArray, expected);
	}
	
}
