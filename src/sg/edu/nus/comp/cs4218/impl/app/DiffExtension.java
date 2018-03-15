package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.exception.DiffException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;

public final class DiffExtension {
	private DiffExtension() {
	}

	/**
	 * Scans buffered reader and stores lines in hash set and vector for efficient computation
	 * @param reader buffered reader to retrieve lines from
	 * @param set hashset to store lines in
	 * @param lines vector to store lines in
	 * @param isNoBlank flag to skip blank lines
	 * @throws DiffException
	 */
	public static void preProcessLines(BufferedReader reader, HashSet<String> set, Vector<String> lines,
			boolean isNoBlank) throws DiffException {
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (isNoBlank && isEmpty(line)) {
					continue;
				}
				set.add(line);
				lines.add(line);
			}
		} catch (IOException e) {
			throw new DiffException("IO error occured");
		}
	}

	/**
	 * efficient computation of checking if string is empty
	 * @param string string to check
	 * @return true if string is empty and false otherwise
	 */
	public static boolean isEmpty(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isWhitespace(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * extracts arguments from command line
	 * @param args string array of arguments to extract from
	 * @param flags boolean array to store flags
	 * @param hasFilesDirStream boolean array to store if there are files, directories, or inputstream
	 * @param files vector to store the files
	 * @param directories vector to store the directories
	 * @throws DiffException
	 */
	public static void getArguments(String[] args, boolean flags[], boolean[] hasFilesDirStream, Vector<String> files,
			Vector<String> directories) throws DiffException {
		for (int i = 0; i < args.length; i++) {
			if (args[i].isEmpty()) {
				throw new DiffException("'" + args[i] + "': No such file or directory");
			}
			if (args[i].equals("-")) {
				hasFilesDirStream[2] = true;
			} else if (args[i].charAt(0) == ('-')) {
				char prevChar = '-';
				for (int j = 1; j < args[i].length(); j++) {
					switch (args[i].charAt(j)) {
					case 's':
						prevChar = 's';
						flags[0] = true;
						break;
					case 'B':
						prevChar = 'B';
						flags[1] = true;
						break;
					case 'q':
						prevChar = 'q';
						flags[2] = true;
						break;
					case '-':
						if ((prevChar == '-') || (j == args[i].length() - 1)) {
							throw new DiffException("Invalid flags");
						}
						prevChar = '-';
						break;
					default:
						throw new DiffException("Invalid flags");
					}
				}
			} else if (FileUtil.isDirectory(args[i])) {
				hasFilesDirStream[1] = true;
				directories.add(args[i]);
			} else {
				hasFilesDirStream[0] = true;
				files.add(args[i]);
			}
		}
	}
}
