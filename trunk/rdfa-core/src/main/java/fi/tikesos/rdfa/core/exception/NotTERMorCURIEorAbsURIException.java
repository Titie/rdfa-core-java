package fi.tikesos.rdfa.core.exception;

/**
 * NotTERMorCURIEorAbsURI is thrown when
 * TERMorCURIEorAbsURI fails to be resolved
 * 
 * @author ssakorho
 *
 */
public class NotTERMorCURIEorAbsURIException extends RDFaException {
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
	public NotTERMorCURIEorAbsURIException(String elementName,
			long line, long column,
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
