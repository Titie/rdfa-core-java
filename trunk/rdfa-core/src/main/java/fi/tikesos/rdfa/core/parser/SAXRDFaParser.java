package fi.tikesos.rdfa.core.parser;

import java.net.URISyntaxException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import fi.tikesos.rdfa.core.exception.ErrorHandler;
import fi.tikesos.rdfa.core.profile.ProfileHandler;
import fi.tikesos.rdfa.core.triple.TripleSink;

/**
 * @author ssakorho
 * 
 */
public class SAXRDFaParser extends RDFaParser implements ContentHandler {
	private Locator locator;
	private long line;
	private long column;

	/**
	 * Constructor
	 * 
	 * @param base
	 * @param tripleSink
	 * @param profileHandler
	 * @param errorHandler
	 * @param format
	 * @throws URISyntaxException
	 */
	public SAXRDFaParser(String base, TripleSink tripleSink,
			ProfileHandler profileHandler, ErrorHandler errorHandler, int format)
			throws URISyntaxException {
		super(base, tripleSink, profileHandler, errorHandler, format);
		line = 0;
		column = 0;
	}

	/**
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	@Override
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	/**
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		// Start document
		saveLocation();
	}

	/**
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		// End document
		saveLocation();
	}

	/**
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// Start prefix mapping
	}

	/**
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// End prefix-mapping
	}

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		beginRDFaElement(uri, localName, qName, atts, line, column,
				locator.getLineNumber(), locator.getColumnNumber());

		saveLocation();
	}

	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		endRDFaElement(uri, localName, qName);
		saveLocation();
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// Capture text content for plain literal
		writeLiteral(ch, start, length, line, column);
		saveLocation();
	}

	/**
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// Whitespace
		characters(ch, start, length);
	}

	/**
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// Processing instruction
		saveLocation();
	}

	/**
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	@Override
	public void skippedEntity(String name) throws SAXException {
		// Skipped entity
		saveLocation();
	}

	/**
	 * Save current location
	 */
	public void saveLocation() {
		// Save current location
		line = locator.getLineNumber();
		column = locator.getColumnNumber();
	}
}
