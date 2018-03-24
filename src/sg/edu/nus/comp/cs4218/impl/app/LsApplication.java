package sg.edu.nus.comp.cs4218.impl.app;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.LsInterface;
import sg.edu.nus.comp.cs4218.exception.LsException;

/**
 * The ls command list information about files.
 * 
 * <p>
 * <b>Command format:</b> <code>ls [-d][FOLDER][-R]</code>
 * <dl>
 * <dt>FOLDER</dt>
 * <dd>the name the folder or folders. If no folders are specified, list files 
 * for current directory. Hidden files should not be listed.</dd>
 * <dt><code>-d</code> - List the folders only</dt>
 * <dt><code>-R</code> - List files and subfolders recursively</dt>
 * </dl>
 * </p>
 */
public class LsApplication implements LsInterface {
	
	private static final int DOPTION = 0;
	private static final int ROPTION = 1;
	/**
	 * Runs the ls application with the specified arguments.
	 * 
	 * @param args
	 *            Array of arguments for the application. Each array element is
	 *            the path to a file or the option that changes the behavior of
	 *            the application.
	 * @param stdin
	 *            An InputStream, not used.
	 * @param stdout
	 *            An OutputStream. Elements of args will be output to stdout,
	 *            separated by a space character.
	 * 
	 * @throws LsException
	 *             If an I/O exception occurs.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) 
			throws LsException {
		if (stdout == null) {
			throw new LsException("Null Pointer Exception");
		}
		
		boolean[] options = new boolean[2];
		Vector<String> paths = new Vector<String>();
		setOptionsAndFiles(options, paths, args);
		
		String output = listFolderContent(options[DOPTION], options[ROPTION], paths.toArray(new String[paths.size()]));
		try {
			stdout.write(output.getBytes());
		} catch (IOException e) {
			throw new LsException("IOException");
		}
	}
	
	/**
	 * Return the string listing the folder content of 
	 * the specified folders. If no folder names are 
	 * specified, list the content of the current 
	 * folder.
	 * @param isFoldersOnly 
	 * 				Boolean option to list only the folders.
	 * @param isRecursive 
	 * 				Boolean option to recursively 
	 * 				list the folder contents (traversing through all
	 * 				folders inside the specified folder).
	 * @param folderName 
	 * 				Array of String of folder names.
	 * @throws LsException
	 * 				If an invalid option is specified
	 */
	@Override
	public String listFolderContent(Boolean isFoldersOnly, Boolean isRecursive, String... folderName) 
			throws LsException {
		String outputStr = "";
		Vector<String> validPaths = getValidPaths(folderName);
		Vector<String> files = new Vector<String>();
		Vector<String> folders = new Vector<String>();
		sortFileAndDirectory(validPaths, files, folders);
		if (isFoldersOnly) {
			outputStr += printFiles(validPaths, Environment.currentDirectory);
		} else {
			outputStr += printFiles(files, Environment.currentDirectory);
			if (!files.isEmpty() && !folders.isEmpty()) {
				outputStr += "\n";
			}
			if (files.isEmpty() && !isRecursive && folders.size() == 1) {
				outputStr += printFilesInFolder(folders.get(0), isRecursive);
			} else {
				outputStr += printFolders(folders, isRecursive);
			}
		}
		return outputStr.trim();
	}
	
	/**
	 * Scan args for ls options and file/folder paths
	 * and set it to options and paths respectively.
	 * @param options 
	 * 				Array of Boolean indicating the respective
	 * 				options specified in the args.
	 * @param paths 
	 * 				Vector of String containing the file/folder
	 * 				paths specified in the args.
	 * @param args 
	 * 				Array of String of the arguments of ls.
	 * @throws LsException
	 * 				If an invalid option is specified
	 */
	private void setOptionsAndFiles(boolean[] options, Vector<String> paths, String... args)
			throws LsException {
		if (args == null) {
			return;
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				for (int j = 1; j < args[i].length(); j++) {
					switch (args[i].charAt(j)) {
					case 'd':
						options[DOPTION] = true;
						break;
					case 'R':
						options[ROPTION] = true;
						break;
					case '-':
						if (args[i].charAt(j - 1) != '-' && j != args[i].length() - 1) {
							break;
						}
						/* falls through */
					default:
						throw new LsException("invalid option -- '" + args[i].charAt(j) + "'");
					}
				}
			} else {
				paths.add(args[i]);
			}
		}
	}
	
	/**
	 * Set existing files or folder in inputPaths into validPaths and
	 * return a String specifying the error files/folders that cannot
	 * be accessed. "." is added to validPaths if there is no inputPaths.
	 * @param inputPaths 
	 * 				Array of String of file/folder path specified in args.
	 * @return paths 
	 * 				Vector of String containing accessible files or folders.
	 * @throws LsException
	 * 				If there is/are files/folders that cannot be access.
	 */
	private Vector<String> getValidPaths(String... inputPaths)
			throws LsException {
		Vector<String> validPaths = new Vector<String>();
		
		if (inputPaths.length == 0) {
			validPaths.add(".");
			return validPaths;
		}
		
		Path filePath;
		Path currentDir = Paths.get(Environment.currentDirectory);
		for (int i = 0; i < inputPaths.length; i++) {
			try {
				filePath = currentDir.resolve(inputPaths[i]);
			} catch (InvalidPathException e) {
				throw new LsException("cannot access '" + inputPaths[i] + "': No such file or directory");
			}
			
			if (Files.exists(filePath) && !inputPaths[i].isEmpty()) {
				validPaths.add(inputPaths[i]);
			} else {
				throw new LsException("cannot access '" + inputPaths[i] + "': No such file or directory");
			}
		}
		return validPaths;
	}
	
	/**
	 * Sort the accessible files and folders in paths into files and
	 * folders vector respectively.
	 * @param paths 
	 * 				Vector of String of accessible file/folder paths.
	 * @param files
	 * 				Vector of String of accessible files to be added.
	 * @param folders
	 * 				Vector of String of accessible folders to be added.
	 */
	private void sortFileAndDirectory(Vector<String> paths, Vector<String> files, Vector<String> folders) {		
		for (int i = 0; i < paths.size(); i++) {
			File dir = getFileFromPath(paths.get(i), Environment.currentDirectory);			
			if (dir.isDirectory()) {
				folders.add(paths.get(i));
			} else {
				files.add(paths.get(i));
			}
		}
		Collections.sort(files);
		Collections.sort(folders);
	}
	
	/**
	 * Format non hidden files for display.
	 * @param files 
	 * 				Vector of String of accessible files in the same folder.
	 * @param folder 
	 * 				String of containing folders of the specified files.
	 * @return String
	 * 				Formatted files for display.
	 */
	private String printFiles(Vector<String> files, String folder) {
		String outputStr = "";
		if (files.size() == 1 && files.get(0).equals(".")) {
			return ".\n";
		}
		
		Collections.sort(files);
		for (int i = 0; i < files.size(); i++) {
			File dir = getFileFromPath(files.get(i), folder);
			if (!dir.isHidden()) {
				outputStr += getFormattedDirName(files.get(i)) + "\t";
			}
		}
		outputStr = outputStr.trim();
		if (!files.isEmpty()) {
			outputStr += "\n";
		}
		return outputStr;
	}
	
	/**
	 * Format folders for display. Include recursive folders and its contents
	 * if isRecursive is true.
	 * @param folders 
	 * 				Vector of String of accessible folders.
	 * @param isRecursive 
	 * 				Boolean indicating recursive display of folders is specified.
	 * @return String
	 * 				Formatted folders, recursive folders and its'
	 * 				content for display.
	 */
	private String printFolders(Vector<String> folders, boolean isRecursive) {
		if (folders.isEmpty()) {
			return "";
		}
		
		String outputStr = "";
		outputStr += getFormattedDirName(folders.get(0))+ ":\n";
		outputStr += printFilesInFolder(folders.get(0), isRecursive);
		for (int i = 1; i < folders.size(); i++) {
			outputStr += "\n" + getFormattedDirName(folders.get(i))+ ":\n";
			outputStr += printFilesInFolder(folders.get(i), isRecursive);
		}	
		return outputStr;
	}
	
	/**
	 * Format contents in the specified folder for display. Include 
	 * recursive folders and its contents if isRecursive is true.
	 * @param folder
	 * 				String of the accessible folder.
	 * @param isRecursive 
	 * 				Boolean indicating recursive display of folders is specified.
	 * @return String
	 * 				Formatted folder contents and it's recursive folders and its'
	 * 				content for display.
	 */
	private String printFilesInFolder(String folder, boolean isRecursive) {
		String outputStr = "";
		File dir = getFileFromPath(folder, Environment.currentDirectory);			
		String[] filesInDir = dir.list();
		outputStr += printFiles(new Vector<String>(Arrays.asList(filesInDir)), dir.getAbsolutePath());			
		
		if (isRecursive) {
			String[] dirInDir = dir.list(new FilenameFilter() {
				  @Override
				  public boolean accept(File current, String name) {
				    return new File(current, name).isDirectory();
				  }
			});
			
			Vector<String> subDirectories = new Vector<String>();
			for (int j = 0; j < dirInDir.length; j++) { 
				subDirectories.add(folder + "/" + dirInDir[j]);
			}
			if (!subDirectories.isEmpty()) {
				outputStr += "\n";
				outputStr += printFolders(subDirectories, isRecursive);
			}
		}
		return outputStr;
	}
	
	/**
	 * Return formatted name of the accessible file/folder regardless
	 * of OS for display. "''" is added if name contain space or *.
	 * @param dirName
	 * 				String of the accessible file/folder path name.
	 * @return String
	 * 				The formatted name of the file/folder for display.
	 */
	private String getFormattedDirName(String dirName) {
		Path osDepPathName = Paths.get(dirName);
		if(dirName.matches("^[^\\s*]*$")) {
			return osDepPathName.toString();
		} else {
			return "'" + osDepPathName.toString() + "'";
		}
	}
	
	/**
	 * Return the file of the specified file/folder name.
	 * @param fileName
	 * 				String of the file/folder path name.
	 * @param pathToFile
	 * 				String of the containing folder 
	 * 				of the specified file/folder path name.
	 * @return File
	 * 				The file of the file/folder.
	 */
	private File getFileFromPath(String fileName, String pathToFile) {
		File dir = new File(fileName);
		if (!dir.isAbsolute()) {
			dir = new File(pathToFile + "/" + fileName);
		}
		return dir;
	}
}