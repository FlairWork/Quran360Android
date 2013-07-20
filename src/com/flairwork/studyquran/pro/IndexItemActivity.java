package com.flairwork.studyquran.pro;


import com.flairwork.studyquran.pro.widget.MenuDialog;
import com.flairwork.studyquran.pro.R;

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class IndexItemActivity extends GDListActivity {

	private AlertDialog alert;
	
	private static final int ACTIVITY_VERSE = 0;
	private static final int BOOKMARK_ID = Menu.FIRST;
	private static final int CHECKPOINT_ID = Menu.FIRST + 1;
	private static final int COPY_ID = Menu.FIRST + 2;
	private static final int SHARE_ID = Menu.FIRST + 3;
	private static final int SHARESOCIAL_ID = Menu.FIRST + 4;
	
	private Long indexId;
	private String indexTitle;
    
	protected QuranDbAdapter myDBHelper;
	protected Cursor cursor;
    
    private ClipboardManager clipboard;

    private Typeface arabicFont;
    
    //private ImageView pairupAds;
     
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		try{
	        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	        
	        setContentView(R.layout.index_item);
	    
	        indexId = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(QuranDbAdapter.INDEX_ID);
			if (indexId == null) {
				Bundle extras = getIntent().getExtras();
				indexId = extras != null ? extras.getLong(QuranDbAdapter.INDEX_ID)
										: null;
			}

			indexTitle = (savedInstanceState == null) ? null :
	            (String) savedInstanceState.getSerializable(QuranDbAdapter.INDEX_INDEX_TITLE);
			if (indexTitle == null) {
				Bundle extras = getIntent().getExtras();
				indexTitle = extras != null ? extras.getString(QuranDbAdapter.INDEX_INDEX_TITLE)
										: null;
			}
			
			setTitle(indexTitle);

			arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/q.ttf");
	  	    //arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/UthmanicHafs1.otf");
	  	    //arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/LateefRegOT.ttf");
	  	    //arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/ScheherazadeRegOT.ttf");
	  	    //arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/me_quran_volt_newmet.ttf");
			
	        myDBHelper = new QuranDbAdapter(this);
	        myDBHelper.open();

	        fillData();

	        //Get the listView ( from: ListActivity )
	        final ListView lv = getListView();
	        lv.setTextFilterEnabled(true);
	        registerForContextMenu(getListView());

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Menu");
	        builder.setItems(MenuDialog.menu_items, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	                //Toast.makeText(getApplicationContext(), MenuDialog.menu_items[item], Toast.LENGTH_SHORT).show();
	                // route to proper activity
	                MenuDialog.RouteActivity(getApplicationContext(), item);
	            }
	        });
	        alert = builder.create();

	        addActionBarItem(getActionBar()
	                .newActionBarItem(NormalActionBarItem.class)
	                .setDrawable(R.drawable.menu)
	                .setContentDescription(R.string.menu), R.id.action_bar_menu);
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			myDBHelper.close();
		}
	}

    private void fillData() {

        cursor = myDBHelper.fetchIndexItems(indexId);
        startManagingCursor(cursor);
  	    
        // Create an array to specify the fields we want to display in the list (only TITLE)
  	    String[] items_from = new String[] { QuranDbAdapter.INDEX_ITEM_ORDER, QuranDbAdapter.VERSE_ARABIC_TEXT, QuranDbAdapter.VERSE_TEXT,
  	    		QuranDbAdapter.INDEX_ITEM_CHAPTER_ID, QuranDbAdapter.INDEX_ITEM_VERSE_ID };
  	    // and an array of the fields we want to bind those fields to (in this case just text1)
  	    int[] items_to = new int[] { R.id.Order, R.id.ArabicText, R.id.VerseText, R.id.SuraID, R.id.VerseID };
  	    // Now create an array adapter and set it to display using our row
  	        
  	    ItemCursorAdapter itemsAdapter = new ItemCursorAdapter(this, R.layout.index_item_list, cursor, items_from, items_to, arabicFont);
  	    getListView().setFastScrollEnabled(true);
        setListAdapter(itemsAdapter); 
            
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SQLiteCursor cursor = (SQLiteCursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);
        Long chapterId = cursor.getLong(1);
        Long verseId = cursor.getLong(2);

        //Toast.makeText(getApplicationContext(), indexId+"|"+VerseID, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ReadVersesActivity.class);
        i.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, chapterId);
        i.putExtra(QuranDbAdapter.VERSE_VERSEID, verseId);
        startActivityForResult(i, ACTIVITY_VERSE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, BOOKMARK_ID, 0, R.string.menu_bookmark);
        menu.add(0, CHECKPOINT_ID, 1, R.string.menu_checkpoint);
        menu.add(0, COPY_ID, 2, R.string.menu_copy);
        menu.add(0, SHARE_ID, 3, R.string.menu_share);
        menu.add(0, SHARESOCIAL_ID, 4, R.string.menu_shareSocial);
        
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        SQLiteCursor  cursor = (SQLiteCursor) getListView().getItemAtPosition(info.position);
        startManagingCursor(cursor);
        final Long chapterId = cursor.getLong(1);
        final Long verseID = cursor.getLong(2);
        String verseText = cursor.getString(3);

        switch(item.getItemId()) {
            case COPY_ID:
            	clipboard.setText("'"+verseText+"' Al-Quran ("+chapterId+":"+verseID+") (Quran360 for Android)");
                Toast.makeText(getApplicationContext(), clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
                return true;
            case CHECKPOINT_ID:
            	myDBHelper.editReadPoint(chapterId, verseID, verseText);
            	Toast.makeText(getApplicationContext(), "ReadPoint Saved ("+chapterId+":"+verseID+")", Toast.LENGTH_SHORT).show();
                return true;                
            case SHARE_ID:
            	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            	sharingIntent.setType("text/plain");
            	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Quran360");
            	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "'"+verseText+"' ("+chapterId+":"+verseID+") - Al-Quran (Quran360 for Android)");
            	Intent chooser = Intent.createChooser(sharingIntent, "Share with");
            	startActivity(chooser);
                return true;        
            case SHARESOCIAL_ID:
            	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            	
            	String shareString = verseText.substring(0, 50);
            	shareString = shareString + "(cont...) http://web.quran360.com/site/verse?tr="+prefs.getString("transPref", Config.DATABASE_ID)+"&ch="+chapterId+"&v="+verseID;
                
            	Intent sharingSocialIntent = new Intent(Intent.ACTION_SEND);
            	sharingSocialIntent.setType("text/plain");
            	sharingSocialIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Quran360");
            	sharingSocialIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareString);
            	Intent chooserSocial = Intent.createChooser(sharingSocialIntent, "Share with");
            	startActivity(chooserSocial);
                return true;        
            case BOOKMARK_ID:
            	myDBHelper.addBookmark("("+chapterId+":"+verseID+")", chapterId, verseID, (verseID-1), verseText);
                Toast.makeText(getApplicationContext(), "Bookmark Saved ("+chapterId+":"+verseID+")", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.verse_menu, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.previous:
        	try{
                if (indexId == 1)
                {
                    Config.PREVIOUS_CHAPTER = 114;
                    indexId = 114l;
                }
                else
                {
                    Config.PREVIOUS_CHAPTER = (indexId.intValue() - 1);
                    indexId = (indexId - 1);
                }
                System.out.println("previous indexId: "+indexId);
	            
                fillData();
	            
			}catch(Exception e){
				e.printStackTrace();
			}
            return true;    
        case R.id.gotoverse:
        	final EditText input = new EditText(this);
        	
        	new AlertDialog.Builder(this)
                .setTitle("Go to Verse no.")
                //.setMessage("")
                .setView(input)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	try{
                    		Editable verseNo = input.getText();
                    		System.out.println("Goto verseID: "+verseNo);
                    		setSelection(Integer.parseInt(verseNo.toString()));
                    	}catch(NumberFormatException e){
                    		 Toast.makeText(getApplicationContext(), "Invalid input: must be a number", Toast.LENGTH_SHORT).show();
                    	}catch(Exception e){
                    		e.printStackTrace();
                    	}
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
            return true;
        case R.id.next:
        	try{
	            if (indexId == 114)
	            {
	                Config.NEXT_CHAPTER = 1;
	                indexId = 1l;
	            }
	            else
	            {
	            	Config.NEXT_CHAPTER = (indexId.intValue() + 1);
	            	indexId = (indexId + 1);
	            }
	            System.out.println("next indexId: "+indexId);

	            fillData();
	            
			}catch(Exception e){
				e.printStackTrace();
			}
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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
    	stopManagingCursor(cursor);
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
