/**
 *
 * @author Arink Verma
 */

package org.apertium.android;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileChooserActivity;
import org.apertium.android.filemanager.FileManager;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.languagepair.TranslationMode;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private DatabaseHandler DB;
	
	/* List of installed modes*/
	private List<TranslationMode> L;
	private static ProgressDialog progressDialog;
	private static String packagetoRemove;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		DB = new DatabaseHandler(this.getBaseContext());
	    L = DB.getAllModes();
	    int len = L.size();
	    final String[] ModeTitle = new String[len];
	    final String[] ModeId = new String[len];
	   
	    for (int i = 0; i < L.size(); i++) {
	    	TranslationMode m = L.get(i);
	    	ModeTitle[i] = m.getTitle();
	        ModeId[i] 	= m.getID();
	    }
	    
	    Arrays.sort(ModeTitle);
	    
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
				AppPreference.setCurrentMode(getApplicationContext(),ModeId[position]);
			    finish();
			}
	    });
	    
	    //Actions on Mode, on LongPress
	    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	        @Override
	        public boolean onItemLongClick(AdapterView<?> av, View v, int pos, final long id) {
	            final AlertDialog.Builder b = new AlertDialog.Builder(ModeManageActivity.this);
	            b.setIcon(android.R.drawable.ic_dialog_alert);
	           
	            TranslationMode tobeRemove = DB.getMode(ModeId[pos]);
	           
	            
	            if(tobeRemove.getPackage().equals(AppPreference.CurrentPackage(getApplicationContext()))){
	            	AppPreference.resetCurrentMode(getApplicationContext());	            	
	            }
	            
	            final String pack = tobeRemove.getPackage();
	            b.setMessage("Are you sure want to remove this package?");
	            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	packagetoRemove = pack;
	                    	run0();			        		
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

	
	
	

}
