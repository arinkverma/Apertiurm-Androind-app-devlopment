package org.apertium.android.Internet;

public class ManifestRow{
	/*PairName	JarURL FileName ModeID
	 * apertium-af-nl	https://apertium.svn.sourceforge.net/svnroot/apertium/builds/apertium-af-nl/apertium-af-nl.jar	file:apertium-af-nl-0.2.0.tar.gz	af-nl, nl-af*/
	
	private String packageName = null;
	private String JarURL = null;
	private String ZipURL = null;
	private String packageMode = null;
	
	public ManifestRow(String Name,String JAR,String ZIP,String Code){
		packageName = Name;
		JarURL		= JAR;
		ZipURL		= ZIP;
		packageMode	= Code;
	}
	
	public String getName(){
		return packageName;
	}
	
	public String getJarURL(){
		return JarURL;
	}
	
	public String getJarFileName(){
		return JarURL.substring(JarURL.lastIndexOf("/")+1,JarURL.length());
	}
	
	public String getZipURL(){
		return ZipURL;
	}
	
	public String getpackageMode(){
		return packageMode;
	}
}

