package com.flairwork.studyquran.pro;

import com.flairwork.studyquran.pro.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ChapterCursorAdapter extends SimpleCursorAdapter{
	
	private Context context;

	private Cursor currentCursor;
	
	String[] from;
	
	int[] to;
	
	public ChapterCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.currentCursor = c;
		this.context = context;
		this.from = from;
		this.to = to;
	}
	
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.read_list, null);
		}
		this.currentCursor.moveToPosition(pos);
		
		TextView chapterIDTextView = (TextView) v.findViewById(to[1]);
		chapterIDTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[1])));

		TextView chapterNameTextView = (TextView) v.findViewById(to[2]);
		chapterNameTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[2])));

		TextView chapterOrderTextView = (TextView) v.findViewById(to[3]);
		chapterOrderTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[3])));

		TextView chapterEnNameTextView = (TextView) v.findViewById(to[4]);
		chapterEnNameTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[4])));

		TextView chapterVerseCountTextView = (TextView) v.findViewById(to[5]);
		chapterVerseCountTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[5])));
					
		/*
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		TextView suraIDTextView = (TextView) v.findViewById(to[0]);
		suraIDTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[0])));
		
		TextView suraNameTextView = (TextView) v.findViewById(to[1]);
		suraNameTextView.setTextSize(Float.parseFloat(prefs.getString("transChapterFont", "14")));
		suraNameTextView.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(from[1])));
		
		if(prefs.getBoolean("isArabicChapters", true)){ 
		//if(Config.SHOW_ARABIC_CHAPTER){			
			//Typeface arabicFont = Typeface.createFromAsset(context.getAssets(), "fonts/q.ttf");
			Typeface arabicFont = Typeface.createFromAsset(context.getAssets(), "fonts/UthmanicHafs1.otf");
			
			TextView arabicTextView =(TextView) v.findViewById(to[2]);
			arabicTextView.setTypeface(arabicFont);
			//arabicTextView.setText(ArabicUtilities.reshape(this.currentCursor.getString(this.currentCursor
				//	.getColumnIndex(from[2])))); 
			arabicTextView.setTextSize(Float.parseFloat(prefs.getString("arabicChapterFont", "18")));
			//arabicTextView.setText(this.currentCursor.getString(this.currentCursor.getColumnIndex(from[2])));
			arabicTextView.setText(DariGlyphUtils.reshapeText(this.currentCursor.getString(this.currentCursor
						.getColumnIndex(from[2]))));
		}
		*/
		return (v);
	}


}
