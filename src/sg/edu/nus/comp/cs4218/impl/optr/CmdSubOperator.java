package sg.edu.nus.comp.cs4218.impl.optr;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Operator;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

/**
 * A Command Substitution is a call-command surrounded by backquotes (`) if the backquotes are not surrounded by 
 * single quotes (if any).
 **/
public class CmdSubOperator implements Operator {
	final private ShellImpl shell;
	
	public CmdSubOperator(ShellImpl shellImpl) {
		shell = shellImpl;
	}
	
	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution. If no back quotes are found, the argsArray from the
	 * input is returned with its quote removed. If back quotes are found, the 
	 * back quotes and its enclosed commands substituted with the output from 
	 * processing the commands enclosed in the back quotes with the back quotes
	 * and any other quotes removed.
	 * 
	 * @param argsArray
	 *            	String array of the individual arguments.
	 * 
	 * @return String array 
	 * 				List of string with the back quotes command processed and
	 * 				quotes removed.
	 * 
	 * @throws AbstractApplicationException
	 *             	If an exception happens while processing the application in the
	 *             	back quotes.
	 * @throws ShellException
	 *             	If an exception happens while processing the content in the
	 *             	back quotes.
	 */
	public String[] evaluate(String... argsArray) throws AbstractApplicationException, ShellException {
		Vector<String> results = new Vector<String>();
		for (int i = 0; i < argsArray.length; i++) {
			Integer[] bqIndices = shell.getIndicesOfCharNotInQuotes(argsArray[i], '`');
			if (bqIndices.length == 0 || bqIndices.length % 2 != 0) {
				// no command sub present
				results.add((shell.removeQuotes(argsArray[i]))[0]);
				continue;
			}
			
			HashSet<Integer> removeIndices = new HashSet<Integer>();
			removeIndices.addAll(Arrays.asList(shell.getIndicesOfCharNotInQuotes(argsArray[i], '"')));
			removeIndices.addAll(Arrays.asList(shell.getIndicesOfCharNotInQuotes(argsArray[i], '\'')));
			Arrays.sort(bqIndices);
			
			StringJoiner cmdSubResult = new StringJoiner("");
			StringJoiner cmdSubCmd = new StringJoiner("");
			int bqIndex = 0;
			for (int j = 0; j < argsArray[i].length(); j++) {
				if (removeIndices.contains(j) || j == bqIndices[bqIndex]) {
					//removed quotes
					continue;
				}

				if  (j > bqIndices[bqIndex] && j < bqIndices[bqIndex + 1]) {
					//command sub characters
					cmdSubCmd.add(argsArray[0].substring(j, j + 1));
					continue;
				}
				
				if (j == bqIndices[bqIndex + 1]) {
					//end of command sub
					cmdSubResult.add(performCmdSub(cmdSubCmd.toString()));
					continue;
				}
				
				//other characters
				cmdSubResult.add(argsArray[0].substring(j, j + 1));
			}
			results.add(cmdSubResult.toString());
		}
		return results.toArray(new String[results.size()]);		
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
	private String performCmdSub(String cmd) throws ShellException {
		ByteArrayOutputStream bqOutputStream = new ByteArrayOutputStream();
		ShellImpl shell = new ShellImpl();
		try {
			shell.parseAndEvaluate(cmd, bqOutputStream);
		} catch (AbstractApplicationException e) {
			throw new ShellException(e.getMessage());
		}

		byte[] byteArray = bqOutputStream.toByteArray();
		return new String(byteArray).replace("\n", " ").replace("\r", " ").trim();
	}
}
