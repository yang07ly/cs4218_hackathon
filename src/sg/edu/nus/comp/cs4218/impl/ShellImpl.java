package sg.edu.nus.comp.cs4218.impl;

import java.io.*;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Shell;
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
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
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

public class ShellImpl implements Shell {
	
	CmdSubOperator cmdSubOptr;
	GlobOperator globOptr;
	IoRedirOperator ioRedirOptr;
	QuoteOperator quoteOptr;
	
	public ShellImpl() {
		cmdSubOptr = new CmdSubOperator(this);
		globOptr = new GlobOperator();
		ioRedirOptr = new IoRedirOperator(this);
		quoteOptr = new QuoteOperator();
	}
	

	@Override
	public Shell newInstance() {
		return new ShellImpl();
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
	@Override
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
	@Override
	public void parseAndEvaluate(String cmdline, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		CommandString cmd = new CommandString(cmdline.replace("\t", "    "));
		processQuotes(cmd);
		
		SeqCommand seqCmd = new SeqCommand(this, cmd);
		seqCmd.parse();
		seqCmd.evaluate(System.in, stdout);
	}
	
	/**
	 * Remove all unescaped double and single quotes and set all characters in
	 * quotes to escaped characters. Back quotes are not removed.
	 * 
	 * @param cmd
	 * 			  	CommandString containing the string to have its 
	 * 				double and single quotes removed and set escaped 
	 * 				characters.
	 * 
	 * @throws ShellException
	 *            	If the quotes are not closed or the input command is null.
	 */
	@Override
	public void processQuotes(CommandString cmd) throws AbstractApplicationException, ShellException {
		quoteOptr.evaluate(cmd);
	}
	
	/**
	 * Replace paths with wildcard with all the paths to existing files and 
	 * directories such that these paths can be obtained by replacing all the 
	 * unescaped asterisk symbols in specified path by some (possibly empty) 
	 * sequences of non-slash characters. If no such path exist, paths with 
	 * wildcard are not replaced.
	 * 
	 * @param cmd
	 * 			  CommandString containing the paths with wildcard.
	 * 
	 * @throws ShellException
	 *            If the input command is null.
	 */
	@Override
	public void performGlob(CommandString cmd) throws AbstractApplicationException, ShellException {
		globOptr.evaluate(cmd);
	}
	
	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution. The commands enclosed by back quotes will be
	 * replaced by the command substitution results with newline replaced 
	 * with a space. The replaced string are not escaped.
	 * 
	 * @param cmd
	 * 			  	CommandString containing the commands enclosed by back 
	 * 				quotes for command substitution.
	 * 
	 * @throws AbstractApplicationException
	 *             	If an exception happens while processing the application in the
	 *             	back quotes.
	 * @throws ShellException
	 *             	If an exception happens while processing the content in the
	 *             	back quotes.
	 */
	@Override
	public void performCmdSub(CommandString cmd) throws AbstractApplicationException, ShellException {
		cmdSubOptr.evaluate(cmd);
	}
	
	/**
	 * Removes any IO redirection operators and their arguments and returns back the string array
	 * @param args
	 *            	String array of the individual arguments.
	 * @return	String array of arguments without IO redirection operators and their arguments
	 * @throws AbstractApplicationException
	 * @throws ShellException
	 */
	@Override
	public String[] removeIOStreamFromArgs(String... args) throws AbstractApplicationException, ShellException {
		return ioRedirOptr.evaluate(args);
	}
	
	/**
	 * Scans the arguments and sets the input stream
	 * @param args
	 *            	String array of the individual arguments.
	 * @return	the input stream
	 * @throws ShellException
	 * 			if more than 1 input stream is specified
	 * @throws AbstractApplicationException 
	 */
	@Override
	public InputStream getInputStream(CommandString cmd) throws ShellException, AbstractApplicationException {
		return ioRedirOptr.getInputStream(cmd);
	}
	
	/**
	 * Scans the arguments and sets the output stream
	 * @param args 
	 *            	String array of the individual arguments.
	 * @return	the output stream
	 * @throws ShellException
	 * 			if more than 1 output stream is specified
	 * @throws AbstractApplicationException 
	 */
	@Override
	public OutputStream getOutputStream(CommandString cmd) throws ShellException, AbstractApplicationException {
		return ioRedirOptr.getOutputStream(cmd);
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
