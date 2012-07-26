/**
 *
 * @author Arink Verma
 */

package org.apertium.android.helper;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;


public class AppPreference   {
	private static final String TAG = "AppPreference";
	
	//App properties
	private static final String version = "2.0.1";
	private static final String _RulePackage = "org.apertium.android.rule";

	//Directories path
	private static final String base_dir = Environment.getExternalStorageDirectory().toString() +"/apertium";
	private static final String jar_dir = Environment.getExternalStorageDirectory().toString() +"/apertium/jars";
	private static final String temp_dir  = Environment.getExternalStorageDirectory().toString() +"/apertium/temp";

	//Preferences name
	private static final String PREFS_NAME = "ore.apertium.Pref";
	public static final String CachePref = "CachePref";
	public static final String MarkPref = "MarkPref";
	public static final String ClipBoardGetPref = "ClipGetPref";
	public static final String ClipBoardPushPref = "ClipPushPref";
	public static final String CrashPref = "CrashPref";
	
	
	private Context context = null;
	
	private static String _SVNaddress = "https://apertium.svn.sourceforge.net/svnroot/apertium/branches/gsoc2012/arink/Mikeljar/";
	
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
	
	public static String JAR_DIR(){
		return jar_dir;
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
		return jar_dir+"/"+PackageID;
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
	
	
	public void ReportCrash(String y){
		 editor.putString(CrashPref, y);
		 editor.commit();
	}
	
	public String GetCrashReport(){
		return prefs.getString(CrashPref, null);
	}
	
	public void ClearCrashReport(){
		editor.putString(CrashPref, null);
		editor.commit();
	}
	
	
	
	//Last state
	public static final String LocalePref = "LocalePref";
	public static final String LastJARDirChangedPref = "LastJARDirChangedPref";
	
	
	public boolean isStateChanged(){
		String lastLocale = prefs.getString(LocalePref, "");
		String currentLocale = Locale.getDefault().getDisplayLanguage();
		
		Log.i(TAG,"lastLocale = "+lastLocale+", currentLocale = "+currentLocale);
		if(!lastLocale.equals(currentLocale)){
			return true;
		}
		
		File f = new File(jar_dir);
		
		String LastModified = f.lastModified()+"";
		String SavedLastModified = prefs.getString(LastJARDirChangedPref,"");
		
		Log.i(TAG,"LastModified = "+LastModified+", SavedLastModified = "+SavedLastModified);
		if(!LastModified.equals(SavedLastModified)){
			return true;
		}
		
		return false;
		
	}
	
	public void SaveState(){
		editor.putString(LocalePref, Locale.getDefault().getDisplayLanguage());
		
		File f = new File(jar_dir);
		editor.putString(LastJARDirChangedPref,f.lastModified()+"");
		
		Log.i(TAG,"lastLocale = "+Locale.getDefault().getDisplayLanguage()+", LastJARDirChanged = "+f.lastModified());
		editor.commit();
	
	}
}
