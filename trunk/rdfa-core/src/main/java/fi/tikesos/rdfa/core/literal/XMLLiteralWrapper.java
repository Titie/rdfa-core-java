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
package fi.tikesos.rdfa.core.literal;

import fi.tikesos.rdfa.core.datatype.Literal;

/**
 * XMLLiteral wrapper class.
 * 
 * @author ssakorho
 *
 */
public class XMLLiteralWrapper {
	private Literal literal;
	private int depth;
	
	/**
	 * Constructor
	 */
	public XMLLiteralWrapper(Literal literal, int depth) {
		this.literal = literal;
		this.depth = depth;
	}
	
	/**
	 * @return
	 */
	public Literal getXMLLiteral() {
		return literal;
	}
	
	/**
	 * @return
	 */
	public int getDepth() {
		return depth;
	}
}
