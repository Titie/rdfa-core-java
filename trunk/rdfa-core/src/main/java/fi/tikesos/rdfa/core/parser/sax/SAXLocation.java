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

package fi.tikesos.rdfa.core.parser.sax;

import fi.tikesos.rdfa.core.datatype.Location;

/**
 * SAXLocation location
 * 
 * @author ssakorho
 *
 */
public class SAXLocation implements Location {
	private long line;
	private long column;
	
	/**
	 * Constructor
	 * 
	 * @param line
	 * @param column
	 */
	public SAXLocation(long line, long column) {
		this.line = line;
		this.column = column;
	}
	
	/**
	 * @return
	 */
	public long getLine() {
		return line;
	}
	
	/**
	 * @return
	 */
	public long getColumn() {
		return column;
	}
}
