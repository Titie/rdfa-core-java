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
import fi.tikesos.rdfa.core.datatype.Literal;
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
	private Stack<Literal> literal;
	private Stack<XMLLiteralWrapper> xmlLiteral;
	private List<List<PrefixMapping>> inheritedNS;
	private int depth;
	private boolean implicitClose;

	/**
	 * Class constructor.
	 */
	public LiteralCollector() {
		literal = new Stack<Literal>();
		xmlLiteral = new Stack<XMLLiteralWrapper>();
		inheritedNS = new ArrayList<List<PrefixMapping>>();
		implicitClose = false;
		depth = 0;
	}

	/**
	 * Start collecting literal
	 */
	public void startCollecting() {
		literal.push(new Literal());
	}

	/**
	 * Start collecting XML literal
	 */
	public void startCollectingXML() {
		xmlLiteral.push(new XMLLiteralWrapper(new Literal(), depth));
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
		if (xmlLiteral.isEmpty() == false) {
			if (shouldEncode == true) {
				StringEscapeUtils
						.escapeXML(toCollect, xmlLiteral.peek().getXMLLiteral().getBuffer());
			} else {
				xmlLiteral.peek().getXMLLiteral().append(toCollect);
			}
		}
		if (literal.isEmpty() == false) {
			literal.peek().append(toCollect);
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
		if (xmlLiteral.isEmpty() == false) {
			if (shouldEncode == true) {
				StringEscapeUtils.escapeXML(toCollect, start, length,
						xmlLiteral.peek().getXMLLiteral().getBuffer());
			} else {
				xmlLiteral.peek().getXMLLiteral().append(toCollect, start, length);
			}
		}
		if (literal.isEmpty() == false) {
			literal.peek().append(toCollect, start, length);
		}
		implicitClose = false;
	}


	/**
	 * Check if literal collector is collecting literal
	 * 
	 * @return
	 */
/*	public boolean isCollecting() {
		return literal.isEmpty() == false || xmlLiteral.isEmpty() == false;
	} */

	/**
	 * Stop collecting literal
	 * 
	 * @return
	 */
	public Literal stopCollecting() {
		Literal result;
		if (xmlLiteral.isEmpty() == true || xmlLiteral.peek().getDepth() != depth) {
			result = literal.pop();
			if (literal.isEmpty() == false) {
				// Copy to parent Literal
				literal.peek().append(result);
			}
		} else {
			result = xmlLiteral.pop().getXMLLiteral();
			if (xmlLiteral.isEmpty() == false) {
				// Copy to parent XMLLiteral
				xmlLiteral.peek().getXMLLiteral().append(result);
			}
		}
		return result;
	}

	/**
	 * Collect start element
	 * 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param rdfaAttributes
	 * @param location
	 */
	public void collectStartElement(String uri, String localName,
			String qName, RDFaAttributes rdfaAttributes, Location location) {
		if (xmlLiteral.isEmpty() == false) {
			// <ELEMENT
			XMLLiteralWrapper wrapper = xmlLiteral.peek();
			Literal literal = wrapper.getXMLLiteral();
			
			literal.append("<");
			literal.append(qName);

			Attributes attributes = rdfaAttributes.getAttributes();
			if (depth == wrapper.getDepth()) {
				// ATTRIBUTE="VALUE"
				Set<String> registeredPrefix = new HashSet<String>();
				for (int i = 0; i < attributes.getCount(); i++) {
					String attributeQName = attributes.getQName(i);
					literal.append(" ");
					literal.append(attributeQName);
					literal.append("=\"");
					StringEscapeUtils.escapeXML(attributes.getValue(i),
							literal.getBuffer());
					literal.append("\"");

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
							literal.append(" xmlns");
							if (pm.getPrefix().isEmpty() == false) {
								literal.append(":");
								literal.append(pm.getPrefix());
							}
							literal.append("=\"");
							StringEscapeUtils.escapeXML(pm.getURI(),
									literal.getBuffer());
							literal.append("\"");
							registeredPrefix.add(pm.getPrefix());
						}
					}
				}
			} else {
				for (int i = 0; i < attributes.getCount(); i++) {
					// ATTRIBUTE="VALUE"
					literal.append(" ");
					literal.append(attributes.getQName(i));
					literal.append("=\"");
					StringEscapeUtils.escapeXML(attributes.getValue(i),
							literal.getBuffer());
					literal.append("\"");
				}
			}
			// >
			literal.append(">");
		}
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
		implicitClose = true;
		depth++;
	}

	/**
	 * Collect end element
	 * 
	 * @param uri
	 * @param localName
	 * @param qName
	 */
	public void collectCloseElement(String uri, String localName,
			String qName) {
		if (xmlLiteral.isEmpty() == false) {
			XMLLiteralWrapper wrapper = xmlLiteral.peek();
			Literal literal = wrapper.getXMLLiteral();
			if (implicitClose == true) {
				// Implicit close
				literal.setLength(literal.length() - 1);
				literal.append(" />");
			} else {
				// Explicit close
				literal.append("</");
				literal.append(qName);
				literal.append(">");
			}
		}
		inheritedNS.remove(inheritedNS.size() - 1);
		implicitClose = false;
		depth--;
	}
}
