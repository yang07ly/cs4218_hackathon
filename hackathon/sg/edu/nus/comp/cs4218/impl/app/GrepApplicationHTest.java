package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.SedException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class GrepApplicationHTest {
    private static final String DASH = "-";

    private static final String TEST_PATTERN_1 = "H.*\\d";

    private static final String THIS_KEYWORD = "This";

    private static final String TAB1_CONTENT = "\t\tabcd"; // with 2 tabs before the text
    private static final String TAB2_CONTENT = "\t\t"; // 2 tabs only

    private static final String SPACE1_CONTENT = "  abcd"; // with 2 spaces before the text
    private static final String SPACE2_CONTENT = "  "; // 2 spaces only

    private static final String HELLO1_CONTENT = "Hello world 1";
    private static final String HELLO2_CONTENT = "Hello world 2";

    private static final String S_FILE1_CONTENT = THIS_KEYWORD + " is single line 1";
    private static final String S_FILE2_CONTENT = THIS_KEYWORD + " is single line 2";
    private static final String M_FILE1_CONTENT1 = THIS_KEYWORD + " is multiple1 line 1";
    private static final String M_FILE1_CONTENT2 = THIS_KEYWORD + " is multiple1 line 2";
    private static final String M_FILE2_CONTENT1 = THIS_KEYWORD + " is multiple2 line 1";
    private static final String M_FILE2_CONTENT2 = THIS_KEYWORD + " is multiple2 line 2";

    private static final String ST_FILE_CONTENT2 = "   hello world2";

    private static final String TEST_FOLDER = "test_files";
    private static final String GREP_TEST_FOL_REL = "test/sg/edu/nus/comp/cs4218/impl/app/testFiles/";
    private static final String S_FILE1_REL_PATH = GREP_TEST_FOL_REL + "single1.txt";
    private static final String S_FILE2_REL_PATH = GREP_TEST_FOL_REL + "single2.txt";
    private static final String M_FILE1_REL_PATH = GREP_TEST_FOL_REL + "multiple1.txt";
    private static final String M_FILE2_REL_PATH = GREP_TEST_FOL_REL + "multiple2.txt";
    private static final String NON_V_REL_PATH = GREP_TEST_FOL_REL + "nonvowel.txt";
    private static final String ST_FILE_REL_PATH = GREP_TEST_FOL_REL + "spacetab.txt";

    private static final String STDIN_PRE = "(standard input)" + ":";
    private static final String S_F1_REL_PRE = S_FILE1_REL_PATH + ":";
    private static final String S_F2_REL_PRE = S_FILE2_REL_PATH + ":";
    private static final String M_F1_REL_PRE = M_FILE1_REL_PATH + ":";
    private static final String M_F2_REL_PRE = M_FILE2_REL_PATH + ":";
    private static final String ST_F_REL_PRE = ST_FILE_REL_PATH + ":";


    private Application grepApp;
    private ByteArrayOutputStream out;
    private ShellImpl shell;
    private ByteArrayOutputStream os;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        grepApp = new GrepApplication();
        out = new ByteArrayOutputStream();
        shell = new ShellImpl();
        System.setOut(new PrintStream(outContent));

        OutputStream bqOutputStream = new ByteArrayOutputStream();
        os = (ByteArrayOutputStream) bqOutputStream;
    }

    @Test(expected = SedException.class)
    public void grepNoPattern() throws AbstractApplicationException, ShellException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pStream = new PrintStream(baos);
        System.setOut(pStream);
        shell.parseAndEvaluate("grep two-lines.txt", System.out);
    }

    @Test(expected = SedException.class)
    public void testGrepNoFileNoStdin() throws AbstractApplicationException, ShellException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pStream = new PrintStream(baos);
        System.setOut(pStream);
        shell.parseAndEvaluate("grep e", System.out);
    }


}
