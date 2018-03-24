package sg.edu.nus.comp.cs4218.impl.optr;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Operator;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

/**
 * A glob operator is used to evaluate wildcards specified in the path to a
 * valid path in the system by replacing all the unquoted asterisk symbols in
 * specified path by some (possibly empty) sequences of non-slash characters.
 */
public class GlobOperator implements Operator {
	private static final String REGEX_WILDCARD = ".*?";

	/**
	 * Replace paths with wildcard with all the paths to existing files and
	 * directories such that these paths can be obtained by replacing all the
	 * unescaped asterisk symbols in specified path by some (possibly empty)
	 * sequences of non-slash characters. If no such path exist, paths with wildcard
	 * are not replaced.
	 * 
	 * @param cmd
	 *            CommandString containing the paths with wildcard.
	 * 
	 * @throws ShellException
	 *             If the input command is null.
	 */
	@Override
	public void evaluate(CommandString cmd) throws AbstractApplicationException, ShellException {
		if (cmd == null) {
			throw new ShellException("Null Pointer Exception");
		}

		int wildCardIndex;
		int iterIndex = 0;
		while ((wildCardIndex = cmd.getFirstIndexOfCharNotEscaped(iterIndex, '*')) != -1) {
			int beginIndex = getStartIndexOfGlobWord(cmd, wildCardIndex);
			int endIndex = getEndIndexOfGlobWord(cmd, wildCardIndex) + 1;

			// replace * with regex wildcard .*? and * with \*
			StringJoiner regexArg = new StringJoiner("");
			for (int i = beginIndex; i < endIndex; i++) {
				if (cmd.charAt(i) == '*' && !cmd.isCharEscaped(i)) {
					regexArg.add(REGEX_WILDCARD);
				} else {
					regexArg.add(cmd.substring(i, i + 1).toString());
				}
			}

			String[] globResult = evaluate(regexArg.toString());
			if (globResult.length == 0) {
				iterIndex = endIndex;
				continue;
			}

			Arrays.sort(globResult);

			iterIndex = beginIndex;
			cmd.removeRange(beginIndex, endIndex);
			for (int i = 0; i < globResult.length; i++) {
				cmd.insertStringAt(iterIndex, globResult[i] + " ");
				cmd.setCharEscapedRange(iterIndex, iterIndex + globResult[i].length(), true);
				iterIndex += globResult[i].length() + 1;
			}
			cmd.removeCharAt(iterIndex - 1);
		}
	}

	/**
	 * Return the start index of the glob word associated the the index of the
	 * wildcard.
	 * 
	 * @param cmd
	 *            CommandString containing the paths with wildcard.
	 * @param wildCardIndex
	 *            Integer index of the wildcard character of interest in the
	 *            CommandString.
	 */
	private int getStartIndexOfGlobWord(CommandString cmd, int wildCardIndex) {
		int index = wildCardIndex;
		while (index > 0) {
			if (cmd.charAt(index) == ' ' && !cmd.isCharEscaped(index)) {
				return index + 1;
			}
			index--;
		}
		return 0;
	}

	/**
	 * Return the end index of the glob word associated the the index of the
	 * wildcard.
	 * 
	 * @param cmd
	 *            CommandString containing the paths with wildcard.
	 * @param wildCardIndex
	 *            Integer index of the wildcard character of interest in the
	 *            CommandString.
	 */
	private int getEndIndexOfGlobWord(CommandString cmd, int wildCardIndex) {
		int index = wildCardIndex;
		while (index < cmd.length()) {
			if (cmd.charAt(index) == ' ' && !cmd.isCharEscaped(index)) {
				return index - 1;
			}
			index++;
		}
		return cmd.length() - 1;
	}

	/**
	 * Returns all the paths to existing files and directories such that these paths
	 * can be obtained by replacing all the unquoted asterisk symbols in specified
	 * path by some (possibly empty) sequences of non-slash characters. If no such
	 * path exist, am empty paths is returned.
	 * 
	 * @param fileName
	 *            String of the file path regex.
	 * @return String Array paths that matches the wildcard fileName.
	 * 
	 * @throws IOException
	 *             If the specified path is null.
	 */
	private String[] evaluate(String fileName) throws ShellException {
		if (fileName == null) {
			throw new ShellException("Null Pointer Exception");
		}
		if (fileName.isEmpty()) {
			return new String[] { fileName };
		}

		// get file separator
		String separatorRegex;
		if (OSUtil.isWindows()) {
			separatorRegex = "/|\\\\";
		} else {
			separatorRegex = "/";
		}

		String[] splitedDir = fileName.split(separatorRegex);
		Vector<String> dirList = new Vector<String>();

		// set first directory
		if (FileUtil.isAbsolute(fileName)) {
			if (OSUtil.isWindows()) {
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

		// append directories to path
		for (int i = 1; i < splitedDir.length; i++) {
			dirList = getMatchedDirs(dirList, splitedDir[i]);
		}

		// check for directory only
		if (fileName.matches(".*" + separatorRegex + "$")) {
			removeFilesFromList(dirList);
		}

		return dirList.toArray(new String[dirList.size()]);
	}

	/**
	 * Remove all paths in the list that is not a folder.
	 * 
	 * @param dirList
	 *            Vector of string containing the file paths.
	 */
	private void removeFilesFromList(Vector<String> dirList) {
		for (int i = dirList.size() - 1; i >= 0; i--) {
			if (!FileUtil.isDirectory(dirList.get(i))) {
				dirList.remove(i);
			}
		}
	}

	/**
	 * Return all paths that matches the next directory when append with the initial
	 * list of paths.
	 * 
	 * @param dirList
	 *            Vector of string containing the file paths.
	 * @param nextDir
	 *            String of the file/folder to append.
	 */
	private Vector<String> getMatchedDirs(Vector<String> dirList, String nextDir) {
		Vector<String> newList = new Vector<String>();

		for (int i = 0; i < dirList.size(); i++) {
			if (nextDir.isEmpty()) {
				newList.add(dirList.get(i));
				continue;
			}

			if (nextDir.contains(REGEX_WILDCARD)) {
				appendMatchedPath(newList, dirList.get(i), nextDir);
			} else {
				appendPath(newList, dirList.get(i), nextDir);
			}
		}

		return newList;
	}

	/**
	 * Evaluate the wildcard and add new paths that matches the content of the
	 * parent directory to the specified list if it is not hidden.
	 * 
	 * @param paths
	 *            Vector of string used to append new paths to.
	 * @param parent
	 *            String of the parent directory.
	 * @param wildCardName
	 *            String of file or folder in the parent directory.
	 */
	private void appendMatchedPath(Vector<String> paths, String parent, String wildCardName) {
		String regex = wildCardName.replace("*", "\\*").replace(".\\*?", REGEX_WILDCARD);
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
	 * Add new path is in the parent directory to the specified list if it is not
	 * hidden.
	 * 
	 * @param paths
	 *            Vector of string used to append new paths to.
	 * @param parent
	 *            String of the parent directory.
	 * @param wildCardName
	 *            String of file or folder in the parent directory.
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
