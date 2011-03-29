package fi.tikesos.rdfa.core.exception;

public class NotCURIEorURIException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotCURIEorURIException(String elementName, long line, long column,
			Throwable cause) {
		super(cause);
	}
}
