package in.arink.gsoc.DB;

public class Language {
		 
	    //private variables
	    private int _id;
	    private String _code12;
	    private String _lang12;
	    private String _code21;
	    private String _lang21;
	    private String _path;
	 
	    public Language(){
	    	
	    }
	    
	    // constructor
	    public Language(int id, String c12, String c21, String lang12,  String lang21,String path){
	        this._id = id;
	        this._code12 = c12;
	        this._lang12 = lang12;
	        this._code21 = c21;
	        this._lang21 = lang21;
	        this._path = path;
	    }
	 
	    // getting ID
	    public int getID(){
	        return this._id;
	    }
	 
	    // setting id
	    public void setID(int id){
	        this._id = id;
	    }
	    
	    public void setMode12(String c){
	        this._code12 = c;
	    }
	    
	    public void setMode21(String c){
	        this._code21 = c;
	    }
	 
	    public void setLang12(String c){
	        this._lang12 = c;
	    }
	    
	    public void setLang21(String c){
	        this._lang21 = c;
	    }
	    
	    public void setPath(String c){
	        this._path = c;
	    }
	 

	    public String getMode12(){
	        return this._code12;
	    }
	    
	    public String getMode21(){
	        return this._code21;
	    }
	 
	    public String getLang12(){
	        return this._lang12;
	    }
	    
	    public String getLang21(){
	        return this._lang21;
	    }
	    
	    public String getPath(){
	        return this._path;
	    }
	    
	    public String getPath(String p){
	        return this._path = p;
	    }

	}
