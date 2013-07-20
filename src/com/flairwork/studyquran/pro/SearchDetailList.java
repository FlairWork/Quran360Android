package com.flairwork.studyquran.pro;


import com.flairwork.studyquran.pro.R;

import greendroid.app.GDListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SearchDetailList extends  GDListActivity   {

	
	private static final int ACTIVITY_VERSE = 1;
    //private static final int ACTIVITY_COMPARE = 2;
	private static final int COPY_ID = Menu.FIRST;
	private static final int BOOKMARK_ID = Menu.FIRST + 1;
	private static final int CHECKPOINT_ID = Menu.FIRST + 2;
	
	protected QuranDbAdapter myDBHelper;

    private ClipboardManager clipboard;

	private String searchText;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        setContentView(R.layout.search_detail);
        
        myDBHelper = new QuranDbAdapter(this);
        myDBHelper.open();

        searchText = (savedInstanceState == null) ? null :
            (String) savedInstanceState.getSerializable(QuranDbAdapter.SEARCH_TEXT);
		if (searchText == null) {
			Bundle extras = getIntent().getExtras();
			searchText = extras != null ? extras.getString(QuranDbAdapter.SEARCH_TEXT)
									: null;
		}
		
		setTitle("Search: '"+searchText+"'");
		
		try{
			fillData();
		}catch(Exception e){
			e.printStackTrace();
		}

        //Get the listView ( from: ListActivity )
        final ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        registerForContextMenu(getListView());
        getListView().setFastScrollEnabled(true);
	      
	}

    private void fillData() {
    	 if (searchText != null) {
    		// Get all of the verses from the database and create the item list
 	        Cursor cursor = myDBHelper.fetchSearches(searchText);
 	        
 	        startManagingCursor(cursor);
 	        // Create an array to specify the fields we want to display in the list (only TITLE)
 	        String[] verses_from = new String[] { QuranDbAdapter.VERSE_CHAPTERID, QuranDbAdapter.VERSE_VERSEID, QuranDbAdapter.VERSE_TEXT};
 	        // and an array of the fields we want to bind those fields to (in this case just text1)
 	        int[] verses_to = new int[] { R.id.SuraID, R.id.VerseID, R.id.AyahText };
 	        // Now create an array adapter and set it to display using our row
 	        SimpleCursorAdapter verses = new SimpleCursorAdapter(this, R.layout.search_detail_list, cursor, verses_from, verses_to);
 	        setListAdapter(verses); 
 	       
 			setTitle("Search: '"+searchText+"' ("+cursor.getCount()+" verses)");
 			
 			Integer status = myDBHelper.addSearch(searchText, cursor.getCount());
 	    	if(status == 1){
 	    		Toast.makeText(getApplicationContext(), "Search Text Saved: '"+searchText, Toast.LENGTH_SHORT).show();
 	    	}
    	 }
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	/*
        super.onListItemClick(l, v, position, id);
        SQLiteCursor  cursor = (SQLiteCursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);
        Long suraID = cursor.getLong(1);
        Long verseID = cursor.getLong(2);
        //Toast.makeText(getApplicationContext(), mRowId+"|"+VerseID, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ReadVersesCompareActivity.class);
        i.putExtra(QuranDbAdapter.VERSE_CHAPTERID, suraID);
        i.putExtra(QuranDbAdapter.VERSE_VERSEID, verseID);
        startActivityForResult(i, ACTIVITY_COMPARE);
        */
    	
        super.onListItemClick(l, v, position, id);
        
        SQLiteCursor  cursor = (SQLiteCursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);
        Long suraID = cursor.getLong(1);
        //String suraName = cursor.getString(2);
        Long verseID = cursor.getLong(2);
        
        //Toast.makeText(getApplicationContext(), id+"|"+name, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ReadVersesActivity.class);
        i.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, suraID);
        i.putExtra(QuranDbAdapter.VERSE_VERSEID, verseID);
        startActivityForResult(i, ACTIVITY_VERSE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, COPY_ID, 0, R.string.menu_copy);
        menu.add(0, BOOKMARK_ID, 1, R.string.menu_bookmark);
        menu.add(0, CHECKPOINT_ID, 1, R.string.menu_checkpoint);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        SQLiteCursor  cursor = (SQLiteCursor) getListView().getItemAtPosition(info.position);
        startManagingCursor(cursor);
        final Long suraID = cursor.getLong(1);
        final Long verseID = cursor.getLong(2);
        String verseText = cursor.getString(3);

        switch(item.getItemId()) {
            case COPY_ID:
            	clipboard.setText("'"+verseText+"' Al-Quran ("+suraID+":"+verseID+") (Quran360 for Android)");
                Toast.makeText(getApplicationContext(), clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
               return true;
            case CHECKPOINT_ID:
            	myDBHelper.editReadPoint(suraID, verseID, verseText);
                return true;                                
            case BOOKMARK_ID:
            	myDBHelper.addBookmark("("+suraID+":"+verseID+")", suraID, verseID, (verseID-1), verseText);
                Toast.makeText(getApplicationContext(), "Bookmark Saved ("+suraID+":"+verseID+")", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onPause() {
    	myDBHelper.close();
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
            super.onResume();
    }
    
    @Override
    protected void onDestroy(){
    	if (myDBHelper != null){
    		myDBHelper.close();
        }
        super.onDestroy();
    }
    
}


