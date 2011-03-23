package fi.tikesos.rdfa.core.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.xerces.util.XMLChar;

import fi.tikesos.rdfa.core.datatype.BaseURI;
import fi.tikesos.rdfa.core.datatype.IncompleteTriple;
import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.registry.Registry;

/**
 * ProcessingContext contains both Local Context and Evaluation Context defined
 * in the RDFa 1.1 specification.
 * 
 * @author ssakorho
 * 
 */
public class ProcessingContext {
	private ProcessingContext parentContext = null;
	// Blank node handler
	private BlankNodeHandler blankNodeHandler = null;
	// Local Context specific
	private boolean skipElement = false;
	private Component newSubject = null;
	private Component currentObjectResource = null;
	private String content = null;
	private String[] property = null;
	private Component datatype = null;
	// Location information (used for Component)
	private long beginTagStartLine = 0;
	private long beginTagStartColumn = 0;
	private long beginTagEndLine = 0;
	private long beginTagEndColumn = 0;
	// Shared between Local Context and Evaluation Context
	private List<IncompleteTriple> incompleteTriples = null;
	private Registry prefixMappings;
	private Registry termMappings;
	private String language = null;
	private String vocabulary = null;
	private BaseURI base = null;
	// Evaluation Context specific
	private Component parentSubject = null;
	private Component parentObject = null;

	/**
	 * Class constructor.
	 * 
	 * @throws URISyntaxException
	 */
	public ProcessingContext(String baseURI) throws URISyntaxException {
		this.base = new BaseURI(baseURI);
		this.incompleteTriples = null;
		this.prefixMappings = new Registry();
		this.termMappings = new Registry();
		this.blankNodeHandler = new BlankNodeHandler();
	}

	/**
	 * @param localContext
	 * @param beginTagStartLine
	 * @param beginTagStartColumn
	 * @param beginTagEndLine
	 * @param beginTagEndColumn
	 */
	public ProcessingContext(ProcessingContext localContext,
			long beginTagStartLine, long beginTagStartColumn,
			long beginTagEndLine, long beginTagEndColumn) {
		this.parentContext = localContext;
		this.blankNodeHandler = localContext.getBlankNodeHandler();
		if (localContext.isSkipElement() == true) {
			// If the skip element flag is 'true' then the new evaluation
			// context is a copy of the current context that was passed in
			// to this level of processing, with the language and list of
			// URI mappings values replaced with the local values
			this.parentSubject = localContext.getParentSubject();
			this.parentObject = localContext.getParentObject();
			this.vocabulary = localContext.getParentContext().getVocabulary();
			this.base = localContext.getParentContext().getBase();
			this.termMappings = localContext.getParentContext()
					.getTermMappings();
		} else {
			// Otherwise, the values are:
			// * the base is set to the base value of the current
			// evaluation context;
			// * the parent subject is set to the value of new subject,
			// if non-null, or the value of the parent subject of the
			// current evaluation context;
			// * the parent object is set to value of current object
			// resource, if non-null, or the value of new subject, if
			// non-null, or the value of the parent subject of the
			// current evaluation context;
			// * the list of URI mappings is set to the local list of
			// URI mappings;
			// * the list of incomplete triples is set to
			// the local list of incomplete triples;
			// * language is set to the value of current language.
			// * the term mappings is set to the value of
			// the local term mappings.
			// * the default vocabulary is set to the value of
			// the local default vocabulary.
			if (localContext.getNewSubject() != null) {
				this.parentSubject = localContext.getNewSubject();
			} else {
				this.parentSubject = localContext.getParentSubject();
			}
			if (localContext.getCurrentObjectResource() != null) {
				this.parentObject = localContext.getCurrentObjectResource();
			} else if (localContext.getNewSubject() != null) {
				this.parentObject = localContext.getNewSubject();
			} else {
				this.parentObject = localContext.getParentSubject();
			}
			this.termMappings = localContext.getTermMappings();
			this.vocabulary = localContext.getVocabulary();
			this.base = localContext.getBase();
		}
		this.beginTagStartLine = beginTagStartLine;
		this.beginTagStartColumn = beginTagStartColumn;
		this.beginTagEndLine = beginTagEndLine;
		this.beginTagEndColumn = beginTagEndColumn;
		this.prefixMappings = localContext.getPrefixMappings();
		this.language = localContext.getLanguage();
	}

	/**
	 * @return
	 */
	public ProcessingContext getParentContext() {
		return parentContext;
	}

	/**
	 * @return
	 */
	public BlankNodeHandler getBlankNodeHandler() {
		return blankNodeHandler;
	}

	/**
	 * @return
	 */
	public BaseURI getBase() {
		return base;
	}

	/**
	 * @return
	 */
	public String generateBlankNode() {
		return blankNodeHandler.generateBlankNode();
	}

	/**
	 * @return the skipElement
	 */
	public boolean isSkipElement() {
		return skipElement;
	}

	/**
	 * @param skipElement
	 *            the skipElement to set
	 */
	public void setSkipElement(boolean skipElement) {
		this.skipElement = skipElement;
	}

	/**
	 * @return the newSubject
	 */
	public Component getNewSubject() {
		return newSubject;
	}

	/**
	 * @param newSubject
	 *            the newSubject to set
	 */
	public void setNewSubject(Component newSubject) {
		this.newSubject = newSubject;
	}

	/**
	 * @return the currentObjectResource
	 */
	public Component getCurrentObjectResource() {
		return currentObjectResource;
	}

	/**
	 * @param currentObjectResource
	 *            the currentObjectResource to set
	 */
	public void setCurrentObjectResource(Component currentObjectResource) {
		this.currentObjectResource = currentObjectResource;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the property
	 */
	public String[] getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(String[] property) {
		this.property = property;
	}

	/**
	 * @return the datatype
	 */
	public Component getDatatype() {
		return datatype;
	}

	/**
	 * @param datatype
	 *            the datatype to set
	 */
	public void setDatatype(Component datatype) {
		this.datatype = datatype;
	}

	/**
	 * @return the beginTagStartLine
	 */
	public long getBeginTagStartLine() {
		return beginTagStartLine;
	}

	/**
	 * @param beginTagStartLine
	 *            the beginTagStartLine to set
	 */
	public void setBeginTagStartLine(long beginTagStartLine) {
		this.beginTagStartLine = beginTagStartLine;
	}

	/**
	 * @return the beginTagStartColumn
	 */
	public long getBeginTagStartColumn() {
		return beginTagStartColumn;
	}

	/**
	 * @param beginTagStartColumn
	 *            the beginTagStartColumn to set
	 */
	public void setBeginTagStartColumn(long beginTagStartColumn) {
		this.beginTagStartColumn = beginTagStartColumn;
	}

	/**
	 * @return the beginTagEndLine
	 */
	public long getBeginTagEndLine() {
		return beginTagEndLine;
	}

	/**
	 * @param beginTagEndLine
	 *            the beginTagEndLine to set
	 */
	public void setBeginTagEndLine(long beginTagEndLine) {
		this.beginTagEndLine = beginTagEndLine;
	}

	/**
	 * @return the beginTagEndColumn
	 */
	public long getBeginTagEndColumn() {
		return beginTagEndColumn;
	}

	/**
	 * @param beginTagEndColumn
	 *            the beginTagEndColumn to set
	 */
	public void setBeginTagEndColumn(long beginTagEndColumn) {
		this.beginTagEndColumn = beginTagEndColumn;
	}

	/**
	 * @param parentContext
	 *            the parentContext to set
	 */
	public void setParentContext(ProcessingContext parentContext) {
		this.parentContext = parentContext;
	}

	/**
	 * @return
	 */
	public Component getParentSubject() {
		return parentSubject;
	}

	/**
	 * @param parentSubject
	 */
	public void setParentSubject(Component parentSubject) {
		this.parentSubject = parentSubject;
	}

	/**
	 * @return
	 */
	public Component getParentObject() {
		return parentObject;
	}

	/**
	 * @param parentObject
	 */
	public void setParentObject(Component parentObject) {
		this.parentObject = parentObject;
	}

	/**
	 * @param Prefix
	 * @return
	 */
	public String resolvePrefix(String Prefix) {
		return prefixMappings.get(Prefix);
	}

	/**
	 * @param Prefix
	 * @param URI
	 */
	public void registerPrefix(String Prefix, String URI) {
		if (Prefix.compareTo("_") != 0) {
			// '_' is prohibited namespace prefix
			if (parentContext != null
					&& prefixMappings == parentContext.getPrefixMappings()) {
				prefixMappings = new Registry(prefixMappings);
			}
			prefixMappings.set(Prefix, URI);
		}
	}

	/**
	 * @return
	 */
	public Registry getPrefixMappings() {
		return prefixMappings;
	}

	/**
	 * @param Term
	 * @return
	 */
	public String resolveTerm(String Term) {
		return termMappings.get(Term);
	}

	/**
	 * @param Term
	 * @param URI
	 */
	public void registerTerm(String Term, String URI) {
		if (parentContext != null && termMappings == parentContext.termMappings) {
			termMappings = new Registry(termMappings);
		}
		termMappings.set(Term, URI);
	}

	/**
	 * @return
	 */
	public Registry getTermMappings() {
		return termMappings;
	}

	/**
	 * @return
	 */
	public List<IncompleteTriple> getEvaluationIncompleteTriples() {
		if (parentContext != null) {
			if (parentContext.isSkipElement() == true) {
				return parentContext.getEvaluationIncompleteTriples();
			} else {
				return parentContext.getLocalIncompleteTriples();
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public List<IncompleteTriple> getLocalIncompleteTriples() {
		return incompleteTriples;
	}

	/**
	 * @param incompleteTriples
	 */
	public void setLocalIncompleteTriples(
			List<IncompleteTriple> incompleteTriples) {
		this.incompleteTriples = incompleteTriples;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return
	 */
	public String getVocabulary() {
		return vocabulary;
	}

	/**
	 * @param vocabulary
	 */
	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}

	/**
	 * @param base
	 */
	public void setBase(String base) {
		try {
			this.base.setURI(base);
		} catch (URISyntaxException exception) {
			// URI violates RFC 2396
		}
	}

	/**
	 * @param inputURI
	 * @return
	 */
	public Component getQualifiedNameU(String inputURI) {
		Component uri = null;
		try {
			if (inputURI.isEmpty() == true) {
				// Empty input URI
				uri = new Component(base);
			} else {
				URI iURI = new URI(inputURI);
				if (iURI.isAbsolute() == true) {
					uri = new Component(inputURI);
				} else if (base != null) {
					uri = new Component(base, iURI);
				}
			}
		} catch (Exception exception) {
			// Input URI violates RFC 2396
		}
		return uri;
	}

	/**
	 * @param CURIEorURI
	 * @return
	 */
	public Component getQualifiedNameCU(String CURIEorURI) {
		if (CURIEorURI.startsWith("[") == true
				&& CURIEorURI.endsWith("]") == true) {
			// SafeCURIE
			CURIEorURI = CURIEorURI.substring(1, CURIEorURI.length() - 1);
			if (CURIEorURI.isEmpty() == true) {
				return null;
			}
		}
		int colon = CURIEorURI.indexOf(':');
		Component uri = null;
		switch (colon) {
		default:
			// Prefix
			String prefix = CURIEorURI.substring(0, colon);
			String baseURI = resolvePrefix(prefix);
			if (baseURI != null) {
				uri = new Component(baseURI + CURIEorURI.substring(colon + 1));
			} else {
				// Prefix not found! Could be URI
				if (prefix.equals("_") == true) {
					// Map blank node
					uri = new Component(
							blankNodeHandler.mapBlankNode(CURIEorURI));
				} else {
					uri = new Component(CURIEorURI);
				}
			}
			break;
		case 0:
			// Default prefix
			if (getVocabulary() != null) {
				String term = CURIEorURI.substring(1);
				if (term.isEmpty() == true || XMLChar.isValidNCName(term) == true) {
					uri = new Component(getVocabulary() + term);
				}
			}
			break;
		case -1:
			uri = getQualifiedNameU(CURIEorURI);
			break;
		}
		return uri;
	}

	/**
	 * @param TERMorCURIEorAbsURI
	 * @return
	 */
	public Component getQualifiedNameTCU(String TERMorCURIEorAbsURI) {
		if (TERMorCURIEorAbsURI.startsWith("[") == true
				&& TERMorCURIEorAbsURI.endsWith("]")) {
			// SafeCURIE
			TERMorCURIEorAbsURI = TERMorCURIEorAbsURI.substring(1,
					TERMorCURIEorAbsURI.length() - 1);
		}
		int colon = TERMorCURIEorAbsURI.indexOf(':');
		Component uri = null;
		switch (colon) {
		default:
			// Prefix
			String prefix = TERMorCURIEorAbsURI.substring(0, colon);
			String baseURI = resolvePrefix(prefix);
			if (baseURI != null) {
				uri = new Component(baseURI
						+ TERMorCURIEorAbsURI.substring(colon + 1));
			} else {
				// Prefix not registered!
				if (prefix.equals("_") == true) {
					// Perhaps Blank node "_:*"
					uri = new Component(
							blankNodeHandler.mapBlankNode(TERMorCURIEorAbsURI));
				} else {
					// Perhaps an absolute uri
					try {
						URI absoluteURI = new URI(TERMorCURIEorAbsURI);
						if (absoluteURI.isAbsolute() == true) {
							uri = new Component(TERMorCURIEorAbsURI);
						}
					} catch (URISyntaxException exception) {
					}
				}
			}
			break;
		case 0:
			if (getVocabulary() != null) {
				// Default prefix
				String term = TERMorCURIEorAbsURI.substring(1);
				if (term.isEmpty() == true || XMLChar.isValidNCName(term) == true) {
					uri = new Component(getVocabulary() + term);
				}
			}
			break;
		case -1:
			// No colon - TERM or EMPTY
			if (TERMorCURIEorAbsURI.isEmpty() == true) {
				uri = new Component("");
			} else {
				// Try to resolve as a term
				String termURI = resolveTerm(TERMorCURIEorAbsURI);
				if (termURI != null) {
					// Term uri
					uri = new Component(termURI);
				} else if (XMLChar.isValidNCName(TERMorCURIEorAbsURI) == true) {
					// Default prefix (if any)
					if (getVocabulary() != null) {
						uri = new Component(getVocabulary()
								+ TERMorCURIEorAbsURI);
					}
				}
			}
			break;
		}
		return uri;
	}
}
