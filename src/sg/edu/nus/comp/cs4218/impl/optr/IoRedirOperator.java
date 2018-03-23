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

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;
import sg.edu.nus.comp.cs4218.impl.commons.FileUtil;

/**
 * IO Redirection Command is a call command which contain "<" or ">" iff the
 * backquotes are not surrounded by single quotes (if any).
 * 
 * Command format: <redirection> ::= "<" [ <whitespace> ] <argument> | ">" [
 * <whitespace> ] <argument>
 **/

public class IoRedirOperator {
	private final Shell shell;

	public IoRedirOperator(Shell shell) {
		this.shell = shell;
	}

	public InputStream getInputStream(CommandString cmd) throws ShellException, AbstractApplicationException {
		if (cmd == null) {
			return null;
		}
		Integer[] indices = cmd.getIndicesOfCharNotEscaped('<');
		if (indices.length > 1) {
			throw new ShellException("only 1 inputstream can be specified");
		} else if (indices.length == 0) {
			return null;
		}
		HashSet<Integer> set = new HashSet<Integer>(Arrays.asList(cmd.getIndicesOfCharNotEscaped(' ')));
		set.addAll(Arrays.asList(cmd.getIndicesOfCharNotEscaped('\t')));
		String command = cmd.toString(), arg = "";
		int endIndex;
		for (endIndex = indices[0] + 1; endIndex < command.length(); endIndex++) {
			if (command.charAt(endIndex) != '\t' && command.charAt(endIndex) != ' ') {
				break;
			}
		}
		int startIndex = endIndex;
		for (; endIndex < command.length(); endIndex++) {
			if (((command.charAt(endIndex) == '>' || command.charAt(endIndex) == ' '
					|| command.charAt(endIndex) == '\t')) && set.contains(endIndex)) {
				break;
			}
			arg += command.charAt(endIndex);
		}
		if (arg.isEmpty()) {
			throw new ShellException("no input file specified");
		}

		CommandString fileString = cmd.substring(startIndex, endIndex);
		shell.performCmdSub(fileString);
		setAsteriskFalse(fileString);
		shell.performGlob(fileString);
		cmd.removeRange(indices[0], endIndex);
		try {
			return new FileInputStream(FileUtil.getFileFromPath(fileString.toString()));
		} catch (IOException e) {
			throw new ShellException(e.getMessage());
		}
	}

	public OutputStream getOutputStream(CommandString cmd) throws ShellException, AbstractApplicationException {
		if (cmd == null) {
			return null;
		}
		Integer[] indices = cmd.getIndicesOfCharNotEscaped('>');
		if (indices.length > 1) {
			throw new ShellException("only 1 outputstream can be specified");
		} else if (indices.length == 0) {
			return null;
		}
		HashSet<Integer> set = new HashSet<Integer>(Arrays.asList(cmd.getIndicesOfCharNotEscaped(' ')));
		set.addAll(Arrays.asList(cmd.getIndicesOfCharNotEscaped('\t')));
		String command = cmd.toString(), arg = "";
		int endIndex;
		for (endIndex = indices[0] + 1; endIndex < command.length(); endIndex++) {
			if (command.charAt(endIndex) != '\t' && command.charAt(endIndex) != ' ') {
				break;
			}
		}
		int startIndex = endIndex;
		for (; endIndex < command.length(); endIndex++) {
			if (((command.charAt(endIndex) == '<' || command.charAt(endIndex) == ' '
					|| command.charAt(endIndex) == '\t')) && set.contains(endIndex)) {
				break;
			}
			arg += command.charAt(endIndex);
		}
		if (arg.isEmpty()) {
			throw new ShellException("no output file specified");
		}

		CommandString fileString = cmd.substring(startIndex, endIndex);
		shell.performCmdSub(fileString);
		setAsteriskFalse(fileString);
		shell.performGlob(fileString);
		cmd.removeRange(indices[0], endIndex);

		try {
			Path path = Paths.get(Environment.currentDirectory).resolve(fileString.toString());
			return new FileOutputStream(new File(path.toString()));
		} catch (IOException e) {
			throw new ShellException(e.getMessage());
		} catch (InvalidPathException pathE) {
			throw new ShellException("invalid file specified");
		}
	}

	/**
	 * sets escapes status for * to false and true otherwise
	 */
	public void setAsteriskFalse(CommandString command) {
		String cmdStr = command.toString();
		for (int i = 0; i < cmdStr.length(); i++) {
			if (cmdStr.charAt(i) == '*') {
				command.setCharEscaped(i, false);
			} else {
				command.setCharEscaped(i, true);
			}
		}
	}

}
