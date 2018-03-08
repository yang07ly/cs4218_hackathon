package sg.edu.nus.comp.cs4218.impl.commons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.Environment;

/**
 * Provide File functionalities to required classes.
 */
public final class FileUtil {
	
	private FileUtil() {}
	
	/**
	 * Returns the File of the specified path and throws exception if the file
	 * cannot be found or is a directory.
	 * 
	 * @param fileName
	 * 			  String of the file path.
	 * 
	 * @throws IOException
	 *            If the file(s) specified do not exist, is unreadable or 
	 *            is a directory.
	 */
	public static File getFileFromPath(String fileName) 
			throws IOException {
		Path filePath;
		Path currentDir = Paths.get(Environment.currentDirectory);
		try {
			filePath = currentDir.resolve(fileName);
		} catch (InvalidPathException e) {
			throw new IOException(fileName + ": No such file or directory");
		}

		if (fileName.isEmpty() || !Files.exists(filePath) || !Files.isReadable(filePath)) {
			throw new IOException(fileName + ": No such file or directory");
		}

		if (Files.isDirectory(filePath)) {
			throw new IOException(fileName + ": Is a directory");
		}
		return filePath.toFile();
	}
}
