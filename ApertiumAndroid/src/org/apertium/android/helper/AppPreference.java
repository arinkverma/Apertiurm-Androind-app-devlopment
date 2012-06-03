/**
 *
 * @author Arink Verma
 */

package org.apertium.android.helper;

import android.os.Environment;


public class AppPreference   {
	private static final String PREFS_NAME = "Apertium.Pref";
	private static final String base_dir = Environment.getExternalStorageDirectory().toString() +"/apertium";
	private static final String temp_dir  = Environment.getExternalStorageDirectory().toString() +"/apertium/temp";
	private static final String version = "2.0.1";
	private static final String _RulePackage = "org.apertium.android.rule";
	
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
}
