package in.arink.gsoc;

import in.arink.gsoc.DB.DatabaseHandler;
import in.arink.gsoc.DB.Language;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InstallActivity extends Activity implements OnClickListener {

	private Button _submitButton,_cancelButton;
	private TextView text1,text2;
	private DatabaseHandler DB;
	
	private Language L;
	private File tempDir;
	private String path = "-1";
	private String extStorageDirectory;
	private int step = 0;
	
	private ProgressDialog progressDialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Bundle extras = getIntent().getExtras();
	    path = extras.getString("filepath");
		DB = new DatabaseHandler(this.getBaseContext());
	    setContentView(R.layout.install);
		text1 = (TextView) findViewById(R.id.textView1);
		text2 = (TextView) findViewById(R.id.textView2);
		_submitButton = (Button) findViewById(R.id.button1);
		_cancelButton = (Button) findViewById(R.id.button2);
		L = new Language();
		text1.setText("Package");
		text2.setText(path);
		_submitButton.setText("Unzip");
		
		extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		
		tempDir = new File(extStorageDirectory +"/apertium/temp");
		
		_submitButton.setOnClickListener(this);
		_cancelButton.setOnClickListener(this);
		

	
	}
	
	@Override
	public void onClick(View v) {
		if (v.equals(_submitButton)){	
			if(step == 0){
				//progressDialog = ProgressDialog.show(this, "", "Unpacking files..",  true,false);	
				//String url = urlField.getText().toString();
			//	Log.d("InstallApp", "install: " + url);
				Intent intent = new Intent(InstallActivity.this, null);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(path), "application/vnd.android.package-archive");

				startActivity(intent);
			
			//	run1();
				
			}else{
				if(install(tempDir,L.getPath())){
					Intent myIntent = new Intent(InstallActivity.this, ApertiumActivity.class);
			    	startActivity(myIntent);
				}else{
					 Toast.makeText(getApplicationContext(),"Cannot be install",   Toast.LENGTH_SHORT).show();
				}
			}
		}else if(v.equals(_cancelButton)){
			if(step > 0) discard(tempDir);
		}
	}
	
	
	private void run1(){
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	            //First Step unzip
	        	try {
					unzip(path);
				} catch (IOException e) {
					text1.setText("Error!");
					text2.setText("Package coruppted");
					e.printStackTrace();
				}
	            // finished first step
	            Message msg = Message.obtain();
	            msg.what = 1;
	            handler.sendMessage(msg);
	        }
	    };
	    t.start();
	}
	
	private void run2(){
	    Thread t = new Thread() {
	        @Override
	        public void run() {
	        	//Second parshing XML
	        	try {
					getEventsFromAnXML();
				} catch (XmlPullParserException e) {
					text1.setText("Error!");
					text2.setText("Install file coruppted");
					e.printStackTrace();
				} catch (IOException e) {
					text1.setText("Error!");
					text2.setText("Install file missing");
					e.printStackTrace();
				}
	        	//finished second
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
	            run2();
	            break;
	        case 2:
	        	text2.setText(L.getLang12()+"\n"+L.getLang21());
	        	_submitButton.setText("Install");
	        	step = 1;
	        	progressDialog.dismiss();	        	
	            break;
	        }
	    }
	};
	
	
	private void unzip(String path) throws IOException {
		  	String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		    File baseDir = new File(extStorageDirectory +"/apertium");
		    File tempDir = new File(extStorageDirectory +"/apertium/temp");		    
		    if(!baseDir.exists()){
		    	baseDir.mkdir();	
		    	tempDir.mkdir();
		    }else if(!tempDir.exists()){
		    	tempDir.mkdir();	   
		    }
		    
	    	InputStream is = new FileInputStream(path);
	    	ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
	    	try {
			     ZipEntry ze;
			     while ((ze = zis.getNextEntry()) != null) {  
			    	 String name = ze.getName();
			         if(ze.isDirectory()) { 
			        	 File f = new File(tempDir+"/"+name); 
			        	 if(!f.isDirectory()) { 
							  f.mkdirs(); 
			        	 } 
			           } else { 
			        	 FileOutputStream fout = new FileOutputStream(tempDir+"/"+ name); 
			             for (int c = zis.read(); c != -1; c = zis.read()) { 
			            	 fout.write(c); 
			             } 
			           }
			     }
     		} finally {
     			zis.close();
	      	}       
	}


	private void getEventsFromAnXML() throws XmlPullParserException, IOException{	
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        String tag = "";
  
        File file = new File(extStorageDirectory +"/apertium/temp/install.xml"); 
        FileInputStream fis = new FileInputStream(file);
        xpp.setInput(new InputStreamReader(fis));
        int eventType = xpp.getEventType();
       
        eventType = xpp.next();
      	tag = xpp.getName();
        eventType = xpp.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
         if(eventType == XmlPullParser.START_DOCUMENT) {
         } else if(eventType == XmlPullParser.START_TAG) {
         	 tag = xpp.getName();
             eventType = xpp.next();
             String text = xpp.getText();
             if(tag.equalsIgnoreCase("mode12")){
               	 L.setMode12(text);
             }else if(tag.equalsIgnoreCase("mode21")){
               	 L.setMode21(text);
             }else if(tag.equalsIgnoreCase("lang12")){
            	 L.setLang12(text);
             }else if(tag.equalsIgnoreCase("lang21")){            	 
            	 L.setLang21(text);
             }             
         } else if(eventType == XmlPullParser.END_TAG) {
         }
         eventType = xpp.next();
        }        
        L.setPath(extStorageDirectory+"/apertium/"+L.getMode12());
	}
	
	Boolean install(File dir,String newpath){
		File file = new File(newpath);
		if(dir.renameTo(file)){
			DB.addLanuagepair(L);
			return true;
		}
		return false;
	}
	
	
	void discard(File dir){
	    if (dir.isDirectory()){
	        for (File child : dir.listFiles())
	            discard(child);
	    }
	    dir.delete();
	}

}
