package fi.tikesos.rdfa.core.exception;

/**
 * ProfileLoadException is thrown when profile fails
 * to be read by ProfileHander
 * 
 * @author ssakorho
 *
 */
public class ProfileLoadException extends RDFaException {
	private static final long serialVersionUID = 1L;
	private String profileURI;

	/**
	 * Constructor
	 * 
	 * @param profileURI
	 * @param line
	 * @param column
	 * @param cause
	 */
	public ProfileLoadException(String profileURI, long line, long column,
			Throwable cause) {
		super(cause, line, column);
		this.profileURI = profileURI;
	}
	
	/**
	 * @return
	 */
	public String getProfileURI() {
		return profileURI;
	}
}
