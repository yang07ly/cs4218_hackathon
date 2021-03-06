package sg.edu.nus.comp.cs4218.impl.app;

import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.app.ExitInterface;
import sg.edu.nus.comp.cs4218.exception.ExitException;

public class ExitApplication implements ExitInterface {

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws ExitException {
		terminateExecution();
	}

	@Override
	public void terminateExecution() throws ExitException {
		System.exit(0);
	}

}
