package sg.edu.nus.comp.cs4218;

import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

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
	 * Returns all indices of the specified character that is not within
	 * any quotes.
	 * @param source	String used to check for the specified character.
	 * @param sepChar	Character to find in the string.
	 */
	public Integer[] getIndicesOfCharNotInQuotes(String source, char character) throws ShellException;
	
	/**
	 * Returns the the list of string with double and single quotes removed.
	 * Back quotes are not removed.
	 * @param cmdArgs	String Array containing the string to have its double and 
	 * 					single quotes removed.
	 */
	public String[] removeQuotes(String... source) throws AbstractApplicationException, ShellException;
	
	/**
	 * Returns all the paths to existing files and directories such that these 
	 * paths can be obtained by replacing all the unquoted asterisk symbols in 
	 * specified path by some (possibly empty) sequences of non-slash characters.
	 * If no such path exist, the specified path is return without changes.
	 * @param args		Array of String specifying the of the file paths.
	 */
	public String[] performGlob(String... args) throws AbstractApplicationException, ShellException;
	
	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution. If no back quotes are found, the argsArray from the
	 * input is returned with its quote removed. If back quotes are found, the 
	 * back quotes and its enclosed commands substituted with the output from 
	 * processing the commands enclosed in the back quotes with the back quotes
	 * and any other quotes removed.
	 * @param args		String array of the individual arguments.
	 */
	public String[] performCmdSub(String... args) throws AbstractApplicationException, ShellException;
	
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