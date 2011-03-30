package fi.tikesos.rdfa.core.exception;

/**
 * ProfileHandlerNotDefinedException is used to indicate
 * that document requests RDFa profile to be loaded, but
 * ProfileHandler is not defined 
 * 
 * @author ssakorho
 *
 */
public class ProfileHandlerNotDefinedException extends RDFaException {
	private static final long serialVersionUID = 1L;
	private String profileURI;

	/**
	 * Constructor
	 * 
	 * @param profileURI
	 * @param line
	 * @param column
	 */
	public ProfileHandlerNotDefinedException(String profileURI, long line,
			long column) {
		super(line, column);
		this.profileURI = profileURI;
	}
	
	/**
	 * @return
	 */
	public String getProfileURI() {
		return profileURI;
	}
}
