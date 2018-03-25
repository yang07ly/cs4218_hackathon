package sg.edu.nus.comp.cs4218.impl.commons;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.ShellException;

public class StreamUtilTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testCloseInputStreamNullExceptionDetectedNull() throws ShellException {
		thrown.expect(ShellException.class);
		thrown.expectMessage("NullPointer Exception");
		StreamUtil.closeInputStream(null);

	}

	@Test
	public void testCloseOutputStreamNullEXceptionDetectedNull() throws ShellException {
		thrown.expect(ShellException.class);
		thrown.expectMessage("NullPointer Exception");
		StreamUtil.closeOutputStream(null);
	}
}
