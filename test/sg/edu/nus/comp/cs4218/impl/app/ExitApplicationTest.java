package sg.edu.nus.comp.cs4218.impl.app;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.ExitException;

public class ExitApplicationTest {

	@Test
	public void testExit() {
		Thread thread = new Thread(){
	    public void run(){
	        ExitApplication app = new ExitApplication();
	        try {
				app.terminateExecution();
			} catch (ExitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	    };
		assertTrue(!thread.isAlive());
	}

}
