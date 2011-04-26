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
 * Language container class.
 * 
 * @author ssakorho
 *
 */
public class Language {
	private String value;
	private Location location;
	
	/**
	 * Constructor
	 * 
	 * @param value
	 * @param location
	 */
	public Language(String value, Location location) {
		this.value = value;
		this.location = location;
	}
	
	/**
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @return
	 */
	public Location getLocation() {
		return location;
	}
}
