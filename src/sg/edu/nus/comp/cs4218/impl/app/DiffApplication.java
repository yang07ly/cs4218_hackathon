package sg.edu.nus.comp.cs4218.impl.app;

import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.app.DiffItf;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;

public class DiffApplication implements DiffItf{

	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws AbstractApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String diffTwoFiles(String fileNameA, String fileNameB, Boolean isShowSame, Boolean isNoBlank,
			Boolean isSimple) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String diffTwoDir(String folderA, String folderB, Boolean isShowSame, Boolean isNoBlank, Boolean isSimple)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String diffFileAndStdin(String fileName, InputStream stdin, Boolean isShowSame, Boolean isNoBlank,
			Boolean isSimple) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
