package fi.tikesos.rdfa.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Null EntityResolver implementation
 * 
 * @author ssakorho
 *
 */
public class NullEntityResolver implements EntityResolver {
	private static final byte []empty = { }; 
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		return new InputSource(new ByteArrayInputStream(empty));
	}
}