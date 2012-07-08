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
import org.apertium.android.helper.RulesHandler;
import org.apertium.android.languagepair.TranslationMode;

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
	            b.setMessage("Are you sure want to remove this package?");
	            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	packagetoRemove = pack;
	                    	
	                    	run0();			 
	                    	
	        	            if(tobeRemove.getPackage().equals(rulesHandler.getCurrentPackage())){
	        	            	rulesHandler.resetCurrentMode();	            	
	        	            }
	                    }
	            });
	            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
	
	private void run0(){		
		progressDialog = ProgressDialog.show(this, "Removing..", "Removing database entries", true,false);
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	try {
	        		File file = new File(AppPreference.BASE_DIR()+"/"+packagetoRemove);
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
	private void run1(){		
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
	        	progressDialog.setMessage("Removing files");
	        	run1();
	            break;
	        case 1:
	        	progressDialog.dismiss();
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
					Translator.setBase(rulesHandler.getClassLoader());
	    		}
				Translator.setCacheEnabled(AppPreference.isCacheEnabled());
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
