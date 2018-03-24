package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.EchoException;

public class EchoApplicationTest {

	private static final String TEXT1 = "text1";
	private static final String TEXT2 = "text2";
	private static final String SPACE = " ";
	private static final String SPACES = "    ";
	private static final String EMPTY = "";
	private static final String TAB = "\t";

	private EchoApplication echoApp;
	private String expected, result;
	private OutputStream stdout;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		echoApp = new EchoApplication();
		stdout = new ByteArrayOutputStream();
	}

	@Test
	public void testRunToEchoTextUsingOneStrInArg() throws EchoException {
		expected = TEXT1;
		String[] strArr = { TEXT1 };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoMultiTextUsingMultiStrsInArg() throws EchoException {
		expected = TEXT1 + SPACE + TEXT2;
		String[] strArr = { TEXT1, TEXT2 };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoNothingUsingEmptyStrInArg() throws EchoException {
		expected = EMPTY;
		String[] strArr = { EMPTY };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoSpaceUsingMultiEmptyStrsInArg() throws EchoException {
		expected = SPACE;
		String[] strArr = { EMPTY, EMPTY };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoMultiSpacesUsingSpacesInArg() throws EchoException {
		expected = SPACES;
		String[] strArr = { SPACES };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoEmptyUsingEmptyArg() throws EchoException {
		expected = EMPTY;
		String[] strArr = {};

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoTextWithSpaceUsingStrWithSpaceInArg() throws EchoException {
		expected = TEXT1 + SPACES + TEXT2;
		String[] strArr = { TEXT1 + SPACES + TEXT2 };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoSpacesAtFrontUsingStrWithSpacesAtFrontInArg() throws EchoException {
		expected = SPACES + TEXT2;
		String[] strArr = { SPACES + TEXT2 };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoSpacesAtEndUsingStrWithSpacesAtEndInArg() throws EchoException {
		expected = TEXT1 + SPACES;
		String[] strArr = { TEXT1 + SPACES };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoMultiTextAndSpacesUsingMultiStrWithSpacesInArg() throws EchoException {
		expected = TEXT1 + SPACES + SPACE + TEXT1 + SPACES + TEXT2 + SPACE + SPACES + TEXT2;
		String[] strArr = { TEXT1 + SPACES, TEXT1 + SPACES + TEXT2, SPACES + TEXT2 };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoTabUsingTabStrInArg() throws EchoException {
		expected = TAB;
		String[] strArr = { TAB };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoTabAtFrontUsingStrWuthTabAtFrontInArg() throws EchoException {
		expected = TAB + TEXT2;
		String[] strArr = { TAB + TEXT2 };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoTabAtEndUsingStrWuthTabAtEndInArg() throws EchoException {
		expected = TEXT1 + TAB;
		String[] strArr = { TEXT1 + TAB };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToEchoMultiTextAndTabUsingMultiStrWithTabInArg() throws EchoException {
		expected = TEXT1 + TAB + SPACE + TEXT1 + TAB + TEXT2 + SPACE + TAB + TEXT2;
		String[] strArr = { TEXT1 + TAB, TEXT1 + TAB + TEXT2, TAB + TEXT2 };

		echoApp.run(strArr, null, stdout);
		result = stdout.toString();
		assertEquals(expected, result);
	}

	@Test
	public void testRunToThrowsEchoExpUsingNullArg() throws EchoException {
		thrown.expect(EchoException.class);
		thrown.expectMessage("echo: Null arguments");
		echoApp.run(null, null, stdout);
	}

	@Test
	public void testRunToThrowsEchoExpUsingNullOutputStream() throws EchoException {
		thrown.expect(EchoException.class);
		thrown.expectMessage("echo: OutputStream not provided");
		echoApp.run(new String[] { TEXT1 }, null, null);
	}
}
