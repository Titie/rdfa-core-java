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
package fi.tikesos.rdfa.core.exception;

import fi.tikesos.rdfa.core.datatype.Location;

/**
 * ProfileHandlerNotDefinedException is used to indicate
 * that document requests RDFa profile to be loaded, but
 * ProfileHandler is not defined 
 * 
 * @author ssakorho
 *
 */
public class ProfileHandlerNotDefinedException extends RDFaException {
	private static final long serialVersionUID = 1L;
	private String profileURI;

	/**
	 * Constructor
	 * 
	 * @param profileURI
	 * @param line
	 * @param column
	 */
	public ProfileHandlerNotDefinedException(String profileURI, String elementName, Location location) {
		super(elementName, location);
		this.profileURI = profileURI;
	}
	
	/**
	 * @return
	 */
	public String getProfileURI() {
		return profileURI;
	}
}
