package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

/**
 * A Sequence Command is a semicolon operator consisting of commands
 * 
 * <p>
 * <b>Command format:</b> <code> <seq> ::= <command> ";" <command></code>
 * </p>
 */

public class SeqCommand implements Command {
	private static final String EXP_INVALID_SEQ = "Invalid semicolon operator/s";

	private final Shell shell;
	private final CommandString cmdline;

	private CommandString[] argsArray;

	public SeqCommand(Shell shell, CommandString cmdline) {
		this.shell = shell;
		this.cmdline = cmdline.trim();
		argsArray = new CommandString[0];
	}

	/**
	 * Evaluates the separated commands by semicolon sequentially. If an exception
	 * occurs on a sub command, any sub commands after it will not be processed.
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
			PipeCommand pipeCmd = new PipeCommand(shell, argsArray[i]);
			pipeCmd.parse();
			pipeCmd.evaluate(stdin, stdout);

			if (i < argsArray.length - 1) {
				try {
					stdout.write("\n".getBytes());
				} catch (IOException e1) {
					throw new ShellException("IO Exception");
				}
			}
		}
	}

	/**
	 * Parses and splits the commands separated by unescaped semicolon.
	 * 
	 * @throws ShellException
	 *             If the command starts with a semicolon or
	 *             if there are no command between semicolons.
	 */
	public void parse() throws ShellException {
		Integer[] spaceIndices = cmdline.getIndicesOfCharNotEscaped(';');
		if (spaceIndices.length == 0) {
			argsArray = new CommandString[] {cmdline};
			return;
		}

		Arrays.sort(spaceIndices);
		Vector<CommandString> cmdArgs = new Vector<CommandString>();
		int startIndex = 0;
		for (int i = 0; i < spaceIndices.length; i++) {
			CommandString pipeCmd = cmdline.substring(startIndex, spaceIndices[i]);
			if (pipeCmd.trim().length() == 0) {
				throw new ShellException(EXP_INVALID_SEQ);
			}
			cmdArgs.add(pipeCmd);
			startIndex = spaceIndices[i] + 1;
		}
		if (startIndex < cmdline.length()) {
			CommandString pipeCmd = cmdline.substring(startIndex, cmdline.length());
			if (pipeCmd.trim().length() != 0) {
				cmdArgs.add(pipeCmd);
			}
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
