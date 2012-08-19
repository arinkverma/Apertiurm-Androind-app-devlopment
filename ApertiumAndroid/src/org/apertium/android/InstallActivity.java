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

/**
 * InstallActivity.java
 * Install jar language package
 * @author Arink Verma
 */

package org.apertium.android;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apertium.Translator;
import org.apertium.android.database.DatabaseHandler;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InstallActivity extends Activity implements OnClickListener {
	private final String TAG = "InstallActivity";
	private Activity thisActivity = null;

	/*Layout variable*/
	//Buttons
	private Button submitButton,cancelButton;
	//Text view
	private TextView heading,info1,info2;
	
	/*Mode related variable*/
	private List<TranslationMode> translationModes;
	//To pharse and manage Config.json
	private LanguagePackage languagePackage;
	private String FilePath = null;
	private String packageID =  null;
	private String lastModified = null;
	private String FileName = null;
	
	
    /*Data Handler
     * Data which persist */
	private DatabaseHandler dataBaseHandler;
	
	/*Process Handlers */
	/* Lint warning
	 * This Handler class should be static or leaks might occur 
	 * Android Lint Problem
	 */
	private static Handler handler = null;
	private ProgressDialog progressDialog = null;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    thisActivity = this;
	    getExtrasData();

	    handler = new Handler();
	    dataBaseHandler = new DatabaseHandler(thisActivity);
		
		try {
			languagePackage = new LanguagePackage(this.FilePath,this.packageID);
			languagePackage.setModifiedDate(this.lastModified);
			/* Finding the mode present in the package */		
			translationModes = languagePackage.getAvailableModes();	
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	    initView();
	}
	
	void getExtrasData(){
	    Bundle extras = getIntent().getExtras();
	    this.FilePath = extras.getString("filepath");
	    this.FileName = extras.getString("filename");
	    this.lastModified = extras.getString("filedate");
	    
	    Log.i(TAG,"getExtrasData filename ="+FileName+", path = "+FilePath+", LastDate="+lastModified);
	    
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
	    
	    this.packageID =  this.FileName.substring(0, this.FileName.length() - 4);
	}
		
	
	/* Init View, 
	 * Initialing view */
	private void initView() {
		Log.i(TAG,"InitView Started");
	    setContentView(R.layout.install_package);
	    /*Sub Heading Titles*/
		heading = (TextView) findViewById(R.id.textView1);
		info1 = (TextView) findViewById(R.id.textView2);
		info2 = (TextView) findViewById(R.id.textView4);
		info1.setText(this.FilePath);
		info2.setText("");	
		for (int i=0; i<this.translationModes.size(); i++) {
			TranslationMode M = this.translationModes.get(i);
			info2.append((i+1)+". "+Translator.getTitle(M.getID())+"\n");						
		}
		
		
		/*Action Button*/
		submitButton = (Button) findViewById(R.id.installButton);
		cancelButton = (Button) findViewById(R.id.discardButton);
		submitButton.setText(R.string.install);
		
		LanguagePackage installedPackage = dataBaseHandler.getPackage(this.packageID);
		if(installedPackage!=null){
			String installedDate = installedPackage.ModifiedDate();
			if(this.lastModified == null || installedDate == null || this.lastModified.equals(installedDate)){
				submitButton.setText(R.string.update);
			}else{
				submitButton.setText(R.string.reinstall);
			}
		}

		submitButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);	
		
	}
	

	
	@Override
	public void onClick(View v) {
		if (v.equals(submitButton)){
			String Action =  (String) submitButton.getText();
			
			progressDialog = ProgressDialog.show(this, getString(R.string.installing)+"...", Action, true,false);
			
			if(Action.equals(getString(R.string.install))){
				//Zero run to remove previous package
				ExtractRun();	
			}else if(Action.equals(getString(R.string.update)) || Action.equals(getString(R.string.reinstall))){
				//First run to copy new package
				RemoveOldRun();					
			}else{
				finish();
			}
		}else if(v.equals(cancelButton)){
			finish();
		}
	}
	
	
	
/*** Installation in 3 steps 
 * Step 0 (if updatating) RemoveOldRun() 
 * Step 1 ExtractRun()
 * Step 2 FileCopyRun()
 * Step 3 DataBaseWriteRun()
 * Step 4 RemoveOtherFileRun()
 * */
	
//Step 0 
//Removing old files and data
		private void RemoveOldRun(){		
			progressDialog.setMessage(getString(R.string.removing_old));
			Thread t = new Thread() {
		        @Override
		        public void run() {
		        	try {
		        		File file = new File(AppPreference.JAR_DIR+"/"+languagePackage.PackageID());
		        		FileManager.remove(file);
		        		dataBaseHandler.deletePackage(languagePackage.PackageID());
		        	} catch (Exception e) {
						heading.setText(getString(R.string.error));
						info1.setText(R.string.error_removing_old);
						e.printStackTrace();
					}
		        	
		        	handler.post(new Runnable() {
		        		@Override
						public void run() {
							ExtractRun();
						}
                    });
		        }
		    };
		    t.start();
		}	
	
	
//Step 1
//Unziping file in temp dir
	private void ExtractRun(){	
		progressDialog.setMessage(getString(R.string.unziping));
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	
				try {
					FileManager.unzip(FilePath,AppPreference.TEMP_DIR+"/"+packageID);
				} catch (IOException e) {
					Log.e(TAG,e+"");
					e.printStackTrace();
				}
				
	        	handler.post(new Runnable() {
	        		@Override
					public void run() {
						FileCopyRun();
					}
                });
	        }
	    };
	    t.start();
	}
	
//Step 2
//Installing..", "Copying files
	private void FileCopyRun(){	
		progressDialog.setMessage(getString(R.string.copying));
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	
	    	   
				FileManager.move(AppPreference.TEMP_DIR+"/"+languagePackage.PackageID(),AppPreference.JAR_DIR+"/"+languagePackage.PackageID()+"/extract");
				
				try {
					FileManager.copyFile(FilePath,AppPreference.JAR_DIR+"/"+languagePackage.PackageID()+"/"+languagePackage.PackageID()+".jar");
				} catch (IOException e) {
					Log.e(TAG,e+"");
					e.printStackTrace();
				}
				
	        	handler.post(new Runnable() {
	        		@Override
					public void run() {
						DataBaseWriteRun();
					}
                });
	        	
	        }
	    };
	    t.start();
	}
	
//Step 3
//Writing database
	private void DataBaseWriteRun(){	
		progressDialog.setMessage(getString(R.string.writing_db));
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	dataBaseHandler.addLanuagepair(languagePackage);
	        	
	        	handler.post(new Runnable() {
	        		@Override
					public void run() {
						RemoveOtherFileRun();
					}
                });

	        }
	    };
	    t.start();
	}
	
	//Step 4
	//Removing  temp files
	private void RemoveOtherFileRun(){	
		progressDialog.setMessage(getString(R.string.removing_temp));
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	File file = new File(AppPreference.JAR_DIR+"/"+languagePackage.PackageID()+"/extract");
	        	File[] childfiles = file.listFiles();
	        	for(int i=0;i<childfiles.length;i++){
	        		if(childfiles[i].isDirectory()){
	        			if(!childfiles[i].getName().equalsIgnoreCase("data")){
	        				FileManager.remove(childfiles[i]);
	        			}
	        		}
	        	}
	        	
	           	progressDialog.dismiss();	
	        	Intent myIntent1 = new Intent(InstallActivity.this,ModeManageActivity.class);
	        	InstallActivity.this.startActivity(myIntent1);
	        	finish();
	      
	        }
	    };
	    t.start();
	}
	
	
	boolean isPackageValid(){
		boolean isValid = true;
		String extn = this.FileName.substring(this.FileName.length() - 3, this.FileName.length());
		if(!extn.equalsIgnoreCase("jar")){
			isValid = false;
		}else{
			try {
				Translator.setBase(this.FilePath);
			} catch (Exception e) {
				e.printStackTrace();
				isValid = false;
			}
			if(Translator.getAvailableModes()==null || Translator.getAvailableModes().length ==0){
				isValid = false;
			}
		}
		return isValid;	
	}
	
}
