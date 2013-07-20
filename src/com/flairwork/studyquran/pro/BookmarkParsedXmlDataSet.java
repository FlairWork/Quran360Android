package com.flairwork.studyquran.pro;

import java.util.ArrayList;

public class BookmarkParsedXmlDataSet {
	protected ArrayList<String> bookmarkNames = new ArrayList<String>();
	protected ArrayList<String> suraIDs = new ArrayList<String>();
	protected ArrayList<String> verseIDs = new ArrayList<String>();
	protected ArrayList<String> positionIDs = new ArrayList<String>();
	protected ArrayList<String> isDeleteds = new ArrayList<String>();
	protected ArrayList<String> dateCreateds = new ArrayList<String>();
	protected ArrayList<String> dateModifieds = new ArrayList<String>();

	public void BookmarkName(int index, String data) {
		bookmarkNames.add(index,data);
	}
	
	public void SuraID(int index, String data) {
		suraIDs.add(index,data);
	}
	
	public void VerseID(int index, String data) {
		verseIDs.add(index,data);
	}
	
	public void PositionID(int index, String data) {
		positionIDs.add(index,data);
	}
	
	public void IsDeleted(int index, String data) {
		isDeleteds.add(index,data);
	}
	
	public void DateCreated(int index, String data) {
		dateCreateds.add(index,data);
	}
	public void DateModified(int index, String data) {
		dateModifieds.add(index,data);
	}
	
	   
	public String toString() {
	     StringBuilder ret = new StringBuilder();
	     for (int i=0;i<suraIDs.size();i++) {
	            ret.append(bookmarkNames.get(i)+"  "+suraIDs.get(i)+"  "+verseIDs.get(i)+"   "+positionIDs.get(i)+"		"+isDeleteds.get(i)+"	"+dateCreateds.get(i)+"		"+dateModifieds.get(i)+"\n");
	     }
	     return ret.toString();
	}
}
