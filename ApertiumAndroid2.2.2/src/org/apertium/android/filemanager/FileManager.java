/**
 *
 * @author Arink Verma
 */

package org.apertium.android.filemanager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apertium.android.helper.AppPreference;

public class FileManager {
	
	public static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
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
	
	
}
