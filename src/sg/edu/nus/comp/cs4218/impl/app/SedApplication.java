package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.SedInterface;
import sg.edu.nus.comp.cs4218.exception.SedException;

/**
 * The sed command copies input file (or input stream) to stdout and performs string
 * replacement. For each line containing a match to a specified pattern (in JAVA format),
 * replaces the matched substring with the specified string.
 * 
 * <p>
 * <b>Command format:</b> <code>sed REPLACEMENT [FILE]</code>
 * <dl>
 * <dt>REPLACEMENT</dt>
 * <dd>
 * <code>s/regexp/replacement/</code> – replace the first (in each line) substring matched 
 * by regexp with the string replacement.
 * <br />
 * <code>s/regexp/replacement/X</code> – X is a number. Only replace the Xth match of the 
 * regexp.
 * <br /><br />
 * Note that the symbols “/” used to separate regexp and replacement string can be
 * substituted by any other symbols. For example, “s/a/b/” and “s|a|b|” are the same
 * replacement rules. However, this separation symbol should not be used inside the
 * regexp and the replacement string.
 * </dd>
 * <dt>FILE</dt>
 * <dd>the name of the file(s). If no files are specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public class SedApplication implements SedInterface {

	public static final String EXP_NULL_POINTER = "Null Pointer Exception";

	/**
	 * Runs the sed application with the specified arguments.
	 * 
	 * @param args
	 *            Array of arguments for the application. The first array element
	 *            should be the REPLACEMENT option followed by elements containing
	 *            the path to a file. If no files are specified stdin is used.
	 * @param stdin
	 *            An InputStream. The input for the command is read from this
	 *            InputStream if no files are specified.
	 * @param stdout
	 *            An OutputStream. The output of the command is written to this
	 *            OutputStream.
	 * 
	 * @throws SedException
	 *            If the file(s) specified do not exist or are unreadable or
	 *            if options id specified incorrectly or
	 *            if an I/O exception occurs..
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) 
			throws SedException {
		if (stdout == null) {
			throw new SedException(EXP_NULL_POINTER);
		}

		if (args == null || args.length == 0) {
			throw new SedException("REPLACEMENT is not specified");
		}

		validateReplOptions(args[0]);
		char sepChar = getSepChar(args[0]);
		String regexp = getRegExp(args[0], sepChar);
		String replexp = getReplExp(args[0], sepChar);
		int matchIndex = getNthValue(args[0], sepChar);

		String outputStr = "";
		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				outputStr += replaceSubstringInFile(regexp, replexp, matchIndex, args[i]);
			}
		} else {
			outputStr += replaceSubstringInStdin(regexp, replexp, matchIndex, stdin);
		}

		try {
			stdout.write(outputStr.getBytes());
		} catch (IOException e) {
			throw new SedException("IOException");
		}
	}

	/**
	 * Returns string of the file content with the matched 
	 * substring on each line replaced. For each line, find
	 * the substring that matched the pattern and replace 
	 * the substring in the specified index of the matched 
	 * substring list. 
	 * @param pattern 
	 * 				String specifying a regular expression 
	 * 				in JAVA format.
	 * @param replacement 
	 * 				String to replace the matched pattern.
	 * @param replacementIndex 
	 * 				Integer specifying the index of the matched
	 * 				substring to be replaced (default is 0).
	 * @param fileName 
	 * 				String specifying name of a valid file.
	 * @throws SedException
	 * 				If pattern is null or empty or
	 * 				if replacement is null or
	 * 				if fileName is null or 
	 * 				if replacementIndex is 0 or
	 * 				if an I/O exception occurs.
	 */
	@Override
	public String replaceSubstringInFile(String pattern, String replacement, int replacementIndex, String fileName)
			throws SedException {
		if (fileName == null) {
			throw new SedException(EXP_NULL_POINTER);
		}

		Path filePath;
		Path currentDir = Paths.get(Environment.currentDirectory);
		try {
			filePath = currentDir.resolve(fileName);
		} catch (InvalidPathException e) {
			throw new SedException("can't read " + fileName + ": No such file or directory");
		}

		if (fileName.isEmpty() || !Files.exists(filePath) || !Files.isReadable(filePath)) {
			throw new SedException("can't read " + fileName + ": No such file or directory");
		}

		if (Files.isDirectory(filePath)) {
			throw new SedException("read error on " + fileName + ": Is a directory");
		}

		String outputStr = "";	
		try {
			BufferedReader content = new BufferedReader(new FileReader(filePath.toFile()));
			String line;
			while((line = content.readLine()) != null) {
				outputStr += getReplacedLine(pattern, replacement, replacementIndex, line);
			}
			content.close();

		} catch (IOException e) {
			throw new SedException("IOException: " + e);
		}
		return outputStr;
	}

	/**
	 * Returns string of the Stdin arg content with the matched 
	 * substring on each line replaced. For each line, find
	 * the substring that matched the pattern and replace 
	 * the substring in the specified index of the matched 
	 * substring list.
	 * @param pattern 
	 * 				String specifying a regular expression 
	 * 				in JAVA format.
	 * @param replacement 
	 * 				String to replace the matched pattern.
	 * @param replacementIndex 
	 * 				Integer specifying the index of the matched 
	 * 				substring to be replaced (default is 0).
	 * @param stdin 
	 * 				InputStream containing arguments from Stdin.
	 * @throws SedException
	 * 				If pattern is null or empty or
	 * 				if replacement is null or
	 * 				if fileName is null or 
	 * 				if replacementIndex is 0 or
	 * 				if an I/O exception occurs.
	 */
	@Override
	public String replaceSubstringInStdin(String pattern, String replacement, int replacementIndex, InputStream stdin)
			throws SedException {
		if (stdin == null) {
			throw new SedException(EXP_NULL_POINTER);
		}

		String outputStr = "";

		InputStreamReader inStream = new InputStreamReader(stdin);
		try {
			BufferedReader content = new BufferedReader(inStream);
			String line;
			while((line = content.readLine()) != null) {
				outputStr += getReplacedLine(pattern, replacement, replacementIndex, line);
			}

		} catch (IOException e) {
			throw new SedException("IOException");
		}
		return outputStr;
	}

	/**
	 * Validate that the Sed options and throw SedException when error occurs.
	 * @param option
	 * 				String of the specified sed option.
	 * @throws SedException
	 * 				If the command is unknown or unterminated.
	 */
	private void validateReplOptions(String option) 
			throws SedException {
		if (!option.startsWith("s")) {
			throw new SedException("char 1: unknown command: '" + option.charAt(0) + "'");
		}

		if (option.length() < 2) {
			throw new SedException("char 1: unterminated: '" + option.charAt(0) + "'");
		}

		char sepChar = option.charAt(1);	
		if (!option.matches("s" + sepChar + ".*" + sepChar + ".*" + sepChar + ".*")) {
			throw new SedException("char " + option.length() + ": unterminated: '" + option.charAt(0) + "'");
		}	
	}

	/**
	 * Return the separating character in the validated option.
	 * @param option
	 * 				String of the validated sed option.
	 * @return char
	 * 				Separating character specified in the option.
	 */
	private char getSepChar(String option) {
		return option.charAt(1);
	}

	/**
	 * Return the regular expression in the validated option.
	 * @param option
	 * 				String of the validated sed option.
	 * @param sepChar
	 * 				String of the separating character specified in sed option.
	 * @return String
	 * 				Regular expression in the option.
	 * @throws SedException
	 * 				If the regular expression is empty.
	 */
	private String getRegExp(String option, char sepChar) 
			throws SedException {
		int endIndex = option.indexOf(sepChar, 2);
		String regexp = option.substring(2, endIndex);

		if (regexp.isEmpty()) {
			throw new SedException("char 0: no previous regular expression");
		}
		return regexp;
	}

	/**
	 * Return the replacement expression in the validated option.
	 * @param option
	 * 				String of the validated sed option.
	 * @param sepChar
	 * 				String of the separating character specified in sed option.
	 * @return String
	 * 				Replacement expression in the option, possibly empty.
	 */
	private String getReplExp(String option, char sepChar) {
		int beginIndex = option.indexOf(sepChar, 2) + 1;
		int endIndex = option.indexOf(sepChar, beginIndex);
		return option.substring(beginIndex, endIndex);
	}

	/**
	 * Return the value of the Nth in the validated option.
	 * @param option
	 * 				String of the validated sed option.
	 * @param sepChar
	 * 				String of the separating character specified in sed option.
	 * @return int
	 * 				The value of the Nth.
	 * @throws SedException
	 * 				If the Nth value is not an integer or is 0.
	 */
	private int getNthValue(String option, char sepChar) 
			throws SedException {
		int beginIndex = option.lastIndexOf(sepChar);
		String nthStr = option.substring(beginIndex + 1);

		int matchIndex;
		if (nthStr.isEmpty()) {
			matchIndex = -1;
		} else {
			if (!nthStr.matches("[0-9]*")) {
				throw new SedException("char " + beginIndex + ": unknown options to '" + option.charAt(0) + "'");
			}
			matchIndex = Integer.parseInt(nthStr);
		}

		if (matchIndex == 0) {
			throw new SedException("char " + beginIndex + ": number option to '" + option.charAt(0) 
			+ "' command may not be zero");
		}
		return matchIndex;
	}

	/**
	 * Returns string of the line with the matched substring replaced. 
	 * @param pattern 
	 * 				String specifying a regular expression in JAVA format.
	 * @param replacement 
	 * 				String to replace the matched pattern.
	 * @param replacementIndex 
	 * 				Integer specifying the index of the matched substring to be 
	 * 				replaced (default is 0).
	 * @param line 
	 * 				String of the line to be replaced.
	 * @return String
	 * 				Line with the matched substring replaced.
	 * @throws SedException
	 * 				If pattern is empty or the Nth value is 0.
	 */
	private String getReplacedLine(String pattern, String replacement, int replacementIndex, String line)
			throws SedException{
		if (pattern == null || replacement == null || line == null) {
			throw new SedException(EXP_NULL_POINTER);
		}

		if (pattern.length() == 0) {
			throw new SedException("regular expression cannot be empty");
		}

		if (replacementIndex == 0) {
			throw new SedException("command may not be zero");
		}

		if (replacementIndex < 0) {
			return line.replaceAll(pattern, replacement) + "\n";
		}

		Matcher matcher = Pattern.compile(pattern).matcher(line);
		for(int i = 0; i < replacementIndex; i++) {
			matcher.find();
		}

		if (matcher.hitEnd()) {
			return line + "\n";
		}

		int matchedIndex;
		try {
			matchedIndex = matcher.start();
		} catch (IllegalStateException e) {
			return line + "\n";
		}
		return line.substring(0, matchedIndex) + line.substring(matchedIndex).replaceFirst(pattern, replacement) + "\n";
	}
}
