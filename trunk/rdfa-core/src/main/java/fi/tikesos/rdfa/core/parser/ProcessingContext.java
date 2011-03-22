package fi.tikesos.rdfa.core.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import fi.tikesos.rdfa.core.datatype.IncompleteTriple;
import fi.tikesos.rdfa.core.datatype.LString;
import fi.tikesos.rdfa.core.registry.Registry;

/**
 * ProcessingContext contains both Local Context and
 * Evaluation Context defined in RDFa 1.1 specification.
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
	private LString newSubject = null;
	private LString currentObjectResource = null;
	private String content = null;
	private String[] property = null;
	private String datatype = null;
	// Location information (used for LString)
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
	private URI base = null;
	// Evaluation Context specific
	private LString parentSubject = null;
	private LString parentObject = null;

	/**
	 * Class constructor.
	 */
	public ProcessingContext() {
		this.incompleteTriples = null;
		this.prefixMappings = new Registry();
		this.termMappings = new Registry();
		this.blankNodeHandler = new BlankNodeHandler();
	}

	/**
	 * @param localContext
	 */
	public ProcessingContext(ProcessingContext localContext) {
		parentContext = localContext;
		blankNodeHandler = localContext.getBlankNodeHandler();
		if (localContext.isSkipElement() == true) {
			// If the skip element flag is 'true' then the new evaluation
			// context is a copy of the current context that was passed in
			// to this level of processing, with the language and list of
			// URI mappings values replaced with the local values
			parentSubject = localContext.getParentSubject();
			parentObject = localContext.getParentObject();
			vocabulary = localContext.getParentContext().getVocabulary();
			base = localContext.getParentContext().getBase();
			termMappings = localContext.getParentContext().getTermMappings();
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
				parentSubject = localContext.getNewSubject();
			} else {
				parentSubject = localContext.getParentSubject();
			}
			if (localContext.getCurrentObjectResource() != null) {
				parentObject = localContext.getCurrentObjectResource();
			} else if (localContext.getNewSubject() != null) {
				parentObject = localContext.getNewSubject();
			} else {
				parentObject = localContext.getParentSubject();
			}
			termMappings = localContext.getTermMappings();
			vocabulary = localContext.getVocabulary();
			base = localContext.getBase();
		}
		prefixMappings = localContext.getPrefixMappings();
		language = localContext.getLanguage();
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
	public LString getNewSubject() {
		return newSubject;
	}

	/**
	 * @param newSubject
	 *            the newSubject to set
	 */
	public void setNewSubject(LString newSubject) {
		this.newSubject = newSubject;
	}

	/**
	 * @return the currentObjectResource
	 */
	public LString getCurrentObjectResource() {
		return currentObjectResource;
	}

	/**
	 * @param currentObjectResource
	 *            the currentObjectResource to set
	 */
	public void setCurrentObjectResource(LString currentObjectResource) {
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
	public String getDatatype() {
		return datatype;
	}

	/**
	 * @param datatype
	 *            the datatype to set
	 */
	public void setDatatype(String datatype) {
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
	public LString getParentSubject() {
		return parentSubject;
	}

	/**
	 * @param parentSubject
	 */
	public void setParentSubject(LString parentSubject) {
		this.parentSubject = parentSubject;
	}

	/**
	 * @return
	 */
	public LString getParentObject() {
		return parentObject;
	}

	/**
	 * @param parentObject
	 */
	public void setParentObject(LString parentObject) {
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
	public void setLocalIncompleteTriples(List<IncompleteTriple> incompleteTriples) {
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
			this.base = new URI(base);
		} catch (URISyntaxException exception) {
			// URI violates RFC 2396
		}
	}

	/**
	 * @return
	 */
	public URI getBase() {
		return base;
	}

	
	/**
	 * @param inputURI
	 * @return
	 */
	public String getQualifiedNameU(String inputURI) {
		String resultURI = null;
		if (getBase() != null) {
			try {
				resultURI = base.resolve(inputURI).toString();
			} catch (Exception exception) {
				// URI violates RFC 2396
			}
		} else {
			// Base is not set
			try {
				resultURI = new URI(inputURI).toString();
				new URI(inputURI);
			} catch (URISyntaxException exception) {
				// URI violates RFC 2396
			}
		}
		return resultURI;
	}

	/**
	 * @param CURIEorURI
	 * @return
	 */
	public String getQualifiedNameCU(String CURIEorURI) {
		if (CURIEorURI.startsWith("[") == true && CURIEorURI.endsWith("]") == true) {
			// SafeCURIE
			CURIEorURI = CURIEorURI.substring(1, CURIEorURI.length() - 1);
			if (CURIEorURI.isEmpty() == true) {
				return null;
			}
		}
		int colon = CURIEorURI.indexOf(':');
		String uri = null;
		switch (colon) {
		default:
			// Prefix
			String prefix = CURIEorURI.substring(0, colon);
			String baseURI = resolvePrefix(prefix);
			if (baseURI != null) {
				uri = baseURI + CURIEorURI.substring(colon + 1);
			} else {
				// Prefix not found! Could be URI
				if (prefix.equals("_") == true) {
					// Map blank node
					uri = blankNodeHandler.mapBlankNode(CURIEorURI);
				} else {
					uri = CURIEorURI;
				}
			}
			break;
		case 0:
			// Default prefix
			if (getVocabulary() != null && getVocabulary().isEmpty() == false) {
				uri = getVocabulary() + CURIEorURI.substring(1);
			}
//			uri = "http://www.w3.org/1999/xhtml/vocab#" + CURIEorURI.substring(1);
			break;
		case -1:
			if (CURIEorURI.isEmpty() == true) {
				// Empty CURIE or relative URI
				uri = base.toString();
			} else {
				uri = getQualifiedNameU(CURIEorURI);
			}
			break;
		}
		return uri;
	}

	/**
	 * @param TERMorCURIEorAbsURI
	 * @return
	 */
	public String getQualifiedNameTCU(String TERMorCURIEorAbsURI) {
		if (TERMorCURIEorAbsURI.startsWith("[") == true
				&& TERMorCURIEorAbsURI.endsWith("]")) {
			// SafeCURIE
			TERMorCURIEorAbsURI = TERMorCURIEorAbsURI.substring(1,
					TERMorCURIEorAbsURI.length() - 1);
		}
		int colon = TERMorCURIEorAbsURI.indexOf(':');
		String uri = null;
		switch (colon) {
		default:
			// Prefix
			String prefix = TERMorCURIEorAbsURI.substring(0, colon);
			String baseURI = resolvePrefix(prefix);
			if (baseURI != null) {
				uri = baseURI + TERMorCURIEorAbsURI.substring(colon + 1);
			} else {
				// Prefix not found! Could be Blank node
				if (prefix.equals("_") == true) {
					// Blank node
					uri = blankNodeHandler.mapBlankNode(TERMorCURIEorAbsURI);
				} else {
					// Perhaps an absolute uri
					try {
						uri = new URI(TERMorCURIEorAbsURI).toString();
					} catch (URISyntaxException exception) {
					}
				}
			}
			break;
		case 0:
			if (getVocabulary() != null) {
				// Default prefix
				uri = getVocabulary() + TERMorCURIEorAbsURI.substring(1);
			}
			break;
		case -1:
			// No colon - TERM or EMPTY
			if (TERMorCURIEorAbsURI.isEmpty() == true) {
				uri = "";
			} else {
				// Try to resolve as a term
				uri = resolveTerm(TERMorCURIEorAbsURI);
				if (uri == null) {
					// Default prefix (if any)
					if (getVocabulary() != null) {
						uri = getVocabulary() + TERMorCURIEorAbsURI;
					}
				}
			}
			break;
		}
		return uri;
	}
}
