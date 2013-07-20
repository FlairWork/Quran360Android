package com.flairwork.studyquran.pro.widget;

import com.flairwork.studyquran.pro.AboutActivity;
import com.flairwork.studyquran.pro.BookmarkActivity;
import com.flairwork.studyquran.pro.CompareActivity;
import com.flairwork.studyquran.pro.IndexesActivity;
import com.flairwork.studyquran.pro.Preferences;
import com.flairwork.studyquran.pro.R;
import com.flairwork.studyquran.pro.ReadActivity;
import com.flairwork.studyquran.pro.SearchActivity;
import com.flairwork.studyquran.pro.TopicsTabActivity;
import com.flairwork.studyquran.pro.TranslationsActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MenuDialog {

	public static final CharSequence[] menu_items = {"Read", "Compare", "BookMark", "Search", "Index", "Topics",
			//"Purchase", "Donate", 
			"Rate this", "Share this", "Visit us online", "Follow @Quran360", 
			"Translations", "Settings", "Feedback", "About"};

	private static String marketUrl = "market://search?q=pname:com.flairwork.studyquran.pro";
	private static String donateUrl = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=TR7CBXNWHNSJN";
	private static String visitUrl = "http://web.quran360.com";
	private static String followUrl = "http://twitter.com/#!/Quran360";
	
	public static void RouteActivity(Context context, int item){
		//Toast.makeText(context, MenuDialog.menu_items[item], Toast.LENGTH_SHORT).show();
		
		if(MenuDialog.menu_items[item].equals("Read")){
			Intent intent = new Intent(context, ReadActivity.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.read_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Compare")){
			Intent intent = new Intent(context, CompareActivity.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.compare_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("BookMark")){
			Intent intent = new Intent(context, BookmarkActivity.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.bookmark_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Search")){
			Intent intent = new Intent(context, SearchActivity.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.search_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Index")){
			Intent intent = new Intent(context, IndexesActivity.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.indexes_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Topics")){
			Intent intent = new Intent(context, TopicsTabActivity.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.topics_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Purchase")){
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Donate")){
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(donateUrl));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Rate this")){
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Share this")){
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        	sharingIntent.setType("text/plain");
        	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I Love Quran360 for Android");
        	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I Love Quran360 for Android");
        	
        	Intent chooser = Intent.createChooser(sharingIntent, "Share with");
        	chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(chooser);
		}else if(MenuDialog.menu_items[item].equals("Visit us online")){
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Follow @Quran360")){
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(followUrl));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Translations")){
			Intent intent = new Intent(context, TranslationsActivity.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.translation_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Settings")){
			Intent intent = new Intent(context, Preferences.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.settings_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}else if(MenuDialog.menu_items[item].equals("Feedback")){
			/* Create the Intent */
    		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

    		/* Fill it with Data */
    		emailIntent.setType("plain/text");
    		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@quran360.com"});
    		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[Quran360 Android]");
    		//emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
    		//emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		
    		Intent chooser = Intent.createChooser(emailIntent, "Quran360 Feedback");
        	chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        
    		/* Send it off to the Activity-Chooser */
    		context.startActivity(chooser);
		}else if(MenuDialog.menu_items[item].equals("About")){
			Intent intent = new Intent(context, AboutActivity.class);
	        //intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, context.getString(R.string.about_label));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}
	}
	
	
}
