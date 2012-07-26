/**
 *
 * @author Arink Verma
 */

package org.apertium.android;

import java.io.File;
import java.util.List;

import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileManager;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.helper.ConfigManager;
import org.apertium.android.languagepair.LanguagePackage;
import org.apertium.android.languagepair.TranslationMode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InstallActivity extends Activity implements OnClickListener {
	
	private final String TAG = "InstallActivity";

	//Buttons
	private Button _submitButton,_cancelButton;
	
	//Text view
	private TextView Heading1,Info1,Heading2,Info2;
	private DatabaseHandler DB;	
	private LanguagePackage pack;
	private List<TranslationMode> translationModes;
	private String _path = "-1";
	private String _packageID = "-1";
	private String _lastModified ="-1";
	
	//Action to be perform
	private enum Action  {install,update,discard};
	Action todo;
	
	private static ProgressDialog progressDialog;
	
	//To pharse and manage Config.json
	private ConfigManager config;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    initView();
		
		todo = Action.install;
		
		try {
			config = new ConfigManager(this._path,this._packageID);
			config.setModifiedDate(this._lastModified);
			pack = new LanguagePackage(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Info1.append("\nName: "+config.PackageTitle()+"\n");
		//At present no version number is supported 
		//Info1.append("Ver: "+config.Version()+"\n");
	
		
		/* Checking the version of package, hence perform action accordingly */
		String LastDate = DB.getLastModifiedDate(config.PackageID());
		
		if(LastDate!=null){
			if(LastDate.equals(this._lastModified)){	
				todo = Action.discard;
				_submitButton.setText("This version is already installed!");
			}else{
				todo = Action.update;				
				_submitButton.setText("Update");
			}
		}
		
		
		/* Finding the mode present in the package */		
		this.translationModes = config.getAvailableModes();			
		
		
		Heading2.setText("Modes found!");
		Info2.setText("");
		for (int i=0; i<this.translationModes.size(); i++) {
			TranslationMode M = this.translationModes.get(i);
			Info2.append((i+1)+". "+M.getID()+"\n");						
		}
		
	}
	
	
	/* Init View, 
	 * Initialing view */
	private void initView() {
		Log.i(TAG,"InitView Started");
	    Bundle extras = getIntent().getExtras();
	    this._path = extras.getString("filepath");
	    this._packageID = extras.getString("filename");
	    this._lastModified = extras.getString("filedate");
	    
		DB = new DatabaseHandler(this.getBaseContext());
	    setContentView(R.layout.install_package);
		Heading1 = (TextView) findViewById(R.id.textView1);
		Info1 = (TextView) findViewById(R.id.textView2);
		Heading2 = (TextView) findViewById(R.id.textView3);
		Info2 = (TextView) findViewById(R.id.textView4);
		_submitButton = (Button) findViewById(R.id.installButton);
		_cancelButton = (Button) findViewById(R.id.discardButton);

		Heading1.setText("Package");
		Info1.setText(this._path);
		_submitButton.setText("Install");
		
		_submitButton.setOnClickListener(this);
		_cancelButton.setOnClickListener(this);	
	}
	

	
	@Override
	public void onClick(View v) {
		if (v.equals(_submitButton)){
			if(todo == Action.install){
				//First run to copy new package
				Log.d("Todo","install");
				run1();	
			}else if(todo == Action.update){
				//Zero run to remove previous package
				run0();					
			}else{
				finish();
			}
		}else if(v.equals(_cancelButton)){
			finish();
		}
	}
	

	
/*** Installation in 2 steps */
//Step 0 
//Removing old files and data
		private void run0(){		
			progressDialog = ProgressDialog.show(this, "Installing..", "Removing old files and data", true,false);
		    Thread t = new Thread() {
		        @Override
		        public void run() {
		        	try {
		        		File file = new File(AppPreference.JAR_DIR()+"/"+config.PackageID());
		        		FileManager.remove(file);
		        		DB.deletePackage(pack.getID());
		        	} catch (Exception e) {
						Heading1.setText("Error!");
						Info1.setText("Cannot remove old package");
						e.printStackTrace();
					}
		            Message msg = Message.obtain();
		            msg.what = 0;
		            handler.sendMessage(msg);
		        }
		    };
		    t.start();
		}	
	
	
	
	
//Step 1
//Installing..", "Copying files
	private void run1(){	
		if(todo == Action.install){
			progressDialog = ProgressDialog.show(this, "Installing..", "Copying files", true,false);
		}
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	
				FileManager.move(_path,AppPreference.JAR_DIR()+"/"+config.PackageID());
				
	            Message msg = Message.obtain();
	            msg.what = 1;
	            handler.sendMessage(msg);
	        }
	    };
	    t.start();
	}
	
//Step 2
//Writing database
	private void run2(){	
		//progressDialog.setMessage("Writing database");
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	DB.addLanuagepair(pack);
	            Message msg = Message.obtain();
	            msg.what = 2;
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
	        	progressDialog.setMessage("Copying new files");
	        	run1();
	            break;
	        case 1:
	        	progressDialog.setMessage("Writing database");
	        	run2();
	            break;
	        case 2:
	        	progressDialog.dismiss();	
	        	Intent myIntent1 = new Intent(InstallActivity.this,ModeManageActivity.class);
	        	InstallActivity.this.startActivity(myIntent1);
	        	finish();
	            break;
	        case -1:
	        	progressDialog.dismiss();
	        	Info1.setText("Error occur while installion");
	        	
	        }
	    }
	};
}
