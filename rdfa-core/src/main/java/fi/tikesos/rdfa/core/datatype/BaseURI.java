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

package fi.tikesos.rdfa.core.datatype;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Containter class for storing Base URI
 * 
 * @author ssakorho
 *
 */
public class BaseURI {
	private URI value;
	
	/**
	 * Class constructor
	 * 
	 * @param uri
	 * @throws URISyntaxException 
	 */
	public BaseURI(String uri) throws URISyntaxException {
		value = new URI(uri);
	}
	
	/**
	 * @param uri
	 * @throws URISyntaxException 
	 */
	public void setURI(String uri) throws URISyntaxException {
		int hash = uri.lastIndexOf('#');
		if (hash != -1) {
			// Fragment is removed from base
			uri = uri.substring(0, hash);
		}
		value = new URI(uri);
	}
	
	/**
	 * @return
	 */
	public URI getURI() {
		return value;
	}
}
