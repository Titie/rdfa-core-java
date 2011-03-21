package fi.tikesos.rdfa.core.literal;

import java.util.Stack;

import org.xml.sax.Attributes;

import fi.tikesos.rdfa.core.util.StringEscapeUtils;



/**
 * Helper class for collecting Literal and XML-Literal values.
 * 
 * @author ssakorho
 *
 */
public class LiteralCollector {
	private Stack<StringBuffer> literalCollector;
	private StringBuffer xmlCollector;
	private boolean ascending;
	private int xmlCollectorDepth;
	
	/**
	 * Class constructor.
	 */
	public LiteralCollector() {
		literalCollector = new Stack<StringBuffer>();
		xmlCollector = null;
		xmlCollectorDepth = 0;
		ascending = false;
	}
	
	/**
	 * 
	 */
	public void startCollecting() {
		literalCollector.push(new StringBuffer());
	}
	
	/**
	 * 
	 */
	public void startCollectingXML() {
		xmlCollector = new StringBuffer();
	}
	
	/**
	 * @param toCollect
	 * @param shouldEncode
	 */
	public void collect(String toCollect, boolean shouldEncode) {
		if (xmlCollector != null) {
			if (shouldEncode == true) {
				StringEscapeUtils.escapeXML(toCollect, xmlCollector);
			} else {
				xmlCollector.append(toCollect);
			}
		}
		if (literalCollector.isEmpty() == false) {
			literalCollector.peek().append(toCollect);
		}
		ascending = false;
	}
	
	/**
	 * @param toCollect
	 * @param start
	 * @param length
	 * @param shouldEncode
	 */
	public void collect(char []toCollect, int start, int length, boolean shouldEncode) {
		if (xmlCollector != null) {
			if (shouldEncode == true) {
				StringEscapeUtils.escapeXML(toCollect, start, length, xmlCollector);
			} else {
				xmlCollector.append(toCollect, start, length);
			}
		}
		if (literalCollector.isEmpty() == false) {
			literalCollector.peek().append(toCollect, start, length);
		}
		ascending = false;
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
	public String stopCollecting() {
		String collected = null;
		if (xmlCollector == null) {
			collected = literalCollector.pop().toString();
			if (literalCollector.empty() == false) {
				collect(collected, false);
			}
		} else {
			collected = xmlCollector.toString();
			xmlCollector = null;
		}
		
		return collected;
	}
	
	/**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param atts
	 * @return
	 */
	public boolean collectStartElement(String uri, String localName, String qName, Attributes atts) {
		boolean result = false;
		if (xmlCollector != null) {
			xmlCollector.append("<");
			xmlCollector.append(qName);
			for (int i = 0;i < atts.getLength();i++) {
				xmlCollector.append(" ");
				xmlCollector.append(atts.getQName(i));
				xmlCollector.append("=\"");
				StringEscapeUtils.escapeXML(atts.getValue(i), xmlCollector); 
				xmlCollector.append("\"");
			}
			xmlCollector.append(">");
			xmlCollectorDepth++;
			result = true;
		}
		ascending = true;
		
		return result;
	}

	/**
	 * @param uri
	 * @param localName
	 * @param qName
	 * @return
	 */
	public boolean collectCloseElement(String uri, String localName, String qName) {
		boolean result = false;
		if (xmlCollector != null && xmlCollectorDepth > 0) {
			if (ascending == true) {
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
		ascending = false;
		
		return result;
	}
}
