/**
 * Copyright (C) 2011 ssakorho <sami.s.korhonen@uef.fi>
 *
 * Licensed under the GNU Lesser General Public Licence, Version 3
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *         http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.tikesos.rdfa.core.datatype;

/**
 * Literal container class.
 * 
 * @author ssakorho
 * 
 */
public class Literal {
	private StringBuffer buffer;
	private String value;
	private Location location;

	/**
	 * Class constructor.
	 */
	public Literal() {
		this.buffer = new StringBuffer();
		this.location = null;
		this.value = null;
	}

	/**
	 * Class constructor.
	 * 
	 * @param value
	 *            The string value of the literal.
	 * @param location
	 *            The literal's location in the document.
	 */
	public Literal(String value, Location location) {
		this.buffer = null;
		this.location = location;
		this.value = value;
	}

	/**
	 * @return The string value of the literal.
	 */
	public String getValue() {
		return value != null ? value : buffer.toString();
	}

	/**
	 * @return The literal's location in the document.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            The literal's location in the document.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return
	 */
	public StringBuffer getBuffer() {
		return buffer;
	}

	/**
	 * @param buffer
	 */
	public void append(Literal literal) {
		buffer.append(literal.getBuffer());
	}

	/**
	 * @param str
	 */
	public void append(String str) {
		buffer.append(str);
	}

	/**
	 * @param ch
	 * @param length
	 * @param offset
	 */
	public void append(char[] ch, int start, int length) {
		buffer.append(ch, start, length);
	}

	/**
	 * @return
	 */
	public int length() {
		return buffer.length();
	}

	/**
	 * @param newLength
	 */
	public void setLength(int newLength) {
		buffer.setLength(newLength);
	}
}
