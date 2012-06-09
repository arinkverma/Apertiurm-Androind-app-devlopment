package org.apertium.android.languagepair;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;




public class LanguagePackage {

		 
	    //private variables
	    private String _package_id;
	    private List<TranslationMode> _modes;
	    private String _path;
	    private String _version;
	    
	    public LanguagePackage(String id){
	    	this._package_id = id;
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
	 
	    public void setVersion(String p){
	        this._version = p;
	    }
	    
	    
	    public boolean isNewerthan(String p){
	    	if(this._version == null){
	    		return false;
	    	}
	    	if(p == null){
	    		return true;
	    	}
	    	
	    	String[] N = p.replace('.', '-').split("-");
	    	
	    	String[] O = this._version.replace('.', '-').split("-");
	    	System.err.println(N.length+"||"+O.length);
	    	
	    	int min = N.length;
	    	if(O.length < min){
	    		min = O.length;
	    	}
	    	
	    	for(int i=0; i<min; i++){
	    		if(Integer.parseInt(N[i]) > Integer.parseInt(O[i])){
	    			return false;
	    		}
	    	}
	        return true;
	    }
	    
/**Path*/	    
	    public void setPath(String c){
	        this._path = c;
	    }
	 	    
	    public String getPath(){
	        return this._path;
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
