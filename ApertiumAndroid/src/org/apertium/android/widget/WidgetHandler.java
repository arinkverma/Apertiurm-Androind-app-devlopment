/**
 *
 * @author Arink Verma
 */

package org.apertium.android.widget;

import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.languagepair.TranslationMode;

import android.content.Context;
import android.content.SharedPreferences;

public class WidgetHandler {
	private Context CTX;
	private final SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	public WidgetHandler(Context ctx,int widgetID){
		this.CTX = ctx;
		this.settings = CTX.getSharedPreferences(AppPreference.PREFERENCE_NAME, 0);
		this.editor = settings.edit();
	}
	
	
	public void setWidgetModes(String []modes){
		for(int i=0;i<modes.length;i++){
		    editor.putString("WidgetMode"+i, modes[i]);
		    editor.commit();
		}
	}
	
	public String[] getWidgetModes(){
		UdateWidget();
		
		String []modes = new String[5];
		for(int i=0;i<modes.length;i++){
			String mode = settings.getString("WidgetMode"+i, "+" );
			modes[i] = mode;
		}
		return modes;
	}
	
	public void setWidgetMode(String mode,int id){
		 editor.putString("WidgetMode"+id, mode);
		 editor.commit();
	}
	
	public String getWidgetMode(int id){
		String mode = settings.getString("WidgetMode"+id, "+" );
		return mode;
	}
	
	public void removeMode(String modeID){
		for(int i=0;i<5;i++){
			String mode = settings.getString("WidgetMode"+i, "+" );
			if(mode.equals(modeID)){
				setWidgetMode("+",i);
			}
		}
	}
	
	private void UdateWidget(){
		DatabaseHandler DB = new DatabaseHandler(this.CTX);
		for(int i=0;i<5;i++){
			String mode = settings.getString("WidgetMode"+i, "+" );
			TranslationMode translationMode = DB.getMode(mode);
			if(translationMode == null || translationMode != null &&  !translationMode.isValid()){
				setWidgetMode("+",i);
			}
		}
	}
	

}
