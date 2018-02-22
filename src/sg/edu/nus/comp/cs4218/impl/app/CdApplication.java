package sg.edu.nus.comp.cs4218.impl.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.CdItf;
import sg.edu.nus.comp.cs4218.exception.CdException;

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
public class CdApplication implements CdItf {

	/**
	 * The cd command changes the current working directory.
	 * 
	 * @param args
	 *            Array of arguments for the application. Array element is
	 *            the path to a file. If there is more than one element or
	 *            path does not exist, exception will be thrown.
	 * @param stdin
	 *            An InputStream, not used.
	 * @param stdout
	 *            An OutputStream, not used.
	 * 
	 * @throws CdException
	 *             If the path specified do not exist, unreadable or is 
	 *             not a directory or if more than one argument is specified.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws CdException {
		if (args == null || args.length == 0) {
			changeToDirectory(null, new Environment());
		} else if (args.length > 1) {
			throw new CdException("too many arguments");
		} else {
			changeToDirectory(args[0], new Environment());	
		}
	}

	/**
	 * Change the environment context to a different directory. 
	 * @param path 
	 * 				String of the path to a directory
	 * @param env 
	 * 				Environment context 
	 * @throws Exception
	 * 				If the path specified do not exist, unreadable or is 
	 *             	not a directory.
	 */
	@Override
	public void changeToDirectory(String path, Environment env) throws CdException {
		if (env == null) {
			throw new CdException("Null Pointer Exception");
		}
		
		Path filePath;
		Path currentDir = Paths.get(Environment.currentDirectory);
		try {
			if (path == null || path.length() == 0) {
				filePath = currentDir.resolve(System.getProperty("user.dir"));
			} else {
				filePath = currentDir.resolve(path);
			}
		} catch (InvalidPathException e) {
			throw new CdException(path + ": No such file or directory");
		}

		if (!Files.exists(filePath)) {
			throw new CdException(path + ": No such file or directory");
		}

		if (!Files.isDirectory(filePath)) {
			throw new CdException(path + ": Not a directory");
		}

		try {
			env.currentDirectory = filePath.toFile().getCanonicalPath();
		} catch (IOException e) {
			throw new CdException("IOException");
		}
	}

}
