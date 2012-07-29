/**
 *
 * @author Arink Verma
 */

package org.apertium.android.widget;

import org.apertium.android.helper.AppPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class WidgetHandler {
	private Context CTX;
	private final SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	public WidgetHandler(Context ctx,int widgetID){
		this.CTX = ctx;
		this.settings = CTX.getSharedPreferences(AppPreference.SharedPreference()+".Widget", 0);
		this.editor = settings.edit();
	}
	
	public void setWidgetModes(String []modes){
		for(int i=0;i<modes.length;i++){
		    editor.putString("Mode"+i, modes[i]);
		    editor.commit();
		}
	}
	
	public String[] getWidgetModes(){
		String []modes = new String[5];
		for(int i=0;i<modes.length;i++){
			String mode = settings.getString("Mode"+i, "+" );
			modes[i] = mode;
		}
		return modes;
	}
	
	public void setWidgetMode(String mode,int id){
		 editor.putString("Mode"+id, mode);
		 editor.commit();
	}
	
	public String getWidgetMode(int id){
		String mode = settings.getString("Mode"+id, "+" );
		return mode;
	}

}
