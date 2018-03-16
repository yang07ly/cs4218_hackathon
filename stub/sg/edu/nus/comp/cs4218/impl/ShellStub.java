package sg.edu.nus.comp.cs4218.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringJoiner;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class ShellStub implements Shell {
	public static final String EVA_RESULT = "Shell stub evaluation result";
	public static final String EVA_EXP = "shell: exception occured";

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
			stdout.write(EVA_RESULT.getBytes());
		} catch (IOException e) {
			throw new ShellException("IO Exception");
		}
	}

	@Override
	public Integer[] getIndicesOfCharNotInQuotes(String source, char character) throws ShellException {
		Vector<Integer> indices = new Vector<Integer>();
		for (int i = 0; i < source.length(); i++) {
			if (source.charAt(i) == character) {
				indices.add(i);
			}
		}
		return indices.toArray(new Integer[indices.size()]);
	}
	
	@Override
	public String[] removeQuotes(String... args) throws AbstractApplicationException, ShellException {
		Vector<String> newArgs = new Vector<String>();
		for (int i = 0; i < args.length; i++) {
			StringJoiner newString = new StringJoiner("");
			for (int j = 0; j < args[i].length(); j++) {
				if (args[i].charAt(j) != '"' && args[i].charAt(j) != '`' && args[i].charAt(j) != '\'') {
					newString.add(args[i].substring(j, j+1));
				}
			}
			newArgs.add(newString.toString());
		}
		return newArgs.toArray(new String[newArgs.size()]);
	}

	@Override
	public String[] performGlob(String... args) throws AbstractApplicationException, ShellException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] performCmdSub(String... args) throws AbstractApplicationException, ShellException {
		// TODO Auto-generated method stub
		return null;
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
