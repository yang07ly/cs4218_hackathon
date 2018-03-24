package sg.edu.nus.comp.cs4218.impl.app;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class LsExtension {
	private LsExtension() {
	};

	/**
	 * Return formatted name of the accessible file/folder regardless of OS for
	 * display. "''" is added if name contain space or *.
	 * 
	 * @param dirName
	 *            String of the accessible file/folder path name.
	 * @return String The formatted name of the file/folder for display.
	 */
	public static String getFormattedDirName(String dirName) {
		Path osDepPathName = Paths.get(dirName);
		if (dirName.matches("^[^\\s*]*$")) {
			return osDepPathName.toString();
		} else {
			return "'" + osDepPathName.toString() + "'";
		}
	}

	/**
	 * Return the file of the specified file/folder name.
	 * 
	 * @param fileName
	 *            String of the file/folder path name.
	 * @param pathToFile
	 *            String of the containing folder of the specified file/folder path
	 *            name.
	 * @return File The file of the file/folder.
	 */
	public static File getFileFromPath(String fileName, String pathToFile) {
		File dir = new File(fileName);
		if (!dir.isAbsolute()) {
			dir = new File(pathToFile + "/" + fileName);
		}
		return dir;
	}
}
