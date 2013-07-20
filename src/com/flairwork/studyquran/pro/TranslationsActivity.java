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

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.flairwork.studyquran.pro.R;
import com.flairwork.studyquran.pro.widget.MenuDialog;

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
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
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TranslationsActivity extends GDListActivity {

	private AlertDialog alert;
	private AlertDialog alertDownload;
	
	protected QuranDbAdapter myDBHelper;
	protected Cursor cursor;
	
	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
 
	private int MAX_SIZE = 6236;
	private int CHAPTER_SIZE = 114;
	private int SIZE = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
	        
	        setContentView(R.layout.translations);

	        setTitle("Translations");

	        myDBHelper = new QuranDbAdapter(this);
	        
	        fillData();

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
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			myDBHelper.close();
		}
        //ItemAdapter adapter = new SectionedItemAdapter(this, CHEESES, SECTIONS);
        //getListView().setFastScrollEnabled(true);
        //setListAdapter(adapter);
        
    }

    private void fillData() {
    	myDBHelper.open();
    	
    	// Get all of the chapters from the database and create the item list
        cursor = myDBHelper.fetchTranslations();
        startManagingCursor(cursor);
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] item_from = new String[] {QuranDbAdapter.TRANS_TRANSLATION_NAME};
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] item_to = new int[] { R.id.trans_name};
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter items = new SimpleCursorAdapter(this, R.layout.translations_list, cursor, item_from, item_to);
        getListView().setFastScrollEnabled(true);
        setListAdapter(items);
        
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SQLiteCursor cursor = (SQLiteCursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);
        
        final Long transId = cursor.getLong(0);
        final String langCode = cursor.getString(2);
        final String transName = cursor.getString(1);
        
        //if versecount for transId <6236
        if(myDBHelper.countVerses(transId) < MAX_SIZE )
        {

        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Are you sure you want to download Translation "+transName+" ?")
        	       .setCancelable(false) 
        	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	    dialog.dismiss();
        	                //download and insert verse
        	                //String xmlUrl = "http://web.quran360.com/services/VersewArab.php?translationId=" + translationId + "&format=xml";
        	                DownloadTranslations(transId, langCode, transName);
        	           }
        	       })
        	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                dialog.cancel();
        	           }
        	       });
        	alertDownload = builder.create();  
        	alertDownload.show();
        	
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.putString("transPref", transId.toString());
    		editor.putString("DATABASE_ID", transId.toString());
    		editor.putString("DATABASE_LANG_CODE", langCode);
    		editor.putString("TRANSLATION_NAME", transName);
    		editor.commit();
    		
            Toast.makeText(getApplicationContext(), transName, Toast.LENGTH_SHORT).show();
            
        }else{
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.putString("transPref", transId.toString());
    		editor.putString("DATABASE_ID", transId.toString());
    		editor.putString("DATABASE_LANG_CODE", langCode);
    		editor.putString("TRANSLATION_NAME", transName);
    		editor.commit();
    		
            Toast.makeText(getApplicationContext(), transName, Toast.LENGTH_SHORT).show();
        }
    }

    protected void DownloadTranslations(Long transId, String langCode, String transName) {
		try {
    		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    		 
    		// prepare for a progress bar dialog
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Downloading ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(SIZE);
			progressBar.setMax(CHAPTER_SIZE);
			progressBar.show();
 
			//reset progress bar status
			progressBarStatus = 0;
 
			//reset filesize
			SIZE = 0;
			
			new Thread(new Runnable() {
				public void run() {
					while (progressBarStatus < CHAPTER_SIZE) {
	 
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
							System.out.println(SIZE);
				        	System.out.println(progressBarStatus);
				        	progressBar.setProgress(progressBarStatus);
				        	progressBar.setProgress(SIZE);
						} 
					  });
					}
	 
					// ok, file is downloaded,
					if (progressBarStatus >= CHAPTER_SIZE) {
						// sleep 5 seconds, so that you can see the 100%
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	 
						// close the progress bar dialog
						progressBar.dismiss();
					}
				}
			}).start();
			
            //set pref trans
        	SharedPreferences.Editor editor = prefs.edit();
    		editor.putString("transPref", transId.toString());
    		editor.putString("DATABASE_ID", transId.toString());
    		editor.putString("DATABASE_LANG_CODE", langCode);
    		editor.putString("TRANSLATION_NAME", transName);
    		editor.commit();
    		
            Toast.makeText(getApplicationContext(), transName, Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("XML Error "+e.getCause());
        	Toast.makeText(getApplicationContext(), "Fast Internet connection required.", Toast.LENGTH_SHORT).show();
        }
	}

    // file download simulator... a really simple
	public int DownloadTranslationsTask() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);	
		
		for(long chapterId=1; chapterId <= CHAPTER_SIZE; chapterId++){
			try{
				//String xmlUrl = "http://web.quran360.com/services/VersewArab.php?translationId=" + translationId + "&format=xml";
				//URL url = new URL("http://web.quran360.com/services/VersewArab.php?translationId="+
						//prefs.getString("transPref", "111")+"&format=xml");
				Cursor cursor = myDBHelper.getChapterById(chapterId);
		    	//startManagingCursor(cursor);
		    	cursor.moveToPosition(0);
		    	
		        Long verseCount = cursor.getLong(6);
		        
		        for(int verseId=1; verseId <= verseCount; verseId++)
		        {
		        	URL url = new URL("http://web.quran360.com/services/VersewArab.php?translationId="+
							prefs.getString("transPref", "111")+"&chapterId="+chapterId+"&verseId="+verseId+"&format=xml");
					
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
			        	myDBHelper.addVerse(Long.parseLong(verseParsedXmlDataSet.IDs.get(i)), 
			        			Long.parseLong(verseParsedXmlDataSet.translationIDs.get(i)), 
			        			Long.parseLong(verseParsedXmlDataSet.chapterIDs.get(i)), 
			        			Long.parseLong(verseParsedXmlDataSet.verseIDs.get(i)), 
			        			verseParsedXmlDataSet.verseTexts.get(i), 
			        			verseParsedXmlDataSet.arabicTexts.get(i));
			        }
			        System.out.println(chapterId+":"+verseId);
		        }
		        
		        SIZE++;
	        	System.out.println(SIZE);
	        	progressBar.setProgress(SIZE);
	        	//return SIZE;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return CHAPTER_SIZE;
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
        		alert.show();        		
        		return true;
        	default:
        		return super.onHandleActionBarItemClick(item, position);
	    }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	alert.show();
    	return true;
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
