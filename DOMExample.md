# Example #

```
// Create DOM builder
DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
		.newInstance();
// Disable validation
documentBuilderFactory.setValidating(false);
// Request parser to resolve element namespaces
documentBuilderFactory.setNamespaceAware(true);
// Request parser not to ingore whitespaces
documentBuilderFactory.setIgnoringElementContentWhitespace(false);

DocumentBuilder documentBuilder = documentBuilderFactory
		.newDocumentBuilder();
// Set entity resolver
// NullEntityResolver disables DTD validation
documentBuilder.setEntityResolver(new NullEntityResolver());

// Create DOM document
Document document = documentBuilder.parse(inputStream);

// Create a ProfileHandler
// SimpleProfileHandler is a simple memory caching profile loader
ProfileHandler profileHandler = new SimpleProfileHandler();

// Create TripleSink
TripleSink sink = new MyTripleSink();

// Create ErrorHandler
// TripleErrorHandler generates errors as Triples
ErrorHandler errorHandler = new TripleErrorHandler(sink);

// Parse document
DOMRDFaParser.parse(document, base, sink, profileHandler,
		errorHandler, RDFaParser.UNKNOWN_XML);
```