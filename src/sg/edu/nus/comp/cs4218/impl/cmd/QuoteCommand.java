package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.exception.QuoteException;

public class QuoteCommand{
	String commandString;
	
	public QuoteCommand(String cmdString) {
		commandString = cmdString;
	}

	public String evaluate(InputStream stdin, OutputStream stdout) throws QuoteException{
		
		return null;
	}
}
