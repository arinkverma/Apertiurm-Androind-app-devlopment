package org.apertium.android.languagepair;

import java.io.File;

import org.apertium.android.helper.AppPreference;

import android.content.Context;
import android.util.Log;
import dalvik.system.DexClassLoader;



public class TranslationRules{
	
	private String _mode;
	private String _path;
	private String _rulepackage;
	private File _tmpDIR;
	private Context CTX;
	
	public TranslationRules(Context ctx){
		this.CTX = ctx;
		this._rulepackage = null;
		this._mode = null;
		this._tmpDIR = ctx.getDir("dex", 0);
		this._rulepackage = AppPreference.RulePackage();
		this._mode = AppPreference.CurrentMode(ctx);
		this._path = AppPreference.PathCurrentPackage(ctx);
	}
	
	
	
	public void setPackage(String p){
		this._rulepackage = p;
	}
	
	/*public void setPath(String p){
		this._path = p;
	}*/
	
	public void setTempDir(File f){
		this._tmpDIR = f;
	}
	
	public void setMode(String p){
		this._mode = p;
		this._path = AppPreference.PathCurrentPackage(this.CTX);
	}
	
	
	public Class<?> get(String r1) throws ClassNotFoundException{
	    final String libPath = AppPreference.BASE_DIR() + "/"+_path+"/rules.jar";
	    
	    String classpath = _rulepackage+"."+_mode.replace("-", "_");
	    final DexClassLoader classloader = new DexClassLoader(libPath,this._tmpDIR.getAbsolutePath(), null, this.getClass().getClassLoader());
	     
	    String r2 = classpath+"."+_mode.replace("-", "_")+"_"+r1;
    	Class<?> c = classloader.loadClass(r2);

		return c;
		
	}
	
	public DexClassLoader getClassLoader(){
		Log.e("path",this._path);
		return new DexClassLoader(this._path,this._tmpDIR.getAbsolutePath(), null, this.getClass().getClassLoader());
		
	}
	
}
