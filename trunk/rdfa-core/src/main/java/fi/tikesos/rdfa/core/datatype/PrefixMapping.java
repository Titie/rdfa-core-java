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
 * @author ssakorho
 * 
 */
public class PrefixMapping {
	private String prefix;
	private String reference;

	/**
	 * @param prefix
	 *            The prefix.
	 * @param reference
	 *            The reference.
	 */
	public PrefixMapping(String prefix, String reference) {
		this.prefix = prefix;
		this.reference = reference;
	}

	/**
	 * @return The prefix.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return The reference.
	 */
	public String getReference() {
		return reference;
	}
}
