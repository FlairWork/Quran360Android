/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class CompareActivity extends GDListActivity {

	private AlertDialog alert;
	
	private static final int COPY_ID = Menu.FIRST;
	
    private ClipboardManager clipboard;

	private String chapterId;
	private String verseId;

	private EditText chapterNoField;

	private EditText verseNoField;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        setContentView(R.layout.compare);
        
        setTitle("Compare");
        
        chapterNoField = (EditText) findViewById(R.id.EditCompareChapterNo);  
          
        verseNoField = (EditText) findViewById(R.id.EditCompareVerseNo);  
        
        chapterId = chapterNoField.getText().toString();
		
        verseId = verseNoField.getText().toString();

		//fillData();

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
    
    public void CompareAction(View button) {  
        // Do click handling here  

        chapterId = chapterNoField.getText().toString();
		
        verseId = verseNoField.getText().toString();

		fillData();

    }
    
    private void fillData() {
   	 if (chapterId != null && verseId != null) {

 		setTitle("Compare ("+chapterId+":"+verseId+")");
 		
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
   	            	//map.put("VerseID", verseParsedXmlDataSet.verseIds.get(i));
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
