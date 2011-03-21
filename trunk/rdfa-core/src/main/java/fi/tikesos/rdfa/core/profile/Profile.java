package fi.tikesos.rdfa.core.profile;

import java.util.Map;

public interface Profile {
	public Map<String, String> getTermMappings();
	public Map<String, String> getPrefixMappings();
}
