package sg.edu.nus.comp.cs4218.exception;

public class CmpException extends AbstractApplicationException {

	private static final long serialVersionUID = -7901129224576219702L;

	public CmpException(String message) {
		super("cmp: " + message);
	}
}
