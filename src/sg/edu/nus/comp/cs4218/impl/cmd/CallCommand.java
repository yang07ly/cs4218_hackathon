package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.commons.StreamUtil;

/**
 * A Call Command is a sub-command consisting of at least one non-keyword and
 * quoted (if any).
 * 
 * <p>
 * <b>Command format:</b> <code>(&lt;non-Keyword&gt; | &lt;quoted&gt;)*</code>
 * </p>
 */

public class CallCommand implements Command {
	private final ShellImpl shell;
	private final String cmdline;
	
	private String app;
	private String[] argsArray;

	public CallCommand(ShellImpl shellImpl, String cmdline) {
		shell = shellImpl;
		this.cmdline = cmdline.trim();
		
		app = "";
		argsArray = new String[0];
	}

	/**
	 * Evaluates sub-command using data provided through stdin stream. Writes
	 * result to stdout stream.
	 * 
	 * @param stdin
	 *            InputStream to get data from.
	 * @param stdout
	 *            OutputStream to write resultant data to.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while evaluating the application.
	 * @throws ShellException
	 *             If an exception happens while evaluating globing, IO redirection
	 *             quoting or command substitution.
	 */
	@Override
	public void evaluate(InputStream stdin, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		InputStream inputStream;
		OutputStream outputStream;
		
		//perform globbing
		argsArray = shell.performGlob(argsArray);
		
		//perform command substitution
		argsArray = shell.performCmdSub(argsArray);
		
		//extract IO Redirection
		inputStream = shell.getInputStream(argsArray);
		if (inputStream == null) {// empty
			inputStream = stdin;
		}
		outputStream = shell.getOutputStream(argsArray);
		if (outputStream == null) { // empty
			outputStream = stdout;
		}
		argsArray = shell.removeIOStreamFromArgs(argsArray);
		
		shell.runApp(app, argsArray, inputStream, outputStream);
		StreamUtil.closeInputStream(inputStream);
		StreamUtil.closeOutputStream(outputStream);
	}

	/**
	 * Parses and splits the sub-command to the call command into its different
	 * components, namely the application name and the arguments (if any).
	 * 
	 * @throws ShellException
	 *             If an exception happens while parsing the sub-command where
	 *             the quotes are not closed properly.
	 */
	public void parse() throws ShellException {
		Integer[] spaceIndices = shell.getIndicesOfCharNotInQuotes(cmdline, ' ');
		if (spaceIndices.length == 0) {
			app = cmdline;
			return;
		}
		
		Arrays.sort(spaceIndices);
		app = cmdline.substring(0, spaceIndices[0]);
		
		Vector<String> cmdArgs = new Vector<String>();
		int startIndex = spaceIndices[0] + 1;
		for (int i = 1; i < spaceIndices.length; i++) {
			if (startIndex != spaceIndices[i]) {
				cmdArgs.add(cmdline.substring(startIndex, spaceIndices[i]));
			}
			startIndex = spaceIndices[i] + 1;
		}
		if (startIndex < cmdline.length()) {
			cmdArgs.add(cmdline.substring(startIndex, cmdline.length()));
		}
		
		argsArray = cmdArgs.toArray(new String[cmdArgs.size()]);
	}

	/**
	 * Terminates current execution of the command (unused for now)
	 */
	@Override
	public void terminate() {
		//not used
	}
}
