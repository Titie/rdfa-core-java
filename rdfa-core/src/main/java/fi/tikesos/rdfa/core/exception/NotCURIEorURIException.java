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
 * NotCURIEorURIException is thrown when CURIEorURI
 * fails to be resolved
 * 
 * @author ssakorho
 *
 */
public class NotCURIEorURIException extends RDFaException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param elementName
	 * @param location
	 * @param cause
	 */
	public NotCURIEorURIException(String elementName, Location location,
			Throwable cause) {
		super(elementName, location, cause);
	}
}
