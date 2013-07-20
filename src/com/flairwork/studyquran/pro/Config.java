package com.flairwork.studyquran.pro;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

public class Config {

	public static int DATABASE_VERSION = 7;
	
	public static String APP_TITLE = "Quran360";
    public static String TRANSLATION_NAME = "English: Yusuf Ali";
   
    public static String DATABASE_ID = "59";
    public static String DATABASE_LANG_CODE = "en";
    public static String DATABASE_OLD_NAME = "STUDYQURAN_PRO";
	public static String DATABASE_NAME = "Quran360";
	public static String DB_PATH = "/data/data/com.flairwork.studyquran.pro/databases/";

	public static boolean SHOW_ARABIC_CHAPTER = true;
	public static boolean SHOW_ARABIC_VERSE = true;
	public static boolean Synchronize = false;
	public static boolean Share = false;
    public static String UserEmail = "your@email.com";

    public static int CURRENT_POSITION = 0;
	public static int NEXT_CHAPTER = 2;
	public static int PREVIOUS_CHAPTER = 114;
    
	public static void SyncData(Context context) throws Exception
    {
        try
        {
        	SharedPreferences prefs = PreferenceManager .getDefaultSharedPreferences(context);
    		if(prefs.getBoolean("isSynchronizePref", true)){
    			String userEmail = prefs.getString("emailTextPref", "your@email.com");
    			if( !userEmail.equals("your@email.com") && !userEmail.equals("") && !userEmail.equals(" ")){
    				System.out.println("Synchronizing for "+userEmail);
        			//Toast.makeText(context,"Synchronizing for "+userEmail, Toast.LENGTH_LONG).show();
            		
        			//Sync Searches
                    BackupSearches(userEmail, context);

                    //Sync Bookmark
                    BackupBookmarks(userEmail, context);
    			}
    		}
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw ex;
        }
    }

    private static void RestoreSearches(String userEmail, Context context)
    {
        System.out.println("RestoreSearches()");

        //Get All Searches, then Insert thru Web Services

        String xmlUrl = "http://www.studyquran.info/services/StudyQuran_Search_Pull.php?UserEmail=" + userEmail + "&format=xml";

    	try {
    		URL url = new URL(xmlUrl);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            
            // Create a new ContentHandler and apply it to the XML-Reader
            SearchXmlHandler mySearchXmlHandler = new SearchXmlHandler();
            xr.setContentHandler(mySearchXmlHandler);
            xr.parse(new InputSource(url.openStream()));
            
            SearchParsedXmlDataSet searchParsedXmlDataSet = mySearchXmlHandler.getParsedData();
            //System.out.println("XML "+verseParsedXmlDataSet.toString());
            
            QuranDbAdapter myDBHelper = new QuranDbAdapter(context);
        	myDBHelper.open();
            for(int i=0; i < searchParsedXmlDataSet.searchTexts.size(); i++){
            	//System.out.println("ID: "+verseParsedXmlDataSet.IDs.get(i));
            	//System.out.println("TranslationName: "+verseParsedXmlDataSet.translationNames.get(i));
            	//System.out.println("SuraID: "+verseParsedXmlDataSet.suraIDs.get(i));
            	//System.out.println("VerseID: "+verseParsedXmlDataSet.verseIDs.get(i));
            	//System.out.println("AyahText: "+verseParsedXmlDataSet.ayahTexts.get(i));
            	
            	//restoreSearch
            	myDBHelper.restoreSearch(searchParsedXmlDataSet.searchTexts.get(i), searchParsedXmlDataSet.databaseIDs.get(i), 
            			searchParsedXmlDataSet.isDeleteds.get(i), searchParsedXmlDataSet.dateCreateds.get(i), 
            			searchParsedXmlDataSet.dateModifieds.get(i));
            	
            }
            myDBHelper.close();

        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("XML Error "+e.getCause());
        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
        }
    }

    private static void RestoreBookmarks(String userEmail, Context context)
    {
        System.out.println("RestoreBookmarks()");

        //Get All Searches, then Insert thru Web Services

        String xmlUrl = "http://www.studyquran.info/services/StudyQuran_Bookmark_Pull.php?UserEmail=" + userEmail + "&format=xml";

    	try {
    		URL url = new URL(xmlUrl);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            
            // Create a new ContentHandler and apply it to the XML-Reader
            BookmarkXmlHandler myBookmarkXmlHandler = new BookmarkXmlHandler();
            xr.setContentHandler(myBookmarkXmlHandler);
            xr.parse(new InputSource(url.openStream()));
            
            BookmarkParsedXmlDataSet bookmarkParsedXmlDataSet = myBookmarkXmlHandler.getParsedData();
            //System.out.println("XML "+verseParsedXmlDataSet.toString());
            
            QuranDbAdapter myDBHelper = new QuranDbAdapter(context);
        	myDBHelper.open();
            //Needed for the listItems
            for(int i=0; i < bookmarkParsedXmlDataSet.suraIDs.size(); i++){
            	//System.out.println("ID: "+verseParsedXmlDataSet.IDs.get(i));
            	//System.out.println("TranslationName: "+verseParsedXmlDataSet.translationNames.get(i));
            	//System.out.println("SuraID: "+verseParsedXmlDataSet.suraIDs.get(i));
            	//System.out.println("VerseID: "+verseParsedXmlDataSet.verseIDs.get(i));
            	//System.out.println("AyahText: "+verseParsedXmlDataSet.ayahTexts.get(i));
            	
            	myDBHelper.restoreBookmark(bookmarkParsedXmlDataSet.bookmarkNames.get(i), 
            			bookmarkParsedXmlDataSet.suraIDs.get(i), bookmarkParsedXmlDataSet.verseIDs.get(i), bookmarkParsedXmlDataSet.positionIDs.get(i), 
            			bookmarkParsedXmlDataSet.isDeleteds.get(i), bookmarkParsedXmlDataSet.dateCreateds.get(i), bookmarkParsedXmlDataSet.dateModifieds.get(i));
            }
            myDBHelper.close();
            
            //Toast.makeText(context, "Synchronization Complete.", Toast.LENGTH_LONG).show();
            
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("XML Error "+e.getCause());
        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
        }
    }

    private static void BackupSearches(String userEmail, Context context)
    {
    	QuranDbAdapter myDBHelper = new QuranDbAdapter(context);
    	try{
    		myDBHelper.open();
    		
	        if (myDBHelper.countSearches() > 0)
	        {
	            System.out.println("BackupSearches()");
	            //Get All Searches, then Insert thru Web Services
	            Cursor cursor = myDBHelper.getAllSearches();
	            
	            while(cursor.moveToNext()){
	            	try { 	
	            		System.out.println("BackupSearch: " + cursor.getString(1));
	
	            		// Create a new HttpClient and Post Header
	            	    HttpClient httpclient = new DefaultHttpClient();
	            	    HttpPost httppost = new HttpPost("http://www.studyquran.info/services/StudyQuran_Search_Push.php");
	
	        	        // Add your data
	        	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	        nameValuePairs.add(new BasicNameValuePair("UserEmail", userEmail));
	        	        nameValuePairs.add(new BasicNameValuePair("SearchText", cursor.getString(1)));
	        	        nameValuePairs.add(new BasicNameValuePair("DatabaseID", cursor.getInt(2)+""));
	        	        nameValuePairs.add(new BasicNameValuePair("UserDeviceCode", "Android"));
	        	        nameValuePairs.add(new BasicNameValuePair("IsDeleted", cursor.getInt(3)+""));
	        	        nameValuePairs.add(new BasicNameValuePair("DateCreated", cursor.getString(4)));
	        	        nameValuePairs.add(new BasicNameValuePair("DateModified", cursor.getString(5)));
	        	        
	        	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
	        	        // Execute HTTP Post Request
	        	        HttpResponse response = httpclient.execute(httppost);
	            	    System.out.println("HttpResponse: "+response);
	            	    
	            	    RestoreSearches(userEmail, context);
	            	    
	        	    } catch (ClientProtocolException e) {
	        	    	e.printStackTrace();
	    	        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
	        	    } catch (IOException e) {
	        	    	e.printStackTrace();
	    	        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
	    	        } catch (Exception e) {
	    	        	e.printStackTrace();
	    	        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
	    	        }
	            }
	        }
	        else
	        {
	            RestoreSearches(userEmail, context);
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
        }finally{
        	myDBHelper.close();
        }
    }

    private static void BackupBookmarks(String userEmail, Context context)
    {
    	QuranDbAdapter myDBHelper = new QuranDbAdapter(context);
    	try{
    		myDBHelper.open();
	        if (myDBHelper.countBookmarks() > 0)
	        {
	            System.out.println("BackupBookmarks()");
	            //Get All Searches, then Insert thru Web Services
	            Cursor cursor = myDBHelper.getAllBookmarks();
	            
	            while(cursor.moveToNext()){
	            	try { 	
	            		System.out.println("BackupBookmarks: " + cursor.getString(1));
	
	            		// Create a new HttpClient and Post Header
	            	    HttpClient httpclient = new DefaultHttpClient();
	            	    HttpPost httppost = new HttpPost("http://www.studyquran.info/services/StudyQuran_Bookmark_Push.php");
	            	    
	        	        // Add your data
	        	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	        nameValuePairs.add(new BasicNameValuePair("UserEmail", userEmail));
	        	        nameValuePairs.add(new BasicNameValuePair("BookmarkName", cursor.getString(1)));
	        	        nameValuePairs.add(new BasicNameValuePair("SuraID", cursor.getInt(2)+""));
	        	        nameValuePairs.add(new BasicNameValuePair("VerseID", cursor.getInt(3)+""));
	        	        nameValuePairs.add(new BasicNameValuePair("PositionID", cursor.getInt(4)+""));
	        	        nameValuePairs.add(new BasicNameValuePair("UserDeviceCode", "Android"));
	        	        nameValuePairs.add(new BasicNameValuePair("IsDeleted", cursor.getInt(5)+""));
	        	        nameValuePairs.add(new BasicNameValuePair("DateCreated", cursor.getString(6)));
	        	        nameValuePairs.add(new BasicNameValuePair("DateModified", cursor.getString(7)));
	        	        
	        	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
	        	        // Execute HTTP Post Request
	        	        HttpResponse response = httpclient.execute(httppost);
	        	        System.out.println("HttpResponse: "+response);
	        	        
	        	        RestoreBookmarks(userEmail, context);
	        	    } catch (ClientProtocolException e) {
	        	    	e.printStackTrace();
	    	        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
	        	    } catch (IOException e) {
	        	    	e.printStackTrace();
	    	        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
	    	        } catch (Exception e) {
	    	        	e.printStackTrace();
	    	        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
	    	        }
	            }
	        }
	        else
	        {
	            RestoreBookmarks(userEmail, context);
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        	//Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show();
        }finally{
        	myDBHelper.close();
        }
    }
}
