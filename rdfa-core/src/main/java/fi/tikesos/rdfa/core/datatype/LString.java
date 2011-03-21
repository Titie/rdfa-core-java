package fi.tikesos.rdfa.core.datatype;

/**
 * LString is a String container class with location information.
 * 
 * @author ssakorho
 *
 */
public class LString {
	private long line;
	private long column;
	private String value;
	
	/**
	 * Class constructor.
	 * 
	 * @param value the value
	 * @param line the line value was located on
	 * @param column the column value was located on
	 */
	public LString(String value, long line, long column) {
		this.value = value;
		this.line = line;
		this.column = column;
	}
	/**
	 * @return the line value was located on
	 */
	public long getLine() {
		return line;
	}
	/**
	 * @param line the line value was located on
	 */
	public void setLine(long line) {
		this.line = line;
	}
	/**
	 * @return the column value was located on
	 */
	public long getColumn() {
		return column;
	}
	/**
	 * @param column the column value was located on
	 */
	public void setColumn(long column) {
		this.column = column;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return string representation of LString
	 */
	public String toString() {
		return value + " [" + line + ":" + column + "]";
	}
}
