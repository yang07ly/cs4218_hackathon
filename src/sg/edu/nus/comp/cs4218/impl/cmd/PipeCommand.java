package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

/**
 * A Pipe Command is a left-associative operator consisting of call/pipe and call commands
 * 
 * <p>
 * <b>Command format:</b> <code> <pipe> ::= <call> “|” <call> | <pipe> “|” <call></code>
 * </p>
 */

public class PipeCommand implements Command{
	public static final String EXP_INVALID_PIPE = "Invalid pipe operator/s";
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
	public static final char PIPE_OPERATOR = '|';

	String app;
	String cmdline, inputStreamS, outputStreamS;
	Vector<String> argsArray;
	Boolean error;
	String errorMsg;

	public PipeCommand(String cmdline) {
		this.cmdline = cmdline.trim();
		app = inputStreamS = outputStreamS = "";
		error = false;
		errorMsg = "";
		argsArray = new Vector<String>();
	}

	public PipeCommand() {
		this("");
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
		InputStream inputStream = stdin;
		OutputStream outputStream = stdout;
		InputStream inputStreamTemp = null;


		ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
 
		if (argsArray.size() == 1) {
			String command = argsArray.get(0);
			CallCommand callCommand = new CallCommand(command);
			callCommand.parse();
			callCommand.evaluate(inputStream, outputStream);
			
			ShellImpl.writeToStdout(byteArrOutStream, stdout);
			ShellImpl.outputStreamToInputStream(byteArrOutStream);
			return;
		}
		 
		for (int i = 0; i < argsArray.size(); i++) {
			String command = argsArray.get(i);
			CallCommand callCommand = new CallCommand(command);
			inputStreamTemp = new ByteArrayInputStream(byteArrOutStream.toByteArray());
			byteArrOutStream = new ByteArrayOutputStream();	
			callCommand.parse();
			callCommand.evaluate(inputStreamTemp, byteArrOutStream);
		}
 
		ShellImpl.writeToStdout(byteArrOutStream, stdout);
		ShellImpl.outputStreamToInputStream(byteArrOutStream);
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
		
		int sizeBQ = 0;
		int sizeDQ = 0;
		int sizeSQ = 0;
		int index = 0;
		
		if (cmdline.length() == 0) {
			return;
		}
		
		if (cmdline.charAt(0) == PIPE_OPERATOR || cmdline.charAt(cmdline.length() - 1) == PIPE_OPERATOR) {
			throw new ShellException(EXP_INVALID_PIPE);
		}
		
		for (int i = 0; i < cmdline.length(); i++) {
			if (cmdline.charAt(i) == CHAR_BQ) {
				sizeBQ++;	
			} else if (cmdline.charAt(i) == CHAR_DQ) {
				sizeDQ++;
			} else if (cmdline.charAt(i) == CHAR_SQ) {
				sizeSQ++;
			} else if (cmdline.charAt(i) == PIPE_OPERATOR && sizeBQ % 2 == 0) {
				argsArray.add(cmdline.substring(index, i));
				index = i + 1;
			} 
			if ((i == cmdline.length() - 1) && (sizeSQ % 2 == 0 || sizeDQ % 2 == 0 || sizeBQ % 2 == 0)) {
				 {
					argsArray.add(cmdline.substring(index, i + 1));
					break;
				}
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
