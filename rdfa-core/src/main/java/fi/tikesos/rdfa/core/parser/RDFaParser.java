package fi.tikesos.rdfa.core.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import fi.tikesos.rdfa.core.datatype.IncompleteTriple;
import fi.tikesos.rdfa.core.datatype.LString;
import fi.tikesos.rdfa.core.profile.Profile;
import fi.tikesos.rdfa.core.profile.ProfileLoader;
import fi.tikesos.rdfa.core.triple.TripleSink;
import fi.tikesos.rdfa.core.literal.LiteralCollector;
import fi.tikesos.rdfa.core.prefix.PrefixMapping;

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
public class RDFaParser implements ContentHandler {
	private static final String DEFAULT_VOCABULARY = "http://www.w3.org/1999/xhtml/vocab#";
	private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
	private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String RDF_XMLLITERAL = RDF_NS + "XMLLiteral";
	private Locator locator;
	private TripleSink tripleSink;
	private LiteralCollector literalCollector;
	private ProcessingContext context;
	private ProfileLoader profileLoader;
	private long line;
	private long column;

	/**
	 * Class constructor
	 * 
	 * @param base
	 * @param tripleSink
	 * @param profileHandler
	 */
	public RDFaParser(String base, TripleSink tripleSink,
			ProfileLoader profileHandler) {
		// Create default evaluation context
		this.context = new ProcessingContext();
		this.context.setBase(base);
		this.context.setVocabulary(DEFAULT_VOCABULARY);
		this.context.setNewSubject(new LString(base, 0, 0));
		this.profileLoader = profileHandler;
		this.tripleSink = tripleSink;
		this.literalCollector = new LiteralCollector();
		this.line = 0;
		this.column = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		// Start document
		saveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		// End document
		saveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// Register prefix to current context
		context.registerPrefix(prefix, uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// End prefix-mapping
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	/**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param atts
	 * @throws SAXException
	 */
	public void beginRDFaElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		String[] rel = null;
		String[] rev = null;
		String about = null;
		String resource = null;
		String src = null;
		String href = null;
		String datatype = null;
		String[] typeof = null;
		String[] profile = null;
		List<PrefixMapping> prefixList = new ArrayList<PrefixMapping>();
		List<PrefixMapping> xmlnsList = new ArrayList<PrefixMapping>();

		// create new evaluation context
		context = new ProcessingContext(context);

		// update begin tag location
		context.setBeginTagStartLine(line);
		context.setBeginTagStartColumn(column);
		context.setBeginTagEndLine(locator.getLineNumber());
		context.setBeginTagEndColumn(locator.getColumnNumber());

		// Process attributes
		for (int i = 0; i < atts.getLength(); i++) {
			String attributeQName = atts.getQName(i);
			if ("vocab".equals(attributeQName) == true) {
				// 2.
				// @vocab
				if (atts.getValue(i).isEmpty() == true) {
					context.setVocabulary(DEFAULT_VOCABULARY);
				} else {
					context.setVocabulary(atts.getValue(i));
				}
			} else if ("profile".equals(attributeQName) == true) {
				// @profile
				profile = atts.getValue(i).trim().split("\\s");
			} else if ("prefix".equals(attributeQName) == true) {
				// @prefix
				String[] prefix = atts.getValue(i).trim().split("\\s+");
				for (int n = 0; n < prefix.length; n += 2) {
					if (prefix[n].endsWith(":") == true) {
						prefixList.add(new PrefixMapping(attributeQName,
								prefix[n].substring(0, prefix[n].length() - 1),
								prefix[n + 1]));
					} // Else exception?
				}
			} else if (atts.getQName(i).startsWith("xmlns:")) {
				// @xmlns:*
				xmlnsList.add(new PrefixMapping(attributeQName, atts
						.getLocalName(i), atts.getValue(i)));
			} else if ("lang".equals(attributeQName) == true
					|| (XML_NS.equals(atts.getURI(i)) == true && "lang"
							.equals(atts.getLocalName(i)) == true)) {
				// @lang or @xml:lang
				context.setLanguage(atts.getValue(i));
			} else if ("rev".equals(attributeQName) == true) {
				// @rev
				rev = atts.getValue(i).trim().split("\\s+");
			} else if ("rel".equals(attributeQName) == true) {
				// @rel
				rel = atts.getValue(i).trim().split("\\s+");
			} else if ("about".equals(attributeQName) == true) {
				// @about
				about = atts.getValue(i);
			} else if ("src".equals(attributeQName) == true) {
				// @src
				src = atts.getValue(i);
			} else if ("href".equals(attributeQName) == true) {
				// @href
				href = atts.getValue(i);
			} else if ("typeof".equals(attributeQName) == true) {
				// @typeof
				typeof = atts.getValue(i).trim().split("\\s+");
			} else if ("property".equals(attributeQName) == true) {
				// @property
				context.setProperty(atts.getValue(i).trim().split("\\s+"));
			} else if ("resource".equals(attributeQName) == true) {
				// @resource
				resource = atts.getValue(i);
			} else if ("content".equals(attributeQName) == true) {
				// @content
				context.setContent(atts.getValue(i));
			} else if ("datatype".equals(attributeQName) == true) {
				// @datatype
				datatype = atts.getValue(i);
			}
		}

		Set<String> registeredPrefixes = new HashSet<String>();
		// Register namespace mappings defined at @prefix
		for (PrefixMapping pm : prefixList) {
			registeredPrefixes.add(pm.getPrefix());
			context.registerPrefix(pm.getPrefix(), pm.getURI());
		}
		// Register namespace mappings defined at @xmlns:*
		for (PrefixMapping pm : xmlnsList) {
			if (registeredPrefixes.contains(pm.getPrefix()) == false) {
				registeredPrefixes.add(pm.getPrefix());
				context.registerPrefix(pm.getPrefix(), pm.getURI());
			}
		}

		if (profile != null && profileLoader != null) {
			// Load profiles
			for (String profileURI : profile) {
				Profile pro = profileLoader.loadProfile(profileURI);
				// Prefix mappings
				for (Entry<String, String> p : pro.getPrefixMappings()
						.entrySet()) {
					if (registeredPrefixes.contains(p.getKey()) == false) {
						// Profile may not overwrite local prefix mappings
						registeredPrefixes.add(p.getKey());
						context.registerPrefix(p.getKey(), p.getValue());
					}
				}
				// Term mappings
				for (Entry<String, String> p : pro.getTermMappings().entrySet()) {
					// May not overwrite existing term mapping
					if (context.resolveTerm(p.getKey()) == null) {
						context.registerTerm(p.getKey(), p.getValue());
					}
				}
			}
		}

		if (datatype != null) {
			String datatypeURI = context.getQualifiedNameTCU(datatype);
			if (datatypeURI != null) {
				context.setDatatype(datatypeURI);
			}
		}

		if (rev == null && rel == null) {
			// 6.
			// If the current element contains no @rel or @rev attribute, then
			// the next step is to establish a value for new subject. Any of
			// the attributes that can carry a resource can set new subject
			if (about != null) {
				// by using the URI from @about, if present
				String aboutURI = context.getQualifiedNameCU(about);
				if (aboutURI != null) {
					context.setNewSubject(new LString(aboutURI, line, column));
				}
			} else if (src != null) {
				// otherwise, by using the URI from @src, if present
				context.setNewSubject(new LString(src, line, column));
			} else if (resource != null) {
				// otherwise, by using the URI from @resource, if present
				String resourceURI = context.getQualifiedNameCU(resource);
				if (resourceURI != null) {
					context.setNewSubject(new LString(resourceURI, line, column));
				}
			} else if (href != null) {
				// otherwise, by using the URI from @href, if present
				String hrefURI = context.getQualifiedNameU(href);
				if (hrefURI != null) {
					context.setNewSubject(new LString(hrefURI, line, column));
				}
			}
			if (context.getNewSubject() == null) {
				// If no URI is provided by a resource attribute, then the first
				// match from the following rules will apply
				if (typeof != null) {
					// if @typeof is present, then new subject is set to be a
					// newly created bnode
					context.setNewSubject(new LString(context
							.generateBlankNode(), line, column));
				} else {
					// otherwise, if parent object is present, new subject is
					// set to the value of parent object
					if (context.getParentObject() != null) {
						// subjectURI =
						// processingContext.getParentObject().getValue();
						context.setNewSubject(context.getParentObject());
					}
					if (context.getProperty() == null) {
						// Additionally, if @property is not present then
						// the skip element flag is set to 'true';
						context.setSkipElement(true);
					}
				}
			}
		} else {
			// 7.
			// If the current element does contain a @rel or @rev attribute,
			// then the next step is to establish both a value for new subject
			// and a value for current object resource:
			if (about != null) {
				// by using the URI from @about, if present
				String aboutURI = context.getQualifiedNameCU(about);
				if (aboutURI != null) {
					context.setNewSubject(new LString(aboutURI, line, column));
				}
			} else if (src != null) {
				// otherwise, by using the URI from @src, if present
				String srcURI = context.getQualifiedNameU(src);
				if (srcURI != null) {
					context.setNewSubject(new LString(srcURI, line, column));
				}
			}
			if (context.getNewSubject() == null) {
				// If no URI is provided then the first match from the following
				// rules will apply:
				if (typeof != null) {
					// if @typeof is present, then new subject is set to be a
					// newly created bnode;
					context.setNewSubject(new LString(context
							.generateBlankNode(), line, column));
				} else if (context.getParentObject() != null) {
					// otherwise, if parent object is present, new subject is
					// set to that
					context.setNewSubject(context.getParentObject());
				}
			}
			// Then the current object resource is set to the URI obtained from
			// the first match from the following rules
			if (resource != null) {
				// by using the URI from @resource, if present
				String resourceURI = context.getQualifiedNameCU(resource);
				if (resourceURI != null) {
					context.setCurrentObjectResource(new LString(resourceURI,
							line, column));
				}
			} else if (href != null) {
				// otherwise, by using the URI from @href, if present
				String hrefURI = context.getQualifiedNameU(href);
				if (hrefURI != null) {
					context.setCurrentObjectResource(new LString(hrefURI, line,
							column));
				}
			}
		}

		if (context.getNewSubject() != null && typeof != null) {
			// 8.
			// If in any of the previous steps a new subject
			// was set to a non-null value, it is now used to
			// provide a subject for type values
			for (String type : typeof) {
				// One or more 'types' for the new subject can
				// be set by using @typeof. If present, the
				// attribute must contain one or more URIs,
				// obtained according to the section on URI
				// and CURIE Processing, each of which is used
				// to generate a triple
				String typeURI = context.getQualifiedNameCU(type);
				if (typeURI != null) {
					tripleSink
							.generateTriple(
									context.getNewSubject(),
									new LString(
											"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
											line, column), new LString(typeURI,
											line, column));
				}
			}
		}

		if (context.getCurrentObjectResource() != null) {
			// 9.
			// If in any of the previous steps a current object
			// resource was set to a non-null value, it is now
			// used to generate triples
			if (rel != null && rel.length > 0 && rel[0].isEmpty() == false) {
				// if present, @rel may contain one or more
				// URIs, obtained according to the section on
				// CURIE and URI Processing each of which is
				// used to generate a triple
				for (String predicate : rel) {
					String predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						tripleSink.generateTriple(context.getNewSubject(),
								new LString(predicateURI, line, column),
								context.getCurrentObjectResource());
					}
				}
			}
			if (rev != null && rev.length > 0 && rev[0].isEmpty() == false) {
				// if present, @rev may contain one or more
				// URIs, obtained according to the section on
				// CURIE and URI Processing each of which is
				// used to generate a triple
				for (String predicate : rev) {
					String predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						tripleSink.generateTriple(
								context.getCurrentObjectResource(),
								new LString(predicateURI, line, column),
								context.getNewSubject());
					}
				}
			}
		} else if (rel != null || rev != null) {
			// 10.
			// If however current object resource was set to null,
			// but there are predicates present, then they must
			// be stored as incomplete triples, pending the
			// discovery of a subject that can be used as the
			// object. Also, current object resource should be set
			// to a newly created bnode
			List<IncompleteTriple> incompleteTriples = new ArrayList<IncompleteTriple>();
			if (rel != null && rel.length > 0 && rel[0].isEmpty() == false) {
				// If present, @rel must contain one or more URIs,
				// obtained according to the section on CURIE and URI
				// Processing each of which is added to the localContext list
				// of incomplete triples
				for (String predicate : rel) {
					String predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						incompleteTriples.add(new IncompleteTriple(
								predicateURI, line, column, false));
					}
				}
			}
			if (rev != null && rev.length > 0 && rev[0].isEmpty() == false) {
				// If present, @rel must contain one or more URIs,
				// obtained according to the section on CURIE and URI
				// Processing each of which is added to the localContext list
				// of incomplete triples
				for (String predicate : rev) {
					String predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						incompleteTriples.add(new IncompleteTriple(
								predicateURI, line, column, true));
					}
				}
			}
			if (incompleteTriples.isEmpty() == false) {
				context.setLocalIncompleteTriples(incompleteTriples);
			}
			context.setCurrentObjectResource(new LString(context
					.generateBlankNode(), line, column));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void endRDFaElement(String uri, String localName, String qName)
			throws SAXException {
		// 11.
		if (context.getProperty() != null) {
			LString currentObjectLiteral = null;
			LString datatype = null;
			LString language = context.getLanguage() != null ? new LString(
					context.getLanguage(), context.getBeginTagStartLine(),
					context.getBeginTagStartColumn()) : null;

			if (context.getDatatype() != null
					&& context.getDatatype().isEmpty() == false
					&& context.getDatatype().equals(RDF_XMLLITERAL) == false) {
				// as a typed literal if @datatype is present, does not
				// have an empty value according to the section on
				// CURIE and URI Processing, and is not set to XMLLiteral
				// in the vocabulary
				// http://www.w3.org/1999/02/22-rdf-syntax-ns#
				if (context.getContent() != null) {
					// The actual literal is either the value of
					// @content (if present)
					currentObjectLiteral = new LString(context.getContent(),
							line, column);
				} else {
					// or a string created by concatenating the
					// value of all descendant text nodes
					currentObjectLiteral = new LString(
							literalCollector.stopCollecting(),
							context.getBeginTagEndLine(),
							context.getBeginTagEndColumn());
				}
				datatype = new LString(context.getDatatype(),
						context.getBeginTagStartLine(),
						context.getBeginTagStartColumn());
			} else if (context.getDatatype() != null
					&& context.getDatatype().equals(RDF_XMLLITERAL) == true) {
				// as an XML literal if @datatype is present and is set to
				// XMLLiteral in the vocabulary
				// http://www.w3.org/1999/02/22-rdf-syntax-ns#
				currentObjectLiteral = new LString(
						literalCollector.stopCollecting(),
						context.getBeginTagEndLine(),
						context.getBeginTagEndColumn());
				datatype = new LString(context.getDatatype(),
						context.getBeginTagStartLine(),
						context.getBeginTagStartColumn());
			} else {
				// otherwise as a plain literal
				if (context.getContent() != null) {
					// The actual literal is either the value of
					// @content (if present)
					currentObjectLiteral = new LString(context.getContent(),
							context.getBeginTagStartLine(),
							context.getBeginTagStartColumn());
				} else {
					// or a string created by concatenating the
					// value of all descendant text nodes
					currentObjectLiteral = new LString(
							literalCollector.stopCollecting(),
							context.getBeginTagEndLine(),
							context.getBeginTagEndColumn());
				}
			}

			if (currentObjectLiteral != null) {
				// The current object literal is then used with
				// each predicate to generate a triple
				for (String predicate : context.getProperty()) {
					String predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						tripleSink.generateTripleLiteral(
								context.getNewSubject(),
								new LString(predicateURI, context
										.getBeginTagStartLine(), context
										.getBeginTagStartColumn()),
								currentObjectLiteral, language, datatype);
					}
				}
			}
		}

		// 12.
		if (context.isSkipElement() == false && context.getNewSubject() != null) {
			// If the skip element flag is 'false', and new subject
			// was set to a non-null value, then any incomplete
			// triples within the current context should be completed
			if (context.getEvaluationIncompleteTriples() != null) {
				for (IncompleteTriple incompleteTriple : context
						.getEvaluationIncompleteTriples()) {
					if (incompleteTriple.isReverse() == false) {
						// Forward
						tripleSink.generateTriple(context.getParentSubject(),
								incompleteTriple, context.getNewSubject());
					} else {
						// Backward
						tripleSink.generateTriple(context.getNewSubject(),
								incompleteTriple, context.getParentSubject());
					}
				}
			}
		}

		// Change context
		context = context.getParentContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if (literalCollector.collectStartElement(uri, localName, qName, atts) == false) {
			beginRDFaElement(uri, localName, qName, atts);
		}
		saveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// XML Literal handling
		if (literalCollector.collectCloseElement(uri, localName, qName) == false) {
			endRDFaElement(uri, localName, qName);
		}
		saveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// Capture text content for plain literal
		literalCollector.collect(ch, start, length, true);
		saveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// Whitespace
		characters(ch, start, length);
		saveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// Processing instruction
		saveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	@Override
	public void skippedEntity(String name) throws SAXException {
		// Skipped entity
		saveLocation();
	}

	/**
	 * Save current location
	 */
	private void saveLocation() {
		// Save current location
		line = locator.getLineNumber();
		column = locator.getColumnNumber();
	}
}
