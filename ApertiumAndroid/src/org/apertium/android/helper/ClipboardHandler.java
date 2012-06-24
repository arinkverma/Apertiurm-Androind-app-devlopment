package org.apertium.android.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardHandler {
	private Activity activity;
	public ClipboardHandler(Activity thisActivity){
		activity = thisActivity;
		
	}
	
	@SuppressWarnings("deprecation")
	public void putText(String text){
		 int sdk = android.os.Build.VERSION.SDK_INT;
		 if(sdk < 11) {
			 android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
			 clipboard.setText(text);
		 } else {
			 android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE); 
			 android.content.ClipData clip = ClipData.newPlainText("simple text",text);
			 clipboard.setPrimaryClip(clip);
		 }
	}
	
	@SuppressWarnings("deprecation")
	public String getText(){
		String text = null;
		 int sdk = android.os.Build.VERSION.SDK_INT;
		 if(sdk < 11) {
			 android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
			 text =  clipboard.getText().toString();
		 } else {
			 android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE); 
			 text =  clipboard.getText().toString();
		 }
		return text;
	}

}
