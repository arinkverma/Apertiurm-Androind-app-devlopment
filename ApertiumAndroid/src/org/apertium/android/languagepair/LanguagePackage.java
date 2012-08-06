package org.apertium.android.languagepair;

import java.util.ArrayList;
import java.util.List;

import org.apertium.Translator;

public class LanguagePackage {		 
	//private variables
	private String _package_id;
	private List<TranslationMode> _modes;
	private String _lastDate;
	private String modifiedDate;
	private String []modesID;
	
	
	public LanguagePackage(String path,String packID) throws Exception {
		Translator.setBase(path);
		this.modesID = Translator.getAvailableModes();
		this._modes = new ArrayList<TranslationMode>();
		for(int i=0;i<this.modesID.length;i++){
			TranslationMode M = new TranslationMode(this.modesID[i], Translator.getTitle(this.modesID[i]));
			this._modes.add(M);
		}
		
		this._package_id =  packID;
	}
	
	
	public LanguagePackage(String Packageid){
		this._package_id = Packageid;
		this._modes=new ArrayList<TranslationMode>();	    	
	}	 
	
 
/**Version*/
    public String getLastDate(){
        return this._lastDate;
    }
 
    public void setLastDate(String V){
        this._lastDate = V;
    }
	    
/**Modes*/
	public void setModes(List<TranslationMode> modeitems){	 
	    	for (int i=0; i<modeitems.size(); i++) {
				TranslationMode m =  modeitems.get(i);
				m.setPackage(this._package_id);
				_modes.add(m);
			}
	 }
	    
	public List<TranslationMode> getAvailableModes(){
		return this._modes;
	}
	
	public String PackageID(){
		return _package_id;		
	}
	
	public void setModifiedDate(String D){
		this.modifiedDate = D;
	}
	
	public String ModifiedDate(){
		return this.modifiedDate;
	}
	
	    
}
