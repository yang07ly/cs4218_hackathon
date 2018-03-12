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
	 * Returns the files/folders in the specified folder. Exception is thrown
	 * if the folder path do not exist or is a file.
	 * 
	 * @param fileName
	 * 			  String of the file path.
	 * 
	 * @throws IOException
	 *            If the folder specified do not exist, is unreadable or 
	 *            is a file.
	 */
	public static String[] getFolderContent(String folderName) 
			throws IOException {
		File folder = getFolderFromPath(folderName);
		return folder.list();
	}
	
	/**
	 * Returns the File class of the specified path and throws exception if the file
	 * cannot be found or is a directory.
	 * 
	 * @param fileName
	 * 			  String of the file path.
	 * 
	 * @throws IOException
	 *            If the file specified do not exist, is unreadable or 
	 *            is a directory.
	 */
	public static File getFileFromPath(String fileName) 
			throws IOException {
		File file = getFileOrDirectoryFromPath(fileName);

		if (file.isDirectory()) {
			throw new IOException(fileName + ": Is a directory");
		}
		return file;
	}
	
	/**
	 * Returns the File class of the specified path and throws exception if the folder
	 * cannot be found or is a file.
	 * 
	 * @param fileName
	 * 			  String of the folder path.
	 * 
	 * @throws IOException
	 *            If the folder specified do not exist, is unreadable or 
	 *            is a file.
	 */
	public static File getFolderFromPath(String folderName) 
			throws IOException {
		File folder = getFileOrDirectoryFromPath(folderName);

		if (!folder.isDirectory()) {
			throw new IOException(folderName + ": Is a file");
		}
		return folder;
	}
	
	/**
	 * Returns the File class of the specified path and throws exception if the file
	 * or folder cannot be found.
	 * 
	 * @param fileName
	 * 			  String of the file/folder path.
	 * 
	 * @throws IOException
	 *            If the file/folder specified do not exist or is unreadable.
	 */
	public static File getFileOrDirectoryFromPath(String fileName) 
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

		return filePath.toFile();
	}
	
	/**
	 * Returns true if the path specified is an absolute path.
	 * 
	 * @param fileName
	 * 			  String of the file/folder path.
	 */
	public static boolean isAbsolute(String fileName) {
		File file = new File(fileName);
		return file.isAbsolute();
	}
	
	/**
	 * Returns true if the path specified is a folder.
	 * 
	 * @param fileName
	 * 			  String of the file/folder path.
	 */
	public static boolean isDirectory(String fileName) {
		File file;
		try {
			file = getFileOrDirectoryFromPath(fileName);
		} catch (IOException e) {
			return false;
		}
		return file.isDirectory();
	}

	/**
	 * Reads contents of a file
	 * 
	 * @param fileName
	 * 			  String of the file/folder path.
	 */
	public static byte[] readAllBytes(String fileName) 
			throws IOException {
		Path currentDir = Paths.get(Environment.currentDirectory);
		try {
			Path filePath = currentDir.resolve(fileName);
			return Files.readAllBytes(filePath);
		} catch (Exception e) {
			throw new IOException(fileName + ": No such file or directory");
		}
	}
}
