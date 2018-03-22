package sg.edu.nus.comp.cs4218;

import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public interface Shell {
	
	/**
	 * Return a new instance of the shell.
	 */	
	public Shell newInstance();
	
	/**
	 * Runs the app specified with its arguments, input and output stream.
	 * @param app 			String containing the keyword that specifies what application
	 *           	 		to run.
	 * @param args			String array containing the arguments to pass to the
	 *            			applications for running.
	 * @param inputStream	InputputStream for the application to get arguments from, if
	 *            			needed.
	 * @param outputStream	OutputStream for the application to print its output to.
	 */	
	void runApp(String app, String[] argsArray, InputStream inputStream, OutputStream outputStream)
			throws AbstractApplicationException, ShellException;
	
	/**
	 * Parses and evaluates user's command line.
	 * @param cmdline		String of the user inputed command.
	 * @param outputStream	OutputStream for the application to print its output to.
	 */	
	public void parseAndEvaluate(String cmdline, OutputStream stdout) throws AbstractApplicationException, ShellException;
	
	
	/**
	 * Remove all unescaped double and single quotes and set all characters in
	 * quotes to escaped characters. Back quotes are not removed.
	 * @param cmd	CommandString containing the string to have its 
	 * 				double and single quotes removed and set escaped 
	 * 				characters.
	 */
	public void processQuotes(CommandString cmd) throws AbstractApplicationException, ShellException;
	
	/**
	 * Replace paths with wildcard with all the paths to existing files and 
	 * directories such that these paths can be obtained by replacing all the 
	 * unescaped asterisk symbols in specified path by some (possibly empty) 
	 * sequences of non-slash characters. If no such path exist, paths with 
	 * wildcard are not replaced.
	 * @param cmd	CommandString containing the paths with wildcard.
	 */
	public void performGlob(CommandString cmd) throws AbstractApplicationException, ShellException;
	
	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution. The commands enclosed by back quotes will be
	 * replaced by the command substitution results with newline replaced 
	 * with a space. The replaced string are not escaped.
	 * @param cmd	CommandString containing the commands enclosed by back 
	 * 				quotes for command substitution.
	 */
	public void performCmdSub(CommandString cmd) throws AbstractApplicationException, ShellException;
	
	/**
	 * Removes any IO redirection operators and their arguments and returns back the string array
	 * @param args		String array of the individual arguments.
	 */
	public String[] removeIOStreamFromArgs(String... args) throws AbstractApplicationException, ShellException;
	
	/**
	 * Scans the arguments and sets the input stream
	 * @param args		String array of the individual arguments.
	 */
	public InputStream getInputStream(String... args) throws ShellException;
	
	/**
	 * Scans the arguments and sets the output stream
	 * @param args		String array of the individual arguments.
	 */
	public OutputStream getOutputStream(String... args) throws ShellException;
}