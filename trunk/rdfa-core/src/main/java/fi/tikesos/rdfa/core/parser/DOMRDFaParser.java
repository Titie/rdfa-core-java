package fi.tikesos.rdfa.core.parser;

import java.net.URISyntaxException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fi.tikesos.rdfa.core.datatype.Attributes;
import fi.tikesos.rdfa.core.datatype.DOMAttributes;
import fi.tikesos.rdfa.core.datatype.DOMLocation;
import fi.tikesos.rdfa.core.exception.ErrorHandler;
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

	private static void process(RDFaParser parser, Node node) {
		do {
			switch (node.getNodeType()) {
			case Node.ELEMENT_NODE:
				// Create DOMAttributes
				Attributes attributes = new DOMAttributes(node.getAttributes());
				// Start element
				parser.beginRDFaElement(node.getNamespaceURI(),
						node.getLocalName(), node.getNodeName(), attributes,
						new DOMLocation(node));
				// Recurse to child
				if (node.hasChildNodes() == true) {
					process(parser, node.getFirstChild());
				}
				// End element
				parser.endRDFaElement(node.getNamespaceURI(),
						node.getLocalName(), node.getNodeName());
				break;
			case Node.TEXT_NODE:
				// Text
				parser.writeLiteral(node.getNodeValue(), new DOMLocation(node));
				break;
			}
			// Iterate to next sibling
			node = node.getNextSibling();

		} while (node != null);
	}

	// public void characters(char[] ch, int start, int length)
	// throws SAXException {
	// // Capture text content for plain literal
	// writeLiteral(ch, start, length, new SAXLocation(line, column));
	// saveLocation();
	// }
	// public void ignorableWhitespace(char[] ch, int start, int length)
	// throws SAXException {
	// // Whitespace
	// characters(ch, start, length);
	// }
}
