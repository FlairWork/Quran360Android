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

import com.flairwork.studyquran.pro.widget.HeadedTextItem;
import com.flairwork.studyquran.pro.R;

import greendroid.widget.ItemAdapter;
import greendroid.widget.item.Item;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteCursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopicsTopActivity extends  ListActivity  {

    private static final String SECTIONS = "(ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private static final int ACTIVITY_TOPIC_ITEM = 0;
    
	protected QuranDbAdapter myDBHelper;
	protected Cursor cursor;
	
    private ClipboardManager clipboard;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
	        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	        
	        setContentView(R.layout.topic);

	        //setTitle("Topics in the Quran");

	        myDBHelper = new QuranDbAdapter(this);
	        
	        fillData();
	        
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
        cursor = myDBHelper.fetchTopics("top");
        startManagingCursor(cursor);
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] item_from = new String[] {QuranDbAdapter.TOPIC_TOPIC_TITLE, QuranDbAdapter.TOPIC_VERSE_COUNT };
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] item_to = new int[] { R.id.title, R.id.verse_count};
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter items = new SimpleCursorAdapter(this, R.layout.topic_list, cursor, item_from, item_to);
        // = new ChapterCursorAdapter(this, R.layout.read_list, cursor, chapters_from, chapters_to);
        getListView().setFastScrollEnabled(true);
        
        setListAdapter(items);
        
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SQLiteCursor cursor = (SQLiteCursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);

        Long topicId = cursor.getLong(0);
        String topicTitle = cursor.getString(1);
        
        Toast.makeText(getApplicationContext(), topicId+"|"+topicTitle, Toast.LENGTH_SHORT).show();
        
        Intent i = new Intent(this, TopicItemActivity.class);
        i.putExtra(QuranDbAdapter.TOPIC_ID, topicId);
        i.putExtra(QuranDbAdapter.TOPIC_TOPIC_TITLE, topicTitle);
        startActivityForResult(i, ACTIVITY_TOPIC_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	//fillData();
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

    /**
     * A SectionedItemAdapter is an extension of an ItemAdapter that implements
     * SectionIndexer. Thanks to it, the fast scroller will act like the one in
     * the contact app.
     * 
     * @author Cyril Mottier
     */
    private class SectionedItemAdapter extends ItemAdapter implements SectionIndexer {

        private AlphabetIndexer mIndexer;

        public SectionedItemAdapter(Context context, Item[] items, String sections) {
            super(context, items);
            mIndexer = new AlphabetIndexer(new FakeCursor(this), 0, sections);
        }

        public int getPositionForSection(int sectionIndex) {
            return mIndexer.getPositionForSection(sectionIndex);
        }

        public int getSectionForPosition(int position) {
            return mIndexer.getSectionForPosition(position);
        }

        public Object[] getSections() {
            return mIndexer.getSections();
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            final HeadedTextItem item = (HeadedTextItem) getItem(position);
            final int section = getSectionForPosition(position);

            if (getPositionForSection(section) == position) {
                String title = mIndexer.getSections()[section].toString().trim();
                item.headerText = title;
            } else {
                item.headerText = null;
            }

            return super.getView(position, convertView, parent);
        }

    }

    /**
     * An implementation of a Cursor that is almost useless. It is simply used
     * for the SectionIndexer to browse our underlying data.
     * 
     * @author Cyril Mottier
     */ 
    private class FakeCursor implements Cursor {

        private ListAdapter mAdapter;
        private int mPosition;

        public FakeCursor(ListAdapter adapter) {
            mAdapter = adapter;
        }

        public void close() {
        }

        public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        }

        public void deactivate() {
        }

        public byte[] getBlob(int columnIndex) {
            return null;
        }

        public int getColumnCount() {
            return 0;
        }

        public int getColumnIndex(String columnName) {
            return 0;
        }

        public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
            return 0;
        }

        public String getColumnName(int columnIndex) {
            return null;
        }

        public String[] getColumnNames() {
            return null;
        }

        public int getCount() {
            return mAdapter.getCount();
        }

        public double getDouble(int columnIndex) {
            return 0;
        }

        public Bundle getExtras() {
            return null;
        }

        public float getFloat(int columnIndex) {
            return 0;
        }

        public int getInt(int columnIndex) {
            return 0;
        }

        public long getLong(int columnIndex) {
            return 0;
        }

        public int getPosition() {
            return 0;
        }

        public short getShort(int columnIndex) {
            return 0;
        }

        public String getString(int columnIndex) {
            final HeadedTextItem item = (HeadedTextItem) mAdapter.getItem(mPosition);
            return (String) item.text.substring(0, 1);
        }

        public boolean getWantsAllOnMoveCalls() {
            return false;
        }

        public boolean isAfterLast() {
            return false;
        }

        public boolean isBeforeFirst() {
            return false;
        }

        public boolean isClosed() {
            return false;
        }

        public boolean isFirst() {
            return false;
        }

        public boolean isLast() {
            return false;
        }

        public boolean isNull(int columnIndex) {
            return false;
        }

        public boolean move(int offset) {
            return false;
        }

        public boolean moveToFirst() {
            return false;
        }

        public boolean moveToLast() {
            return false;
        }

        public boolean moveToNext() {
            return false;
        }

        public boolean moveToPosition(int position) {
            if (position < -1 || position > getCount()) {
                return false;
            }
            mPosition = position;
            return true;
        }

        public boolean moveToPrevious() {
            return false;
        }

        public void registerContentObserver(ContentObserver observer) {
        }

        public void registerDataSetObserver(DataSetObserver observer) {
        }

        public boolean requery() {
            return false;
        }

        public Bundle respond(Bundle extras) {
            return null;
        }

        public void setNotificationUri(ContentResolver cr, Uri uri) {
        }

        public void unregisterContentObserver(ContentObserver observer) {
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

		@Override
		public int getType(int columnIndex) {
			// TODO Auto-generated method stub
			return 0;
		}
    }

}
