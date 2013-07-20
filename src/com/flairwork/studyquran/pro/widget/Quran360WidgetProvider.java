package com.flairwork.studyquran.pro.widget;


import java.util.Arrays;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;


import com.flairwork.studyquran.pro.QuranDbAdapter;
import com.flairwork.studyquran.pro.R;
import com.flairwork.studyquran.pro.ReadVersesActivity;

public class Quran360WidgetProvider extends AppWidgetProvider { 
 
   public static final String DEBUG_TAG = "Quran360WidgetProvider";
   private static Long chapterId;
   private static Long verseId;
   private static String verseText;
   //private static String araibicText = cursor.getString(4);
   
  
   @Override
   public void onDeleted(Context context, int[] appWidgetIds) {
           super.onDeleted(context, appWidgetIds);
   }

   @Override
   public void onDisabled(Context context) {
           super.onDisabled(context);
   }

   @Override
   public void onEnabled(Context context) {
           super.onEnabled(context);
   }

   @Override
   public void onReceive(Context context, Intent intent) {
           super.onReceive(context, intent);
   }

   @Override
   public void onUpdate(Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds) {
 
      try {
         updateWidgetContent(context, appWidgetManager, appWidgetIds);
         
         super.onUpdate(context, appWidgetManager, appWidgetIds);
         
      } catch (Exception e) {
         Log.e(DEBUG_TAG, "Failed", e);
      }
   }
 
   public static void updateWidgetContent(Context context,
      AppWidgetManager appWidgetManager, int[] appWidgetIds) {
 
      //String widgetTitle = context.getString(R.string.app_name);
      
      final int N = appWidgetIds.length;
      Log.i("Quran360WidgetProvider",  "Updating widgets " + Arrays.asList(appWidgetIds));
      // Perform this loop procedure for each App Widget that belongs to this
      // provider
      for (int i = 0; i < N; i++) {
        int appWidgetId = appWidgetIds[i];
        
        String widgetContent = generateWidgetContent(context);
        
        // Create an Intent to launch ExampleActivity
        Intent intent = new Intent(context, ReadVersesActivity.class);
        intent.putExtra(QuranDbAdapter.CHAPTER_CHAPTER_ID, chapterId);
        intent.putExtra(QuranDbAdapter.VERSE_VERSEID, verseId);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        
        // Get the layout for the App Widget and attach an on-click listener
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
        views.setOnClickPendingIntent(R.id.widgetContent, pendingIntent);
        
        // To update a label
        //views.setTextViewText(R.id.widgetTitle, widgetTitle);
        views.setTextViewText(R.id.widgetContent, widgetContent);
        
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
      }
   }

   private static String generateWidgetContent(Context context) {
	   String content = "";
	   chapterId = 1l;
	   verseId = 1l;
	   
       try{
    	   QuranDbAdapter myDBHelper = new QuranDbAdapter(context);
           myDBHelper.open();

           Cursor cursor = myDBHelper.getRandomVerse();
           cursor.moveToPosition(0);

           chapterId = cursor.getLong(1);
           verseId = cursor.getLong(2);
           verseText = cursor.getString(3);
           //araibicText = cursor.getString(4);
           
           content = "'"+verseText+"' ("+chapterId+":"+verseId+")";
           
           myDBHelper.close();
       } catch (Exception e) {
    	   e.printStackTrace();
       }
	   return content;
   }
}