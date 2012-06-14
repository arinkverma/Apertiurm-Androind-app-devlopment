package org.apertium.android.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorAlert  extends AlertDialog {

	boolean toshow = false;
	boolean isError = false;
	String Message = "";
	
	public ErrorAlert(final Context context) {
	       super(context);
	       setMessage("Error\n");
	       setButton("Okay", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int whichButton) {
	            	                      	
	               }
	       });
	}
	
	public ErrorAlert(final Context context,String msg) {
       super(context);
       setMessage("Error\n" +msg);
       setButton("Okay", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int whichButton) {
            	                      	
               }
       });
	}
	
	public void AddMessage(String M){		
		Message += M;
		setMessage("Error\n" +Message);
	}
	
	public void ClearMessage(){		
		Message = "";
		setMessage("Error\n");
	}
	
	public void RegisterError(boolean v){
		isError = v;
	}
	
	public boolean isError(){
		return isError;
	}
	
}
