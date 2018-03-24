package sg.edu.nus.comp.cs4218.impl.app;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.SplitException;

public final class SplitExtension {
	private SplitExtension() {
	}

	/**
	 * checks if split by bytes or lines
	 * 
	 * @param lines
	 *            Int of the number of lines to split the file by
	 * @param bytes
	 *            String of the number of lines to split the file by
	 * @throws SplitException
	 *             if more than 1 line and byte is specified.
	 */
	public static boolean isSplitByLines(String bytes, int lines) throws SplitException {
		return (bytes.length() == 0);
	}

	/**
	 * Takes in the current counter for naming output files and returns the next
	 * lexicographical name.
	 * 
	 * @param currCounter
	 *            String of current counter
	 * @return String of the next counter for the file name
	 */
	public static String getNextName(String currCounter) {
		if (currCounter == null) {
			return "aa";
		}
		char[] name = currCounter.toCharArray();
		String newCounter;
		boolean hasIncremented = false;
		for (int i = name.length; i > 0; i--) {
			if (name[i - 1] == 'z') {
				name[i - 1] = 'a';
			} else {
				name[i - 1]++;
				hasIncremented = true;
				break;
			}
		}
		if (hasIncremented) {
			newCounter = new String(name);
		} else {
			newCounter = "z" + new String(name);
		}

		return newCounter;
	}

	/**
	 * returns path to prefix
	 * 
	 * @param prefix
	 *            String of prefix
	 * @return String of prefix
	 */
	public static String getAbsolutePath(String prefix) {
		if (prefix == null) {
			return Environment.currentDirectory + File.separator + "x";
		}
		Path path = Paths.get(prefix);
		if (path.isAbsolute()) {
			return prefix;
		} else {
			return Environment.currentDirectory + File.separator + prefix;
		}
	}

	/**
	 * parses the number of lines
	 * 
	 * @param linesString
	 *            the string of lines
	 * @return Int of lines
	 * @throws SplitException
	 */
	public static int parseLines(String linesString) throws SplitException {
		try {
			return Integer.parseInt(linesString);
		} catch (NumberFormatException exNum) {
			throw new SplitException(linesString + ": invalid number of lines: ");
		}
	}

	/**
	 * Gets the number of bytes by multiplying with the appended letter
	 * 
	 * @param bytes
	 *            String of the number of bytes e.g (100k, 100m)
	 * @return
	 * @throws SplitException
	 *             if invalid string of bytes
	 */
	public static int parseBytes(String bytes) throws SplitException {
		int numBytes;
		try {
			numBytes = Integer.parseInt(bytes);
		} catch (NumberFormatException ne) {
			try {
				String num = bytes.substring(0, bytes.length() - 1);
				char multiplier = bytes.charAt(bytes.length() - 1);
				numBytes = Integer.parseInt(num);

				switch (multiplier) {
				case 'b':
					numBytes *= 512;
					break;
				case 'k':
					numBytes *= 1024;
					break;
				case 'm':
					numBytes *= 1048576;
					break;
				default:
					throw new SplitException(bytes + ": invalid number of bytes");
				}
			} catch (Exception e) {
				throw new SplitException(bytes + ": invalid number of bytes");
			}
		}

		if (numBytes == 0) {
			throw new SplitException(bytes + ": invalid number of bytes");
		}
		return numBytes;
	}
}
