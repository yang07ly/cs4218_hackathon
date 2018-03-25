package sg.edu.nus.comp.cs4218.impl.commons;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandStringTest {
	
	CommandString TestString;

	@Before
	public void setUp() throws Exception {
		TestString = new CommandString();
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
		TestString.removeCharAt(9);
		Actual =TestString.toString();
		String Expected = new String("TestStrin");
		assertEquals(Expected,Actual);
	}

	@Test
	public void testRemoveRange() {
		String Actual = new String();
		TestString.removeRange(0, 3);
		Actual =TestString.toString();
		String Expected = new String("tString");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testRemoveRangeAtEnd() {
		String Actual = new String();
		TestString.removeRange(9, 10);
		Actual =TestString.toString();
		String Expected = new String("TestStrin");
		assertEquals(Expected,Actual);
		
	}

	@Test
	public void testReplaceRange() {
		String Actual = new String();
		TestString.replaceRange(0, 3, "Wel");
		Actual =TestString.toString();
		String Expected = new String("WeltString");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testReplaceRangeAtEnd() {
		String Actual = new String();
		TestString.replaceRange(9, 10, "Well");
		Actual =TestString.toString();
		String Expected = new String("TestStrinWell");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testIsCharEscaped() {
		boolean Expected = false;
		boolean Actual = TestString.isCharEscaped(0);
		assertEquals(Expected,Actual);
	}

	@Test
	public void testSetCharEscaped() {
		boolean[] Expected = {true, true,true};
		TestString.setCharEscaped(0, Expected);
		boolean[] Actual = new boolean[3];
		
		for(int i = 0; i < Expected.length; i++) {
			Actual[i] = TestString.isCharEscaped(i);
		}
		assertArrayEquals(Actual, Expected);
	}

	@Test
	public void testSetCharEscapedRange() {
		boolean bool = true;
		boolean[] Expected = {true, true,true};
		TestString.setCharEscapedRange(0, 3, bool);
		boolean[] Actual = new boolean[3];
		
		for(int i = 0; i < Expected.length; i++) {
			Actual[i] = TestString.isCharEscaped(i);
		}
		assertArrayEquals(Actual, Expected);
	}

	@Test
	public void testGetIndicesOfCharNotEscaped() {
		
		Integer[] Actual = TestString.getIndicesOfCharNotEscaped('e');
		Integer[] Expected = {1};
		assertArrayEquals(Actual, Expected);
		
	}

	@Test
	public void testGetFirstIndexOfCharNotEscaped() {
		int Actual = TestString.getFirstIndexOfCharNotEscaped(0, 'e');
		int Expected = 1;
		assertEquals(Actual, Expected);
	}
	
	@Test
	public void testGetFirstIndexOfCharNotEscapedNotFound() {
		int Actual = TestString.getFirstIndexOfCharNotEscaped(0, 'z');
		int Expected = -1;
		assertEquals(Actual, Expected);
	}

	@Test
	public void testCharAt() {
		char Expected = 'T';
		char Actual = TestString.charAt(0);
		assertEquals(Expected,Actual);
	}

	@Test
	public void testSubstring() {
		String Actual = new String();
		Actual= TestString.substring(0, 4).toString();
		
		String Expected = new String("Test");
		assertEquals(Expected,Actual);
		
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
	
	@Test
	public void testEqualsTrue() {
		CommandString Actual = new CommandString("TestString");
		
		assertTrue(TestString.equals(Actual));
		
	}
	@Test
	public void testEqualsTrueWithItsIntance() {
		
		assertTrue(TestString.equals(TestString));
		
	}
	
	@Test
	public void testEqualsTrueFalseSameObj() {
		CommandString Actual = new CommandString("TeString");
		
		assertFalse(TestString.equals(Actual));
		
	}
	
	@Test
	public void testEqualsTrueFalseDiffObj() {
		String Actual = new String("TestString");
		
		assertFalse(TestString.equals(Actual));
		
	}
	
	
	@Test
	public void testHashCode() {
		String ActualString = new String("TestString");
		int Actual = ActualString.hashCode();
		int Expected = TestString.hashCode();
		
		assertEquals(Expected, Actual);
		
	}
	
	@Test
	public void testTrimSpacesBeforeNAfter() {
		CommandString Actual = new CommandString(" Test ");
		Actual = Actual.trim();
		CommandString Expected = new CommandString("Test");
		assertEquals(Actual,Expected);
	}
	
	@Test
	public void testTrimStringWithOnlySpaces() {
		CommandString Actual = new CommandString("  ");
		Actual=Actual.trim();
		CommandString Expected = new CommandString();
		assertEquals(Actual,Expected);
	}
	
	@Test
	public void testTrimTabs() {
		CommandString Actual = new CommandString("	Test");
		Actual=Actual.trim();
		CommandString Expected = new CommandString("Test");
		assertEquals(Actual,Expected);
	}
	
	@Test
	public void testTrimNewLine() {
		CommandString Actual = new CommandString("Test"
				+ "Now");
		Actual=Actual.trim();
		CommandString Expected = new CommandString("TestNow");
		assertEquals(Actual,Expected);
	}
	
	
	

}

