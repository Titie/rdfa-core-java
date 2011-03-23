package fi.tikesos.rdfa.core.datatype;

import java.net.URI;

/**
 * Component is a component container class with location information.
 * 
 * @author ssakorho
 * 
 */
public class Component implements Location {
	private long line;
	private long column;
	private String absoluteURI;
	private URI currentBaseURI;
	private URI uri;
	private BaseURI baseURI;

	/**
	 * Class constructor.
	 * 
	 * @param absoulteURI
	 *            the absolute uri
	 * @param line
	 *            the line value was located on
	 * @param column
	 *            the column value was located on
	 */
	public Component(String absoluteURI, long line, long column) {
		this.absoluteURI = absoluteURI;
		this.line = line;
		this.column = column;
	}

	/**
	 * Class constructor.
	 * 
	 * @param absoulteURI
	 *            the absolute uri
	 */
	public Component(String absoluteURI) {
		this.absoluteURI = absoluteURI;
	}

	/**
	 * Class constructor.
	 * 
	 * @param uri
	 *            the uri
	 */
	public Component(URI uri) {
		this.absoluteURI = uri.toString();
	}

	/**
	 * Class constructor.
	 * 
	 * @param baseURI
	 *            the base uri
	 */
	public Component(BaseURI baseURI) {
		this.baseURI = baseURI;
	}

	/**
	 * Class constructor.
	 * 
	 * @param baseURI
	 *            the base uri
	 * @param uri
	 *            the uri
	 */
	public Component(BaseURI baseURI, URI uri) {
		this.baseURI = baseURI;
		this.uri = uri;
	}

	/**
	 * @return the line value was located on
	 */
	public long getLine() {
		return line;
	}

	/**
	 * @return the column value was located on
	 */
	public long getColumn() {
		return column;
	}

	/**
	 * @param line
	 *            the line value was located on
	 * @param column
	 *            the column value was located on
	 */
	public void setLocation(long line, long column) {
		this.line = line;
		this.column = column;
	}

	/**
	 * @return the uri
	 */
	public String getValue() {
		if (baseURI != null && baseURI.getURI() != currentBaseURI) {
			currentBaseURI = baseURI.getURI();
			absoluteURI = (uri != null ? currentBaseURI.resolve(uri).toString()
					: currentBaseURI.toString());
		}
		return absoluteURI;
	}

	/**
	 * @return string representation of Component
	 */
	public String toString() {
		return getValue() + " [" + line + ":" + column + "]";
	}
}
