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
 * Common interface for attributes.
 * 
 * @author ssakorho
 * 
 */
public interface Attributes {
	/**
	 * @return The number of attributes in the list.
	 */
	public int getCount();

	/**
	 * @param index
	 *            The attribute index (zero-based).
	 * @return The XML qualified name, or the empty string if none is available,
	 *         or null if the index is out of range.
	 */
	public String getQName(int index);

	/**
	 * @param index
	 *            The attribute index (zero-based).
	 * @return The local name, or the empty string if Namespace processing is
	 *         not being performed, or null if the index is out of range.
	 */
	public String getLocalName(int index);

	/**
	 * @param index
	 *            The attribute index (zero-based).
	 * @return The Namespace URI, or the empty string if none is available, or
	 *         null if the index is out of range.
	 */
	public String getURI(int index);

	/**
	 * @param index
	 *            The attribute index (zero-based).
	 * @return The attribute's value as a string, or null if the index is out of
	 *         range.
	 */
	public String getValue(int index);

	/**
	 * @param index
	 *            The attribute index (zero-based).
	 * @return The attribute's location in the document.
	 */
	public Location getLocation(int index);
}
