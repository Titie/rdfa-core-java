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
package fi.tikesos.rdfa.core.registry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ssakorho
 *
 */
public class Registry {
	Map<String, String> mappings;

	public Registry() {
		this.mappings = new HashMap<String, String>();
	}
	
	public Registry(Registry toCopy) {
		this.mappings = new HashMap<String, String>(toCopy.getMappings());
	}
	
	public String set(String Key, String Value) {
		return mappings.put(Key, Value);
	}
	
	public String get(String Key) {
		return mappings.get(Key);
	}
	
	public Map<String, String> getMappings() {
		return mappings;
	}
}
