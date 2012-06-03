/**
 *
 * @author Arink Verma
 */

package org.apertium.android;

import org.apertium.android.filemanager.FileChooserActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ManageActivity extends ListActivity {
	
	//List menu items
	private String[] Menu = {"Language Pairs","Install New Pair","Preferences"};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, Menu);
	    this.setListAdapter(adapter);
	    ListView lv = getListView();
	    lv.setTextFilterEnabled(true);
	    lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				switch(position){
					case 0:			 
						//Start ModeManage Activity
						ManageActivity.this.startActivity(new Intent(ManageActivity.this,ModeManageActivity.class));
						break;
					case 1:			
						//TODO Option to download from Internet
						//Start FileChooser Activity
						ManageActivity.this.startActivity(new Intent(ManageActivity.this,FileChooserActivity.class));
						break;
				}

			}
	    });
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.manage_menu, menu);
	      return true;
	 }
	
	public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	      case R.id.install:
	    	  Intent myIntent = new Intent(this, FileChooserActivity.class);
	    	  ManageActivity.this.startActivity(myIntent);
	    	  return true;
	      default:
	            return super.onOptionsItemSelected(item);
	      }
	}
	
	
	

}
