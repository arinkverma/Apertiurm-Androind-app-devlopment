/**
 *
 * @author Arink Verma
 */

package org.apertium.android.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigManager {
	
	private JSONObject jObject;
	private JSONObject pairObject;
	private JSONObject modesObject;
	private JSONArray modeitemArray;
	private Map<String,String> modeMap;
	
	private String version;
	private String package_id;
	
	public ConfigManager(String path) throws IOException, JSONException  {
		ZipFile zipFile;
		String strUnzipped = ""; 		


			zipFile = new ZipFile(path);
			
			ZipEntry entry1 = zipFile.getEntry("config.json");
			InputStream stream1;
			stream1 = zipFile.getInputStream(entry1);
			for (int c = stream1.read(); (int)c != -1; c = stream1.read()) {
			      strUnzipped += (char) c;
			}
			
			jObject = new JSONObject(strUnzipped);	
			pairObject = jObject.getJSONObject("pair");
			modesObject = pairObject.getJSONObject("modes");
			version	= pairObject.getString("version");
			package_id	= pairObject.getString("id");
			modeitemArray = modesObject.getJSONArray("modeitem");
	}
	
	
	public JSONArray getModeItems(){
		return modeitemArray;
	}
	
	public Map<String, String> getModeMap(){
		this.modeMap=new HashMap<String, String>();
		
		for (int i=0; i<modeitemArray.length(); i++) {
			try {
				modeMap.put(modeitemArray.getJSONObject(i).getString("id").toString(),modeitemArray.getJSONObject(i).getString("title").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		return modeMap;
	}
	
	public String Version(){
		return version;		
	}
	
	public String Package(){
		return package_id;		
	}
	
}
