package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class CommandSubstitutionTest {

	SequenceCommand sequenceCommand;
	String expected,result;

	@Before
	public void setup() {
		sequenceCommand = new SequenceCommand();
	}
	
	@Test 
	public void testInvalidAppName() {
		expected = "shell: xyz: Invalid app.";
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo `xyz cats`");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}
	
	@Test 
	public void testInvalidFrontBQ(){
		expected = "shell: Invalid syntax encountered.";
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo ``echo cats`");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}

	@Test 
	public void testInvalidBackBQ(){
		expected = "shell: Invalid syntax encountered.";
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo `echo cats``");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}
	
	@Test 
	public void testBQInBQ(){
		expected = "shell: Invalid syntax encountered.";
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo `echo `echo cats``");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}
	
	@Test 
	public void testBQWithSemicolon(){
		expected = "cats dogs" + "\n";
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo `echo cats; echo dogs`");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}
	
	@Test 
	public void testCommandSub(){
		expected = "On the other hand, we denounce with righteous"
				+ " indignation and dislike men who are so beguiled "
				+ "and demoralized by the charms of pleasure of the moment,"
				+ " so blinded by desire, that they cannot foresee the pain"
				+ " and trouble that are bound to ensue; and equal blame "
				+ "belongs to those who fail in their duty through weakness of will,"
				+ " which is the same as saying through shrinking from toil and pain."
				+ " These cases are perfectly simple and easy to distinguish."
				+ " In a free hour, when our power of choice is untrammelled and"
				+ " when nothing prevents our being able to do what we like best,"
				+ " every pleasure is to be welcomed and every pain avoided."
				+ " But in certain circumstances and owing to the claims of duty "
				+ "or the obligations of business it will frequently occur that"
				+ " pleasures have to be repudiated and annoyances accepted."
				+ " The wise man therefore always holds in these matters to this"
				+ " principle of selection: he rejects pleasures to secure other"
				+ " greater pleasures, or else he endures pains to avoid worse "
				+ "pains\n"; 
				
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("cat `echo text.txt`");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}
	
	@Test 
	public void testCommandSubWithPipe(){
		expected = "On the other hand, we denounce with righteous"
				+ " indignation and dislike men who are so beguiled "
				+ "and demoralized by the charms of pleasure of the moment,"
				+ " so blinded by desire, that they cannot foresee the pain"
				+ " and trouble that are bound to ensue; and equal blame "
				+ "belongs to those who fail in their duty through weakness of will,"
				+ " which is the same as saying through shrinking from toil and pain."
				+ " These cases are perfectly simple and easy to distinguish."
				+ " In a free hour, when our power of choice is untrammelled and"
				+ " when nothing prevents our being able to do what we like best,"
				+ " every pleasure is to be welcomed and every pain avoided."
				+ " But in certain circumstances and owing to the claims of duty "
				+ "or the obligations of business it will frequently occur that"
				+ " pleasures have to be repudiated and annoyances accepted."
				+ " The wise man therefore always holds in these matters to this"
				+ " principle of selection: he rejects pleasures to secure other"
				+ " greater pleasures, or else he endures paining to avoid worse "
				+ "paining\n"; 
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo `cat text.txt | sed s/pains/paining/`");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}
	
	@Test 
	public void testCommandSubWithMultiplePipe(){
		expected = "On the other hand, we denounce with righteous"
				+ " indignation and dislike men who are so beguiled "
				+ "and demoralized by the charms of pleasure of the moment,"
				+ " so blinded by desire, that they cannot foresee the pain"
				+ " and trouble that are bound to ensue; and equal blame "
				+ "belongs to those who fail in their duty through weakness of will,"
				+ " which is the same as saying through shrinking from toil and pain."
				+ " These cases are perfectly simple and easy to distinguish."
				+ " In a free hour, when our power of choice is untrammelled and"
				+ " when nothing prevents our being able to do what we like best,"
				+ " every pleasure is to be welcomed and every pain avoided."
				+ " But in certain circumstances and owing to the claims of duty "
				+ "or the obligations of business it will frequently occur that"
				+ " pleasuring have to be repudiated and annoyances accepted."
				+ " The wise man therefore always holds in these matters to this"
				+ " principle of selection: he rejects pleasuring to secure other"
				+ " greater pleasuring, or else he endures paining to avoid worse "
				+ "paining\n"; 
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo `cat text.txt | sed s/pains/paining/ | sed s/pleasures/pleasuring/`");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}
	
	@Test 
	public void testCommandSubWithMultipleSequence(){
		expected = "a b\n"; 
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		sequenceCommand = new SequenceCommand("echo `echo a ; echo b`");
		try {
			sequenceCommand.parse();
			sequenceCommand.evaluate(System.in, stdout);
			result = new String(stdout.toByteArray());
		} catch (AbstractApplicationException e) {
			result = e.getMessage();
		} catch (ShellException e) {
			result = e.getMessage();
		}
		assertEquals(expected, result);
		
	}
	
}
