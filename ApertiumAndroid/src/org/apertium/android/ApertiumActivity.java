/**
 * ApertiumActivity.java
 * Main Launcher Activity of application
 * @author Arink Verma
 * 
 */


package org.apertium.android;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apertium.Translator;
import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileManager;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.helper.ClipboardHandler;
import org.apertium.android.languagepair.RulesHandler;
import org.apertium.android.languagepair.TranslationMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ApertiumActivity extends Activity implements OnClickListener{

	private final String TAG = "ApertiumActiviy";
	private Activity thisActivity = null;
	private AppPreference appPreference = null;

	private static String MODE = null;
	private static String FROM_TITLE = null;
	private static String TO_TITLE = null;
	private String outputText = null;

	//Text Fields
	private EditText _inputText;
	private TextView _outputText;

	//Button
	private Button _submitButton,_toButton,_fromButton,_dirButton;
	private ProgressDialog progressDialog;


	private TranslationMode translationMode;
	
	private ClipboardHandler clipboardHandler = null;
	private DatabaseHandler databaseHandler = null;
	private RulesHandler rulesHandler = null;


	/* OnCreate */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity = this;
		appPreference 	= new AppPreference(thisActivity);
		databaseHandler = new DatabaseHandler(thisActivity);
		rulesHandler 	= new RulesHandler(thisActivity);
		clipboardHandler = new ClipboardHandler(thisActivity);
		
		/* Recovery and restore states */
		CrashRecovery();
		FileManager.setDIR();
		updateDirChanges();
	
		/* Fetching if mode is sent by widgets */
		MODE = rulesHandler.getCurrentMode();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			String BundleMODE = extras.getString("Mode");
			if(BundleMODE!=null){
				MODE = BundleMODE;
			}
		}
		
		/* Setting up Translator base and properties */
		Log.i(TAG,"Current mode = "+MODE+", Cache = "+appPreference.isCacheEnabled()+", Clipboard push get = "+appPreference.isClipBoardPushEnabled()+appPreference.isClipBoardGetEnabled());
		translationMode = databaseHandler.getMode(MODE);
		if(translationMode!=null && translationMode.isValid()){
			try {
				rulesHandler.setCurrentMode(MODE);
				Translator.setBase(rulesHandler.ExtractPathCurrentPackage(), rulesHandler.getClassLoader());
				Translator.setDelayedNodeLoadingEnabled(true);
	     		Translator.setDelayedNodeLoadingEnabled(true);
	    		Translator.setMemmappingEnabled(true);
	    		Translator.setPipingEnabled(false);
			} catch (Exception e) {
				Log.e(TAG, "Error while loading class"+e);
				e.printStackTrace(); 
			}
		}

		/* Generating layout view */
		initView();
	}
	
	
	/* OnResume */
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG,"onResume mode=" + rulesHandler.getCurrentMode());
		
		/* Fetching if mode is sent by widgets */
		MODE = rulesHandler.getCurrentMode();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			String BundleMODE = extras.getString("Mode");
			if(BundleMODE!=null){
				MODE = BundleMODE;
			}
		}

		
		/* updating if mode is changed */
		translationMode = databaseHandler.getMode(MODE);
		if(translationMode!= null && translationMode.isValid()){
			UpdateMode();
		}else{
			Log.i(TAG,"Invalid mode");
			_toButton.setText(R.string.to);
			_fromButton.setText(R.string.from);
			
			TO_TITLE 	= getString(R.string.to);
			FROM_TITLE 	= getString(R.string.from);
		}
	}

	/* Init View,
	 * Initialing view */
	private void initView() {
		Log.i(TAG,"ApertiumActivityInitView Started");
		setContentView(R.layout.main_layout);
		_inputText 		= (EditText) findViewById(R.id.inputtext);
		if(appPreference.isClipBoardGetEnabled()){
			_inputText.setText(clipboardHandler.getText());
		}

		_submitButton	= (Button) findViewById(R.id.translateButton);
		_outputText 	= (TextView) findViewById(R.id.outputText);
		_toButton 		= (Button) findViewById(R.id.toButton);
		_fromButton		= (Button) findViewById(R.id.fromButton);
		_dirButton 		= (Button) findViewById(R.id.modeSwitch);

		_submitButton.setOnClickListener(this);
		_toButton.setOnClickListener(this);
		_fromButton.setOnClickListener(this);
		_dirButton.setOnClickListener(this);
	}

	/* Update Translator mode if user change */
	private void UpdateMode(){
		if (MODE==null) {
			_submitButton.setEnabled(false);
			if(databaseHandler.getAllModes().isEmpty()){
				// No modes, go to download
				startActivity(new Intent(ApertiumActivity.this, DownloadActivity.class));
			}
			_toButton.setText(R.string.to);
			_toButton.setText(R.string.from);
			
			return;
		}
		_submitButton.setEnabled(true);
		

		Log.i(TAG,"UpdateMode ="+MODE+", cache= "+appPreference.isCacheEnabled());

    	try {

    		String currentPackage = rulesHandler.getCurrentPackage();
    		String PackageTOLoad = rulesHandler.findPackage(MODE);
    		Log.i(TAG,"Currentpackage = "+currentPackage+",PackageTOLoad = " +PackageTOLoad);
    		
    		//If mode is changed
    		if(!MODE.equals(rulesHandler.getCurrentMode())){
    			Log.i(TAG,"setCurrentMode="+MODE);    			
    			rulesHandler.setCurrentMode(MODE);
    		}
    		
    		//If package is changed
    		if(currentPackage==null || !currentPackage.equals(PackageTOLoad)){
    			Log.i(TAG,"setBase="+PackageTOLoad+" CurrentPackage ="+currentPackage+", "+PackageTOLoad);
    			if(appPreference.isCacheEnabled()){
    				Translator.clearCache();
    			}
        		Log.i(TAG,"BASE ="+rulesHandler.getClassLoader()+"path = "+rulesHandler.ExtractPathCurrentPackage());
    			
        		Translator.setBase(rulesHandler.ExtractPathCurrentPackage(), rulesHandler.getClassLoader());
        		Translator.setDelayedNodeLoadingEnabled(true);
        		Translator.setMemmappingEnabled(true);
        		Translator.setPipingEnabled(false);
    		}
    		    		
			Translator.setMode(MODE);

			translationMode = databaseHandler.getMode(MODE);
			FROM_TITLE = translationMode.getFrom();
			TO_TITLE = translationMode.getTo();
			
			_toButton.setText(TO_TITLE);
			_fromButton.setText(FROM_TITLE);
		} catch (Exception e) {
			Log.e(TAG,"UpdateMode "+e+"Mode = "+MODE);
			e.printStackTrace();
		}

	}
	
	
	@Override
	public void onClick(View v) {
		
		if (v.equals(_submitButton)){
			if(translationMode!= null && translationMode.isValid()){
				//Hiding soft keypad
				InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(_inputText.getApplicationWindowToken(), 0);
		
				progressDialog = ProgressDialog.show(this, getString(R.string.translator), getString(R.string.working),  true,false);
				TranslationRun();
			}
		}else if(v.equals(_fromButton)){
			
			if (databaseHandler.getAllModes().isEmpty()) {					
				// No modes, go to download
				startActivity(new Intent(ApertiumActivity.this, DownloadActivity.class));
				return;
			} 
				
			final String[] ModeTitle = databaseHandler.getModeTitlesOut();			

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.translate_from));
			builder.setItems(ModeTitle, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int position) {
				    Toast.makeText(getApplicationContext(), ModeTitle[position],   Toast.LENGTH_SHORT).show();
				    FROM_TITLE = ModeTitle[position];
				    TO_TITLE = null;
				    _fromButton.setText(FROM_TITLE);
				    _toButton.setText(R.string.to);				    
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
		}else if(v.equals(_toButton)){
			
			if (databaseHandler.getAllModes().isEmpty()) {					
				// No modes, go to download
				startActivity(new Intent(ApertiumActivity.this, DownloadActivity.class));
				return;
			} 

			final String[] ModeTitle =  databaseHandler.getModeTitlesInFrom(FROM_TITLE);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.translate_to));
			builder.setItems(ModeTitle, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int position) {
			    	Toast.makeText(getApplicationContext(), ModeTitle[position],   Toast.LENGTH_SHORT).show();
				    TO_TITLE = ModeTitle[position];
				    _toButton.setText(TO_TITLE);
				    Log.i(TAG,databaseHandler.getModeID(FROM_TITLE,TO_TITLE));
				    MODE = databaseHandler.getModeID(FROM_TITLE,TO_TITLE);
				    
				    UpdateMode();
			    }
			});
			
			AlertDialog alert = builder.create();
			alert.show();
		}else if(v.equals(_dirButton)){
			String temp = FROM_TITLE;
			FROM_TITLE = TO_TITLE;
			TO_TITLE = temp;
			temp = databaseHandler.getModeID(FROM_TITLE,TO_TITLE);
			if(temp == null){
				Toast.makeText(getApplicationContext(), getString(R.string.no_mode_available,FROM_TITLE,TO_TITLE),   Toast.LENGTH_SHORT).show();
				temp = FROM_TITLE;
				FROM_TITLE = TO_TITLE;
				TO_TITLE = temp;				  
			}else{
				MODE = temp;
				UpdateMode();
			}
		}

	}


	/* Translation Thread,
	 * Load translation rules and excute lttoolbox.jar */
	private void TranslationRun(){
		
		Thread t = new Thread() {
			@Override
			public void run() {
			String inputText = _inputText.getText().toString();
			if (!TextUtils.isEmpty(inputText)) {
				outputText = "";
				
				try {
					Translator.setCacheEnabled(appPreference.isCacheEnabled());
					Log.i(TAG,"Translator Run Cache ="+appPreference.isCacheEnabled()+", Mark ="+appPreference.isDisplayMarkEnabled()+ ", MODE = "+MODE);
					Translator.setDisplayMarks(appPreference.isDisplayMarkEnabled());
					outputText  = Translator.translate(_inputText.getText().toString());
					
					if(appPreference.isClipBoardPushEnabled()){
					clipboardHandler.putText(outputText);
					}
			
				} catch (Exception e) {
					Log.e(TAG,"ApertiumActivity.TranslationRun MODE ="+MODE+";InputText = "+_inputText.getText());
					e.printStackTrace(); 
				}
			}
			
			Message msg = Message.obtain();
			msg.what = 1;
			handler.sendMessage(msg);
			}
		};
		t.start();
	    
	    
	    //Saving and setting crash happen flag
	    
	    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
            public void uncaughtException(Thread t, Throwable e) {
				
	    	    Translator.clearCache();
	    	    String error = "["+e+"]";
	    	    Log.e("Error", error);
	    	    appPreference.ReportCrash(error);
	    	    progressDialog.dismiss();
	    	    e.printStackTrace(); 
	    	    thisActivity.finish();
	    	    android.os.Process.killProcess(android.os.Process.myPid());
	    	    
	         }
        });
	    
	}


	/* Waiting for Acknowledgement from threads running */
	private Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        switch(msg.what){
	        case 1:
	        	_outputText.setText(outputText);
	        	progressDialog.dismiss();
	            break;
	        case 2:
	        	progressDialog.dismiss();
				appPreference.SaveState();
	            break;
	        }
	    }
	};



	private void updateDirChanges() {
		if(appPreference.isStateChanged()){
			progressDialog = new ProgressDialog(thisActivity);
			progressDialog.setTitle(getString(R.string.updating_db));
			progressDialog.setMessage(getString(R.string.working));
			progressDialog.setCancelable(false);
			progressDialog.show();
			Thread t = new Thread() {
				 @Override
				 public void run() {
					 databaseHandler.updateDB();
					 Message msg;
					 msg = Message.obtain();
					 msg.what = 2;
					 handler.sendMessage(msg);
				}
			};
			t.start();
		}
		
	}


	@SuppressWarnings("deprecation")
	private void CrashRecovery(){
		final String crash = appPreference.GetCrashReport();
		if(crash != null){
			appPreference.ClearCrashReport();
			Log.i(TAG,"Crash on last run time" + crash);
			 
    	    final AlertDialog alertDialog = new AlertDialog.Builder(thisActivity).create();
    	    alertDialog.setTitle(R.string.crash_detect);
    	    alertDialog.setMessage(getString(R.string.crash_message_with_error_and_support_address,crash,"arinkverma@gmail.com"));
    	    
    	    alertDialog.setButton(getString(R.string.report), new DialogInterface.OnClickListener() {
    	        public void onClick(final DialogInterface dialog, final int which) {  
    	        	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
    	        	emailIntent.setType("plain/text");
    	        	String aEmailList[] = { "arinkverma@gmail.com" }; 	  
    	        	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);    
    	        	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Apertium Android Error Report");   
    	        	emailIntent.setType("plain/text");  
    	        	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Error "+crash);  
    	        	startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_in))); 
    	        	alertDialog.dismiss();    	   
    	     } });
    	    
    	    alertDialog.setButton2(getString(R.string.setting), new DialogInterface.OnClickListener() {
    	        public void onClick(final DialogInterface dialog, final int which) {
    	   
    	    		final Intent myIntent = new Intent(ApertiumActivity.this, ManageActivity.class);
    				ApertiumActivity.this.startActivity(myIntent);
    	   
    	     } });
    	    
    	    alertDialog.show();
		
		}
	}
	
	
	
	/****
	 *  Option menu 
	 *  1. share
	 *  2. setting
	 *  3. about*/
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.option_menu, menu);
	    return true;
	 }

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.manage:
				Intent myIntent = new Intent(ApertiumActivity.this, ManageActivity.class);
				ApertiumActivity.this.startActivity(myIntent);
				return true;
			case R.id.share:
				share_text();
				return true;
			case R.id.about:
				Toast.makeText(this, "This is under construction",Toast.LENGTH_SHORT).show();
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/* Share text
	 * Intent to share translated text over other installed application services */
	private void share_text() {
		Log.i(TAG,"ApertiumActivity.share_text Started");
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Apertium Translate");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, outputText);
		startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
	}

}