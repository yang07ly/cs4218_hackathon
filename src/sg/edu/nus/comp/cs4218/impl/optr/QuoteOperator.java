package sg.edu.nus.comp.cs4218.impl.optr;

import java.util.StringJoiner;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Operator;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

/**
 * A quote operator provides quoting related functions such as getting indices
 * of character not within quotes or removing quotes.
 */
public class QuoteOperator implements Operator {
	
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
	
	/**
	 * Returns all indices of the specified character that is not within
	 * any quotes.
	 * 
	 * @param source
	 * 			  	String used to check for the specified character.
	 * @param sepChar
	 * 			  	Character to find in the string.
	 * @return Integer Array
	 * 			  	indices of the specfied character not within quotes.
	 * 
	 * @throws ShellException
	 *            	If the quotes are not closed.
	 */
	public Integer[] getIndices(String source, char sepChar) throws ShellException {
		if (source == null) {
			throw new ShellException("Null Pointer Exception");
		}
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
				if (hasFoundDQ || hasFoundBQ) {
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
	
	/**
	 * Returns the the list of string with quotes removed.
	 * 
	 * @param cmdArgs
	 * 			  	String Array containing the string to have its 
	 * 				quotes removed.
	 * @return String Array
	 * 			  	list of string with its quotes removed.
	 * 
	 * @throws ShellException
	 *            	If the quotes are not closed.
	 */
	public String[] evaluate(String... cmdArgs) throws AbstractApplicationException, ShellException {
		String[] newCmdArgs = new String[cmdArgs.length];
		for (int i = 0; i < cmdArgs.length; i++) {
			if (cmdArgs[i] == null) {
				throw new ShellException("Null Pointer Exception");
			}
			
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
					if (hasFoundSQ) {
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
