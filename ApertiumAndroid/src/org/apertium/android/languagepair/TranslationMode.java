package org.apertium.android.languagepair;

public class TranslationMode {
    //private variables
    private String _id;
    private String _title;
    private String _package;
    
    public TranslationMode(String id,String title){  
    	this._id = id;
    	this._title = title;
    	this._package = null;
    }
    
    public String getID(){
    	return this._id;
    }
    
    public String getTitle(){
    	return this._title;
    }
    
    public void setPackage(String i){
    	this._package = i;
    }
    
    public String getPackage(){
    	return this._package;
    }

}
