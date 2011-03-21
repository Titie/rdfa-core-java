package fi.tikesos.rdfa.core.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ssakorho
 *
 */
public class BlankNodeHandler {
	private long blankNodeId = 0;
	private Map<String, String> blankNodeMappings = new HashMap<String, String>();
	
	/**
	 * @return
	 */
	public String generateBlankNode() {
		blankNodeId++;
		return "_:BNsk" + blankNodeId;
	}

	/**
	 * @param nodeName
	 * @return
	 */
	public String mapBlankNode(String nodeName) {
		String id = blankNodeMappings.get(nodeName);
		if (id == null) {
			id = generateBlankNode();
			blankNodeMappings.put(nodeName, id);
		}
		return id;
	}
}
