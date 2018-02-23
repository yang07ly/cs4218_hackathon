package sg.edu.nus.comp.cs4218.impl.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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
			if (stdin == null || stdout == null) {
				throw new CatException("Null Pointer Exception");
			}
			try {
				int intCount;
				while ((intCount = stdin.read()) != -1) {
					stdout.write(intCount);
				}
				stdout.write("\n".getBytes());
			} catch (Exception exIO) {
				throw new CatException("Exception Caught");
			}
		} else {

			int numOfFiles = args.length;

			if (numOfFiles > 0) {
				Path filePath;
				Path currentDir = Paths.get(Environment.currentDirectory);

				for (int i = 0; i < numOfFiles; i++) {
					try {
						filePath = currentDir.resolve(args[i]);
						checkIfFileIsReadable(filePath, args[i]);
						byte[] byteFileArray = Files.readAllBytes(filePath);
						stdout.write(byteFileArray);
						stdout.write("\n".getBytes());
					}catch(CatException catE) {
						if(numOfFiles == 1) {
							throw catE;
						}
						try {
							String message = catE.getMessage()+"\n";
							stdout.write(message.getBytes());
						} catch (IOException e) {
							throw new CatException("Could not write to output stream");
						}
					}
					catch (IOException e) {
						throw new CatException("Could not write to output stream");
					}
				}

			}
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
