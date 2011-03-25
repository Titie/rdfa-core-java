package fi.tikesos.rdfa.core.parser;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import fi.tikesos.rdfa.core.datatype.IncompleteTriple;
import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.datatype.Language;
import fi.tikesos.rdfa.core.datatype.Lexical;
import fi.tikesos.rdfa.core.datatype.PrefixMapping;
import fi.tikesos.rdfa.core.profile.Profile;
import fi.tikesos.rdfa.core.profile.ProfileLoader;
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
public class RDFaParser implements ContentHandler {
	public static final int DYNAMIC = 0;
	public static final int XHTML_RDFA = 1;
	public static final int XML_RDFA = 2;
	private static final String XHTML_PROFILE = "http://www.w3.org/1999/xhtml/vocab";
	private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
	private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
	private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String RDF_XMLLITERAL = RDF_NS + "XMLLiteral";
	private int format;
	private int depth;
	private boolean lookForBase;
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
	 * @param profileLoader
	 * @throws URISyntaxException
	 */
	public RDFaParser(String base, TripleSink tripleSink,
			ProfileLoader profileLoader, int format) throws URISyntaxException {
		// Create default evaluation context
		this.context = new ProcessingContext(base);
		this.context.setNewSubject(new Component(base));
		this.profileLoader = profileLoader;
		this.tripleSink = tripleSink;
		this.literalCollector = new LiteralCollector();
		this.line = 0;
		this.column = 0;
		this.format = format;
		this.depth = 0;
		this.lookForBase = (format == XHTML_RDFA ? true : false);
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
		String vocab = null;
		String datatype = null;
		String[] typeof = null;
		String[] profile = null;
		List<PrefixMapping> prefixList = new ArrayList<PrefixMapping>();
		List<PrefixMapping> xmlnsList = new ArrayList<PrefixMapping>();

		// create new evaluation context
		depth++;
		context = new ProcessingContext(context, line, column,
				locator.getLineNumber(), locator.getColumnNumber());

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
				vocab = atts.getValue(i);
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
			} else if (depth == 1 && "xmlns".equals(attributeQName) == true
					&& format == DYNAMIC) {
				if (XHTML_NS.equals(atts.getValue(i)) == true) {
					// XHTML+RDFa extension
					format = XHTML_RDFA;
					lookForBase = true;
				} else {
					// XML-RDFa core
					format = XML_RDFA;
					lookForBase = false;
				}
			}
		}

		if (format == XHTML_RDFA) {
			// From XHTML
			switch (depth) {
			case 1:
				// Default vocabulary for XHTML
				if (profileLoader != null) {
					Profile pro = profileLoader.loadProfile(XHTML_PROFILE);
					if (pro != null) {
						processProfile(pro);
					} else {
						// Failed profile causes all subsequent elements
						// to be ignored!
					}
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
					context.setBase(href);
				}
				break;
			}
		}

		if (profileLoader != null && profile != null) {
			// Load profiles
			for (String profileURI : profile) {
				Profile pro = profileLoader.loadProfile(profileURI);
				if (pro != null) {
					processProfile(pro);
				} else {
					// Failed profile causes all subsequent elements
					// to be ignored!
				}
			}
		}

		// Set vocabulary
		if (vocab != null) {
			context.setVocabulary(vocab.isEmpty() == true ? null : vocab);
		}

		// Register namespace mappings defined at @xmlns:*
		for (PrefixMapping pm : xmlnsList) {
			context.registerPrefix(pm.getPrefix(), pm.getURI());
		}

		// Register namespace mappings defined at @prefix
		for (PrefixMapping pm : prefixList) {
			context.registerPrefix(pm.getPrefix(), pm.getURI());
		}

		if (datatype != null) {
			Component datatypeURI = context.getQualifiedNameTCU(datatype);
			if (datatypeURI != null) {
				datatypeURI.setLocation(line, column);
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
				Component aboutURI = context.getQualifiedNameCU(about);
				if (aboutURI != null) {
					aboutURI.setLocation(line, column);
					context.setNewSubject(aboutURI);
				}
			} else if (src != null) {
				// otherwise, by using the URI from @src, if present
				Component srcURI = context.getQualifiedNameU(src);
				if (srcURI != null) {
					srcURI.setLocation(line, column);
					context.setNewSubject(srcURI);
				}
			} else if (resource != null) {
				// otherwise, by using the URI from @resource, if present
				Component resourceURI = context.getQualifiedNameCU(resource);
				if (resourceURI != null) {
					resourceURI.setLocation(line, column);
					context.setNewSubject(resourceURI);
				}
			} else if (href != null) {
				// otherwise, by using the URI from @href, if present
				Component hrefURI = context.getQualifiedNameU(href);
				if (hrefURI != null) {
					hrefURI.setLocation(line, column);
					context.setNewSubject(hrefURI);
				}
			} else if (depth == 2 && format == XHTML_RDFA) {
				// if no URI is provided by a resource attribute, then
				// first check to see if the element is the head or body
				// element. If it is, then act as if there is an empty
				// @about present, and process it according to the rule
				// for @about.
				if (("head".equals(localName) == true || "body"
						.equals(localName)) && XHTML_NS.equals(uri) == true) {
					Component aboutURI = context.getQualifiedNameCU("");
					if (aboutURI != null) {
						aboutURI.setLocation(line, column);
						context.setNewSubject(aboutURI);
					}
				}
			}

			if (context.getNewSubject() == null) {
				// If no URI is provided by a resource attribute, then the first
				// match from the following rules will apply
				if (typeof != null) {
					// if @typeof is present, then new subject is set to be a
					// newly created bnode
					context.setNewSubject(new Component(context
							.generateBlankNode(), line, column));
				} else {
					// otherwise, if parent object is present, new subject is
					// set to the value of parent object
					if (context.getParentObject() != null) {
						context.setNewSubject(context.getParentObject());

						if (context.getProperty() == null) {
							// Additionally, if @property is not present then
							// the skip element flag is set to 'true';
							context.setSkipElement(true);
						}
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
				Component aboutURI = context.getQualifiedNameCU(about);
				if (aboutURI != null) {
					aboutURI.setLocation(line, column);
					context.setNewSubject(aboutURI);
				}
			} else if (src != null) {
				// otherwise, by using the URI from @src, if present
				Component srcURI = context.getQualifiedNameU(src);
				if (srcURI != null) {
					srcURI.setLocation(line, column);
					context.setNewSubject(srcURI);
				}
			} else if (depth == 2 && format == XHTML_RDFA) {
				// if no URI is provided by a resource attribute, then
				// first check to see if the element is the head or body
				// element. If it is, then act as if there is an empty
				// @about present, and process it according to the rule
				// for @about.
				if (("head".equals(localName) == true || "body"
						.equals(localName)) && XHTML_NS.equals(uri) == true) {
					Component aboutURI = context.getQualifiedNameCU("");
					if (aboutURI != null) {
						aboutURI.setLocation(line, column);
						context.setNewSubject(aboutURI);
					}
				}
			}

			if (context.getNewSubject() == null) {
				// If no URI is provided then the first match from the following
				// rules will apply:
				if (typeof != null) {
					// if @typeof is present, then new subject is set to be a
					// newly created bnode;
					context.setNewSubject(new Component(context
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
				Component resourceURI = context.getQualifiedNameCU(resource);
				if (resourceURI != null) {
					resourceURI.setLocation(line, column);
					context.setCurrentObjectResource(resourceURI);
				}
			} else if (href != null) {
				// otherwise, by using the URI from @href, if present
				Component hrefURI = context.getQualifiedNameU(href);
				if (hrefURI != null) {
					hrefURI.setLocation(line, column);
					context.setCurrentObjectResource(hrefURI);
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
				Component typeURI = context.getQualifiedNameTCU(type);
				if (typeURI != null) {
					typeURI.setLocation(line, column);
					tripleSink.generateTriple(context.getNewSubject(),
							new Component(RDF_NS + "type", line, column),
							typeURI);
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
					Component predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						predicateURI.setLocation(line, column);
						tripleSink.generateTriple(context.getNewSubject(),
								predicateURI,
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
					Component predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						predicateURI.setLocation(line, column);
						tripleSink.generateTriple(
								context.getCurrentObjectResource(),
								predicateURI, context.getNewSubject());
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
					Component predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						predicateURI.setLocation(line, column);
						incompleteTriples.add(new IncompleteTriple(
								predicateURI, false));
					}
				}
			}
			if (rev != null && rev.length > 0 && rev[0].isEmpty() == false) {
				// If present, @rel must contain one or more URIs,
				// obtained according to the section on CURIE and URI
				// Processing each of which is added to the localContext list
				// of incomplete triples
				for (String predicate : rev) {
					Component predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						predicateURI.setLocation(line, column);
						incompleteTriples.add(new IncompleteTriple(
								predicateURI, true));
					}
				}
			}
			if (incompleteTriples.isEmpty() == false) {
				context.setLocalIncompleteTriples(incompleteTriples);
			}
			context.setCurrentObjectResource(new Component(context
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
			Component datatype = null;
			Lexical lexical = null;
			Language language = context.getLanguage() != null ? new Language(
					context.getLanguage(), context.getBeginTagStartLine(),
					context.getBeginTagStartColumn()) : null;

			if (context.getDatatype() != null
					&& context.getDatatype().getValue().isEmpty() == false
					&& context.getDatatype().getValue().equals(RDF_XMLLITERAL) == false) {
				// as a typed literal if @datatype is present, does not
				// have an empty value according to the section on
				// CURIE and URI Processing, and is not set to XMLLiteral
				// in the vocabulary
				// http://www.w3.org/1999/02/22-rdf-syntax-ns#
				if (context.getContent() != null) {
					// The actual literal is either the value of
					// @content (if present)
					lexical = new Lexical(context.getContent(), line, column);
				} else {
					// or a string created by concatenating the
					// value of all descendant text nodes
					lexical = new Lexical(literalCollector.stopCollecting(),
							context.getBeginTagEndLine(),
							context.getBeginTagEndColumn());
				}
				datatype = context.getDatatype();
			} else if (context.getDatatype() != null
					&& context.getDatatype().getValue().equals(RDF_XMLLITERAL) == true) {
				// as an XML literal if @datatype is present and is set to
				// XMLLiteral in the vocabulary
				// http://www.w3.org/1999/02/22-rdf-syntax-ns#
				lexical = new Lexical(literalCollector.stopCollecting(),
						context.getBeginTagEndLine(),
						context.getBeginTagEndColumn());
				datatype = context.getDatatype();
			} else {
				// otherwise as a plain literal
				if (context.getContent() != null) {
					// The actual literal is either the value of
					// @content (if present)
					lexical = new Lexical(context.getContent(),
							context.getBeginTagStartLine(),
							context.getBeginTagStartColumn());
				} else {
					// or a string created by concatenating the
					// value of all descendant text nodes
					lexical = new Lexical(literalCollector.stopCollecting(),
							context.getBeginTagEndLine(),
							context.getBeginTagEndColumn());
				}
			}

			if (lexical != null) {
				// The current object literal is then used with
				// each predicate to generate a triple
				for (String predicate : context.getProperty()) {
					Component predicateURI = context
							.getQualifiedNameTCU(predicate);
					if (predicateURI != null) {
						predicateURI.setLocation(
								context.getBeginTagStartLine(),
								context.getBeginTagStartColumn());
						tripleSink.generateTripleLiteral(
								context.getNewSubject(), predicateURI, lexical,
								language, datatype);
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

		// Change context
		context = context.getParentContext();
		depth--;
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

	/*
	 * Save current location
	 */
	private void saveLocation() {
		// Save current location
		line = locator.getLineNumber();
		column = locator.getColumnNumber();
	}
}
