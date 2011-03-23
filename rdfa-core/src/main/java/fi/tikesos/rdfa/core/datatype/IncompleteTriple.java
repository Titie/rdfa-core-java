package fi.tikesos.rdfa.core.datatype;

/**
 * @author ssakorho
 *
 */
public class IncompleteTriple {
	private Component uri;
	private boolean reverse;
	
	public IncompleteTriple(Component uri, boolean reverse) {
		this.uri = uri;
		this.reverse = reverse;
	}
	/**
	 * @return
	 */
	public Component getURI() {
		return uri;
	}
	/**
	 * @return the reverse
	 */
	public boolean isReverse() {
		return reverse;
	}
}