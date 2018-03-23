package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.cmd.SeqCommand;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class SeqCommandIT {
	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system";
	private static final String NEW_LINE = "\n";
	
	private SeqCommand seqCmd;
	private CommandString cmdLine;
	private String expected;
	private ByteArrayOutputStream output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		expected = "";
		cmdLine = new CommandString();
		output = new ByteArrayOutputStream();
	}
	
	@Test
	public void testEvalSeqWithoutParse() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo no parse");
		expected = "";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testInvalidSeqEmpty() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: : Invalid app");
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}

	@Test
	public void testNoSeq() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo no sequence");
		expected = "no sequence";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOneSeq() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo one; echo sequence");
		expected = "one" + NEW_LINE + "sequence";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testMultiSeq() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo seq1; echo seq2; echo seq3; echo seq4");
		expected = "seq1" + NEW_LINE + "seq2" + NEW_LINE + "seq3" + NEW_LINE + "seq4";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testSeqWithinText() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo within;echo text");
		expected = "within" + NEW_LINE + "text";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testInvalidSeqAtStart() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("; echo semicolon at front");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid semicolon operator/s");

		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}

	@Test
	public void testSeqAtEnd() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo semicolon; echo at end;");
		expected = "semicolon" + NEW_LINE + "at end";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testInvalidSeqAtStartAndEnd() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("; echo front; echo end;");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid semicolon operator/s");
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}
	
	@Test
	public void testSeqWithleadingAndTrailingSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("    echo leading spaces; echo trailing spaces    ");
		expected = "leading spaces" + NEW_LINE + "trailing spaces";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}
	
	@Test
	public void testSeqEscapedSemicolon() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo escaped; echo sequence");
		cmdLine.setCharEscaped(12, true);
		expected = "escaped; echo sequence";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testSeqEscapedWithValidSemicolon() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo escaped; echo sequence; echo sequence 2");
		cmdLine.setCharEscaped(12, true);
		expected = "escaped; echo sequence" + NEW_LINE + "sequence 2";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testMultiSeqWithFrontAffectingBack() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("cd ls_test_system; ls");
		expected = "\n'file name with space.txt'  file1.txt  file2.txt  'folder name with space'  folder1  folder2";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testNoSeqWithPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo no seq with pipe | sed s/pipe/Pipe/");
		expected = "no seq with Pipe";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOneSeqWithPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo one seq | sed s/seq/Seq/; echo with pipe | sed s/pipe/Pipe/");
		expected = "one Seq" + NEW_LINE + "with Pipe";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testMultiSeqWithPipe() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo seq1 | sed s/seq/Seq/; echo seq2; echo seq3 | sed s/seq/Seq/");
		expected = "Seq1" + NEW_LINE + "seq2" + NEW_LINE + "Seq3";
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testInvalidSeqAppException() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("cat nonExistentFile");

		thrown.expect(AbstractApplicationException.class);
		thrown.expectMessage("cat: nonExistentFile: No such file or directory");
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}
	
	@Test
	public void testInvalidSeqShellException() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo seq1; echo seq2 |");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: Invalid pipe operator/s");
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}

	@Test
	public void testInvalidMultiSeqWithExceptionAtStart() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("eco seq1; echo seq2; echo seq3; echo seq4");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: eco: Invalid app");
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}

	@Test
	public void testInvalidMultiSeqWithExceptionAtMiddle() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo seq1; echo seq2; eco seq3; echo seq4");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: eco: Invalid app");
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}

	@Test
	public void testInvalidMultiSeqWithExceptionAtEnd() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo seq1; echo seq2; eco seq3; echo seq4");

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: eco: Invalid app");
		
		seqCmd = new SeqCommand(new ShellImpl(), cmdLine);
		seqCmd.parse();
		seqCmd.evaluate(System.in, output);
	}
}
