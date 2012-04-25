package in.arink.gsoc.DB;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ApertiumLanugManager.db";
 
/*Language Table */
    // LANG table name
    private static final String TABLE_LANG = "Language";
 
    // LANG Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_mode12 = "mode12";
    private static final String KEY_mode21 = "mode21";
    private static final String KEY_lang12 = "lang12";
    private static final String KEY_lang21 = "lang21";
//    private static final String KEY_path = "path";
    
    private Context context;
    
    public DatabaseHandler(Context context) {
        this.context = context;
    }
 

    public void closeDatabase(){
	      OpenHelper openHelper = new OpenHelper(this.context);
	      openHelper.close();
    }
    
 // Adding new contact
    public void addLanuagepair(Language L) {
	      OpenHelper openHelper = new OpenHelper(this.context);
	      SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_mode12, L.getMode12()); 
        values.put(KEY_mode21, L.getMode21()); 
        values.put(KEY_lang12, L.getLang12()); 
        values.put(KEY_lang21, L.getLang21()); 
//        values.put(KEY_path, L.getPath()); 
        db.insert(TABLE_LANG, null, values);
        db.close();
    }
     
     
    // Getting All LANG
    public List<Language> getAllLanuagepairs() {
	      OpenHelper openHelper = new OpenHelper(this.context);
	      SQLiteDatabase db = openHelper.getReadableDatabase();
    	
        List<Language> LangList = new ArrayList<Language>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LANG;
    	
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                
                int id = Integer.parseInt(cursor.getString(0));
                String m12 = cursor.getString(1);
                String m21 = cursor.getString(2);
                String l12 = cursor.getString(3);
                String l21 = cursor.getString(4);
//                String path = cursor.getString(5);
                Language L= new Language(id,m12,m21,l12,l21,null);
                LangList.add(L);
            } while (cursor.moveToNext());
        }
        db.close();
        return LangList;
    	
    }
     
    // Getting LANG Count
    public int getLanguagesCount() {
    	String countQuery = "SELECT  * FROM " + TABLE_LANG;
	      OpenHelper openHelper = new OpenHelper(this.context);
	      SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close(); 
        return cursor.getCount();    	
    }
   
    // Deleting single contact
    public void deleteLanuagepair(Language L) {
    	 OpenHelper openHelper = new OpenHelper(this.context);
	      SQLiteDatabase db = openHelper.getWritableDatabase();
    	    db.delete(TABLE_LANG, KEY_ID + " = ?",new String[] { String.valueOf(L.getID()) });
    	    db.close();
    }
    
    
    
    public void addLanuagepair(String M,String R) {
	      OpenHelper openHelper = new OpenHelper(this.context);
	      SQLiteDatabase db = openHelper.getWritableDatabase();
	      ContentValues values = new ContentValues();
	      values.put(KEY_Mode, M); 
	      values.put(KEY_RULE, R); 

      db.insert(TABLE_RULES, null, values);
      db.close();
    }
    
    
    /*Rule Table */   
    private static final String TABLE_RULES = "Rules";
    private static final String KEY_RULE_ID = "id";
    private static final String KEY_Mode = "trans_mode";
    private static final String KEY_RULE = "rule";
    
    
    public void addRule(String M,String R) {
	      OpenHelper openHelper = new OpenHelper(this.context);
	      SQLiteDatabase db = openHelper.getWritableDatabase();
	      ContentValues values = new ContentValues();
	      values.put(KEY_Mode, M); 
	      values.put(KEY_RULE, R);
	      db.insert(TABLE_RULES, null, values);
	      db.close();
    }
    
    public void addRules(String M,String[] R) {
	      OpenHelper openHelper = new OpenHelper(this.context);
	      SQLiteDatabase db = openHelper.getWritableDatabase();
	      int len = R.length;
	      for(int i=0;i<len;i++){
		      ContentValues values = new ContentValues();
		      values.put(KEY_Mode, M);
		      values.put(KEY_RULE, R[i]);
		      db.insert(TABLE_RULES, null, values);
	      }
	      db.close();
    }
    
    public List<String> getAllRulesbyMode(String mode) {
	      OpenHelper openHelper = new OpenHelper(this.context);
	      SQLiteDatabase db = openHelper.getReadableDatabase();
  	
	      List<String> List = new ArrayList<String>();
	      // Select All Query
	      String selectQuery = "SELECT  * FROM " + TABLE_RULES+" WHERE "+KEY_Mode+" = '"+mode+"'";
  	
	      Cursor cursor = db.rawQuery(selectQuery, null);
   
      // looping through all rows and adding to list
      if (cursor.moveToFirst()) {
          do {              
              int id = Integer.parseInt(cursor.getString(0));
              String m = cursor.getString(1);
              String r = cursor.getString(2);
              List.add(r);
          } while (cursor.moveToNext());
      }
      db.close();
      return List;  	
  }
    
    private static class OpenHelper extends SQLiteOpenHelper {
    	 
        OpenHelper(Context context) {
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
   
        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {	
            db.execSQL("CREATE TABLE " + TABLE_LANG + "(id INTEGER PRIMARY KEY, mode12 TEXT,mode21 TEXT,lang12 TEXT,lang21 TEXT)");
            db.execSQL("CREATE TABLE " + TABLE_RULES + "(id INTEGER PRIMARY KEY, trans_mode TEXT, rule TEXT)");
         }
     
        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANG);
     
            // Create tables again
            onCreate(db);
        }
     }
}