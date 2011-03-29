package fi.tikesos.rdfa.core.exception;

public class ProfileLoadException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProfileLoadException(String profileURI, long line, long column,
			Throwable cause) {
		super(cause);
	}
}
