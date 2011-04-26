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
package fi.tikesos.rdfa.core.parser.dom;

import org.w3c.dom.NamedNodeMap;

import fi.tikesos.rdfa.core.datatype.Attributes;
import fi.tikesos.rdfa.core.datatype.Location;

/**
 * @author ssakorho
 * 
 */
public class DOMAttributes implements Attributes {
	private NamedNodeMap attributes;

	/**
	 * Class constructor
	 * 
	 * @param attributes
	 */
	public DOMAttributes(NamedNodeMap attributes) {
		this.attributes = attributes;
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getCount()
	 */
	public int getCount() {
		return attributes.getLength();
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getQName(long)
	 */
	public String getQName(int index) {
		return attributes.item(index).getNodeName();
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getLocalName(int)
	 */
	public String getLocalName(int index) {
		return attributes.item(index).getLocalName();
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getURI(int)
	 */
	public String getURI(int index) {
		return attributes.item(index).getNamespaceURI();
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getValue(int)
	 */
	public String getValue(int index) {
		return attributes.item(index).getNodeValue();
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getLocation(int)
	 */
	public Location getLocation(int index) {
		return new DOMLocation(attributes.item(index));
	}
}
