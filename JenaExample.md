# Example #

The most recent version of Jena integration example is provided as a unit test.

## JenaTripleSink ##
```
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
```

## SAXRDFaReader ##
```
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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;
import com.hp.hpl.jena.rdf.model.RDFReader;

import fi.tikesos.rdfa.core.exception.ErrorHandler;
import fi.tikesos.rdfa.core.exception.TripleErrorHandler;
import fi.tikesos.rdfa.core.parser.RDFaParser;
import fi.tikesos.rdfa.core.parser.sax.SAXRDFaParser;
import fi.tikesos.rdfa.core.profile.ProfileHandler;
import fi.tikesos.rdfa.core.profile.SimpleProfileHandler;
import fi.tikesos.rdfa.core.triple.TripleSink;
import fi.tikesos.rdfa.core.util.NullEntityResolver;

/**
 * @author ssakorho
 * 
 */
public class SAXRDFaReader implements RDFReader {
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
		// Default method
		XMLReader reader;
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setFeature("http://xml.org/sax/features/validation",
					Boolean.FALSE);
			reader.setFeature("http://xml.org/sax/features/namespace-prefixes",
					Boolean.TRUE);
			reader.setEntityResolver(new NullEntityResolver());

			if (profileHandler == null) {
				profileHandler = new SimpleProfileHandler();
			}

			try {
				TripleSink sink = new JenaTripleSink(model);
				ErrorHandler errorHandler = new TripleErrorHandler(sink);
				ContentHandler parser = new SAXRDFaParser(base, sink,
						profileHandler, errorHandler, RDFaParser.UNKNOWN_XML);
				reader.setContentHandler(parser);
				reader.parse(new InputSource(r));
			} catch (IOException e) {
				errorHandler.fatalError(e);
			} catch (URISyntaxException e) {
				errorHandler.fatalError(e);
			}
		} catch (SAXException e) {
			errorHandler.fatalError(e);
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.hpl.jena.rdf.model.RDFReader#read(com.hp.hpl.jena.rdf.model.Model,
	 *      java.lang.String)
	 */
	public void read(Model model, String url) {
		// Not implemented
		if (errorHandler != null) {
			errorHandler.fatalError(new NoSuchMethodException(
					"Method has not been implemented"));
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.hpl.jena.rdf.model.RDFReader#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public Object setProperty(String propName, Object propValue) {
		// Set reader property
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.hp.hpl.jena.rdf.model.RDFReader#setErrorHandler(com.hp.hpl.jena.rdf
	 *      .model.RDFErrorHandler)
	 */
	public RDFErrorHandler setErrorHandler(RDFErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		RDFErrorHandler oldErrorHandler = errorHandler;
		this.errorHandler = errorHandler;
		return oldErrorHandler;
	}
}
```

## Test ##
```
// Register reader
RDFReaderFImpl.setBaseReaderClassName("RDFA-CORE-JAVA-SAX",
	"fi.tikesos.rdfa.core.jena.SAXRDFaReader");
// Create empty model
Model testModel = ModelFactory.createDefaultModel();
// Read
rdfReader.read(testModel, inputStream, baseURI);
```