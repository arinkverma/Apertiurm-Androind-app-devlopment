/**
 *
 * @author Arink Verma
 */

package org.apertium.android.helper;

import android.os.Environment;


public class AppPreference   {
	private static final String PREFS_NAME = "ore.apertium.Pref";
	private static final String base_dir = Environment.getExternalStorageDirectory().toString() +"/apertium";
	private static final String temp_dir  = Environment.getExternalStorageDirectory().toString() +"/apertium/temp";
	private static final String version = "2.0.1";
	private static final String _RulePackage = "org.apertium.android.rule";
	private static boolean _isCacheEnabled = false;
	private static boolean _isMarkEnabled = false;
	private static boolean _isClipBoardGetEnabled = false;
	private static boolean _isClipBoardPushEnabled = false;
	
	private static String _SVNaddress = "https://apertium.svn.sourceforge.net/svnroot/apertium/branches/gsoc2012/artetxem/packages/jars/";//"https://apertium.svn.sourceforge.net/svnroot/apertium/branches/gsoc2012/arink/packages/";
	
	
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
	
	public static  String PackagePath(String PackageID){
		if(PackageID==null){
			return null;
		}
		return base_dir+"/"+PackageID+ _PackageExtention;
	}
	
	public static void setCacheEnabled(boolean y){
		_isCacheEnabled = y;
	}
	
	public static boolean isCacheEnabled(){
		return _isCacheEnabled;
	}
	
	public static void setDisplayMark(boolean y){
		_isMarkEnabled = y;
	}
	
	public static boolean isDisplayMarkEnabled(){
		return _isMarkEnabled;
	}
	
	public static void setClipBoardPush(boolean y){
		_isClipBoardPushEnabled = y;
	}
	
	public static boolean isClipBoardPushEnabled(){
		return _isClipBoardPushEnabled;
	}
	
	public static void setClipBoardGet(boolean y){
		_isClipBoardGetEnabled = y;
	}
	
	public static boolean isClipBoardGetEnabled(){
		return _isClipBoardGetEnabled;
	}
	
	
	public static String getSVN(){
		return _SVNaddress;
	}
}
