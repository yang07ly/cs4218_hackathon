package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.EchoException;

public class EchoApplicationTest {
	
	EchoApplication echoApp;
	String expected, result;
	OutputStream stdout;
	
	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system";
		echoApp = new EchoApplication();
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void testEchoText() {
		expected = "text\n";
		try {
			String[] strArr = {"text"};
			echoApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (EchoException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testEchoMultipleText() {
		expected = "text1 text2\n";
		try {
			String[] strArr = {"text1", "text2"};
			echoApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (EchoException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testEchoEmptyString() {
		expected = "\n";
		try {
			String[] strArr = {""};
			echoApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (EchoException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testEchoSpaces() {
		expected = "   \n";
		try {
			String[] strArr = {"   "};
			echoApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (EchoException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testEchoEmptyArgs() {
		expected = "\n";
		try {
			String[] strArr = {};
			echoApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (EchoException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testEchoTextWithSpaces() {
		expected = "text1     text2\n";
		try {
			String[] strArr = {"text1     text2"};
			echoApp.run(strArr, null, stdout);
			result = stdout.toString();
		} catch (EchoException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullArgs() {
		expected = "echo: Null arguments";
		try {
			echoApp.run(null, null, stdout);
			result = stdout.toString();
		} catch (EchoException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNullOutputStream() {
		expected = "echo: OutputStream not provided";
		try {
			String[] strArr = {"text"};
			echoApp.run(strArr, null, null);
		} catch (EchoException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
	}
}
