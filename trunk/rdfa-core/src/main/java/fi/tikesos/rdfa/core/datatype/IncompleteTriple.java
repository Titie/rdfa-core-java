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
 * Wrapper class for incomplete triples.
 * 
 * @author ssakorho
 * 
 */
public class IncompleteTriple {
	private Component component;
	private boolean reverse;

	/**
	 * Class constructor.
	 * 
	 * @param component
	 *            The component.
	 * @param reverse
	 *            The reverse flag.
	 */
	public IncompleteTriple(Component component, boolean reverse) {
		this.component = component;
		this.reverse = reverse;
	}

	/**
	 * @return The component.
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @return The reverse flag.
	 */
	public boolean isReverse() {
		return reverse;
	}
}