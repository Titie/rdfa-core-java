package fi.tikesos.rdfa.core.triple;

import fi.tikesos.rdfa.core.datatype.Component;
import fi.tikesos.rdfa.core.datatype.Language;
import fi.tikesos.rdfa.core.datatype.Lexical;

/**
 * TripleSink interface
 * 
 * @author ssakorho
 *
 */
public interface TripleSink {
	public void generateTriple(Component subject, Component predicate, Component object);
	public void generateTripleLiteral(Component subject, Component predicate, Lexical lexical, Language language, Component datatype);
}
