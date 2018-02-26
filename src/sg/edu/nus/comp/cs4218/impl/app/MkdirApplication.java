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

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws MkdirException {
		createFolder(args);
	}

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
