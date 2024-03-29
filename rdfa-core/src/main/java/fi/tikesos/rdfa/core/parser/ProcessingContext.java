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
package fi.tikesos.rdfa.core.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.util.XMLChar;

import fi.tikesos.rdfa.core.datatype.BaseURI;
import fi.tikesos.rdfa.core.datatype.IncompleteTriple;
import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.datatype.Language;
import fi.tikesos.rdfa.core.datatype.Literal;
import fi.tikesos.rdfa.core.datatype.Location;

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
	private Literal content = null;
	private String[] property = null;
	private Location propertyLocation = null;
	private Component datatype = null;
	// Custom data: flag to indicate profile loading has failed
	private boolean profileFailed = false;
	// Shared between Local Context and Evaluation Context
	private List<IncompleteTriple> incompleteTriples = null;
	private Map<String, String> prefixMappings = null;
	private Map<String, String> termMappings = null;
	private Language language = null;
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
		this.prefixMappings = new HashMap<String, String>();
		this.termMappings = new HashMap<String, String>();
		this.blankNodeHandler = new BlankNodeHandler();
		// Register default prefix
		this.prefixMappings.put("", "http://www.w3.org/1999/xhtml/vocab#");
	}

	/**
	 * @param localContext
	 */
	public ProcessingContext(ProcessingContext localContext) {
		if (localContext.isSkipElement() == true) {
			// If the skip element flag is 'true' then the new evaluation
			// context is a copy of the current context that was passed in
			// to this level of processing, with the language and list of
			// URI mappings values replaced with the local values
			this.parentSubject = localContext.getParentSubject();
			this.parentObject = localContext.getParentObject();
			// !! Is this really intended? !!
			this.vocabulary = localContext.getParentContext().getVocabulary();
			// this.termMappings = localContext.getParentContext()
			// .getTermMappings();
			this.base = localContext.getParentContext().getBase();
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
			// !! Is this really intended? !!
			this.vocabulary = localContext.getVocabulary();
			// this.termMappings = localContext.getTermMappings();
			this.base = localContext.getBase();
		}
		this.skipElement = false;
		this.profileFailed = localContext.isProfileFailed();
		this.parentContext = localContext;
		this.blankNodeHandler = localContext.getBlankNodeHandler();
		this.prefixMappings = localContext.getPrefixMappings();
		this.termMappings = localContext.getTermMappings();
		this.language = localContext.getLanguage();
	}

	/**
	 * @return The parent context
	 */
	public ProcessingContext getParentContext() {
		return parentContext;
	}

	/**
	 * @return The blank node handler
	 */
	public BlankNodeHandler getBlankNodeHandler() {
		return blankNodeHandler;
	}

	/**
	 * @param profileFailed The profile failed flag to set
	 */
	public void setProfileFailed(boolean profileFailed) {
		this.profileFailed = profileFailed;
	}

	/**
	 * @return The profile failed flag
	 */
	public boolean isProfileFailed() {
		return profileFailed;
	}

	/**
	 * @return The current base
	 */
	public BaseURI getBase() {
		return base;
	}

	/**
	 * @return A blank node
	 */
	public String generateBlankNode() {
		return blankNodeHandler.generateBlankNode();
	}

	/**
	 * @return The skip element flag
	 */
	public boolean isSkipElement() {
		return skipElement;
	}

	/**
	 * @param skipElement
	 *            The skip element flag to set
	 */
	public void setSkipElement(boolean skipElement) {
		this.skipElement = skipElement;
	}

	/**
	 * @return The New Subject
	 */
	public Component getNewSubject() {
		return newSubject;
	}

	/**
	 * @param newSubject
	 *            The New Subject to set
	 */
	public void setNewSubject(Component newSubject) {
		this.newSubject = newSubject;
	}

	/**
	 * @return The Current Object Resource
	 */
	public Component getCurrentObjectResource() {
		return currentObjectResource;
	}

	/**
	 * @param currentObjectResource
	 *            The Current Object Resource to set
	 */
	public void setCurrentObjectResource(Component currentObjectResource) {
		this.currentObjectResource = currentObjectResource;
	}

	/**
	 * @return The content
	 */
	public Literal getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set
	 */
	public void setContent(Literal content) {
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
	 * @return the propertyLocation
	 */
	public Location getPropertyLocation() {
		return propertyLocation;
	}

	/**
	 * @param propertyLocation
	 *            the propertyLocation to set
	 */
	public void setPropertyLocation(Location propertyLocation) {
		this.propertyLocation = propertyLocation;
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
	 * @param prefix
	 * @return
	 */
	public String resolvePrefix(String prefix) {
		return prefixMappings.get(prefix);
	}

	/**
	 * @param prefix
	 * @param uri
	 */
	public void registerPrefix(String prefix, String uri) {
		if (prefix.length() > 0 && prefix.compareTo("_") != 0) {
			// '_' is prohibited namespace prefix
			if (parentContext != null
					&& prefixMappings == parentContext.getPrefixMappings()) {
				prefixMappings = new HashMap<String, String>(prefixMappings);
			}
			prefixMappings.put(prefix.toLowerCase(), uri);
		}
	}

	/**
	 * @return
	 */
	public Map<String, String> getPrefixMappings() {
		return prefixMappings;
	}
	
	/**
	 * @param term
	 * @return
	 */
	public boolean isTermRegistered(String term) {
		return termMappings.get(term) != null;
	}

	/**
	 * @param term
	 * @return
	 */
	public String resolveTerm(String term) {
		String uri = termMappings.get(term);
		if (uri == null) {
			// Falling to case-insensitive matching (slow!)
			for (String registeredTerm : termMappings.keySet()) {
				if (term.equalsIgnoreCase(registeredTerm) == true) {
					uri = termMappings.get(registeredTerm);
					break;
				}
			}
		}
		return uri;
	}

	/**
	 * @param term
	 * @param uri
	 */
	public void registerTerm(String term, String uri) {
		if (parentContext != null
				&& parentContext.getTermMappings() == termMappings) {
			termMappings = new HashMap<String, String>(termMappings);
		}
		termMappings.put(term, uri);
	}

	/**
	 * @return
	 */
	public Map<String, String> getTermMappings() {
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
	public Language getLanguage() {
		return language;
	}

	/**
	 * @param language
	 */
	public void setLanguage(Language language) {
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
	 * @throws URISyntaxException
	 */
	public void setBase(String base) throws URISyntaxException {
		this.base.setURI(base);
	}

	/**
	 * @param inputURI
	 * @return
	 */
	public Component expandURI(String inputURI) throws URISyntaxException {
		return inputURI.isEmpty() == true ? new Component(base)
				: new Component(base, new URI(inputURI));
	}

	/**
	 * @param colon
	 * @param CURIEorAbsURI
	 * @return
	 * @throws URISyntaxException
	 */
	private Component expandCURIEorAbsURI(int colon, String CURIEorAbsURI)
			throws URISyntaxException {
		Component uri = null;
		
		// Perhaps a CURIE
		String prefixURI = resolvePrefix(CURIEorAbsURI.substring(0, colon));
		if (prefixURI != null) {
			uri = new Component(base, new URI(prefixURI
					+ CURIEorAbsURI.substring(colon + 1)));
		} else {
			// Prefix not registered, not a CURIE
			if (colon == 1 && CURIEorAbsURI.charAt(0) == '_') {
				// Blank node "_:*"
				uri = new Component(
						blankNodeHandler.mapBlankNode(CURIEorAbsURI));
			} else {
				// Perhaps an absolute uri
				URI absoluteURI = new URI(CURIEorAbsURI);
				if (absoluteURI.isAbsolute() == false) {
					throw new URISyntaxException(CURIEorAbsURI,
							"not an absolute uri");
				}
				uri = new Component(CURIEorAbsURI);
			}
		}
		return uri;
	}

	/**
	 * @param term
	 * @return
	 * @throws URISyntaxException
	 */
	private Component expandTerm(String term) throws URISyntaxException {
		Component uri = null;
		String termURI = resolveTerm(term);
		if (termURI != null) {
			// Term URI
			uri = new Component(termURI);
		} else if (XMLChar.isValidNCName(term) == true) {
			// No prefix (if any)
			if (getVocabulary() == null) {
				throw new URISyntaxException(term,
						"define no prefix vocabulary or add prefix to term");
			}
			uri = new Component(getVocabulary() + term);
		} else {
			throw new URISyntaxException(term, "not a valid term");
		}
		return uri;
	}

	/**
	 * @param CURIEorURI
	 * @return
	 */
	public Component expandCURIEorURI(String CURIEorURI)
			throws URISyntaxException {
		if (CURIEorURI.startsWith("[") == true
				&& CURIEorURI.endsWith("]") == true) {
			// SafeCURIE
			if (CURIEorURI.length() == 2) {
				throw new URISyntaxException(CURIEorURI,
						"not a valid safe curie");
			}
			CURIEorURI = CURIEorURI.substring(1, CURIEorURI.length() - 1);
		}
		int colon = CURIEorURI.indexOf(':');
		return colon != -1 ? expandCURIEorAbsURI(colon, CURIEorURI)
				: expandURI(CURIEorURI);
	}

	/**
	 * @param TERMorCURIEorAbsURI
	 * @return
	 */
	public Component expandTERMorCURIEorAbsURI(String TERMorCURIEorAbsURI)
			throws URISyntaxException {
		int colon = TERMorCURIEorAbsURI.indexOf(':');
		return colon != -1 ? expandCURIEorAbsURI(colon, TERMorCURIEorAbsURI)
				: expandTerm(TERMorCURIEorAbsURI);
	}
}
