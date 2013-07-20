package com.flairwork.studyquran.pro;

import java.util.ArrayList;

public class SearchParsedXmlDataSet {
	protected ArrayList<String> IDs = new ArrayList<String>();
	protected ArrayList<String> searchTexts = new ArrayList<String>();
	protected ArrayList<String> databaseIDs = new ArrayList<String>();
	protected ArrayList<String> isDeleteds = new ArrayList<String>();
	protected ArrayList<String> dateCreateds = new ArrayList<String>();
	protected ArrayList<String> dateModifieds = new ArrayList<String>();

	public void ID(int index, String data) {
		IDs.add(index,data);
	}
	
	public void SearchText(int index, String data) {
		searchTexts.add(index,data);
	}
	
	public void DatabaseID(int index, String data) {
		databaseIDs.add(index,data);
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
	     for (int i=0;i<searchTexts.size();i++) {
	            ret.append(IDs.get(i)+"  "+searchTexts.get(i)+"   "+databaseIDs.get(i)+"	"+isDeleteds.get(i)+"	"+dateCreateds.get(i)+"		"+dateModifieds.get(i)+"\n");
	     }
	     return ret.toString();
	}
}
