package com.flairwork.studyquran.pro;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SearchXmlHandler extends DefaultHandler {
	 
    // Fields. Change these to match your own tags.
    protected boolean in_searches = false;
    protected boolean in_search = false;
    protected boolean in_SearchText = false;
    protected boolean in_DatabaseID = false;
    protected boolean in_IsDeleted = false;
    protected boolean in_DateCreated = false;
    protected boolean in_DateModified = false;

    protected int count = 0;
    
    protected SearchParsedXmlDataSet searchParsedXmlDataSet = new SearchParsedXmlDataSet();

    @Override
    public void startDocument() throws SAXException {
            this.searchParsedXmlDataSet = new SearchParsedXmlDataSet();
    }

    @Override
    public void endDocument() throws SAXException {}

    
    //Start Tag: <
    @Override
    public void startElement(String namespaceURI, String localSearchText, String qSearchText, Attributes atts) 
    		throws SAXException {

    	if (localSearchText.equals("searches")) {
                this.in_searches = true;
        }else if (localSearchText.equals("search")) {
                this.in_search = true;
        }else if (localSearchText.equals("SearchText")) {
            this.in_SearchText = true;                
        }else if (localSearchText.equals("DatabaseID")) {
                this.in_DatabaseID = true;
        }else if (localSearchText.equals("IsDeleted")) {
            this.in_IsDeleted = true;                 
        }else if (localSearchText.equals("DateCreated")) {
                this.in_DateCreated = true;
        }else if (localSearchText.equals("DateModified")) {
                this.in_DateModified = true;
        }    	
    }
    
    
    //End Tag: />
    @Override
    public void endElement(String namespaceURI, String localSearchText, String qSearchText) throws SAXException {
        if (localSearchText.equals("searches")) {
            this.in_searches = false;
	    } else if (localSearchText.equals("search")) {
	        this.in_search = false;
	        count++;
	    } else if (localSearchText.equals("SearchText")) {
	    	this.in_SearchText = false;	        
	    } else if (localSearchText.equals("DatabaseID")) {
	    	this.in_DatabaseID = false;
	    } else if (localSearchText.equals("IsDeleted")) {
	    	this.in_IsDeleted = false;		    	
	    } else if (localSearchText.equals("DateCreated")) {
	    	this.in_DateCreated = false;
	    } else if (localSearchText.equals("DateModified")) {
	    	this.in_DateModified = false;
	    }
    }
    
    public SearchParsedXmlDataSet getParsedData() {
        return this.searchParsedXmlDataSet;
    }
    
	@Override
	public void characters(char ch[], int start, int length) {
		if(this.in_SearchText) {
		    searchParsedXmlDataSet.SearchText(count,new String(ch, start, length));
		}else if(this.in_DatabaseID) {
		    searchParsedXmlDataSet.DatabaseID(count,new String(ch, start, length));
		}else if(this.in_IsDeleted) {
		    searchParsedXmlDataSet.IsDeleted(count,new String(ch, start, length));			    
		} else if(this.in_DateCreated) {
		    searchParsedXmlDataSet.DateCreated(count,new String(ch, start, length));
		} else if(this.in_DateModified) {
		    searchParsedXmlDataSet.DateModified(count,new String(ch, start, length));
		}
	}
}
