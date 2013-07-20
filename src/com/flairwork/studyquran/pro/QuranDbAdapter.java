package com.flairwork.studyquran.pro;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;
import android.util.Log;

public class QuranDbAdapter {

    //translation_lang

    public static final String TRANS_TABLE = "translation_lang";
    
    //public static final String TRANS_ROWID = "rowid";
    public static final String TRANS_ID = "_id";
    public static final String TRANS_TRANSLATION_NAME = "translation_name";
    public static final String TRANS_LANG_CODE = "lang_code";
    public static final String TRANS_LANG_NAME = "lang_name";
    public static final String TRANS_ACTIVE = "active";
    


    //chapter
    
    public static final String CHAPTERS_TABLE = "chapter";
    
    //public static final String CHAPTER_ROWID = "rowid";
    public static final String CHAPTER_ID = "_id";
    public static final String CHAPTER_CHAPTER_ID = "chapter_id";
    public static final String CHAPTER_NAME = "name";
    public static final String CHAPTER_TR_NAME = "tr_name";
    public static final String CHAPTER_EN_NAME = "en_name";
    public static final String CHAPTER_TYPE = "type";
    public static final String CHAPTER_LANG_CODE = "lang_code";
    public static final String CHAPTER_VERSE_COUNT = "verse_count";
    public static final String CHAPTER_START = "start";
    public static final String CHAPTER_ORDER = "orders";
    public static final String CHAPTER_RUKUS = "rukus";
    

    //verse
    
    public static final String VERSES_TABLE = "verse";
    
    //public static final String VERSE_ROWID = "rowid";
    public static final String VERSE_ROWID = "_id";
    public static final String VERSE_DATABASEID = "translation_id";
    public static final String VERSE_CHAPTERID = "chapter_id";
    public static final String VERSE_VERSEID = "verse_id";
    public static final String VERSE_TEXT = "verse_text";
    public static final String VERSE_ARABIC_TEXT = "arabic_text";
    
    //book_mark
    
    private static final String BOOKMARK_TABLE = "book_mark";
    
    //public static final String BOOKMARK_ROWID = "rowid";
    public static final String BOOKMARK_ID = "_id";
    public static final String BOOKMARK_NAME = "name";
    public static final String BOOKMARK_CHAPTERID = "chapter_id";
    public static final String BOOKMARK_VERSEID = "verse_id";
    public static final String BOOKMARK_POSITIONID = "position_id";
    public static final String BOOKMARK_ACCOUNTID = "account_id";
    public static final String BOOKMARK_ISDELETED = "is_deleted";
    public static final String BOOKMARK_DATECREATED = "date_created";
    public static final String BOOKMARK_DATEMODIFIED = "date_modified";
    public static final String BOOKMARK_TEXT = "bookmark_text";
    
    //search_term
    
    private static final String SEARCH_TABLE = "search_term";
    
    //public static final String SEARCH_ROWID = "rowid";
    public static final String SEARCH_ID = "_id";
    public static final String SEARCH_TEXT = "search_text";
    public static final String SEARCH_LANG_CODE = "lang_code";
    public static final String SEARCH_VERSE_COUNT = "verse_count";
    public static final String SEARCH_ACCOUNTID = "account_id";
    public static final String SEARCH_ISDELETED = "is_deleted";
    public static final String SEARCH_DATECREATED = "date_created";
    public static final String SEARCH_DATEMODIFIED = "date_modified";
    
    //index
    private static final String INDEX_TABLE = "index";
    
    //public static final String INDEX_ROWID = "_id";
    public static final String INDEX_ID = "_id";
    public static final String INDEX_INDEX_TITLE = "index_title";
    public static final String INDEX_INDEX_CODE = "index_code";
    public static final String INDEX_TAGS = "tags";
    public static final String INDEX_DESC = "desc";
    public static final String INDEX_LANG_CODE = "lang_code";
    public static final String INDEX_VIEW_COUNT = "view_count";
    public static final String INDEX_VOTE_UP = "vote_up";
    public static final String INDEX_VOTE_DOWN = "vote_down";
    public static final String INDEX_STATUS = "status";
    public static final String INDEX_CATEGORY_ID = "category_id";
    public static final String INDEX_VERSE_COUNT = "verse_count";
    public static final String INDEX_ISDELETED = "is_deleted";
    public static final String INDEX_DATECREATED = "date_created";
    public static final String INDEX_DATEMODIFIED = "date_modified";

    //index_item
	private static final String INDEX_ITEM_TABLE = "index_item";
    
    //public static final String INDEX_ITEM_ROWID = "rowid";
    public static final String INDEX_ITEM_ID = "_id";
    public static final String INDEX_ITEM_INDEX_ID = "index_id";
    public static final String INDEX_ITEM_CHAPTER_ID = "chapter_id";
    public static final String INDEX_ITEM_VERSE_ID = "verse_id";
    public static final String INDEX_ITEM_ORDER = "orders";
    public static final String INDEX_ITEM_VOTE_UP = "vote_up";
    public static final String INDEX_ITEM_VOTE_DOWN = "vote_down";

    //topic
    private static final String TOPIC_TABLE = "topic";
    
    //public static final String TOPIC_ROWID = "_id";
    public static final String TOPIC_ID = "_id";
    public static final String TOPIC_TOPIC_TITLE = "topic_title";
    public static final String TOPIC_TAGS = "tags";
    public static final String TOPIC_DESC = "desc";
    public static final String TOPIC_LANG_CODE = "lang_code";
    public static final String TOPIC_VIEW_COUNT = "view_count";
    public static final String TOPIC_VOTE_UP = "vote_up";
    public static final String TOPIC_VOTE_DOWN = "vote_down";
    public static final String TOPIC_STATUS = "status";
    public static final String TOPIC_CATEGORY_ID = "category_id";
    public static final String TOPIC_ACCOUNT_ID = "account_id";
    public static final String TOPIC_VERSE_COUNT = "verse_count";
    public static final String TOPIC_ISDELETED = "is_deleted";
    public static final String TOPIC_DATECREATED = "date_created";
    public static final String TOPIC_DATEMODIFIED = "date_modified";

    //topic_item
    private static final String TOPIC_ITEM_TABLE = "topic_item";
    
    //public static final String TOPIC_ITEM_ROWID = "rowid";
    public static final String TOPIC_ITEM_ID = "_id";
    public static final String TOPIC_ITEM_TOPIC_ID = "topic_id";
    public static final String TOPIC_ITEM_CHAPTER_ID = "chapter_id";
    public static final String TOPIC_ITEM_VERSE_ID = "verse_id";
    public static final String TOPIC_ITEM_ORDER = "orders";
    public static final String TOPIC_ITEM_VOTE_UP = "vote_up";
    public static final String TOPIC_ITEM_VOTE_DOWN = "vote_down";
    public static final String TOPIC_ITEM_STATUS = "status";
    public static final String TOPIC_ITEM_ACCOUNT_ID = "account_id";
    
    
    private static final String TAG = "QuranDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;
	
	//private ArrayList<Search> searchArray = new ArrayList<Search>();
	//private ArrayList<Bookmark> bookmarkArray = new ArrayList<Bookmark>();
	
    /**
     * Database creation sql statement
     */
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


        }
        
		@Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
	        if(newVersion > oldVersion){
	        	//db.execSQL("DROP TABLE IF EXISTS "+SEARCH_TABLE+" ;");
		        //db.execSQL("DROP TABLE IF EXISTS "+BOOKMARK_TABLE+" ;");
	        }
	    }
    }

	public boolean createDataBase() throws IOException{
		System.out.println("createDataBase()");
		
    	boolean dbExist = checkDataBase(); 
    	
    	if(dbExist){
    		//do nothing - database already exist
        	System.out.println("dbExist: "+dbExist);
        	
    	}else {
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
    		mDbHelper = new DatabaseHelper(mCtx);
    		mDbHelper.getReadableDatabase();
 
        	try {
    			copyDataBase();
    			
    			dbExist = true;
    			
    		} catch (IOException e) {
    			e.printStackTrace();
        		throw new Error("Error copying database");
    		} catch (Exception e) {
        		throw new Error(e.getMessage());
        	}finally{
        		mDbHelper.close();
        	}
    	}
    	return dbExist;
 
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    	System.out.println("checkDataBase()");
    	
		SQLiteDatabase checkDB = null;
    	try{
    		String myPath = Config.DB_PATH + Config.DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		
    	}catch(SQLiteException e){
    		//database does't exist yet.
    		System.out.println("database doesn't exist yet.");
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }

    
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
    	System.out.println("copyDataBase()");
    	
    	//Open your local db as the input stream
    	InputStream myInput =  mCtx.getAssets().open(Config.DATABASE_NAME+".jet");
    	System.out.println("myInput: "+myInput);
    	
    	// Path to the just created empty db
    	String outFileName = Config.DB_PATH + Config.DATABASE_NAME;
    	System.out.println("outFileName: "+outFileName);
    	
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }

    

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public QuranDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
	
    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public QuranDbAdapter open() throws SQLException {
    	/*
    	String myPath = Config.DB_PATH + Config.DATABASE_NAME;
    	mDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	*/
    	if(mDbHelper == null){
	    	mDbHelper = new DatabaseHelper(mCtx);
	        mDb = mDbHelper.getWritableDatabase();
	        //mDb = mDbHelper.getReadableDatabase();
	        System.out.println("open()");
    	}else{
    		mDb = mDbHelper.getWritableDatabase();
	        //mDb = mDbHelper.getReadableDatabase();
	        System.out.println("open()");
    	}
        return this;
    }

    //---closes the database---    
    public synchronized void close() {
        //mDbHelper.close();
    	 if(mDb != null){
    		 mDb.close();
    	 }
    	 //mDb.close();
    }


    public void insert(ContentValues contentValues, String tableName) throws SQLException {
    	mDb.insert(tableName, null, contentValues);
    }
    
    public void runQuery(String query) throws SQLException {
    	mDb.execSQL(query);
    }
    
    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllChapters(String sort) throws SQLException {
    	if(sort.equals("Revelation")){
    		return mDb.rawQuery("SELECT T."+CHAPTER_ID+", T."+CHAPTER_CHAPTER_ID+", T."+CHAPTER_TR_NAME+", T."+CHAPTER_ORDER +", T."+CHAPTER_EN_NAME +", T."+CHAPTER_TYPE +", T."+CHAPTER_VERSE_COUNT +
            		" FROM "+CHAPTERS_TABLE+" T "+
            		//" WHERE T."+CHAPTER_LANG_CODE+" = '"+prefs.getString("DATABASE_LANG_CODE", "en")+"' "+
            		//" WHERE T."+CHAPTER_LANG_CODE+" = '"+prefs.getString("DATABASE_LANG_CODE", "en")+"' "+
            		" ORDER BY T."+CHAPTER_ORDER+" ASC;", null); 
    	}else {
    		return mDb.rawQuery("SELECT T."+CHAPTER_ID+", T."+CHAPTER_CHAPTER_ID+", T."+CHAPTER_TR_NAME+", T."+CHAPTER_ORDER +", T."+CHAPTER_EN_NAME +", T."+CHAPTER_TYPE +", T."+CHAPTER_VERSE_COUNT +
            		" FROM "+CHAPTERS_TABLE+" T "+
            		//" WHERE T."+CHAPTER_LANG_CODE+" = '"+prefs.getString("DATABASE_LANG_CODE", "en")+"' "+
            		//" WHERE T."+CHAPTER_LANG_CODE+" = '"+prefs.getString("DATABASE_LANG_CODE", "en")+"' "+
            		" ORDER BY T."+CHAPTER_CHAPTER_ID+" ASC;", null); 
    	}        
    }
    
    public Cursor fetchAllVerses() throws SQLException {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	return mDb.rawQuery("SELECT "+VERSE_ROWID+", "+VERSE_CHAPTERID+", "+VERSE_VERSEID+", "+VERSE_TEXT+
    			" FROM "+VERSES_TABLE+
    			" WHERE "+VERSE_DATABASEID+" = "+prefs.getString("transPref", Config.DATABASE_ID)+";", null);
    }

	public Cursor getChapterById(Long chapterId) throws SQLException {
		return mDb.rawQuery("SELECT "+CHAPTER_ID+", "+CHAPTER_CHAPTER_ID+", "+CHAPTER_TR_NAME+", "+CHAPTER_ORDER +", "+CHAPTER_EN_NAME +", "+CHAPTER_TYPE +", "+CHAPTER_VERSE_COUNT +
	      		" FROM "+CHAPTERS_TABLE+" " +
        		//" WHERE "+CHAPTER_LANG_CODE+" = "+prefs.getString("transPref", Config.DATABASE_ID)+
        		" WHERE "+CHAPTER_CHAPTER_ID+" = "+chapterId+
        		" ORDER BY "+CHAPTER_CHAPTER_ID+" ASC;", null);
	}
	
	/*
    public Long countChapters() throws SQLException {
    	SQLiteStatement dbCountQuery = mDb.compileStatement("SELECT Count(*) " +
        		" FROM "+CHAPTERS_TABLE);
        		//" WHERE "+CHAPTER_LANG_CODE+" = "+prefs.getString("transPref", Config.DATABASE_ID));
          
        return dbCountQuery.simpleQueryForLong();
    }
    */
    
    public Long countVerses(Long transId) throws SQLException {
    	SQLiteStatement dbCountQuery = mDb.compileStatement("SELECT Count(*) " +
    			" FROM "+VERSES_TABLE+" " +
    			" WHERE "+VERSE_DATABASEID+" = "+transId+";");
    	
    	return dbCountQuery.simpleQueryForLong();
    }
    
    public Long countChapterVerses(Integer chapterId) throws SQLException {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	SQLiteStatement dbCountQuery = mDb.compileStatement("SELECT Count(*) " +
    			" FROM "+VERSES_TABLE+" " +
    			" WHERE "+VERSE_DATABASEID+" = "+prefs.getString("transPref", Config.DATABASE_ID)+
    			" AND "+VERSE_CHAPTERID+" = "+chapterId+" ;");
    	
    	return dbCountQuery.simpleQueryForLong();
    }
    
    public Cursor fetchVerses(long chapterId) throws SQLException {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	return mDb.rawQuery("SELECT T."+VERSE_ROWID+", T."+VERSE_CHAPTERID+", T."+VERSE_VERSEID+", T."+VERSE_TEXT+", T."+VERSE_ARABIC_TEXT+
    			" FROM "+VERSES_TABLE+" T "+
    			" WHERE T."+VERSE_DATABASEID+" = "+prefs.getString("transPref", Config.DATABASE_ID)+
    			" AND T."+VERSE_CHAPTERID+" = "+chapterId+
    			" ORDER BY T."+VERSE_VERSEID+" ASC;", null);
    }
    
    public Cursor getVerseSpecific(String suraId, String verseId) throws SQLException {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	return mDb.rawQuery("SELECT "+VERSE_ROWID+", "+VERSE_CHAPTERID+", "+VERSE_VERSEID+", "+VERSE_TEXT+", "+VERSE_ARABIC_TEXT+
    			" FROM "+VERSES_TABLE+
    			" WHERE "+VERSE_DATABASEID+" = "+prefs.getString("transPref", Config.DATABASE_ID)+
    			" AND "+VERSE_CHAPTERID+" = "+suraId+
    			" AND "+VERSE_VERSEID+" = "+verseId+
    			" ORDER BY "+VERSE_VERSEID+" ASC;", null);
    }
    
    public void addVerse(Long id, Long translation_id, Long chapter_id, Long verse_id, String verse_text, String arabic_text){    	
    	try{
	    	ContentValues verseValues = new ContentValues();
	    	verseValues.put(VERSE_ROWID, id);
	    	verseValues.put(VERSE_DATABASEID, translation_id);
	    	verseValues.put(VERSE_CHAPTERID, chapter_id);
	    	verseValues.put(VERSE_VERSEID, verse_id);	
	    	verseValues.put(VERSE_TEXT, verse_text);
	    	verseValues.put(VERSE_ARABIC_TEXT, arabic_text);
	    	
	    	//mDb.insert(VERSES_TABLE, null, verseValues);
	    	mDb.replace(VERSES_TABLE, null, verseValues);
 
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public Cursor fetchSearches(String searchText) throws SQLException {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	return mDb.rawQuery("SELECT "+VERSES_TABLE+"."+VERSE_ROWID+", "+VERSES_TABLE+"."+VERSE_CHAPTERID+", "+VERSES_TABLE+"."+VERSE_VERSEID+", "+VERSES_TABLE+"."+VERSE_TEXT+
    			" FROM "+VERSES_TABLE+
    			" WHERE "+VERSES_TABLE+"."+VERSE_DATABASEID+" = "+prefs.getString("transPref", Config.DATABASE_ID)+" AND "+VERSES_TABLE+"."+VERSE_TEXT+" LIKE '%"+searchText+"%' ORDER BY "+VERSES_TABLE+"."+VERSE_CHAPTERID+" ASC;", null);
    }
    
    public Cursor fetchAllSearches() throws SQLException {
    	return mDb.rawQuery("SELECT "+SEARCH_ID+", "+SEARCH_TEXT+", "+SEARCH_LANG_CODE+", "+SEARCH_ISDELETED+", "+SEARCH_DATECREATED+", "+SEARCH_DATEMODIFIED+
    			" FROM "+SEARCH_TABLE+
    			//" WHERE "+SEARCH_LANG_CODE+" = '"+prefs.getString("DATABASE_LANG_CODE", "en")+"'"+
    			//" AND "+SEARCH_ISDELETED+" = 0 "+
    			" ORDER BY "+SEARCH_DATEMODIFIED+" DESC;", null);
    }
    
    public Cursor getAllSearches() throws SQLException {
    	return mDb.rawQuery("SELECT "+SEARCH_ID+", "+SEARCH_TEXT+", "+SEARCH_LANG_CODE+", "+SEARCH_ISDELETED+", "+SEARCH_DATECREATED+", "+SEARCH_DATEMODIFIED+
    			" FROM "+SEARCH_TABLE+
    			" ORDER BY "+SEARCH_DATEMODIFIED+" DESC;", null);
    }
    
    public Integer addSearch(String searchText, int verseCount) throws SQLException {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	Cursor checkText = mDb.rawQuery("SELECT "+SEARCH_ID+", "+SEARCH_TEXT+
    			" FROM "+SEARCH_TABLE+ 
    			" WHERE "+SEARCH_TEXT+" = '"+searchText+"' "+
    			" AND "+SEARCH_LANG_CODE+" = '"+prefs.getString("DATABASE_LANG_CODE", Config.DATABASE_LANG_CODE)+"'"+
    			" ORDER BY "+SEARCH_ID+" DESC;", null);
    	
    	if(checkText.getCount()<1){
    		Date today = new Date();
        	ContentValues searchValues = new ContentValues();
        	searchValues.put(SEARCH_TEXT, searchText);
    		searchValues.put(SEARCH_LANG_CODE, prefs.getString("DATABASE_LANG_CODE", Config.DATABASE_LANG_CODE));
    		searchValues.put(SEARCH_VERSE_COUNT, verseCount);
    		searchValues.put(SEARCH_ISDELETED, 0);
    		searchValues.put(SEARCH_DATECREATED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
    		searchValues.put(SEARCH_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
            mDb.insert(SEARCH_TABLE, null, searchValues);
            return 1;
    	}else{
    		return 0;
    	}
    }
    
    public void deleteSearch(Long rowID) throws SQLException {
    	Date today = new Date();
    	ContentValues searchValues = new ContentValues();
    	searchValues.put(SEARCH_ISDELETED, 1);
		searchValues.put(SEARCH_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());

        mDb.update(SEARCH_TABLE, searchValues, SEARCH_ID+"=?", new String[] {Long.toString(rowID)});
    }
    
    public int clearSearch() throws SQLException {
    	Date today = new Date();
    	ContentValues searchValues = new ContentValues();
    	searchValues.put(SEARCH_ISDELETED, 1);
		searchValues.put(SEARCH_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());

        return mDb.update(SEARCH_TABLE, searchValues, null, null);
    }

    public Cursor getAllBookmarks() throws SQLException {
    	return mDb.rawQuery("SELECT "+BOOKMARK_ID+", "+BOOKMARK_NAME+", "+BOOKMARK_CHAPTERID+", "+BOOKMARK_VERSEID+", "+BOOKMARK_POSITIONID+", "+BOOKMARK_ISDELETED+", "+BOOKMARK_DATECREATED+", "+BOOKMARK_DATEMODIFIED+
    			" FROM "+BOOKMARK_TABLE+
    			" ORDER BY "+BOOKMARK_ID+" ASC;", null);
    }
     
    public Cursor fetchBookmarks() throws SQLException {
    	//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	return mDb.rawQuery("SELECT B."+BOOKMARK_ID+", B."+BOOKMARK_NAME+", B."+BOOKMARK_CHAPTERID+", B."+BOOKMARK_VERSEID+", B."+BOOKMARK_POSITIONID+
    			", B."+BOOKMARK_ISDELETED+", B."+BOOKMARK_DATECREATED+", B."+BOOKMARK_DATEMODIFIED+
    			", B."+BOOKMARK_TEXT+
    			" FROM "+BOOKMARK_TABLE+" B "+
    			" WHERE B."+BOOKMARK_ISDELETED+" = 0 "+ 
    			" ORDER BY B."+BOOKMARK_DATEMODIFIED+" DESC;", null);
    }
    
    public void addBookmark(String name, Long suraID, Long verseID, Long positionID, String verseText){    	
    	Date today = new Date();
    	ContentValues bookmarkValues = new ContentValues();
        bookmarkValues.put(BOOKMARK_CHAPTERID, suraID);
        bookmarkValues.put(BOOKMARK_VERSEID, verseID);
        bookmarkValues.put(BOOKMARK_POSITIONID, (verseID-1));
        bookmarkValues.put(BOOKMARK_NAME, name);	
		bookmarkValues.put(BOOKMARK_ISDELETED, 0);
		bookmarkValues.put(BOOKMARK_TEXT, verseText);
		bookmarkValues.put(BOOKMARK_DATECREATED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
		bookmarkValues.put(BOOKMARK_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
		
		name = "("+suraID+":"+verseID+")";
		bookmarkValues.put(BOOKMARK_NAME, name);
		
        mDb.insert(BOOKMARK_TABLE, null, bookmarkValues);
    }
    
    public Cursor getReadPoint() throws SQLException {
    	Cursor result = mDb.rawQuery("SELECT "+BOOKMARK_ID+", "+BOOKMARK_NAME+", "+BOOKMARK_CHAPTERID+", "+BOOKMARK_VERSEID+", "+BOOKMARK_POSITIONID+", "+BOOKMARK_ISDELETED+", "+BOOKMARK_DATECREATED+", "+BOOKMARK_DATEMODIFIED+
    			" FROM "+BOOKMARK_TABLE+
    			" WHERE "+BOOKMARK_POSITIONID+" = -1 ;", null);
    	if(result.getCount() == 0){
    		addReadPoint(2l, 100l, " ");
    		result = mDb.rawQuery("SELECT "+BOOKMARK_ID+", "+BOOKMARK_NAME+", "+BOOKMARK_CHAPTERID+", "+BOOKMARK_VERSEID+", "+BOOKMARK_POSITIONID+" FROM "+BOOKMARK_TABLE+" WHERE "+BOOKMARK_POSITIONID+" = -1 ;", null);
    	}
    	return result;
    }
    
    public void addReadPoint(Long suraID, Long verseID, String verseText){    	
    	Date today = new Date();
    	ContentValues bookmarkValues = new ContentValues();
        bookmarkValues.put(BOOKMARK_CHAPTERID, suraID);
        bookmarkValues.put(BOOKMARK_VERSEID, verseID);
        bookmarkValues.put(BOOKMARK_POSITIONID, -1);
        bookmarkValues.put(BOOKMARK_NAME, "ReadPoint");	
        bookmarkValues.put(BOOKMARK_TEXT, verseText);
		bookmarkValues.put(BOOKMARK_ISDELETED, 0);
		bookmarkValues.put(BOOKMARK_DATECREATED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
		bookmarkValues.put(BOOKMARK_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
		
        mDb.insert(BOOKMARK_TABLE, null, bookmarkValues);
    }
    
    public void editReadPoint(Long suraID, Long verseID, String verseText){    	
    	Date today = new Date();
    	ContentValues bookmarkValues = new ContentValues();
    	bookmarkValues.put(BOOKMARK_CHAPTERID, suraID);
        bookmarkValues.put(BOOKMARK_VERSEID, verseID);
        bookmarkValues.put(BOOKMARK_POSITIONID, -1);
        bookmarkValues.put(BOOKMARK_NAME, "ReadPoint");	
        bookmarkValues.put(BOOKMARK_TEXT, verseText);
		bookmarkValues.put(BOOKMARK_ISDELETED, 0);
		bookmarkValues.put(BOOKMARK_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
		
        int result = mDb.update(BOOKMARK_TABLE, bookmarkValues, BOOKMARK_POSITIONID+"=?", new String[] {Long.toString(-1)});
        
        if(result == 0){
    		addReadPoint(suraID, verseID, verseText);
        }
    }
    
    public void editBookmark(Long rowID, String name, Long suraID, Long verseID, Long positionID){    	
    	Date today = new Date();
    	ContentValues bookmarkValues = new ContentValues();
    	bookmarkValues.put(BOOKMARK_ID, rowID);
        bookmarkValues.put(BOOKMARK_CHAPTERID, suraID);
        bookmarkValues.put(BOOKMARK_VERSEID, verseID);
        bookmarkValues.put(BOOKMARK_POSITIONID, (verseID-1));
        bookmarkValues.put(BOOKMARK_NAME, name);	
        bookmarkValues.put(BOOKMARK_ISDELETED, 0);
		bookmarkValues.put(BOOKMARK_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
			
		/*
    	if(name.equals("null")){
        	Cursor chapter = mDb.rawQuery("SELECT "+CHAPTER_ID+", "+CHAPTER_CHAPTER_ID+", "+CHAPTER_NAME+" " +
            		" FROM "+CHAPTERS_TABLE+" " +
            		//" WHERE "+CHAPTER_LANG_CODE+" = "+prefs.getString("transPref", Config.DATABASE_ID)+
            		" WHERE "+CHAPTER_CHAPTER_ID+" = "+suraID+
            		" ORDER BY "+CHAPTER_CHAPTER_ID+" ASC;", null); 
        		
        	if(chapter.moveToFirst()){
        		name = chapter.getString(2);
        	}
            bookmarkValues.put(BOOKMARK_NAME, name);	
    	}else{
            bookmarkValues.put(BOOKMARK_NAME, name);	
    	}
		 */
		name = "("+suraID+":"+verseID+")";
		bookmarkValues.put(BOOKMARK_NAME, name);
		
        mDb.update(BOOKMARK_TABLE, bookmarkValues, BOOKMARK_ID+"=?", new String[] {Long.toString(rowID)});
    }
    
    public void deleteBookmark(Long rowID) throws SQLException {
    	//mDb.delete(BOOKMARK_TABLE, BOOKMARK_ID+"=?", new String[] {Long.toString(rowID)});
    	Date today = new Date();
    	ContentValues bookmarkValues = new ContentValues();
    	bookmarkValues.put(BOOKMARK_ISDELETED, 1);
		bookmarkValues.put(BOOKMARK_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());

        mDb.update(BOOKMARK_TABLE, bookmarkValues, BOOKMARK_ID+"=?", new String[] {Long.toString(rowID)});
    }
    
    public int clearBookmarks() throws SQLException {
    	//mDb.delete(BOOKMARK_TABLE, BOOKMARK_ID+"=?", new String[] {Long.toString(rowID)});
    	Date today = new Date();
    	ContentValues bookmarkValues = new ContentValues();
    	bookmarkValues.put(BOOKMARK_ISDELETED, 1);
		bookmarkValues.put(BOOKMARK_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());

        return mDb.update(BOOKMARK_TABLE, bookmarkValues, BOOKMARK_POSITIONID+" > ? ", new String[] {"-1"});
    }
	
    public Long countSearches() throws SQLException {
    	SQLiteStatement dbCountQuery = mDb.compileStatement("SELECT Count(*) " +
        		" FROM "+SEARCH_TABLE);
          
        return dbCountQuery.simpleQueryForLong();
    }
    
    public Long countBookmarks() throws SQLException {
    	SQLiteStatement dbCountQuery = mDb.compileStatement("SELECT Count(*) " +
    			" FROM "+BOOKMARK_TABLE);
    	
    	return dbCountQuery.simpleQueryForLong();
    }
    
    public int restoreSearch(String SearchText, String DatabaseID, String IsDeleted, String DateCreated, String DateModified) throws Exception
    {
        try
        {
        	Cursor checkText = mDb.rawQuery("SELECT "+SEARCH_ID+", "+SEARCH_TEXT+", "+SEARCH_LANG_CODE+", "+SEARCH_ISDELETED+", "+SEARCH_DATECREATED+", "+SEARCH_DATEMODIFIED+
        			" FROM "+SEARCH_TABLE+
        			" WHERE "+SEARCH_TEXT+" = '"+SearchText+"'"+
        			" AND "+SEARCH_LANG_CODE+" = " + DatabaseID+
    				" ORDER BY "+SEARCH_ID+" DESC;", null);
        	
        	checkText.moveToNext();
        	
        	if(checkText.getCount() < 1 ){
        		System.out.println("Insert SearchText: "+SearchText);
        		ContentValues searchValues = new ContentValues();
            	searchValues.put(SEARCH_TEXT, SearchText);
        		searchValues.put(SEARCH_LANG_CODE, Integer.parseInt(DatabaseID));
        		searchValues.put(SEARCH_ISDELETED, Integer.parseInt(IsDeleted));
        		searchValues.put(SEARCH_DATECREATED, DateCreated);
        		searchValues.put(SEARCH_DATEMODIFIED, DateModified);
        		
                mDb.insert(SEARCH_TABLE, null, searchValues);
                return 1;
        	}else{
        		return 1;
        	}
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception();
        }
    }
    
    public int restoreBookmark(String BookmarkName, String SuraID, String VerseID, String PositionID, String IsDeleted, String DateCreated, String DateModified) throws Exception
    {
        try
        {
            if (PositionID.equals("-1"))
            {
            	String[] input = {PositionID};
            	Cursor cursor = mDb.rawQuery("SELECT "+BOOKMARK_ID+", "+BOOKMARK_NAME+", "+BOOKMARK_CHAPTERID+", "+BOOKMARK_VERSEID+", "+BOOKMARK_POSITIONID+", "+BOOKMARK_ISDELETED+", "+BOOKMARK_DATECREATED+", "+BOOKMARK_DATEMODIFIED+
            			" FROM "+BOOKMARK_TABLE+
            			" WHERE "+BOOKMARK_POSITIONID+" = ? ;", input);
            	
            	cursor.moveToNext();
            	
        		if(cursor.getCount() < 1){
        			ContentValues bookmarkValues = new ContentValues();
            		//bookmarkValues.put(BOOKMARK_ID, 1);
            		bookmarkValues.put(BOOKMARK_NAME, BookmarkName);
            		bookmarkValues.put(BOOKMARK_CHAPTERID, Integer.parseInt(SuraID));
            		bookmarkValues.put(BOOKMARK_VERSEID, Integer.parseInt(VerseID));
            		bookmarkValues.put(BOOKMARK_POSITIONID, Integer.parseInt(PositionID));
            		bookmarkValues.put(BOOKMARK_ISDELETED, Integer.parseInt(IsDeleted));
            		bookmarkValues.put(BOOKMARK_DATECREATED, DateCreated);
            		bookmarkValues.put(BOOKMARK_DATEMODIFIED, DateModified);

                    mDb.insert(BOOKMARK_TABLE, null, bookmarkValues);	

                    return 1;
                }
                else
                {
                	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                	Date dateInternet = df.parse(DateModified);
                	Date dateClient = df.parse(cursor.getString(7));
                	
                	if (dateInternet.after(dateClient)){
                		Date today = new Date();
	                	ContentValues bookmarkValues = new ContentValues();
	                	bookmarkValues.put(BOOKMARK_NAME, BookmarkName);	
	            		bookmarkValues.put(BOOKMARK_CHAPTERID, Integer.parseInt(SuraID));
	            		bookmarkValues.put(BOOKMARK_VERSEID, Integer.parseInt(VerseID));
	            		bookmarkValues.put(BOOKMARK_POSITIONID, Integer.parseInt(PositionID));
	            		bookmarkValues.put(BOOKMARK_ISDELETED, Integer.parseInt(IsDeleted));
	            		bookmarkValues.put(BOOKMARK_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
	            		
	                    mDb.update(BOOKMARK_TABLE, bookmarkValues, BOOKMARK_POSITIONID+"=?", new String[] {PositionID});
                	}
                	return 1;
                }
            }
            else
            {
            	String[] input = {SuraID, VerseID, PositionID};
            	Cursor cursor = mDb.rawQuery("SELECT "+BOOKMARK_ID+", "+BOOKMARK_NAME+", "+BOOKMARK_CHAPTERID+", "+BOOKMARK_VERSEID+", "+BOOKMARK_POSITIONID+", "+BOOKMARK_ISDELETED+", "+BOOKMARK_DATECREATED+", "+BOOKMARK_DATEMODIFIED+
            			" FROM "+BOOKMARK_TABLE+
            			" WHERE "+BOOKMARK_CHAPTERID+" = ? "+
            			" AND "+BOOKMARK_VERSEID+" = ? "+
            			" AND "+BOOKMARK_POSITIONID+" = ? ;", input);
        		
            	cursor.moveToNext();
            	
            	if(cursor.getCount() < 1){
        			ContentValues bookmarkValues = new ContentValues();
            		//bookmarkValues.put(BOOKMARK_ID, 1);
            		bookmarkValues.put(BOOKMARK_NAME, BookmarkName);
            		bookmarkValues.put(BOOKMARK_CHAPTERID, Integer.parseInt(SuraID));
            		bookmarkValues.put(BOOKMARK_VERSEID, Integer.parseInt(VerseID));
            		bookmarkValues.put(BOOKMARK_POSITIONID, Integer.parseInt(PositionID));
            		bookmarkValues.put(BOOKMARK_ISDELETED, Integer.parseInt(IsDeleted));
            		bookmarkValues.put(BOOKMARK_DATECREATED, DateCreated);
            		bookmarkValues.put(BOOKMARK_DATEMODIFIED, DateModified);

                    mDb.insert(BOOKMARK_TABLE, null, bookmarkValues);	

                    return 1;
                }
                else
                {
                	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                	Date dateInternet = df.parse(DateModified);
                	Date dateClient = df.parse(cursor.getString(7));
                	
                	if (dateInternet.after(dateClient)){
                		Date today = new Date();
	                	ContentValues bookmarkValues = new ContentValues();
	                	bookmarkValues.put(BOOKMARK_NAME, BookmarkName);	
	            		bookmarkValues.put(BOOKMARK_CHAPTERID, Integer.parseInt(SuraID));
	            		bookmarkValues.put(BOOKMARK_VERSEID, Integer.parseInt(VerseID));
	            		bookmarkValues.put(BOOKMARK_POSITIONID, Integer.parseInt(PositionID));
	            		bookmarkValues.put(BOOKMARK_ISDELETED, Integer.parseInt(IsDeleted));
	            		bookmarkValues.put(BOOKMARK_DATEMODIFIED, (today.getYear()+1900)+"-"+(today.getMonth()+1)+"-"+today.getDate());
	            		
	                    mDb.update(BOOKMARK_TABLE, bookmarkValues, BOOKMARK_CHAPTERID+"=? AND "+BOOKMARK_VERSEID+"=? AND "+BOOKMARK_POSITIONID+" =? ", new String[] {SuraID,VerseID,PositionID});
                	}
                	return 1;
                }
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            throw new Exception();
        }
    }
    
    public Cursor getRandomVerse()
    {
    	Random rand = new Random();
    	
    	Integer RandomSuraID = rand.nextInt(114 - 1 + 1) + 1;

    	Long totalVerse = countChapterVerses(RandomSuraID);

    	Integer RandomVerseID = rand.nextInt(totalVerse.intValue() - 1 + 1) + 1;

        return getVerseSpecific(RandomSuraID.toString(), RandomVerseID.toString());
    }
    
    public Cursor fetchIndexes() throws SQLException {
    	return mDb.rawQuery("SELECT "+INDEX_ID+", "+INDEX_INDEX_TITLE+", "+INDEX_VERSE_COUNT+", "+INDEX_VIEW_COUNT+
    			", "+INDEX_ISDELETED+", "+INDEX_DATECREATED+", "+INDEX_DATEMODIFIED+
    			" FROM '"+INDEX_TABLE+"' "+
    			" WHERE "+INDEX_ISDELETED+" = 0 "+ 
    			" AND "+INDEX_VERSE_COUNT+" > 0 "+
    			" ORDER BY "+INDEX_INDEX_TITLE+" ASC;", null);
    }

    public Cursor fetchTopics(String type) throws SQLException {
    	if(type.equals("recent")){
    		return mDb.rawQuery("SELECT T."+TOPIC_ID+", T."+TOPIC_TOPIC_TITLE+", T."+TOPIC_VERSE_COUNT+", T."+TOPIC_VIEW_COUNT+
        			", T."+TOPIC_ISDELETED+", T."+TOPIC_DATECREATED+", T."+TOPIC_DATEMODIFIED+
        			" FROM "+TOPIC_TABLE+" T "+
        			" WHERE T."+TOPIC_ISDELETED+" = 0 "+ 
        			" AND T."+TOPIC_VERSE_COUNT+" > 0 "+
        			" ORDER BY T."+TOPIC_DATEMODIFIED+" DESC;", null);
    	}else if(type.equals("top")){
    		return mDb.rawQuery("SELECT T."+TOPIC_ID+", T."+TOPIC_TOPIC_TITLE+", T."+TOPIC_VERSE_COUNT+", T."+TOPIC_VIEW_COUNT+
        			", T."+TOPIC_ISDELETED+", T."+TOPIC_DATECREATED+", T."+TOPIC_DATEMODIFIED+
        			" FROM "+TOPIC_TABLE+" T "+
        			" WHERE T."+TOPIC_ISDELETED+" = 0 "+ 
        			" AND T."+TOPIC_VERSE_COUNT+" > 0 "+
        			" ORDER BY T."+TOPIC_VERSE_COUNT+" DESC;", null);
    	}else{
    		return mDb.rawQuery("SELECT T."+TOPIC_ID+", T."+TOPIC_TOPIC_TITLE+", T."+TOPIC_VERSE_COUNT+", T."+TOPIC_VIEW_COUNT+
        			", T."+TOPIC_ISDELETED+", T."+TOPIC_DATECREATED+", T."+TOPIC_DATEMODIFIED+
        			" FROM "+TOPIC_TABLE+" T "+
        			" WHERE T."+TOPIC_ISDELETED+" = 0 "+ 
        			" AND T."+TOPIC_VERSE_COUNT+" > 0 "+
        			" ORDER BY T."+TOPIC_TOPIC_TITLE+" ASC;", null);
    	}
    }

    public Cursor fetchIndexItems(Long indexId) throws SQLException {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	return mDb.rawQuery("SELECT B."+INDEX_ITEM_ID+", B."+INDEX_ITEM_CHAPTER_ID+", B."+INDEX_ITEM_VERSE_ID+", V."+VERSE_TEXT+", B."+INDEX_ITEM_ORDER+", V."+VERSE_ARABIC_TEXT+ 
    			" FROM "+INDEX_ITEM_TABLE+" B, "+VERSES_TABLE+" V "+
    			" WHERE B."+INDEX_ITEM_INDEX_ID+" = "+indexId+
    			" AND B."+INDEX_ITEM_CHAPTER_ID+" = V."+VERSE_CHAPTERID+
    			" AND B."+INDEX_ITEM_VERSE_ID+" = V."+VERSE_VERSEID+
    			" AND V."+VERSE_DATABASEID+" = "+prefs.getString("transPref", Config.DATABASE_ID)+
    			" ORDER BY B."+INDEX_ITEM_ORDER+" ASC;", null);
    } 

    public Cursor fetchTopicItems(Long topicId) throws SQLException {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mCtx);
    	
    	return mDb.rawQuery("SELECT B."+TOPIC_ITEM_ID+", B."+TOPIC_ITEM_CHAPTER_ID+", B."+TOPIC_ITEM_VERSE_ID+", V."+VERSE_TEXT+", B."+TOPIC_ITEM_ORDER+", V."+VERSE_ARABIC_TEXT+ 
    			" FROM "+TOPIC_ITEM_TABLE+" B, "+VERSES_TABLE+" V "+
    			" WHERE B."+TOPIC_ITEM_TOPIC_ID+" = "+topicId+
    			" AND B."+TOPIC_ITEM_CHAPTER_ID+" = V."+VERSE_CHAPTERID+
    			" AND B."+TOPIC_ITEM_VERSE_ID+" = V."+VERSE_VERSEID+
    			" AND V."+VERSE_DATABASEID+" = "+prefs.getString("transPref", Config.DATABASE_ID)+
    			" ORDER BY B."+TOPIC_ITEM_ORDER+" ASC;", null);
    }

    public Cursor fetchTranslations() throws SQLException {
    	return mDb.rawQuery("SELECT "+TRANS_ID+", "+TRANS_TRANSLATION_NAME+", "+TRANS_LANG_CODE+", "+TRANS_LANG_NAME+
    			" FROM "+TRANS_TABLE+" "+
    			" WHERE "+TRANS_ACTIVE+" = 1 "+ 
    			" AND "+TRANS_ID+" > 3 "+
    			" ORDER BY "+TRANS_TRANSLATION_NAME+" ASC;", null);
    } 

}
