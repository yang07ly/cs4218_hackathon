package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.app.DiffInterface;
import sg.edu.nus.comp.cs4218.exception.DiffException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;

public class DiffApplication implements DiffInterface {
	String newLine = System.getProperty("line.separator");

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws DiffException {
		if (args == null || args.length == 0) {
			throw new DiffException("No files specified");
		} else {
			Vector<String> files = new Vector<String>();
			Vector<String> directories = new Vector<String>();
			boolean[] flags = new boolean[3];
			boolean[] hasFilesDirStream = new boolean[3];
			DiffExtension.getArguments(args, flags, hasFilesDirStream, files, directories);
			if ((files.size() + directories.size()) != 2) {
				throw new DiffException("requires 2 files to be specified");
			}
			String[] allFiles = files.toArray(new String[files.size()]);
			String[] allDir = directories.toArray(new String[directories.size()]);
			String message = "";
			if (hasFilesDirStream[0]) {
				if (hasFilesDirStream[1]) {
					message = diffDirAndFile(allDir[0], allFiles[0], flags[0], flags[1], flags[2]);
				} else if (hasFilesDirStream[2]) {
					message = diffFileAndStdin(allFiles[0], stdin, flags[0], flags[1], flags[2]);
				} else {
					message = diffTwoFiles(allFiles[0], allFiles[1], flags[0], flags[1], flags[2]);
				}
			} else if (hasFilesDirStream[1]) {
				message = diffTwoDir(allDir[0], allDir[1], flags[0], flags[1], flags[2]);
			}
			try {
				stdout.write(message.getBytes());
			} catch (IOException e) {
				throw new DiffException(e.getMessage());
			}

		}
	}

	/**
	 * compares two files in the current directory and another directory of the same
	 * name
	 * 
	 * @param folder
	 *            directory of the file to compare
	 * @param fileNameB
	 *            filename of the files to compare
	 * @param isShowSame
	 *            boolean flag for showing if files are identical
	 * @param isNoBlank
	 *            boolean flag for skipping blank lines
	 * @param isSimpleboolean
	 *            flag for simplifying output if files are different
	 * @return message output
	 * @throws DiffException
	 */
	public String diffDirAndFile(String folder, String fileNameB, Boolean isShowSame, Boolean isNoBlank,
			Boolean isSimple) throws DiffException {
		String fileNameA = folder + File.separator + fileNameB;
		return diffTwoFiles(fileNameA, fileNameB, isShowSame, isNoBlank, isSimple);
	}

	@Override
	public String diffTwoFiles(String fileNameA, String fileNameB, Boolean isShowSame, Boolean isNoBlank,
			Boolean isSimple) throws DiffException {
		try {
			byte[] byteArrayA = FileUtil.readAllBytes(fileNameA);
			byte[] byteArrayB = FileUtil.readAllBytes(fileNameB);
			DiffExtension.checkIfBinaryFile(fileNameA, byteArrayA);
			DiffExtension.checkIfBinaryFile(fileNameB, byteArrayB);
			byte[] newLine = System.getProperty("line.separator").getBytes();
			ByteArrayOutputStream outputStreamA = new ByteArrayOutputStream();
			ByteArrayOutputStream outputStreamB = new ByteArrayOutputStream();
			outputStreamA.write(byteArrayA);
			outputStreamA.write(newLine);
			outputStreamB.write(byteArrayB);
			outputStreamB.write(newLine);
			byteArrayA = outputStreamA.toByteArray();
			byteArrayB = outputStreamB.toByteArray();
			BufferedReader readerA = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(byteArrayA)));
			BufferedReader readerB = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(byteArrayB)));
			String message = diffFiles(fileNameA, fileNameB, readerA, readerB, isShowSame, isNoBlank, isSimple);
			readerA.close();
			readerB.close();
			return message;
		} catch (IOException e) {
			throw new DiffException(e.getMessage());
		}
	}

	/**
	 * compares two files and output the difference, if any
	 * 
	 * @param fileNameA
	 *            filename of first file
	 * @param fileNameB
	 *            filename of second file
	 * @param readerA
	 *            reader of first file
	 * @param readerB
	 *            reader of second file
	 * @param isShowSame
	 *            boolean flag for showing if files are identical
	 * @param isNoBlank
	 *            boolean flag for skipping blank lines
	 * @param isSimpleboolean
	 *            flag for simplifying output if files are different
	 * @return message output
	 * @throws DiffException
	 */
	private String diffFiles(String fileNameA, String fileNameB, BufferedReader readerA, BufferedReader readerB,
			Boolean isShowSame, Boolean isNoBlank, Boolean isSimple) throws DiffException {
		HashSet<String> setA = new HashSet<String>();
		HashSet<String> setB = new HashSet<String>();
		Vector<String> linesOfA = new Vector<String>();
		Vector<String> linesOfB = new Vector<String>();
		DiffExtension.preProcessLines(readerA, setA, linesOfA, isNoBlank);
		DiffExtension.preProcessLines(readerB, setB, linesOfB, isNoBlank);
		StringBuilder stringBuilder = new StringBuilder();
		Boolean isSame = true, hasLines = false;
		for (int i = 0; i < linesOfA.size(); i++) {
			String difference = null;
			if (!setB.contains(linesOfA.get(i))) {
				difference = "< " + linesOfA.get(i);
				if (hasLines) {
					stringBuilder.append(newLine);
				}
				stringBuilder.append(difference);
				isSame = false;
				hasLines = true;
			}
		}
		for (int i = 0; i < linesOfB.size(); i++) {
			String difference = null;
			if (!setA.contains(linesOfB.get(i))) {
				difference = "> " + linesOfB.get(i);
				if (hasLines) {
					stringBuilder.append(newLine);
				}
				stringBuilder.append(difference);
				isSame = false;
				hasLines = true;
			}
		}
		String message = new String(stringBuilder);
		if (isShowSame && isSame) {
			message = "Files " + fileNameA + " and " + fileNameB + " are identical";
		} else if (isSimple && !isSame) {
			message = "Files " + fileNameA + " and " + fileNameB + " differ";
		}
		return message;
	}

	@Override
	public String diffTwoDir(String folderA, String folderB, Boolean isShowSame, Boolean isNoBlank, Boolean isSimple)
			throws DiffException {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			File dirA = FileUtil.getFileOrDirectoryFromPath(folderA),
					dirB = FileUtil.getFileOrDirectoryFromPath(folderB);
			String[] dirAFiles = dirA.list(), dirBFiles = dirB.list();
			String pathA = folderA + File.separator, pathB = folderB + File.separator;
			boolean hasLines = false;
			List<String> listA = Arrays.asList(dirAFiles), listB = Arrays.asList(dirBFiles);
			for (int i = 0; i < dirAFiles.length; i++) {
				String message = "";
				if (listB.contains(dirAFiles[i])) {
					String filePathA = pathA + dirAFiles[i], filePathB = pathB + dirAFiles[i];
					if (FileUtil.isDirectory(dirAFiles[i])) {
						hasLines = commonSubDir(stringBuilder, hasLines, filePathA, filePathB);
					} else {
						message = diffTwoFiles(filePathA, filePathB, isShowSame, isNoBlank, isSimple);
						if (!message.isEmpty()) {
							if (hasLines) {
								stringBuilder.append(newLine);
							}
							hasLines = true;
						}
						stringBuilder.append(message);
					}
				} else {
					if (hasLines) {
						stringBuilder.append(newLine);
					}
					stringBuilder.append("Only in " + folderA + ": " + dirAFiles[i]);
					hasLines = true;
				}
			}
			for (int i = 0; i < dirBFiles.length; i++) {
				String message = "";
				if (!listA.contains(dirBFiles[i])) {
					if (hasLines) {
						stringBuilder.append(newLine);
					}
					message = "Only in " + folderB + ": " + dirBFiles[i];
					stringBuilder.append(message);
					hasLines = true;
				}
			}
			return new String(stringBuilder);
		} catch (IOException e) {
			throw new DiffException(e.getMessage());
		}
	}

	/**
	 * appends the msg for common subdirectories
	 * 
	 * @param stringBuilder
	 *            string builder for message
	 * @param hasLines
	 *            boolean variable if there is msg in stringbuilder
	 * @param filePathA
	 *            path of first file
	 * @param filePathB
	 *            path of second file
	 * @return boolean variable if there is msg in in stringbuilder
	 */
	private boolean commonSubDir(StringBuilder stringBuilder, boolean hasLines, String filePathA, String filePathB) {
		if (hasLines) {
			stringBuilder.append(newLine);
		}
		stringBuilder.append("Common subdirectories: " + filePathA + " and " + filePathB);
		return true;
	}

	@Override
	public String diffFileAndStdin(String fileName, InputStream stdin, Boolean isShowSame, Boolean isNoBlank,
			Boolean isSimple) throws DiffException {
		try {
			File file = FileUtil.getFileFromPath(fileName);
			BufferedReader readerA = new BufferedReader(new FileReader(file));
			BufferedReader readerB = new BufferedReader(new InputStreamReader(stdin));
			String message = diffFiles(fileName, "-", readerA, readerB, isShowSame, isNoBlank, isSimple);
			readerA.close();
			readerB.close();
			return message;
		} catch (IOException e) {
			throw new DiffException(e.getMessage());
		}
	}

}
