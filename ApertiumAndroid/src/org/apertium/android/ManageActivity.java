/**
 *
 * @author Arink Verma
 */
package org.apertium.android;

import org.apertium.android.filemanager.FileChooserActivity;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.widget.WidgetConfigActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
 
public class ManageActivity extends PreferenceActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.setting);
                
                
                /*List Package*/
				Preference listPref = (Preference) findPreference("listPref");
				listPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            		public boolean onPreferenceClick(Preference preference) {
            			Intent myIntent1 = new Intent(ManageActivity.this, ModeManageActivity.class);
            			ManageActivity.this.startActivity(myIntent1);
                        return true;
                    }
				});
				
				/*Install Package*/
                Preference installLocalPref = (Preference) findPreference("installLocalPref");
                installLocalPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            		public boolean onPreferenceClick(Preference preference) {
            			Intent myIntent1 = new Intent(ManageActivity.this, FileChooserActivity.class);
            			ManageActivity.this.startActivity(myIntent1);
                        return true;
                    }
                });
                
                Preference installSVNPref = (Preference) findPreference("installSVNPref");
                installSVNPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            		public boolean onPreferenceClick(Preference preference) {
            			Intent myIntent1 = new Intent(ManageActivity.this, DownloadActivity.class);
            			ManageActivity.this.startActivity(myIntent1);
            			//Toast.makeText(ManageActivity.this, "This is under construction",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
				
                
                ;	
          
                /*Cache Enable */
                Preference CachePref = (Preference) findPreference("CachePref");
                CachePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            		public boolean onPreferenceClick(Preference preference) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        boolean CachePreference = prefs.getBoolean("CachePref", AppPreference.isCacheEnabled());            
                        AppPreference.setCacheEnabled(CachePreference);  
        	            Log.i("CachePref",CachePreference+"");
            			return true;   
                    }
                });
                
                
                /*DisplayMark Enable */
                Preference MarkPref = (Preference) findPreference("MarkPref");
                MarkPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            		public boolean onPreferenceClick(Preference preference) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        boolean MarkPreference = prefs.getBoolean("MarkPref", AppPreference.isDisplayMarkEnabled());            
                        AppPreference.setDisplayMark(MarkPreference);  
        	            Log.i("MarkPref",MarkPreference+"");
            			return true;   
                    }
                });
                
                
      
                /*Widget */
                Preference WidgetPref = (Preference) findPreference("WidgetPref");
                WidgetPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            		public boolean onPreferenceClick(Preference preference) {
            			Intent myIntent1 = new Intent(ManageActivity.this, WidgetConfigActivity.class);
            			ManageActivity.this.startActivity(myIntent1);
                        return true;
                    }
                });
        }
        
        @Override
        protected void onStart(){
        	super.onStart();
        	getPrefs();        	
        }
        
        private void getPrefs() {
            // Get the xml/preferences.xml preferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean CachePreference = prefs.getBoolean("CachePref", AppPreference.isCacheEnabled());            
            AppPreference.setCacheEnabled(CachePreference);     
            
            boolean MarkPreference = prefs.getBoolean("MarkPref", AppPreference.isDisplayMarkEnabled());            
            AppPreference.setDisplayMark(MarkPreference);    
           
        }
}