package com.flairwork.studyquran.pro;


import com.flairwork.studyquran.pro.R;
import com.flairwork.studyquran.pro.widget.MenuDialog;

import greendroid.app.GDListActivity;import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SearchActivity extends  GDListActivity  {

	private AlertDialog alert;
	
	private static final int ACTIVITY_SEARCH_DETAIL = 3;
	private static final int COPY_ID = Menu.FIRST;
	//private static final int EDIT_ID = Menu.FIRST + 1;
	private static final int DELETE_ID = Menu.FIRST + 2;
	private static final int CLEAR_ID = Menu.FIRST + 3;
	private static final int REFRESH_ID = Menu.FIRST + 4;
	
	protected QuranDbAdapter myDBHelper;

    private ClipboardManager clipboard;
    
    private EditText searchEditor;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        setContentView(R.layout.search);

        setTitle("Search");
        
        myDBHelper = new QuranDbAdapter(this);
        myDBHelper.open();
 
		fillData();

        searchEditor = (EditText) findViewById(R.id.SearchText);
        searchEditor.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				String searchText = v.getText().toString();
				search(searchText);
				return true;
			}
		});

        //Get the listView ( from: ListActivity )
        final ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        registerForContextMenu(getListView());
        getListView().setFastScrollEnabled(true);
	      
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

    public void SearchAction(View button) {  
        // Do click handling here  

    	String searchText = searchEditor.getText().toString();
    	search(searchText);
    	
		fillData();

    }
    
    private void fillData() {
		// Get all of the verses from the database and create the item list
	        Cursor cursor = myDBHelper.fetchAllSearches();
	        
	        startManagingCursor(cursor);
	        // Create an array to specify the fields we want to display in the list (only TITLE)
	        String[] searches_from = new String[] { QuranDbAdapter.SEARCH_TEXT};
	        // and an array of the fields we want to bind those fields to (in this case just text1)
	        int[] searches_to = new int[] { R.id.SearchText };
	        // Now create an array adapter and set it to display using our row
	        SimpleCursorAdapter searches = new SimpleCursorAdapter(this, R.layout.search_list, cursor, searches_from, searches_to);
	        
	        setListAdapter(searches); 
    }
    
    private void search(String searchText) {
    	Intent i = new Intent(this, SearchDetailList.class);
        i.putExtra(QuranDbAdapter.SEARCH_TEXT, searchText);
        startActivityForResult(i, ACTIVITY_SEARCH_DETAIL);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SQLiteCursor  cursor = (SQLiteCursor) l.getItemAtPosition(position);
        
        String searchText = cursor.getString(1);

        //Toast.makeText(getApplicationContext(), "Searching for: "+searchText, Toast.LENGTH_SHORT).show();
        
        Intent i = new Intent(this, SearchDetailList.class);
        i.putExtra(QuranDbAdapter.SEARCH_TEXT, searchText);
        startActivityForResult(i, ACTIVITY_SEARCH_DETAIL);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, REFRESH_ID, 0, R.string.menu_refresh);
        menu.add(0, COPY_ID, 1, R.string.menu_copy);
        //menu.add(0, EDIT_ID, 1, R.string.menu_search_edit);
        menu.add(0, DELETE_ID, 2, R.string.menu_search_delete);
        menu.add(0, CLEAR_ID, 3, R.string.menu_search_clear);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        SQLiteCursor  cursor = (SQLiteCursor) getListView().getItemAtPosition(info.position);
        Long rowID = cursor.getLong(0);
        String searchText = cursor.getString(1);
        switch(item.getItemId()) {
    		case REFRESH_ID:
    			fillData();
    			return true;        
            case COPY_ID:
                clipboard.setText(searchText);
                Toast.makeText(getApplicationContext(), clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
                return true;
            case DELETE_ID:
            	myDBHelper.deleteSearch(rowID);
            	Toast.makeText(getApplicationContext(), "Search Text Deleted: "+searchText, Toast.LENGTH_SHORT).show();	
            	fillData();
                return true;
            case CLEAR_ID:
            	if(myDBHelper.clearSearch() > 0){
            		Toast.makeText(getApplicationContext(), "All Searches Deleted", Toast.LENGTH_SHORT).show();	
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
