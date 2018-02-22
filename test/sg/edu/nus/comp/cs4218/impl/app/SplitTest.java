package sg.edu.nus.comp.cs4218.impl.app;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;

public class SplitTest {
	SplitApplication app;
	InputStream is = System.in;
	OutputStream os;
	String expected, output;

	@BeforeClass
	public void setUpOnce() {
	}

	@Before
	public void setUp() {
		app = new SplitApplication();
		os = new ByteArrayOutputStream();
		Environment.currentDirectory = System.getProperty("user.dir") + File.separator + "test_system";
		output = "";
		expected = "";
	}

	@Test
	public void testLinesEmptyFileName() {
		expected = "split: can't have empty argument";
		try {
			app.splitFileByLines("", "", 4);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testLinesSpaceFileName() {
		expected = "split: invalid file:  ";
		try {
			app.splitFileByLines(" ", "", 4);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testLinesFileNotFound() {
		expected = "split: cannot open 'asdf' for reading: No such file or directory";
		try {
			app.splitFileByLines("asdf", "", 4);
		} catch (Exception e) {
			output = e.getMessage();
		}
		assertEquals(expected, output);
	}

	@Test
	public void testLinesDirectoryFileNotFound() {
		try {
			app.splitFileByLines("asdf.txt", "", 4);
		} catch (Exception e) {
			output = e.getMessage();
		}
		expected = "split: cannot open 'asdf.txt' for reading: No such file or directory";
		assertEquals(expected, output);
	}

	@Test
	public void testLinesValidFile() {
		try {
			app.splitFileByLines("file1.txt", "", 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		assertEquals("", output);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("aa")));
			assertEquals("asdfgh", reader.readLine());
			assertEquals("qwerty", reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File("ab")));
			assertEquals("zxcvbn", reader.readLine());
			reader.close();
			Files.delete(Paths.get("aa"));
			Files.delete(Paths.get("ab"));
		} catch (IOException e) {
			output = "IO Error in test";
		}
		assertEquals("", output);
	}

	@Test
	public void testLinesFileSpecifiedAsAbsolutePath() {
		Path path = Paths.get(Environment.currentDirectory + File.separator + "file1.txt");
		try {
			app.splitFileByLines(path.toString(), "", 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("aa")));
			assertEquals("asdfgh", reader.readLine());
			assertEquals("qwerty", reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File("ab")));
			assertEquals("zxcvbn", reader.readLine());
			reader.close();
			Files.delete(Paths.get("aa"));
			Files.delete(Paths.get("ab"));
		} catch (IOException e) {
			output = "IO Error in test";
		}

		assertEquals("", output);
	}

	@Test
	public void testLinesFileSpecifiedAsRelativePath() {
		Path path = Paths.get(Environment.currentDirectory + File.separator + "src" + File.separator + "file1.txt");
		try {
			app.splitFileByLines(path.toString(), "", 2);
		} catch (Exception e) {
			output = e.getMessage();
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("aa")));
			assertEquals("asdfgh", reader.readLine());
			assertEquals("qwerty", reader.readLine());
			reader.close();
			reader = new BufferedReader(new FileReader(new File("ab")));
			assertEquals("zxcvbn", reader.readLine());
			reader.close();
			Files.delete(Paths.get("aa"));
			Files.delete(Paths.get("ab"));
		} catch (IOException e) {
			output = "IO Error in test";
		}

		assertEquals("", output);
	}

}
