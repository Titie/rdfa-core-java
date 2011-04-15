/**
 * Copyright (C) 2011 ssakorho <sami.s.korhonen@uef.fi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fi.tikesos.rdfa.core.literal;

import java.util.Stack;

import fi.tikesos.rdfa.core.datatype.Attributes;
import fi.tikesos.rdfa.core.datatype.Lexical;
import fi.tikesos.rdfa.core.datatype.Location;
import fi.tikesos.rdfa.core.util.StringEscapeUtils;

/**
 * Helper class for collecting Literal and XML-Literal values.
 * 
 * @author ssakorho
 * 
 */
public class LiteralCollector {
	private Stack<Lexical> literalCollector;
	private Lexical xmlCollector;
	private boolean implicitClose;
	private int xmlCollectorDepth;

	/**
	 * Class constructor.
	 */
	public LiteralCollector() {
		literalCollector = new Stack<Lexical>();
		xmlCollector = null;
		xmlCollectorDepth = 0;
		implicitClose = false;
	}

	/**
	 * 
	 */
	public void startCollecting() {
		literalCollector.push(new Lexical());
	}

	/**
	 * 
	 */
	public void startCollectingXML() {
		xmlCollector = new Lexical();
	}

	/**
	 * @param toCollect
	 * @param shouldEncode
	 * @param location
	 */
	public void collect(String toCollect, boolean shouldEncode,
			Location location) {
		if (xmlCollector != null) {
			if (shouldEncode == true) {
				StringEscapeUtils.escapeXML(toCollect, xmlCollector.getBuffer());
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
	 * @return
	 */
	public boolean isCollectingXML() {
		return xmlCollector != null && xmlCollectorDepth > 0;
	}

	/**
	 * @return
	 */
	public boolean isCollecting() {
		return literalCollector.isEmpty() == false || xmlCollector != null;
	}

	/**
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
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param atts
	 * @param location
	 * @return
	 */
	public boolean collectStartElement(String uri, String localName,
			String qName, Attributes atts, Location location) {
		boolean result = false;
		if (xmlCollector != null) {
			xmlCollector.append("<");
			xmlCollector.append(qName);
			for (int i = 0; i < atts.getCount(); i++) {
				xmlCollector.append(" ");
				xmlCollector.append(atts.getQName(i));
				xmlCollector.append("=\"");
				StringEscapeUtils.escapeXML(atts.getValue(i), xmlCollector.getBuffer());
				xmlCollector.append("\"");
			}
			xmlCollector.append(">");
			xmlCollectorDepth++;
			result = true;
		}
		implicitClose = true;

		return result;
	}

	/**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @return
	 */
	public boolean collectCloseElement(String uri, String localName,
			String qName) {
		boolean result = false;
		if (xmlCollector != null && xmlCollectorDepth > 0) {
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
		implicitClose = false;

		return result;
	}
}
