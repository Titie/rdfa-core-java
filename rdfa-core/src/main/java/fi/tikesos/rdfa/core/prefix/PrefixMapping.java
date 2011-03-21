package fi.tikesos.rdfa.core.prefix;

/**
 * @author ssakorho
 *
 */
public class PrefixMapping {
	private String qName;
	private String prefix;
	private String uri;
	
	/**
	 * @param qName
	 * @param prefix
	 * @param uri
	 */
	public PrefixMapping(String qName, String prefix, String uri) {
		this.qName = qName;
		this.prefix = prefix;
		this.uri = uri;
	}
	
	/**
	 * @return the qName
	 */
	public String getqName() {
		return qName;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the uri
	 */
	public String getURI() {
		return uri;
	}
}
