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
package fi.tikesos.rdfa.core.jena;

import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.datatype.Language;
import fi.tikesos.rdfa.core.datatype.Literal;
import fi.tikesos.rdfa.core.triple.TripleSink;

/**
 * @author ssakorho
 * 
 */
public class JenaTripleSink implements TripleSink {
	private Model model;
	private Map<String, Resource> nodeMap;

	/**
	 * @param model
	 */
	public JenaTripleSink(Model model) {
		this.model = model;
		this.nodeMap = new HashMap<String, Resource>();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.core.triple.TripleSink#startRelativeTripleCaching()
	 */
	public void startRelativeTripleCaching() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.core.triple.TripleSink#stopRelativeTripleCaching()
	 */
	public void stopRelativeTripleCaching() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.core.triple.TripleSink#generateTriple(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void generateTriple(String subject, String predicate, String object) {
		Resource s = createResource(subject);
		Property p = model.createProperty(predicate);
		Resource o = createResource(object);
		model.add(s, p, o);
//		System.out.println("+ <" + s + "> <" + p + "> <" + o + ">");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.core.triple.TripleSink#generateTripleLiteral(java.lang
	 *      .String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public void generateTripleLiteral(String subject, String predicate,
			String lexical, String language, String datatype) {
		Resource s = createResource(subject);
		Property p = model.createProperty(predicate);
		com.hp.hpl.jena.rdf.model.Literal o;
		if (datatype != null) {
			o = model.createTypedLiteral(lexical, datatype);
		} else if (language != null) {
			o = model.createLiteral(lexical, language);
		} else {
			o = model.createLiteral(lexical);
		}
		model.add(s, p, o);
//		System.out.println("+ <" + s + "> <" + p + "> <" + o + ">");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.TripleSink#generateTriple(fi.tikesos.rdfa.Component,
	 *      fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component)
	 */
	@Override
	public void generateTriple(Component subject, Component predicate,
			Component object) {
		generateTriple(subject.getValue(), predicate.getValue(),
				object.getValue());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.TripleSink#generateTripleLiteral(fi.tikesos.rdfa.Component
	 *      , fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component,
	 *      fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component)
	 */
	@Override
	public void generateTripleLiteral(Component subject, Component predicate,
			Literal literal, Language language, Component datatype) {
		generateTripleLiteral(subject.getValue(), predicate.getValue(),
				literal.getValue(), language != null ? language.getValue()
						: null, datatype != null ? datatype.getValue() : null);
	}

	/**
	 * @param URI
	 * @return
	 */
	private Resource createResource(String URI) {
		Resource resource = null;
		if (URI.startsWith("_:") == true) {
			// Anonymous
			resource = nodeMap.get(URI);
			if (resource == null) {
				resource = model.createResource();
				nodeMap.put(URI, resource);
			}
		} else {
			// Named
			resource = model.createResource(URI);
		}
		return resource;
	}
}
