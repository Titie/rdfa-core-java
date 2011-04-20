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

package fi.tikesos.rdfa.core.parser.dom;

import org.w3c.dom.Node;

import fi.tikesos.rdfa.core.datatype.Location;

/**
 * SAXLocation location
 * 
 * @author ssakorho
 *
 */
public class DOMLocation implements Location {
	private Node node;
	
	/**
	 * Constructor
	 * 
	 * @param node
	 */
	public DOMLocation(Node node) {
		this.node = node;
	}
	
	/**
	 * @return
	 */
	public Node getNode() {
		return node;
	}
}
