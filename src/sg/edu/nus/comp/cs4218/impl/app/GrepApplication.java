package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.app.GrepInterface;
import sg.edu.nus.comp.cs4218.exception.GrepException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

/**
 * The grep command searches for lines containing a match to a specified
 * pattern. The output of the command is the list of the lines matching the
 * pattern. Each line is printed followed by a newline.
 * 
 * <p>
 * <b>Command format:</b> <code>grep [-v] PATTERN [FILE]...</code>
 * <dl>
 * <dt>PATTERN</dt>
 * <dd>specifies a regular expression in JAVA format
 * (https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html).
 * </dd>
 * <dt>-v</dt>
 * <dd>invert the sense of matching, to select non-matching lines.</dd>
 * <dt>FILE</dt>
 * <dd>the name of the file or files. If not specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public class GrepApplication implements GrepInterface {

	public static final String EXP_NULL_POINTER = "Null Pointer Exception";

	/**
	 * Runs the grep application with the specified arguments.
	 * 
	 * @param args
	 *            Array of arguments for the application. Array element consist of
	 *            the option of grep, first found non-option element should be the
	 *            PATTERN followed by elements containing the path to a file. If no
	 *            files are specified stdin is used.
	 * @param stdin
	 *            An InputStream. The input for the command is read from this
	 *            InputStream if no files are specified.
	 * @param stdout
	 *            An OutputStream. The output of the command is written to this
	 *            OutputStream.
	 * 
	 * @throws GrepException
	 *             If the file(s) specified do not exist or are unreadable or if
	 *             options id specified incorrectly or if an I/O exception occurs.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws GrepException {
		if (stdout == null) {
			throw new GrepException("Null Pointer Exception");
		}

		boolean isInvert = getOption(args);
		String pattern = getPattern(args);
		Vector<String> paths = getPaths(args);

		String outputStr = "";
		if (paths.isEmpty()) {
			outputStr += grepFromStdin(pattern, isInvert, stdin);
		} else {
			outputStr += grepFromMultipleFiles(pattern, isInvert, paths.toArray((new String[paths.size()])));
		}

		try {
			stdout.write(outputStr.getBytes());
		} catch (IOException e) {
			throw new GrepException("IOException");
		}
	}

	/**
	 * Returns string containing lines which match the specified pattern in Stdin.
	 * 
	 * @param pattern
	 *            String specifying a regular expression in JAVA format.
	 * @param isInvert
	 *            Boolean option to filter the output that doesn't match. the
	 *            pattern
	 * @param stdin
	 *            InputStream containing arguments from Stdin.
	 * @throws GrepException
	 *             If an I/O exception occurs.
	 */
	@Override
	public String grepFromStdin(String pattern, Boolean isInvert, InputStream stdin) throws GrepException {
		if (stdin == null) {
			throw new GrepException(EXP_NULL_POINTER);
		}

		String outputStr = "";
		InputStreamReader inStream = new InputStreamReader(stdin);
		try {
			BufferedReader content = new BufferedReader(inStream);
			String line = content.readLine();
			if (line == null) {
				return "";
			}

			do {
				if (hasPattern(isInvert, pattern, line)) {
					outputStr += line + OSUtil.NEWLINE;
				}
			} while ((line = content.readLine()) != null);

		} catch (IOException e) {
			throw new GrepException("IOException");
		}
		return outputStr.trim();
	}

	/**
	 * Returns string containing lines which match the specified pattern in the
	 * given files.
	 * 
	 * @param pattern
	 *            String specifying a regular expression in JAVA format.
	 * @param isInvert
	 *            Boolean option to filter the output that doesn't match. the
	 *            pattern
	 * @param fileNames
	 *            Array of file names.
	 * @throws GrepException
	 *             If the file(s) specified do not exist or are unreadable or if an
	 *             I/O exception occurs.
	 */
	@Override
	public String grepFromMultipleFiles(String pattern, Boolean isInvert, String... fileNames) throws GrepException {
		if (fileNames.length == 0) {
			throw new GrepException(EXP_NULL_POINTER);
		}

		boolean printFileName = fileNames.length > 1;
		String outputStr = "";
		for (int i = 0; i < fileNames.length; i++) {
			File file;
			try {
				file = FileUtil.getFileFromPath(fileNames[i]);
			} catch (IOException e) {
				throw new GrepException(e.getMessage());
			}
			try {
				BufferedReader content = new BufferedReader(new FileReader(file));
				String line = content.readLine();
				if (line == null) {
					if (printFileName) {
						outputStr += fileNames[i] + ": ";
					}
					outputStr += OSUtil.NEWLINE;
					content.close();
					continue;
				}

				do {
					if (hasPattern(isInvert, pattern, line)) {
						if (printFileName) {
							outputStr += fileNames[i] + ": ";
						}
						outputStr += line + OSUtil.NEWLINE;
					}
				} while ((line = content.readLine()) != null);
				content.close();

			} catch (IOException e) {
				throw new GrepException("IOException");
			}
		}
		return outputStr.trim();
	}

	/**
	 * Returns true if a valid option is found.
	 * 
	 * @param args
	 *            Array of arguments for the application.
	 * @throws GrepException
	 *             If the options specified is invalid or args is null.
	 */
	private boolean getOption(String... args) throws GrepException {
		if (args == null) {
			throw new GrepException(EXP_NULL_POINTER);
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && args[i].length() >= 2) {
				for (int j = 1; j < args[i].length(); j++) {
					if (args[i].charAt(1) != 'v') {
						throw new GrepException("invalid option -- '" + args[i].charAt(1) + "'");
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the pattern found in the args.
	 * 
	 * @param args
	 *            Array of arguments for the application.
	 * @throws GrepException
	 *             If no pattern is found or args is null.
	 */
	private String getPattern(String... args) throws GrepException {
		if (args == null) {
			throw new GrepException(EXP_NULL_POINTER);
		}

		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && args[i].length() >= 2) {
				continue;
			} else {
				return args[i];
			}
		}
		throw new GrepException("pattern is not specified");
	}

	/**
	 * Returns the file paths found in the args.
	 * 
	 * @param args
	 *            Array of arguments for the application.
	 */
	private Vector<String> getPaths(String... args) {
		Vector<String> paths = new Vector<String>();
		if (args == null) {
			return paths;
		}

		boolean hasFoundPattern = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && args[i].length() >= 2) {
				continue;
			}

			if (!hasFoundPattern) {
				hasFoundPattern = true;
				continue;
			}
			paths.add(args[i]);
		}
		return paths;
	}

	/**
	 * Returns true if the pattern is found in the line.
	 * 
	 * @param isInvert
	 *            Boolean to indicate the inversion of matching.
	 * @param pattern
	 *            String of the regex expression to match.
	 * @param line
	 *            String of the line to check for matching.
	 * @throws GrepException
	 *             If pattern or line is null.
	 */
	private boolean hasPattern(Boolean isInvert, String pattern, String line) throws GrepException {
		if (pattern == null || line == null) {
			throw new GrepException(EXP_NULL_POINTER);
		}

		Matcher matcher = Pattern.compile(pattern).matcher(line);
		boolean hasMatched = matcher.find();
		return (hasMatched && !isInvert) || (!hasMatched && isInvert);
	}
}
