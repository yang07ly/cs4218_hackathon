package sg.edu.nus.comp.cs4218.impl.app;

import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.app.PasteItf;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;

public class PasteApplication implements PasteItf{

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws AbstractApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String mergeStdin(InputStream stdin) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mergeFile(String... fileName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mergeFileAndStdin(InputStream stdin, String... fileName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
