/**
 * Copyright (C) 2011 ssakorho <sami.s.korhonen@uef.fi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fi.tikesos.rdfa.core.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ssakorho
 *
 */
public class BlankNodeHandler {
	private long blankNodeId = 0;
	private Map<String, String> blankNodeMappings = new HashMap<String, String>();
	
	/**
	 * @return
	 */
	public String generateBlankNode() {
		blankNodeId++;
		return "_:BNsk" + blankNodeId;
	}

	/**
	 * @param nodeName
	 * @return
	 */
	public String mapBlankNode(String nodeName) {
		String id = blankNodeMappings.get(nodeName);
		if (id == null) {
			id = generateBlankNode();
			blankNodeMappings.put(nodeName, id);
		}
		return id;
	}
}
