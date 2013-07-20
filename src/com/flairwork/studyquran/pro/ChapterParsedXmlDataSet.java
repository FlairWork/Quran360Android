package com.flairwork.studyquran.pro;

import java.util.ArrayList;

public class ChapterParsedXmlDataSet {
	protected ArrayList<String> IDs = new ArrayList<String>();
	protected ArrayList<String> suraIDs = new ArrayList<String>();
	protected ArrayList<String> databaseIDs = new ArrayList<String>();
	protected ArrayList<String> suraNames = new ArrayList<String>();
	
	public void ID(int index, String data) {
		IDs.add(index,data);
	}
	
	public void SuraID(int index, String data) {
		suraIDs.add(index,data);
	}
	
	public void DatabaseID(int index, String data) {
		databaseIDs.add(index,data);
	}
	
	public void SuraName(int index, String data) {
		suraNames.add(index,data);
	}

	   
	public String toString() {
	     StringBuilder ret = new StringBuilder();
	     for (int i=0;i<IDs.size();i++) {
	    	 ret.append(IDs.get(i)+"   "+suraIDs.get(i)+"   "+databaseIDs.get(i)+"  "+suraNames.get(i)+"\n");
	     }
	     return ret.toString();
	}
}
