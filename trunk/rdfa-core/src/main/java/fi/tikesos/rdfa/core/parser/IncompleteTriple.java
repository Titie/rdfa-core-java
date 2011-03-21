package fi.tikesos.rdfa.core.parser;

import fi.tikesos.rdfa.core.datatype.LString;

/**
 * @author ssakorho
 *
 */
public class IncompleteTriple extends LString {
	private boolean reverse;
	
	public IncompleteTriple(String value, long row, long column, boolean reverse) {
		super(value, row, column);
		this.reverse = reverse;
	}
	
	/**
	 * @return the reverse
	 */
	public boolean isReverse() {
		return reverse;
	}
	
	/**
	 * @param reverse the reverse to set
	 */
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}