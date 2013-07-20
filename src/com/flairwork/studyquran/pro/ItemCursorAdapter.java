package com.flairwork.studyquran.pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ItemCursorAdapter extends SimpleCursorAdapter{
	
	private Context context;

	private Cursor currentCursor;
	
	private int layout;
	
	private String[] from;
	
	private int[] to;

    private Typeface arabicFont;
    
	public ItemCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, Typeface arabicFont) {
		super(context, layout, c, from, to);
		this.currentCursor = c;
		this.context = context;
		this.layout = layout;
		this.from = from;
		this.to = to;
		this.arabicFont = arabicFont;
	}
	
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(layout, null);
		}
		this.currentCursor.moveToPosition(pos);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		TextView orderTextView = (TextView) v.findViewById(to[0]);
		orderTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[0])));

		TextView chapterIDTextView = (TextView) v.findViewById(to[3]);
		chapterIDTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[3])));
 
		TextView verseIDTextView = (TextView) v.findViewById(to[4]);
		verseIDTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[4])));

		if(prefs.getBoolean("isArabicVerses", true)){
		//if(Config.SHOW_ARABIC_VERSE){
			//Typeface arabicFont = Typeface.createFromAsset(context.getAssets(), "fonts/q.ttf");
			//Typeface arabicFont = Typeface.createFromAsset(context.getAssets(), "fonts/UthmanicHafs1.otf");
			
			TextView arabicTextView =(TextView) v.findViewById(to[1]);
			arabicTextView.setTypeface(arabicFont);
			//arabicTextView.setText(ArabicUtilities.reshape(this.currentCursor.getString(this.currentCursor
					//.getColumnIndex(from[2]))));
			arabicTextView.setTextSize(Float.parseFloat(prefs.getString("arabicVerseFont", "28")));
			//arabicTextView.setText(this.currentCursor.getString(this.currentCursor.getColumnIndex(from[2])));
			arabicTextView.setText(DariGlyphUtils.reshapeText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[1]))));
		} 
		
		if(prefs.getBoolean("isTransVerses", true)){
			TextView verseTextView = (TextView) v.findViewById(to[2]);
			verseTextView.setTextSize(Float.parseFloat(prefs.getString("transVerseFont", "18")));
			verseTextView.setText(this.currentCursor.getString(this.currentCursor
					.getColumnIndex(from[2])));
		}
		
		return (v);
	}
}
