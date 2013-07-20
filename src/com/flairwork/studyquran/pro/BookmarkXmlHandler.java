package com.flairwork.studyquran.pro;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BookmarkXmlHandler extends DefaultHandler {
	 
    // Fields. Change these to match your own tags.
    protected boolean in_bookmarks = false;
    protected boolean in_bookmark = false;
    protected boolean in_BookmarkName = false;
    protected boolean in_SuraID = false;
    protected boolean in_VerseID = false;
    protected boolean in_PositionID = false;
    protected boolean in_IsDeleted = false;
    protected boolean in_DateCreated = false;
    protected boolean in_DateModified = false;

    protected int count = 0;
    
    protected BookmarkParsedXmlDataSet bookmarkParsedXmlDataSet = new BookmarkParsedXmlDataSet();

    @Override
    public void startDocument() throws SAXException {
            this.bookmarkParsedXmlDataSet = new BookmarkParsedXmlDataSet();
    }

    @Override
    public void endDocument() throws SAXException {}

    
    //Start Tag: <
    @Override
    public void startElement(String namespaceURI, String localBookmarkName, String qBookmarkName, Attributes atts) 
    		throws SAXException {

    	if (localBookmarkName.equals("bookmarks")) {
                this.in_bookmarks = true;
        }else if (localBookmarkName.equals("bookmark")) {
                this.in_bookmark = true;
        }else if (localBookmarkName.equals("BookmarkName")) {
            this.in_BookmarkName = true;                
        }else if (localBookmarkName.equals("SuraID")) {
                this.in_SuraID = true;
        }else if (localBookmarkName.equals("VerseID")) {
            this.in_VerseID = true;    
        }else if (localBookmarkName.equals("PositionID")) {
            this.in_PositionID = true; 
        }else if (localBookmarkName.equals("IsDeleted")) {
            this.in_IsDeleted = true;                 
        }else if (localBookmarkName.equals("DateCreated")) {
                this.in_DateCreated = true;
        }else if (localBookmarkName.equals("DateModified")) {
                this.in_DateModified = true;
        }    	
    }
    
    
    //End Tag: />
    @Override
    public void endElement(String namespaceURI, String localBookmarkName, String qBookmarkName) throws SAXException {
        if (localBookmarkName.equals("bookmarks")) {
            this.in_bookmarks = false;
	    } else if (localBookmarkName.equals("bookmark")) {
	        this.in_bookmark = false;
	        count++;
	    } else if (localBookmarkName.equals("BookmarkName")) {
	    	this.in_BookmarkName = false;	        
	    } else if (localBookmarkName.equals("SuraID")) {
	    	this.in_SuraID = false;
	    } else if (localBookmarkName.equals("VerseID")) {
	    	this.in_VerseID = false;	    
	    } else if (localBookmarkName.equals("PositionID")) {
	    	this.in_PositionID = false;	
	    } else if (localBookmarkName.equals("IsDeleted")) {
	    	this.in_IsDeleted = false;		    	
	    } else if (localBookmarkName.equals("DateCreated")) {
	    	this.in_DateCreated = false;
	    } else if (localBookmarkName.equals("DateModified")) {
	    	this.in_DateModified = false;
	    }
    }
    
    public BookmarkParsedXmlDataSet getParsedData() {
        return this.bookmarkParsedXmlDataSet;
    }
    
	@Override
	public void characters(char ch[], int start, int length) {
		if(this.in_BookmarkName) {
		    bookmarkParsedXmlDataSet.BookmarkName(count,new String(ch, start, length));
		}else if(this.in_SuraID) {
		    bookmarkParsedXmlDataSet.SuraID(count,new String(ch, start, length));
		}else if(this.in_VerseID) {
		    bookmarkParsedXmlDataSet.VerseID(count,new String(ch, start, length));		
		}else if(this.in_PositionID) {
		    bookmarkParsedXmlDataSet.PositionID(count,new String(ch, start, length));	
		}else if(this.in_IsDeleted) {
		    bookmarkParsedXmlDataSet.IsDeleted(count,new String(ch, start, length));			    
		} else if(this.in_DateCreated) {
		    bookmarkParsedXmlDataSet.DateCreated(count,new String(ch, start, length));
		} else if(this.in_DateModified) {
		    bookmarkParsedXmlDataSet.DateModified(count,new String(ch, start, length));
		}
	}
}
