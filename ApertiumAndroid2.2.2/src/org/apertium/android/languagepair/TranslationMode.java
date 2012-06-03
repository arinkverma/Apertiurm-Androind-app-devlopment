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
    
    public void setID(String i){
    	this._id = i;
    }
    
    public void setTitle(String i){
    	this._title = i;
    }
    
    public void setPackage(String i){
    	this._package = i;
    }
    
    public String getID(){
    	return this._id;
    }
    
    public String getPackage(){
    	return this._package;
    }
    
    public String getTitle(){
    	return this._title;
    }

}
