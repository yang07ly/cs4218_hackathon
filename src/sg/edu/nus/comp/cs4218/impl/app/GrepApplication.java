package sg.edu.nus.comp.cs4218.impl.app;

import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.app.GrepItf;
import sg.edu.nus.comp.cs4218.exception.GrepException;

public class GrepApplication implements GrepItf {

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws GrepException {
		// TODO Auto-generated method stub

	}

	@Override
	public String grepFromStdin(String pattern, Boolean isInvert, InputStream stdin) throws GrepException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String grepFromMultipleFiles(String pattern, Boolean isInvert, String... fileNames) throws GrepException {
		// TODO Auto-generated method stub
		return null;
	}

}
