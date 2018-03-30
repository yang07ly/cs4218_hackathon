package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.LsException;
import sg.edu.nus.comp.cs4218.impl.commons.OSUtil;

public class LsApplicationHTest {

	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system"
			+ File.separator + "ls_test_system";

	private static final String STR_COLON = ":";
	private static final String STR_TABS = "\t";

	private static final String FOLDER1 = "folder1";
	private static final String FOLDER1_1 = "folder1_1";
	private static final String FOLDER2 = "folder2";
	private static final String FOLDER_WITH_SPACE = "folder name with space";

	private static final String ABS_FOLDER1 = TEST_DIR + File.separator + FOLDER1;
	private static final String ABS_FOLDER2 = TEST_DIR + File.separator + FOLDER2;
	private static final String ABS_FOLDER1_1 = ABS_FOLDER1 + File.separator + FOLDER1_1;

	private static final String REL_FOLDER1_1 = FOLDER1 + File.separator + FOLDER1_1;

	private static final String HEAD_FOLDER1 = FOLDER1 + STR_COLON;
	private static final String HEAD_FOLDER2 = FOLDER2 + STR_COLON;
	private static final String HEAD_FOLDER1_1 = FOLDER1 + File.separator + FOLDER1_1 + STR_COLON;
	private static final String HEAD_ABS_FOLDER1 = ABS_FOLDER1 + STR_COLON;
	private static final String HEAD_ABS_FOLDER2 = ABS_FOLDER2 + STR_COLON;
	private static final String HEAD_ABS_FOLDER11 = ABS_FOLDER1_1 + STR_COLON;

	private static final String FILE1 = "file1.txt";
	private static final String FILE2 = "file2.txt";
	private static final String FOLDER1_FILE1 = "file1_in_folder1.txt";
	private static final String FOLDER2_FILE1 = "file1_in_folder2.txt";
	private static final String FOLDER2_FILE2 = "file2_in_folder2.txt";
	private static final String FOLDER1_1_FILE2 = "file2_in_folder1_1.txt";
	private static final String FILE_WITH_SPACE = "file name with space.txt";
	private static final String FILE_HIDDEN = ".hiddenFile1.txt";
	private static final String FILE_NON_EXISTENT = "nonExistentFile";

	private static final String REL_FOLDER1_FILE1 = FOLDER1 + File.separator + FOLDER1_FILE1;
	private static final String REL_FOLDER2_FILE1 = FOLDER2 + File.separator + FOLDER2_FILE1;
	private static final String REL_FOLDER2_FILE2 = FOLDER2 + File.separator + FOLDER2_FILE2;
	private static final String REL_F1_1_FILE2 = FOLDER1 + File.separator + FOLDER1_1 + File.separator
			+ FOLDER1_1_FILE2;

	private static final String ABS_FILE1 = TEST_DIR + File.separator + FILE1;
	private static final String ABS_FOLDER1_FILE1 = TEST_DIR + File.separator + REL_FOLDER1_FILE1;
	private static final String ABS_F1_1_FILE2 = TEST_DIR + File.separator + REL_F1_1_FILE2;

	private static final String CUR_CONTENT = "'file name with space.txt'" + STR_TABS + "file1.txt" + STR_TABS
			+ "file2.txt" + STR_TABS + "'folder name with space'" + STR_TABS + "folder1" + STR_TABS + "folder2";
	private static final String FOLDER1_CONTENT = "file1_in_folder1.txt" + STR_TABS + "file2_in_folder1.txt" + STR_TABS
			+ "folder1_1";
	private static final String FOLDER2_CONTENT = "file1_in_folder2.txt" + STR_TABS + "file2_in_folder2.txt";
	private static final String FOLDER1_1_CONTENT = "file1_in_folder1_1.txt" + STR_TABS + "file2_in_folder1_1.txt";
	private static final String FOLDER_WS_CONTENT = "file1_in_folder_spaces.txt" + STR_TABS
			+ "file2_in_folder_spaces.txt";

	private LsApplication lsApp;
	private String expected, result;
	private OutputStream stdout;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws LsException {
		Environment.currentDirectory = TEST_DIR;
		lsApp = new LsApplication();
		stdout = new ByteArrayOutputStream();
	}
	
	/*
	 * Test Case LS1
	 */
	@Test
	public void testHiddenFile() throws LsException {
		Environment.currentDirectory = System.getProperty("user.dir");
		String[] args = {"-R"};
		lsApp.run(args, null, stdout);
		result = stdout.toString();
		assertFalse(result.contains(".git"));
	}
}
