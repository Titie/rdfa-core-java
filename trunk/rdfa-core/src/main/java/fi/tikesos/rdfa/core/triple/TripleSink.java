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
package fi.tikesos.rdfa.core.triple;

import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.datatype.Language;
import fi.tikesos.rdfa.core.datatype.Lexical;

/**
 * TripleSink interface
 * 
 * @author ssakorho
 *
 */
public interface TripleSink {
	public void startRelativeTripleCaching();
	public void stopRelativeTripleCaching();
	public void generateTriple(Component subject, Component predicate, Component object);
	public void generateTriple(String subject, String predicate, String object);
	public void generateTripleLiteral(Component subject, Component predicate, Lexical lexical, Language language, Component datatype);
	public void generateTripleLiteral(String subject, String predicate, String lexical, String language, String datatype);
}