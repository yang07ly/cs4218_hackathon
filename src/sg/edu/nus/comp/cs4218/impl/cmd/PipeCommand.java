package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class PipeCommand implements Command{
	public static final String EXP_INVALID_PIPE_OPERATOR = "Invalid pipe operator/s";
	public static final String EXP_SYNTAX = "Invalid syntax encountered.";
	public static final String EXP_REDIR_PIPE = "File output redirection and pipe "
			+ "operator cannot be used side by side.";
	public static final String EXP_SAME_REDIR = "Input redirection file same as "
			+ "output redirection file.";
	public static final String EXP_STDOUT = "Error writing to stdout.";
	public static final String EXP_NOT_SUPPORTED = " not supported yet";
	
	public static final char CHAR_BQ = '`';
	public static final char CHAR_DQ = '"';
	public static final char CHAR_SQ = '\'';
	public static final char PIPE_OPERATOR = '|';



	String app;
	String cmdline, inputStreamS, outputStreamS;
	Vector<String> argsArray;
	Boolean error;
	String errorMsg;

	public PipeCommand(String cmdline) {
		this.cmdline = cmdline.trim();
		app = inputStreamS = outputStreamS = "";
		error = false;
		errorMsg = "";
		argsArray = new Vector<String>();
	}

	public PipeCommand() {
		this("");
	}

	@Override
	public void evaluate(InputStream stdin, OutputStream stdout) throws AbstractApplicationException, ShellException {
		InputStream inputStream = stdin;
		OutputStream outputStream = stdout;
		InputStream inputStreamTemp = null;


		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		if (argsArray.size() == 1) {
			String command = argsArray.get(0);
			CallCommand callCommand = new CallCommand(command);
			callCommand.parse();
			callCommand.evaluate(inputStream, outputStream);
			
			ShellImpl.writeToStdout(byteArrayOutputStream, stdout);
			ShellImpl.outputStreamToInputStream(byteArrayOutputStream);
			return;
		}
		
		for (int i = 0; i < argsArray.size(); i++) {
			String command = argsArray.get(i);
			CallCommand callCommand = new CallCommand(command);
			inputStreamTemp = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream = new ByteArrayOutputStream();	
			callCommand.parse();
			callCommand.evaluate(inputStreamTemp, byteArrayOutputStream);
		}
 
		ShellImpl.writeToStdout(byteArrayOutputStream, stdout);
		ShellImpl.outputStreamToInputStream(byteArrayOutputStream);
		return;
	}
 
	public void parse() throws ShellException {
		
		int sizeBQ = 0;
		int sizeDQ = 0;
		int sizeSQ = 0;
		int index = 0;
		
		if (cmdline.length() == 0) {
			return;
		}
		
		if (cmdline.charAt(0) == PIPE_OPERATOR || cmdline.charAt(cmdline.length() - 1) == PIPE_OPERATOR) {
			throw new ShellException(EXP_INVALID_PIPE_OPERATOR);
		}
		
		for (int i = 0; i < cmdline.length(); i++) {
			if (cmdline.charAt(i) == CHAR_BQ) {
				sizeBQ++;	
			} else if (cmdline.charAt(i) == CHAR_DQ) {
				sizeDQ++;
			} else if (cmdline.charAt(i) == CHAR_SQ) {
				sizeSQ++;
			} else if (cmdline.charAt(i) == PIPE_OPERATOR) {
				if (sizeBQ % 2 == 0) {
					argsArray.add(cmdline.substring(index, i));
					index = i + 1;
				}
			} 
			if (i == cmdline.length() - 1) {
				if (sizeSQ % 2 == 0 || sizeDQ % 2 == 0 || sizeBQ % 2 == 0) {
					argsArray.add(cmdline.substring(index, i + 1));
					break;
				}
			}
			
		}
	}
	
	
	public void outputArgsArray() {
		for(String i: argsArray) {
			System.out.println(i);
		}
	}
	
	public void outputArgsArraySize() {
		System.out.println(argsArray.size());
	}
	/**
	 * Terminates current execution of the command (unused for now)
	 */
	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

}
