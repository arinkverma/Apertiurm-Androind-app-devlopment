/**
 *
 * @author Arink Verma
 */

package org.apertium.android.helper;

import java.util.ArrayList;
import java.util.List;

import org.apertium.Translator;
import org.apertium.android.languagepair.TranslationMode;


public class ConfigManager {
	
	private String modifiedDate;
	private String package_id;
	private String package_Title;
	private String []modesID;
	private List<TranslationMode> modes;
	
	public ConfigManager(String path,String packID) throws Exception {
			this.package_Title = Translator.getTitle(path);
			Translator.setBase(path);
			this.modesID = Translator.getAvailableModes();
			modes = new ArrayList<TranslationMode>();
			for(int i=0;i<this.modesID.length;i++){
				TranslationMode M = new TranslationMode(this.modesID[i], Translator.getTitle(this.modesID[i]));
				modes.add(M);
			}
			
			this.package_id =  packID;
	}
	
	public List<TranslationMode> getAvailableModes(){
		return this.modes;
	}
	
	public String PackageID(){
		return package_id;		
	}
	
	public void setModifiedDate(String D){
		this.modifiedDate = D;
	}
	
	public String ModifiedDate(){
		return this.modifiedDate;
	}
	
	public String PackageTitle(){
		return this.package_Title;
	}
	
}
