# Example #

```
XMLReader reader = XMLReaderFactory.createXMLReader();
// Disable validation
reader.setFeature("http://xml.org/sax/features/validation",
			Boolean.FALSE);
// Request parser to provide namespace mappings in the Attributes list
reader.setFeature("http://xml.org/sax/features/namespace-prefixes",
		Boolean.TRUE);
// Set entity resolver
// NullEntityResolver disables DTD validation
reader.setEntityResolver(new NullEntityResolver());

// Create a ProfileHandler
// SimpleProfileHandler is a simple memory caching profile loader
ProfileHandler profileHandler = new SimpleProfileHandler();

// Create TripleSink
TripleSink sink = new MyTripleSink();

// Create ErrorHandler
// TripleErrorHandler generates errors as Triples
ErrorHandler errorHandler = new TripleErrorHandler(sink);

// Create content handler
ContentHandler parser = new SAXRDFaParser(base, sink,
		profileHandler, errorHandler, RDFaParser.UNKNOWN_XML);
reader.setContentHandler(parser);

// Parse the document, triples appear in TripleSink
reader.parse(new InputSource(inputStream));
```