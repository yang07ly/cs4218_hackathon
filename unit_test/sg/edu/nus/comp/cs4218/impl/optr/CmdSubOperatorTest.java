package sg.edu.nus.comp.cs4218.impl.optr;

import static org.junit.Assert.*;

import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.LsException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellStub;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
import sg.edu.nus.comp.cs4218.impl.optr.CmdSubOperator;

public class CmdSubOperatorTest {
	private static final String CMD_STR = "cmd";
	private static final String CMDSUB_STR = "`" + CMD_STR + "`";
	private static final String NOT_CLOSED_EXP = "shell: Back Quotes not closed";
	
	private ShellStub spyShell;
	private CmdSubOperator cmdSubOptr;
	private CommandString cmd;
	private String expectedStr;
	private Boolean[] expectedBool;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		spyShell = Mockito.spy(new ShellStub());
		cmdSubOptr = new CmdSubOperator(spyShell);
		cmd = new CommandString();
		expectedStr = "";
		expectedBool = new Boolean[0];
	}
	
	@Test
	public void testNoCmdSub() throws AbstractApplicationException, ShellException {
		expectedStr = CMD_STR;
		expectedBool = new Boolean[] {false, false, false};
		
		cmd = new CommandString(CMD_STR);
		cmdSubOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testCmdSub() throws AbstractApplicationException, ShellException {
		expectedStr = ShellStub.SHELL_RESULT;
		expectedBool = new Boolean[] {false, false, false, false, false, false};
		
		cmd = new CommandString(CMDSUB_STR);
		cmdSubOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testCmdSubEscaped() throws AbstractApplicationException, ShellException {
		expectedStr = CMDSUB_STR;
		expectedBool = new Boolean[] {true, true, true, true, true};
		
		cmd = new CommandString(CMDSUB_STR);
		cmd.setCharEscaped(0, true, true, true, true, true);
		cmdSubOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testCmdSubWithinChars() throws AbstractApplicationException, ShellException {
		expectedStr = "F" + ShellStub.SHELL_RESULT + "E";
		expectedBool = new Boolean[] {false, false, false, false, false, false, false, false};
		
		cmd = new CommandString("F" + CMDSUB_STR + "E");
		cmd.setCharEscaped(0, false, false, true, true, true, false, false);
		cmdSubOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testMutipleCmdSub() throws AbstractApplicationException, ShellException {
		expectedStr = ShellStub.SHELL_RESULT + ShellStub.SHELL_RESULT;
		expectedBool = new Boolean[] {false, false, false, false, false, false, false, false, false, false, false, false};
		
		cmd = new CommandString(CMDSUB_STR + CMDSUB_STR);
		cmd.setCharEscaped(0, false, true, true, true, false, false, true, true, true, false);
		cmdSubOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testCmdSubAppReturnMultipleLines() throws AbstractApplicationException, ShellException {
		expectedStr = "Rline1. Rline2.";
		expectedBool = new Boolean[] {false, false, false, false, false, false, false, false, 
										false, false, false, false, false, false, false};
		
		Shell mockShell = Mockito.mock(Shell.class);
        Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
        Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				OutputStream output = (OutputStream) invocation.getArguments()[1];
				output.write("Rline1.\nRline2.".getBytes());
				return null;
			}
		}).when(mockShell).parseAndEvaluate(Mockito.anyString(), Mockito.any(OutputStream.class));
		
		cmd = new CommandString(CMDSUB_STR);
		cmd.setCharEscaped(0, false, true, true, true, false);
		cmdSubOptr.evaluate(cmd);
		
		assertEquals(expectedStr, cmd.toString());
		assertArrayEquals(expectedBool, cmd.toBoolArray());
	}
	
	@Test
	public void testInvalidCmdSubQuoteNotClosed() throws AbstractApplicationException, ShellException {
		cmd = new CommandString("`" + CMD_STR);
		cmd.setCharEscaped(0, false, true, true, true);	
		thrown.expect(ShellException.class);
		thrown.expectMessage(NOT_CLOSED_EXP); 
		cmdSubOptr.evaluate(cmd);
	}
	
	@Test
	public void testInvalidCmdSubCmd() throws AbstractApplicationException, ShellException {
        Shell mockShell = Mockito.mock(Shell.class);
        Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
        Mockito.doThrow(ShellException.class).when(mockShell).parseAndEvaluate(Mockito.anyString(), Mockito.any(OutputStream.class));

        thrown.expect(ShellException.class);
        cmd = new CommandString(CMDSUB_STR);
        cmdSubOptr.evaluate(cmd);
	}
	
	@Test
	public void testInvalidCmdSubApp() throws AbstractApplicationException, ShellException {
        Shell mockShell = Mockito.mock(Shell.class);
        Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
        Mockito.doThrow(LsException.class).when(mockShell).parseAndEvaluate(Mockito.anyString(), Mockito.any(OutputStream.class));
        
        thrown.expect(LsException.class);
        cmd = new CommandString(CMDSUB_STR);
        cmdSubOptr.evaluate(cmd);
	}
}
