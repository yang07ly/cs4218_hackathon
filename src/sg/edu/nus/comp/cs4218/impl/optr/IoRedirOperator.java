package sg.edu.nus.comp.cs4218.impl.optr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Operator;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;

/**
 * IO Redirection Command is a call command which contain "<" or ">" iff the
 * backquotes are not surrounded by single quotes (if any).
 * 
 * Command format: <redirection> ::= "<" [ <whitespace> ] <argument> | ">" [
 * <whitespace> ] <argument>
 **/

public class IoRedirOperator implements Operator {
	ShellImpl shell;

	public IoRedirOperator(ShellImpl shell) {
		this.shell = shell;
	}

	@Override
	public String[] evaluate(String... args) throws AbstractApplicationException, ShellException {
		Vector<String> trimmedArgs = new Vector<String>();
		boolean hasStream = false;
		for (int i = 0; i < args.length; i++) {
			String arg = "";
			HashSet<Integer> set = new HashSet<Integer>(Arrays.asList(shell.getIndicesOfCharNotInQuotes(args[i], '<')));
			set.addAll(Arrays.asList(shell.getIndicesOfCharNotInQuotes(args[i], '>')));
			for (int j = 0; j < args[i].length(); j++) {
				if (((args[i].charAt(j) == '<') || (args[i].charAt(j) == '>')) && set.contains(j)) {
					if (hasStream || arg.isEmpty()) {
						arg = "";
					} else {
						trimmedArgs.add(arg);
					}
					hasStream = true;
					continue;
				}
				arg += args[i].charAt(j);
			}
			if (!hasStream && !arg.isEmpty()) {
				trimmedArgs.add(arg);
			}
		}
		return trimmedArgs.toArray(new String[trimmedArgs.size()]);
	}

	/**
	 * Scans the arguments and sets the input stream
	 * 
	 * @param args
	 *            String array of the individual arguments.
	 * @return the input stream
	 * @throws ShellException
	 *             if more than 1 input stream is specified
	 */
	public InputStream getInputStream(String... args) throws ShellException {
		try {
			String inputFile = null;
			boolean hasStream = false;
			for (int i = 0; i < args.length; i++) {
				String arg = "";
				HashSet<Integer> set = new HashSet<Integer>(
						Arrays.asList(shell.getIndicesOfCharNotInQuotes(args[i], '<')));
				for (int j = 0; j < args[i].length(); j++) {
					if ((args[i].charAt(j) == '<') && set.contains(j)) {
						if (hasStream) {
							throw new ShellException("only 1 input can be specified");
						}
						arg = "";
						hasStream = true;
						continue;
					}else if(args[i].charAt(j) == '>') {
						if(hasStream && inputFile == null) {
							inputFile = arg;
						}
						arg = "";
						continue;
					}
					arg += args[i].charAt(j);
				}
				if (hasStream && !arg.isEmpty() && inputFile == null) {
					inputFile = arg;
				}
			}
			if (inputFile == null) {
				if (hasStream) {
					throw new ShellException("no input file specified");
				}
				return null;
			}
			return new FileInputStream(FileUtil.getFileFromPath(inputFile));
		} catch (IOException e) {
			throw new ShellException(e.getMessage());
		}
	}

	/**
	 * Scans the arguments and sets the output stream
	 * 
	 * @param args
	 *            String array of the individual arguments.
	 * @return the output stream
	 * @throws ShellException
	 *             if more than 1 output stream is specified
	 */
	public OutputStream getOutputStream(String... args) throws ShellException {
		try {
			String outputFile = null;
			boolean hasStream = false;
			for (int i = 0; i < args.length; i++) {
				String arg = "";
				HashSet<Integer> set = new HashSet<Integer>(
						Arrays.asList(shell.getIndicesOfCharNotInQuotes(args[i], '>')));
				for (int j = 0; j < args[i].length(); j++) {
					if ((args[i].charAt(j) == '>') && set.contains(j)) {
						if (hasStream) {
							throw new ShellException("only 1 output can be specified");
						}
						arg = "";
						hasStream = true;
						continue;
					}else if(args[i].charAt(j) == '<') {
						if(hasStream && outputFile == null) {
							outputFile = arg;
						}
						arg = "";
					}
					arg += args[i].charAt(j);
				}
				if (hasStream && !arg.isEmpty() && outputFile == null) {
					outputFile = arg;
				}
			}
			if (outputFile == null) {
				if (hasStream) {
					throw new ShellException("no output file specified");
				}
				return null;
			}
			Path path = Paths.get(Environment.currentDirectory).resolve(outputFile);
			return new FileOutputStream(new File(path.toString()));
		} catch (IOException e) {
			throw new ShellException(e.getMessage());
		} catch (InvalidPathException pathE) {
			throw new ShellException("invalid file specified");
		}
	}
}
