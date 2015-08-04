# Description #

rdfa-core-java is a Java implementation of the RDFa Core 1.1 draft. Project is following specification strictly and passes all current unit tests.

rdfa-core-java includes XHTML+RDFa 1.1 host language support and supports both SAX and DOM parsing. Project is part of [a closed world assumption based message validation service](http://code.google.com/p/rdf-rule-validator/) created under Finnish government funded Tikesos-project.

Unlike other parsers, rdfa-core-java stores information about the locations of RDFa attributes in XML (Line and Column numbers in SAX, Nodes in DOM). This information is used by the validation service to provide user-friendly messages (such as cardinality failures, undefined data types, invalid data types, unexpected properties, etc.) about the document structure.

A Jena integration example is provided as a unit test.

# Details #

&lt;wiki:gadget url="http://www.ohloh.net/p/488976/widgets/project\_cocomo.xml" height="240" border="0"/&gt;

&lt;wiki:gadget url="http://www.ohloh.net/p/488976/widgets/project\_basic\_stats.xml" height="220" border="1"/&gt;