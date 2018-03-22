package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
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
	
	private final Shell shell;
	private final CommandString cmdline;
	private CommandString[] argsArray;

	public PipeCommand(Shell shell, CommandString cmdline) {
		this.shell = shell;
		this.cmdline = cmdline.trim();
		argsArray = new CommandString[0];
	}

	/**
	 * Evaluates the separated commands by pipe and pipe the output of the preceding sub command
	 * to the input of the current sub command. If an exception occurs on a sub command, any sub 
	 * commands after it will not be processed.
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
		CommandString command = argsArray[0];
		CallCommand callCommand = new CallCommand(shell, command);
		callCommand.parse();
		callCommand.evaluate(inputStream, outputStream);

		for (int i = 1; i < argsArray.length; i++) {
			inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			outputStream = new ByteArrayOutputStream();
			
			callCommand = new CallCommand(shell, argsArray[i]);
			callCommand.parse();
			callCommand.evaluate(inputStream, outputStream);
		}

		StreamUtil.writeToStdout(outputStream, stdout);
		return;
	}

	/**
	 * Parses and splits the commands separated by unescaped pipe operator.
	 * 
	 * @throws ShellException
	 *             If the command starts or ends with a pipe or
	 *             if there are no command between pipes.
	 */
	public void parse() throws ShellException {
		Integer[] spaceIndices = cmdline.getIndicesOfCharNotEscaped('|');
		if (spaceIndices.length == 0) {
			argsArray = new CommandString[] {cmdline};
			return;
		}
		
		Arrays.sort(spaceIndices);
		Vector<CommandString> cmdArgs = new Vector<CommandString>();
		int startIndex = 0;
		for (int i = 0; i < spaceIndices.length; i++) {
			CommandString callCmd = cmdline.subCmdString(startIndex, spaceIndices[i]);
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
			CommandString callCmd = cmdline.subCmdString(startIndex, cmdline.length());
			if (callCmd.matches("\\s*")) {
				throw new ShellException(EXP_INVALID_PIPE);
			}
			cmdArgs.add(callCmd);
		}
		
		argsArray = cmdArgs.toArray(new CommandString[cmdArgs.size()]);
	}

	/**
	 * Terminates current execution of the command (unused for now)
	 */
	@Override
	public void terminate() {
		//unused for now
	}
}
