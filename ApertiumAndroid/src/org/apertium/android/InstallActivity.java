/**
 *
 * @author Arink Verma
 */

package org.apertium.android;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apertium.Translator;
import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileManager;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.languagepair.LanguagePackage;
import org.apertium.android.languagepair.TranslationMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
	private List<TranslationMode> translationModes;
	private String _path = null;
	private String _packageID =  null;
	private String _lastModified = null;
	private String FileName = null;
	
	private static ProgressDialog progressDialog;
	
	//To pharse and manage Config.json
	private LanguagePackage languagePackage;
	
	private Activity thisActivity = null;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    thisActivity = this;

	    initView();
		
		
		try {
			languagePackage = new LanguagePackage(this._path,this._packageID);
			languagePackage.setModifiedDate(this._lastModified);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Info1.append("\n"+languagePackage.PackageTitle()+"\n");		
		
		/* Finding the mode present in the package */		
		this.translationModes = languagePackage.getAvailableModes();			
		
		
		Heading2.setText(getString(R.string.modes));
		Info2.setText("");
		
	
		for (int i=0; i<this.translationModes.size(); i++) {
			TranslationMode M = this.translationModes.get(i);
			Info2.append((i+1)+". "+Translator.getTitle(M.getID())+"\n");						
		}
		
	}
	
	boolean isPackageValid(){
		String extn = this.FileName.substring(this.FileName.length() - 3, this.FileName.length());
		if(!extn.equalsIgnoreCase("jar")){
			return false;
		}else{
			try {
				Translator.setBase(this._path);
				if(Translator.getAvailableModes()==null || Translator.getAvailableModes().length ==0){
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;	
	}
	
	
	/* Init View, 
	 * Initialing view */
	private void initView() {
		Log.i(TAG,"InitView Started");
	    Bundle extras = getIntent().getExtras();
	    this._path = extras.getString("filepath");
	    this.FileName = extras.getString("filename");  
	    
	    
	    if(!isPackageValid()){
	        final AlertDialog.Builder b = new AlertDialog.Builder(this);
	        b.setIcon(android.R.drawable.ic_dialog_alert);
	        b.setCancelable(false);
	        b.setMessage(this.FileName+"\n"+getString(R.string.invalid_jar));
	        b.setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	thisActivity.finish();
	                }
	        });
	
	        b.show();
	    }
	    
	    this._packageID =  this.FileName.substring(0, this.FileName.length() - 4);
	    this._lastModified = extras.getString("filedate");
	    
		DB = new DatabaseHandler(this.getBaseContext());
		
	    setContentView(R.layout.install_package);
		Heading1 = (TextView) findViewById(R.id.textView1);
		Info1 = (TextView) findViewById(R.id.textView2);
		Heading1.setText("Package");
		Info1.setText(this._path);
		
		
		Heading2 = (TextView) findViewById(R.id.textView3);
		Info2 = (TextView) findViewById(R.id.textView4);
		
		_submitButton = (Button) findViewById(R.id.installButton);
		_cancelButton = (Button) findViewById(R.id.discardButton);
		_submitButton.setText(R.string.install);
		
		LanguagePackage installedPackage = DB.getPackage(this._packageID);
		if(installedPackage!=null){
			String installedDate = installedPackage.ModifiedDate();
			if(this._lastModified == null || installedDate == null || this._lastModified.equals(installedDate)){
				_submitButton.setText(R.string.update);
			}else{
				_submitButton.setText(R.string.reinstall);
			}
		}

			
			_submitButton.setOnClickListener(this);
			_cancelButton.setOnClickListener(this);	
		
		
	}
	

	
	@Override
	public void onClick(View v) {
		if (v.equals(_submitButton)){
			String Action =  (String) _submitButton.getText();
			if(Action.equals(getString(R.string.install))){
				//First run to copy new package
				Log.d("Todo","install");
				
				progressDialog = ProgressDialog.show(this, getString(R.string.installing)+"...", getString(R.string.unziping), true,false);
				run1();	
			}else if(Action.equals(getString(R.string.update)) || Action.equals(getString(R.string.reinstall))){
				//Zero run to remove previous package
				progressDialog = ProgressDialog.show(this, getString(R.string.installing)+"...", getString(R.string.removing_old), true,false);
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
			Thread t = new Thread() {
		        @Override
		        public void run() {
		        	try {
		        		File file = new File(AppPreference.JAR_DIR+"/"+languagePackage.PackageID());
		        		FileManager.remove(file);
		        		DB.deletePackage(languagePackage.PackageID());
		        	} catch (Exception e) {
						Heading1.setText(getString(R.string.error));
						Info1.setText(R.string.error_removing_old);
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
//Unziping file in temp dir
	private void run1(){	

	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	
				try {
					FileManager.unzip(_path,AppPreference.TEMP_DIR+"/"+_packageID);
				} catch (IOException e) {
					Log.e(TAG,e+"");
					e.printStackTrace();
				}
				
	            Message msg = Message.obtain();
	            msg.what = 1;
	            handler.sendMessage(msg);
	        }
	    };
	    t.start();
	}
	
//Step 2
//Installing..", "Copying files
	private void run2(){	
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	
	    	   
				FileManager.move(AppPreference.TEMP_DIR+"/"+languagePackage.PackageID(),AppPreference.JAR_DIR+"/"+languagePackage.PackageID()+"/extract");
				
				try {
					FileManager.copyFile(_path,AppPreference.JAR_DIR+"/"+languagePackage.PackageID()+"/"+languagePackage.PackageID()+".jar");
				} catch (IOException e) {
					Log.e(TAG,e+"");
					e.printStackTrace();
				}
				
	            Message msg = Message.obtain();
	            msg.what = 2;
	            handler.sendMessage(msg);
	        }
	    };
	    t.start();
	}
	
//Step 3
//Writing database
	private void run3(){	
		//progressDialog.setMessage("Writing database");
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	DB.addLanuagepair(languagePackage);
	            Message msg = Message.obtain();
	            msg.what = 3;
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
	        	progressDialog.setMessage(getString(R.string.unziping));
	        	run1();
	            break;
	        case 1:
	        	progressDialog.setMessage(getString(R.string.copying));
	        	run2();
	            break;	        	
	        case 2:
	        	progressDialog.setMessage(getString(R.string.writing_db));
	        	run3();
	            break;
	        case 3:
	        	progressDialog.dismiss();	
	        	Intent myIntent1 = new Intent(InstallActivity.this,ModeManageActivity.class);
	        	InstallActivity.this.startActivity(myIntent1);
	        	finish();
	            break;
	        	
	        }
	    }
	};
}
