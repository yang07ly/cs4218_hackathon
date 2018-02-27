package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class IoRedirectionTest {

	public static final String EXP_SYNTAX = "shell: Invalid syntax encountered.";

	IoRedirCommand ioRedirCommand;
	Vector<String> cmdVector;
	int actual;
	
	@Before
	public void setup() {
		ioRedirCommand = new IoRedirCommand();
		cmdVector = new Vector<String>();
		actual = 0;
		String fileDir = "test_system" + File.separator + "cmd_test_system";
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + fileDir;
		
	}
	
	@Test
	public void testExtractInputRedirOutputArgs() {
		try {
			actual = ioRedirCommand.extractInputRedir(">", cmdVector, 1);
		} catch (ShellException e) {
			e.printStackTrace();
		}
		assertEquals(1, actual);
		
	
	}
	
	@Test
	public void testExtractInputRedirInvalidArgs() {
		String message = "";
		try {
			actual = ioRedirCommand.extractInputRedir("empty", cmdVector, 1);
		} catch (ShellException e) {
			message = e.getMessage();
		}
		assertEquals(EXP_SYNTAX, message);
	}
	
	@Test
	public void testExtractInputRedirValidArgs() {
		try {
			actual = ioRedirCommand.extractInputRedir("cat < abc.txt def.txt", cmdVector, 4);
		} catch (ShellException e) {
			e.printStackTrace();
		}
		assertEquals(4, actual);
	}
	
	@Test
	public void testExtractOutputRedirEmptyArgs() {
		try {
			actual = ioRedirCommand.extractOutputRedir("", cmdVector, 0);
		} catch (ShellException e) {
			e.printStackTrace();
		}
		assertEquals(0, actual);	
	}
	
	@Test
	public void testExtractOutputRedirInvalidArgs() {
		String message = "";
		try {
			actual = ioRedirCommand.extractOutputRedir("output", cmdVector, 0);
		} catch (ShellException e) {
			message = e.getMessage();
		}
		assertEquals(EXP_SYNTAX, message);	
	}
	
	@Test
	public void testExtractOutputRedirOutputArgs() {
		try {
			actual = ioRedirCommand.extractOutputRedir("echo \"HELLO\" > output.txt", cmdVector, 13);
		} catch (ShellException e) {
			e.printStackTrace();
		}
		assertEquals(13, actual);	
	}
	
	@Test
	public void testOpenInputRedirValidFile() {
		String expected = "On the other hand, we denounce with righteous indignation and dislike men"
				+ " who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire,"
				+ " that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs"
				+ " to those who fail in their duty through weakness of will, which is the same as saying through"
				+ " shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour,"
				+ " when our power of choice is untrammelled and when nothing prevents our being able to do what we like best,"
				+ " every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims"
				+ " of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and "
				+ "annoyances accepted. The wise man therefore always holds in these matters to this principle of selection:"
				+ " he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains";
		
		InputStream fInputStream = null;
		try {
			fInputStream = (FileInputStream) ioRedirCommand.openInputRedir("text.txt");
		} catch (ShellException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(expected, getStringFromInputStream(fInputStream));	

	}
	
	@Test
	public void testOpenInputRedirInvalidFile() {
		String message = "";
		String filename = "file-not-found.txt";
		String expected = "shell: File not found";
		InputStream fInputStream = null;
		try {
			fInputStream = (FileInputStream) ioRedirCommand.openInputRedir(filename);
		} catch (ShellException e) {
			message = e.getMessage();
		}
		assertEquals(expected, message);	

	}
	
	@Test
	public void testOpenOutputRedirValidFile() {
		String filename = "newtext.txt";
		OutputStream outputStream = null;
		try {
			outputStream = ioRedirCommand.openOutputRedir(filename);
		} catch (ShellException e) {
			e.printStackTrace();
		}
		
		File f = new File(Environment.currentDirectory + File.separator + filename);
		
		assertTrue(f.exists());
		
		deleteFile(filename);
		
		assertFalse(f.exists());
	}
	

	
	public void deleteFile(String filename) {
		File file = new File(Environment.currentDirectory + File.separator + filename);
		file.delete();
	}
	
	public String getFileContents(String filename) throws IOException {
		
	    File file = new File(Environment.currentDirectory + File.separator + filename);

	    BufferedReader buffReader = new BufferedReader(new FileReader(file));
	    StringBuffer fileContents = new StringBuffer();
	    String line = buffReader.readLine();
	    while (line != null) {
	        fileContents.append(line);
	        line = buffReader.readLine();
	    }

	    buffReader.close();

	    return fileContents.toString();
	}
	
	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
}
