package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.app.CmpInterface;
import sg.edu.nus.comp.cs4218.exception.CmpException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;

public class CmpApplication implements CmpInterface {
	private final String NEWLINE = System.lineSeparator();

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws CmpException {
		if (args == null || args.length == 0) {
			throw new CmpException("requires 2 files to be specified");
		} else {
			boolean flags[] = new boolean[3];
			Vector<String> files = new Vector<String>();
			CmpExtension.getArguments(args, flags, files);
			if (files.size() != 2) {
				throw new CmpException("requires 2 files to be specified");
			}
			try {
				String output = "";
				if (CmpExtension.hasInputFromStream(files)) {
					if (files.size() == 2) {
						output = cmpFileAndStdin(files.get(0), stdin, flags[0], flags[1], flags[2]);
					}
				} else {
					output = cmpTwoFiles(files.get(0), files.get(1), flags[0], flags[1], flags[2]);
				}
				stdout.write(output.getBytes());
			} catch (IOException e) {
				throw new CmpException("");
			}
		}
	}

	@Override
	public String cmpTwoFiles(String fileNameA, String fileNameB, Boolean isPrintCharDiff, Boolean isPrintSimplify,
			Boolean isPrintOctalDiff) throws CmpException {
		try {
			File fileA = FileUtil.getFileFromPath(fileNameA);
			File fileB = FileUtil.getFileFromPath(fileNameB);
			BufferedReader readerA = new BufferedReader(new FileReader(fileA));
			BufferedReader readerB = new BufferedReader(new FileReader(fileB));
			String msg = cmpFiles(fileNameA, fileNameB, isPrintCharDiff, isPrintSimplify, isPrintOctalDiff, readerA,
					readerB);
			readerA.close();
			readerB.close();
			return msg;
		} catch (IOException e) {
			throw new CmpException(e.getMessage());
		}
	}

	/**
	 * Compares 2 files and print the messages corresponding to the flags
	 * 
	 * @param fileNameA
	 *            String of first filepath
	 * @param fileNameB
	 *            String of second filepath
	 * @param isPrintCharDiff
	 *            Boolean to print the differing characters and their octal value
	 * @param isPrintSimplify
	 *            Boolean to print only whether files differ by printing a message
	 *            “Files differ”
	 * @param isPrintOctalDiff
	 *            Boolean to print the (decimal) offsets and (octal) values of all
	 *            differing bytes.
	 * @param readerA
	 *            The buffered reader to the first file
	 * @param readerB
	 *            The buffered reader to the second file
	 * @return
	 * @throws IOException
	 * @throws CmpException
	 */
	private String cmpFiles(String fileNameA, String fileNameB, Boolean isPrintCharDiff, Boolean isPrintSimplify,
			Boolean isPrintOctalDiff, BufferedReader readerA, BufferedReader readerB) throws IOException, CmpException {
		int readValueA = readerA.read(), readValueB = readerB.read(), byteNumber = 1, lineNumber = 1, numLinesInMsg = 0;
		String msgWithoutL = fileNameA + " " + fileNameB + " differ: ", msg = "";
		while ((readValueA != -1) || (readValueB != -1)) {
			CmpExtension.checkBinaryFiles(fileNameA, fileNameB, readValueA, readValueB);
			if ((readValueB == -1) && isPrintOctalDiff && !isPrintSimplify) {
				msg += "cmp: EOF on " + fileNameB;
				break;
			} else if ((readValueA == -1) && isPrintOctalDiff && !isPrintSimplify) {
				msg += "cmp: EOF on " + fileNameA;
				break;
			}
			if (readValueA != readValueB) {
				numLinesInMsg++;
				if (numLinesInMsg > 1) {
					msg += NEWLINE;
				}
				if (isPrintSimplify) { // -s
					return "Files differ";
				} else if (isPrintCharDiff) {
					if (isPrintOctalDiff) { // -cl
						msg += byteNumber + " " + getOctalString(readValueA) + " " + getChar(readValueA) + " "
								+ getOctalString(readValueB) + " " + getChar(readValueB);
					} else { // -c
						msgWithoutL += "char " + byteNumber + ", " + "line " + lineNumber + " is "
								+ getOctalString(readValueA) + " " + getChar(readValueA) + " "
								+ getOctalString(readValueB) + " " + getChar(readValueB);
						return msgWithoutL;
					}
				} else if (isPrintOctalDiff) { // -l
					msg += byteNumber + " " + getOctalString(readValueA) + " " + getOctalString(readValueB);
				} else { // no flags
					msgWithoutL += "char " + byteNumber + ", " + "line " + lineNumber;
					return msgWithoutL;
				}
			}
			byteNumber++;
			if (readValueA == 10) {
				lineNumber++;
			}
			readValueA = readerA.read();
			readValueB = readerB.read();
		}
		return msg;
	}

	@Override
	public String cmpFileAndStdin(String fileName, InputStream stdin, Boolean isPrintCharDiff, Boolean isPrintSimplify,
			Boolean isPrintOctalDiff) throws CmpException, IOException {
		try {
			File file = FileUtil.getFileFromPath(fileName);
			BufferedReader readerA = new BufferedReader(new FileReader(file));
			BufferedReader readerB = new BufferedReader(new InputStreamReader(stdin));
			String msg = cmpFiles(fileName, "-", isPrintCharDiff, isPrintSimplify, isPrintOctalDiff, readerA, readerB);
			readerA.close();
			readerB.close();
			return msg;
		} catch (IOException e) {
			throw new CmpException(e.getMessage());
		}
	}

	/**
	 * converts int to character using ascii conversion
	 * 
	 * @param readValueB
	 *            the int value to convert
	 * @return the converted character
	 */
	public String getChar(int readValueB) {
		if (readValueB == -1) {
			return "EOF";
		} else if (readValueB == 10) {
			return "LF";
		} else if (readValueB == 13) {
			return "CR";
		} else {
			String character = "";
			character += (char) readValueB;
			return character;
		}
	}

	/**
	 * gets the octalString from an integer
	 * 
	 * @param readValueA
	 *            Integer to convert to octal
	 * @return String of octal value
	 */
	public String getOctalString(int readValueA) {
		return Integer.toOctalString(readValueA);
	}
}
