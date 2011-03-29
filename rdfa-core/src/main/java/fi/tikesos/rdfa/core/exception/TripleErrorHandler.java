package fi.tikesos.rdfa.core.exception;

import fi.tikesos.rdfa.core.exception.ErrorHandler;
import fi.tikesos.rdfa.core.triple.TripleSink;

public class TripleErrorHandler implements ErrorHandler {
	private TripleSink tripleSink;
	
	public TripleErrorHandler(TripleSink tripleSink) {
		this.tripleSink = tripleSink;
	}
	
	@Override
	public void warning(Exception exception) {
		// Generate a triple from exception
		exception.printStackTrace();
	}

	@Override
	public void fatalError(Exception exception) {
		// Generate a triple from exception
		exception.printStackTrace();
	}
}
