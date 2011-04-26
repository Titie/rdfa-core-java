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