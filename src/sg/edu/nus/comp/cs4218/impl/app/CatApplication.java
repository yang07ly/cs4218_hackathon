package sg.edu.nus.comp.cs4218.impl.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.CatException;

/**
 * The cat command concatenates the content of given files and prints on the
 * standard output.
 * 
 * <p>
 * <b>Command format:</b> <code>cat [FILE]...</code>
 * <dl>
 * <dt>FILE</dt>
 * <dd>the name of the file(s). If no files are specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public class CatApplication implements Application {

	/**
	 * Runs the cat application with the specified arguments.
	 * 
	 * @param args
	 *            Array of arguments for the application. Each array element is the
	 *            path to a file. If no files are specified stdin is used.
	 * @param stdin
	 *            An InputStream. The input for the command is read from this
	 *            InputStream if no files are specified.
	 * @param stdout
	 *            An OutputStream. The output of the command is written to this
	 *            OutputStream.
	 * 
	 * @throws CatException
	 *             If the file(s) specified do not exist or are unreadable.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws CatException {
		if (args == null || args.length == 0) {
			catInputStream(stdin, stdout);
		} else {
			catFiles(args, stdin, stdout);
		}
	}

	/**
	 * Concatenates input from multiple files and/or inputstream
	 * 
	 * @param args
	 *            string array of files
	 * @param stdin
	 *            inputstream to concatenate
	 * @param stdout
	 *            outputstream to write output to
	 * @throws CatException
	 */
	private void catFiles(String[] args, InputStream stdin, OutputStream stdout) throws CatException {
		boolean hasInputStream = false;
		int numOfFiles = args.length;
		if (numOfFiles > 0) {
			Path filePath, currentDir = Paths.get(Environment.currentDirectory);
			for (int i = 0; i < numOfFiles; i++) {
				try {
					if (i > 0) {
						stdout.write("\n".getBytes());
					}
					if (args[i].equals("-")) {
						if (!hasInputStream) {
							catInputStream(stdin, stdout);
							hasInputStream = true;
						}
						continue;
					}
					filePath = currentDir.resolve(args[i]);
					checkIfFileIsReadable(filePath, args[i]);
					stdout.write(Files.readAllBytes(filePath));
				} catch (CatException catE) {
					if (numOfFiles == 1) {
						throw catE;
					}
					try {
						String message = catE.getMessage();
						stdout.write(message.getBytes());
					} catch (IOException e) {
						throw new CatException("Could not write to output stream");
					}
				} catch (IOException e) {
					throw new CatException("Could not write to output stream");
				} catch (InvalidPathException pathE) {
					throw new CatException(args[i] + ": No such file or directory");
				}
			}
		}
	}

	/**
	 * Concatenates input inputstream
	 * 
	 * @param stdin
	 *            inputstream to concatenate
	 * @param stdout
	 *            outputstream to write output to
	 * @throws CatException
	 */
	private void catInputStream(InputStream stdin, OutputStream stdout) throws CatException {
		if (stdin == null || stdout == null) {
			throw new CatException("Null Pointer Exception");
		}
		try {
			int intCount;
			while ((intCount = stdin.read()) != -1) {
				stdout.write(intCount);
			}
		} catch (Exception exIO) {
			throw new CatException("Exception Caught");
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
	boolean checkIfFileIsReadable(Path filePath, String file) throws CatException {
		if (Files.isDirectory(filePath)) {
			throw new CatException(file + ": Is a directory");
		}
		if (Files.exists(filePath) && Files.isReadable(filePath)) {
			return true;
		} else {
			throw new CatException(file + ": No such file or directory");
		}
	}
}
