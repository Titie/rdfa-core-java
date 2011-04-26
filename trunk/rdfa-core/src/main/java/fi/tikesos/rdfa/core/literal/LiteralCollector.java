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
package fi.tikesos.rdfa.core.literal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import fi.tikesos.rdfa.core.datatype.Attributes;
import fi.tikesos.rdfa.core.datatype.Lexical;
import fi.tikesos.rdfa.core.datatype.Location;
import fi.tikesos.rdfa.core.datatype.PrefixMapping;
import fi.tikesos.rdfa.core.datatype.RDFaAttributes;
import fi.tikesos.rdfa.core.util.StringEscapeUtils;

/**
 * Helper class for collecting Literal and XMLLiteral values.
 * 
 * @author ssakorho
 * 
 */
public class LiteralCollector {
	private Stack<Lexical> literalCollector;
	private List<List<PrefixMapping>> inheritedNS;
	private Lexical xmlCollector;
	private boolean implicitClose;
	private int xmlCollectorDepth;

	/**
	 * Class constructor.
	 */
	public LiteralCollector() {
		literalCollector = new Stack<Lexical>();
		inheritedNS = new ArrayList<List<PrefixMapping>>();
		xmlCollector = null;
		xmlCollectorDepth = 0;
		implicitClose = false;
	}

	/**
	 * Start collecting literal
	 */
	public void startCollecting() {
		literalCollector.push(new Lexical());
	}

	/**
	 * Start collecting XML literal
	 */
	public void startCollectingXML() {
		xmlCollector = new Lexical();
	}

	/**
	 * Collect string to literal
	 * 
	 * @param toCollect
	 * @param shouldEncode
	 * @param location
	 */
	public void collect(String toCollect, boolean shouldEncode,
			Location location) {
		if (xmlCollector != null) {
			if (shouldEncode == true) {
				StringEscapeUtils
						.escapeXML(toCollect, xmlCollector.getBuffer());
			} else {
				xmlCollector.append(toCollect);
			}
		}
		if (literalCollector.isEmpty() == false) {
			literalCollector.peek().append(toCollect);
		}
		implicitClose = false;
	}

	/**
	 * Collect char array to literal
	 * 
	 * @param toCollect
	 * @param start
	 * @param length
	 * @param shouldEncode
	 * @param location
	 */
	public void collect(char[] toCollect, int start, int length,
			boolean shouldEncode, Location location) {
		if (xmlCollector != null) {
			if (shouldEncode == true) {
				StringEscapeUtils.escapeXML(toCollect, start, length,
						xmlCollector.getBuffer());
			} else {
				xmlCollector.append(toCollect, start, length);
			}
		}
		if (literalCollector.isEmpty() == false) {
			literalCollector.peek().append(toCollect, start, length);
		}
		implicitClose = false;
	}

	/**
	 * Check if literal collector is collecting XML literal
	 * 
	 * @return
	 */
	public boolean isCollectingXML() {
		return xmlCollector != null && xmlCollectorDepth > 0;
	}

	/**
	 * Check if literal collector is collecting literal
	 * 
	 * @return
	 */
	public boolean isCollecting() {
		return literalCollector.isEmpty() == false || xmlCollector != null;
	}

	/**
	 * Stop collecting literal
	 * 
	 * @return
	 */
	public Lexical stopCollecting() {
		Lexical collected = null;
		if (xmlCollector == null) {
			collected = literalCollector.pop();
			if (literalCollector.empty() == false) {
				collect(collected.getValue(), false, collected.getLocation());
			}
		} else {
			collected = xmlCollector;
			xmlCollector = null;
		}
		return collected;
	}

	/**
	 * Collect start element
	 * 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param rdfaAttributes
	 * @param location
	 * @return
	 */
	public boolean collectStartElement(String uri, String localName,
			String qName, RDFaAttributes rdfaAttributes, Location location) {
		boolean result = false;

		if (xmlCollector != null) {
			// <ELEMENT
			xmlCollector.append("<");
			xmlCollector.append(qName);

			Attributes attributes = rdfaAttributes.getAttributes();
			if (xmlCollectorDepth == 0) {
				// ATTRIBUTE="VALUE"
				Set<String> registeredPrefix = new HashSet<String>();
				for (int i = 0; i < attributes.getCount(); i++) {
					String attributeQName = attributes.getQName(i);
					xmlCollector.append(" ");
					xmlCollector.append(attributeQName);
					xmlCollector.append("=\"");
					StringEscapeUtils.escapeXML(attributes.getValue(i),
							xmlCollector.getBuffer());
					xmlCollector.append("\"");

					if (attributeQName.startsWith("xmlns") == true) {
						// Register namespace
						if (attributeQName.startsWith("xmlns")) {
							if (attributeQName.length() == 5) {
								// @xmlns
								registeredPrefix.add("");
							} else if (attributeQName.charAt(5) == ':') {
								// @xmlns:*
								registeredPrefix.add(attributeQName
										.substring(6));
							}
						}
					}
				}

				// Add inherited namespaces
				for (int n = inheritedNS.size(); n-- > 0;) {
					for (PrefixMapping pm : inheritedNS.get(n)) {
						if (registeredPrefix.contains(pm.getPrefix()) == false) {
							// XMLNS="URI"
							xmlCollector.append(" xmlns");
							if (pm.getPrefix().isEmpty() == false) {
								xmlCollector.append(":");
								xmlCollector.append(pm.getPrefix());
							}
							xmlCollector.append("=\"");
							StringEscapeUtils.escapeXML(pm.getURI(),
									xmlCollector.getBuffer());
							xmlCollector.append("\"");
							registeredPrefix.add(pm.getPrefix());
						}
					}
				}
			} else {
				for (int i = 0; i < attributes.getCount(); i++) {
					// ATTRIBUTE="VALUE"
					xmlCollector.append(" ");
					xmlCollector.append(attributes.getQName(i));
					xmlCollector.append("=\"");
					StringEscapeUtils.escapeXML(attributes.getValue(i),
							xmlCollector.getBuffer());
					xmlCollector.append("\"");
				}
			}
			// >
			xmlCollector.append(">");
			xmlCollectorDepth++;
			result = true;
		} else {
			// Save namespaces for XMLLiteral
			List<PrefixMapping> pm = new ArrayList<PrefixMapping>(
					1 + (rdfaAttributes.getXmlns() != null ? rdfaAttributes
							.getXmlns().size() : 0));
			if (rdfaAttributes.getDefaultXmlns() != null) {
				pm.add(new PrefixMapping("", rdfaAttributes.getDefaultXmlns()));
			}
			if (rdfaAttributes.getXmlns() != null) {
				pm.addAll(rdfaAttributes.getXmlns());
			}
			inheritedNS.add(pm);
		}
		implicitClose = true;

		return result;
	}

	/**
	 * Collect end element
	 * 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @return
	 */
	public boolean collectCloseElement(String uri, String localName,
			String qName) {
		boolean result = false;
		if (xmlCollector != null) {
			if (xmlCollectorDepth > 0) {
				if (implicitClose == true) {
					// Implicit close
					xmlCollector.setLength(xmlCollector.length() - 1);
					xmlCollector.append(" />");
				} else {
					// Explicit close
					xmlCollector.append("</");
					xmlCollector.append(qName);
					xmlCollector.append(">");
				}
				xmlCollectorDepth--;
				result = true;
			}
		} else {
			inheritedNS.remove(inheritedNS.size() - 1);
		}
		implicitClose = false;

		return result;
	}
}
