package com.flairwork.studyquran.pro;

import greendroid.app.GDListActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

import java.net.URL;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.flairwork.studyquran.pro.widget.MenuDialog;
import com.flairwork.studyquran.pro.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ReadActivity extends GDListActivity {
	
	private AlertDialog alert;

	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
 
	private int SIZE = 0;
	
	private Long chapterId;
	private String chapterTrName;
	private String chapterEnName;
	private String chapterType;
	private Long verseCount;
	
    //private static final int ACTIVITY_CHAPTER = 0;
    private static final int ACTIVITY_VERSE = 1;
    //private static final int ACTIVITY_COMPARE = 2;

    private static final int CHECKPOINT_ID = Menu.FIRST;
    private static final int COPY_ID = Menu.FIRST+1;
    //private static final int COMPARE_ID = Menu.FIRST + 1;

	protected QuranDbAdapter myDBHelper;
	protected Cursor cursor;
	
    private ClipboardManager clipboard;
    
    //private ImageView pairupAds;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try{
	        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	        
	        setContentView(R.layout.read);

	        setTitle("Read");
	        
	        myDBHelper = new QuranDbAdapter(this);
	        
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
	                //route to proper activity
	                MenuDialog.RouteActivity(getApplicationContext(), item);
	            }
	        });
	        alert = builder.create();

	        addActionBarItem(getActionBar()
	                .newActionBarItem(NormalActionBarItem.class)
	                .setDrawable(R.drawable.menu)
	                .setContentDescription(R.string.menu), R.id.action_bar_menu);
	        
	        addActionBarItem(getActionBar()
	                .newActionBarItem(NormalActionBarItem.class) 
	                .setDrawable(R.drawable.favs)
	                .setContentDescription(R.string.readpoint), R.id.action_bar_export);
	        /*
	        addActionBarItem(getActionBar()
	                .newActionBarItem(NormalActionBarItem.class)
	                .setDrawable(new ActionBarDrawable(this, R.drawable.ic_title_export_default)), R.id.action_bar_view_trans);
	        */
	        
	        addActionBarItem(getActionBar()
	                .newActionBarItem(NormalActionBarItem.class)
	                .setDrawable(new ActionBarDrawable(this, R.drawable.settings)), R.id.action_bar_view_setting);
	        
	        addActionBarItem(getActionBar()
	                .newActionBarItem(NormalActionBarItem.class)
	                .setDrawable(new ActionBarDrawable(this, R.drawable.ic_action_bar_info)), R.id.action_bar_view_info);
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			myDBHelper.close();
		}
    }

    	
    private void fillData() {
    	Date start = new Date();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        TextView translationNameField = (TextView) findViewById(R.id.TranslationTitle);  
        translationNameField.setText(prefs.getString("TRANSLATION_NAME", Config.TRANSLATION_NAME));
        
    	myDBHelper.open();
    	
    	// Get all of the chapters from the database and create the item list
        cursor = myDBHelper.fetchAllChapters(prefs.getString("chapterSort", "Number"));
        startManagingCursor(cursor);
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] chapters_from = new String[] { QuranDbAdapter.CHAPTER_ID, QuranDbAdapter.CHAPTER_CHAPTER_ID, QuranDbAdapter.CHAPTER_TR_NAME, 
        		QuranDbAdapter.CHAPTER_ORDER, QuranDbAdapter.CHAPTER_EN_NAME, QuranDbAdapter.CHAPTER_TYPE, QuranDbAdapter.CHAPTER_VERSE_COUNT};
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] chapters_to = new int[] { R.id._id, R.id.chapter_id, R.id.chapter_tr_name, 
        		R.id.chapter_order, R.id.chapter_en_name, R.id.chapter_type, R.id.chapter_verse_count};
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter chapters = new SimpleCursorAdapter(this, R.layout.read_list, cursor, chapters_from, chapters_to);
        // = new ChapterCursorAdapter(this, R.layout.read_list, cursor, chapters_from, chapters_to);
        getListView().setFastScrollEnabled(true);
        
        setListAdapter(chapters);
        
        Date end = new Date();
   	 	long duration =  end.getTime() - start.getTime();
   	 	System.out.println("Chapters fillData() duration: "+duration/1000+" s");
    }

    public void TransAction(View textView) {  
    	Intent intent = new Intent(getApplicationContext(), TranslationsActivity.class);
        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, getApplicationContext().getString(R.string.translation_label));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SQLiteCursor cursor = (SQLiteCursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);
        
        chapterId = cursor.getLong(1);
        chapterTrName = cursor.getString(2);
        chapterEnName = cursor.getString(4);
        chapterType = cursor.getString(5);

        verseCount = cursor.getLong(6);

        Long inCount = myDBHelper.countChapterVerses(chapterId.intValue());
        
        if( inCount < verseCount){
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        	
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Are you sure you want to download Translation "+chapterId+". "+chapterTrName+" ("
        			+prefs.getString("TRANSLATION_NAME", Config.TRANSLATION_NAME)+") ? ")
        			
        	       .setCancelable(false) 
        	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   dialog.dismiss();
        	               //download and insert verse
        	               //String xmlUrl = "http://web.quran360.com/services/VersewArab.php?translationId=" + translationId + "&format=xml";
        	        	   DownloadTranslations();
        	           }
        	       })
        	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                dialog.cancel();
        	           }
        	       });
        	AlertDialog alertDownload = builder.create();  
        	alertDownload.show();
        }else{
            //Toast.makeText(getApplicationContext(), id+"|"+name, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ReadVersesActivity.class);
            i.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, chapterId);
            i.putExtra(QuranDbAdapter.CHAPTER_TR_NAME, chapterTrName);
            i.putExtra(QuranDbAdapter.CHAPTER_EN_NAME, chapterEnName);
            i.putExtra(QuranDbAdapter.CHAPTER_TYPE, chapterType);
            startActivityForResult(i, ACTIVITY_VERSE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CHECKPOINT_ID, 1, R.string.menu_gotocheckpoint);
        menu.add(0, COPY_ID, 0, R.string.menu_copy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	alert.show();
    	return true;
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        SQLiteCursor  cursor = (SQLiteCursor) getListView().getItemAtPosition(info.position);
        startManagingCursor(cursor);
        Long suraID = cursor.getLong(1);
        String suraName = cursor.getString(2);
        startManagingCursor(cursor);
        switch(item.getItemId()) {
            case COPY_ID:
                clipboard.setText(suraID+": "+suraName);
                Toast.makeText(getApplicationContext(), clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
                return true;      
            case CHECKPOINT_ID:
            	Cursor chCursor = myDBHelper.getReadPoint();
            	startManagingCursor(chCursor);
            	chCursor.moveToPosition(0);
            	
            	Long chapterId = chCursor.getLong(2);
                Long verseId = chCursor.getLong(3);
                
            	String chapterTrName = cursor.getString(2);
                String chapterEnName = cursor.getString(4);
                String chapterType = cursor.getString(5);
                
                //Toast.makeText(getApplicationContext(), id+"|"+name, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, ReadVersesActivity.class);
                i.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, chapterId);
                i.putExtra(QuranDbAdapter.VERSE_VERSEID, verseId);
                
                i.putExtra(QuranDbAdapter.CHAPTER_TR_NAME, chapterTrName);
                i.putExtra(QuranDbAdapter.CHAPTER_EN_NAME, chapterEnName);
                i.putExtra(QuranDbAdapter.CHAPTER_TYPE, chapterType);
                
                startActivityForResult(i, ACTIVITY_VERSE);
            	return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	//fillData();
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
        	case R.id.action_bar_menu:
        		//show menu
        		alert.show();        		
        		return true;
        	case R.id.action_bar_view_info:
                //startActivity(new Intent(this, InfoTabActivity.class));
            	startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_bar_view_setting:
                //startActivity(new Intent(this, InfoTabActivity.class));
            	startActivity(new Intent(this, Preferences.class));
                return true;
            case R.id.action_bar_export:
            	//Toast.makeText(this, "ReadPoint", Toast.LENGTH_SHORT).show();
            	Cursor cursor = myDBHelper.getReadPoint();
            	startManagingCursor(cursor);
            	cursor.moveToPosition(0);
            	
            	Long chapterId = cursor.getLong(2);
            	Long verseId = cursor.getLong(3);
                
            	String chapterTrName = cursor.getString(1);
                String chapterEnName = cursor.getString(1);
                String chapterType = cursor.getString(1);

            	myDBHelper.close();
            	
                Intent i = new Intent(this, ReadVersesActivity.class);
                i.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, chapterId);
                i.putExtra(QuranDbAdapter.VERSE_VERSEID, verseId);
                i.putExtra(QuranDbAdapter.CHAPTER_TR_NAME, chapterTrName);
                i.putExtra(QuranDbAdapter.CHAPTER_EN_NAME, chapterEnName);
                i.putExtra(QuranDbAdapter.CHAPTER_TYPE, chapterType);
                
                startActivityForResult(i, ACTIVITY_VERSE);
                
                return true;
            case R.id.action_bar_view_trans:
            	startActivity(new Intent(this, TranslationsActivity.class));
                return true;
               
            default:
                return super.onHandleActionBarItemClick(item, position);
        }
    }

    protected void DownloadTranslations() {
    	try {
    		// prepare for a progress bar dialog
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Downloading ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(verseCount.intValue());
			progressBar.show();
 
			//reset progress bar status
			progressBarStatus = 0;
 
			//reset filesize
			SIZE = 0;
			
			new Thread(new Runnable() {
				public void run() {
					while (progressBarStatus < verseCount) {
	 
					  // process some tasks
					  progressBarStatus = DownloadTranslationsTask();
	 
					  // your computer is too fast, sleep 1 seconds
					  try {
						Thread.sleep(1000);
					  } catch (InterruptedException e) {
						e.printStackTrace();
					  }
	 
					  // Update the progress bar
					  progressBarHandler.post(new Runnable() {
						public void run() {
						  progressBar.setProgress(progressBarStatus);
						  //progressBar.setProgress(SIZE);
						}
					  });
					}
	 
					// ok, file is downloaded,
					if (progressBarStatus >= verseCount) {
						// sleep 5 seconds, so that you can see the 100%
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						// close the progress bar dialog
						progressBar.dismiss();
						
						Intent i = new Intent(getApplicationContext(), ReadVersesActivity.class);
			            i.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, chapterId);
			            i.putExtra(QuranDbAdapter.CHAPTER_TR_NAME, chapterTrName);
			            i.putExtra(QuranDbAdapter.CHAPTER_EN_NAME, chapterEnName);
			            i.putExtra(QuranDbAdapter.CHAPTER_TYPE, chapterType);
			            startActivityForResult(i, ACTIVITY_VERSE);
						
					}
				}
			}).start();
			
			//fillData();

        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("XML Error "+e.getCause());
        	Toast.makeText(getApplicationContext(), "Fast Internet connection required.", Toast.LENGTH_SHORT).show();
        }
	}
    
 // file download simulator... a really simple
	public int DownloadTranslationsTask() {
		try{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			
			URL url = new URL("http://web.quran360.com/services/VersewArab.php?translationId="+
					prefs.getString("transPref", Config.DATABASE_ID)+"&chapterId="+chapterId+"&verseId="+(SIZE+1)+"&format=xml");
			
	        SAXParserFactory spf = SAXParserFactory.newInstance(); 
	        SAXParser sp = spf.newSAXParser();
	        XMLReader xr = sp.getXMLReader();
	        
	        // Create a new ContentHandler and apply it to the XML-Reader
	        VerseXmlHandler myVerseXmlHandler = new VerseXmlHandler();
	        xr.setContentHandler(myVerseXmlHandler);
	        xr.parse(new InputSource(url.openStream()));
	        
	        VerseParsedXmlDataSet verseParsedXmlDataSet = myVerseXmlHandler.getParsedData();
	        //System.out.println("XML "+verseParsedXmlDataSet.toString());
	        
	        for(int i=0; i < verseParsedXmlDataSet.IDs.size(); i++){
	        	//System.out.println("ID: "+verseParsedXmlDataSet.IDs.get(i));
	        	//System.out.println("TranslationName: "+verseParsedXmlDataSet.translationNames.get(i));
	        	//System.out.println("SuraID: "+verseParsedXmlDataSet.chapterIds.get(i));
	        	//System.out.println("VerseID: "+verseParsedXmlDataSet.verseIds.get(i));
	        	//System.out.println("AyahText: "+verseParsedXmlDataSet.ayahTexts.get(i));
	        	/*
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("TranslationName", verseParsedXmlDataSet.translationNames.get(i));
	        	//map.put("VerseID", verseParsedXmlDataSet.verseIDs.get(i));
	        	map.put("AyahText", verseParsedXmlDataSet.ayahTexts.get(i));
	        	*/
	        	myDBHelper.addVerse(Long.parseLong(verseParsedXmlDataSet.IDs.get(i)), 
	        			Long.parseLong(verseParsedXmlDataSet.translationIDs.get(i)), 
	        			Long.parseLong(verseParsedXmlDataSet.chapterIDs.get(i)), 
	        			Long.parseLong(verseParsedXmlDataSet.verseIDs.get(i)), 
	        			verseParsedXmlDataSet.verseTexts.get(i), 
	        			verseParsedXmlDataSet.arabicTexts.get(i));
	        	
	        	SIZE++;
	        	//progressBarStatus++;
	        	System.out.println(SIZE);
	        	//return SIZE;
	        	progressBar.setProgress(SIZE);
	        }
	        return SIZE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verseCount.intValue();
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