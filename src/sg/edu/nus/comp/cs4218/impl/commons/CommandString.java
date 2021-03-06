package sg.edu.nus.comp.cs4218.impl.commons;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

/**
 * A CommandString is a string with a boolean for each character to indicate
 * whether if the character is suppose to be escaped in a unix like command. It
 * provides similar function to a string with additional functions for the
 * booleans for each character.
 */
public class CommandString {
	private String cmdStr;
	private final LinkedList<Boolean> hasEscaped;

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
	 *            Integer of index at which the specified string is to be inserted.
	 * @param insertStr
	 *            String to be inserted.
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
	 * Removes the character at the specified position in this CommandString. Shifts
	 * any subsequent characters to the left (subtracts one from their indices).
	 * 
	 * @param index
	 *            Integer of index at which the specified character is to be
	 *            removed.
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
	 * Removes the characters at the specified range in this CommandString. The
	 * range begins at the specified beginIndex and extends to the character at
	 * index endIndex - 1. Thus the length of the range is endIndex-beginIndex.
	 * Shifts any subsequent characters to the left.
	 * 
	 * @param beginIndex
	 *            Integer of the beginning index, inclusive.
	 * @param endIndex
	 *            Integer of the ending index, exclusive.
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
	 * Replaces the characters at the specified range in this CommandString. The
	 * range begins at the specified beginIndex and extends to the character at
	 * index endIndex - 1. Thus the length of the range is endIndex-beginIndex. The
	 * replacement proceeds from the beginning of the string to the end.
	 * 
	 * @param beginIndex
	 *            Integer of the beginning index, inclusive.
	 * @param endIndex
	 *            Integer of the ending index, exclusive.
	 * @param replacement
	 *            String of the replacement sequence of char values.
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
	 *            Integer of the index at which the specified character would have
	 *            its escape state set.
	 * @param bool
	 *            Boolean indicating the escape state to be set.
	 */
	public void setCharEscaped(int index, boolean... bool) {
		for (int i = index; (i - index) < bool.length && i < hasEscaped.size(); i++) {
			hasEscaped.set(i, bool[i - index]);
		}
	}

	/**
	 * Set the escape state for the characters at the specified range.
	 * 
	 * @param beginIndex
	 *            Integer of the beginning index, inclusive.
	 * @param endIndex
	 *            Integer of the ending index, exclusive.
	 * @param index
	 *            Boolean indicating the escape state to be set.
	 */
	public void setCharEscapedRange(int beginIndex, int endIndex, boolean bool) {
		for (int i = beginIndex; i < endIndex; i++) {
			hasEscaped.set(i, bool);
		}
	}

	/**
	 * Return the indices of the specified character in the CommandString which are
	 * not escaped.
	 * 
	 * @param char
	 *            Char of the character value of interest.
	 * @return Integer Array Containing the indices of the character of interest
	 *         which are not escaped.
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
	 * Return the index of the first encounter of the specified character in the
	 * CommandString which are not escaped. The search begins with the character at
	 * the specified index and extends to the end of this string.
	 * 
	 * @param beginIndex
	 *            Integer of the beginning index, inclusive.
	 * @param char
	 *            Char of the character value of interest.
	 * @return int Containing the index of the first occurrence of the character of
	 *         interest which is not escaped. If it is not found, -1 is returned.
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
	 * Returns the char value at the specified index. An index ranges from 0 to
	 * length() - 1. The first char value of the sequence is at index 0, the next at
	 * index 1, and so on, as for array indexing.
	 * 
	 * @param index
	 *            Integer of the index of the char value.
	 * @return char The char value at the specified index of this string. The first
	 *         char value is at index 0.
	 */
	public char charAt(int index) {
		return cmdStr.charAt(index);
	}

	/**
	 * Returns the escape state of char value at the specified index. An index
	 * ranges from 0 to length() - 1. The first char value of the sequence is at
	 * index 0, the next at index 1, and so on, as for array indexing.
	 * 
	 * @param index
	 *            Integer of the index of the char value.
	 * @return boolean The escape state of the char value at the specified index of
	 *         this string. The first char value is at index 0.
	 */
	public boolean isCharEscaped(int index) {
		return hasEscaped.get(index);
	}

	/**
	 * Returns a CommandString that is a substring of this CommandString. The
	 * substring begins at the specified beginIndex and extends to the character at
	 * index endIndex - 1. Thus the length of the substring is endIndex-beginIndex.
	 * The character state of the characters in the substring is also copied.
	 * 
	 * @param beginIndex
	 *            Integer of the beginning index, inclusive.
	 * @param endIndex
	 *            Integer of the ending index, exclusive.
	 * @return CommandString The specified substring.
	 */
	public CommandString substring(int beginIndex, int endIndex) {
		CommandString newCmdStr = new CommandString(cmdStr.substring(beginIndex, endIndex));
		for (int i = beginIndex; i < endIndex; i++) {
			newCmdStr.setCharEscaped(i - beginIndex, hasEscaped.get(i));
		}
		return newCmdStr;
	}

	/**
	 * This object string is returned.
	 *
	 * @return String The string of this CommandString.
	 */
	public String toString() {
		return cmdStr;
	}

	/**
	 * Returns the length of this CommandString. The length is equal to the number
	 * of Unicode code units in the string.
	 *
	 * @return int The length of the sequence of characters represented by this
	 *         object.
	 */
	public int length() {
		return cmdStr.length();
	}

	/**
	 * Returns a string whose value is this string, with any leading and trailing
	 * whitespace, newline or tabs removed.
	 *
	 * @return CommandString whose value is this CommandString, with any leading and
	 *         trailing whitespace, newline or tabs removed.
	 */
	public CommandString trim() {
		int beginIndex = 0;
		while (beginIndex < cmdStr.length()) {
			if (hasEscaped.get(beginIndex) || (cmdStr.charAt(beginIndex) != ' ' && cmdStr.charAt(beginIndex) != '\t'
					&& cmdStr.charAt(beginIndex) != '\n')) {
				break;
			}
			beginIndex++;
		}

		int endIndex = cmdStr.length() - 1;
		while (endIndex > 0) {
			if (hasEscaped.get(endIndex) || (cmdStr.charAt(endIndex) != ' ' && cmdStr.charAt(endIndex) != '\t'
					&& cmdStr.charAt(endIndex) != '\n')) {
				break;
			}
			endIndex--;
		}

		if (endIndex < beginIndex) {
			return new CommandString();
		}

		return substring(beginIndex, endIndex + 1);
	}

	/**
	 * Compares this CommandString to the specified object. The result is true if
	 * and only if the argument is not null and is a CommandString object that
	 * represents the same sequence of characters and escape states as this object.
	 *
	 * @param obj
	 *            The object to compare this String against
	 * @return boolean true if the given object represents a String equivalent to
	 *         this string, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof CommandString)) {
			return false;
		}

		CommandString cmdStrObj = (CommandString) obj;
		if (!cmdStr.equals(cmdStrObj.toString())) {
			return false;
		}
		for (int i = 0; i < hasEscaped.size(); i++) {
			if (hasEscaped.get(i) != cmdStrObj.isCharEscaped(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a hash code for this string.
	 * 
	 * @return int A hashcode value of this object.
	 */

	@Override
	public int hashCode() {
		return cmdStr.hashCode();
	}
}
