package sg.edu.nus.comp.cs4218.impl.optr;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

/**
 * A Command Substitution is a call-command surrounded by backquotes (`) iff the backquotes are not surrounded by 
 * single quotes (if any).
 **/
public class CmdSubOperator {
	final private ShellImpl shell;
	
	public CmdSubOperator(ShellImpl shellImpl) {
		shell = shellImpl;
	}
	
	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution.If no back quotes are found, the argsArray from the
	 * input is returned unchanged. If back quotes are found, the back quotes
	 * and its enclosed commands substituted with the output from processing the
	 * commands enclosed in the back quotes.
	 * 
	 * @param argsArray
	 *            String array of the individual commands.
	 * 
	 * @return String array with the back quotes command processed.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while processing the content in the
	 *             back quotes.
	 * @throws ShellException
	 *             If an exception happens while processing the content in the
	 *             back quotes.
	 */
	public String[] evaluate(String... argsArray) throws AbstractApplicationException, ShellException {
		Vector<String> results = new Vector<String>();
		for (int i = 0; i < argsArray.length; i++) {
			Integer[] bqIndices = shell.getIndicesOfCharNotInQuote(argsArray[i], '`');
			if (bqIndices.length == 0 || bqIndices.length % 2 != 0) {
				results.add(argsArray[i]);
				continue;
			}

			Arrays.sort(bqIndices);
			StringJoiner cmdSubResult = new StringJoiner("");
			int startIndex = 0;
			for (int j = 0; j < bqIndices.length; j+=2) {
				cmdSubResult.add(argsArray[i].substring(startIndex, bqIndices[j]));
				if (bqIndices[j] + 1 != bqIndices[j+1]) {
					String cmdSubCmd = argsArray[i].substring(bqIndices[j] + 1, bqIndices[j+1]);
					String cmdSubOut = performCmdSub(cmdSubCmd);
//					if (cmdSubOut.matches(".*[\"'`].*")) {
//						cmdSubResult.add(cmdSubOut.replace("'", "'''").replace("\"", "'\"'").replace("`", "'`'"));
//					} else {
						cmdSubResult.add(cmdSubOut);
//					}
				}
				startIndex = bqIndices[j+1] + 1;
			}
			if (startIndex < argsArray[i].length()) {
				cmdSubResult.add(argsArray[i].substring(startIndex, argsArray[i].length()));
			}
			
			results.add(cmdSubResult.toString());
		}
		return results.toArray(new String[results.size()]);		
	}
	
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
