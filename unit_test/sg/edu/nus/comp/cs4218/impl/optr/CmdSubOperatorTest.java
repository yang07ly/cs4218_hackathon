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
import sg.edu.nus.comp.cs4218.impl.optr.CmdSubOperator;

public class CmdSubOperatorTest {
	private static final String CMD_STR = "command";
	private static final String CMDSUB_STR = "`" + CMD_STR + "`";
	
	private ShellStub spyShell;
	private CmdSubOperator cmdSubOptr;
	private String[] input, output, expected;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		input = output = expected = new String[0];
		spyShell = Mockito.spy(new ShellStub());
		cmdSubOptr = new CmdSubOperator(spyShell);
	}
	
	@Test
	public void testNoCmdSub() throws AbstractApplicationException, ShellException {
		expected = new String[] {CMD_STR};
		input = new String[] {CMD_STR};
		output = cmdSubOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testNoCmdSubWithSQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {CMD_STR};
		input = new String[] {"'" + CMD_STR + "'"};
		output = cmdSubOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testNoCmdSubWithDQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {CMD_STR};
		input = new String[] {"\"" + CMD_STR + "\""};
		output = cmdSubOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testCmdSub() throws AbstractApplicationException, ShellException {
		expected = new String[] {ShellStub.EVA_RESULT};
		input = new String[] {CMDSUB_STR};
		output = cmdSubOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testCmdSubWithDQ() throws AbstractApplicationException, ShellException {
		expected = new String[] {ShellStub.EVA_RESULT};
		input = new String[] {"\"" + CMDSUB_STR + "\""};
		output = cmdSubOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testCmdSubWithChars() throws AbstractApplicationException, ShellException {
		expected = new String[] {"out" + ShellStub.EVA_RESULT + "side"};
		input = new String[] {"out" + CMDSUB_STR + "side"};
		output = cmdSubOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testMutipleCmdSub() throws AbstractApplicationException, ShellException {
		expected = new String[] {ShellStub.EVA_RESULT + ShellStub.EVA_RESULT};
		input = new String[] {CMDSUB_STR + CMDSUB_STR};
		output = cmdSubOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testMutipleCmdSubElement() throws AbstractApplicationException, ShellException {
		expected = new String[] {ShellStub.EVA_RESULT, ShellStub.EVA_RESULT, ShellStub.EVA_RESULT};
		input = new String[] {CMDSUB_STR, CMDSUB_STR, CMDSUB_STR};
		output = cmdSubOptr.evaluate(input);
		assertArrayEquals(expected, output);
	}
	
	@Test
	public void testCmdSubAppReturnMultipleLines() throws AbstractApplicationException, ShellException {
		input = new String[] {"result line 1. result line 2."};  
        Shell mockShell = Mockito.mock(Shell.class);
        Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
        Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				OutputStream output = (OutputStream) invocation.getArguments()[1];
				output.write("result line 1.\n result line 2.".getBytes());
				return null;
			}
		}).when(mockShell).parseAndEvaluate(Mockito.anyString(), Mockito.any(OutputStream.class));
        
        cmdSubOptr.evaluate(input);
	}
	
	@Test
	public void testInvalidCmdSubQuoteNotClosed() throws AbstractApplicationException, ShellException {
		input = new String[] {"`" + CMD_STR};
		Mockito.doThrow(ShellException.class).when(spyShell).removeQuotes(Mockito.anyString());
		
		thrown.expect(ShellException.class);    
        output = cmdSubOptr.evaluate(input);
	}
	
	@Test
	public void testInvalidCmdSubCmd() throws AbstractApplicationException, ShellException {
		input = new String[] {CMDSUB_STR};       
        Shell mockShell = Mockito.mock(Shell.class);
        Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
        Mockito.doThrow(ShellException.class).when(mockShell).parseAndEvaluate(Mockito.anyString(), Mockito.any(OutputStream.class));

        thrown.expect(ShellException.class);
        cmdSubOptr.evaluate(input);
	}
	
	@Test
	public void testInvalidCmdSubApp() throws AbstractApplicationException, ShellException {
		input = new String[] {CMDSUB_STR};  
        Shell mockShell = Mockito.mock(Shell.class);
        Mockito.when(spyShell.newInstance()).thenReturn(mockShell);
        Mockito.doThrow(LsException.class).when(mockShell).parseAndEvaluate(Mockito.anyString(), Mockito.any(OutputStream.class));
        
        thrown.expect(LsException.class);
        cmdSubOptr.evaluate(input);
	}
}
