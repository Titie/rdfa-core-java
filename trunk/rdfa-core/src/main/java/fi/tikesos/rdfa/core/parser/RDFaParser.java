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

package fi.tikesos.rdfa.core.parser;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import fi.tikesos.rdfa.core.datatype.Attributes;
import fi.tikesos.rdfa.core.datatype.IncompleteTriple;
import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.datatype.Language;
import fi.tikesos.rdfa.core.datatype.Lexical;
import fi.tikesos.rdfa.core.datatype.Location;
import fi.tikesos.rdfa.core.datatype.PrefixMapping;
import fi.tikesos.rdfa.core.datatype.RDFaAttributes;
import fi.tikesos.rdfa.core.exception.ErrorHandler;
import fi.tikesos.rdfa.core.exception.NotCURIEorURIException;
import fi.tikesos.rdfa.core.exception.NotTERMorCURIEorAbsURIException;
import fi.tikesos.rdfa.core.exception.NotURIException;
import fi.tikesos.rdfa.core.exception.ProfileHandlerNotDefinedException;
import fi.tikesos.rdfa.core.exception.ProfileLoadException;
import fi.tikesos.rdfa.core.profile.Profile;
import fi.tikesos.rdfa.core.profile.ProfileHandler;
import fi.tikesos.rdfa.core.triple.TripleSink;
import fi.tikesos.rdfa.core.literal.LiteralCollector;

/**
 * RDFaParserImpl
 * 
 * W3C RDFa 1.1 parser implementation for SAX.
 * 
 * Attributes from evaluationContext are not passed to literalCollector!
 * 
 * @author Sami Korhonen, University Of Eastern Finland
 * @email sami.s.korhonen@uef.fi
 * @version 0.1
 */
public class RDFaParser {
	public static final int UNKNOWN_XML = 0;
	public static final int XHTML_RDFA = 1;
	public static final int XML_RDFA = 2;
	public static final String XHTML_PROFILE = "http://www.w3.org/1999/xhtml/vocab";
	public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
	public static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
	public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDF_XMLLITERAL = RDF_NS + "XMLLiteral";
	private int format;
	private int depth;
	private boolean lookForBase;
	private TripleSink tripleSink;
	private LiteralCollector literalCollector;
	private ErrorHandler errorHandler;
	private ProcessingContext context;
	private ProfileHandler profileHandler;

	/**
	 * Class constructor
	 * 
	 * @param base
	 * @param tripleSink
	 * @param profileHandler
	 * @param errorHandler
	 * @param format
	 * @throws URISyntaxException
	 */
	public RDFaParser(String base, TripleSink tripleSink,
			ProfileHandler profileHandler, ErrorHandler errorHandler, int format)
			throws URISyntaxException {
		// Create default evaluation context
		this.context = new ProcessingContext(base);
		this.context.setNewSubject(new Component(base));
		this.tripleSink = tripleSink;
		this.profileHandler = profileHandler;
		this.errorHandler = errorHandler;
		this.literalCollector = new LiteralCollector();
		this.format = format;
		this.depth = 0;
		this.lookForBase = (format == XHTML_RDFA ? true : false);
	}

	public void processProfile(Profile profile) {
		// Prefix mappings
		for (Entry<String, String> p : profile.getPrefixMappings().entrySet()) {
			context.registerPrefix(p.getKey(), p.getValue());
		}
		// Term mappings
		for (Entry<String, String> p : profile.getTermMappings().entrySet()) {
			// May not overwrite existing term mapping
			if (context.resolveTerm(p.getKey()) == null) {
				context.registerTerm(p.getKey(), p.getValue());
			}
		}
		// Default vocabulary
		if (profile.getDefaultVocabulary() != null) {
			context.setVocabulary(profile.getDefaultVocabulary());
		}
	}

	/**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * @param location
	 * @throws SAXException
	 */
	public void beginRDFaElement(String uri, String localName, String qName,
			Attributes attributes, Location location) {
		if (literalCollector.collectStartElement(uri, localName, qName,
				attributes, location) == true) {
			return;
		}

		// Create new evaluation context
		depth++;
		context = new ProcessingContext(context);

		// Process attributes
		RDFaAttributes rdfaAttributes = new RDFaAttributes(attributes);
		if (depth == 1 && format == UNKNOWN_XML) {
			if (XHTML_NS.equals(rdfaAttributes.getDefaultXmlns()) == true) {
				// XHTML+RDFa extension
				format = XHTML_RDFA;
				lookForBase = true;
			} else {
				// XML-RDFa core
				format = XML_RDFA;
				lookForBase = false;
			}
		}
		if (rdfaAttributes.getLang() != null) {
			// Set language
			context.setLanguage(new Language(rdfaAttributes.getLang(), rdfaAttributes.getLangLocation()));
		}
		if (rdfaAttributes.getProperty() != null) {
			// Set property
			context.setProperty(rdfaAttributes.getProperty());
			context.setPropertyLocation(rdfaAttributes.getPropertyLocation());
		}
		if (rdfaAttributes.getContent() != null) {
			// Set content
			context.setContent(new Lexical(rdfaAttributes.getContent(), rdfaAttributes.getContentLocation()));
		}

		if (format == XHTML_RDFA) {
			// From XHTML
			switch (depth) {
			case 1:
				// Default vocabulary for XHTML
				if (profileHandler != null) {
					try {
						processProfile(profileHandler
								.loadProfile(XHTML_PROFILE));
					} catch (Exception exception) {
						// Failed profile causes all subsequent elements
						// to be ignored!
						errorHandler.fatalError(new ProfileLoadException(
								XHTML_PROFILE, "profile", location, exception));
						context.setProfileFailed(true);
					}
				} else {
					errorHandler
							.fatalError(new ProfileHandlerNotDefinedException(
									XHTML_PROFILE, "profile", location));
					context.setProfileFailed(true);
				}
				break;
			case 2:
				if ("head".equals(localName) == true
						&& XHTML_NS.equals(uri) == true) {
					// Start looking for base
					lookForBase = true;
					tripleSink.startRelativeTripleCaching();
				}
				break;
			case 3:
				if (lookForBase == true && "base".equals(localName) == true
						&& XHTML_NS.equals(uri) == true) {
					// Set base to @href
					try {
						context.setBase(rdfaAttributes.getHref());
					} catch (Exception exception) {
						errorHandler.warning(new NotURIException("href",
								rdfaAttributes.getHrefLocation(), exception));
					}
				}
				break;
			}
		}

		if (rdfaAttributes.getProfile() != null) {
			// Load profiles
			if (profileHandler != null) {
				for (String profileURI : rdfaAttributes.getProfile()) {
					try {
						processProfile(profileHandler.loadProfile(profileURI));
					} catch (Exception exception) {
						// Failed profile causes all subsequent elements
						// to be ignored!
						errorHandler.fatalError(new ProfileLoadException(
								profileURI, "profile", rdfaAttributes.getProfileLocation(), exception));
						context.setProfileFailed(true);
						break;
					}
				}
			} else {
				// Can not continue processing
				errorHandler.fatalError(new ProfileHandlerNotDefinedException(
						rdfaAttributes.getProfile()[0], "profile", rdfaAttributes.getProfileLocation()));
				context.setProfileFailed(true);
			}
		}

		// Failed profile causes all subsequent elements
		// to be ignored!
		if (context.isProfileFailed() == false) {
			// Set vocabulary
			if (rdfaAttributes.getVocab() != null) {
				context.setVocabulary(rdfaAttributes.getVocab().isEmpty() == true ? null
						: rdfaAttributes.getVocab());
			}

			// Register namespace mappings defined at @xmlns:*
			for (PrefixMapping pm : rdfaAttributes.getXmlns()) {
				context.registerPrefix(pm.getPrefix(), pm.getURI());
			}

			// Register namespace mappings defined at @prefix
			for (PrefixMapping pm : rdfaAttributes.getPrefix()) {
				context.registerPrefix(pm.getPrefix(), pm.getURI());
			}

			if (rdfaAttributes.getDatatype() != null) {
				try {
					Component datatypeURI = context
							.expandTERMorCURIEorAbsURI(rdfaAttributes
									.getDatatype());
					datatypeURI.setLocation(rdfaAttributes.getDatatypeLocation());
					context.setDatatype(datatypeURI);
				} catch (Exception exception) {
					errorHandler.warning(new NotTERMorCURIEorAbsURIException(
							"datatype", rdfaAttributes.getDatatypeLocation(), exception));
				}
			}

			if (rdfaAttributes.getRev() == null
					&& rdfaAttributes.getRel() == null) {
				// 6.
				// If the current element contains no @rel or @rev attribute,
				// then
				// the next step is to establish a value for new subject. Any of
				// the attributes that can carry a resource can set new subject
				if (rdfaAttributes.getAbout() != null) {
					// by using the URI from @about, if present
					try {
						Component aboutURI = context
								.expandCURIEorURI(rdfaAttributes.getAbout());
						aboutURI.setLocation(rdfaAttributes.getAboutLocation());
						context.setNewSubject(aboutURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotCURIEorURIException(
								"about", rdfaAttributes.getAboutLocation(), exception));
					}
				} else if (rdfaAttributes.getSrc() != null) {
					// otherwise, by using the URI from @src, if present
					try {
						Component srcURI = context.expandURI(rdfaAttributes
								.getSrc());
						srcURI.setLocation(rdfaAttributes.getSrcLocation());
						context.setNewSubject(srcURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotURIException("src",
								rdfaAttributes.getSrcLocation(), exception));
					}
				} else if (rdfaAttributes.getResource() != null) {
					// otherwise, by using the URI from @resource, if present
					try {
						Component resourceURI = context
								.expandCURIEorURI(rdfaAttributes.getResource());
						resourceURI.setLocation(rdfaAttributes.getResourceLocation());
						context.setNewSubject(resourceURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotCURIEorURIException(
								"resource", rdfaAttributes.getResourceLocation(), exception));
					}
				} else if (rdfaAttributes.getHref() != null) {
					// otherwise, by using the URI from @href, if present
					try {
						Component hrefURI = context.expandURI(rdfaAttributes
								.getHref());
						hrefURI.setLocation(rdfaAttributes.getHrefLocation());
						context.setNewSubject(hrefURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotURIException("href",
								rdfaAttributes.getHrefLocation(), exception));
					}
				} else if (depth == 2) {
					// if no URI is provided by a resource attribute, then
					// first check to see if the element is a root
					// element. If it is, then act as if there is an empty
					// @about present, and process it according to the rule
					// for @about.
					try {
						Component aboutURI = context.expandCURIEorURI("");
						aboutURI.setLocation(location);
						context.setNewSubject(aboutURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotCURIEorURIException(
								"about", location, exception));
					}
				}

				if (context.getNewSubject() == null) {
					// If no URI is provided by a resource attribute, then the
					// first
					// match from the following rules will apply
					if (rdfaAttributes.getTypeof() != null) {
						// if @typeof is present, then new subject is set to be
						// a newly created bnode
						context.setNewSubject(new Component(context
								.generateBlankNode(), location));
					} else {
						// otherwise, if parent object is present, new subject
						// is set to the value of parent object
						if (context.getParentObject() != null) {
							context.setNewSubject(context.getParentObject());
							if (context.getProperty() == null) {
								// Additionally, if @property is not present
								// then the skip element flag is set to 'true';
								context.setSkipElement(true);
							}
						}
					}
				}
			} else {
				// 7.
				// If the current element does contain a @rel or @rev attribute,
				// then the next step is to establish both a value for new
				// subject
				// and a value for current object resource:
				if (rdfaAttributes.getAbout() != null) {
					// by using the URI from @about, if present
					try {
						Component aboutURI = context
								.expandCURIEorURI(rdfaAttributes.getAbout());
						aboutURI.setLocation(rdfaAttributes.getAboutLocation());
						context.setNewSubject(aboutURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotCURIEorURIException(
								"about", rdfaAttributes.getAboutLocation(), exception));
					}
				} else if (rdfaAttributes.getSrc() != null) {
					// otherwise, by using the URI from @src, if present
					try {
						Component srcURI = context.expandURI(rdfaAttributes
								.getSrc());
						srcURI.setLocation(rdfaAttributes.getSrcLocation());
						context.setNewSubject(srcURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotURIException("src",
								rdfaAttributes.getSrcLocation(), exception));
					}
				} else if (depth == 2) {
					// if no URI is provided by a resource attribute, then
					// first check to see if the element is a root
					// element. If it is, then act as if there is an empty
					// @about present, and process it according to the rule
					// for @about.
					try {
						Component aboutURI = context.expandCURIEorURI("");
						aboutURI.setLocation(location);
						context.setNewSubject(aboutURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotCURIEorURIException(
								"about", location, exception));
					}
				}

				if (context.getNewSubject() == null) {
					// If no URI is provided then the first match from the
					// following
					// rules will apply:
					if (rdfaAttributes.getTypeof() != null) {
						// if @typeof is present, then new subject is set to be
						// a
						// newly created bnode;
						context.setNewSubject(new Component(context
								.generateBlankNode(), location));
					} else if (context.getParentObject() != null) {
						// otherwise, if parent object is present, new subject
						// is
						// set to that
						context.setNewSubject(context.getParentObject());
					}
				}
				// Then the current object resource is set to the URI obtained
				// from
				// the first match from the following rules
				if (rdfaAttributes.getResource() != null) {
					// by using the URI from @resource, if present
					try {
						Component resourceURI = context
								.expandCURIEorURI(rdfaAttributes.getResource());
						resourceURI.setLocation(rdfaAttributes.getResourceLocation());
						context.setCurrentObjectResource(resourceURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotCURIEorURIException(
								"resource", rdfaAttributes.getResourceLocation(), exception));
					}
				} else if (rdfaAttributes.getHref() != null) {
					// otherwise, by using the URI from @href, if present
					try {
						Component hrefURI = context.expandURI(rdfaAttributes
								.getHref());
						hrefURI.setLocation(rdfaAttributes.getHrefLocation());
						context.setCurrentObjectResource(hrefURI);
					} catch (Exception exception) {
						errorHandler.warning(new NotURIException("href",
								rdfaAttributes.getHrefLocation(), exception));
					}
				}
			}

			if (context.getNewSubject() != null
					&& rdfaAttributes.getTypeof() != null) {
				// 8.
				// If in any of the previous steps a new subject
				// was set to a non-null value, it is now used to
				// provide a subject for type values
				for (String type : rdfaAttributes.getTypeof()) {
					// One or more 'types' for the new subject can
					// be set by using @typeof. If present, the
					// attribute must contain one or more URIs,
					// obtained according to the section on URI
					// and CURIE Processing, each of which is used
					// to generate a triple
					try {
						Component typeURI = context
								.expandTERMorCURIEorAbsURI(type);
						typeURI.setLocation(rdfaAttributes.getTypeofLocation());
						tripleSink.generateTriple(context.getNewSubject(),
								new Component(RDF_NS + "type", location), typeURI);
					} catch (URISyntaxException exception) {
						errorHandler
								.warning(new NotTERMorCURIEorAbsURIException(
										"typeof", rdfaAttributes.getTypeofLocation(),
										exception));
					}
				}
			}

			if (context.getCurrentObjectResource() != null) {
				// 9.
				// If in any of the previous steps a current object
				// resource was set to a non-null value, it is now
				// used to generate triples
				String[] rel = rdfaAttributes.getRel();
				if (rel != null && rel.length > 0 && rel[0].isEmpty() == false) {
					// if present, @rel may contain one or more
					// URIs, obtained according to the section on
					// CURIE and URI Processing each of which is
					// used to generate a triple
					for (String predicate : rel) {
						try {
							Component predicateURI = context
									.expandTERMorCURIEorAbsURI(predicate);
							predicateURI.setLocation(rdfaAttributes.getRelLocation());
							tripleSink.generateTriple(context.getNewSubject(),
									predicateURI,
									context.getCurrentObjectResource());
						} catch (URISyntaxException exception) {
							errorHandler
									.warning(new NotTERMorCURIEorAbsURIException(
											"rel", rdfaAttributes.getRelLocation(),
											exception));
						}
					}
				}
				String[] rev = rdfaAttributes.getRev();
				if (rev != null && rev.length > 0 && rev[0].isEmpty() == false) {
					// if present, @rev may contain one or more
					// URIs, obtained according to the section on
					// CURIE and URI Processing each of which is
					// used to generate a triple
					for (String predicate : rev) {
						try {
							Component predicateURI = context
									.expandTERMorCURIEorAbsURI(predicate);
							predicateURI.setLocation(rdfaAttributes.getRevLocation());
							tripleSink.generateTriple(
									context.getCurrentObjectResource(),
									predicateURI, context.getNewSubject());
						} catch (URISyntaxException exception) {
							errorHandler
									.warning(new NotTERMorCURIEorAbsURIException(
											"rev", rdfaAttributes.getRevLocation(),
											exception));
						}
					}
				}
			} else if (rdfaAttributes.getRel() != null
					|| rdfaAttributes.getRev() != null) {
				// 10.
				// If however current object resource was set to null,
				// but there are predicates present, then they must
				// be stored as incomplete triples, pending the
				// discovery of a subject that can be used as the
				// object. Also, current object resource should be set
				// to a newly created bnode
				String[] rel = rdfaAttributes.getRel();
				List<IncompleteTriple> incompleteTriples = new ArrayList<IncompleteTriple>();
				if (rel != null && rel.length > 0 && rel[0].isEmpty() == false) {
					// If present, @rel must contain one or more URIs,
					// obtained according to the section on CURIE and URI
					// Processing each of which is added to the localContext
					// list
					// of incomplete triples
					for (String predicate : rel) {
						try {
							Component predicateURI = context
									.expandTERMorCURIEorAbsURI(predicate);
							predicateURI.setLocation(rdfaAttributes.getRelLocation());
							incompleteTriples.add(new IncompleteTriple(
									predicateURI, false));
						} catch (URISyntaxException exception) {
							errorHandler
									.warning(new NotTERMorCURIEorAbsURIException(
											"rel", rdfaAttributes.getRelLocation(),
											exception));
						}
					}
				}
				String[] rev = rdfaAttributes.getRev();
				if (rev != null && rev.length > 0 && rev[0].isEmpty() == false) {
					// If present, @rel must contain one or more URIs,
					// obtained according to the section on CURIE and URI
					// Processing each of which is added to the localContext
					// list
					// of incomplete triples
					for (String predicate : rev) {
						try {
							Component predicateURI = context
									.expandTERMorCURIEorAbsURI(predicate);
							predicateURI.setLocation(rdfaAttributes.getRevLocation());
							incompleteTriples.add(new IncompleteTriple(
									predicateURI, true));
						} catch (URISyntaxException exception) {
							errorHandler
									.warning(new NotTERMorCURIEorAbsURIException(
											"rev", rdfaAttributes.getRevLocation(),
											exception));
						}
					}
				}
				if (incompleteTriples.isEmpty() == false) {
					context.setLocalIncompleteTriples(incompleteTriples);
				}
				context.setCurrentObjectResource(new Component(context
						.generateBlankNode(), location));
			}

			if (context.getProperty() != null) {
				// determinate literal collecting mode
				if (context.getContent() == null) {
					// @content is not present
					if (context.getDatatype() != null
							&& context.getDatatype().equals(RDF_XMLLITERAL)) {
						literalCollector.startCollectingXML();
					} else {
						literalCollector.startCollecting();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void endRDFaElement(String uri, String localName, String qName) {
		// XML Literal handling
		if (literalCollector.collectCloseElement(uri, localName, qName) == true) {
			return;
		}

		// 11.
		if (context.isProfileFailed() == false) {
			if (context.getProperty() != null) {
				Component datatype = null;
				Lexical lexical = null;
				Language language = context.getLanguage();

				if (context.getDatatype() != null
						&& context.getDatatype().getValue().isEmpty() == false
						&& context.getDatatype().getValue()
								.equals(RDF_XMLLITERAL) == false) {
					// as a typed literal if @datatype is present, does not
					// have an empty value according to the section on
					// CURIE and URI Processing, and is not set to XMLLiteral
					// in the vocabulary
					// http://www.w3.org/1999/02/22-rdf-syntax-ns#
					if (context.getContent() != null) {
						// The actual literal is either the value of
						// @content (if present)
						lexical = context.getContent();
					} else {
						// or a string created by concatenating the
						// value of all descendant text nodes
						lexical = literalCollector.stopCollecting();
					}
					datatype = context.getDatatype();
				} else if (context.getDatatype() != null
						&& context.getDatatype().getValue()
								.equals(RDF_XMLLITERAL) == true) {
					// as an XML literal if @datatype is present and is set to
					// XMLLiteral in the vocabulary
					// http://www.w3.org/1999/02/22-rdf-syntax-ns#
					lexical = literalCollector.stopCollecting();
					datatype = context.getDatatype();
				} else {
					// otherwise as a plain literal
					if (context.getContent() != null) {
						// The actual literal is either the value of
						// @content (if present)
						lexical = context.getContent();
					} else {
						// or a string created by concatenating the
						// value of all descendant text nodes
						lexical = literalCollector.stopCollecting();
					}
				}

				if (lexical != null) {
					// The current object literal is then used with
					// each predicate to generate a triple
					for (String predicate : context.getProperty()) {
						try {
							Component predicateURI = context
									.expandTERMorCURIEorAbsURI(predicate);
							predicateURI.setLocation(context.getPropertyLocation());
							tripleSink.generateTripleLiteral(
									context.getNewSubject(), predicateURI,
									lexical, language, datatype);
						} catch (URISyntaxException exception) {
							errorHandler
									.warning(new NotTERMorCURIEorAbsURIException(
											"property", context.getPropertyLocation(),
											exception));
						}
					}
				}
			}

			// 12.
			if (context.isSkipElement() == false
					&& context.getNewSubject() != null) {
				// If the skip element flag is 'false', and new subject
				// was set to a non-null value, then any incomplete
				// triples within the current context should be completed
				if (context.getEvaluationIncompleteTriples() != null) {
					for (IncompleteTriple incompleteTriple : context
							.getEvaluationIncompleteTriples()) {
						if (incompleteTriple.isReverse() == false) {
							// Forward
							tripleSink.generateTriple(
									context.getParentSubject(),
									incompleteTriple.getURI(),
									context.getNewSubject());
						} else {
							// Backward
							tripleSink.generateTriple(context.getNewSubject(),
									incompleteTriple.getURI(),
									context.getParentSubject());
						}
					}
				}
			}

			if (format == XHTML_RDFA) {
				// From XHTML
				if (lookForBase == true && depth == 2
						&& "head".equals(localName) == true
						&& XHTML_NS.equals(uri) == true) {
					// Start looking for base
					lookForBase = false;
					tripleSink.stopRelativeTripleCaching();
				}
			}
		}

		// Change context
		context = context.getParentContext();
		depth--;
	}

	/**
	 * @param ch
	 * @param start
	 * @param length
	 * @param location
	 */
	public void writeLiteral(char[] ch, int start, int length,
			Location location) {
		literalCollector.collect(ch, start, length, true, location);
	}

	/**
	 * @param str
	 * @param location
	 */
	public void writeLiteral(String str, Location location) {
		literalCollector.collect(str, true, location);
	}
}
