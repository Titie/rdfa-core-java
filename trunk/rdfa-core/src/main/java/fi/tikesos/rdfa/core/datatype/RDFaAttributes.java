package fi.tikesos.rdfa.core.datatype;

import java.util.ArrayList;
import java.util.List;

import fi.tikesos.rdfa.core.parser.RDFaParser;

/**
 * @author ssakorho
 *
 */
public class RDFaAttributes {
	private String about;
	private Location aboutLocation;
	private String content;
	private Location contentLocation;
	private String datatype;
	private Location datatypeLocation;
	private String href;
	private Location hrefLocation;
	private String defaultXmlns;
	private Location defaultXmlnsLocation;
	private List<PrefixMapping> xmlns;
	private List<Location> xmlnsLocation;
	private List<PrefixMapping> prefix;
	private Location prefixLocation;
	private String []profile;
	private Location profileLocation;
	private String []property;
	private Location propertyLocation;
	private String []rel;
	private Location relLocation;
	private String resource;
	private Location resourceLocation;
	private String []rev;
	private Location revLocation;
	private String src;
	private Location srcLocation;
	private String []typeof;
	private Location typeofLocation;
	private String vocab;
	private Location vocabLocation;
	private String lang;
	private Location langLocation;
	
	/**
	 * Constructor
	 * 
	 * @param Attributes the attributes
	 */
	public RDFaAttributes(Attributes attributes) {
		xmlns = new ArrayList<PrefixMapping>();
		xmlnsLocation = new ArrayList<Location>();
		prefix = new ArrayList<PrefixMapping>();
		
		// Process attributes
		for (int i = 0; i < attributes.getCount(); i++) {
			String attributeQName = attributes.getQName(i);
			if ("vocab".equals(attributeQName) == true) {
				// 2.
				// @vocab
				vocab = attributes.getValue(i);
				vocabLocation = attributes.getLocation(i);
			} else if ("profile".equals(attributeQName) == true) {
				// @profile
				profile = attributes.getValue(i).trim()
						.split("\\s");
				profileLocation = attributes.getLocation(i);
			} else if ("prefix".equals(attributeQName) == true) {
				// @prefix
				String[] prefix = attributes.getValue(i).trim().split("\\s+");
				for (int n = 0; n < prefix.length; n += 2) {
					if (prefix[n].endsWith(":") == true) {
						this.prefix.add
								(new PrefixMapping(
										prefix[n].substring(0,
												prefix[n].length() - 1),
										prefix[n + 1]));
					} // Else exception?
				}
				prefixLocation = attributes.getLocation(i);
			} else if (attributeQName.startsWith("xmlns:")) {
				// @xmlns:*
				xmlns.add(new PrefixMapping(
						attributeQName.substring(6), attributes.getValue(i)));
				xmlnsLocation.add(attributes.getLocation(i));
			} else if ("lang".equals(attributeQName) == true
					|| (RDFaParser.XML_NS.equals(attributes.getURI(i)) == true && "lang"
							.equals(attributes.getLocalName(i)) == true)) {
				// @lang or @xml:lang
				lang = attributes.getValue(i);
				langLocation = attributes.getLocation(i);
			} else if ("rev".equals(attributeQName) == true) {
				// @rev
				rev = attributes.getValue(i).trim()
						.split("\\s+");
				revLocation = attributes.getLocation(i);
			} else if ("rel".equals(attributeQName) == true) {
				// @rel
				rel = attributes.getValue(i).trim()
						.split("\\s+");
				relLocation = attributes.getLocation(i);
		} else if ("about".equals(attributeQName) == true) {
				// @about
				about = attributes.getValue(i);
				aboutLocation = attributes.getLocation(i);
			} else if ("src".equals(attributeQName) == true) {
				// @src
				src = attributes.getValue(i);
				srcLocation = attributes.getLocation(i);
			} else if ("href".equals(attributeQName) == true) {
				// @href
				href = attributes.getValue(i);
				hrefLocation = attributes.getLocation(i);
			} else if ("typeof".equals(attributeQName) == true) {
				// @typeof
				typeof = attributes.getValue(i).trim()
						.split("\\s+");
				typeofLocation = attributes.getLocation(i);
			} else if ("property".equals(attributeQName) == true) {
				// @property
				property = attributes.getValue(i).trim()
						.split("\\s+");
				propertyLocation = attributes.getLocation(i);
			} else if ("resource".equals(attributeQName) == true) {
				// @resource
				resource = attributes.getValue(i);
				resourceLocation = attributes.getLocation(i);
			} else if ("content".equals(attributeQName) == true) {
				// @content
				content = attributes.getValue(i);
				contentLocation = attributes.getLocation(i);
			} else if ("datatype".equals(attributeQName) == true) {
				// @datatype
				datatype = attributes.getValue(i);
				datatypeLocation = attributes.getLocation(i);
			} else if ("xmlns".equals(attributeQName) == true) {
				// @xmlns
				defaultXmlns = attributes.getValue(i);
				defaultXmlnsLocation = attributes.getLocation(i);
			}
		}
	}
	/**
	 * @return the about
	 */
	public String getAbout() {
		return about;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @return the datatype
	 */
	public String getDatatype() {
		return datatype;
	}
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}
	/**
	 * @return the defaultXmlns
	 */
	public String getDefaultXmlns() {
		return defaultXmlns;
	}
	/**
	 * @return the xmlns
	 */
	public List<PrefixMapping> getXmlns() {
		return xmlns;
	}
	/**
	 * @return the prefix
	 */
	public List<PrefixMapping> getPrefix() {
		return prefix;
	}
	/**
	 * @return the profile
	 */
	public String[] getProfile() {
		return profile;
	}
	/**
	 * @return the property
	 */
	public String[] getProperty() {
		return property;
	}
	/**
	 * @return the rel
	 */
	public String[] getRel() {
		return rel;
	}
	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	/**
	 * @return the rev
	 */
	public String[] getRev() {
		return rev;
	}
	/**
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * @return the typeof
	 */
	public String[] getTypeof() {
		return typeof;
	}
	/**
	 * @return the vocab
	 */
	public String getVocab() {
		return vocab;
	}
	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}
	/**
	 * @return the aboutLocation
	 */
	public Location getAboutLocation() {
		return aboutLocation;
	}
	/**
	 * @return the contentLocation
	 */
	public Location getContentLocation() {
		return contentLocation;
	}
	/**
	 * @return the datatypeLocation
	 */
	public Location getDatatypeLocation() {
		return datatypeLocation;
	}
	/**
	 * @return the hrefLocation
	 */
	public Location getHrefLocation() {
		return hrefLocation;
	}
	/**
	 * @return the defaultXmlnsLocation
	 */
	public Location getDefaultXmlnsLocation() {
		return defaultXmlnsLocation;
	}
	/**
	 * @return the xmlnsLocation
	 */
	public List<Location> getXmlnsLocation() {
		return xmlnsLocation;
	}
	/**
	 * @return the prefixLocation
	 */
	public Location getPrefixLocation() {
		return prefixLocation;
	}
	/**
	 * @return the profileLocation
	 */
	public Location getProfileLocation() {
		return profileLocation;
	}
	/**
	 * @return the propertyLocation
	 */
	public Location getPropertyLocation() {
		return propertyLocation;
	}
	/**
	 * @return the relLocation
	 */
	public Location getRelLocation() {
		return relLocation;
	}
	/**
	 * @return the resourceLocation
	 */
	public Location getResourceLocation() {
		return resourceLocation;
	}
	/**
	 * @return the revLocation
	 */
	public Location getRevLocation() {
		return revLocation;
	}
	/**
	 * @return the srcLocation
	 */
	public Location getSrcLocation() {
		return srcLocation;
	}
	/**
	 * @return the typeofLocation
	 */
	public Location getTypeofLocation() {
		return typeofLocation;
	}
	/**
	 * @return the vocabLocation
	 */
	public Location getVocabLocation() {
		return vocabLocation;
	}
	/**
	 * @return the langLocation
	 */
	public Location getLangLocation() {
		return langLocation;
	}
}
