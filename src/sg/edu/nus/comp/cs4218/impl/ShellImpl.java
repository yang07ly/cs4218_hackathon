package sg.edu.nus.comp.cs4218.impl;

import java.io.*;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.app.CatApplication;
import sg.edu.nus.comp.cs4218.impl.app.CdApplication;
import sg.edu.nus.comp.cs4218.impl.app.CmpApplication;
import sg.edu.nus.comp.cs4218.impl.app.DiffApplication;
import sg.edu.nus.comp.cs4218.impl.app.EchoApplication;
import sg.edu.nus.comp.cs4218.impl.app.ExitApplication;
import sg.edu.nus.comp.cs4218.impl.app.GrepApplication;
import sg.edu.nus.comp.cs4218.impl.app.LsApplication;
import sg.edu.nus.comp.cs4218.impl.app.MkdirApplication;
import sg.edu.nus.comp.cs4218.impl.app.PasteApplication;
import sg.edu.nus.comp.cs4218.impl.app.SedApplication;
import sg.edu.nus.comp.cs4218.impl.app.SplitApplication;
import sg.edu.nus.comp.cs4218.impl.cmd.SeqCommand;
import sg.edu.nus.comp.cs4218.impl.optr.CmdSubOperator;
import sg.edu.nus.comp.cs4218.impl.optr.GlobOperator;
import sg.edu.nus.comp.cs4218.impl.optr.IoRedirOperator;
import sg.edu.nus.comp.cs4218.impl.optr.QuoteOperator;

/**
 * A Shell is a command interpreter and forms the backbone of the entire
 * program. Its responsibility is to interpret commands that the user type and
 * to run programs that the user specify in her command lines.
 * 
 * <p>
 * <b>Command format:</b>
 * <code>&lt;Pipe&gt; | &lt;Sequence&gt; | &lt;Call&gt;</code>
 * </p>
 */

public class ShellImpl {
	
	CmdSubOperator cmdSubOptr;
	GlobOperator globOptr;
	IoRedirOperator ioRedirOptr;
	QuoteOperator quoteOptr;
	
	public ShellImpl() {
		cmdSubOptr = new CmdSubOperator(this);
		globOptr = new GlobOperator(this);
		ioRedirOptr = new IoRedirOperator();
		quoteOptr = new QuoteOperator();
	}
	
	/**
	 * Static method to run the application as specified by the application
	 * command keyword and arguments.
	 * 
	 * @param app
	 *            String containing the keyword that specifies what application
	 *            to run.
	 * @param args
	 *            String array containing the arguments to pass to the
	 *            applications for running.
	 * @param inputStream
	 *            InputputStream for the application to get arguments from, if
	 *            needed.
	 * @param outputStream
	 *            OutputStream for the application to print its output to.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while running any of the
	 *             application(s).
	 * @throws ShellException
	 *             If an unsupported or invalid application command is detected.
	 */
	public void runApp(String app, String[] argsArray,
			InputStream inputStream, OutputStream outputStream)
			throws AbstractApplicationException, ShellException {
		Application absApp = null;
		if (("cat").equals(app)) {// cat [FILE]...
			absApp = new CatApplication();
		} else if (("echo").equals(app)) {// echo [args]...
			absApp = new EchoApplication();
		} else if (("ls").equals(app)) {// ls [-d][FOLDER][-R]
			absApp = new LsApplication();
		} else if (("cd").equals(app)) {// cd PATH
			absApp = new CdApplication();
		} else if (("sed").equals(app)) {// sed REPLACEMENT [FILE]
			absApp = new SedApplication();
		} else if (("exit").equals(app)) { // exit
			absApp = new ExitApplication();
		} else if (("cmp").equals(app)) { // cmp Options... FILE
			absApp = new CmpApplication();
		} else if (("split").equals(app)) { // split [Options] [FILE [PREFIX]]
			absApp = new SplitApplication();
		} else if (("mkdir").equals(app)) { // mkdir FOLDERS
			absApp = new MkdirApplication();
		} else if (("grep").equals(app)) { // grep [-v] PATTERN [FILE]...
			absApp = new GrepApplication();
		} else if (("paste").equals(app)) { // paste [FILE]...
			absApp = new PasteApplication();
		} else if (("diff").equals(app)) { // diff [Options] FILES...
			absApp = new DiffApplication();
		} else { // invalid command
			throw new ShellException(app + ": Invalid app.");
		}
		absApp.run(argsArray, inputStream, outputStream);
	}
	
	/**
	 * Parses and evaluates user's command line.
	 * 
	 * @param cmdline
	 *            String of the user inputed command.
	 * @param outputStream
	 *            OutputStream for the application to print its output to.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while running any of the
	 *             application(s).
	 * @throws ShellException
	 *             If an unsupported or invalid command is detected.
	 */	
	public void parseAndEvaluate(String cmdline, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		SeqCommand seqCmd = new SeqCommand(this, cmdline);
		seqCmd.parse();
		seqCmd.evaluate(System.in, stdout);
	}
	
	/**
	 * Returns all indices of the specified character that is not within
	 * any quotes.
	 * 
	 * @param source
	 * 			  	String used to check for the specified character.
	 * @param sepChar
	 * 			  	Character to find in the string.
	 * @return Integer Array
	 * 			  	indices of the specfied character not within quotes.
	 * 
	 * @throws ShellException
	 *            	If the quotes are not closed.
	 */
	public Integer[] getIndicesOfCharNotInQuotes(String source, char character) throws ShellException {
		return quoteOptr.getIndices(source, character);
	}
	
	/**
	 * Returns the the list of string with quotes removed.
	 * 
	 * @param cmdArgs
	 * 			  	String Array containing the string to have its 
	 * 				quotes removed.
	 * @return String Array
	 * 			  	list of string with its quotes removed.
	 * 
	 * @throws ShellException
	 *            	If the quotes are not closed.
	 */
	public String[] removeQuotes(String... source) throws AbstractApplicationException, ShellException {
		return quoteOptr.evaluate(source);
	}
	
	/**
	 * Returns all the paths to existing files and directories such that these 
	 * paths can be obtained by replacing all the unquoted asterisk symbols in 
	 * specified path by some (possibly empty) sequences of non-slash characters.
	 * If no such path exist, the specified path is return without changes.
	 * 
	 * @param fileNames
	 * 			  Array of String specifying the of the file paths.
	 * @return String Array
	 * 			  paths that matches the wildcard fileNames.
	 * 
	 * @throws IOException
	 *            If the specified path is null.
	 */
	public String[] performGlob(String... source) throws AbstractApplicationException, ShellException {
		return globOptr.evaluate(source);
	}
	
	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution. If no back quotes are found, the argsArray from the
	 * input is returned with its quote removed. If back quotes are found, the 
	 * back quotes and its enclosed commands substituted with the output from 
	 * processing the commands enclosed in the back quotes with the back quotes
	 * and any other quotes removed.
	 * 
	 * @param argsArray
	 *            	String array of the individual arguments.
	 * 
	 * @return String array 
	 * 				List of string with the back quotes command processed and
	 * 				quotes removed.
	 * 
	 * @throws AbstractApplicationException
	 *             	If an exception happens while processing the application in the
	 *             	back quotes.
	 * @throws ShellException
	 *             	If an exception happens while processing the content in the
	 *             	back quotes.
	 */
	public String[] performCmdSub(String... source) throws AbstractApplicationException, ShellException {
		return cmdSubOptr.evaluate(source);
	}
	
	public String[] removeIOStreamFromArgs(String... source) throws AbstractApplicationException, ShellException {
		return ioRedirOptr.evaluate(source);
	}
	
	public InputStream getInputStream(String... source) throws ShellException {
		return ioRedirOptr.getInputStream(source);
	}
	
	public OutputStream getOutputStream(String... source) throws ShellException {
		return ioRedirOptr.getOutputStream(source);
	}

	/**
	 * Main method for the Shell Interpreter program.
	 * 
	 * @param args
	 *            List of strings arguments, unused.
	 */
	public static void main(String... args) {
		ShellImpl shell = new ShellImpl();

		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				System.in));
		String readLine = null;
		String currentDir;

		while (true) {
			try {
				currentDir = Environment.currentDirectory;
				System.out.print(currentDir + ">");
				readLine = bReader.readLine();
				if (readLine == null) {
					break;
				}
				if (("").equals(readLine)) {
					continue;
				}
				shell.parseAndEvaluate(readLine, System.out);
				System.out.println();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
