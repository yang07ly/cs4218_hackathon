package sg.edu.nus.comp.cs4218.impl.commons;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandStringTest {
	
	CommandString TestString;

	@Before
	public void setUp() throws Exception {
		TestString = new CommandString("TestString");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsertStringAtBefIndex() {
		String Actual = new String();
		TestString.insertStringAt(0, "Re");
		Actual =TestString.toString();
		String Expected = new String("ReTestString");
		assertEquals(Expected,Actual);
	}
	@Test
	public void testInsertStringAtAftIndex() {
		String Actual = new String();
		TestString.insertStringAt(10, "Re");
		Actual =TestString.toString();
		String Expected = new String("TestStringRe");
		assertEquals(Expected,Actual);
	}

	@Test
	public void testRemoveCharAtBetween() {
		String Actual = new String();
		TestString.removeCharAt(0);
		Actual =TestString.toString();
		String Expected = new String("estString");
		assertEquals(Expected,Actual);		
	}
	@Test
	public void testRemoveCharAtEnd() {
		String Actual = new String();
		TestString.removeCharAt(10);
		Actual =TestString.toString();
		String Expected = new String("TestStrin");
		assertEquals(Expected,Actual);
	}

	@Test
	public void testRemoveRange() {
		String Actual = new String();
		TestString.removeRange(0, 3);
		Actual =TestString.toString();
		String Expected = new String("String");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testRemoveRangeAtEnd() {
		String Actual = new String();
		TestString.removeRange(9, 11);
		Actual =TestString.toString();
		String Expected = new String("TestStrin");
		assertEquals(Expected,Actual);
		
	}

	@Test
	public void testReplaceRange() {
		String Actual = new String();
		TestString.replaceRange(0, 3, "Well");
		Actual =TestString.toString();
		String Expected = new String("WellString");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testReplaceRangeAtEnd() {
		String Actual = new String();
		TestString.replaceRange(10, 11, "Well");
		Actual =TestString.toString();
		String Expected = new String("TestStrinWell");
		assertEquals(Expected,Actual);
	}

	@Test
	public void testSetCharEscaped() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCharEscapedRange() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIndicesOfCharNotEscaped() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFirstIndexOfCharNotEscaped() {
		fail("Not yet implemented");
	}

	@Test
	public void testCharAt() {
		char Expected = 'T';
		char Actual = TestString.charAt(0);
		assertEquals(Expected,Actual);
	}
	

	@Test
	public void testIsCharEscaped() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubstring() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		String Expected = new String("TestString");
		String Actual = new String(TestString.toString());
		assertEquals(Expected,Actual);
	}

	@Test
	public void testLength() {
		int Expected = 10, Actual = 0;
		Actual = TestString.length();
		assertEquals(Expected,Actual);
	}

}
