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
package fi.tikesos.rdfa.core.parser.sax;

import org.xml.sax.Attributes;

import fi.tikesos.rdfa.core.datatype.Location;

/**
 * @author ssakorho
 * 
 */
public class SAXAttributes implements fi.tikesos.rdfa.core.datatype.Attributes {
	private Attributes attributes;
	private SAXLocation location;

	/**
	 * @param attributes
	 */
	public SAXAttributes(Attributes attributes, SAXLocation location) {
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
		return attributes.getQName(index);
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getLocalName(int)
	 */
	public String getLocalName(int index) {
		return attributes.getLocalName(index);
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getURI(int)
	 */
	public String getURI(int index) {
		return attributes.getURI(index);
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getValue(int)
	 */
	public String getValue(int index) {
		return attributes.getValue(index);
	}

	/**
	 * @see fi.tikesos.rdfa.core.datatype.Attributes#getLocation(int)
	 */
	public Location getLocation(int index) {
		return location;
	}
}
