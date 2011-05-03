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
import fi.tikesos.rdfa.core.datatype.Literal;

/**
 * TripleSink interface
 * 
 * @author ssakorho
 * 
 */
public interface TripleSink {

	/**
	 * Signals sink to start caching triples with relative IRI references
	 */
	public void startRelativeTripleCaching();

	/**
	 * Signals sink to flush the triple cache and to stop caching triples with
	 * relative IRI references
	 */
	public void stopRelativeTripleCaching();

	/**
	 * Generate a triple
	 * 
	 * @param subject
	 *            Subject of the triple defined as IRI or blank node
	 * @param predicate
	 *            Predicate of the triple defined as IRI
	 * @param object
	 *            Object of the triple defined as IRI or blank node
	 */
	public void generateTriple(Component subject, Component predicate,
			Component object);

	/**
	 * Generate a triple
	 * 
	 * @param subject
	 *            Subject of the triple defined as IRI or blank node
	 * @param predicate
	 *            Predicate of the triple defined as IRI
	 * @param object
	 *            Object of the triple defined IRI or blank node
	 */
	public void generateTriple(String subject, String predicate, String object);

	/**
	 * Generate a triple
	 * 
	 * @param subject
	 *            Subject of the triple. IRI or blank node
	 * @param predicate
	 *            Predicate of the triple. IRI
	 * @param literal
	 *            Literal value of the triple
	 * @param language
	 *            Language of the literal or null
	 * @param datatype
	 *            Datatype of the literal or null
	 */
	public void generateTripleLiteral(Component subject, Component predicate,
			Literal literal, Language language, Component datatype);

	/**
	 * Generate a triple
	 * 
	 * @param subject
	 *            Subject of the triple
	 * @param predicate
	 *            Predicate of the triple
	 * @param literal
	 *            Literal value of the triple
	 * @param language
	 *            Language of the literal or null
	 * @param datatype
	 *            Datatype of the literal or null
	 */
	public void generateTripleLiteral(String subject, String predicate,
			String literal, String language, String datatype);
}
