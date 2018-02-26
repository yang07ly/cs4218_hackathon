//package sg.edu.nus.comp.cs4218.impl.cmd;
//
//import static org.junit.Assert.*;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import sg.edu.nus.comp.cs4218.Environment;
//import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
//import sg.edu.nus.comp.cs4218.exception.ShellException;
//
//public class IoRedirectionTest {
//
//SeqCommand sequenceCommand;
//
//	
//	@Before
//	public void setup() {
//		sequenceCommand = new SeqCommand();
//	}
//	
//	@Test
//	public void testInputRedirectionValid() {
//		String expected = "On the other hand, we denounce with righteous indignation and dislike men "
//				+ "who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire,"
//				+ " that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs "
//				+ "to those who fail in their duty through weakness of will, which is the same as saying through shrinking"
//				+ " from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our"
//				+ " power of choice is untrammelled and when nothing prevents our being able to do what we like best, every"
//				+ " pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims"
//				+ " of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and "
//				+ "annoyances accepted. The wise man therefore always holds in these matters to this principle of selection:"
//				+ " he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains\n";
//		
//		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
//		sequenceCommand = new SeqCommand("cat < text.txt");
//		try {
//			sequenceCommand.parse();
//			sequenceCommand.evaluate(System.in, stdout);
//
//		} catch (ShellException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (AbstractApplicationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(expected, new String(stdout.toByteArray()));
//	}
//	
//	@Test
//	public void testInputRedirectionInvalid() {
//		String filename = "texting.txt";
//		String actual = "";
//		String expected = "shell: " + Environment.currentDirectory + "/" + filename + " (No such file or directory)";
//		
//		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
//		sequenceCommand = new SeqCommand("cat < " + filename);
//		try {
//			sequenceCommand.parse();
//			sequenceCommand.evaluate(System.in, stdout);
//
//		} catch (ShellException e) {
//			// TODO Auto-generated catch block
//			actual = e.getMessage();
//		} catch (AbstractApplicationException e) {
//			// TODO Auto-generated catch block
//			actual = e.getMessage();
//		}
//		
//		 
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	public void testOutputRedirection() {
//		String filename = "newtext.txt";
//		String actual = "";
//		String expected = "";
//		
//		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
//		sequenceCommand = new SeqCommand("echo \"output something here\" > " + filename);
//		try {
//			sequenceCommand.parse();
//			sequenceCommand.evaluate(System.in, stdout);
//
//		} catch (ShellException e) {
//			// TODO Auto-generated catch block
//			actual = e.getMessage();
//		} catch (AbstractApplicationException e) {
//			// TODO Auto-generated catch block
//			actual = e.getMessage();
//		}
//		
//		File file = new File(Environment.currentDirectory + "/" + filename);
//		assertTrue(file.exists());
//		String contents;
//		try {
//			actual = getFileContents(filename);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			actual = e.getMessage();
//		}
//		assertEquals("output something here", actual);
//		deleteFile(filename);
//		 
//	} 
//	
//	public void deleteFile(String filename) {
//		File file = new File(Environment.currentDirectory + "/" + filename);
//		file.delete();
//	}
//	
//	public String getFileContents(String filename) throws IOException {
//		
//	    File file = new File(Environment.currentDirectory + "/" + filename);
//
//	    BufferedReader br = new BufferedReader(new FileReader(file));
//	    StringBuffer fileContents = new StringBuffer();
//	    String line = br.readLine();
//	    while (line != null) {
//	        fileContents.append(line);
//	        line = br.readLine();
//	    }
//
//	    br.close();
//
//	    return fileContents.toString();
//	}
//}
