# DOMLocation and SAXLocation #

The main difference between the DOM and the SAX-parser is that the DOM-parser returns the node bound to the RDFa-attribute while the SAX-parser returns an estimate of the line and the column number.

# DOMLocation #

Returns the node bound to the RDFa-attribute. If returned by a literal not defined in a content-attribute, location is the parent node for the literal content.

# SAXLocation #

Returns an estimate of the line and the colum number of the element RDFa-attiribute was defined in. If returned by a literal not defined in content-attribute, location is the end of the current element.