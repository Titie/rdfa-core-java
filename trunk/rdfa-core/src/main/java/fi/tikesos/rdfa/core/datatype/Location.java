package fi.tikesos.rdfa.core.datatype;

/**
 * Location information interface.
 * 
 * @author ssakorho
 *
 */
public interface Location {
	public long getLine();
	public long getColumn();
	public void setLocation(long line, long column);
}
