package fi.tikesos.rdfa.core.jena;

import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import fi.tikesos.rdfa.core.datatype.LString;
import fi.tikesos.rdfa.core.triple.TripleSink;

/**
 * @author ssakorho
 *
 */
public class JenaTripleSink implements TripleSink {
	private Model model;
	private Map<String, Resource> nodeMap;
	
	/**
	 * @param model
	 */
	public JenaTripleSink(Model model) {
		this.model = model;
		this.nodeMap = new HashMap<String, Resource>();
	}

	/* (non-Javadoc)
	 * @see fi.tikesos.rdfa.TripleSink#generateTriple(fi.tikesos.rdfa.LString, fi.tikesos.rdfa.LString, fi.tikesos.rdfa.LString)
	 */
	@Override
	public void generateTriple(LString subject, LString predicate,
			LString object) {
		Resource s = createResource(subject.getValue());
		Property p = model.createProperty(predicate.getValue());
		Resource o = createResource(object.getValue());
		model.add(s, p, o);
//		System.out.println("<" + s + "> <" + p + "> <" + o + ">");
	}

	/* (non-Javadoc)
	 * @see fi.tikesos.rdfa.TripleSink#generateTripleLiteral(fi.tikesos.rdfa.LString, fi.tikesos.rdfa.LString, fi.tikesos.rdfa.LString, fi.tikesos.rdfa.LString, fi.tikesos.rdfa.LString)
	 */
	@Override
	public void generateTripleLiteral(LString subject, LString predicate,
			LString object, LString language, LString datatype) {
		Resource s = createResource(subject.getValue());
		Property p = model.createProperty(predicate.getValue());
		Literal o;
		if (datatype != null) {
			o = model.createTypedLiteral(object.getValue(), datatype.getValue());
		} else if (language != null) {
			o = model.createLiteral(object.getValue(), language.getValue());
		} else {
			o = model.createLiteral(object.getValue());
		}
		model.add(s, p, o);
//		System.out.println("<" + s + "> <" + p + "> <" + o + ">");
	}
	
	/**
	 * @param URI
	 * @return
	 */
	private Resource createResource(String URI) {
		Resource resource = null;
		if (URI.startsWith("_:") == true) {
			// Anonymous
			resource = nodeMap.get(URI);
			if (resource == null) {
				resource = model.createResource();
				nodeMap.put(URI, resource);
			}
		} else {
			// Named
			resource = model.createResource(URI);
		}
		return resource;
	}
}
