package fi.tikesos.rdfa.core.datatype;

/**
 * Lexical container class.
 * 
 * @author ssakorho
 *
 */
public class Lexical implements Location {
	private String value;
	private long line;
	private long column;
	
	public Lexical(String value, long line, long column) {
		this.value = value;
		this.line = line;
		this.column = column;
	}
	public String getValue() {
		return value;
	}
	public long getLine() {
		return line;
	}
	public long getColumn() {
		return column;
	}
	public void setLocation(long line, long column) {
		this.line = line;
		this.column = column;
	}	
}
