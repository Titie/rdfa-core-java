package fi.tikesos.rdfa.core.exception;

public class NotURIException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NotURIException(String string, long line, long column,
			Throwable cause) {
		super(cause);
	}
}
