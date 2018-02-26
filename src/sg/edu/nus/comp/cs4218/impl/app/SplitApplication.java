package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.SplitInterface;
import sg.edu.nus.comp.cs4218.exception.CatException;
import sg.edu.nus.comp.cs4218.exception.SplitException;

public class SplitApplication implements SplitInterface{

	private static final String FILE_NOT_FOUND = "': No such file or directory";

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws SplitException {
		String[] flags = new String[4];
		getArgs(args, flags);
		String file = flags[0], prefix = flags[1], bytes = flags[2], lineString = flags[3];
		int lines = parseLines(lineString);
		
		if(file == null) {
			if (stdin == null) {
				throw new SplitException("Null Pointer Exception");
			}
			if(isSplitByLines(bytes, lines)) {
				splitStreamByLines(stdin, prefix, lines);
			}else {
				splitStreamByBytes(stdin, prefix, bytes);
			}
		}else {
			if(isSplitByLines(bytes, lines)) {
				splitFileByLines(file, prefix, lines);
			}else {
				splitFileByBytes(file, prefix, bytes);
			}
		}
	}
	
	/**
	 * parses user arguments and stores them in processedArgs
	 * @param args user arguments
	 * @param processedArgs parsed arguments
	 * @throws SplitException
	 */
	private void getArgs(String[] args, String... processedArgs) throws SplitException {
		String file = null, prefix = null, bytes = "", lines = "-1";
		boolean hasFlag = false, hasSplitter = false;
		if(args != null) {
			for (int i = 0; i < args.length; i++) {
				try {
					if(hasFlag) {
						hasFlag = false;
					}else if (args[i].equals("-b")) {
						if(hasSplitter) {
							throw new SplitException("cannot split in more than one way");
						}else {
							bytes = args[i + 1];
							hasFlag = hasSplitter = true;
						}
					} else if (args[i].equals("-l")) {
						if(hasSplitter) {
							throw new SplitException("cannot split in more than one way");
						}else {
							lines = args[i + 1];
							hasFlag = hasSplitter = true;
						}
						lines = args[i + 1];
					} else if(file == null) {
						file = args[i];
					}else if(prefix == null){
						prefix = args[i];
					}else {
						throw new SplitException("extra operand '" + args[i] + "'");
					}
				} catch (ArrayIndexOutOfBoundsException exArr) {
					throw new SplitException("option requires an argument -- '" + args[i] + "'");
				}
			}
		}
		processedArgs[0] = file;
		processedArgs[1] = prefix;
		processedArgs[2] = bytes;
		processedArgs[3] = lines;
		return;
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
	private boolean isSplitByLines(String bytes, int lines) throws SplitException {
		int numLines = lines;
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
	private String getNextName(String currCounter) {
		if(currCounter == null) {
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
	
	@Override
	public void splitFileByLines(String fileName, String prefix, int linesPerFile) throws SplitException {
		FileInputStream stream = getFileInputStream(fileName);
		splitStreamByLines(stream, prefix, linesPerFile);
	}
	
	/**
	 * Split a stream into fixed size pieces with specified number of 
	 * lines. Output splits naming convention: prefix + counter.
	 * Default prefix is "x". Default counter is aa, ab, ..., zz, 
	 * zaa, zab, ..., zzz, zzaa, etc. For example: xaa, xab, etc.
	 * This is the default option for 'split'.
	 * @param fileName String of source file name
	 * @param prefix String of output file prefix (default is 'x')
	 * @param linesPerFile Int of lines to have in the output file
	 * (default is 1,000 lines)
	 * @throws Exception
	 */
	public void splitStreamByLines(InputStream stdin, String prefix, int linesPerFile) throws SplitException {
		String outputPrefix = getAbsolutePath(prefix);
		int lines = linesPerFile;
		if (lines == 0) {
			throw new SplitException(lines + ": invalid number of lines");
		}else if (lines == -1) {
			lines = 1000;
		}
		String currCounter = "aa", line;
		int count = 1;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputPrefix + currCounter));
			line = reader.readLine();
			writer.write(line);
			while ((line = reader.readLine()) != null) {
				if (count == lines) {
					writer.flush();
					writer.close();
					currCounter = getNextName(currCounter);
					writer = new BufferedWriter(new FileWriter(outputPrefix + currCounter));
					count = 1;
					writer.write(line);
					continue;
				}
				writer.newLine();
				writer.write(line);
				count++;
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException exIO) {
			throw new SplitException("'" + prefix + FILE_NOT_FOUND);
		}
	}

	@Override
	public void splitFileByBytes(String fileName, String prefix, String bytesPerFile) throws SplitException {
		FileInputStream stream = getFileInputStream(fileName);
		splitStreamByBytes(stream, prefix, bytesPerFile);
	}
	
	/**
	 * Split a stream into fixed size pieces with specified number of 
	 * bytes. Output splits naming convention: prefix + counter.
	 * Default prefix is "x". Default counter is aa, ab, ..., zz, 
	 * zaa, zab, ..., zzz, zzaa, etc. For example: xaa, xab, etc.
	 * @param fileName String of source file name
	 * @param prefix String of output file prefix (default is 'x')
	 * @param bytesPerFile String of number of bytes of content to 
	 * fit into a file. Can have a suffix of either 'b', 'k', or 'm'.
	 * Impact of suffix:
	 * 'b' - multiply the bytes by 512
	 * 'k' - multiply the bytes by 1024
	 * 'm' - multiply the bytes by 1048576
	 * @throws Exception
	 */
	private void splitStreamByBytes(InputStream stdin, String prefix, String bytesPerFile) throws SplitException {
		String outputPrefix = getAbsolutePath(prefix);
		int numBytes = parseBytes(bytesPerFile);
		char[] buffer = new char[numBytes];
		String currCounter = null;
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
			BufferedWriter writer;
			while ((count = reader.read(buffer, 0, numBytes)) != -1) {
				currCounter = getNextName(currCounter);
				writer = new BufferedWriter(new FileWriter(outputPrefix + currCounter));
				writer.write(buffer, 0, count);
				writer.flush();
				writer.close();
				buffer = new char[numBytes];
			}
			reader.close();
		} catch (IOException exIO) {
			throw new SplitException("'" + prefix + FILE_NOT_FOUND);
		}
	}

	/**
	 * returns path to prefix
	 * @param prefix String of prefix
	 * @return String of prefix
	 */
	private String getAbsolutePath(String prefix) {
		if (prefix == null) {
			return Environment.currentDirectory + File.separator + "x";
		}
		Path path = Paths.get(prefix);
		if(path.isAbsolute()) {
			return prefix;
		}else {
			return Environment.currentDirectory + File.separator + prefix;
		}
	}

	/**
	 * parses the number of lines
	 * @param linesString the string of lines
	 * @return Int of lines
	 * @throws SplitException
	 */
	private int parseLines(String linesString) throws SplitException {
		try {
			return Integer.parseInt(linesString);
		}catch (NumberFormatException exNum) {
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
	private int parseBytes(String bytes) throws SplitException {
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

	/**
	 * gets the fileInputStream of a file
	 * @param file
	 *            String of file name or file path
	 * @return fileInputStream of file
	 * @throws SplitException
	 */
	FileInputStream getFileInputStream(String file) throws SplitException {
		if (file.length() == 0) {
			throw new SplitException("can't have empty argument");
		}
		try {
			Path filePathB = Paths.get(file);
			if (!filePathB.isAbsolute()) {
				filePathB = Paths.get(Environment.currentDirectory).resolve(file);
			}
			checkIfFileIsReadable(filePathB, file);
			return new FileInputStream(filePathB.toString());
		} catch (InvalidPathException exPath) {
			throw new SplitException("'" + file + FILE_NOT_FOUND);
		} catch (FileNotFoundException e) {
			throw new SplitException("'" + file + FILE_NOT_FOUND);
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
	boolean checkIfFileIsReadable(Path filePath, String file) throws SplitException {
		if (Files.isDirectory(filePath)) {
			throw new SplitException("'" + file + "': this is a directory");
		}
		if (!Files.exists(filePath)) {
			throw new SplitException("'" + file + FILE_NOT_FOUND);
		}
		if (Files.isReadable(filePath)) {
			return true;
		} else {
			throw new SplitException("'" + file + "': Could not read file");
		}
	}

}
