//package sg.edu.nus.comp.cs4218.impl.cmd;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.Arrays;
//import java.util.Vector;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import sg.edu.nus.comp.cs4218.exception.ShellException;
//
//public class CallCommandTest {
//
//	private static final String ABC = "abc";
//
//	public static final String EXP_SYNTAX = "shell: Invalid syntax encountered.";
//
//	static CallCommand callCommand;
//	Vector<String> commands;
//	String[] expected, actual;
//	String expectedSyntaxErr;
//	String errorMessage;
//
//	@Before
//	public void setup() {
//		callCommand = new CallCommand();
//		commands = new Vector<String>();
//		errorMessage = "";
//	}
//
//	@Test
//	public void testOneCallCommandParsingWithArgs() {
//		callCommand = new CallCommand("echo abc");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals(1, callCommand.argsArray.length);
//		assertEquals("echo", callCommand.app);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//	@Test
//	public void testOneCallCommandParsingWithEmptyArgs() {
//		callCommand = new CallCommand("");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[0];
//		assertEquals(0, callCommand.argsArray.length);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//	@Test
//	public void testInvalidIoRedirInput() {
//		callCommand = new CallCommand("cat << input.txt");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testInvalidIoRedirInputOutput() {
//		String expected = "shell: Input redirection file same as output redirection file.";
//
//		callCommand = new CallCommand("cat < input.txt > input.txt");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//
//		assertEquals(expected, errorMessage);
//	}
//
//	@Test
//	public void testInvalidIoRedirOutputOutput() {
//
//		callCommand = new CallCommand("cat > output.txt  > output.txt");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testInvalidIoRedirInputInput() {
//
//		callCommand = new CallCommand("cat < input.txt < input.txt");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testInvalidIoRedirInputEmpty() {
//
//		callCommand = new CallCommand("cat < ");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testInvalidIoRedirOutput() {
//		callCommand = new CallCommand("echo >> output.txt");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testValidIoRedirInput() {
//		callCommand = new CallCommand("cat < input.txt");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//
//		assertEquals("input.txt", callCommand.inputStreamS);
//	}
//
//	@Test
//	public void testValidIoRedirOutput() {
//		callCommand = new CallCommand("echo > output.txt");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//
//		assertEquals("output.txt", callCommand.outputStreamS);
//	}
//
//	@Test
//	public void testExtractArgsInvalidSingleQuotes() {
//		String message = "";
//		try {
//			callCommand.extractArgs("argA '' argB ' argC", commands);
//		} catch (ShellException e) {
//			message = e.getMessage();
//		}
//		assertEquals(EXP_SYNTAX, message);
//	}
//
//	@Test
//	public void testExtractArgsInvalidDoubleQuotes() {
//		try {
//			callCommand.extractArgs("argA \"\" argB \" argC", commands);
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testExtractArgsInvalidBackQuotes() {
//		try {
//			callCommand.extractArgs("argA `` argB ` argC", commands);
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testExtractArgsInvalidSequence() {
//		try {
//			callCommand.extractArgs("argA ; argB ; argC", commands);
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testExtractArgsInvalidPipe() {
//		try {
//			callCommand.extractArgs("argA | argB | argC", commands);
//		} catch (ShellException e) {
//			errorMessage = e.getMessage();
//		}
//		assertEquals(EXP_SYNTAX, errorMessage);
//	}
//
//	@Test
//	public void testExtractArgsValidSingleQuotes() {
//		try {
//			callCommand.extractArgs(" 'argA' 'argB' 'argC' ", commands);
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		assertEquals(commands, new Vector<String>(Arrays.asList(new String[] { "argA", "argB", "argC" })));
//
//	}
//
//	@Test
//	public void testExtractArgsValidDoubleQuotes() {
//		try {
//			callCommand.extractArgs(" \"argD\" \"argE\" \"argF\" ", commands);
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		assertEquals(commands, new Vector<String>(Arrays.asList(new String[] { "argD", "argE", "argF" })));
//	}
//
//	@Test
//	public void testExtractArgsValidBackQuotes() {
//		try {
//			callCommand.extractArgs(" `argG` `argH` `argI` ", commands);
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		assertEquals(commands, new Vector<String>(Arrays.asList(new String[] { "`argG`", "`argH`", "`argI`" })));
//	}
//
//	@Test
//	public void testExtractArgsBQinDQ() {
//		try {
//			callCommand.extractArgs(" argA \"`argB`\" ", commands);
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		assertEquals(commands, new Vector<String>(Arrays.asList(new String[] { "argA", "`argB`" })));
//	}
//
//	@Test
//	public void testExtractArgsMixQuotes() {
//
//		try {
//			callCommand.extractArgs(" argA \"`argB`\" 'argC' \"argD\" `argE` ", commands);
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		assertEquals(commands,
//				new Vector<String>(Arrays.asList(new String[] { "argA", "`argB`", "argC", "argD", "`argE`" })));
//	}
//
//	@Test
//	public void testEcho() {
//		callCommand = new CallCommand("echo abc");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals("echo", callCommand.app);
//		assertEquals(1, callCommand.argsArray.length);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//	@Test
//	public void testCat() {
//		callCommand = new CallCommand("cat abc");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals("cat", callCommand.app);
//		assertEquals(1, callCommand.argsArray.length);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//	@Test
//	public void testLs() {
//		callCommand = new CallCommand("ls abc");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals("ls", callCommand.app);
//		assertEquals(1, callCommand.argsArray.length);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//	@Test
//	public void testExit() {
//		callCommand = new CallCommand("exit");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals("exit", callCommand.app);
//	}
//
//	@Test
//	public void testCd() {
//		callCommand = new CallCommand("cd abc");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals("cd", callCommand.app);
//		assertEquals(1, callCommand.argsArray.length);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//	@Test
//	public void testSed() {
//		callCommand = new CallCommand("sed abc");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals("sed", callCommand.app);
//		assertEquals(1, callCommand.argsArray.length);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//	@Test
//	public void testSplit() {
//		callCommand = new CallCommand("split abc");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals("split", callCommand.app);
//		assertEquals(1, callCommand.argsArray.length);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//	@Test
//	public void testCmp() {
//		callCommand = new CallCommand("cmp abc");
//		try {
//			callCommand.parse();
//		} catch (ShellException e) {
//			e.printStackTrace();
//		}
//		expected = new String[1];
//		expected[0] = ABC;
//		assertEquals("cmp", callCommand.app);
//		assertEquals(1, callCommand.argsArray.length);
//		assertTrue(Arrays.equals(expected, callCommand.argsArray));
//	}
//
//}
