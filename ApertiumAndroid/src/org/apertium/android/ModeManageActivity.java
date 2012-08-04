/**
 *
 * @author Arink Verma
 */

package org.apertium.android;

import java.io.File;
import java.util.List;

import org.apertium.Translator;
import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileChooserActivity;
import org.apertium.android.filemanager.FileManager;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.languagepair.RulesHandler;
import org.apertium.android.languagepair.TranslationMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ModeManageActivity extends ListActivity {
	String TAG = "ModeManageActivity";
	private Activity thisActivity = null;

	private DatabaseHandler DB;

	//Rules Manager
	private RulesHandler rulesHandler;

	/* List of installed modes*/
	private List<TranslationMode> L;
	private static ProgressDialog progressDialog;
	private static String packagetoRemove;
	private String PrefToSet = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    thisActivity = this;
	    Intent intent = getIntent();
		Bundle extras = intent.getExtras();
	    if (extras != null) {
	    	PrefToSet = extras.getString("PrefToSet");
		}

		DB = new DatabaseHandler(this.getBaseContext());
		rulesHandler = new RulesHandler(this.getBaseContext());
	    L = DB.getAllModes();
	    int len = L.size();
	    final String[] ModeTitle = new String[len];
	    final String[] ModeId = new String[len];

	    for (int i = 0; i < L.size(); i++) {
	    	TranslationMode m = L.get(i);
	    	ModeTitle[i] = m.getTitle();
	        ModeId[i] 	= m.getID();
	    }

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, ModeTitle);
	
	    this.setListAdapter(adapter);

	    ListView lv = getListView();
	    lv.setTextFilterEnabled(true);


	    //Set current mode on click
	    lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				TextView v = (TextView) view;
				Toast.makeText(getApplicationContext(), v.getText(),   Toast.LENGTH_SHORT).show();
				UpdateMode(ModeId[position]);
			    finish();
			}
	    });

	    //Actions on Mode, on LongPress
	    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	        @Override
	        public boolean onItemLongClick(AdapterView<?> av, View v, int pos, final long id) {
	            final AlertDialog.Builder b = new AlertDialog.Builder(ModeManageActivity.this);
	            b.setIcon(android.R.drawable.ic_dialog_alert);

	            final TranslationMode tobeRemove = DB.getMode(ModeId[pos]);

	            final String pack = tobeRemove.getPackage();
	            b.setTitle(getString(R.string.confirm_packageRemove));
	            String message = "";
	            List<TranslationMode> removeModes =  DB.getModes(pack);
	            for(int i=0;i<removeModes.size();i++){
	            	message += ((TranslationMode) removeModes.get(i)).getTitle()+"\n";
	            }
	            b.setMessage(message);
	            b.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	packagetoRemove = pack;

	                    	FileRemoveRun();

	                    	String currentPackage = rulesHandler.getCurrentPackage();
	                    	
	                    	Log.i(TAG,"PacketToRemove = "+packagetoRemove+", CurrentPackage = "+currentPackage);
	        	            if(currentPackage!=null && packagetoRemove.equals(currentPackage)){
	        	            	rulesHandler.resetCurrentMode();
	        	            }
	 
	                    }
	            });
	            b.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                       // yesOrNo = 0;
	                    }
	            });

	            b.show();

	            return true;
	        }
	    });

	}

	public boolean onCreateOptionsMenu(Menu menu) {
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.manage_menu, menu);
	      return true;
	 }

	public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	      case R.id.install:
	    	  Intent myIntent = new Intent(this, FileChooserActivity.class);
	    	  ModeManageActivity.this.startActivity(myIntent);
	    	  return true;
	      default:
	            return super.onOptionsItemSelected(item);
	      }
	}


	/*Removing Package Entries*/

	private void FileRemoveRun(){
		progressDialog = ProgressDialog.show(this, getString(R.string.deleting)+"...", getString(R.string.removing_files), true,false);
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	try {
	        		Log.i(TAG,"removing file="+AppPreference.JAR_DIR+"/"+packagetoRemove);
	        		File file = new File(AppPreference.JAR_DIR+"/"+packagetoRemove);
	        		FileManager.remove(file);
	        	} catch (Exception e) {
					e.printStackTrace();
				}
	            Message msg = Message.obtain();
	            msg.what = 0;
	            handler.sendMessage(msg);
	        }
	    };
	    t.start();
	}

	/*Removing Package files*/
	private void DB_EntriesRemoveRun(){
		Thread t = new Thread() {
	        @Override
	        public void run() {
	        	try {
	        		DB.deletePackage(packagetoRemove);
	        	} catch (Exception e) {
					e.printStackTrace();
				}
	            Message msg = Message.obtain();
	            msg.what = 1;
	            handler.sendMessage(msg);
	        }
	    };
	    t.start();
	}

	private Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        switch(msg.what){
	        case 0:
	        	progressDialog.setMessage(getString(R.string.removing_db));
	        	DB_EntriesRemoveRun();
	            break;
	        case 1:
	        	progressDialog.dismiss();
	        	AppPreference appPreference = new AppPreference(thisActivity);
	        	appPreference.SaveState();
	        	
	        	Intent myIntent1 = new Intent(ModeManageActivity.this,ModeManageActivity.class);
	        	ModeManageActivity.this.startActivity(myIntent1);
	        	finish();
	            break;
	        }
	    }
	};



	private void UpdateMode(String MODE){
		if(PrefToSet !=null ){
			Intent intent = getIntent();
		    intent.putExtra("Mode", MODE);
		    setResult(RESULT_OK, intent);
			Log.i("CurrentMode", MODE);
		}else{
	    	try {
	    		String currentPackage = rulesHandler.getCurrentPackage();
	    		String PackageTOLoad = rulesHandler.findPackage(MODE);
	    		Log.i(TAG,"CurrentPackage ="+currentPackage+", PackageToLoad="+PackageTOLoad+", ModeToset="+MODE);

    			rulesHandler.setCurrentMode(MODE);
	    		if(!PackageTOLoad.equals(currentPackage)){
	    			Log.i(TAG,"BASE ="+rulesHandler.getClassLoader()+"path = "+rulesHandler.ExtractPathCurrentPackage());
	        		
	    			Translator.setBase(rulesHandler.ExtractPathCurrentPackage(), rulesHandler.getClassLoader());
	  
	          		Translator.setDelayedNodeLoadingEnabled(true);
	        		Translator.setMemmappingEnabled(true);
	        		Translator.setPipingEnabled(false);
	    		}
    			
        		
				Translator.setMode(MODE);
				Log.e("CurrentMode",rulesHandler.getCurrentMode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	@Override
    public void onBackPressed(){
		Intent intent = getIntent();
	    intent.putExtra("Mode", "+");
	    setResult(RESULT_CANCELED, intent);
	    finish();
    }
}
