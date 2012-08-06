package org.apertium.android.Internet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apertium.Translator;
import org.apertium.android.helper.AppPreference;

public class InternetManifest {

	private List<ManifestRow> manifestRowList = null;
	
	public InternetManifest(String ManifestFile) throws IOException{
		manifestRowList = new ArrayList<ManifestRow>();
		InputStream is = new FileInputStream(AppPreference.TEMP_DIR+"/"+AppPreference.MANIFEST_FILE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String input="";
    	if (is!=null) {
    		while ((input = reader.readLine()) != null) {
    			/*PairName	JarURL FileName ModeID
    			 * apertium-af-nl	https://apertium.svn.sourceforge.net/svnroot/apertium/builds/apertium-af-nl/apertium-af-nl.jar	file:apertium-af-nl-0.2.0.tar.gz	af-nl, nl-af*/
    			String[] Row = input.split("\t");
    			if(Row.length>2){
    				manifestRowList.add(new ManifestRow(Row[0],Row[1],Row[2],Row[3]));
    			}
    		}
    	}
	}
	
	public int size(){
		return manifestRowList.size();
	}
	
	public ManifestRow get(int location){
		return manifestRowList.get(location);
	}
	
	public String[] PackageTitleList(){
		String[] list = new String[manifestRowList.size()];
		for(int i=0;i<manifestRowList.size();i++){
			list[i] = Translator.getTitle(manifestRowList.get(i).getpackageMode());
		}
		return list;
	}
	

}
