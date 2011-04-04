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

/**
 * @author ssakorho
 *
 */
public class PrefixMapping {
	private String qName;
	private String prefix;
	private String uri;
	
	/**
	 * @param qName
	 * @param prefix
	 * @param uri
	 */
	public PrefixMapping(String qName, String prefix, String uri) {
		this.qName = qName;
		this.prefix = prefix;
		this.uri = uri;
	}
	
	/**
	 * @return the qName
	 */
	public String getqName() {
		return qName;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the uri
	 */
	public String getURI() {
		return uri;
	}
}
