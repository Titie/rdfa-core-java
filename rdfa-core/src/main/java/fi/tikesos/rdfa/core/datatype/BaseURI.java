package fi.tikesos.rdfa.core.datatype;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Containter class for storing Base URI
 * 
 * @author ssakorho
 *
 */
public class BaseURI {
	private URI value;
	
	/**
	 * Class constructor
	 * 
	 * @param uri
	 * @throws URISyntaxException 
	 */
	public BaseURI(String uri) throws URISyntaxException {
		value = new URI(uri);
	}
	
	/**
	 * @param uri
	 * @throws URISyntaxException 
	 */
	public void setURI(String uri) throws URISyntaxException {
		int hash = uri.lastIndexOf('#');
		if (hash != -1) {
			// Fragment is removed from base
			uri = uri.substring(0, hash);
		}
		value = new URI(uri);
	}
	
	/**
	 * @return
	 */
	public URI getURI() {
		return value;
	}
}
