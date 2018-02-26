package sg.edu.nus.comp.cs4218.exception;

public class QuoteException extends AbstractApplicationException {

	private static final long serialVersionUID = -1455865627627481803L;

	public QuoteException(String message) {
		super("Quote: " + message);
	}
}
