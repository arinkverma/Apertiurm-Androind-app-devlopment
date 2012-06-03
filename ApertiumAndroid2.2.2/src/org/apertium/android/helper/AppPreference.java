/**
 *
 * @author Arink Verma
 */

package org.apertium.android.helper;

import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.languagepair.TranslationMode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;


public class AppPreference   {
	private static final String PREFS_NAME = "Apertium.Pref";
	private static final String base_dir = Environment.getExternalStorageDirectory().toString() +"/apertium";
	private static final String temp_dir  = Environment.getExternalStorageDirectory().toString() +"/apertium/temp";
	private static final String version = "2.0.1";
	private static final String _RulePackage = "org.apertium.android.rule";
	private static final String _CurrentMode = "currentmode";
	
//Package file name = <Base_Dir><Package Name><PackageExtention>
	private static final String _PackageExtention = ".zip";
		
	public static String BASE_DIR(){
		return base_dir;
	}
	
	public static String TEMP_DIR(){
		return temp_dir;
	}
	
	public String VERSION(){
		return version;
	}
	
	public static String SharedPreference(){
		return PREFS_NAME;
	}
	
	public static String RulePackage(){
		return _RulePackage;
	}
	
	public static String CurrentMode(Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		String mode = settings.getString(_CurrentMode, "??-??" );
		return mode;
	}
	
	public static  void setCurrentMode(Context ctx,String mode){
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
	    editor.putString(_CurrentMode, mode);
	    // Commit the edits!
	    editor.commit();
	}
	
	public static  void resetCurrentMode(Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
	    editor.putString(_CurrentMode, "??-??");
	    // Commit the edits!
	    editor.commit();
	}
	
	public static boolean isSetCurrentMode(Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		String mode = settings.getString(_CurrentMode, "??-??" );
		
		if(mode.equals("??-??")){
			return false;
		}		
		return true;		
	}
	
	public static  String CurrentPackage(Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		String mode = settings.getString(_CurrentMode, "??-??" );
		if(mode.equals("??-??")){
			return null;
		}
		DatabaseHandler DB = new DatabaseHandler(ctx);
		TranslationMode m = DB.getMode(mode);
		return m.getPackage();
	}
	
	public static  String PathCurrentPackage(Context ctx){
		if(CurrentPackage(ctx)==null){
			return null;
		}
		return base_dir+"/"+CurrentPackage(ctx)+ _PackageExtention;
	}
}
