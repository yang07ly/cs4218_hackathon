package sg.edu.nus.comp.cs4218.impl.app;

import java.util.Vector;

import sg.edu.nus.comp.cs4218.exception.CmpException;

public final class CmpExtension {
	private CmpExtension() {
	}

	/**
	 * checks if taking from input stream
	 * 
	 * @param files
	 * @return true if user specifies '-' as a file
	 */
	public static boolean hasInputFromStream(Vector<String> files) {
		boolean hasStream = false;
		for (int i = 0; i < files.size(); i++) {
			hasStream = hasStream || files.get(i).equals("-");
		}
		return hasStream;
	}

	/**
	 * parses user command and extracts arguments
	 * 
	 * @param args
	 *            String array of user-written arguments
	 * @param flags
	 *            boolean array that will store the parsed flags
	 * @param files
	 *            vector of string that will store the parsed files
	 * @throws CmpException
	 */
	public static void getArguments(String[] args, boolean[] flags, Vector<String> files) throws CmpException {
		for (int i = 0; i < args.length; i++) {
			if (args[i].isEmpty()) {
				throw new CmpException("'" + args[i] + "': No such file or directory");
			}
			if (args[i].equals("-")) {
				files.add(args[i]);
			} else if (args[i].charAt(0) == ('-')) {
				char prevChar = '-';
				for (int j = 1; j < args[i].length(); j++) {
					switch (args[i].charAt(j)) {
					case 'c':
						prevChar = 'c';
						flags[0] = true;
						break;
					case 's':
						prevChar = 's';
						flags[1] = true;
						break;
					case 'l':
						prevChar = 'l';
						flags[2] = true;
						break;
					case '-':
						if ((prevChar == '-') || (j == args[i].length() - 1)) {
							throw new CmpException("Invalid flags");
						}
						prevChar = '-';
						break;
					default:
						throw new CmpException("Invalid flags");
					}
				}
			} else {
				files.add(args[i]);
			}
		}
	}

	/**
	 * Checks if the ascii value of a byte is not a text character
	 * 
	 * @param fileNameA
	 *            file name of first file
	 * @param fileNameB
	 *            file name of second file
	 * @param readValueA
	 *            the ascii value
	 * @param readValueB
	 *            the ascii value
	 * @throws CmpException
	 *             if any ascii value is not a text character
	 */
	public static void checkBinaryFiles(String fileNameA, String fileNameB, int readValueA, int readValueB)
			throws CmpException {
		if (readValueA > 127 || readValueA == 0) {
			throw new CmpException(fileNameA + " is not a text file");
		}
		if (readValueB > 127 || readValueB == 0) {
			throw new CmpException(fileNameB + " is not a text file");
		}
	}
}
