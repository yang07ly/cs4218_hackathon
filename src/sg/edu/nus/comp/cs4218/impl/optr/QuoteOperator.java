package sg.edu.nus.comp.cs4218.impl.optr;

import java.util.StringJoiner;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class QuoteOperator{
	
	private boolean hasFoundSQ;
	private boolean hasFoundDQ;
	private boolean hasFoundBQ;

	private StringJoiner singleArg;

	private void init() {
		hasFoundSQ = false;
		hasFoundDQ = false;
		hasFoundBQ = false;

		singleArg = new StringJoiner("");
	}

	public Integer[] getIndices(String source, char sepChar) throws ShellException {
		init();
		
		Vector<Integer> indices = new Vector<Integer>();
		char character;
		
		for (int i = 0; i < source.length(); i++) {
			character = source.charAt(i);
			switch (character) {
			case '"':
				if (hasFoundSQ || hasFoundBQ) {	
				} else  {
					if (sepChar == '"') {
						indices.add(i);
					}
					hasFoundDQ = !hasFoundDQ;
				}
				break;
			case '\'':
				if (hasFoundDQ) {
				} else {
					if (sepChar == '\'') {
						indices.add(i);
					}
					hasFoundSQ = !hasFoundSQ;
				}
				break;
			case '`':
				if (hasFoundSQ) {
				} else {
					if (sepChar == '`') {
						indices.add(i);
					}
					hasFoundBQ = !hasFoundBQ;
				}
				break;
			default:
				if (character == sepChar && !hasFoundDQ && !hasFoundSQ && !hasFoundBQ) {
					indices.add(i);
				}
				break;
			}
		}

		if (hasFoundDQ || hasFoundSQ || hasFoundBQ) {
			throw new ShellException("Quotes not closed");
		}

		return indices.toArray(new Integer[indices.size()]);
	}

	public String[] evaluate(String... cmdArgs) throws AbstractApplicationException, ShellException {
		String[] newCmdArgs = new String[cmdArgs.length];
		for (int i = 0; i < cmdArgs.length; i++) {
			init();
	
			for (int j = 0; j < cmdArgs[i].length(); j++) {
				switch (cmdArgs[i].charAt(j)) {
				case '"':
					if (hasFoundSQ || hasFoundBQ) {
						singleArg.add("\"");
					} else {
						hasFoundDQ = !hasFoundDQ;
					}
					break;
				case '\'':
					if (hasFoundDQ || hasFoundBQ) {
						singleArg.add("'");
					} else {
						hasFoundSQ = !hasFoundSQ;
					}
					break;
				case '`':
					if (hasFoundSQ || hasFoundDQ) {
						singleArg.add("`");
					} else {
						hasFoundBQ = !hasFoundBQ;
					}
					break;
				default:
					singleArg.add(cmdArgs[i].substring(j, j + 1));
					break;
				}
			}
	
			if (hasFoundDQ || hasFoundSQ || hasFoundBQ) {
				throw new ShellException("Quotes not closed");
			}
	
			newCmdArgs[i] = singleArg.toString();
		}
		return newCmdArgs;
	}
}
