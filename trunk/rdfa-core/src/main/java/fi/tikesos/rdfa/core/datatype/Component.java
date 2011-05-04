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

import java.net.URI;

/**
 * Component is a component container class with location information.
 * 
 * @author ssakorho
 * 
 */
public class Component {
	private Location location;
	private String absoluteURI;
	private URI currentBaseURI;
	private URI uri;
	private BaseURI baseURI;

	/**
	 * Class constructor.
	 * 
	 * @param absoulteURI
	 *            The absolute uri.
	 * @param location
	 *            The location value was located on.
	 */
	public Component(String absoluteURI, Location location) {
		this.absoluteURI = absoluteURI;
		this.location = location;
	}

	/**
	 * Class constructor.
	 * 
	 * @param absoulteURI
	 *            The absolute uri.
	 */
	public Component(String absoluteURI) {
		this.absoluteURI = absoluteURI;
	}

	/**
	 * Class constructor.
	 * 
	 * @param baseURI
	 *            The base uri.
	 */
	public Component(BaseURI baseURI) {
		this.baseURI = baseURI;
	}

	/**
	 * Class constructor.
	 * 
	 * @param baseURI
	 *            The base uri.
	 * @param uri
	 *            The uri.
	 */
	public Component(BaseURI baseURI, URI uri) {
		if (uri.isAbsolute() == true) {
			this.absoluteURI = uri.toString();
		} else {
			this.baseURI = baseURI;
			this.uri = uri;
		}
	}

	/**
	 * @return The component's location in the document.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            The component's location in the document.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return The URI value.
	 */
	public String getValue() {
		if (baseURI != null && baseURI.getURI() != currentBaseURI) {
			currentBaseURI = baseURI.getURI();
			absoluteURI = (uri != null ? currentBaseURI.resolve(uri).toString()
					: currentBaseURI.toString());
		}
		return absoluteURI;
	}
	
	/**
	 * @return True if uri is absolute, otherwise false.
	 */
	public boolean isAbsolute() {
		return baseURI != null;
	}

	/**
	 * @return The string representation of Component.
	 */
	public String toString() {
		return getValue() + (location != null ? " [" + location.toString() + "]" : "");
	}
}
