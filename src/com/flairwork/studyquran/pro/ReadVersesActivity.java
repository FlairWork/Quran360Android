package com.flairwork.studyquran.pro;


import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.flairwork.studyquran.pro.widget.MenuDialog;
import com.flairwork.studyquran.pro.R;

import greendroid.app.GDListActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ReadVersesActivity extends GDListActivity {

	private AlertDialog alert;

	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
 
	private int SIZE = 0;
	
	private static final int ACTIVITY_VERSE = 1;
	private static final int ACTIVITY_COMPARE = 2;
	private static final int BOOKMARK_ID = Menu.FIRST;
	private static final int CHECKPOINT_ID = Menu.FIRST + 1;
	private static final int COPY_ID = Menu.FIRST + 2;
	private static final int SHARE_ID = Menu.FIRST + 3;
	private static final int SHARESOCIAL_ID = Menu.FIRST + 4;
	
	private Long chapterId;
	private String chapterTrName;
	private String chapterEnName;
	private String chapterType;
	private Long verseCount;
	
    private Long verseID = 0l;
	private Long positionID = 0l;
	
	protected QuranDbAdapter myDBHelper;
	protected Cursor cursor;
    
    private ClipboardManager clipboard;

    private VerseCursorAdapter versesAdapter;
    
    private Typeface arabicFont;
    
    //private ImageView pairupAds;
     
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		try{
	        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	        
	        setContentView(R.layout.read_verse);
	    
	        chapterId = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(QuranDbAdapter.CHAPTER_CHAPTER_ID);
			if (chapterId == null) {
				Bundle extras = getIntent().getExtras();
				chapterId = extras != null ? extras.getLong(QuranDbAdapter.CHAPTER_CHAPTER_ID)
										: null;
			}

			chapterTrName = (savedInstanceState == null) ? null :
	            (String) savedInstanceState.getSerializable(QuranDbAdapter.CHAPTER_TR_NAME);
			if (chapterTrName == null) {
				Bundle extras = getIntent().getExtras();
				chapterTrName = extras != null ? extras.getString(QuranDbAdapter.CHAPTER_TR_NAME)
										: null;
			}

			chapterEnName = (savedInstanceState == null) ? null :
	            (String) savedInstanceState.getSerializable(QuranDbAdapter.CHAPTER_EN_NAME);
			if (chapterEnName == null) {
				Bundle extras = getIntent().getExtras();
				chapterEnName = extras != null ? extras.getString(QuranDbAdapter.CHAPTER_EN_NAME)
										: null;
			}

			chapterType = (savedInstanceState == null) ? null :
	            (String) savedInstanceState.getSerializable(QuranDbAdapter.CHAPTER_TYPE);
			if (chapterType == null) {
				Bundle extras = getIntent().getExtras();
				chapterType = extras != null ? extras.getString(QuranDbAdapter.CHAPTER_TYPE)
										: null;
			}
			
			verseID = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(QuranDbAdapter.VERSE_VERSEID);
			if (verseID == null) {
				Bundle extras = getIntent().getExtras();
				verseID = extras != null ? extras.getLong(QuranDbAdapter.VERSE_VERSEID)
										: null;
			}
			
			positionID = (verseID-1);
			Config.CURRENT_POSITION = positionID.intValue();
			
	  	    arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/q.ttf");
	  	    //arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/UthmanicHafs1.otf");
	  	    //arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/LateefRegOT.ttf");
	  	    //arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/ScheherazadeRegOT.ttf");
	  	    //arabicFont = Typeface.createFromAsset(this.getAssets(), "fonts/me_quran_volt_newmet.ttf");
			 
	  	    //setTitle(chapterId+". "+chapterTrName+" | "+chapterType);
			
	        myDBHelper = new QuranDbAdapter(this);
	        myDBHelper.open();
	        
			//new FillDataTask().execute();
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

    private void setChapterTitle() {
		cursor = myDBHelper.getChapterById(chapterId);
    	//startManagingCursor(cursor);
    	cursor.moveToPosition(0);

        chapterId = cursor.getLong(1);
        chapterTrName = cursor.getString(2);
        //chapterEnName = cursor.getString(4);
        chapterType = cursor.getString(5);
        
        verseCount = cursor.getLong(6);
        
  	    setTitle(chapterId+". "+chapterTrName+" | "+chapterType);
	}

	private void fillData() {

        setChapterTitle();
        
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
			cursor = myDBHelper.fetchVerses(chapterId);
		 	startManagingCursor(cursor);
		 	// Create an array to specify the fields we want to display in the list (only TITLE)
		 	String[] verses_from = new String[] { QuranDbAdapter.VERSE_VERSEID, QuranDbAdapter.VERSE_TEXT, QuranDbAdapter.VERSE_ARABIC_TEXT};
	  	    // and an array of the fields we want to bind those fields to (in this case just text1)
	  	    int[] verses_to = new int[] { R.id.VerseID, R.id.VerseText, R.id.ArabicText };
	  	    // Now create an array adapter and set it to display using our row
	  	        
	  	    //SimpleCursorAdapter versesAdapter = new SimpleCursorAdapter(this, R.layout.read_verses_list, cursor, verses_from, verses_to);
	  	    versesAdapter = new VerseCursorAdapter(getApplicationContext(), R.layout.read_verses_list, cursor, verses_from, verses_to, arabicFont);
	  	    getListView().setFastScrollEnabled(true);
	      
	        setListAdapter(versesAdapter); 
	        
		    if(Config.CURRENT_POSITION > 0){
		    	setSelection(Config.CURRENT_POSITION);
		    }
		    
		    if(verseID > 0){
		    	setSelection(positionID.intValue());
		    }
		}
	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SQLiteCursor cursor = (SQLiteCursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);
        Long chapterId = cursor.getLong(1);
        Long verseId = cursor.getLong(2);
        Config.CURRENT_POSITION = (verseID.intValue() - 1);
        //Toast.makeText(getApplicationContext(), chapterId+"|"+VerseID, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ReadVersesCompareActivity.class);
        i.putExtra(QuranDbAdapter.VERSE_CHAPTERID, chapterId);
        i.putExtra(QuranDbAdapter.VERSE_VERSEID, verseId);
        startActivityForResult(i, ACTIVITY_COMPARE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.verse_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.previous:
        	long previousChapterId = 1;
        	if (chapterId == 1)
            {
                previousChapterId = 114l;
            }
            else
            {
                previousChapterId = (chapterId - 1);
            }
            Intent i = new Intent(this, ReadVersesActivity.class);
            i.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, previousChapterId);
            startActivityForResult(i, ACTIVITY_VERSE);
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
        	long netxtChapterId = 1;
        	if (chapterId == 114)
            {
        		netxtChapterId = 1l;
            }
            else
            {
            	netxtChapterId = (chapterId + 1);
            }
            Intent next = new Intent(this, ReadVersesActivity.class);
            next.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, netxtChapterId);
            startActivityForResult(next, ACTIVITY_VERSE);
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
						//fillData();
						
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
