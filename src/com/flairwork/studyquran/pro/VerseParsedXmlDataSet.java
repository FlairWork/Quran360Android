package com.flairwork.studyquran.pro;

import java.util.ArrayList;

public class VerseParsedXmlDataSet {
	protected ArrayList<String> IDs = new ArrayList<String>();
	protected ArrayList<String> translationIDs = new ArrayList<String>();
	protected ArrayList<String> chapterIDs = new ArrayList<String>();
	protected ArrayList<String> verseIDs = new ArrayList<String>();
	protected ArrayList<String> verseTexts = new ArrayList<String>();
	protected ArrayList<String> arabicTexts = new ArrayList<String>();

	public void ID(int index, String data) {
		IDs.add(index,data);
	}

	public void TranslationID(int index, String data) {
		translationIDs.add(index,data);
	}
	
	public void ChapterID(int index, String data) {
		chapterIDs.add(index,data);
	}
	
	public void VerseID(int index, String data) {
		verseIDs.add(index,data);
	}
	
	public void VerseText(int index, String data) {
		verseTexts.add(index,data);
	}
	
	public void ArabicText(int index, String data) {
		arabicTexts.add(index,data);
	}
	
	   
	public String toString() {
	     StringBuilder ret = new StringBuilder();
	     for (int i=0; i<IDs.size(); i++) {
	            ret.append(IDs.get(i)+"  "+translationIDs.get(i)+"  "+chapterIDs.get(i)+"  "+verseIDs.get(i)+"  "+verseTexts.get(i)+"   "+arabicTexts.get(i)+"\n");
	     } 
	     return ret.toString();
	}
}
