/**
 *
 * @author Arink Verma
 */
package org.apertium.android;

import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileChooserActivity;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.widget.WidgetConfigActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
 
public class ManageActivity extends PreferenceActivity {
	
	 ProgressDialog progressDialog;
	AppPreference appPreference = null;
	Activity thisActivity = null;
        @SuppressWarnings("deprecation")
		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);
                
            appPreference = new AppPreference(this);
        	thisActivity = this;
            this.setTheme(R.style.PreferenceTheme);    
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
        			return true;
                }
            });
				
      
            /*Cache Enable */
            Preference CachePref = (Preference) findPreference(AppPreference.CachePref);
            CachePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        		public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    boolean CachePreference = prefs.getBoolean(AppPreference.CachePref, appPreference.isCacheEnabled());            
                    Log.i(AppPreference.CachePref,CachePreference+"");
        			return true;   
                }
            });
            
            
            /*Push Clip Enable */
            Preference ClipPushPref = (Preference) findPreference(AppPreference.ClipBoardPushPref);
            ClipPushPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        		public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    boolean ClipPushPreference = prefs.getBoolean(AppPreference.ClipBoardPushPref, appPreference.isClipBoardPushEnabled());            
                    Log.i(AppPreference.ClipBoardPushPref,ClipPushPreference+"");
        			return true;   
                }
            });
            
            /*Get Clip Enable */
            Preference ClipGetPref = (Preference) findPreference(AppPreference.ClipBoardGetPref);
            ClipGetPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        		public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    boolean ClipGetPreference = prefs.getBoolean(AppPreference.ClipBoardGetPref, appPreference.isClipBoardGetEnabled());            
                    Log.i(AppPreference.ClipBoardGetPref,ClipGetPreference+"");
        			return true;   
                }
            });
            
            /*DisplayMark Enable */
            Preference MarkPref = (Preference) findPreference(AppPreference.MarkPref);
            MarkPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        		public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    boolean MarkPreference = prefs.getBoolean(AppPreference.MarkPref, appPreference.isDisplayMarkEnabled());            
                    Log.i(AppPreference.MarkPref,MarkPreference+"");
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
            
            /*Update DB */
            Preference UpdateDBPref = (Preference) findPreference("UpdateDBPref");
            UpdateDBPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        		public boolean onPreferenceClick(Preference preference) {
        			  progressDialog = new ProgressDialog(thisActivity);
        			    progressDialog.setTitle(getString(R.string.updating_db));
        			    progressDialog.setMessage(getString(R.string.working));
        			    progressDialog.setCancelable(false);
        			    progressDialog.show();
        			 Thread t = new Thread() {
        				 @Override
        			     public void run() {
        					 DatabaseHandler DB = new DatabaseHandler(thisActivity);
			        		DB.updateDB();
			        		
			            	
			        		Message msg;
			                
			                
							msg = Message.obtain();
							msg.what = 1;
							handler.sendMessage(msg);
        			        }
        			        };
        			t.start();
        			
                    return true;
                }
            });
            
        }
        
        private Handler handler = new Handler(){
    	    @Override
    	    public void handleMessage(Message msg) {
    	        switch(msg.what){
    	        case 1:progressDialog.dismiss();
    	        break;
    	        
    	        }
    	    }
        };
    	        
        
}