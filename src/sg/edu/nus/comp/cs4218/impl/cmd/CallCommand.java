package sg.edu.nus.comp.cs4218.impl.cmd;

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
 * A Call Command is a sub-command consisting of at least one non-keyword and
 * quoted (if any).
 * 
 * <p>
 * <b>Command format:</b> <code>(&lt;non-Keyword&gt; | &lt;quoted&gt;)*</code>
 * </p>
 */

public class CallCommand implements Command {
	private final ShellImpl shell;
	private final String cmdline;
	
	private String app;
	private String[] argsArray;

	public CallCommand(ShellImpl shellImpl, String cmdline) {
		shell = shellImpl;
		this.cmdline = cmdline.trim();
		
		app = "";
		argsArray = new String[0];
	}

	/**
	 * Evaluates sub-command using data provided through stdin stream. Writes
	 * result to stdout stream.
	 * 
	 * @param stdin
	 *            InputStream to get data from.
	 * @param stdout
	 *            OutputStream to write resultant data to.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while evaluating the sub-command.
	 * @throws ShellException
	 *             If an exception happens while evaluating the sub-command.
	 */
	@Override
	public void evaluate(InputStream stdin, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		InputStream inputStream;
		OutputStream outputStream;
		
		//perform globbing
		argsArray = shell.performGlob(argsArray);
		
		//perform command substitution
		argsArray = shell.performCmdSub(argsArray);
		
		//extract IO Redirection
		argsArray = shell.removeStreamFromArgs(argsArray);
		inputStream = shell.getInputStream(argsArray);
		if (inputStream == null) {// empty
			inputStream = stdin;
		}
		outputStream = shell.getOutputStream(argsArray);
		if (outputStream == null) { // empty
			outputStream = stdout;
		}
		
		//remove quotes from evaluated arguments
		argsArray = shell.removeQuote(argsArray);
		
		shell.runApp(app, argsArray, inputStream, outputStream);
		StreamUtil.closeInputStream(inputStream);
		StreamUtil.closeOutputStream(outputStream);
	}

	/**
	 * Parses and splits the sub-command to the call command into its different
	 * components, namely the application name, the arguments (if any), the
	 * input redirection file path (if any) and output redirection file path (if
	 * any).
	 * 
	 * @throws ShellException
	 *             If an exception happens while parsing the sub-command, or if
	 *             the input redirection file path is same as that of the output
	 *             redirection file path.
	 */
	public void parse() throws ShellException {
		Integer[] spaceIndices = shell.getIndicesOfCharNotInQuote(cmdline, ' ');
		if (spaceIndices.length == 0) {
			app = cmdline;
			return;
		}
		
		Arrays.sort(spaceIndices);
		app = cmdline.substring(0, spaceIndices[0]);
		
		Vector<String> cmdArgs = new Vector<String>();
		int startIndex = spaceIndices[0] + 1;
		for (int i = 1; i < spaceIndices.length; i++) {
			if (startIndex != spaceIndices[i]) {
				cmdArgs.add(cmdline.substring(startIndex, spaceIndices[i]));
			}
			startIndex = spaceIndices[i] + 1;
		}
		if (startIndex < cmdline.length()) {
			cmdArgs.add(cmdline.substring(startIndex, cmdline.length()));
		}
		
		argsArray = cmdArgs.toArray(new String[cmdArgs.size()]);
	}

//	private void processIoRedir(String[] cmdTokensArray, int nTokens) throws ShellException {
//		if (nTokens >= 3) { // last 2 for inputRedir & >outputRedir
//			this.inputStreamS = cmdTokensArray[nTokens - 2].trim();
//			this.outputStreamS = cmdTokensArray[nTokens - 1].trim();
//			if (!("").equals(inputStreamS)
//					&& inputStreamS.equals(outputStreamS)) {
//				error = true;
//				errorMsg = EXP_SAME_REDIR;
//				throw new ShellException(errorMsg);
//			} 
//			this.argsArray = Arrays.copyOfRange(cmdTokensArray, 1,
//					cmdTokensArray.length - 2);
//		} else {
//			this.argsArray = new String[0]; 
//		}
//	}


	/**
	 * Parses the sub-command's arguments to the call command and splits it into
	 * its different components, namely the application name and the arguments
	 * (if any), based on rules: Unquoted: any char except for whitespace
	 * characters, quotes, newlines, semicolons �;�, �|�, �<� and �>�. Double
	 * quoted: any char except \n, ", ` Single quoted: any char except \n, '
	 * Back quotes in Double Quote for command substitution: DQ rules for
	 * outside BQ + `anything but \n` in BQ.
	 * 
	 * @param str
	 *            String of command to split.
	 * @param cmdVector
	 *            Vector of String to store the split arguments into.
	 * 
	 * @return endIdx Index of string where the parsing of arguments stopped
	 *         (due to no more matches).
	 * 
	 * @throws ShellException
	 *             If an error in the syntax of the command is detected while
	 *             parsing.
	 */
//	int extractArgs(String str, Vector<String> cmdVector) throws ShellException {
//		String patternDash = "[\\s]+(-[A-Za-z]*)[\\s]";
//		String patternUQ = "[\\s]+([^\\s\"'`\\n;|<>]*)[\\s]";
//		String patternDQ = "[\\s]+\"([^\\n\"`]*)\"[\\s]";
//		String patternSQ = "[\\s]+\'([^\\n']*)\'[\\s]";
//		String patternBQ = "[\\s]+(`[^\\n`]*`)[\\s]";
//		String patternBQinDQ = "[\\s]+\"([^\\n\"`]*`[^\\n]*`[^\\n\"`]*)\"[\\s]";
//		String[] patterns = { patternDash, patternUQ, patternDQ, patternSQ,
//				patternBQ, patternBQinDQ };
//		String substring;
//		int newStartIdx = 0, smallestStartIdx, smallestPattIdx, newEndIdx = 0;
//		do {
//			substring = str.substring(newEndIdx);
//			smallestStartIdx = -1;
//			smallestPattIdx = -1;
//			if (substring.trim().startsWith("<")
//					|| substring.trim().startsWith(">")) {
//				break;
//			}
//			for (int i = 0; i < patterns.length; i++) {
//				Pattern pattern = Pattern.compile(patterns[i]);
//				Matcher matcher = pattern.matcher(substring);
//				if (matcher.find()
//						&& (matcher.start() < smallestStartIdx || smallestStartIdx == -1)) {
//					smallestPattIdx = i;
//					smallestStartIdx = matcher.start();
//				}
//			}
//			if (smallestPattIdx != -1) { // if a pattern is found
//				Pattern pattern = Pattern.compile(patterns[smallestPattIdx]);
//				Matcher matcher = pattern.matcher(str.substring(newEndIdx));
//				if (matcher.find()) {
//					String matchedStr = matcher.group(1);
//					newStartIdx = newEndIdx + matcher.start();
//					if (newStartIdx != newEndIdx) {
//						error = true;
//						errorMsg = EXP_SYNTAX;
//						throw new ShellException(errorMsg);
//					} // check if there's any invalid token not detected
//					cmdVector.add(matchedStr);
//					newEndIdx = newEndIdx + matcher.end() - 1;
//				}
//			}
//		} while (smallestPattIdx != -1);
//		return newEndIdx;
//	}


	/**
	 * Terminates current execution of the command (unused for now)
	 */
	@Override
	public void terminate() {
		//not used
	}
}
