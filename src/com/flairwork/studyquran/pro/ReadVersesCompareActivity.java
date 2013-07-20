package com.flairwork.studyquran.pro;

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.flairwork.studyquran.pro.widget.MenuDialog;
import com.flairwork.studyquran.pro.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ReadVersesCompareActivity extends GDListActivity {

	private AlertDialog alert;
	
	private static final int COPY_ID = Menu.FIRST;
	
	private Long chapterId;
	private Long verseId;

    private ClipboardManager clipboard;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        setContentView(R.layout.read_verse_compare);

        chapterId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(QuranDbAdapter.VERSE_CHAPTERID);
		if (chapterId == null) {
			Bundle extras = getIntent().getExtras();
			chapterId = extras != null ? extras.getLong(QuranDbAdapter.VERSE_CHAPTERID)
									: null;
		}
		
        verseId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(QuranDbAdapter.VERSE_VERSEID);
		if (verseId == null) {
			Bundle extras = getIntent().getExtras();
			verseId = extras != null ? extras.getLong(QuranDbAdapter.VERSE_VERSEID)
									: null;
		}

		setTitle("Compare ("+chapterId+":"+verseId+")");
		
		fillData();

        //Get the listView ( from: ListActivity )
        final ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        registerForContextMenu(getListView());
        getListView().setFastScrollEnabled(true);
	      
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
    	 if (chapterId != null && verseId != null) {
    		 try {
    			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	    		
    			URL url = new URL("http://web.quran360.com/services/VerseCompare.php?langCode="+prefs.getString("DATABASE_LANG_CODE", Config.DATABASE_LANG_CODE)+"&sura="+chapterId+"&ayah="+verseId+"&format=xml");
 	            SAXParserFactory spf = SAXParserFactory.newInstance();
 	            SAXParser sp = spf.newSAXParser();
 	            XMLReader xr = sp.getXMLReader();
 	            
 	            // Create a new ContentHandler and apply it to the XML-Reader
 	            VerseCompareXmlHandler myVerseCompareXmlHandler = new VerseCompareXmlHandler();
 	            xr.setContentHandler(myVerseCompareXmlHandler);
 	            xr.parse(new InputSource(url.openStream()));
 	            
 	            VerseCompareParsedXmlDataSet verseParsedXmlDataSet = myVerseCompareXmlHandler.getParsedData();
 	            //System.out.println("XML "+verseParsedXmlDataSet.toString());
 	            
 	            //Needed for the listItems
 	            ArrayList<HashMap<String, String>> verseComparelist = new ArrayList<HashMap<String, String>>();
 	            
 	            for(int i=0; i < verseParsedXmlDataSet.suraIDs.size(); i++){
 	            	//System.out.println("ID: "+verseParsedXmlDataSet.IDs.get(i));
 	            	//System.out.println("TranslationName: "+verseParsedXmlDataSet.translationNames.get(i));
 	            	//System.out.println("SuraID: "+verseParsedXmlDataSet.chapterIds.get(i));
 	            	//System.out.println("VerseID: "+verseParsedXmlDataSet.verseIds.get(i));
 	            	//System.out.println("AyahText: "+verseParsedXmlDataSet.ayahTexts.get(i));
 	            	
 	            	HashMap<String, String> map = new HashMap<String, String>();
 	            	map.put("TranslationName", verseParsedXmlDataSet.translationNames.get(i));
 	            	//map.put("VerseID", verseParsedXmlDataSet.verseIDs.get(i));
 	            	map.put("AyahText", verseParsedXmlDataSet.ayahTexts.get(i));
 	            	
 	            	verseComparelist.add(map);
 	            }
 	            
 	            //Make a new listadapter
 	            ListAdapter adapter = new SimpleAdapter(this, verseComparelist , R.layout.read_verse_compare_list,
 	                            new String[] { "TranslationName", "AyahText" },
 	                            new int[] { R.id.TranslationName, R.id.AyahText  });
 	            
 	            setListAdapter(adapter);
 	            
 	        } catch (Exception e) {
 	        	e.printStackTrace();
 	        	System.out.println("XML Error "+e.getCause());
 	        	Toast.makeText(getApplicationContext(), "Internet connection required.", Toast.LENGTH_SHORT).show();
 	        }
    	 }
    }
        
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Toast.makeText(getApplicationContext(), id+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, COPY_ID, 0, R.string.menu_copy);
        //menu.add(0, COMPARE_ID, 1, R.string.menu_compare);
    }

    @SuppressWarnings("unchecked")
	@Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case COPY_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                HashMap<String, String> map = (HashMap<String, String>) getListView().getItemAtPosition(info.position);
                String translationName =  map.get("TranslationName");
                String verseText =  map.get("AyahText");
                clipboard.setText("'"+verseText+"' Al-Quran ("+chapterId+":"+verseId+") Translation: "+translationName+"");
                Toast.makeText(getApplicationContext(), clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
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
    }

    
}
