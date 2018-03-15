package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
 * A Pipe Command is a left-associative operator consisting of call/pipe and call commands
 * 
 * <p>
 * <b>Command format:</b> <code> <pipe> ::= <call> "|" <call> | <pipe> "|" <call></code>
 * </p>
 */

public class PipeCommand implements Command{
	public static final String EXP_INVALID_PIPE = "Invalid pipe operator/s";
	
	private final ShellImpl shell;
	private final String cmdline;
	private String[] argsArray;

	public PipeCommand(ShellImpl shellImpl, String cmdline) {
		shell = shellImpl;
		this.cmdline = cmdline.trim();
		argsArray = new String[0];
	}

	/**
	 * Evaluates parts of the pipe-command separated by pipe operator. The resultant of evaluation which is the output of left part will
	 *  be pass to the input of the right part 
	 * 
	 * @param stdin
	 *            InputStream to get data from.
	 * @param stdout
	 *            OutputStream to write resultant data to.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while evaluating the sub-command of pipe commands.
	 * @throws ShellException
	 *             If an exception happens while evaluating the sub-command of pipe commands.
	 */
	@Override
	public void evaluate(InputStream stdin, OutputStream stdout) throws AbstractApplicationException, ShellException {
		if (argsArray.length == 0) {
			return;
		}
		
		InputStream inputStream = stdin;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String command = argsArray[0];
		CallCommand callCommand = new CallCommand(shell, command);
		callCommand.parse();
		callCommand.evaluate(inputStream, outputStream);

		for (int i = 1; i < argsArray.length; i++) {
			if (outputStream.size() > 0) {
				byte[] temp = Arrays.copyOfRange(outputStream.toByteArray(), 0, outputStream.size() - 1);
				inputStream = new ByteArrayInputStream(temp);
			} else {
				inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			}
			outputStream = new ByteArrayOutputStream();
			
			command = argsArray[i];
			callCommand = new CallCommand(shell, command);
			callCommand.parse();
			callCommand.evaluate(inputStream, outputStream);
		}

		StreamUtil.writeToStdout(outputStream, stdout);
		return;
	}

	/**
	 * Parses and splits the pipe-command to the call/pipe command into its different
	 * components, separated by pipe operator.
	 * 
	 * @throws ShellException
	 *             If an exception happens while parsing the pipe-command, or if
	 *             the input redirection file path is same as that of the output
	 *             redirection file path.
	 */
	public void parse() throws ShellException {
		Integer[] spaceIndices = shell.getIndicesOfCharNotInQuotes(cmdline, '|');
		if (spaceIndices.length == 0) {
			argsArray = new String[] {cmdline};
			return;
		}
		
		Arrays.sort(spaceIndices);
		Vector<String> cmdArgs = new Vector<String>();
		int startIndex = 0;
		for (int i = 0; i < spaceIndices.length; i++) {
			String callCmd = cmdline.substring(startIndex, spaceIndices[i]);
			if (callCmd.matches("\\s*")) {
				throw new ShellException(EXP_INVALID_PIPE);
			}
			cmdArgs.add(callCmd);
			startIndex = spaceIndices[i] + 1;
		}
		if (startIndex >= cmdline.length()) {
			throw new ShellException(EXP_INVALID_PIPE);
		}
		if (startIndex < cmdline.length()) {
			String callCmd = cmdline.substring(startIndex, cmdline.length());
			if (callCmd.matches("\\s*")) {
				throw new ShellException(EXP_INVALID_PIPE);
			}
			cmdArgs.add(callCmd);
		}
		
		argsArray = cmdArgs.toArray(new String[cmdArgs.size()]);
	}

	/**
	 * Terminates current execution of the command (unused for now)
	 */
	@Override
	public void terminate() {
		// not used
	}
}
