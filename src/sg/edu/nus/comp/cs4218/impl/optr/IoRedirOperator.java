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
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;

/**
 * IO Redirection Command is a call command which contain "<" or ">" iff the
 * backquotes are not surrounded by single quotes (if any).
 * 
 * Command format: <redirection> ::= “<“ [ <whitespace> ] <argument> |
 * “>“ [ <whitespace> ] <argument>
 **/

public class IoRedirOperator {

	public String[] evaluate(String... args) throws AbstractApplicationException, ShellException {
		Vector<String> trimmedArgs = new Vector<String>();
		boolean isPrevIORedir = false;
		for (int i = 0; i < args.length; i++) {
			if (isPrevIORedir) {
				isPrevIORedir = false;
				continue;
			}
			if (args[i].equals("<") || args[i].equals(">")) {
				isPrevIORedir = true;
				continue;
			}
			trimmedArgs.add(args[i]);
		}
		return args;
	}

	public InputStream getInputStream(String... args) throws ShellException {
		try {
			boolean hasFile = false;
			InputStream input = null;
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("<")) {
					if (hasFile) {
						throw new ShellException("only 1 input can be specified");
					}
					hasFile = true;
					File inputFile = FileUtil.getFileFromPath(args[i + 1]);
					input = new FileInputStream(inputFile);
				}
			}
			return input;
		} catch (IOException e) {
			throw new ShellException(e.getMessage());
		} catch (ArrayIndexOutOfBoundsException arrayE) {
			return null;
		}
	}

	public OutputStream getOutputStream(String... args) throws ShellException {
		try {
			boolean hasFile = false;
			OutputStream output = null;
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals(">")) {
					if (hasFile) {
						throw new ShellException("only 1 output can be specified");
					}
					hasFile = true;
					Path path = Paths.get(Environment.currentDirectory).resolve(args[i + 1]);
					File outputFile = new File(path.toString());
					output = new FileOutputStream(outputFile);
				}
			}
			return output;
		} catch (IOException e) {
			throw new ShellException(e.getMessage());
		} catch (InvalidPathException pathE) {
			throw new ShellException("invalid file specified");
		}  catch (ArrayIndexOutOfBoundsException arrayE) {
			return null;
		} 
	}
}
