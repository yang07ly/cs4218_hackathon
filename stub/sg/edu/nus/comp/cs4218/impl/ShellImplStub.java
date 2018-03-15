package sg.edu.nus.comp.cs4218.impl;

import java.util.StringJoiner;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class ShellImplStub extends ShellImpl {
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
}
