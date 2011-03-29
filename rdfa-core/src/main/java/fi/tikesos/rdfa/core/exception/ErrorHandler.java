package fi.tikesos.rdfa.core.exception;

public interface ErrorHandler {
	public void warning(Exception exception);
	public void fatal(Exception exception);
}
