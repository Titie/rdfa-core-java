package fi.tikesos.rdfa.core.profile;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.datatype.Language;
import fi.tikesos.rdfa.core.datatype.Lexical;
import fi.tikesos.rdfa.core.parser.RDFaParser;
import fi.tikesos.rdfa.core.triple.TripleSink;
import fi.tikesos.rdfa.core.util.NullEntityResolver;

/**
 * Non-caching (simple) profile loader.
 * 
 * @author ssakorho
 * 
 */
public class SimpleProfileLoader implements ProfileLoader {
	private Map<String, Profile> profileCache = new HashMap<String, Profile>();
	
	public Profile loadProfile(String profileURI) {
		Profile profile = profileCache.get(profileURI);
		if (profile == null) {
			try {
				XMLReader reader = XMLReaderFactory.createXMLReader();
				ProfileTripleSink profileTripleSink = new ProfileTripleSink();
				RDFaParser parser = new RDFaParser(profileURI, profileTripleSink,
						null, RDFaParser.XML_RDFA);
	
				reader.setFeature("http://xml.org/sax/features/validation",
						Boolean.FALSE);
				reader.setContentHandler(parser);
				reader.setEntityResolver(new NullEntityResolver());
				reader.parse(new InputSource(new URI(profileURI).toURL()
						.openStream()));
	
				profile = new SimpleProfile(profileTripleSink.getTermMappings(),
						profileTripleSink.getPrefixMappings(),
						profileTripleSink.getDefaultVocabulary());
			} catch (Exception exception) {
				// TODO: log exception?
			} finally {
				// Cache profile
				profileCache.put(profileURI, profile);
			}
		}
		return profile;
	}

	private class SimpleProfile implements Profile {
		private String defaultVocabulary;
		private Map<String, String> termMappings;
		private Map<String, String> prefixMappings;

		public SimpleProfile(Map<String, String> termMappings,
				Map<String, String> prefixMappings, String defaultVocabulary) {
			this.termMappings = termMappings;
			this.prefixMappings = prefixMappings;
			this.defaultVocabulary = defaultVocabulary;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see fi.tikesos.rdfa.core.profile.Profile#getTermMappings()
		 */
		public Map<String, String> getTermMappings() {
			return termMappings;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see fi.tikesos.rdfa.core.profile.Profile#getPrefixMappings()
		 */
		public Map<String, String> getPrefixMappings() {
			return prefixMappings;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see fi.tikesos.rdfa.core.profile.Profile#getDefaultVocabulary()
		 */
		public String getDefaultVocabulary() {
			return defaultVocabulary;
		}
	}

	private class ProfileTripleSink implements TripleSink {
		private final static String RDFA_NS = "http://www.w3.org/ns/rdfa#";
		private String defaultVocabulary = null;
		private Map<String, Map<String, String>> tripleMap = new HashMap<String, Map<String, String>>();

		/**
		 * @return list of term and uri pairs
		 */
		public Map<String, String> getTermMappings() {
			Map<String, String> termMappings = new HashMap<String, String>();
			for (Entry<String, Map<String, String>> tripleEntry : tripleMap
					.entrySet()) {
				String term = tripleEntry.getValue().get("term");
				if (term != null && term.isEmpty() == false) {
					String uri = tripleEntry.getValue().get("uri");
					if (uri != null && uri.isEmpty() == false) {
						termMappings.put(term, uri);
					}
				}
			}
			return termMappings;
		}

		/**
		 * @return the default vocabulary or null
		 */
		public String getDefaultVocabulary() {
			return defaultVocabulary;
		}

		/**
		 * @return list of prefix and uri pairs
		 */
		public Map<String, String> getPrefixMappings() {
			Map<String, String> prefixMappings = new HashMap<String, String>();
			for (Entry<String, Map<String, String>> tripleEntry : tripleMap
					.entrySet()) {
				String prefix = tripleEntry.getValue().get("prefix");
				if (prefix != null && prefix.isEmpty() == false) {
					String uri = tripleEntry.getValue().get("uri");
					if (uri != null && uri.isEmpty() == false) {
						prefixMappings.put(prefix, uri);
					}
				}
			}
			return prefixMappings;
		}

		public void generateTriple(Component subject, Component predicate,
				Component object) {
			// Ignored
		}

		public void generateTripleLiteral(Component subject,
				Component predicate, Lexical lexical, Language language,
				Component datatype) {
			// Store triple to map, if it's of known type
			String type = null;
			if ((RDFA_NS + "uri").equals(predicate.getValue()) == true) {
				type = "uri";
			} else if ((RDFA_NS + "prefix").equals(predicate.getValue()) == true) {
				type = "prefix";
			} else if ((RDFA_NS + "term").equals(predicate.getValue()) == true) {
				type = "term";
			} else if ((RDFA_NS + "vocabulary").equals(predicate.getValue()) == true) {
				defaultVocabulary = lexical.getValue();
			}
			if (type != null) {
				Map<String, String> valueMap = tripleMap
						.get(subject.getValue());
				if (valueMap == null) {
					valueMap = new HashMap<String, String>();
					tripleMap.put(subject.getValue(), valueMap);
				}
				valueMap.put(type, lexical.getValue());
			}
		}
	}
}
