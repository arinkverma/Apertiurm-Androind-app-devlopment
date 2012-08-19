/*
 * Copyright (C) 2012 Arink Verma
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */

package org.apertium.android.widget;

import org.apertium.android.database.DatabaseHandler;
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
