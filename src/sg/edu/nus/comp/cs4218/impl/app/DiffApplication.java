package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.app.DiffInterface;
import sg.edu.nus.comp.cs4218.exception.DiffException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;

public class DiffApplication implements DiffInterface {

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws DiffException {
		if (args == null || args.length == 0) {
			throw new DiffException("No files specified");
		} else {
			if (args.length > 2) {
				throw new DiffException("More than 2 files specified");
			}
			Vector<String> files = new Vector<String>();
			Vector<String> directories = new Vector<String>();
			boolean[] flags = new boolean[3];
			boolean[] hasFilesDirStream = new boolean[3];
			DiffExtension.getArguments(args, flags, hasFilesDirStream, files, directories);
			String[] allFiles = files.toArray(new String[files.size()]);
			String[] allDir = directories.toArray(new String[directories.size()]);
			if (hasFilesDirStream[0]) {
				if (hasFilesDirStream[1]) {
					diffDirAndFile(allDir[0], allFiles[0], flags[0], flags[1], flags[2]);
				} else if (hasFilesDirStream[2]) {
					diffFileAndStdin(allFiles[0], stdin, flags[0], flags[1], flags[2]);
				} else {
					diffTwoFiles(allFiles[0], allFiles[1], flags[0], flags[1], flags[2]);
				}
			} else if (hasFilesDirStream[1]) {
				diffTwoDir(allDir[0], allDir[1], flags[0], flags[1], flags[2]);
			}

		}
	}

	public String diffDirAndFile(String folder, String fileNameB, Boolean isShowSame, Boolean isNoBlank,
			Boolean isSimple) throws DiffException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String diffTwoFiles(String fileNameA, String fileNameB, Boolean isShowSame, Boolean isNoBlank,
			Boolean isSimple) throws DiffException {
		try {
			File fileA = FileUtil.getFileFromPath(fileNameA);
			File fileB = FileUtil.getFileFromPath(fileNameB);
			BufferedReader readerA = new BufferedReader(new FileReader(fileA));
			BufferedReader readerB = new BufferedReader(new FileReader(fileB));
			String message = diffFiles(fileNameA, fileNameB, readerA, readerB, isShowSame, isNoBlank, isSimple);
			readerA.close();
			readerB.close();
			return message;
		} catch (IOException e) {
			throw new DiffException(e.getMessage());
		}
	}

	private String diffFiles(String fileNameA, String fileNameB, BufferedReader readerA, BufferedReader readerB, Boolean isShowSame, Boolean isNoBlank,
			Boolean isSimple) throws DiffException {
		HashSet<String> setA = new HashSet<String>();
		HashSet<String> setB = new HashSet<String>();
		Vector<String> linesOfA = new Vector<String>();
		Vector<String> linesOfB = new Vector<String>();
		DiffExtension.preProcessLines(readerA, setA, linesOfA, isNoBlank);
		DiffExtension.preProcessLines(readerB, setB, linesOfB, isNoBlank);
		StringBuilder stringBuilder = new StringBuilder();
		Boolean isSame = true;
		for(int i = 0; i < linesOfA.size(); i++) {
			String difference = null;
			if(!setB.contains(linesOfA.get(i))) {
				difference =  "< " + linesOfA.get(i) + "\n";
				stringBuilder.append(difference);
				isSame = false;
			}
		}
		for(int i = 0; i < linesOfB.size(); i++) {
			String difference = null;
			if(!setA.contains(linesOfB.get(i))) {
				difference =  "> " + linesOfB.get(i) + "\n";
				stringBuilder.append(difference);
				isSame = false;
			}
		}
		String message = new String(stringBuilder);
		if(isShowSame && isSame) {
			message = "Files " + fileNameA + " and " + fileNameB + " are identical\n";
		}else if(isSimple && !isSame){
			message = "Files " + fileNameA + " and " + fileNameB + " differ\n";
		}else {
			message = new String(stringBuilder);
		}
		return message;
	}

	@Override
	public String diffTwoDir(String folderA, String folderB, Boolean isShowSame, Boolean isNoBlank, Boolean isSimple)
			throws DiffException {
		// TODO Auto-generated method stub
		return null;
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
