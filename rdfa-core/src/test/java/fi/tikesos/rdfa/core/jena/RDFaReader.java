package fi.tikesos.rdfa.core.jena;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;
import com.hp.hpl.jena.rdf.model.RDFReader;

import fi.tikesos.rdfa.core.exception.ErrorHandler;
import fi.tikesos.rdfa.core.exception.TripleErrorHandler;
import fi.tikesos.rdfa.core.parser.RDFaParser;
import fi.tikesos.rdfa.core.profile.ProfileHandler;
import fi.tikesos.rdfa.core.profile.SimpleProfileHandler;
import fi.tikesos.rdfa.core.triple.TripleSink;
import fi.tikesos.rdfa.core.util.NullEntityResolver;

/**
 * @author ssakorho
 * 
 */
public class RDFaReader implements RDFReader {
	private static ProfileHandler profileHandler = null;
	private RDFErrorHandler errorHandler = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.hpl.jena.rdf.model.RDFReader#read(com.hp.hpl.jena.rdf.model.Model,
	 * java.io.InputStream, java.lang.String)
	 */
	@Override
	public void read(Model model, InputStream r, String base) {
		// Default method
		XMLReader reader;
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setFeature("http://xml.org/sax/features/validation",
					Boolean.FALSE);
			reader.setEntityResolver(new NullEntityResolver());

			if (profileHandler == null) {
				profileHandler = new SimpleProfileHandler();
			}

			try {
				TripleSink sink = new JenaTripleSink(model);
				ErrorHandler errorHandler = new TripleErrorHandler(sink);
				ContentHandler parser = new RDFaParser(base, sink,
						profileHandler, errorHandler, RDFaParser.XHTML_RDFA);
				reader.setContentHandler(parser);
				reader.parse(new InputSource(r));
			} catch (IOException e) {
				errorHandler.fatalError(e);
			} catch (URISyntaxException e) {
				errorHandler.fatalError(e);
			}
		} catch (SAXException e) {
			errorHandler.fatalError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.hpl.jena.rdf.model.RDFReader#read(com.hp.hpl.jena.rdf.model.Model,
	 * java.io.Reader, java.lang.String)
	 */
	public void read(Model model, Reader r, String base) {
		// Not implemented
		if (errorHandler != null) {
			errorHandler.fatalError(new NoSuchMethodException(
					"Method has not been implemented"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.hpl.jena.rdf.model.RDFReader#read(com.hp.hpl.jena.rdf.model.Model,
	 * java.lang.String)
	 */
	public void read(Model model, String url) {
		// Not implemented
		if (errorHandler != null) {
			errorHandler.fatalError(new NoSuchMethodException(
					"Method has not been implemented"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.hpl.jena.rdf.model.RDFReader#setProperty(java.lang.String,
	 * java.lang.Object)
	 */
	public Object setProperty(String propName, Object propValue) {
		// Set reader property
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.hpl.jena.rdf.model.RDFReader#setErrorHandler(com.hp.hpl.jena.rdf
	 * .model.RDFErrorHandler)
	 */
	public RDFErrorHandler setErrorHandler(RDFErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		RDFErrorHandler oldErrorHandler = errorHandler;
		this.errorHandler = errorHandler;
		return oldErrorHandler;
	}
}
