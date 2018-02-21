package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.SplitItf;
import sg.edu.nus.comp.cs4218.exception.CatException;
import sg.edu.nus.comp.cs4218.exception.CmpException;
import sg.edu.nus.comp.cs4218.exception.SplitException;

/**
 * The split command writes its arguments separated by spaces and terminates by
 * a newline on the standard output.
 * 
 * <p>
 * <b>Command format:</b> <code>split [OPTIONS] [FILE {PREFIX]]...</code>
 * </p>
 */
public class SplitApplication implements SplitItf {

	/**
	 * Runs the split application with the specified arguments.
	 * 
	 * @param args
	 *            Array of arguments for the application. Each array element is the
	 *            path to a file. If a file is not specified stdin is used.
	 * @param stdin
	 *            An InputStream. The input for the command is read from this
	 *            InputStream if no files are specified.
	 * @param stdout
	 *            An OutputStream. The output of the command is written to this
	 *            OutputStream.
	 * 
	 * @throws SplitException
	 *             If the file(s) specified do not exist or are unreadable.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws SplitException {

		if (args == null || args.length == 0) {
			if (stdin == null) {
				throw new SplitException("Null Pointer Exception");
			}
		} else {
			int lines = -1;
			String file = "", prefix = "", bytes = "";
			boolean hasFlag = false;
			for (int i = 0; i < args.length; i++) {
				try {
					if(args[i].length() == 0) {
						throw new SplitException("can't have empty argument");
					}
					if (args[i].equals("-b")) {
						hasFlag = true;
						bytes = args[i + 1];
					} else if (args[i].equals("-l")) {
						hasFlag = true;
						lines = Integer.parseInt(args[i + 1]);
					} else if(!hasFlag){
						if(file.length() != 0) {
							throw new SplitException("can't split more than 1 file!");
						}
						file = args[i];
						hasFlag = false;
					}
				} catch (NumberFormatException exNum) {
					throw new SplitException(args[i + 1] + ": invalid number of lines: ");
				} catch (ArrayIndexOutOfBoundsException exArr) {
					throw new SplitException("option requires an argument -- 'l'");
				}
			}
			splitFile(file, prefix, lines, bytes);

		}
	}

	/**
	 * Splits a file into several files by the number of lines or bytes. 
	 * The output files will be named starting with the given prefix. 
	 * @param file String of the source filepath
	 * @param prefix String of prefix for the output files
	 * @param lines Int of the number of lines to split the file by
	 * @param bytes String of the number of lines to split the file by
	 * @throws SplitException if more than 1 line and byte is specified.
	 */
	private void splitFile(String file, String prefix, int lines, String bytes) throws SplitException {
		String outputPrefix = prefix;
		int numLines = lines;
		if (numLines != -1 && bytes.length() != 0) {
			throw new SplitException("cannot split in more than one way");
		}
		
		if (outputPrefix.length() == 0) {
			outputPrefix = "x";
		}
		if (bytes.length() == 0) {
			if(numLines == -1) {
				numLines = 1000;
			}
			splitFileByLines(file, outputPrefix, numLines);
		} else {
			splitFileByBytes(file, outputPrefix, bytes);
		}
	}

	/**
	 * Takes in the current counter for naming output files and returns the next lexicographical name. 
	 * @param currCounter String of current counter
	 * @return String of the next counter for the file name
	 */
	private String getNextName(String currCounter) {
		char[] name = currCounter.toCharArray();
		String newCounter;
		boolean hasIncremented = false;
		for(int i = name.length; i > 0; i --) {
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
	public void splitFileByLines(String file, String prefix, int lines) throws SplitException {
		if(lines == 0) {
			throw new SplitException(lines + ": invalid number of lines");
		}
		Path path = getAbsolutePath(file);
		checkIfFileIsReadable(path);
		String filePath = path.toString();
		
		String line, currCounter = "aa";
		int count = 1;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			BufferedWriter writer = new BufferedWriter(new FileWriter(prefix + currCounter));
			line = reader.readLine();
			writer.write(line);
			while ((line = reader.readLine()) != null) {
				if (count == lines) {
					writer.flush();
					writer.close();
					currCounter = getNextName(currCounter);
					writer = new BufferedWriter(new FileWriter(prefix + currCounter));
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
			throw new SplitException("IOException caught");
		}
	}

	@Override
	public void splitFileByBytes(String file, String prefix, String bytes) throws SplitException {
		int numBytes = getBytes(bytes);
		char[] buffer = new char[numBytes];
		Path path = getAbsolutePath(file);
		
		checkIfFileIsReadable(path);
		
		String filePath = path.toString();
		String currCounter = "aa";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			BufferedWriter writer = new BufferedWriter(new FileWriter(prefix + currCounter));
			while ((count = reader.read(buffer, 0, numBytes)) != -1) {
				writer.write(buffer,0,count);
				writer.flush();
				writer.close();
				currCounter = getNextName(currCounter);
				writer = new BufferedWriter(new FileWriter(prefix + currCounter));
				buffer = new char[numBytes];
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException exIO) {
			throw new SplitException("IOException caught");
		}
	}

	/**
	 * Gets the number of bytes by multiplying with the appended letter
	 * @param bytes String of the number of bytes e.g (100k, 100m)
	 * @return
	 * @throws SplitException if invalid string of bytes
	 */
	private int getBytes(String bytes) throws SplitException {
		int numBytes;
		try {
			numBytes = Integer.parseInt(bytes);
		}catch(NumberFormatException ne) {
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
			} catch (NumberFormatException exNum) {
				throw new SplitException(bytes + ": invalid number of bytes");
			}
		}
			
		if(numBytes == 0) {
			throw new SplitException(bytes + ": invalid number of bytes");
		}
		return numBytes;
	}
	
	/**
	 * gets the absolute filepath of a file
	 * @param file String of file name or file path
	 * @return Path of file path
	 * @throws SplitException
	 */
	Path getAbsolutePath(String file) throws SplitException {
		try {
			Path filePathB = Paths.get(file);
			if (!filePathB.isAbsolute()) {
				filePathB = Paths.get(Environment.currentDirectory).resolve(file);
			}
			return filePathB;
		}catch(InvalidPathException exPath) {
			throw new SplitException("invalid file: " + file);
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
	boolean checkIfFileIsReadable(Path filePath) throws SplitException {

		if (Files.isDirectory(filePath)) {
			throw new SplitException("This is a directory");
		}
		if (!Files.exists(filePath)) {
			throw new SplitException(
					"cannot open '" + filePath.toString() + "' for reading: No such file or directory");
		}
		if (Files.isReadable(filePath)) {
			return true;
		} else {
			throw new SplitException("Could not read file");
		}
	}
}
