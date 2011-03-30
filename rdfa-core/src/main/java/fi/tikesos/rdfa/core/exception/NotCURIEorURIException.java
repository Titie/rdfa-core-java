package fi.tikesos.rdfa.core.exception;

/**
 * NotCURIEorURIException is thrown when CURIEorURI
 * fails to be resolved
 * 
 * @author ssakorho
 *
 */
public class NotCURIEorURIException extends RDFaException {
	private static final long serialVersionUID = 1L;
	private String elementName;

	/**
	 * Constructor
	 * 
	 * @param elementName
	 * @param line
	 * @param column
	 * @param cause
	 */
	public NotCURIEorURIException(String elementName, long line, long column,
			Throwable cause) {
		super(cause, line, column);
		this.elementName = elementName;
	}
	
	/**
	 * @return
	 */
	public String getElementName() {
		return elementName;
	}
}
