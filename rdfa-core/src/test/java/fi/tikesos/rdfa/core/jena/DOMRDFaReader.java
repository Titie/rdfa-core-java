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

import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;
import com.hp.hpl.jena.rdf.model.RDFReader;

import fi.tikesos.rdfa.core.exception.ErrorHandler;
import fi.tikesos.rdfa.core.exception.TripleErrorHandler;
import fi.tikesos.rdfa.core.parser.DOMRDFaParser;
import fi.tikesos.rdfa.core.parser.RDFaParser;
import fi.tikesos.rdfa.core.profile.ProfileHandler;
import fi.tikesos.rdfa.core.profile.SimpleProfileHandler;
import fi.tikesos.rdfa.core.triple.TripleSink;
import fi.tikesos.rdfa.core.util.NullEntityResolver;

/**
 * @author ssakorho
 * 
 */
public class DOMRDFaReader implements RDFReader {
	private static ProfileHandler profileHandler = null;
	private RDFErrorHandler errorHandler = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.hpl.jena.rdf.model.RDFReader#read(com.hp.hpl.jena.rdf.model.Model,
	 *      java.io.InputStream, java.lang.String)
	 */
	@Override
	public void read(Model model, InputStream r, String base) {
		try {
			// Create DOM builder
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setValidating(false);
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory.setIgnoringElementContentWhitespace(false);
			// Create DOM document
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			documentBuilder.setEntityResolver(new NullEntityResolver());
			Document document = documentBuilder.parse(r);
			
			// Create profile handler
			if (profileHandler == null) {
				profileHandler = new SimpleProfileHandler();
			}
			
			// Parse document
			TripleSink sink = new JenaTripleSink(model);
			ErrorHandler errorHandler = new TripleErrorHandler(sink);
			DOMRDFaParser.parse(document, base, sink,
					profileHandler, errorHandler, RDFaParser.UNKNOWN_XML);
		} catch (Exception exception) {
			errorHandler.fatalError(exception);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.hpl.jena.rdf.model.RDFReader#read(com.hp.hpl.jena.rdf.model.Model,
	 *      java.io.Reader, java.lang.String)
	 */
	public void read(Model model, Reader r, String base) {
		// Not implemented
		if (errorHandler != null) {
			errorHandler.fatalError(new NoSuchMethodException(
					"Method has not been implemented"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.hpl.jena.rdf.model.RDFReader#read(com.hp.hpl.jena.rdf.model.Model,
	 * java.lang.String)
	 */
	public void read(Model model, String url) {
		// Not implemented
		if (errorHandler != null) {
			errorHandler.fatalError(new NoSuchMethodException(
					"Method has not been implemented"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.hpl.jena.rdf.model.RDFReader#setProperty(java.lang.String,
	 * java.lang.Object)
	 */
	public Object setProperty(String propName, Object propValue) {
		// Set reader property
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.hpl.jena.rdf.model.RDFReader#setErrorHandler(com.hp.hpl.jena.rdf
	 * .model.RDFErrorHandler)
	 */
	public RDFErrorHandler setErrorHandler(RDFErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		RDFErrorHandler oldErrorHandler = errorHandler;
		this.errorHandler = errorHandler;
		return oldErrorHandler;
	}
}
