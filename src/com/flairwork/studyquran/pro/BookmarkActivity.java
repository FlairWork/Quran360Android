package com.flairwork.studyquran.pro;


import com.flairwork.studyquran.pro.R;
import com.flairwork.studyquran.pro.widget.MenuDialog;

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BookmarkActivity extends GDListActivity {

	private AlertDialog alert;
	
	private static final int ACTIVITY_VERSES = 1;
	
	private static final int COPY_ID = Menu.FIRST;
	//private static final int DEFAULT_ID = Menu.FIRST + 1;
	private static final int EDIT_ID = Menu.FIRST + 2;
	private static final int DELETE_ID = Menu.FIRST + 3;
	private static final int CLEAR_ID = Menu.FIRST + 4;
	private static final int REFRESH_ID = Menu.FIRST + 5;
	
	protected QuranDbAdapter myDBHelper;

    private ClipboardManager clipboard;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        setContentView(R.layout.bookmark);
        
        setTitle("BookMark");
        
        myDBHelper = new QuranDbAdapter(this);
        myDBHelper.open();

		fillData();

        //Get the listView ( from: ListActivity )
        final ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        registerForContextMenu(getListView());

		//myDBHelper.close();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menu");
        builder.setItems(MenuDialog.menu_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //Toast.makeText(getApplicationContext(), MenuDialog.menu_items[item], Toast.LENGTH_SHORT).show();
                //route to proper activity
                MenuDialog.RouteActivity(getApplicationContext(), item);
            }
        });
        alert = builder.create();

        addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
                .setDrawable(R.drawable.menu)
                .setContentDescription(R.string.menu), R.id.action_bar_menu);
        
	}

    
    private void fillData() {
		// Get all of the verses from the database and create the item list
	        Cursor cursor = myDBHelper.fetchBookmarks();
	        
	        startManagingCursor(cursor);
	        // Create an array to specify the fields we want to display in the list (only TITLE)
	        String[] bookmarks_from = new String[] { QuranDbAdapter.BOOKMARK_NAME, QuranDbAdapter.BOOKMARK_TEXT, QuranDbAdapter.BOOKMARK_CHAPTERID, QuranDbAdapter.BOOKMARK_VERSEID };
	         
	        // and an array of the fields we want to bind those fields to (in this case just text1)
	        int[] bookmarks_to = new int[] { R.id.Name, R.id.AyahText, R.id.SuraID, R.id.VerseID };
	         
	        // Now create an array adapter and set it to display using our row
	        SimpleCursorAdapter bookmarks = new SimpleCursorAdapter(this, R.layout.bookmark_list, cursor, bookmarks_from, bookmarks_to);
	           
	        setListAdapter(bookmarks); 
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SQLiteCursor  cursor = (SQLiteCursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);
         
        String suraName = cursor.getString(1);
        Long suraID = cursor.getLong(2);
        Long verseID = cursor.getLong(3);
        Long positionID = cursor.getLong(4);

        //Toast.makeText(getApplicationContext(), suraID+"|"+verseID, Toast.LENGTH_SHORT).show();
        
        Intent i = new Intent(this, ReadVersesActivity.class);
        i.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, suraID);
        i.putExtra(QuranDbAdapter.CHAPTER_NAME, suraName);
        i.putExtra(QuranDbAdapter.VERSE_VERSEID, verseID);
        i.putExtra(QuranDbAdapter.BOOKMARK_POSITIONID, positionID);
        startActivityForResult(i, ACTIVITY_VERSES);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, REFRESH_ID, 0, R.string.menu_refresh);
        menu.add(0, COPY_ID, 1, R.string.menu_copy);
        //menu.add(0, DEFAULT_ID, 1, R.string.menu_bookmark_default);
        //menu.add(0, EDIT_ID, 2, R.string.menu_bookmark_edit);
        menu.add(0, DELETE_ID, 3, R.string.menu_bookmark_delete);
        menu.add(0, CLEAR_ID, 4, R.string.menu_bookmark_clear);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        SQLiteCursor  cursor = (SQLiteCursor) getListView().getItemAtPosition(info.position);
        startManagingCursor(cursor);
        final Long rowID = cursor.getLong(0);
        String bookmarkName = cursor.getString(1);
        final Long suraID = cursor.getLong(2);
        final Long verseID = cursor.getLong(3);
        switch(item.getItemId()) {
        	case REFRESH_ID:
        		fillData();
        		return true;
            case COPY_ID:
                clipboard.setText("'"+bookmarkName+"' ("+suraID+":"+verseID+")");
                Toast.makeText(getApplicationContext(), clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
                return true;
            case EDIT_ID:
            	final EditText input = new EditText(this);
            	new AlertDialog.Builder(this)
	                .setTitle("Bookmark Name")
	                .setMessage("")
	                .setView(input)
	                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        Editable bookmarkName = input.getText();
	                        myDBHelper.editBookmark(rowID, bookmarkName.toString(), suraID, verseID, (verseID-1));
	                        fillData();
	                        Toast.makeText(getApplicationContext(), "Bookmark Saved "+bookmarkName+" ("+suraID+":"+verseID+")", Toast.LENGTH_SHORT).show();
	                    }
	                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        // Do nothing.
	                    }
	                }).show();
                return true;
            case DELETE_ID:
            	myDBHelper.deleteBookmark(rowID);
            	Toast.makeText(getApplicationContext(), "Bookmark Deleted: "+bookmarkName+"("+suraID+":"+verseID+")", Toast.LENGTH_SHORT).show();
            	fillData();
                return true;
            case CLEAR_ID:
            	if(myDBHelper.clearBookmarks() > 0){
	            	Toast.makeText(getApplicationContext(), "All Bookmarks Deleted", Toast.LENGTH_SHORT).show();
	            	fillData();
            	}
                return true;                
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
        	case R.id.action_bar_menu:
        		alert.show();        		
        		return true;
        	default:
        		return super.onHandleActionBarItemClick(item, position);
	    }
	}
    
    @Override
    protected void onPause() {
    	myDBHelper.close();
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	myDBHelper.open();
    	fillData();
    }
    
    @Override
    protected void onDestroy(){
    	if (myDBHelper != null){
    		myDBHelper.close();
        }
        super.onDestroy();
    }


}
