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
	private String content;
	private String datatype;
	private String href;
	private String defaultXmlns;
	private List<PrefixMapping> xmlns;
	private List<PrefixMapping> prefix;
	private String []profile;
	private String []property;
	private String []rel;
	private String resource;
	private String []rev;
	private String src;
	private String []typeof;
	private String vocab;
	private String lang;
	
	/**
	 * Constructor
	 * 
	 * @param Attributes the attributes
	 */
	public RDFaAttributes(Attributes attributes) {
		xmlns = new ArrayList<PrefixMapping>();
		prefix = new ArrayList<PrefixMapping>();
		
		// Process attributes
		for (int i = 0; i < attributes.getCount(); i++) {
			String attributeQName = attributes.getQName(i);
			if ("vocab".equals(attributeQName) == true) {
				// 2.
				// @vocab
				vocab = attributes.getValue(i);
			} else if ("profile".equals(attributeQName) == true) {
				// @profile
				profile = attributes.getValue(i).trim()
						.split("\\s");
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
			} else if (attributeQName.startsWith("xmlns:")) {
				// @xmlns:*
				xmlns.add(new PrefixMapping(
						attributeQName.substring(6), attributes.getValue(i)));
			} else if ("lang".equals(attributeQName) == true
					|| (RDFaParser.XML_NS.equals(attributes.getURI(i)) == true && "lang"
							.equals(attributes.getLocalName(i)) == true)) {
				// @lang or @xml:lang
				lang = attributes.getValue(i);
			} else if ("rev".equals(attributeQName) == true) {
				// @rev
				rev = attributes.getValue(i).trim()
						.split("\\s+");
			} else if ("rel".equals(attributeQName) == true) {
				// @rel
				rel = attributes.getValue(i).trim()
						.split("\\s+");
			} else if ("about".equals(attributeQName) == true) {
				// @about
				about = attributes.getValue(i);
			} else if ("src".equals(attributeQName) == true) {
				// @src
				src = attributes.getValue(i);
			} else if ("href".equals(attributeQName) == true) {
				// @href
				href = attributes.getValue(i);
			} else if ("typeof".equals(attributeQName) == true) {
				// @typeof
				typeof = attributes.getValue(i).trim()
						.split("\\s+");
			} else if ("property".equals(attributeQName) == true) {
				// @property
				property = attributes.getValue(i).trim()
						.split("\\s+");
			} else if ("resource".equals(attributeQName) == true) {
				// @resource
				resource = attributes.getValue(i);
			} else if ("content".equals(attributeQName) == true) {
				// @content
				content = attributes.getValue(i);
			} else if ("datatype".equals(attributeQName) == true) {
				// @datatype
				datatype = attributes.getValue(i);
			} else if ("xmlns".equals(attributeQName) == true) {
				// @xmlns
				defaultXmlns = attributes.getValue(i);
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
	 * @param about the about to set
	 */
	public void setAbout(String about) {
		this.about = about;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the datatype
	 */
	public String getDatatype() {
		return datatype;
	}
	/**
	 * @param datatype the datatype to set
	 */
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}
	/**
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}
	/**
	 * @return the defaultXmlns
	 */
	public String getDefaultXmlns() {
		return defaultXmlns;
	}
	/**
	 * @param defaultXmlns the defaultXmlns to set
	 */
	public void setDefaultXmlns(String defaultXmlns) {
		this.defaultXmlns = defaultXmlns;
	}
	/**
	 * @return the xmlns
	 */
	public List<PrefixMapping> getXmlns() {
		return xmlns;
	}
	/**
	 * @param xmlns the xmlns to set
	 */
	public void addXmlns(PrefixMapping xmlns) {
		this.xmlns.add(xmlns);
	}
	/**
	 * @return the prefix
	 */
	public List<PrefixMapping> getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix the prefix to set
	 */
	public void addPrefix(PrefixMapping prefix) {
		this.prefix.add(prefix);
	}
	/**
	 * @return the profile
	 */
	public String[] getProfile() {
		return profile;
	}
	/**
	 * @param profile the profile to set
	 */
	public void setProfile(String[] profile) {
		this.profile = profile;
	}
	/**
	 * @return the property
	 */
	public String[] getProperty() {
		return property;
	}
	/**
	 * @param property the property to set
	 */
	public void setProperty(String[] property) {
		this.property = property;
	}
	/**
	 * @return the rel
	 */
	public String[] getRel() {
		return rel;
	}
	/**
	 * @param rel the rel to set
	 */
	public void setRel(String[] rel) {
		this.rel = rel;
	}
	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
	/**
	 * @return the rev
	 */
	public String[] getRev() {
		return rev;
	}
	/**
	 * @param rev the rev to set
	 */
	public void setRev(String[] rev) {
		this.rev = rev;
	}
	/**
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * @param src the src to set
	 */
	public void setSrc(String src) {
		this.src = src;
	}
	/**
	 * @return the typeof
	 */
	public String[] getTypeof() {
		return typeof;
	}
	/**
	 * @param typeof the typeof to set
	 */
	public void setTypeof(String[] typeof) {
		this.typeof = typeof;
	}
	/**
	 * @return the vocab
	 */
	public String getVocab() {
		return vocab;
	}
	/**
	 * @param vocab the vocab to set
	 */
	public void setVocab(String vocab) {
		this.vocab = vocab;
	}
	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}
	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
}
