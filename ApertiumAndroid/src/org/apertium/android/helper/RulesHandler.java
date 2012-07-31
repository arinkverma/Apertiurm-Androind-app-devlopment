/**
 *
 * @author Arink Verma
 */


package org.apertium.android.helper;

import java.io.File;
import java.security.SecureClassLoader;

import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.languagepair.TranslationMode;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class RulesHandler extends SecureClassLoader{
	String  TAG = "RulesHandler";

	private File _tmpDIR;
	private Context CTX;
	private final String _CurrentMode = "currentmode";
	private final SharedPreferences settings;
	private SharedPreferences.Editor editor;

	public RulesHandler(Context ctx){
		this.CTX = ctx;
		this._tmpDIR = ctx.getDir("dex", 0);
		this.settings = CTX.getSharedPreferences(AppPreference.SharedPreference(), 0);
		this.editor = settings.edit();
	}


	/** Gets the current mode. Returns null if no mode */
	public String getCurrentMode(){
		String mode = settings.getString(_CurrentMode, null );
		return mode;
	}

	public void setCurrentMode(String mode){
	    editor.putString(_CurrentMode, mode);
	    editor.commit();
	}


	public void resetCurrentMode(){
	    editor.putString(_CurrentMode, "??-??");
	    editor.commit();
	}

	public boolean isSetCurrentMode(){
		String mode = settings.getString(_CurrentMode, "??-??" );
		if(mode.equals("??-??")){
			return false;
		}
		return true;
	}

	public String getCurrentPackage(){
		String mode = settings.getString(_CurrentMode, "??-??" );
		if(mode.equals("??-??")){
			return null;
		}
		DatabaseHandler DB = new DatabaseHandler(this.CTX);
		TranslationMode m = DB.getMode(mode);
		return m.getPackage();
	}

	public String findPackage(String M){
		DatabaseHandler DB = new DatabaseHandler(this.CTX);
		TranslationMode m = DB.getMode(M);
		return m.getPackage();
	}

	public String PathCurrentPackage(){
		return AppPreference.PackagePath(getCurrentPackage());
	}
	
	public String ExtractPathCurrentPackage(){
		return AppPreference.PackagePath(getCurrentPackage())+"/extract";
	}

	public DexClassLoader getClassLoader(){
		Log.d(TAG,"PathCurrentPackage ="+PathCurrentPackage()+"/"+getCurrentPackage()+".jar, ODEX path="+this._tmpDIR.getAbsolutePath());
		return new DexClassLoader(PathCurrentPackage()+"/"+getCurrentPackage()+".jar",this._tmpDIR.getAbsolutePath(), null, this.getClass().getClassLoader());
	}

}
