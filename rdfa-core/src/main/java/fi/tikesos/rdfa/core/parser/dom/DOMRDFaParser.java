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
package fi.tikesos.rdfa.core.parser.dom;

import java.net.URISyntaxException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fi.tikesos.rdfa.core.datatype.Attributes;
import fi.tikesos.rdfa.core.exception.ErrorHandler;
import fi.tikesos.rdfa.core.parser.RDFaParser;
import fi.tikesos.rdfa.core.profile.ProfileHandler;
import fi.tikesos.rdfa.core.triple.TripleSink;

/**
 * @author ssakorho
 * 
 */
public class DOMRDFaParser {
	/**
	 * Constructor
	 * 
	 * @param document
	 * @param base
	 * @param tripleSink
	 * @param profileHandler
	 * @param errorHandler
	 * @param format
	 * @throws URISyntaxException
	 */
	public static void parse(Document document, String base,
			TripleSink tripleSink, ProfileHandler profileHandler,
			ErrorHandler errorHandler, int format) throws URISyntaxException {
		// Create RDFaParser
		RDFaParser parser = new RDFaParser(base, tripleSink, profileHandler,
				errorHandler, format);
		if (document.getDocumentElement() != null) {
			// Parse children
			process(parser, document.getDocumentElement());
		}
	}

	/**
	 * Process selected node, children and siblings
	 * 
	 * @param parser
	 * @param node
	 */
	private static void process(RDFaParser parser, Node node) {
		do {
			switch (node.getNodeType()) {
			case Node.ELEMENT_NODE:
				// Create DOMAttributes
				Attributes attributes = new DOMAttributes(node.getAttributes());
				// Start element
				DOMLocation location = new DOMLocation(node);
				parser.beginRDFaElement(node.getNamespaceURI(),
						node.getLocalName(), node.getNodeName(), attributes,
						location);
				// Recurse to child
				if (node.hasChildNodes() == true) {
					process(parser, node.getFirstChild());
				}
				// End element
				parser.endRDFaElement(node.getNamespaceURI(),
						node.getLocalName(), node.getNodeName(), location);
				break;
			case Node.CDATA_SECTION_NODE:
			case Node.TEXT_NODE:
				// Text or CDATA
				parser.writeCharacters(node.getNodeValue(), new DOMLocation(node));
				break;
			}
			// Iterate to next sibling
		} while ((node = node.getNextSibling()) != null);
	}
}
