package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.ExitException;

public class ExitApplicationTest {

	@Test
	public void testExitToRunCorrectly() {
		Thread thread1 = new Thread() {
			public void run() {
				ExitApplication app = new ExitApplication();
				try {
					app.terminateExecution();
				} catch (ExitException e) {
				}
			}
		};
		assertTrue(!thread1.isAlive());
	}
}
