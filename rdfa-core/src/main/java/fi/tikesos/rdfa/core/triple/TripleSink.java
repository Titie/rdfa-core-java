package fi.tikesos.rdfa.core.triple;

import fi.tikesos.rdfa.core.datatype.LString;

public interface TripleSink {
	public void generateTriple(LString subject, LString predicate, LString object);
	public void generateTripleLiteral(LString subject, LString predicate, LString object, LString language, LString datatype);
}
