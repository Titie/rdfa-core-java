package fi.tikesos.rdfa.core.profile;

public interface ProfileLoader {
	public Profile loadProfile(String profileURI) throws Exception;
}
