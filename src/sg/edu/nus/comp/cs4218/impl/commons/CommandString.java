package sg.edu.nus.comp.cs4218.impl.commons;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

/**
 * A CommandString is a string with a boolean for each character to indicate whether if the
 * character is suppose to be escaped in a unix like command. It provides similar function
 * to a string with additional functions for the booleans for each character.
 */
public class CommandString {
	private String cmdStr;
	public final LinkedList<Boolean> hasEscaped;

	public CommandString() {
		cmdStr = "";
		hasEscaped = new LinkedList<Boolean>();
	}

	public CommandString(String initStr) {
		cmdStr = initStr;
		hasEscaped = new LinkedList<Boolean>(Collections.nCopies(initStr.length(), false));
	}
	
	/**
	 * Inserts the specified string at the specified position in this CommandString. 
	 * Shifts the characters currently at that position (if any) and any subsequent 
	 * characters to the right (adds one to their indices).
	 * 
	 * @param index
	 * 			  	Integer of index at which the specified string is to be inserted.
	 * @param insertStr
	 * 			  	String to be inserted.
	 */	
	public void insertStringAt(int index, String insertStr) {
		if (index < cmdStr.length()) {
			cmdStr = cmdStr.substring(0, index) + insertStr + cmdStr.substring(index);
			hasEscaped.addAll(index, Collections.nCopies(insertStr.length(), false));
		} else {
			cmdStr = cmdStr + insertStr;
			hasEscaped.addAll(Collections.nCopies(insertStr.length(), false));
		}
	}
	
	/**
	 * Removes the character at the specified position in this CommandString. Shifts any 
	 * subsequent characters to the left (subtracts one from their indices).
	 * 
	 * @param index
	 * 			  	Integer of index at which the specified character is to be removed.
	 */	
	public void removeCharAt(int index) {
		if (index < cmdStr.length() - 1) {
			cmdStr = cmdStr.substring(0, index) + cmdStr.substring(index + 1);	
		} else {
			cmdStr = cmdStr.substring(0, index);
		}
		hasEscaped.remove(index);
	}
	
	/**
	 * Removes the characters at the specified range in this CommandString. 
	 * The range begins at the specified beginIndex and extends to the 
	 * character at index endIndex - 1. Thus the length of the range is 
	 * endIndex-beginIndex. Shifts any subsequent characters to the left.
	 * 
	 * @param beginIndex
	 * 				Integer of the beginning index, inclusive.
	 * @param endIndex
	 * 				Integer of the ending index, exclusive.
	 */		
	public void removeRange(int beginIndex, int endIndex) {
		if (endIndex < cmdStr.length() - 1) {
			cmdStr = cmdStr.substring(0, beginIndex) + cmdStr.substring(endIndex);
		} else {
			cmdStr = cmdStr.substring(0, beginIndex);
		}

		for (int i = endIndex - 1; i >= beginIndex; i--) {
			hasEscaped.remove(i);
		}
	}
	
	/**
	 * Replaces the characters at the specified range in this CommandString. 
	 * The range begins at the specified beginIndex and extends to the 
	 * character at index endIndex - 1. Thus the length of the range is 
	 * endIndex-beginIndex. The replacement proceeds from the beginning of 
	 * the string to the end.
	 * 
	 * @param beginIndex
	 * 				Integer of the beginning index, inclusive.
	 * @param endIndex
	 * 				Integer of the ending index, exclusive.
	 * @param replacement
	 * 				String of the replacement sequence of char values.
	 */	
	public void replaceRange(int beginIndex, int endIndex, String replacement) {
		if (endIndex < cmdStr.length()) {
			cmdStr = cmdStr.substring(0, beginIndex) + replacement + cmdStr.substring(endIndex);
		} else {
			cmdStr = cmdStr.substring(0, beginIndex) + replacement;
		}

		for (int i = endIndex - 1; i >= beginIndex; i--) {
			hasEscaped.remove(i);
		}
		hasEscaped.addAll(beginIndex, Collections.nCopies(replacement.length(), false));
	}
	
	/**
	 * Set the escape state for the character at the specified position.
	 * 
	 * @param index
	 * 			  	Integer of the index at which the specified character
	 * 				would have its escape state set.
	 * @param bool
	 * 			  	Boolean indicating the escape state to be set.
	 */	
	public void setCharEscaped(int index, boolean bool) {
		hasEscaped.set(index, bool);
	}
	
	/**
	 * Set the escape state for the characters at the specified range.
	 * 
	 * @param beginIndex
	 * 				Integer of the beginning index, inclusive.
	 * @param endIndex
	 * 				Integer of the ending index, exclusive.
	 * @param index
	 * 			  	Boolean indicating the escape state to be set.
	 */	
	public void setCharEscapedRange(int beginIndex, int endIndex,  boolean bool) {
		for (int i = beginIndex; i < endIndex; i++) {
			hasEscaped.set(i, bool);
		}
	}
	
	/**
	 * Return the indices of the specified character in the CommandString which
	 * are not escaped.
	 * 
	 * @param char
	 * 				Char of the character value of interest.
	 * @return Integer Array
	 * 			  	Containing the indices of the character of interest which
	 * 				are not escaped.
	 */	
	public Integer[] getIndicesOfCharNotEscaped(char character) {
		Vector<Integer> charIndics = new Vector<Integer>();
		for (int i = 0; i < cmdStr.length(); i++) {
			if (cmdStr.charAt(i) == character && !hasEscaped.get(i)) {
				charIndics.add(i);
			}
		}
		return charIndics.toArray(new Integer[charIndics.size()]);
	}
	
	/**
	 * Return the index of the first encounter of the specified character in the CommandString 
	 * which are not escaped. The search begins with the character at the specified index and 
	 * extends to the end of this string. 
	 * 
	 * @param beginIndex
	 * 				Integer of the beginning index, inclusive.
	 * @param char
	 * 				Char of the character value of interest.
	 * @return int
	 * 			  	Containing the index of the first occurrence of the character of interest
	 * 				which is not escaped. If it is not found, -1 is returned.
	 */	
	public int getFirstIndexOfCharNotEscaped(int beginIndex, char character) {
		for (int i = beginIndex; i < cmdStr.length(); i++) {
			if (cmdStr.charAt(i) == character && !hasEscaped.get(i)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the char value at the specified index. An index ranges from 0 to length() - 1. 
	 * The first char value of the sequence is at index 0, the next at index 1, and so on, 
	 * as for array indexing. 
	 * 
	 * @param index
	 * 			  	Integer of the index of the char value.
	 * @return char
	 * 			  	The char value at the specified index of this string. 
	 * 				The first char value is at index 0.
	 */	
	public char charAt(int index) {
		return cmdStr.charAt(index);
	}
	
	/**
	 * Returns the escape state of char value at the specified index. An index ranges 
	 * from 0 to length() - 1. The first char value of the sequence is at index 0, 
	 * the next at index 1, and so on, as for array indexing. 
	 * 
	 * @param index
	 * 			  	Integer of the index of the char value.
	 * @return boolean
	 * 			  	The escape state of the char value at the specified index of 
	 * 				this string. The first char value is at index 0.
	 */	
	public boolean isCharEscaped(int index) {
		return hasEscaped.get(index);
	}
	
	/**
	 * Returns a string that is a substring of this string. The substring begins 
	 * at the specified beginIndex and extends to the end of this string. 
	 * 
	 * @param beginIndex
	 * 				Integer of the beginning index, inclusive.
	 * @return String
	 * 				The specified substring.
	 */	
	public String substring(int beginIndex) {
		return cmdStr.substring(beginIndex, cmdStr.length());
	}
	
	/**
	 * Returns a string that is a substring of this CommandString. The substring begins 
	 * at the specified beginIndex and extends to the character at index endIndex - 1. 
	 * Thus the length of the substring is endIndex-beginIndex.  
	 * 
	 * @param beginIndex
	 * 				Integer of the beginning index, inclusive.
	 * @param endIndex
	 * 				Integer of the ending index, exclusive.
	 * @return String
	 * 				The specified substring.
	 */	
	public String substring(int beginIndex, int endIndex) {
		return cmdStr.substring(beginIndex, endIndex);
	}
	
	/**
	 * Returns a CommandString that is a substring of this CommandString. The substring begins 
	 * at the specified beginIndex and extends to the character at index endIndex - 1. 
	 * Thus the length of the substring is endIndex-beginIndex. The character state of the
	 * characters in the substring is also copied. 
	 * 
	 * @param beginIndex
	 * 				Integer of the beginning index, inclusive.
	 * @param endIndex
	 * 				Integer of the ending index, exclusive.
	 * @return CommandString
	 * 				The specified substring.
	 */	
	public CommandString subCmdString(int beginIndex, int endIndex) {
		CommandString newCmdStr = new CommandString(cmdStr.substring(beginIndex, endIndex));
		for (int i = beginIndex; i < endIndex; i++) {
			newCmdStr.setCharEscaped(i - beginIndex, hasEscaped.get(i));
		}
		return newCmdStr;
	}
	
	/**
	 * This object string is returned.
	 *
	 * @return String
	 * 				The string of this CommandString.
	 */
	public String toString() {
		return cmdStr;
	}
	
	/**
	 * Return the escape states of this CommandString.
	 *
	 * @return Boolean Array
	 * 				The escape states of this CommandString.
	 */
	public Boolean[] toBoolArray() {
		return hasEscaped.toArray(new Boolean[hasEscaped.size()]);
	}
	
	/**
	 * Returns the length of this CommandString. The length is equal to the number of Unicode 
	 * code units in the string.
	 *
	 * @return int
	 * 				The length of the sequence of characters represented by this object.
	 */
	public int length() {
		return cmdStr.length();
	}
	
	/**
	 * Returns a string whose value is this string, with any leading and trailing whitespace removed.
	 *
	 * @return CommandString
	 * 				A CommandString whose value is this CommandString, with any leading and 
	 * 				trailing white space removed, or this CommandString if it has no leading 
	 * 				or trailing white space.
	 */
	public CommandString trim() {
		int beginIndex = 0;
		while (beginIndex < cmdStr.length()) {
			if (cmdStr.charAt(beginIndex) != ' ' || hasEscaped.get(beginIndex)) {
				break;
			}
			beginIndex++;
		}

		int endIndex = cmdStr.length() - 1;
		while (endIndex > 0) {
			if (cmdStr.charAt(endIndex) != ' ' || hasEscaped.get(endIndex)) {
				break;
			}
			endIndex--;
		}
		
		if (beginIndex == 0 && endIndex == cmdStr.length() - 1) {
			return this;
		}
		if (endIndex < beginIndex) {
			return new CommandString();
		}
		
		return subCmdString(beginIndex, endIndex + 1);
	}
	
	/**
	 * Tells whether or not this string matches the given regular expression. 
	 * An invocation of this method of the form str.matches(regex) yields 
	 * exactly the same result as the expression.
	 *
	 * @param regex
	 * 				The regular expression to which this string is to be matched.
	 * @return 
	 * 				true if, and only if, this CommandString matches the given regular expression.
	 */
	public boolean matches(String regex) {
		return cmdStr.matches(regex);
	}
}
