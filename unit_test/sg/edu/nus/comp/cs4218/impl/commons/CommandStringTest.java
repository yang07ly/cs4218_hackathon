package sg.edu.nus.comp.cs4218.impl.commons;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommandStringTest {

	private static final String TEST_STRING = "TestString";
	private static final String TEST = "Test";
	private CommandString testString;
	private String expected, actual;
	CommandString actualCmd, expectedCmd;

	@Before
	public void setUp() throws Exception {
		testString = new CommandString();
		testString = new CommandString(TEST_STRING);
		actual = expected = "";
		actualCmd = new CommandString();
		expectedCmd = new CommandString();
	}

	@Test
	public void testInsertStringAtBefIndex() {
		testString.insertStringAt(0, "Re");
		actual = testString.toString();
		expected = "ReTestString";
		assertEquals(expected, actual);
	}

	@Test
	public void testInsertStringAtAftIndex() {
		testString.insertStringAt(10, "Re");
		actual = testString.toString();
		expected = "TestStringRe";
		assertEquals(expected, actual);
	}

	@Test
	public void testRemoveCharAtBetween() {
		testString.removeCharAt(0);
		actual = testString.toString();
		expected = "estString";
		assertEquals(expected, actual);
	}

	@Test
	public void testRemoveCharAtEnd() {
		testString.removeCharAt(9);
		actual = testString.toString();
		expected = "TestStrin";
		assertEquals(expected, actual);
	}

	@Test
	public void testRemoveRange() {
		testString.removeRange(0, 3);
		actual = testString.toString();
		expected = "tString";
		assertEquals(expected, actual);
	}

	@Test
	public void testRemoveRangeAtEnd() {
		testString.removeRange(9, 10);
		actual = testString.toString();
		expected = "TestStrin";
		assertEquals(expected, actual);

	}

	@Test
	public void testReplaceRange() {
		testString.replaceRange(0, 3, "Wel");
		actual = testString.toString();
		expected = "WeltString";
		assertEquals(expected, actual);
	}

	@Test
	public void testReplaceRangeAtEnd() {
		testString.replaceRange(9, 10, "Well");
		actual = testString.toString();
		expected = "TestStrinWell";
		assertEquals(expected, actual);
	}

	@Test
	public void testIsCharEscaped() {
		boolean expectedBool = false;
		boolean actualBool = testString.isCharEscaped(0);
		assertEquals(expectedBool, actualBool);
	}

	@Test
	public void testSetCharEscaped() {
		boolean[] expectedBoolArr = { true, true, true };
		testString.setCharEscaped(0, expectedBoolArr);
		boolean[] actualBoolArr = new boolean[3];

		for (int i = 0; i < expectedBoolArr.length; i++) {
			actualBoolArr[i] = testString.isCharEscaped(i);
		}
		assertArrayEquals(expectedBoolArr, actualBoolArr);
	}

	@Test
	public void testSetCharEscapedRange() {
		boolean[] expectedBoolArr = { true, true, true };
		testString.setCharEscapedRange(0, 3, true);
		boolean[] actualBoolArr = new boolean[3];

		for (int i = 0; i < expectedBoolArr.length; i++) {
			actualBoolArr[i] = testString.isCharEscaped(i);
		}
		assertArrayEquals(expectedBoolArr, actualBoolArr);
	}

	@Test
	public void testGetIndicesOfCharNotEscaped() {
		Integer[] actualIntArr = testString.getIndicesOfCharNotEscaped('e');
		Integer[] expectedIntArr = { 1 };
		assertArrayEquals(actualIntArr, expectedIntArr);

	}

	@Test
	public void testGetFirstIndexOfCharNotEscaped() {
		int actualInt = testString.getFirstIndexOfCharNotEscaped(0, 'e');
		int expectedInt = 1;
		assertEquals(expectedInt, actualInt);
	}

	@Test
	public void testGetFirstIndexOfCharNotEscapedNotFound() {
		int actualInt = testString.getFirstIndexOfCharNotEscaped(0, 'z');
		int expectedInt = -1;
		assertEquals(expectedInt, actualInt);
	}

	@Test
	public void testCharAt() {
		char expectedChar = 'T';
		char actualChar = testString.charAt(0);
		assertEquals(expectedChar, actualChar);
	}

	@Test
	public void testSubstring() {
		actual = testString.substring(0, 4).toString();
		expected = TEST;
		assertEquals(expected, actual);
	}

	@Test
	public void testToString() {
		expected = TEST_STRING;
		actual = new String(testString.toString());
		assertEquals(expected, actual);
	}

	@Test
	public void testLength() {
		int expectedInt = 10;
		int actualInt = testString.length();
		assertEquals(expectedInt, actualInt);
	}

	@Test
	public void testEqualsTrue() {
		actualCmd = new CommandString(TEST_STRING);
		assertTrue(testString.equals(actualCmd));
	}

	@Test
	public void testEqualsTrueWithItsIntance() {
		assertTrue(testString.equals(testString));
	}

	@Test
	public void testEqualsTrueFalseSameObj() {
		actualCmd = new CommandString("TeString");
		assertFalse(testString.equals(actualCmd));
	}

	@Test
	public void testEqualsTrueFalseDiffObj() {
		actualCmd = new CommandString(TEST_STRING);
		assertTrue(testString.equals(actualCmd));
	}

	@Test
	public void testHashCode() {
		actual = TEST_STRING;
		int actualInt = actual.hashCode();
		int expectedInt = testString.hashCode();
		assertEquals(expectedInt, actualInt);
	}

	@Test
	public void testTrimSpacesBeforeNAfter() {
		actualCmd = new CommandString(" Test ");
		actualCmd = actualCmd.trim();
		expectedCmd = new CommandString(TEST);
		assertEquals(actualCmd, expectedCmd);
	}

	@Test
	public void testTrimStringWithOnlySpaces() {
		actualCmd = new CommandString("  ");
		actualCmd = actualCmd.trim();
		expectedCmd = new CommandString();
		assertEquals(actualCmd, expectedCmd);
	}

	@Test
	public void testTrimTabs() {
		actualCmd = new CommandString("	Test");
		actualCmd = actualCmd.trim();
		expectedCmd = new CommandString(TEST);
		assertEquals(actualCmd, expectedCmd);
	}

	@Test
	public void testTrimNewLine() {
		actualCmd = new CommandString(TEST + "Now");
		actualCmd = actualCmd.trim();
		expectedCmd = new CommandString("TestNow");
		assertEquals(actualCmd, expectedCmd);
	}

}
