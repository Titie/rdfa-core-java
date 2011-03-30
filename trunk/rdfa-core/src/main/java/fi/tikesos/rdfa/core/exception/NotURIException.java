package fi.tikesos.rdfa.core.exception;

/**
 * NotURIException is thrown when URI fails
 * consistency tests
 * 
 * @author ssakorho
 *
 */
public class NotURIException extends RDFaException {
	private static final long serialVersionUID = 1L;
	private String uri;
	
	/**
	 * Constructor
	 * 
	 * @param uri
	 * @param line
	 * @param column
	 * @param cause
	 */
	public NotURIException(String uri, long line, long column,
			Throwable cause) {
		super(cause, line, column);
		this.uri = uri;
	}
	
	/**
	 * @return
	 */
	public String getURI() {
		return uri;
	}
}
