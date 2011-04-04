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

package fi.tikesos.rdfa.core.jena;

import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.datatype.Language;
import fi.tikesos.rdfa.core.datatype.Lexical;
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
	
	/* (non-Javadoc)
	 * @see fi.tikesos.rdfa.core.triple.TripleSink#startRelativeTripleCaching()
	 */
	public void startRelativeTripleCaching() {
	}

	/* (non-Javadoc)
	 * @see fi.tikesos.rdfa.core.triple.TripleSink#stopRelativeTripleCaching()
	 */
	public void stopRelativeTripleCaching() {
	}
	
	/* (non-Javadoc)
	 * @see fi.tikesos.rdfa.TripleSink#generateTriple(fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component)
	 */
	@Override
	public void generateTriple(Component subject, Component predicate,
			Component object) {
		Resource s = createResource(subject.getValue());
		Property p = model.createProperty(predicate.getValue());
		Resource o = createResource(object.getValue());
		model.add(s, p, o);
//		System.out.println("<" + s + "> <" + p + "> <" + o + ">");
	}

	/* (non-Javadoc)
	 * @see fi.tikesos.rdfa.TripleSink#generateTripleLiteral(fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component, fi.tikesos.rdfa.Component)
	 */
	@Override
	public void generateTripleLiteral(Component subject, Component predicate,
			Lexical lexical, Language language, Component datatype) {
		Resource s = createResource(subject.getValue());
		Property p = model.createProperty(predicate.getValue());
		Literal o;
		if (datatype != null) {
			o = model.createTypedLiteral(lexical.getValue(), datatype.getValue());
		} else if (language != null) {
			o = model.createLiteral(lexical.getValue(), language.getValue());
		} else {
			o = model.createLiteral(lexical.getValue());
		}
		model.add(s, p, o);
//		System.out.println("<" + s + "> <" + p + "> <" + o + ">");
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
