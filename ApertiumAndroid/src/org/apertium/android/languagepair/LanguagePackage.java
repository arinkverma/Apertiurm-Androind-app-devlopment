package org.apertium.android.languagepair;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class LanguagePackage {		 
	//private variables
	private String _package_id;
	private List<TranslationMode> _modes;
	private String _version;
	
	public LanguagePackage(String Packageid){
		this._package_id = Packageid;
		this._modes=new ArrayList<TranslationMode>();	    	
	}	    
	    
/**ID*/
    public String getID(){
        return this._package_id;
    }
	 
/**Version*/
    public String getVersion(){
        return this._version;
    }
 
    public void setVersion(String V){
        this._version = V;
    }
	    
/* Is package version is latest than argument version */
    public boolean isNewerthan(String Version){
    	if(this._version == null){
    		return false;
    	}
    	if(Version == null){
    		return true;
    	}
    	
    	String[] ArgumentVersion = Version.replace('.', '-').split("-");
    	
    	String[] PackageVersion = this._version.replace('.', '-').split("-");

    	
    	int min = ArgumentVersion.length;
    	if(PackageVersion.length < min){
    		min = PackageVersion.length;
    	}
    	
    	for(int i=0; i<min; i++){
    		if(Integer.parseInt(ArgumentVersion[i]) > Integer.parseInt(PackageVersion[i])){
    			return false;
    		}
    	}
        return true;
    }
	    
/**Modes*/
	    public void setModes(JSONArray modeitems) throws JSONException{	 
	    	for (int i=0; i<modeitems.length(); i++) {
				String id = modeitems.getJSONObject(i).getString("id").toString();
				String title = modeitems.getJSONObject(i).getString("title").toString();
				TranslationMode m = new TranslationMode(id,title);
				m.setPackage(this._package_id);
				_modes.add(m);
			}
	    }
	    
	    public List<TranslationMode> getModes(){
	    	return this._modes;
	    }
	    
}
