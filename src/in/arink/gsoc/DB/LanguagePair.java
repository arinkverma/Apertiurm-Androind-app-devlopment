package in.arink.gsoc.DB;

public class LanguagePair {
	static private Class<?> lang; 
	public LanguagePair(Class<?> L){
		lang = (Class<?>) L;		
	}
	
	public Class<?> get(){
		return lang;
	}

}
