package org.apertium.android;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apertium.android.helper.AppPreference;
import org.apertium.android.helper.ErrorAlert;
import org.apertium.android.languagepair.TranslationMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DownloadActivity extends Activity {
	public static final String TAG = "DownloadActivity";
	
    // Used to communicate state changes in the DownloaderThread
    public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
    public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
    public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
    public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
    public static final int MESSAGE_CONNECTING_STARTED = 1004;
    public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;
    
 // constants
    private static final int DOWNLOAD_BUFFER_SIZE = 4096;
    private int totalReadInKB = 0;
	
    ProgressDialog progressDialog;
    private DownloadActivity thisActivity;
    
  
    
    private ListView listView;
    private Button _submitButton;
    private String []LIST = null;
    private String []Address = null;
    
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.svnlayout);
	    listView  = (ListView) findViewById(R.id.listView1);
	    _submitButton 	= (Button) findViewById(R.id.button1);
	    thisActivity = this;
	    
	    progressDialog = ProgressDialog.show(thisActivity, "", "Fetching list",  true,false);
	    DownloadRun("https://apertium.svn.sourceforge.net/svnroot/apertium/trunk/",AppPreference.TEMP_DIR());
		
	}

	
	

	private void DownloadRun(final String Source,final String Target){
	    Thread t = new Thread() {
	        @Override
	        public void run() {
				URL url;
				URLConnection conn;
				int fileSize, lastSlash;
				String fileName;
				BufferedInputStream inStream;
				BufferedOutputStream outStream;
				File outFile;
				FileOutputStream fileStream;
				Message msg;
	                
            
				msg = Message.obtain();
				msg.what = MESSAGE_CONNECTING_STARTED;
				handler.sendMessage(msg);
	                try
	                {
	                        url = new URL(Source);
	                        conn = url.openConnection();
	                        conn.setUseCaches(false);
	                        fileSize = conn.getContentLength();
	                        
	                        // get the filename
	                        lastSlash = url.toString().lastIndexOf('/');
	                        fileName = "file.txt";
	                        if(lastSlash >=0)
	                        {
	                                fileName = url.toString().substring(lastSlash + 1);
	                        }
	                        if(fileName.equals(""))
	                        {
	                                fileName = "file.txt";
	                        }
	                        
	                        // notify download start
	                        int fileSizeInKB = fileSize / 1024;
	                
	                        msg = Message.obtain();
	        	            msg.what = MESSAGE_DOWNLOAD_STARTED;
	        	            handler.sendMessage(msg);
	                        
	                        // start download
	                        inStream = new BufferedInputStream(conn.getInputStream());
	                        outFile = new File(Target + "/" + fileName);
	                        fileStream = new FileOutputStream(outFile);
	                        outStream = new BufferedOutputStream(fileStream, DOWNLOAD_BUFFER_SIZE);
	                        byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
	                        int bytesRead = 0, totalRead = 0;
	                        while(!isInterrupted() && (bytesRead = inStream.read(data, 0, data.length)) >= 0)
	                        {
	                                outStream.write(data, 0, bytesRead);
	                                
	                                // update progress bar
	                                totalRead += bytesRead;
	                                int totalReadInKB = totalRead / 1024;
	                                msg = Message.obtain(thisActivity.handler,MESSAGE_UPDATE_PROGRESS_BAR,totalReadInKB,0);
	    	        	            //msg.what = MESSAGE_UPDATE_PROGRESS_BAR;
	    	        	            handler.sendMessage(msg);
	                        }
	                        
	                        outStream.close();
	                        fileStream.close();
	                        inStream.close();
	                        
	                        if(isInterrupted())
	                        {
	                                // the download was canceled, so let's delete the partially downloaded file
	                                outFile.delete();
	                        }
	                        else
	                        {
	                                // notify completion
	                               	msg = Message.obtain();
	    	        	            msg.what = MESSAGE_DOWNLOAD_COMPLETE;
	    	        	            handler.sendMessage(msg);
	                        }
	                }
	                catch(MalformedURLException e)
	                {
	                	 	msg = Message.obtain(thisActivity.handler,MESSAGE_ENCOUNTERED_ERROR,e.toString());
	        	            handler.sendMessage(msg);
	                }
	                catch(FileNotFoundException e)
	                {
	                     
	                	msg = Message.obtain(thisActivity.handler,MESSAGE_ENCOUNTERED_ERROR,e.toString());
	        	            handler.sendMessage(msg);
	                }
	                catch(Exception e)
	                {
	                	msg = Message.obtain(thisActivity.handler,MESSAGE_ENCOUNTERED_ERROR,e.toString());
	        	        handler.sendMessage(msg);
	                }
	        }
	    };
	    t.start();
	}
	
	
	private void run2(){	
		//progressDialog.setMessage("Writing database");
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	
	        	
	        	String input="";
	        	StringBuffer buf = new StringBuffer();	
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
		        		LIST[i] = Content.get(i+1);
		        		Address[i] = ContentAddress.get(i+1);
		        	}
		        	

		        	is.close();	
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        		
	            Message msg = Message.obtain();
	            msg.what = 2;
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
	        case   MESSAGE_DOWNLOAD_STARTED : 
	        	
	        	Log.i(TAG,"Download started"); 
	        	break;
	        case   MESSAGE_UPDATE_PROGRESS_BAR : 
	        	int currentProgress = msg.arg1;
               // progressDialog.setProgress(currentProgress);
                Log.i(TAG,"Progessbar "+currentProgress); 
                break;
	        case   MESSAGE_DOWNLOAD_CANCELED : 
	        	Log.i(TAG,"Download cancel"); 
	        	break;
	        case   MESSAGE_CONNECTING_STARTED : 
	        	Log.i(TAG,"Connecting Started"); 
	        	break;
	        case   MESSAGE_ENCOUNTERED_ERROR : 
	        	progressDialog.dismiss();
	        	String error = (String)msg.obj;
	        	Log.e(TAG,error); 
	        	
	           /* final AlertDialog.Builder b = new AlertDialog.Builder(thisActivity);
	            b.setIcon(android.R.drawable.ic_dialog_alert);
	         
	            b.setMessage("Error :"+error);
	            b.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	thisActivity.finish();	                    	
	                    }
	            });

	            b.show();*/
	        	
	        	
	        	
	        	
	        	break;
	        	
	        case   MESSAGE_DOWNLOAD_COMPLETE : 
	        	//progressDialog.dismiss();
	        	progressDialog.setMessage("Generating view");
	        	run2();
	        	Log.i(TAG,"Download complete"); 
	        	break;
	        	
	        case 2:
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
	    			    _submitButton.setText("Download "+LIST[position]);	    			    
	    			}
	    	    });
	        	
	        	
	        	
	     	    break;
	        }
	    }
	};
}
