package sg.edu.nus.comp.cs4218.impl.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.CatException;

public class AppStub implements Application {
	
	public AppStub() {
		super();	
	}
	
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws AbstractApplicationException {
		
		if (args.length == 0 || args[0] == "") {
			return;
		}
		
		for (int i = 0; i < args.length; i++) {
			try {
				
				stdout.write(args[i].getBytes());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
		
		
//		if (args[0] == "cat") {
//			String text = "file1 contents";
//			try {
//				stdout.write(text.getBytes());
//			} catch (IOException e) {
//				throw new CatException("");
//			}
//			return;
//		}

	}

}
