package sg.edu.nus.comp.cs4218.impl.commons;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.ShellException;

public class StreamUtilTest {
	
	private ByteArrayInputStream InputStream;
	private ByteArrayOutputStream OutputStream;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		byte[] buf = new byte[10];
		InputStream = new ByteArrayInputStream(buf);
		OutputStream = new ByteArrayOutputStream();
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCloseInputStreamNullExceptionDetectedNull() throws ShellException {
		thrown.expect(ShellException.class);
		thrown.expectMessage("NullPointer Exception");
		StreamUtil.closeInputStream(null);
		
	}
	
	@Test
	public void testCloseInputStreamSuccessfully() throws Exception {
		StreamUtil.closeInputStream(InputStream);
		
		//thrown.expect(IOException.class);
		InputStream.read();
			
	}
	
	@Test
	public void testCloseOutputStreamNullEXceptionDetectedNull() throws ShellException {
		thrown.expect(ShellException.class);
		thrown.expectMessage("NullPointer Exception");
		StreamUtil.closeOutputStream(null);
	}
	
	@Test
	public void testCloseOutputStreamSuccessfully() throws ShellException {
		StreamUtil.closeOutputStream(OutputStream);
		
		thrown.expect(ShellException.class);
		StreamUtil.closeOutputStream(OutputStream);
	}
	
	

}
