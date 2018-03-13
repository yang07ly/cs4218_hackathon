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

	public void parseAndEvaluate(String cmdline, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		SeqCommand seqCmd = new SeqCommand(this, cmdline);
		seqCmd.parse();
		seqCmd.evaluate(System.in, stdout);
	}
	
	public Integer[] getIndicesOfCharNotInQuote(String source, char character) throws ShellException {
		return quoteOptr.getIndices(source, character);
	}
	
	public String[] removeQuote(String... source) throws AbstractApplicationException, ShellException {
		return quoteOptr.evaluate(source);
	}
	
	public String[] performGlob(String... source) throws AbstractApplicationException, ShellException {
		return globOptr.evaluate(source);
	}
	
	public String[] performCmdSub(String... source) throws AbstractApplicationException, ShellException {
		return cmdSubOptr.evaluate(source);
	}
	
	public String[] removeStreamFromArgs(String... source) throws AbstractApplicationException, ShellException {
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
