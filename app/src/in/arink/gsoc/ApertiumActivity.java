package in.arink.gsoc;

import org.apertium.port.Port;
import org.apertium.transfer.generated.GeneratedTransferBase;


import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.ClipboardManager;
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
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import in.arink.gsoc.Filemanager;
import in.arink.gsoc.DB.DatabaseHandler;
import in.arink.gsoc.DB.Language;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApertiumActivity extends Activity implements OnClickListener{

	private static String MODE;
	private static String[] MODES = {"",""};
	private static String DEFAULT = "eo-en";
	private String outputText = null;
	private EditText _inputText;
	private Button _submitButton,_modeButton;
	private TextView _outputText;
	private ProgressDialog progressDialog;
	private DatabaseHandler DB;
	private SharedPreferences settings;
	private  Map<String, Class<?>> RULECLASS;
	
	public static final String PREFS_NAME = "Apertium.Pref";
	private String RulePackage = "in.arink.rule";



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = ProgressDialog.show(this, "", "Installing language pair...");
		run1();
		initView();
	}
	
	
	  @Override
	    protected void onResume() {
	        super.onResume();

	        MODE = settings.getString("mode12", DEFAULT );
	        DEFAULT = MODE;
	        MODES[0] = MODE;
	        String[] S = MODE.split("-");
	        MODES[1] = S[1]+"-"+S[0];
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
	      case R.id.install:
	    	  		Toast.makeText(this, "This is under construction",Toast.LENGTH_SHORT).show();	
				 // Intent myIntent2 = new Intent(ApertiumActivity.this, FileChooser.class);
				 // ApertiumActivity.this.startActivity(myIntent2);
				  return true;
	      case R.id.share:
	    	  	share_text();
	    	  	return true;
	      default:
	            return super.onOptionsItemSelected(item);
	      }
	}

	private void initView() {
		setContentView(R.layout.main);
		_inputText = (EditText) findViewById(R.id.inputtext);
		_submitButton = (Button) findViewById(R.id.submit);
		_outputText = (TextView) findViewById(R.id.outputtext);
		_modeButton = (Button) findViewById(R.id.button1);
		
		_submitButton.setOnClickListener(this);
		_modeButton.setOnClickListener(this);
		
	     settings = getSharedPreferences(PREFS_NAME, 0);
	     MODE = settings.getString("mode12", DEFAULT );
	     _modeButton.setText(MODE);
	     DB = new DatabaseHandler(this.getBaseContext());
	}
	

	private void run1(){
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	if(isInstalled(DEFAULT)==false){
	    			InstallLanguage(DEFAULT);
	    		}	        	
	            Message msg = Message.obtain();
	            msg.what = 1;
	            handler.sendMessage(msg);
	        }
	    };
	    t.start();
	}
	
	
	private Map<String, Class<?>> Loadclass(String Mode) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        final String libPath = Environment.getExternalStorageDirectory() + "/apertium/"+DEFAULT+"/rules.jar";
        final File tmpDir = getDir("dex", 0);
        String classpath = RulePackage+"."+Mode.replace("-", "_");
        
        final DexClassLoader classloader = new DexClassLoader(libPath, tmpDir.getAbsolutePath(), null, this.getClass().getClassLoader());
          
        Map<String, Class<?>> ruleclass = new HashMap<String, Class<?>>();
        
        
        List<String> ruleList = DB.getAllRulesbyMode(Mode);
        int len=ruleList.size();
        for(int i=0;i<len;i++){
        	String r1 = ruleList.get(i);
        	String r2 = classpath+"."+r1;
        	Class<?> c = classloader.loadClass(r2);
        	String r3 = r1.substring(Mode.length()+1); 
        	ruleclass.put(r3,c);
        	System.err.print(r3);
        }
        
        return ruleclass;

	}
	
	private void run2(){
	    Thread t = new Thread() {
	        @Override
	        public void run() {
		   		 String inputText = _inputText.getText().toString();		 
				 if (!TextUtils.isEmpty(inputText)) {
					 	outputText = "";
			      
					
				     try {
				
				    	 	RULECLASS = Loadclass(MODE);
						         
							Port.setAddresss("/sdcard/apertium/"+DEFAULT,RULECLASS);
							outputText = Port.get(_inputText.getText().toString(), MODE);
	

				        } catch (Exception e) {
				            e.printStackTrace();
				        }
					 
	
				}
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
	        case 1:
	        	progressDialog.dismiss();
	            break;
	        case 2:
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
			run2();
		}else if(v.equals(_modeButton)){
			//Toast.makeText(this, "This is under construction",Toast.LENGTH_SHORT).show();	 
						
			if(MODE.equals(MODES[0])){
				MODE = MODES[1];
			}else{
				MODE = MODES[0];
			}
			_modeButton.setText(MODE);
		}
	}
	
	

	
	/** To copy data files on sdcard */
	private boolean isInstalled(String pair){
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		File langDirmode = new File(extStorageDirectory +"/apertium/"+pair+"/modes");
		if(!langDirmode.exists()){
			return false;
		}
		return true;			
	}
	
	private void InstallLanguage(String pair) {
	    AssetManager assetManager = getAssets();
	    String[] files = null;	    
	    String newFolder = "/"+pair;
	    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	    File baseDir = new File(extStorageDirectory +"/apertium");
	    File langDir = new File(extStorageDirectory +"/apertium/"+ newFolder);
	    File langDirmode = new File(extStorageDirectory +"/apertium/"+newFolder+"/modes");
	    if(!baseDir.exists()){
	    	baseDir.mkdir();	
	    	langDir.mkdir();
	    	langDirmode.mkdir();
	    }else if(!langDir.exists()){
	    	langDir.mkdir();	    	
	    	langDirmode.mkdir();
	    }else if(!langDirmode.exists()){
	    	langDirmode.mkdir();
	    }
	    
	    try {
	        files = assetManager.list(pair);
	    } catch (IOException e) {
	        Log.e("tag1", e.getMessage());
	    }
	    for(String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(pair+"/"+filename);
	          out = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/apertium/"+newFolder+"/" + filename);
	          Filemanager.copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(Exception e) {
	            Log.e("tag2", e.getMessage());
	        }       
	    }
	    
	    
	    InputStream in = null;
	    OutputStream out = null;
        try {
	          in = assetManager.open(pair+"/eo-en.mode");
	          out = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/apertium/"+newFolder+"/modes/eo-en.mode");
	          Filemanager.copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          in = assetManager.open(pair+"/en-eo.mode");
	          out = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/apertium/"+newFolder+"/modes/en-eo.mode");
	          Filemanager.copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(Exception e) {
	            Log.e("tag2", e.getMessage());
	        }
        
    	Language L = new Language(0,"eo-en","en-eo","Esperanto-English","English-Esperanto",null);
    	DB.addLanuagepair(L);
    	String[] Rules1 = {"eo_en_t1x","eo_en_t2x","eo_en_t3x","eo_en_antaux_t2x"};
    	String[] Rules2 = {"en_eo_t1x","en_eo_t2x","en_eo_t2x","en_eo_genitive_t1x"};
    	DB.addRules("eo-en",Rules1);
    	DB.addRules("en-eo",Rules2);
	}
	
	
	private void share_text() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Apertium Translate");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, outputText);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
	
	
}