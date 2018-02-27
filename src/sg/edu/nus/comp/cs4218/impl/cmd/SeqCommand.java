package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

/**
 * A Sequence Command is a semicolon operator consisting of commands
 * 
 * <p>
 * <b>Command format:</b> <code> <seq> ::= <command> “;” <command></code>
 * </p>
 */

public class SeqCommand implements Command {
	public static final String EXP_INVALID_SEQ = "Invalid semicolon operator/s";
	public static final String EXP_SYNTAX = "Invalid syntax encountered.";
	public static final String EXP_REDIR_PIPE = "File output redirection and pipe "
			+ "operator cannot be used side by side.";
	public static final String EXP_SAME_REDIR = "Input redirection file same as "
			+ "output redirection file.";
	public static final String EXP_STDOUT = "Error writing to stdout.";
	public static final String EXP_NOT_SUPPORTED = " not supported yet";
	
	public static final char CHAR_BQ = '`';
	public static final char CHAR_DQ = '"';
	public static final char CHAR_SQ = '\'';
	public static final char SEQ_OPERATOR = ';';
	public static final String PIPE_OPERATOR = "|";

	String app;
	String cmdline, inputStreamS, outputStreamS;
	Vector<String> argsArray;
	Boolean error;
	String errorMsg;

	public SeqCommand(String cmdline) {
		this.cmdline = cmdline.trim();
		app = inputStreamS = outputStreamS = "";
		error = false;
		errorMsg = "";
		argsArray = new Vector<String>();
	}

	public SeqCommand() {
		this("");
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
		if (error) {
			throw new ShellException(errorMsg);
		}
		for (int i = 0; i < argsArray.size(); i++) {
			String args = argsArray.get(i);
			if (args.contains(PIPE_OPERATOR)) {
				PipeCommand pipeCommand = new PipeCommand(args);
				pipeCommand.parse();
				pipeCommand.evaluate(stdin, stdout);
			} else {
				CallCommand callCommand = new CallCommand(args);
				callCommand.parse();
				callCommand.evaluate(stdin, stdout);
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
		int sizeBQ = 0;
		int sizeDQ = 0;
		int sizeSQ = 0;
		int index = 0;
		
		if (cmdline.length() == 0) {
			return;
		}
		
		if (cmdline.charAt(0) == SEQ_OPERATOR || cmdline.charAt(cmdline.length() - 1) == SEQ_OPERATOR) {
			throw new ShellException(EXP_INVALID_SEQ);
		}
		
		for (int i = 0; i < cmdline.length(); i++) {
			if (cmdline.charAt(i) == CHAR_BQ) {
				sizeBQ++;	
			} else if (cmdline.charAt(i) == CHAR_DQ) {
				sizeDQ++;
			} else if (cmdline.charAt(i) == CHAR_SQ) {
				sizeSQ++;
			} else if (cmdline.charAt(i) == SEQ_OPERATOR && sizeBQ % 2 == 0) {
				String command = cmdline.substring(index, i);
				argsArray.add(command);
				index = i + 1;
			} 
			
			if (i == cmdline.length() - 1 && (sizeSQ % 2 == 0 || sizeDQ % 2 == 0 || sizeBQ % 2 == 0)) {
				argsArray.add(cmdline.substring(index, i + 1));
				break;
			}
			
		}
	}
	
	/**
	 * Terminates current execution of the command (unused for now)
	 */
	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}
}
