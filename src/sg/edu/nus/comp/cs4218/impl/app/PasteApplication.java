package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.app.PasteInterface;
import sg.edu.nus.comp.cs4218.exception.PasteException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class PasteApplication implements PasteInterface {

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws PasteException {
		if (args == null || args.length == 0) {
			throw new PasteException("No files specified");
		} else {
			Vector<String> files = new Vector<String>();
			boolean[] flags = new boolean[2];
			getArguments(args, flags, files);
			String[] allFiles = files.toArray(new String[files.size()]);
			try {
				if (flags[0]) { // has files
					if (flags[1]) { // has stream
						stdout.write(mergeFileAndStdin(stdin, allFiles).getBytes());
					} else {
						stdout.write(mergeFile(allFiles).getBytes());
					}
				} else {
					stdout.write(mergeStdin(stdin).getBytes());
				}
			} catch (IOException e) {
				throw new PasteException(e.getMessage());
			}

		}
	}

	/**
	 * extracts flags and arguments from cmd string
	 * 
	 * @param args
	 *            string array of arguments
	 * @param flags
	 *            boolean array to store the flags
	 * @param files
	 *            Vector of string to store the files
	 * @return Vector of string for the files
	 * @throws PasteException
	 */
	private Vector<String> getArguments(String[] args, boolean flags[], Vector<String> files) throws PasteException {
		for (int i = 0; i < args.length; i++) {
			if (args[i].isEmpty()) {
				throw new PasteException("'" + args[i] + "': No such file or directory");
			} else {
				if (args[i].equals("-")) {
					flags[1] = true;
				} else {
					flags[0] = true;
					files.add(args[i]);
				}
			}
		}
		return files;
	}

	@Override
	public String mergeStdin(InputStream stdin) throws PasteException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
		String message = merge(reader);
		try {
			reader.close();
		} catch (IOException e) {
			throw new PasteException(e.getMessage());
		}
		return message;
	}

	@Override
	public String mergeFile(String... fileName) throws PasteException {
		BufferedReader[] readers = new BufferedReader[fileName.length];
		for (int i = 0; i < fileName.length; i++) {
			try {
				File file = FileUtil.getFileFromPath(fileName[i]);
				readers[i] = new BufferedReader(new FileReader(file));
			} catch (IOException e) {
				throw new PasteException(e.getMessage());
			}
		}
		String message = merge(readers);
		for (int i = 0; i < readers.length; i++) {
			try {
				readers[i].close();
			} catch (IOException e) {
				throw new PasteException(e.getMessage());
			}
		}
		return message;
	}

	/**
	 * merge all contents from all buffered readers
	 * 
	 * @param readers
	 *            buffered readers to merge content of
	 * @return the merged string
	 * @throws PasteException
	 */
	private String merge(BufferedReader... readers) throws PasteException {
		try {
			boolean hasLines = true;
			StringBuilder stringBuilder = new StringBuilder();
			int numLines = 0;
			while (hasLines) {
				hasLines = false;
				StringBuilder lineBuilder = new StringBuilder();
				for (int i = 0; i < readers.length; i++) {
					String line = readers[i].readLine();
					if (line != null) {
						hasLines = true;
						if (i == 0) {
							lineBuilder.append(line);
						} else {
							line = "\t" + line;
							lineBuilder.append(line);
						}
					}
				}
				if (numLines != 0 && hasLines) {
					stringBuilder.append(OSUtil.NEWLINE);
				}
				numLines++;
				stringBuilder.append(new String(lineBuilder));
			}
			return new String(stringBuilder);
		} catch (IOException e) {
			throw new PasteException(e.getMessage());
		}
	}

	@Override
	public String mergeFileAndStdin(InputStream stdin, String... fileName) throws PasteException {
		BufferedReader[] readers = new BufferedReader[fileName.length + 1];
		for (int i = 0; i < fileName.length; i++) {
			try {
				File file = FileUtil.getFileFromPath(fileName[i]);
				readers[i] = new BufferedReader(new FileReader(file));
			} catch (IOException e) {
				throw new PasteException(e.getMessage());
			}
		}
		readers[readers.length - 1] = new BufferedReader(new InputStreamReader(stdin));
		String message = merge(readers);
		for (int i = 0; i < readers.length; i++) {
			try {
				readers[i].close();
			} catch (IOException e) {
				throw new PasteException(e.getMessage());
			}
		}
		return message;
	}

}
