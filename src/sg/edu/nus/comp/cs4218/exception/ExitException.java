package sg.edu.nus.comp.cs4218.exception;

public class ExitException extends AbstractApplicationException{
	
	private static final long serialVersionUID = 3100988233903874795L;

	public ExitException(String message) {
		super("exit: " + message);
	}
}
