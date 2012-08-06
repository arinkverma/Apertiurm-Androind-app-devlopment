/**
 *
 * @author Arink Verma
 */

package org.apertium.android;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apertium.android.SMS.SMSobject;
import org.apertium.android.SMS.SmsArrayAdapter;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class SMSInboxActivity extends ListActivity {
	String TAG = "SMSInboxActivity";
	private SmsArrayAdapter adapter;
	private SMSobject smsObj;

	ContentResolver mContentResolver = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mContentResolver = getContentResolver();
	    
		fill();
	}
	
	private void fill() {
         this.setTitle(getString(R.string.inbox));
         List<SMSobject>dir = getSms();
        
         Comparator<Object> comparator = Collections.reverseOrder();
         Collections.sort(dir,comparator);
        
         adapter = new SmsArrayAdapter(SMSInboxActivity.this,R.layout.sms_layout,dir);
		 this.setListAdapter(adapter);
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		smsObj = adapter.getItem(position);	
		Intent myIntent = new Intent(SMSInboxActivity.this, ApertiumActivity.class);	
    	myIntent.putExtra("input", smsObj.getBody());
    	startActivity(myIntent);
	}
	
	public List<SMSobject> getSms() {
		Log.i(TAG, "getSMS");
        Uri mSmsQueryUri = Uri.parse("content://sms/inbox");
        List<SMSobject> messages = new ArrayList<SMSobject>();
        Cursor cursor = null;
        try {
           
			cursor = mContentResolver.query(mSmsQueryUri, null, null, null, null);
            if (cursor == null) {
                Log.i(TAG, "cursor is null. uri: " + mSmsQueryUri);
                return messages;
            }

            for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {
                final String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                final String sender = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                final Long date = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
                
                SMSobject s = new SMSobject(body,sender,date);
                messages.add(s);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cursor.close();
        }
        return messages;
    }
}
