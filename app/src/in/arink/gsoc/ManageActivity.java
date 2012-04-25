package in.arink.gsoc;

import in.arink.gsoc.DB.DatabaseHandler;
import in.arink.gsoc.DB.Language;

import java.util.List;



import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ManageActivity extends ListActivity {
	private DatabaseHandler DB;
	private List<Language> L;
	public static final String PREFS_NAME = "Apertium.Pref";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		DB = new DatabaseHandler(this.getBaseContext());
	    L = DB.getAllLanuagepairs();
	    int len = L.size();
	    String[] lang = new String[len];
	    final String[] mode12 = new String[len];
	    for (int i = 0; i < L.size(); i++) {
	    	Language l = L.get(i);
	        lang[i] = l.getLang12();
	        mode12[i] = l.getMode12();
	    }
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, lang);

	    this.setListAdapter(adapter);

	    ListView lv = getListView();
	    lv.setTextFilterEnabled(true);

	    lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				TextView v = (TextView) view;
				Toast.makeText(getApplicationContext(), v.getText(),   Toast.LENGTH_SHORT).show();

				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			      SharedPreferences.Editor editor = settings.edit();
			      editor.putString("mode12", mode12[position]);
			      // Commit the edits!
			      editor.commit();
			      finish();
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
	    	  	//InstallLanguage("apertium-eo-en","eo-en");
	            //Toast.makeText(this, "The default EN-EO language pair have been installed ",Toast.LENGTH_SHORT).show();
	            return true;
	      default:
	            return super.onOptionsItemSelected(item);
	      }
	}
	
	
	

}
