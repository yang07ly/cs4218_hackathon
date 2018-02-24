package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.CmpItf;
import sg.edu.nus.comp.cs4218.exception.CatException;
import sg.edu.nus.comp.cs4218.exception.CmpException;

public class CmpApplication implements CmpItf {

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws CmpException {
		if (args == null || args.length == 0) {
			throw new CmpException("requires 2 files to be specified");
		} else {
			boolean flags[] = new boolean[3];
			Vector<String> files = new Vector<String>();
			getArguments(args, flags, files);
			if (files.size() > 2) { 
				throw new CmpException("Can't compare more than 2 sources");
			}
			try {
				String output = "";
				if(!hasInputFromStream(files)) {
					output = cmpTwoFiles(files.get(0), files.get(1), flags[0], flags[1], flags[2]);
				}else if(files.size() == 1) {
					output = cmpFileAndStdin(files.get(0),stdin, flags[0], flags[1], flags[2]);
				}
				stdout.write(output.getBytes());
			} catch (IOException e) {
				throw new CmpException("");
			}
		}
	}

	/**
	 * checks if taking from input stream
	 * @param files
	 * @return true if user specifies '-' as a file
	 */
	private boolean hasInputFromStream(Vector<String> files) {
		boolean hasStream = false;
		for(int i = 0; i < files.size(); i++) {
			hasStream = hasStream || files.get(i).equals("-");
		}
		return hasStream;
	}

	/**
	 * parses user command and extracts arguments
	 * @param args String array of user-written arguments
	 * @param flags boolean array that will store the parsed flags
	 * @param files vector of string that will store the parsed files
	 * @throws CmpException
	 */
	private static void getArguments(String[] args, boolean[] flags, Vector<String> files) throws CmpException {
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-")) {
				files.add(args[i]);
			}else if (args[i].charAt(0) == ('-')) {
				char prevChar = '-';
				for(int j = 1; j < args[i].length(); j++) {
					switch(args[i].charAt(j)) {
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
						if((prevChar == '-') || (j == args[i].length()-1)) {
							throw new CmpException("Invalid flags");
						}
						prevChar = '-';
						break;
						default:throw new CmpException("Invalid flags");
					}
				}
			} else {
				files.add(args[i]);
			}
		}
	}

	@Override
	public String cmpTwoFiles(String fileNameA, String fileNameB, Boolean isPrintCharDiff, Boolean isPrintSimplify,
			Boolean isPrintOctalDiff) throws CmpException, IOException {

		Path filePathA = checkIfValidFile(fileNameA);
		Path filePathB = checkIfValidFile(fileNameB);
		BufferedReader readerA = new BufferedReader(new FileReader(new File(filePathA.toString())));
		BufferedReader readerB = new BufferedReader(new FileReader(new File(filePathB.toString())));
		String msg = cmpFiles(fileNameA, fileNameB, isPrintCharDiff, isPrintSimplify, isPrintOctalDiff, readerA,
				readerB);
		readerA.close();
		readerB.close();
		return msg;
	}

	/**
	 * Compares 2 files and print the messages corresponding to the flags
	 * @param fileNameA String of first filepath
	 * @param fileNameB String of second filepath
	 * @param isPrintCharDiff Boolean to print the differing characters and their octal value
	 * @param isPrintSimplify Boolean to print only whether files differ by printing a message “Files differ”
	 * @param isPrintOctalDiff Boolean to print the (decimal) offsets and (octal) values of all differing bytes.
	 * @param readerA The buffered reader to the first file
	 * @param readerB The buffered reader to the second file
	 * @return
	 * @throws IOException
	 */
	private String cmpFiles(String fileNameA, String fileNameB, Boolean isPrintCharDiff, Boolean isPrintSimplify,
			Boolean isPrintOctalDiff, BufferedReader readerA, BufferedReader readerB) throws IOException {
		int readValueA = 0, readValueB = 0, byteNumber = 1, lineNumber = 1;
		String msgWithoutL = fileNameA + " " + fileNameB + " differ: ";
		String msg = "";

		readValueA = readerA.read();
		readValueB = readerB.read();
		while ((readValueA != -1) || (readValueB != -1)) {
			if (readValueA != readValueB) {
				if (isPrintSimplify) { // -s
					return "Files differ";
				} else if (isPrintCharDiff) {
					if (isPrintOctalDiff) { // -cl
						msg += byteNumber + " " + getOctalString(readValueA) + " " + ((char) readValueA) + " "
								+ getOctalString(readValueB) + " " + ((char) readValueB) + "\n";
					} else { // -c
						msgWithoutL += "byte " + byteNumber + ", " + "line " + lineNumber + " is "
								+ getOctalString(readValueA) + " " + ((char) readValueA) + " "
								+ getOctalString(readValueB) + " " + ((char) readValueB);
						return msgWithoutL;
					}
				} else if (isPrintOctalDiff) { // -l
					msg += byteNumber + " " + getOctalString(readValueA) + " "
							+ getOctalString(readValueB) + "\n";

				} else { // no flags
					msgWithoutL += "byte " + byteNumber + ", " + "line " + lineNumber;
					return msgWithoutL;
				}
			}
			byteNumber++;
			if (readValueA == 10) {
				lineNumber++;
			}
			if((readValueB == -1)&& isPrintOctalDiff) {
				msg += "cmp: EOF on " + fileNameB;
				break;
			}
			if((readValueA == -1) && isPrintOctalDiff) {
				msg += "cmp: EOF on " + fileNameA;
				break;
			}
			readValueA = readerA.read();
			readValueB = readerB.read();
		}
		return msg;
	}

	/**
	 * gets the octalString from an integer
	 * @param readValueA Integer to convert to octal
	 * @return String of octal value
	 */
	private String getOctalString(int readValueA) {
		if(readValueA == -1) {
			return "EOF";
		}
		return Integer.toOctalString(readValueA);
	}

	@Override
	public String cmpFileAndStdin(String fileName, InputStream stdin, Boolean isPrintCharDiff, Boolean isPrintSimplify,
			Boolean isPrintOctalDiff) throws CmpException, IOException {
		Path filePath = checkIfValidFile(fileName);
		BufferedReader readerA = new BufferedReader(new FileReader(new File(filePath.toString())));
		BufferedReader readerB = new BufferedReader(new InputStreamReader(stdin));
		String msg = cmpFiles(fileName, "-", isPrintCharDiff, isPrintSimplify, isPrintOctalDiff, readerA,
				readerB);
		readerA.close();
		readerB.close();
		return msg;
	}

	/**
	 * gets the absolute filepath of a file
	 * @param file String of file name or file path
	 * @return Path of file path
	 * @throws CmpException
	 */
	Path checkIfValidFile(String file) throws CmpException {
		if (file.length() == 0) {
			throw new CmpException("can't have empty argument");
		}
		try {
			Path filePathB = Paths.get(file);
			if (!filePathB.isAbsolute()) {
				filePathB = Paths.get(Environment.currentDirectory).resolve(file);
			}
			checkIfFileIsReadable(filePathB, file);
			return filePathB;
		} catch (InvalidPathException exPath) {
			throw new CmpException("'" + file + "': No such file or directory");
		}
	}
	
	/**
	 * Checks if a file is readable.
	 * 
	 * @param filePath
	 *            The path to the file
	 * @return True if the file is readable.
	 * @throws CatException
	 *             If the file is not readable
	 */
	boolean checkIfFileIsReadable(Path filePath, String file) throws CmpException {
		if (Files.isDirectory(filePath)) {
			throw new CmpException("'" + file + "': this is a directory");
		}
		if (!Files.exists(filePath)) {
			throw new CmpException("'" + file + "': No such file or directory");
		}
		if (Files.isReadable(filePath)) {
			return true;
		} else {
			throw new CmpException("'" + file + "': Could not read file");
		}
	}

}
