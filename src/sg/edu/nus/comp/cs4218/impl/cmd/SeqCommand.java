package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

/**
 * A Sequence Command is a semicolon operator consisting of commands
 * 
 * <p>
 * <b>Command format:</b> <code> <seq> ::= <command> ";" <command></code>
 * </p>
 */

public class SeqCommand implements Command {
	private static final String EXP_INVALID_SEQ = "Invalid semicolon operator/s";

	private final ShellImpl shell;
	private final String cmdline;

	private String[] argsArray;

	public SeqCommand(ShellImpl shellImpl, String cmdline) {
		shell = shellImpl;
		this.cmdline = cmdline.trim();
	}

	/**
	 * Evaluates parts of the sequence-command separated by semicolon operator. The first part will be executed first followed by the subsequent commands
	 * preceding it.
	 * 
	 * @param stdin
	 *            InputStream to get data from.
	 * @param stdout
	 *            OutputStream to write resultant data to.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while evaluating the sub-command of sequence commands.
	 * @throws ShellException
	 *             If an exception happens while evaluating the sub-command of sequence commands.
	 */
	@Override
	public void evaluate(InputStream stdin, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		if (argsArray.length == 0) {
			return;
		}

		for (int i = 0; i < argsArray.length; i++) {
			try {
				String args = argsArray[i];
				PipeCommand pipeCmd = new PipeCommand(shell, args);
				pipeCmd.parse();
				pipeCmd.evaluate(stdin, stdout);
			} catch (AbstractApplicationException | ShellException e) {
				try {
					stdout.write((e.getMessage()).getBytes());
				} catch (IOException e1) {
					throw new ShellException("IO Exception");
				}
			}
		}
	}

	/**
	 * Parses and splits the commands to the call command into its different
	 * components by semicolon operator
	 * 
	 * @throws ShellException
	 *             If an exception happens while parsing the sub-command, or if
	 *             the input redirection file path is same as that of the output
	 *             redirection file path.
	 */
	public void parse() throws ShellException {
		Integer[] spaceIndices = shell.getIndicesOfCharNotInQuote(cmdline, ';');
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
				throw new ShellException(EXP_INVALID_SEQ);
			}
			cmdArgs.add(callCmd);
			startIndex = spaceIndices[i] + 1;
		}
		if (startIndex >= cmdline.length()) {
			throw new ShellException(EXP_INVALID_SEQ);
		}
		if (startIndex < cmdline.length()) {
			String callCmd = cmdline.substring(startIndex, cmdline.length());
			if (callCmd.matches("\\s*")) {
				throw new ShellException(EXP_INVALID_SEQ);
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
