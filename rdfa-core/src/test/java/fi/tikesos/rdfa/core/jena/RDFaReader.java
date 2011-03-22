package fi.tikesos.rdfa.core.jena;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;
import com.hp.hpl.jena.rdf.model.RDFReader;

import fi.tikesos.rdfa.core.parser.RDFaParser;
import fi.tikesos.rdfa.core.triple.TripleSink;

/**
 * @author ssakorho
 *
 */
public class RDFaReader implements RDFReader {
	RDFErrorHandler errorHandler;

	@Override
	public void read(Model model, InputStream r, String base) {
		// Default method
		XMLReader reader;
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setFeature("http://xml.org/sax/features/validation", Boolean.FALSE);
			reader.setEntityResolver(new NullEntityResolver());
			
			try {
				TripleSink sink = new JenaTripleSink(model);
				ContentHandler parser = new RDFaParser(base, sink, null);
				reader.setContentHandler(parser);
				reader.parse(new InputSource(r));
			} catch (IOException e) {
				errorHandler.fatalError(e);
			}
		} catch (SAXException e) {
			errorHandler.fatalError(e);
		}
	}

	@Override
	public void read(Model model, Reader r, String base) {
		// Not implemented
		if (errorHandler != null) {
			errorHandler.fatalError(new NoSuchMethodException("Method has not been implemented"));
		}
	}
	
	@Override
	public void read(Model model, String url) {
		// Not implemented
		if (errorHandler != null) {
			errorHandler.fatalError(new NoSuchMethodException("Method has not been implemented"));
		}
	}

	@Override
	public Object setProperty(String propName, Object propValue) {
		// Set reader property
		return null;
	}

	@Override
	public RDFErrorHandler setErrorHandler(RDFErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		RDFErrorHandler oldErrorHandler = errorHandler;
		this.errorHandler = errorHandler;
		return oldErrorHandler;
	}
	
	/**
	 * Null EntityResolver implementation
	 * 
	 * @author ssakorho
	 *
	 */
	private class NullEntityResolver implements EntityResolver {
		@Override
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {
			byte []empty = { };
			return new InputSource(new ByteArrayInputStream(empty));
		}
	}
}
