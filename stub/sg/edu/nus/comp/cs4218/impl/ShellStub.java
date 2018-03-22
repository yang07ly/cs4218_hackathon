package sg.edu.nus.comp.cs4218.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class ShellStub implements Shell {
	public static final String SHELL_RESULT = "ShellR";
	public static final String GLOB_RESULT = "GlobR";
	public static final String CMDSUB_RESULT = "CmdsubR";
	public static final String SHELL_EXP = "shell: exception occured";

	@Override
	public Shell newInstance() {
		return new ShellStub();
	}

	@Override
	public void runApp(String app, String[] argsArray, InputStream inputStream, OutputStream outputStream)
			throws AbstractApplicationException, ShellException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseAndEvaluate(String cmdline, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		try {
			stdout.write(SHELL_RESULT.getBytes());
		} catch (IOException e) {
			throw new ShellException("IO Exception");
		}
	}
	
	@Override
	public void processQuotes(CommandString cmd) {
		// TODO Auto-generated method stub
	}

	@Override
	public void performGlob(CommandString cmd) throws AbstractApplicationException, ShellException {
		int startIndex = 0;
		int wildCardIndex;
		while ((wildCardIndex = cmd.getFirstIndexOfCharNotEscaped(startIndex, '*')) != -1) {
			cmd.replaceRange(wildCardIndex, wildCardIndex + 1, GLOB_RESULT);
			startIndex += GLOB_RESULT.length();
		}
	}

	@Override
	public void performCmdSub(CommandString cmd) throws AbstractApplicationException, ShellException {
		Integer[] bqIndices = cmd.getIndicesOfCharNotEscaped('`');
		for (int i = 0; i < bqIndices.length; i+=2) {
			cmd.replaceRange(bqIndices[i], bqIndices[i + 1] + 1, CMDSUB_RESULT);
		}
	}

	@Override
	public String[] removeIOStreamFromArgs(String... args) throws AbstractApplicationException, ShellException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream(String... args) throws ShellException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream(String... args) throws ShellException {
		// TODO Auto-generated method stub
		return null;
	}
}
