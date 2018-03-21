package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.cmd.SeqCommand;

public class SeqCommandIT {
	private final String line = "\n";
	private SeqCommand seqCom;
	private String cmdLine, expected;
	private ByteArrayOutputStream output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		expected = cmdLine = "";
		output = new ByteArrayOutputStream();
	}

	@Test
	public void testEmptyCmd() throws ShellException, AbstractApplicationException {
		cmdLine = "";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "shell: : Invalid app";

		thrown.expect(ShellException.class);
		thrown.expectMessage(expected);
		
		seqCom.parse();
		seqCom.evaluate(System.in, output);
	}

	@Test
	public void testNoSeq() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "abc";

		seqCom.parse();
		seqCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testOneSeq() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc; echo def";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "abc" + line + "def";

		seqCom.parse();
		seqCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testSeqAtStart() throws ShellException, AbstractApplicationException {
		cmdLine = "; echo abc; echo def";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "shell: Invalid semicolon operator/s";

		thrown.expect(ShellException.class);
		thrown.expectMessage(expected);
		
		seqCom.parse();
		seqCom.evaluate(System.in, output);
	}

	@Test
	public void testSeqAtEnd() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc; echo def;";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "abc" + line + "def";

		seqCom.parse();
		seqCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testSeqAtStartAndEnd() throws ShellException, AbstractApplicationException {
		cmdLine = "; echo abc; echo def;";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "shell: Invalid semicolon operator/s";

		thrown.expect(ShellException.class);
		thrown.expectMessage(expected);
		
		seqCom.parse();
		seqCom.evaluate(System.in, output);
	}

	@Test
	public void testMultiSeq() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc; echo def; echo ghi; echo jkl";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "abc" + line + "def" + line + "ghi" + line + "jkl";

		seqCom.parse();
		seqCom.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testMultiSeqWithExceptionAtStart() throws ShellException, AbstractApplicationException {
		cmdLine = "eco abc; echo def; echo ghi; echo jkl";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "shell: eco: Invalid app";

		thrown.expect(ShellException.class);
		thrown.expectMessage(expected);
		
		seqCom.parse();
		seqCom.evaluate(System.in, output);
	}

	@Test
	public void testMultiSeqWithExceptionAtMiddle() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc; echo def; eco ghi; echo jkl";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "abc" + line + "def" + line + "shell: eco: Invalid app";

		thrown.expect(ShellException.class);
		thrown.expectMessage(expected);
		
		seqCom.parse();
		seqCom.evaluate(System.in, output);
	}

	@Test
	public void testMultiSeqWithExceptionAtEnd() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc; echo def; echo ghi; eco jkl";
		seqCom = new SeqCommand(new ShellImpl(), cmdLine);
		expected = "abc" + line + "def" + line + "ghi" + line + "shell: eco: Invalid app";

		thrown.expect(ShellException.class);
		thrown.expectMessage(expected);
		
		seqCom.parse();
		seqCom.evaluate(System.in, output);
	}

}
