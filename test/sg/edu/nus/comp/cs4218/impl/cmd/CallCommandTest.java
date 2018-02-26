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
	String[] expected, actual;
	 
	@Before
	public void setup() {
		callCommand = new CallCommand();
		commands = new Vector<String>();
	}
	
	@Test 
	public void testCallCommandParsing() {
		callCommand = new CallCommand("echo abc");
		try {
			callCommand.parse();
		} catch (ShellException e) {
			e.printStackTrace();
		}
		
		expected = new String[1];
		expected[0] = "abc";
		assertEquals(1,callCommand.argsArray.length);
		assertTrue(Arrays.equals(expected, callCommand.argsArray) );
	}
	
	@Test 
	public void testInvalidIoRedirInput() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		 
		callCommand = new CallCommand("cat << input.txt");
		try {
			callCommand.parse();
		} catch (ShellException e) {
			message = e.getMessage(); 
		}
		
		assertEquals(expected,message);
	}
	
	@Test 
	public void testInvalidIoRedirInputOutput() {
		String expected = "shell: Input redirection file same as output redirection file.";
		String message = "";
		
		callCommand = new CallCommand("cat < input.txt > input.txt");
		try {
			callCommand.parse();
		} catch (ShellException e) {
			message = e.getMessage(); 
		}
		
		assertEquals(expected,message);
	}
	
	@Test 
	public void testInvalidIoRedirOutputOutput() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		
		callCommand = new CallCommand("cat > output.txt  > output.txt");
		try {
			callCommand.parse();
		} catch (ShellException e) {
			message = e.getMessage(); 
		}
		
		assertEquals(expected,message);
	}
	
	@Test 
	public void testInvalidIoRedirInputInput() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		
		callCommand = new CallCommand("cat < input.txt < input.txt");
		try {
			callCommand.parse(); 
		} catch (ShellException e) {
			message = e.getMessage(); 
		}
		
		assertEquals(expected,message);
	}
	
	@Test 
	public void testInvalidIoRedirInputEmpty() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		
		callCommand = new CallCommand("cat < ");
		try {
			callCommand.parse(); 
		} catch (ShellException e) {
			message = e.getMessage(); 
		}
		
		assertEquals(expected,message);
	}
	
	
	
	
	@Test 
	public void testInvalidIoRedirOutput() {
		String expected = "shell: Invalid syntax encountered.";
		String message = "";
		
		callCommand = new CallCommand("echo >> output.txt");
		try {
			callCommand.parse();
		} catch (ShellException e) {
			message = e.getMessage();
		}
		
		assertEquals(expected,message);
	}
	@Test 
	public void testValidIoRedirInput() {
		callCommand = new CallCommand("cat < input.txt");
		try {
			callCommand.parse();
		} catch (ShellException e) {
			e.printStackTrace();
		}
		
		assertEquals("input.txt",callCommand.inputStreamS);
	}
	
	@Test 
	public void testValidIoRedirOutput() {
		callCommand = new CallCommand("echo > output.txt");
		try {
			callCommand.parse();
		} catch (ShellException e) { 
			e.printStackTrace();
		}
		
		assertEquals("output.txt",callCommand.outputStreamS);
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

