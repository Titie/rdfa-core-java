package fi.tikesos.rdfa.core.datatype;

/**
 * Common interface for attributes.
 * 
 * @author ssakorho
 *
 */
public interface Attributes {
	public int getCount();
	public String getQName(int index);
	public String getLocalName(int index);
	public String getURI(int index);
	public String getValue(int index);
	public Location getLocation(int index);
}
