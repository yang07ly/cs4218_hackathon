package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
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
	private final Shell shell;
	private final CommandString cmdline;
	
	private String app;
	private String[] argsArray;
	private InputStream inputStream;
	private OutputStream outputStream;

	public CallCommand(Shell shell, CommandString cmdline) {
		this.shell = shell;
		this.cmdline = cmdline.trim();
		
		app = "";
		argsArray = new String[0];
		inputStream = null;
		outputStream = null;
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
		if (inputStream == null) { // empty
			inputStream = stdin;
		}
		if (outputStream == null) { // empty
			outputStream = stdout;
		}		
		shell.runApp(app, argsArray, inputStream, outputStream);
		StreamUtil.closeInputStream(stdin);
		StreamUtil.closeOutputStream(stdout);
	}

	/**
	 * Parses and splits the sub-command to the call command into its different
	 * components, namely the application name and the arguments (if any).
	 * 
	 * @throws ShellException
	 *             If an exception happens while parsing the sub-command where
	 *             the quotes are not closed properly.
	 */
	public void parse() throws AbstractApplicationException, ShellException {
		//remove IO args from cmdline. Cmdsub and glob have to be done within IORedir.
		inputStream = shell.getInputStream(cmdline);
		outputStream = shell.getOutputStream(cmdline);
		shell.performCmdSub(cmdline);
		shell.performGlob(cmdline);
		extractArgs();
	}
	
	/**
	 * Parses the sub-command's arguments to the call command and splits it into
	 * its different components, namely the application name and the arguments
	 * (if any) separated by an unescaped space. All operations that manipulates
	 * the arguments, such as removing quotes, extracting IO redirection, command 
	 * subtitution and globbing, are assumed to be processed.
	 */
	private void extractArgs() {
		Integer[] sepIndices = cmdline.getIndicesOfCharNotEscaped(' ');
		if (sepIndices.length == 0) {
			app = cmdline.toString();
			return;
		}
		app = cmdline.substring(0, sepIndices[0]).toString();
		
		Vector<String> cmdArgs = new Vector<String>();
		int startIndex = sepIndices[0] + 1;
		for (int i = 1; i < sepIndices.length; i++) {
			if (startIndex != sepIndices[i]) {
				cmdArgs.add(cmdline.substring(startIndex, sepIndices[i]).toString());
			}
			startIndex = sepIndices[i] + 1;
		}
		if (startIndex < cmdline.length()) {
			cmdArgs.add(cmdline.substring(startIndex, cmdline.length()).toString());
		}
		argsArray = cmdArgs.toArray(new String[cmdArgs.size()]);
	}

	/**
	 * Terminates current execution of the command (unused for now)
	 */
	@Override
	public void terminate() {
		//unused for now
	}
}
