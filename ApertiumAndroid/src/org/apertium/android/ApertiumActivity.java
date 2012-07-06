/**
 *
 * @author Arink Verma
 */


package org.apertium.android;

import java.util.List;

import org.apertium.Translator;
import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileManager;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.helper.ClipboardHandler;
import org.apertium.android.helper.RulesHandler;
import org.apertium.android.languagepair.TranslationMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ApertiumActivity extends Activity implements OnClickListener{
	
	private final String TAG = "ApertiumActiviy";

	private static String MODE = "??-??";
	private String outputText = null;

	//Text Fields 
	private EditText _inputText;
	private TextView _outputText;
	
	//Button
	private Button _submitButton,_modeButton;
	private ProgressDialog progressDialog;
	private DatabaseHandler DB;
	
	//Rules Manager
	private RulesHandler rulesHandler;
	
	
	//Clipboard
	ClipboardHandler clipboardHandler = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FileManager.setDIR();
		
		DB = new DatabaseHandler(this.getBaseContext());
		rulesHandler = new RulesHandler(this.getBaseContext());
		
		clipboardHandler = new ClipboardHandler(this);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		if (extras != null) {
			MODE = extras.getString("Mode");
			Log.i(TAG,"MODE set from other activity"+MODE);
			if(MODE!=null){
				rulesHandler.setCurrentMode(MODE);
			}
		}
		
    	
		MODE = rulesHandler.getCurrentMode();
		
		initView();
	    UpdateMode();
		
		Log.i(TAG,"Created with Mode"+MODE);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		rulesHandler = new RulesHandler(this);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			MODE = extras.getString("Mode");
			if(MODE!=null){
				rulesHandler.setCurrentMode(MODE);
			}
		}
		
		MODE = rulesHandler.getCurrentMode();
		_modeButton.setText(MODE);
		Log.e(TAG,"onResume mode=" + rulesHandler.getCurrentMode());
	}
	  
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.option_menu, menu);
	    return true;
	 }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.manage:
				Intent myIntent1 = new Intent(ApertiumActivity.this, ManageActivity.class);
				ApertiumActivity.this.startActivity(myIntent1);
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
	
	
	/* Init View, 
	 * Initialing view */
	private void initView() {
		Log.i(TAG,"ApertiumActivity.InitView Started");
		setContentView(R.layout.main);
		_inputText 		= (EditText) findViewById(R.id.inputtext);
		if(AppPreference.isClipBoardGetEnabled()){
			_inputText.setText(clipboardHandler.getText());
		}
		
		_submitButton 	= (Button) findViewById(R.id.translateButton);
		_outputText 	= (TextView) findViewById(R.id.outputtext);
		_modeButton 	= (Button) findViewById(R.id.modeButton);
		
		_submitButton.setOnClickListener(this);
		_modeButton.setOnClickListener(this);		
		_modeButton.setText(MODE);   
		 	 	
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
				    	Log.i(TAG,"Translator Run Cache ="+AppPreference.isCacheEnabled()+", Mark ="+AppPreference.isDisplayMarkEnabled()+ ", MODE = "+MODE);
				    	Translator.setDisplayMarks(AppPreference.isDisplayMarkEnabled());
						outputText  = Translator.translate(_inputText.getText().toString());
						
						if(AppPreference.isClipBoardPushEnabled()){
							clipboardHandler.putText(outputText);
						}
						
						
				     	} catch (Exception e) {
							 Log.e(TAG,"ApertiumActivity.TranslationRun MODE ="+MODE+";InputText = "+_inputText.getText());
				        }		
				}
	            Message msg = Message.obtain();
	            msg.what = 1;
	            handler.sendMessage(msg);
	        }
	    };
	    t.start();
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
	        }
	    }
	};

	@Override
	public void onClick(View v) {
		if (v.equals(_submitButton)){	
					progressDialog = ProgressDialog.show(this, "", "Translating..",  true,false);
					TranslationRun();
		}else if(v.equals(_modeButton) && !MODE.equals("??-??")){
			TranslationMode M = DB.getMode(MODE);
			List<TranslationMode> ModeList = DB.getModes(M.getPackage());
			final String[] ModeTitle = new String[ModeList.size()];
		    final String[] ModeId = new String[ModeList.size()];
			for(int i=0;i<ModeList.size();i++){
				TranslationMode m = ModeList.get(i);
				ModeTitle[i] = m.getTitle();
				ModeId[i] 	= m.getID();
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select mode");
			builder.setItems(ModeTitle, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int position) {
			    	rulesHandler.setCurrentMode(ModeId[position]);
				    Toast.makeText(getApplicationContext(), ModeTitle[position],   Toast.LENGTH_SHORT).show();
				    MODE = ModeId[position];
				    UpdateMode();				
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();		
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
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
	
	private void UpdateMode(){			    	
    	try {
    		rulesHandler.setCurrentMode(MODE);	
			Translator.setBase(rulesHandler.getClassLoader());
			Translator.setCacheEnabled(AppPreference.isCacheEnabled());
			Translator.setMode(MODE);
			_modeButton.setText(MODE);
			Log.i(TAG,"UpdateMode ="+MODE+", cache= "+AppPreference.isCacheEnabled());
		} catch (Exception e) {
			Log.e(TAG,"UpdateMode "+e);
		}

	}	
	
}