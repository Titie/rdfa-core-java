package fi.tikesos.rdfa.core.datatype;

import org.xml.sax.Attributes;

/**
 * @author ssakorho
 * 
 */
public class SAXAttributes implements fi.tikesos.rdfa.core.datatype.Attributes {
	private Attributes attributes;
	private SAXLocation location;

	/**
	 * @param attributes
	 */
	public SAXAttributes(Attributes attributes, SAXLocation location) {
		this.attributes = attributes;
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getCount()
	 */
	public int getCount() {
		return attributes.getLength();
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getQName(long)
	 */
	public String getQName(int index) {
		return attributes.getQName(index);
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getLocalName(int)
	 */
	public String getLocalName(int index) {
		return attributes.getLocalName(index);
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getURI(int)
	 */
	public String getURI(int index) {
		return attributes.getURI(index);
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getValue(int)
	 */
	public String getValue(int index) {
		return attributes.getValue(index);
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getLocation(int)
	 */
	public Location getLocation(int index) {
		return location;
	}
}
