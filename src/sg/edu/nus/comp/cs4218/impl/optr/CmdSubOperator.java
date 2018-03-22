package sg.edu.nus.comp.cs4218.impl.optr;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import sg.edu.nus.comp.cs4218.Operator;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

/**
 * A Command Substitution is a call-command surrounded by backquotes (`) if the backquotes are not surrounded by 
 * single quotes (if any).
 **/
public class CmdSubOperator implements Operator {
	private final Shell shell;
	
	public CmdSubOperator(Shell shell) {
		this.shell = shell;
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
	public void evaluate(CommandString cmd) throws AbstractApplicationException, ShellException {
		if (cmd == null) {
			throw new ShellException("Null Pointer Exception");
		}
		
		Integer[] bqIndices = cmd.getIndicesOfCharNotEscaped('`');
		if (bqIndices.length == 0) {
			// no command sub present
			return;
		}
		
		if (bqIndices.length % 2 != 0) {
			throw new ShellException("Back Quotes not closed");
		}

		Arrays.sort(bqIndices);
		for (int i = bqIndices.length - 2; i >= 0; i-=2) {
			if (bqIndices[i] + 1 == bqIndices[i+1]) {
				cmd.removeRange(bqIndices[i], bqIndices[i+1] + 1);
				continue;
			}
			String cmdSubCmd = cmd.substring(bqIndices[i] + 1, bqIndices[i+1]);
			String cmdSubResult = performCmdSub(cmdSubCmd);
			cmd.replaceRange(bqIndices[i], bqIndices[i+1] + 1, cmdSubResult);
		}
	}
	
	/**
	 * Returns the result of processing the command specified in a single line.
	 * 
	 * @param cmd
	 *            	String of the specified command.
	 * 
	 * @return String
	 * 				Result of processing the command appended into a single line.
	 * 
	 * @throws AbstractApplicationException
	 *             	If an exception happens while processing the application in the
	 *             	back quotes.
	 * @throws ShellException
	 *             	If an exception happens while processing the content in the
	 *             	back quotes.
	 */
	private String performCmdSub(String cmd) throws AbstractApplicationException, ShellException {
		ByteArrayOutputStream bqOutputStream = new ByteArrayOutputStream();
		Shell newShell = shell.newInstance();
		newShell.parseAndEvaluate(cmd, bqOutputStream);

		byte[] byteArray = bqOutputStream.toByteArray();
		return new String(byteArray).replace("\n", " ").replace("\r", " ").trim();
	}
}
