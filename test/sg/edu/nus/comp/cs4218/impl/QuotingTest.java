package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.app.EchoApplication;

public class QuotingTest {
	
	EchoApplication echoApp;
	String expected, result;
	OutputStream stdout;

	@Before
	public void setUp() throws Exception {
		echoApp = new EchoApplication();
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void DoubleQuoteTest() {
		
	}

}
