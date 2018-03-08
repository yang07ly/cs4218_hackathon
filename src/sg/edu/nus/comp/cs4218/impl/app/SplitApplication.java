package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.app.SplitInterface;
import sg.edu.nus.comp.cs4218.exception.SplitException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;

public class SplitApplication implements SplitInterface {

	private static final String FILE_NOT_FOUND = "': No such file or directory";

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws SplitException {
		String[] flags = new String[4];
		getArgs(args, flags);
		String file = flags[0], prefix = flags[1], bytes = flags[2], lineString = flags[3];
		int lines = SplitExtension.parseLines(lineString);
		if (file == null) {
			if (stdin == null) {
				throw new SplitException("Null Pointer Exception");
			}
			if (SplitExtension.isSplitByLines(bytes, lines)) {
				splitStreamByLines(stdin, prefix, lines);
			} else {
				splitStreamByBytes(stdin, prefix, bytes);
			}
		} else {
			if (SplitExtension.isSplitByLines(bytes, lines)) {
				splitFileByLines(file, prefix, lines);
			} else {
				splitFileByBytes(file, prefix, bytes);
			}
		}
	}

	/**
	 * parses user arguments and stores them in processedArgs
	 * 
	 * @param args
	 *            user arguments
	 * @param processedArgs
	 *            parsed arguments
	 * @throws SplitException
	 */
	private void getArgs(String[] args, String... processedArgs) throws SplitException {
		String file = null, prefix = null, bytes = "", lines = "1000";
		boolean hasFlag = false, hasSplitter = false;
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				try {
					if (hasFlag) {
						hasFlag = false;
					} else if (args[i].charAt(0) == '-') {
						if (args[i].equals("-b")) {
							if (hasSplitter) {
								throw new SplitException("cannot split in more than one way");
							} else {
								bytes = args[i + 1];
								hasFlag = hasSplitter = true;
							}
						} else if (args[i].equals("-l")) {
							if (hasSplitter) {
								throw new SplitException("cannot split in more than one way");
							} else {
								lines = args[i + 1];
								hasFlag = hasSplitter = true;
							}
						} else {
							throw new SplitException(args[i] + ": invalid flag");
						}
					} else if (file == null) {
						file = args[i];
					} else if (prefix == null) {
						prefix = args[i];
					} else {
						throw new SplitException("extra operand '" + args[i] + "'");
					}
				} catch (ArrayIndexOutOfBoundsException exArr) {
					throw new SplitException("option requires an argument -- '" + args[i] + "'");
				} catch (StringIndexOutOfBoundsException exStr) {
					throw new SplitException("'" + args[i] + "': No such file or directory");
				}
			}
		}
		processedArgs[0] = file;
		processedArgs[1] = prefix;
		processedArgs[2] = bytes;
		processedArgs[3] = lines;
		return;
	}

	@Override
	public void splitFileByLines(String fileName, String prefix, int linesPerFile) throws SplitException {
		try {
			File file = FileUtil.getFileFromPath(fileName);
			FileInputStream stream = new FileInputStream(file);
			splitStreamByLines(stream, prefix, linesPerFile);
		} catch (IOException e) {
			throw new SplitException(e.getMessage());
		}
	}

	/**
	 * Split a stream into fixed size pieces with specified number of lines. Output
	 * splits naming convention: prefix + counter. Default prefix is "x". Default
	 * counter is aa, ab, ..., zz, zaa, zab, ..., zzz, zzaa, etc. For example: xaa,
	 * xab, etc. This is the default option for 'split'.
	 * 
	 * @param fileName
	 *            String of source file name
	 * @param prefix
	 *            String of output file prefix (default is 'x')
	 * @param linesPerFile
	 *            Int of lines to have in the output file (default is 1,000 lines)
	 * @throws Exception
	 */
	public void splitStreamByLines(InputStream stdin, String prefix, int linesPerFile) throws SplitException {
		String outputPrefix = SplitExtension.getAbsolutePath(prefix);
		int lines = linesPerFile;
		if (lines <= 0) {
			throw new SplitException(lines + ": invalid number of lines");
		}
		String currCounter = "aa", line;
		int count = 1;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputPrefix + currCounter));
			line = reader.readLine();
			writer.write(line);
			while ((line = reader.readLine()) != null) {
				if (count == lines) {
					writer.flush();
					writer.close();
					currCounter = SplitExtension.getNextName(currCounter);
					writer = new BufferedWriter(new FileWriter(outputPrefix + currCounter));
					count = 1;
					writer.write(line);
					continue;
				}
				writer.newLine();
				writer.write(line);
				count++;
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException exIO) {
			throw new SplitException("'" + prefix + FILE_NOT_FOUND);
		}
	}

	@Override
	public void splitFileByBytes(String fileName, String prefix, String bytesPerFile) throws SplitException {
		try {
			File file = FileUtil.getFileFromPath(fileName);
			FileInputStream stream = new FileInputStream(file);
			splitStreamByBytes(stream, prefix, bytesPerFile);
		} catch (IOException e) {
			throw new SplitException(e.getMessage());
		}
	}

	/**
	 * Split a stream into fixed size pieces with specified number of bytes. Output
	 * splits naming convention: prefix + counter. Default prefix is "x". Default
	 * counter is aa, ab, ..., zz, zaa, zab, ..., zzz, zzaa, etc. For example: xaa,
	 * xab, etc.
	 * 
	 * @param fileName
	 *            String of source file name
	 * @param prefix
	 *            String of output file prefix (default is 'x')
	 * @param bytesPerFile
	 *            String of number of bytes of content to fit into a file. Can have
	 *            a suffix of either 'b', 'k', or 'm'. Impact of suffix: 'b' -
	 *            multiply the bytes by 512 'k' - multiply the bytes by 1024 'm' -
	 *            multiply the bytes by 1048576
	 * @throws Exception
	 */
	private void splitStreamByBytes(InputStream stdin, String prefix, String bytesPerFile) throws SplitException {
		String outputPrefix = SplitExtension.getAbsolutePath(prefix);
		int numBytes = SplitExtension.parseBytes(bytesPerFile);
		char[] buffer = new char[numBytes];
		String currCounter = null;
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
			BufferedWriter writer;
			while ((count = reader.read(buffer, 0, numBytes)) != -1) {
				currCounter = SplitExtension.getNextName(currCounter);
				writer = new BufferedWriter(new FileWriter(outputPrefix + currCounter));
				writer.write(buffer, 0, count);
				writer.flush();
				writer.close();
				buffer = new char[numBytes];
			}
			reader.close();
		} catch (IOException exIO) {
			throw new SplitException("'" + prefix + FILE_NOT_FOUND);
		}
	}
}
