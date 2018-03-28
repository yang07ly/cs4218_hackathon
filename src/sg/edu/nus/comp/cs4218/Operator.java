package sg.edu.nus.comp.cs4218;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public interface Operator {
	/**
	 * Perform operation on the arguments and return the evaluated arguments. 
	 */
	public void evaluate(CommandString cmd) throws AbstractApplicationException, ShellException;
}
