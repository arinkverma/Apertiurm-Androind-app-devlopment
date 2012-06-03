/**
 *
 * @author Arink Verma
 */

package org.apertium.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileManager;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.helper.ConfigManager;
import org.apertium.android.languagepair.LanguagePackage;
import org.apertium.android.languagepair.TranslationMode;
import org.json.JSONArray;
import org.json.JSONException;

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

	//Buttons
	private Button _submitButton,_cancelButton;
	
	//Text view
	private TextView Heading1,Info1,Heading2,Info2;
	private DatabaseHandler DB;	
	private LanguagePackage pack;
	private List<TranslationMode> translationModes;
	private String path = "-1";
	
	//Action to be perform
	private enum Action  {install,update,discard};
	Action todo;
	
	private static ProgressDialog progressDialog;
	
	//To pharse and manage Config.json
	private ConfigManager config;
	private JSONArray modeitems;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    initView();
		
		todo = Action.install;
		
		try {
			config = new ConfigManager(path);
			Info1.append("\nName: "+config.Package()+"\n");
			Info1.append("Ver: "+config.Version()+"\n");
			
		} catch (IOException e1) {
			Info1.append("IO error"+e1);
		} catch (JSONException e1) {
			Info1.append("Error in config file "+e1);
		}catch (Exception e1) {
			Info1.append("Error in config file "+e1);
		}
		
		
		/* Checking the version of package, hence perform action accordingly */
		LanguagePackage P = DB.getPackage(config.Package());
		
		if(P!=null){
			if(P.isNewerthan(config.Version())){	
				todo = Action.discard;
				_submitButton.setText("This is old version");
			}else{
				todo = Action.update;				
				_submitButton.setText("Update");
			}
			Info1.append("Ver installed: "+P.getVersion()+"\n");
		}
		
		
		/* Finding the mode present in the package */		
		modeitems = config.getModeItems();	
		
		try {
			pack = new LanguagePackage(config.Package());
			pack.setVersion(config.Version());
			pack.setModes(modeitems);
			translationModes = pack.getModes();
			Heading2.setText("Modes found!");
			Info2.setText("");
			for (int i=0; i<translationModes.size(); i++) {
				TranslationMode m = translationModes.get(i);
				Info2.append((i+1)+". "+m.getTitle()+" ["+m.getID()+"]\n");						
			}
			
		} catch (JSONException e) {
			Info2.setText("Error in config file "+e);
		}	
	}
	
	
	/* Init View, 
	 * Initialing view */
	private void initView() {
		Log.i("InstallActivity.InitView","Started");
	    Bundle extras = getIntent().getExtras();
	    path = extras.getString("filepath");
		DB = new DatabaseHandler(this.getBaseContext());
	    setContentView(R.layout.install);
		Heading1 = (TextView) findViewById(R.id.textView1);
		Info1 = (TextView) findViewById(R.id.textView2);
		Heading2 = (TextView) findViewById(R.id.TextView01);
		Info2 = (TextView) findViewById(R.id.TextView02);
		_submitButton = (Button) findViewById(R.id.button1);
		_cancelButton = (Button) findViewById(R.id.button2);

		Heading1.setText("Package");
		Info1.setText(path);
		_submitButton.setText("Install");
		
		_submitButton.setOnClickListener(this);
		_cancelButton.setOnClickListener(this);	
	}
	

	
	@Override
	public void onClick(View v) {
		if (v.equals(_submitButton)){
			if(todo == Action.install){
				//First run to copy new package
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
		        		File file = new File(AppPreference.BASE_DIR()+"/"+config.Package()+".zip");
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
	        	InputStream in = null;
				OutputStream out = null;
	        	try {	        		
	        		FileManager.setDIR();				    
					in = new FileInputStream(path);
					out = new FileOutputStream(AppPreference.BASE_DIR()+"/"+config.Package()+".zip");					
	        		FileManager.copyFile(in,out);	        		
	        		in.close();
					out.flush();
					out.close();
	        	} catch (IOException e) {
					e.printStackTrace();
					Log.e("Install",e.getMessage());
					Message msg = Message.obtain();
		            msg.what = -1;
		            handler.sendMessage(msg);
		            return;
				}
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
