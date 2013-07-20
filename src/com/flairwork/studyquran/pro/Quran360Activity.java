package com.flairwork.studyquran.pro;

import com.flairwork.studyquran.pro.R;

import greendroid.app.GDListActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ItemAdapter;
import greendroid.widget.NormalActionBarItem;
import greendroid.widget.item.SeparatorItem;
import greendroid.widget.item.TextItem;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


public class Quran360Activity extends GDListActivity {

	private String marketUrl = "market://search?q=pname:com.flairwork.studyquran.pro";
	private String donateUrl = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=TR7CBXNWHNSJN";
	private String visitUrl = "http://web.quran360.com";
	private String followUrl = "http://twitter.com/#!/Quran360";
	
	private static final int ACTIVITY_VERSE = 1;

	protected QuranDbAdapter myDBHelper;
	protected Cursor cursor;
	
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.home);

        setTitle(getString(R.string.app_name));

        myDBHelper = new QuranDbAdapter(this);
    	myDBHelper.open();
    	
        ItemAdapter adapter = new ItemAdapter(this);
        
        adapter.add(new SeparatorItem("MENU"));
        adapter.add(createTextItem(R.string.read_label, ReadActivity.class));
        adapter.add(createTextItem(R.string.compare_label, CompareActivity.class));
        adapter.add(createTextItem(R.string.bookmark_label, BookmarkActivity.class));
        adapter.add(createTextItem(R.string.search_label, SearchActivity.class));
        adapter.add(createTextItem(R.string.indexes_label, IndexesActivity.class));
        adapter.add(createTextItem(R.string.topics_label, TopicsTabActivity.class));
        adapter.add(new SeparatorItem("APP"));
        adapter.add(createTextItem(R.string.purchase_label));
        adapter.add(createTextItem(R.string.donate_label));
        adapter.add(createTextItem(R.string.rate_label));
        adapter.add(createTextItem(R.string.share_label));
        adapter.add(createTextItem(R.string.visit_label));
        adapter.add(createTextItem(R.string.follow_label));
        adapter.add(createTextItem(R.string.translation_label, TranslationsActivity.class));
        adapter.add(createTextItem(R.string.settings_label, Preferences.class));
        adapter.add(createTextItem(R.string.feedback_label));
        adapter.add(createTextItem(R.string.about_label, AboutActivity.class));
        
        /*
        adapter.add(createTextItem(R.string.basic_item_label, BasicItemActivity.class));
        adapter.add(createTextItem(R.string.xml_item_label, XmlItemActivity.class));
        adapter.add(createTextItem(R.string.tweaked_item_view_label, TweakedItemViewActivity.class));
        adapter.add(createTextItem(R.string.segmented_label, SegmentedActivity.class));
        adapter.add(createTextItem(R.string.action_bar_activity_label, ActionBarActivity.class));
        adapter.add(createTextItem(R.string.quick_action_label, QuickActionActivity.class));
        adapter.add(createTextItem(R.string.simple_async_image_view_label, SimpleAsyncImageViewActivity.class));
        adapter.add(createTextItem(R.string.async_image_view_list_view_label, AsyncImageViewListActivity.class));
        adapter.add(createTextItem(R.string.map_pin_drawable_label, MapPinMapActivity.class));
        adapter.add(createTextItem(R.string.paged_view_label, PagedViewActivity.class));
*/
        
        setListAdapter(adapter);

        //Get the listView ( from: ListActivity )
        final ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        registerForContextMenu(getListView());

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
        
    }

    private TextItem createTextItem(int stringId, Class<?> klass) {
        final TextItem textItem = new TextItem(getString(stringId));
        textItem.setTag(klass);
        return textItem;
    }

    private TextItem createTextItem(int stringId) {
        final TextItem textItem = new TextItem(getString(stringId));
        return textItem;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	final TextItem textItem = (TextItem) l.getAdapter().getItem(position);
    	 
    	if(textItem.text.equals(getString(R.string.purchase_label))){
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl));
            startActivity(intent);
    	}else if(textItem.text.equals(getString(R.string.donate_label))){
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(donateUrl));
            startActivity(intent);
    	}else if(textItem.text.equals(getString(R.string.rate_label))){
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl));
            startActivity(intent);
    	}else if(textItem.text.equals(getString(R.string.share_label))){
    		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        	sharingIntent.setType("text/plain");
        	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I Love Quran360 for Android");
        	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I Love Quran360 for Android");
        	Intent chooser = Intent.createChooser(sharingIntent, "Share with");
        	startActivity(chooser);
    	}else if(textItem.text.equals(getString(R.string.visit_label))){
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
            startActivity(intent);
    	}else if(textItem.text.equals(getString(R.string.follow_label))){
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(followUrl));
            startActivity(intent);
    	}else if(textItem.text.equals(getString(R.string.feedback_label))){
    		/* Create the Intent */
    		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

    		/* Fill it with Data */
    		emailIntent.setType("plain/text");
    		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@quran360.com"});
    		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Quran360 Android");
    		//emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

    		/* Send it off to the Activity-Chooser */
    		startActivity(Intent.createChooser(emailIntent, "Quran360 Feedback"));
    	}else{
    		/*
	        Intent intent = new Intent(Quran360Activity.this, (Class<?>) textItem.getTag());
	        intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, textItem.text);
	        startActivity(intent);
	        */
    	}
        
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
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
}
