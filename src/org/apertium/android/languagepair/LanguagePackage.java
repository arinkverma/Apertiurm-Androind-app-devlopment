package org.apertium.android.languagepair;

import java.util.ArrayList;
import java.util.List;

import org.apertium.android.helper.ConfigManager;

public class LanguagePackage {		 
	//private variables
	private String _package_id;
	private String _package_title;
	private List<TranslationMode> _modes;
	private String _lastDate;
	
	public LanguagePackage(String Packageid){
		this._package_id = Packageid;
		this._modes=new ArrayList<TranslationMode>();	    	
	}	 
	
	public LanguagePackage(ConfigManager config){
		this._lastDate = config.ModifiedDate();
		this._modes = config.getAvailableModes();
		this._package_id = config.PackageID();
		this._package_title = config.PackageTitle();		
	}
	
	
	    
/**ID*/
    public String getID(){
        return this._package_id;
    }
	 
/**Version*/
    public String getLastDate(){
        return this._lastDate;
    }
 
    public void setLastDate(String V){
        this._lastDate = V;
    }
	    
/* Is package version is latest than argument version 
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
    }*/
	    
/**Modes*/
	    public void setModes(List<TranslationMode> modeitems){	 
	    	for (int i=0; i<modeitems.size(); i++) {
				TranslationMode m =  modeitems.get(i);
				m.setPackage(this._package_id);
				_modes.add(m);
			}
	    }
	    
	    public List<TranslationMode> getModes(){
	    	return this._modes;
	    }
	    
}
