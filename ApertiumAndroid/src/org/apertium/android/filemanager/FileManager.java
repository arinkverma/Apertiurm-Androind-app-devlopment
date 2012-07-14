/**
 *
 * @author Arink Verma
 */

package org.apertium.android.filemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apertium.android.helper.AppPreference;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FileManager {
	
	static String TAG = "FileManager";
	
	public static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	
	
	public static void downloadFile(String source, String target) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new URL(source).openStream());
		java.io.FileOutputStream fos = new java.io.FileOutputStream(target);
		java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
		byte data[] = new byte[1024];
		while(in.read(data,0,1024)>=0)
		{
			bout.write(data);
		}
		bout.close();
		in.close();
	}
	
	
	public static void setDIR(){
	    File baseDir = new File(AppPreference.BASE_DIR());
	    File tempDir = new File(AppPreference.TEMP_DIR());		    
	    if(!baseDir.exists()){
	    	baseDir.mkdir();	
	    	tempDir.mkdir();
	    }else if(!tempDir.exists()){
	    	tempDir.mkdir();	   
	    }
	}
	
	
	public static void unzip(String path) throws IOException {		  
	    File baseDir = new File(AppPreference.BASE_DIR());
	    File tempDir = new File(AppPreference.TEMP_DIR());		    
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




	public static Boolean move(String oldpath,String newpath){
		File dir = new File(oldpath);
		File file = new File(newpath);
		if(dir.renameTo(file)){
			return true;
		}
		return false;
	}


	public static void remove(File dir){
	    if (dir.isDirectory()){
	        for (File child : dir.listFiles())
	        	remove(child);
	    }
	    dir.delete();
	}
	
	
	
	/* Download fucntion with handle communication */
	
    // Used to communicate state changes in the DownloaderThread
    public static final int MESSAGE_DOWNLOAD_STARTED 		= 1000;
    public static final int MESSAGE_DOWNLOAD_COMPLETE 	= 1001;
    public static final int MESSAGE_UPDATE_PROGRESS_BAR 	= 1002;
    public static final int MESSAGE_DOWNLOAD_CANCELED 	= 1003;
    public static final int MESSAGE_CONNECTING_STARTED 	= 1004;
    public static final int MESSAGE_ENCOUNTERED_ERROR 	= 1005;
    // constants
    public static final int DOWNLOAD_BUFFER_SIZE = 4096;
    
	public static void DownloadRun(final String Source,final String Target,final Handler handler){
	    Thread t = new Thread() {
	        @Override
	        public void run() {
				URL url;
				URLConnection conn;
				int fileSize, lastSlash;
				String ModifiedSince = null;
				String fileName;
				BufferedInputStream inStream;
				BufferedOutputStream outStream;
				File outFile;
				FileOutputStream fileStream;
				Message msg;
	                
            
				msg = Message.obtain();
				msg.what = MESSAGE_CONNECTING_STARTED;
				handler.sendMessage(msg);
	                try {
	                        url = new URL(Source);
	                        conn = url.openConnection();
	                        conn.setUseCaches(false);
	                        fileSize = conn.getContentLength();
	                        ModifiedSince = conn.getLastModified()+"";           
	                        // get the filename
	                        lastSlash = url.toString().lastIndexOf('/');
	                        fileName = "file.txt";
	                        if(lastSlash >=0) {
	                                fileName = url.toString().substring(lastSlash + 1);
	                        }
	                        if(fileName.equals("")) {
	                                fileName = "file.txt";
	                        }
	                        
	                        // notify download start
	                        int fileSizeInKB = fileSize / 1024;
	                        msg = Message.obtain(handler, MESSAGE_DOWNLOAD_STARTED, fileSizeInKB , 0, ModifiedSince);
	        	            handler.sendMessage(msg);
	                        
	                        // start download
	                        inStream = new BufferedInputStream(conn.getInputStream());
	                        outFile = new File(Target + "/" + fileName);
	                        fileStream = new FileOutputStream(outFile);
	                        outStream = new BufferedOutputStream(fileStream, DOWNLOAD_BUFFER_SIZE);
	                        byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
	                        int bytesRead = 0, totalRead = 0;
	                        while(!isInterrupted() && (bytesRead = inStream.read(data, 0, data.length)) >= 0) {
	                                outStream.write(data, 0, bytesRead);	                                
	                                // update progress bar
	                                totalRead += bytesRead;
	                                int totalReadInKB = totalRead / 1024;
	                                msg = Message.obtain(handler,MESSAGE_UPDATE_PROGRESS_BAR,totalReadInKB,0);
	    	        	            handler.sendMessage(msg);
	                        }
	                        
	                        outStream.close();
	                        fileStream.close();
	                        inStream.close();
	                        
	                        if(isInterrupted()) {
	                                // the download was canceled, so let's delete the partially downloaded file
	                                outFile.delete();
	                        }
	                        else {
	                                // notify completion
	                               	msg = Message.obtain();
	                               	msg.what = MESSAGE_DOWNLOAD_COMPLETE;	                               
	    	        	            handler.sendMessage(msg);
	                        }
	                } catch(Exception e) {
	                	msg = Message.obtain(handler,MESSAGE_ENCOUNTERED_ERROR,e.toString());
	        	        handler.sendMessage(msg);
	                }
	        }
	    };
	    t.start();
	}
	
	
	public static void FileInfoRun(final String Source,final Handler handler){
	    Thread t = new Thread() {
	        @Override
	        public void run() {
				URL url;
				URLConnection conn;
				int FileSize = 0, lastSlash;
				String ModifiedSince = null;
				String FileName = null;
				
				Message msg;
	                
            
				msg = Message.obtain();
				msg.what = MESSAGE_CONNECTING_STARTED;
				handler.sendMessage(msg);
	                
                try {
					url = new URL(Source);
					conn = url.openConnection();
                    conn.setUseCaches(false);
                    FileSize = conn.getContentLength();
                    ModifiedSince = conn.getLastModified()+"";           
                    // get the filename
                    lastSlash = url.toString().lastIndexOf('/');
                    FileName = "file.txt";
                    if(lastSlash >=0) {
                            FileName = url.toString().substring(lastSlash + 1);
                    }
                    if(FileName.equals("")) {
                            FileName = "untitled";
                    }
				} catch (MalformedURLException e) {
					Log.e(TAG,e+"");
				} catch (IOException e) {
					Log.e(TAG,e+"");
				}
	                        
                // notify download start
                int fileSizeInKB = FileSize / 1024;
                msg = Message.obtain(handler, MESSAGE_DOWNLOAD_STARTED, fileSizeInKB , 0, ModifiedSince);
	            handler.sendMessage(msg);	              
	        }
	    };
	    t.start();
	}
}
