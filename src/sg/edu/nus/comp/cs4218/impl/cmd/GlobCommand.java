package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;
import sg.edu.nus.comp.cs4218.impl.commons.OSValidator;

/**
 * A glob command is used to evaluate wildcards specified in the path to a 
 * valid path in the system by replacing all the unquoted asterisk symbols 
 * in specified path by some (possibly empty) sequences of non-slash characters.
 */
public class GlobCommand {
	
	/**
	 * Returns all the paths to existing files and directories such that these 
	 * paths can be obtained by replacing all the unquoted asterisk symbols in 
	 * specified path by some (possibly empty) sequences of non-slash characters.
	 * If no such path exist, the specified path is return without changes.
	 * 
	 * @param fileName
	 * 			  String of the file path.
	 * 
	 * @throws IOException
	 *            If the specified path is null.
	 */
	public String[] evaluate(String fileName) throws AbstractApplicationException, ShellException {
		if (fileName == null) {
			throw new ShellException("Null Pointer Exception");
		}
		if (fileName.isEmpty()) {
			return new String[] {fileName};
		}
		
		//get file separator
		String separatorRegex;
		if (OSValidator.isWindows()) {
			separatorRegex = "/|\\\\";
		} else {
			separatorRegex = "/";
		}
		
		String[] splitedDir = fileName.split(separatorRegex);
		Vector<String> dirList = new Vector<String>();
		
		//set first directory
		if (FileUtil.isAbsolute(fileName)) {
			if(OSValidator.isWindows()) {
				dirList.add(splitedDir[0]);
			} else {
				dirList.add("");
			}
		} else {
			dirList.add(Environment.currentDirectory);
			dirList = getMatchedDirs(dirList, splitedDir[0]);
			for (int i = 0; i < dirList.size(); i++) {
				dirList.set(i, dirList.get(i).replace(Environment.currentDirectory + File.separator, ""));
			}
		}
		
		//append directories to path
		for (int i = 1; i < splitedDir.length; i++) {
			dirList = getMatchedDirs(dirList, splitedDir[i]);
		}

		//check for directory only
		if (fileName.matches(".*" + separatorRegex + "$")) {
			removeFilesFromList(dirList);
		}
		
		if (dirList.isEmpty() ) {
			return new String[] {fileName};
		} else {
			return dirList.toArray(new String[dirList.size()]);
		}
	}
	
	/**
	 * Remove all paths in the list that is not a folder.
	 * 
	 * @param dirList
	 * 			  Vector of string containing the file paths.
	 */
	private void removeFilesFromList(Vector<String> dirList) {
		for (int i = dirList.size() - 1; i >= 0; i--) {
			if (!FileUtil.isDirectory(dirList.get(i))) {
				dirList.remove(i);
			}
		}
	}
	
	/**
	 * Return all paths that matches the next directory when append with the
	 * initial list of paths.
	 * 
	 * @param dirList
	 * 			  Vector of string containing the file paths.
	 * @param nextDir
	 * 			  String of the file/folder to append.
	 */
	private Vector<String> getMatchedDirs(Vector<String> dirList, String nextDir) {
		Vector<String> newList = new Vector<String>();
		
		for (int i = 0; i < dirList.size(); i++) {
			if (nextDir.isEmpty()) {
				newList.add(dirList.get(i));
				continue;
			}
			
			if (nextDir.contains("*")) {
				appendMatchedPath(newList, dirList.get(i), nextDir);
			} else {
				appendPath(newList, dirList.get(i), nextDir);
			}
		}
		
		return newList;
	}
	
	/**
	 * Evaluate the wildcard and add new paths that matches the
	 * content of the parent directory to the specified list 
	 * if it is not hidden.
	 * 
	 * @param paths
	 * 			  Vector of string used to append new paths to.
	 * @param parent
	 * 			  String of the parent directory.
	 * @param wildCardName
	 * 			  String of file or folder in the parent directory.
	 */
	private void appendMatchedPath(Vector<String> paths, String parent, String wildCardName) {
		String regex = wildCardName.replace("*",".*?");
		String[] filesInDir;
		try {
			filesInDir = FileUtil.getFolderContent(parent);
		} catch (IOException e) {
			return;
		}
		for (int k = 0; k < filesInDir.length; k++) {
			if (filesInDir[k].matches(regex)) {
				appendPath(paths, parent, filesInDir[k]);
			}
		}
	}
	
	/**
	 * Add new path is in the parent directory
	 * to the specified list if it is not hidden.
	 * 
	 * @param paths
	 * 			  Vector of string used to append new paths to.
	 * @param parent
	 * 			  String of the parent directory.
	 * @param wildCardName
	 * 			  String of file or folder in the parent directory.
	 */
	private void appendPath(Vector<String> paths, String parent, String current) {
		File file;
		try {
			file = FileUtil.getFileOrDirectoryFromPath(parent + File.separator + current);
		} catch (IOException e) {
			return;
		}
		
		if (!file.isHidden()) {
			paths.add(parent + File.separator + current);
		}
	}
}
