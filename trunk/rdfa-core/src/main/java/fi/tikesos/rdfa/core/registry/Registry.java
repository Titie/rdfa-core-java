package fi.tikesos.rdfa.core.registry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ssakorho
 *
 */
public class Registry {
	Map<String, String> mappings;

	public Registry() {
		this.mappings = new HashMap<String, String>();
	}
	
	public Registry(Registry toCopy) {
		this.mappings = new HashMap<String, String>(mappings);
	}
	
	public String set(String Key, String Value) {
		return mappings.put(Key, Value);
	}
	
	public String get(String Key) {
		return mappings.get(Key);
	}
	
	public Map<String, String> getMappings() {
		return mappings;
	}
}
