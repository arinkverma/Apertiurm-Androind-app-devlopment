package org.apertium.android.languagepair;


public class TranslationMode {
    //private variables
    private String _id;
    private String _title;
    private String _package;
    private String _from;
    private String _to;
    
    public TranslationMode(String id,String title){  
    	this._id = id;
    	this._title = title;
    	this._package = null;
    	String []s = title.split("[^\\w]+");
    	this._from = s[0];
    	this._to = s[1];    	
    }
    
    public String getID(){
    	return this._id;
    }
    
    public String getTitle(){
    	return this._title;
    }
    
    public void setTo(String i){
    	this._to = i;
    }
    
    public String getTo(){
    	return this._to;
    }
    public void setFrom(String i){
    	this._from = i;
    }
    
    public String getFrom(){
    	return this._from;
    }
    
    public void setPackage(String i){
    	this._package = i;
    }
    
    public String getPackage(){
    	return this._package;
    }

}
