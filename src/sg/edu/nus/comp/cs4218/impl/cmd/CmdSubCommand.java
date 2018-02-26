package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class CmdSubCommand implements Command{
	
	public CmdSubCommand() {
		super();
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
	public String[] processBQ(String... argsArray)
			throws AbstractApplicationException, ShellException {
		String[] resultArr = new String[argsArray.length];
		System.arraycopy(argsArray, 0, resultArr, 0, argsArray.length);
		String patternBQ = "`([^\\n`]*)`";
		Pattern patternBQp = Pattern.compile(patternBQ);
		Vector<String> results = new Vector<String>();
		for (int i = 0; i < argsArray.length; i++) {
			Matcher matcherBQ = patternBQp.matcher(argsArray[i]);
			if (matcherBQ.find()) {// found backquoted
				String bqStr = matcherBQ.group(1);
				// process back quote
				OutputStream bqOutputStream = new ByteArrayOutputStream();
				ShellImpl shell = new ShellImpl();
				shell.parseAndEvaluate(bqStr, bqOutputStream);

				ByteArrayOutputStream outByte = (ByteArrayOutputStream) bqOutputStream;
				byte[] byteArray = outByte.toByteArray();
				String bqResult = new String(byteArray).replace("\n", " ")
						.replace("\r", " ");
				String[] parts = bqResult.trim().split("\\s+");
				for (int j = 0; j < parts.length; j++) {
					// replace substring of back quote with result
					String replacedStr = argsArray[i].replace("`" + bqStr + "`",
							parts[j]);
					
					resultArr[i] = replacedStr;
					results.add(replacedStr);
				}
			} else {
				results.add(argsArray[i]);
			}
		}
		return results.toArray(new String[results.size()]);
	}

	@Override
	public void evaluate(InputStream stdin, OutputStream stdout) throws AbstractApplicationException, ShellException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}
	
}
