package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class CallCommandTest {

	private String expected, cmdLine, app;

	@Before
	public void setUp() throws Exception {
		cmdLine = expected = app = "";
	}
	
    @Test
    public void testParseAppName() throws ShellException, AbstractApplicationException {
    	cmdLine = "echo abc";
    	expected = "echo";
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        assertEquals(expected, callCommand.app);
    }
    
    @Test
    public void testParseAppNameWithOneSpace() throws ShellException, AbstractApplicationException {
    	cmdLine = " echo abc";
    	expected = "echo";
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        assertEquals(expected, callCommand.app);
    }
    
    @Test
    public void testParseAppNameWithMultipleSpaces() throws ShellException, AbstractApplicationException {
    	cmdLine = "    echo abc";
    	expected = "echo";
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        assertEquals(expected, callCommand.app);
    }
    
    @Test
    public void testParseAppNameWithOneTab() throws ShellException, AbstractApplicationException {
    	cmdLine = "\techo abc";
    	expected = "echo";
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        assertEquals(expected, callCommand.app);
    }
    
    @Test
    public void testParseAppNameWithMultipleTabs() throws ShellException, AbstractApplicationException {
    	cmdLine = "\t\t\techo abc";
    	expected = "echo";
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        assertEquals(expected, callCommand.app);
    }
    
    @Test
    public void testParseAppNameWithMultipleTabsSpace() throws ShellException, AbstractApplicationException {
    	cmdLine = "  \t\t   \t  echo abc";
    	expected = "echo";
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        assertEquals(expected, callCommand.app);
    }

    @Test
    public void testParseArgs() throws ShellException, AbstractApplicationException {
        int expectedNumArgs = 2;
    	cmdLine = "echo abc def";
        String[] expectedArgs = {"abc", "def"};

        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();

        assertEquals(expectedNumArgs, callCommand.argsArray.length);
        assertArrayEquals(expectedArgs, callCommand.argsArray);
    }

    @Test
    public void testParseArgsWithMultipleSpace() throws ShellException, AbstractApplicationException {
        int expectedNumArgs = 2;
    	cmdLine = "echo  abc     def";
        String[] expectedArgs = {"abc", "def"};
        
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        
        assertEquals(expectedNumArgs, callCommand.argsArray.length);
        assertArrayEquals(expectedArgs, callCommand.argsArray);
    }

    @Test
    public void testParseArgsWithOneTab() throws ShellException, AbstractApplicationException {
        int expectedNumArgs = 2;
    	cmdLine = "echo\tabc\tdef";
        String[] expectedArgs = {"abc", "def"};
        
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        
        assertEquals(expectedNumArgs, callCommand.argsArray.length);
        assertArrayEquals(expectedArgs, callCommand.argsArray);
    }

    @Test
    public void testParseArgsWithMultipleTabs() throws ShellException, AbstractApplicationException {
        int expectedNumArgs = 2;
    	cmdLine = "echo\t\tabc\t\t\tdef";
        String[] expectedArgs = {"abc", "def"};
        
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        
        assertEquals(expectedNumArgs, callCommand.argsArray.length);
        assertArrayEquals(expectedArgs, callCommand.argsArray);
    }

    @Test
    public void testParseArgsWithMultipleTabsSpace() throws ShellException, AbstractApplicationException {
        int expectedNumArgs = 2;
    	cmdLine = "echo\t    \tabc   \t\t\t     def";
        String[] expectedArgs = {"abc", "def"};
        
        CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
        callCommand.parse();
        
        assertEquals(expectedNumArgs, callCommand.argsArray.length);
        assertArrayEquals(expectedArgs, callCommand.argsArray);
    }

}