package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class CallCommandTest {

	static CallCommand callCommand;
	Vector<String> commands;
	
	@Before
	public void setup() {
		callCommand = new CallCommand();
		commands = new Vector<String>();
	}
	
	@Test 
	public void testExtractArgsInvalidSingleQuotes() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		try {
			callCommand.extractArgs("argA '' argB ' argC", commands);
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
		}
		assertEquals(expected, message);
	}
	
	@Test 
	public void testExtractArgsInvalidDoubleQuotes() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		try {
			callCommand.extractArgs("argA \"\" argB \" argC", commands);
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
		}
		assertEquals(expected, message);
	}
	
	@Test 
	public void testExtractArgsInvalidBackQuotes() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		try {
			callCommand.extractArgs("argA `` argB ` argC", commands);
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
		}
		assertEquals(expected, message);
	}
	
	@Test 
	public void testExtractArgsInvalidSequence() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		try {
			callCommand.extractArgs("argA ; argB ; argC", commands);
		} catch (ShellException e) {
			message = e.getMessage();
		}
		assertEquals(expected, message);
	}
	
	@Test 
	public void testExtractArgsInvalidPipe() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		try {
			callCommand.extractArgs("argA | argB | argC", commands);
		} catch (ShellException e) {
			message = e.getMessage();
		}
		assertEquals(expected, message);
	}
	
	@Test 
	public void testExtractArgsValidSingleQuotes() {
		String message = "";
		try {
			callCommand.extractArgs(" 'argA' 'argB' 'argC' ", commands);
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
		}
		assertEquals(commands, new Vector<String>(Arrays.asList(new String[]{"argA", "argB", "argC"})));
		
	}
	
	@Test 
	public void testExtractArgsValidDoubleQuotes() {
		String message = "";
		try {
			callCommand.extractArgs(" \"argA\" \"argB\" \"argC\" ", commands);
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
		}
		assertEquals(commands, new Vector<String>(Arrays.asList(new String[]{"argA", "argB", "argC"})));
	}
	
	@Test 
	public void testExtractArgsValidBackQuotes() {
		String message = "";
		try {
			callCommand.extractArgs(" `argA` `argB` `argC` ", commands);
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
		}
		assertEquals(commands, new Vector<String>(Arrays.asList(new String[]{"`argA`", "`argB`", "`argC`"})));
	}
	
	@Test 
	public void testExtractArgsBQinDQ() {
		String message = "";
		try {
			callCommand.extractArgs(" argA \"`argB`\" ", commands);
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
		}
		assertEquals(commands, new Vector<String>(Arrays.asList(new String[]{"argA", "`argB`"})));
	}
	
	@Test 
	public void testExtractArgsMixQuotes() {
		String message = "";
		try {
			callCommand.extractArgs(" argA \"`argB`\" 'argC' \"argD\" `argE` ", commands);
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			message = e.getMessage();
		}
		assertEquals(commands, new Vector<String>(Arrays.asList(new String[]{"argA", "`argB`", "argC", "argD", "`argE`"})));
	}
	
}

