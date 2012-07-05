/**
 *
 * @author Arink Verma
 */

package org.apertium.android;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apertium.Translator;
import org.apertium.android.DB.DatabaseHandler;
import org.apertium.android.filemanager.FileManager;
import org.apertium.android.helper.AppPreference;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends Activity implements OnClickListener{
	public static final String TAG = "DownloadActivity";
	

	
    ProgressDialog progressDialog;
    private DownloadActivity thisActivity;
    
    private final int HTML_PARSING_DONE = 2;
    
    private boolean isListLoaded = false;
    
    private ListView listView;
    private Button _submitButton;
    private String []LIST = null;
    private String []Address = null;
    private String toDownload = null;
    private int FILE_SIZE = 0;
    private String ModifiedSince = null;
    private DatabaseHandler DB = null;
    
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.svnlayout);
	    listView  = (ListView) findViewById(R.id.listView1);
	    _submitButton 	= (Button) findViewById(R.id.button1);
	    _submitButton.setOnClickListener(this);
	    thisActivity = this;
	  
	    DB = new DatabaseHandler(this);
	    progressDialog = new ProgressDialog(thisActivity);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    progressDialog.setTitle("Downloading\nSVN List");
	    progressDialog.setCancelable(false);
	    progressDialog.show();
                  
	    FileManager.DownloadRun(AppPreference.getSVN(),AppPreference.TEMP_DIR(),thisActivity.handler);
		
	}

	
	private void ParseHtmlRun(){	
	    Thread t = new Thread() {
	        @Override
	        public void run() {	        	
	        	String input="";
	        	List<String>  Content = new ArrayList<String>();
	        	List<String>  ContentAddress = new ArrayList<String>();
	        	InputStream is;
				try {
					is = new FileInputStream(AppPreference.TEMP_DIR()+"/file.txt");
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		        	if (is!=null) {							
		        		while ((input = reader.readLine()) != null) {	
		        			String pattern 	= "<li><a href=\"|\">|</a></li>";
		        			String[] updated = input.split(pattern);
		        			if(updated.length>2){
		        				Log.e(updated[1],updated[2] + "\n" );
		        				Content.add(updated[2]);
		        				ContentAddress.add(updated[1]); 		        				
		        			}		        			
		        		}				
		        	}	
		        	
		           	LIST = new String[Content.size()-1];
		        	Address = new String[Content.size()-1];
		        	for(int i=0;i<Content.size()-1;i++){
		        		String LastDate = DB.getLastModifiedDate(Content.get(i+1));
		        		Log.e("arink"," "+LastDate+"|"+ModifiedSince);
		        		if(LastDate != null && LastDate.equals(ModifiedSince)){
		        			LIST[i] = Translator.getTitle(AppPreference.getSVN()+"/"+Content.get(i+1))+" (updated)";
		        		}else{
		        			LIST[i] = Translator.getTitle(AppPreference.getSVN()+"/"+Content.get(i+1));	        			
		        		}
		        		Address[i] = ContentAddress.get(i+1);
		        	}
		        	

		        	is.close();	
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        		
	            Message msg = Message.obtain();
	            msg.what = HTML_PARSING_DONE;
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
	        //HTTP connection started
		        case   FileManager.MESSAGE_CONNECTING_STARTED : 
		        	Log.i(TAG,"Connecting Started"); 
		        	break;
		    
		    //Download started    	
		        case   FileManager.MESSAGE_DOWNLOAD_STARTED : 
		        	FILE_SIZE = (int)msg.arg1;	        	
		        	ModifiedSince = (String) msg.obj;
		        	Log.d("Download","Lastmodified ="+ModifiedSince);
		        	if(isListLoaded == false){
		        		progressDialog.setTitle("Fetching List"+"["+FILE_SIZE+"kb]");
		        		progressDialog.setMessage("Downloading ["+ FILE_SIZE+"kb]");
		        	}else{
		    		    progressDialog.setTitle("Downloading \n"+toDownload+"["+FILE_SIZE+"kb]");
		        		progressDialog.setMessage("Downloading ["+ FILE_SIZE+"kb]");	        		
		        	}
	        	
		        	Log.i(TAG,"Download started "+ FILE_SIZE+"kb"); 
		        	break;
	        	
		    //Getting progress    	
		        case   FileManager.MESSAGE_UPDATE_PROGRESS_BAR : 
		        	int currentProgress = msg.arg1;
		        	int percent = 0;
		        	if(FILE_SIZE>0){
		        		percent = (100*currentProgress)/FILE_SIZE;
		        		progressDialog.setMessage(currentProgress+"kb/"+FILE_SIZE+"kb completed");
		        		progressDialog.setProgress(percent);
		        	}
	                Log.i(TAG,"Progessbar "+currentProgress+"kb ,"+percent+"%"); 
	                break;
	                
            //Download cancel by user    
		        case   FileManager.MESSAGE_DOWNLOAD_CANCELED : 
		        	progressDialog.dismiss();
		        	Log.i(TAG,"Download cancel"); 
		        	break;

		    //Download cancel by Error 
		        case   FileManager.MESSAGE_ENCOUNTERED_ERROR : 
		        	progressDialog.dismiss();
		        	String error = (String)msg.obj;
		        	Log.e(TAG,error); 
		        	
		            final AlertDialog.Builder ErrorDialog = new AlertDialog.Builder(thisActivity);
		            ErrorDialog.setIcon(android.R.drawable.ic_dialog_alert);
		         
		            ErrorDialog.setMessage("Error :"+error);
		            ErrorDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	thisActivity.finish();	                    	
	                    }
		            });
	
		            ErrorDialog.show();
		        	
		            break;
	       
		    //Download Completed  call install activity if package downloaded
		    //parse html if svn list download
	        case   FileManager.MESSAGE_DOWNLOAD_COMPLETE : 
	        	if(isListLoaded == false){
		        	progressDialog.setMessage("Generating view");
		        	ParseHtmlRun();
		        	Log.i(TAG,"Download complete"); 
		        	isListLoaded = true;
	        	}else{
		        	progressDialog.dismiss();
		        	Intent myIntent = new Intent(thisActivity, InstallActivity.class);	
			    	myIntent.putExtra("filepath", AppPreference.TEMP_DIR()+"/"+toDownload);
			    	myIntent.putExtra("filename", toDownload);
			    	myIntent.putExtra("filedate",ModifiedSince);
			    	startActivity(myIntent);
	        	}
	        	break;
	        	
	        
	       //Generate List View after parsing 	
	        case HTML_PARSING_DONE:
	        	progressDialog.dismiss();        	
	        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(thisActivity,android.R.layout.simple_list_item_1, android.R.id.text1, LIST);
	        	listView.setAdapter(adapter);
	        	listView.setTextFilterEnabled(true);
	    	    //Set current mode on click
	        	listView.setOnItemClickListener(new OnItemClickListener() {
	    			@Override
	    			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	    				TextView v = (TextView) view;
	    				Toast.makeText(getApplicationContext(), v.getText(),   Toast.LENGTH_SHORT).show();
	    				toDownload = Address[position];
	    			    _submitButton.setText("Download "+LIST[position]);	    			    
	    			}
	    	    });
	        	
	        	
	        	
	     	    break;
	        }
	    }
	};


	@Override
	public void onClick(View v) {
		if (v.equals(_submitButton)){	
		    progressDialog = new ProgressDialog(thisActivity);
		    progressDialog.setTitle("Downloading\n"+toDownload);
		    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		    progressDialog.setCancelable(false);
		    progressDialog.show();
		    FileManager.DownloadRun(AppPreference.getSVN()+toDownload,AppPreference.TEMP_DIR(),thisActivity.handler);			
		}
	}
}
