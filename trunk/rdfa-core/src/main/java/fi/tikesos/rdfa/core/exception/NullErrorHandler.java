package fi.tikesos.rdfa.core.exception;

/**
 * NullErrorHandler does absolutely nothing.
 * 
 * @author ssakorho
 *
 */
public class NullErrorHandler implements ErrorHandler {
	public void warning(Exception exception) {
		// Does absolutely nothing!
	}

	public void fatalError(Exception exception) {
		// Does absolutely nothing!
	}
}
