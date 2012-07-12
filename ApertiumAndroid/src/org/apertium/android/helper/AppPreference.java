/**
 *
 * @author Arink Verma
 */

package org.apertium.android.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;


public class AppPreference   {
	private static final String PREFS_NAME = "ore.apertium.Pref";
	private static final String base_dir = Environment.getExternalStorageDirectory().toString() +"/apertium";
	private static final String temp_dir  = Environment.getExternalStorageDirectory().toString() +"/apertium/temp";
	private static final String version = "2.0.1";
	private static final String _RulePackage = "org.apertium.android.rule";
	public static final String CachePref = "CachePref";
	public static final String MarkPref = "MarkPref";
	public static final String ClipBoardGetPref = "ClipGetPref";
	public static final String ClipBoardPushPref = "ClipPushPref";
	
	private Context context = null;
	
	private static String _SVNaddress = "https://apertium.svn.sourceforge.net/svnroot/apertium/branches/gsoc2012/arink/Mikeljar/";
	
//Package file name = <Base_Dir><Package Name><PackageExtention>
	private static final String _PackageExtention = ".jar";
	private SharedPreferences prefs = null;
	private SharedPreferences.Editor editor;
	
	public AppPreference(Context ctx){
		this.context = ctx;
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
		this.editor = prefs.edit();
	}
	
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
	
	public static  String PackagePath(String PackageID){
		if(PackageID==null){
			return null;
		}
		return base_dir+"/"+PackageID;//+ _PackageExtention;
	}
	
	
	public void setCacheEnabled(boolean y){
		 editor.putBoolean(CachePref, y);
		 editor.commit();
	}
	
	public boolean isCacheEnabled(){
        return prefs.getBoolean(CachePref, false);
	}
	
	public void setDisplayMark(boolean y){
		 editor.putBoolean(MarkPref, y);
		 editor.commit();
	}
	
	public boolean isDisplayMarkEnabled(){
		return prefs.getBoolean(MarkPref, false);
	}
	
	public void setClipBoardPush(boolean y){
		 editor.putBoolean(ClipBoardPushPref, y);
		 editor.commit();
	}
	
	public boolean isClipBoardPushEnabled(){
		return prefs.getBoolean(ClipBoardPushPref, false);
	}
	
	public void setClipBoardGet(boolean y){
		 editor.putBoolean(ClipBoardGetPref, y);
		 editor.commit();
	}
	
	public boolean isClipBoardGetEnabled(){
		return prefs.getBoolean(ClipBoardGetPref, false);
	}
	
	
	public static String getSVN(){
		return _SVNaddress;
	}
}
