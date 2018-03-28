package sg.edu.nus.comp.cs4218.impl.optr;

import java.util.Vector;

import sg.edu.nus.comp.cs4218.Operator;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

/**
 * A quote operator provides quoting related functions such as getting indices
 * of character not within quotes or removing quotes.
 */
public class QuoteOperator implements Operator {
	private boolean hasFoundSQ;
	private boolean hasFoundDQ;
	private boolean hasFoundBQ;
	private boolean isCharEscaped;
	private Vector<Integer> removeIndices;

	private void init() {
		hasFoundSQ = false;
		hasFoundDQ = false;
		hasFoundBQ = false;
		isCharEscaped = false;
		removeIndices = new Vector<Integer>();
	}

	/**
	 * Remove all unescaped double and single quotes and set all characters in
	 * quotes to escaped characters. Back quotes are not removed.
	 * 
	 * @param cmd
	 *            CommandString containing the string to have its double and single
	 *            quotes removed and set escaped characters.
	 * 
	 * @throws ShellException
	 *             If the quotes are not closed or the input command is null.
	 */
	public void evaluate(CommandString cmd) throws AbstractApplicationException, ShellException {
		if (cmd == null) {
			throw new ShellException("Null Pointer Exception");
		}

		init();

		char character;
		for (int i = 0; i < cmd.length(); i++) {
			character = cmd.charAt(i);
			switch (character) {
			case '"':
				processDQ(cmd, i);
				break;
			case '\'':
				processSQ(cmd, i);
				break;
			case '`':
				processBQ(cmd, i);
				break;
			default:
				cmd.setCharEscaped(i, isCharEscaped);
				break;
			}
		}
		if (hasFoundDQ || hasFoundSQ || hasFoundBQ) {
			throw new ShellException("Quotes not closed");
		}

		for (int i = removeIndices.size() - 1; i >= 0; i--) {
			cmd.removeCharAt(removeIndices.get(i));
		}
	}

	/**
	 * Execute the process back quote when encountered.
	 * 
	 * @param cmd
	 *            CommandString containing the string to have its double back escape
	 *            state set.
	 */
	private void processBQ(CommandString cmd, int index) {
		if (hasFoundSQ) {
			cmd.setCharEscaped(index, isCharEscaped);
		} else {
			cmd.setCharEscaped(index, false);
			isCharEscaped = !isCharEscaped;
			hasFoundBQ = !hasFoundBQ;
		}
	}

	/**
	 * Execute the process single quote when encountered.
	 * 
	 * @param cmd
	 *            CommandString containing the string to have its single quotes
	 *            remove or escape state set.
	 */
	private void processSQ(CommandString cmd, int index) {
		if (hasFoundDQ || hasFoundBQ) {
			cmd.setCharEscaped(index, isCharEscaped);
		} else {
			removeIndices.add(index);
			cmd.setCharEscaped(index, false);
			isCharEscaped = !isCharEscaped;
			hasFoundSQ = !hasFoundSQ;
		}
	}

	/**
	 * Execute the process double quote when encountered.
	 * 
	 * @param cmd
	 *            CommandString containing the string to have its double quotes
	 *            remove or escape state set.
	 */
	private void processDQ(CommandString cmd, int index) {
		if (hasFoundSQ || hasFoundBQ) {
			cmd.setCharEscaped(index, isCharEscaped);
		} else {
			removeIndices.add(index);
			cmd.setCharEscaped(index, false);
			isCharEscaped = !isCharEscaped;
			hasFoundDQ = !hasFoundDQ;
		}
	}
}
