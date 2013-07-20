package com.flairwork.studyquran.pro;

import java.util.ArrayList;

public class VerseCompareParsedXmlDataSet {
	protected ArrayList<String> IDs = new ArrayList<String>();
	protected ArrayList<String> translationNames = new ArrayList<String>();
	protected ArrayList<String> suraIDs = new ArrayList<String>();
	protected ArrayList<String> verseIDs = new ArrayList<String>();
	protected ArrayList<String> ayahTexts = new ArrayList<String>();

	public void ID(int index, String data) {
		IDs.add(index,data);
	}
	
	public void TranslationName(int index, String data) {
		translationNames.add(index,data);
	}
	
	public void SuraID(int index, String data) {
		suraIDs.add(index,data);
	}
	
	public void VerseID(int index, String data) {
		verseIDs.add(index,data);
	}
	
	public void AyahText(int index, String data) {
		ayahTexts.add(index,data);
	}
	
	   
	public String toString() {
	     StringBuilder ret = new StringBuilder();
	     for (int i=0;i<suraIDs.size();i++) {
	            ret.append(IDs.get(i)+"  "+translationNames.get(i)+"  "+suraIDs.get(i)+"  "+verseIDs.get(i)+"   "+ayahTexts.get(i)+"\n");
	     }
	     return ret.toString();
	}
}
