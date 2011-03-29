package fi.tikesos.rdfa.core.exception;

public class NotTERMorCURIEorAbsURIException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotTERMorCURIEorAbsURIException(String elementName,
			long line, long column,
			Throwable cause) {
		super(cause);
	}
}
