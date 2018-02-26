package sg.edu.nus.comp.cs4218.impl.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.MkdirInterface;
import sg.edu.nus.comp.cs4218.exception.MkdirException;

public class MkdirApplication implements MkdirInterface {
	
	/**
	 * Create folder from the given folder names. Do nothing
	 * if folder already exists. If folder name is a path 
	 * format, create the folder that satisfies the path
	 * specification.
	 * @param folderName 
	 * 				Array of string of folder names to
	 * 				be created.
	 * @throws MkdirException
	 * 				If no args is specified or
	 * 				if the file/folder name is empty or
	 * 				if the folder already exists or	
	 * 				if the path do not exist.		
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws MkdirException {
		createFolder(args);
	}

	/**
	 * Create folder from the given folder names. Do nothing
	 * if folder already exists. If folder name is a path 
	 * format, create the folder that satisfies the path
	 * specification.
	 * @param folderName 
	 * 				Array of string of folder names to
	 * 				be created.
	 * @throws MkdirException
	 * 				If no foldername is specified or
	 * 				if the file/folder name is empty or
	 * 				if the folder already exists or	
	 * 				if the path do not exist.		
	 */
	@Override
	public void createFolder(String... folderName) throws MkdirException {
		if (folderName == null || folderName.length == 0) {
			throw new MkdirException("missing operand");
		}
		
		for (int i = 0; i < folderName.length; i++) {
			if (folderName[i].length() == 0) {
				throw new MkdirException("cannot create directory ‘’: No such file or directory");
			}
			
			try {
				Path folderPath = Paths.get(Environment.currentDirectory).resolve(folderName[i]);
				Files.createDirectory(folderPath);
			} catch (InvalidPathException e) {
				throw new MkdirException("cannot create directory ‘" + folderName[i] + "’: No such file or directory");
			} catch (FileAlreadyExistsException e) {
				throw new MkdirException("cannot create directory ‘" + folderName[i] + "’: File exists");
			}catch (IOException e) {
				throw new MkdirException("cannot create directory ‘" + folderName[i] + "’: No such file or directory");
			}
		}
	}

}
