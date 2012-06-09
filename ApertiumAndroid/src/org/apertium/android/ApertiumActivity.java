/**
 *
 * @author Arink Verma
 */


package org.apertium.android;

import java.util.List;

import org.apertium.Translator;
import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.helper.AppPreference;
import org.apertium.android.languagepair.TranslationMode;
import org.apertium.android.languagepair.TranslationRules;

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
	private TranslationRules translationRules;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DB = new DatabaseHandler(this.getBaseContext());
		initView();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MODE = AppPreference.CurrentMode(this);
		_modeButton.setText(MODE);
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
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	/* Init View, 
	 * Initialing view */
	private void initView() {
		Log.i("ApertiumActivity.InitView","Started");
		setContentView(R.layout.main);
		_inputText 		= (EditText) findViewById(R.id.inputtext);
		_submitButton 	= (Button) findViewById(R.id.submit);
		_outputText 	= (TextView) findViewById(R.id.outputtext);
		_modeButton 	= (Button) findViewById(R.id.button1);
		
		_submitButton.setOnClickListener(this);
		_modeButton.setOnClickListener(this);
		

	     MODE = AppPreference.CurrentMode(this);
	     _modeButton.setText(MODE);
	     
 	 	translationRules = new TranslationRules(this);
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
				    	translationRules.setMode(MODE);				    	
				    	Translator.setBase(translationRules.getClassLoader());
						Translator.setMode(MODE);
						outputText  = Translator.translate(_inputText.getText().toString());
						
				     	} catch (Exception e) {
							 Log.e("ApertiumActivity.TranslationRun",e.getMessage()+"MODE ="+MODE+";InputText = "+_inputText.getText());
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
			Log.i("ApertiumActivity.onclick","SubmitButton");
			progressDialog = ProgressDialog.show(this, "", "Translating..",  true,false);
			TranslationRun();
		}else if(v.equals(_modeButton) && !MODE.equals("??-??")){
			Log.i("ApertiumActivity.onclick","ModeButton");
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
			    	AppPreference.setCurrentMode(getApplicationContext(),ModeId[position]);
				    Toast.makeText(getApplicationContext(), ModeTitle[position],   Toast.LENGTH_SHORT).show();
				    MODE = ModeId[position];
					_modeButton.setText(MODE);
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();		
		}
		
		  
	}
	
	
	/* Share text 
	 * Intent to share translated text over other installed application services */
	private void share_text() {
		Log.i("ApertiumActivity.share_text","Started");
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Apertium Translate");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, outputText);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
	
	
}