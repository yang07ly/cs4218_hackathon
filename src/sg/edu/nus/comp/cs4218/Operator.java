package sg.edu.nus.comp.cs4218;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public interface Operator {
	/**
	 * Perform operation on the arguments and return the evaluated arguments. 
	 */
	public String[] evaluate(String... args) 
				throws AbstractApplicationException, ShellException;
}
