package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class CommandSubstitutionTest {
	
	CallCommand callCommand;

	CmdSubCommand cmdSubCommand;
	
	String[] expected,actual;

	@Before
	public void setup() {
		callCommand = new CallCommand();
		Environment.currentDirectory = System.getProperty("user.dir");

		
		
	}
	
	@Test 
	public void testInvalidAppName() {
		String expected = "shell: xyz: Invalid app.";
		String actual = "";
		callCommand = new CallCommand("echo `xyz cats`");
		try {
			callCommand.parse();
			cmdSubCommand = new CmdSubCommand(callCommand.argsArray);
			cmdSubCommand.evaluate(System.in, System.out);
		} catch (ShellException e) {
			actual = e.getMessage();
		} catch (AbstractApplicationException e) {
			actual = e.getMessage();
		}
		assertEquals(expected, actual);
		
	}
	
//	@Test 
//	public void testInvalidBQFront() {
//		try {
//			actual = ShellImpl.processBQ("``echo cats`");
//		} catch (ShellException e) {
//			e.printStackTrace();
//		} catch (AbstractApplicationException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = "";
//		assertEquals(1, actual.length);
//		assertTrue(Arrays.equals(expected, actual));
//
//	}
//	
//	@Test 
//	public void testInvalidBQBack() {
//		try {
//			actual = ShellImpl.processBQ("`echo cats``");
//		} catch (ShellException e) {
//			e.printStackTrace();
//		} catch (AbstractApplicationException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = "cats";
//		assertEquals(1, actual.length);
//		assertTrue(Arrays.equals(expected, actual));
//	
//	}
	
	@Test 
	public void testBQinBQ() {
		try {
			cmdSubCommand = new CmdSubCommand("`echo ` echo cats ``");
			cmdSubCommand.evaluate(System.in, System.out);
			actual = cmdSubCommand.getArgsArray();
		} catch (ShellException e) {
			e.printStackTrace();
		} catch (AbstractApplicationException e) {
			e.printStackTrace();
		}
		expected = new String[1];
		expected[0] = " echo cats ``";
		assertEquals(1, actual.length);
		assertTrue(Arrays.equals(expected, actual));
	
	}
		
	@Test 
	public void testCommandSubWithSemicolon() {

		try {
			cmdSubCommand = new CmdSubCommand("`echo cats; echo dogs`");
			cmdSubCommand.evaluate(System.in, System.out);
			actual = cmdSubCommand.getArgsArray();
		} catch (ShellException e) {
			e.printStackTrace();
		} catch (AbstractApplicationException e) {
			e.printStackTrace();
		} 

		expected = new String[2];
		expected[0] = "cats";
		expected[1] = "dogs";

		assertEquals(2, actual.length);
		assertTrue(Arrays.equals(expected, actual));
		
	}
	
	@Test 
	public void testCommandSubWithPipe() {

		try {
			cmdSubCommand = new CmdSubCommand("`cat text.txt | sed s/pains/paining/`");
			cmdSubCommand.evaluate(System.in, System.out);
			actual = cmdSubCommand.getArgsArray();
		} catch (ShellException e) {
			e.printStackTrace();
		} catch (AbstractApplicationException e) {
			e.printStackTrace();
		}
		
		assertEquals(173, actual.length);
		assertEquals("paining", actual[172]);
		assertEquals("paining", actual[168]);
		
	}
	
	@Test 
	public void testCommandSubWithPipes() {
		InputStream inputBuffer = null;
		ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
		try {
			cmdSubCommand = new CmdSubCommand("`cat text.txt | sed s/pains/pain/ | sed s/pleasures/pleasuring/`");
			cmdSubCommand.evaluate(inputBuffer, outputBuffer);
			actual = cmdSubCommand.getArgsArray();
			
		} catch (ShellException e) {
			e.printStackTrace();
		} catch (AbstractApplicationException e) {
			e.printStackTrace();
		}
		
		assertEquals(173, actual.length);
		assertEquals("pain", actual[172]);
		assertEquals("pain", actual[168]);
		assertEquals("pleasuring,", actual[163]);
		assertEquals("pleasuring", actual[158]);
		assertEquals("pleasuring", actual[134]);

	}
	
	@Test 
	public void testCommandSubWithSemicolons() {

		try {
			cmdSubCommand = new CmdSubCommand("`echo first; echo second; echo third`");
			cmdSubCommand.evaluate(System.in, System.out);
			actual = cmdSubCommand.getArgsArray();
		} catch (ShellException e) {
			e.printStackTrace();
		} catch (AbstractApplicationException e) {
			e.printStackTrace();
		}
		expected = new String[3];
		expected[0] = "first";
		expected[1] = "second";
		expected[2] = "third";

		assertEquals(3, actual.length);
		assertTrue(Arrays.equals(expected, actual));

	}
	


	
}
