package sg.edu.nus.comp.cs4218.impl.commons;

import static org.junit.Assert.*;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandStringTest {
	
	CommandString testString;

	@Before
	public void setUp() throws Exception {
		testString = new CommandString();
		testString = new CommandString("TestString");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsertStringAtBefIndex() {
		String actual = new String();
		testString.insertStringAt(0, "Re");
		actual =testString.toString();
		String expected = new String("ReTestString");
		assertEquals(expected,actual);
	}
	@Test
	public void testInsertStringAtAftIndex() {
		String actual = new String();
		testString.insertStringAt(10, "Re");
		actual =testString.toString();
		String expected = new String("TestStringRe");
		assertEquals(expected,actual);
	}

	@Test
	public void testRemoveCharAtBetween() {
		String actual = new String();
		testString.removeCharAt(0);
		actual =testString.toString();
		String Expected = new String("estString");
		assertEquals(Expected,actual);		
	}
	@Test
	public void testRemoveCharAtEnd() {
		String Actual = new String();
		testString.removeCharAt(9);
		Actual =testString.toString();
		String Expected = new String("TestStrin");
		assertEquals(Expected,Actual);
	}

	@Test
	public void testRemoveRange() {
		String Actual = new String();
		testString.removeRange(0, 3);
		Actual =testString.toString();
		String Expected = new String("tString");
		assertEquals(Expected,Actual);
	}
	
	
	@Test
	public void testRemoveRangeAtEnd() {
		String Actual = new String();
		testString.removeRange(9, 10);
		Actual =testString.toString();
		String Expected = new String("TestStrin");
		assertEquals(Expected,Actual);
		
	}

	@Test
	public void testReplaceRange() {
		String Actual = new String();
		testString.replaceRange(0, 3, "Wel");
		Actual =testString.toString();
		String Expected = new String("WeltString");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testReplaceRangeAtEnd() {
		String Actual = new String();
		testString.replaceRange(9, 10, "Well");
		Actual =testString.toString();
		String Expected = new String("TestStrinWell");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testIsCharEscaped() {
		boolean Expected = false;
		boolean Actual = testString.isCharEscaped(0);
		assertEquals(Expected,Actual);
	}

	@Test
	public void testSetCharEscaped() {
		boolean[] Expected = {true, true,true};
		testString.setCharEscaped(0, Expected);
		boolean[] Actual = new boolean[3];
		
		for(int i = 0; i < Expected.length; i++) {
			Actual[i] = testString.isCharEscaped(i);
		}
		assertArrayEquals(Actual, Expected);
	}

	@Test
	public void testSetCharEscapedRange() {
		boolean bool = true;
		boolean[] Expected = {true, true,true};
		testString.setCharEscapedRange(0, 3, bool);
		boolean[] Actual = new boolean[3];
		
		for(int i = 0; i < Expected.length; i++) {
			Actual[i] = testString.isCharEscaped(i);
		}
		assertArrayEquals(Actual, Expected);
	}

	@Test
	public void testGetIndicesOfCharNotEscaped() {
		
		Integer[] Actual = testString.getIndicesOfCharNotEscaped('e');
		Integer[] Expected = {1};
		assertArrayEquals(Actual, Expected);
		
	}

	@Test
	public void testGetFirstIndexOfCharNotEscaped() {
		int Actual = testString.getFirstIndexOfCharNotEscaped(0, 'e');
		int Expected = 1;
		assertEquals(Actual, Expected);
	}
	
	@Test
	public void testGetFirstIndexOfCharNotEscapedNotFound() {
		int Actual = testString.getFirstIndexOfCharNotEscaped(0, 'z');
		int Expected = -1;
		assertEquals(Actual, Expected);
	}

	@Test
	public void testCharAt() {
		char Expected = 'T';
		char Actual = testString.charAt(0);
		assertEquals(Expected,Actual);
	}

	@Test
	public void testSubstring() {
		String Actual = new String();
		Actual= testString.substring(0, 4).toString();
		
		String Expected = new String("Test");
		assertEquals(Expected,Actual);
		
	}

	@Test
	public void testToString() {
		String Expected = new String("TestString");
		String Actual = new String(testString.toString());
		assertEquals(Expected,Actual);
	}

	@Test
	public void testLength() {
		int Expected = 10, Actual = 0;
		Actual = testString.length();
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testEqualsTrue() {
		CommandString Actual = new CommandString("TestString");
		
		assertTrue(testString.equals(Actual));
		
	}
	@Test
	public void testEqualsTrueWithItsIntance() {
		
		assertTrue(testString.equals(testString));
		
	}
	
	@Test
	public void testEqualsTrueFalseSameObj() {
		CommandString Actual = new CommandString("TeString");
		
		assertFalse(testString.equals(Actual));
		
	}
	
	@Test
	public void testEqualsTrueFalseDiffObj() {
		String Actual = new String("TestString");
		
		assertFalse(testString.equals(Actual));
		
	}
	
	
	@Test
	public void testHashCode() {
		String ActualString = new String("TestString");
		int Actual = ActualString.hashCode();
		int Expected = testString.hashCode();
		
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

