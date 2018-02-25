package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.EchoException;
import sg.edu.nus.comp.cs4218.impl.app.EchoApplication;

public class QuotingTest {
	
	ShellImpl TestShell;
	String expected, result;
	OutputStream stdout;

	@Before
	public void setUp() throws Exception {
		TestShell = new ShellImpl();
		expected = new String();
		result = new String();
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void DoubleQuoteTest() {
		expected = "its a double quote";
		
		result = TestShell.DetectQuotes("\" ");
		assertEquals(expected, result);
		
	}
	@Test
	public void SingleQuoteTest() {
		expected = "its a single quote";
		
		result = TestShell.DetectQuotes("'");
		assertEquals(expected, result);
		
	}
	@Test
	public void BackQuoteTest() {
		expected = "its a backquote";
		
		result = TestShell.DetectQuotes("`");
		assertEquals(expected, result);
		
	}

}
