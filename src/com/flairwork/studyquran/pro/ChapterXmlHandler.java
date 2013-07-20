package com.flairwork.studyquran.pro;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ChapterXmlHandler extends DefaultHandler {
	 
    // Fields. Change these to match your own tags.
    protected boolean in_chapters = false;
    protected boolean in_chapter = false;
    protected boolean in_ID = false;
    protected boolean in_SuraID = false;
    protected boolean in_DatabaseID = false;
    protected boolean in_SuraName = false;

    protected int count = 0;
    
    protected ChapterParsedXmlDataSet chapterParsedXmlDataSet = new ChapterParsedXmlDataSet();

    @Override
    public void startDocument() throws SAXException {
            this.chapterParsedXmlDataSet = new ChapterParsedXmlDataSet();
    }

    @Override
    public void endDocument() throws SAXException {}

    
    //Start Tag: <
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) 
    		throws SAXException {

    	if (localName.equals("chapters")) {
                this.in_chapters = true;
        }else if (localName.equals("chapter")) {
                this.in_chapter = true;
        }else if (localName.equals("ID")) {
            this.in_ID = true;                
        }else if (localName.equals(QuranDbAdapter.CHAPTER_CHAPTER_ID)) {
                this.in_SuraID = true;
        }else if (localName.equals(QuranDbAdapter.CHAPTER_LANG_CODE)) {
            this.in_DatabaseID = true;                
        }else if (localName.equals(QuranDbAdapter.CHAPTER_NAME)) {
                this.in_SuraName = true;
        }
    }
    
    
    //End Tag: />
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (localName.equals("chapters")) {
            this.in_chapters = false;
	    } else if (localName.equals("chapter")) {
	        this.in_chapter = false;
	        count++;
	    } else if (localName.equals("ID")) {
	    	this.in_ID = false;	        
	    } else if (localName.equals(QuranDbAdapter.CHAPTER_CHAPTER_ID)) {
	    	this.in_SuraID = false;
	    } else if (localName.equals(QuranDbAdapter.CHAPTER_LANG_CODE)) {
	    	this.in_DatabaseID = false;	    	
	    } else if (localName.equals(QuranDbAdapter.CHAPTER_NAME)) {
	    	this.in_SuraName = false;
	    }
    }
    
    public ChapterParsedXmlDataSet getParsedData() {
        return this.chapterParsedXmlDataSet;
    }
    
	@Override
	public void characters(char ch[], int start, int length) {
		if(this.in_ID) {
		    chapterParsedXmlDataSet.ID(count,new String(ch, start, length));
		}else if(this.in_SuraID) {
		    chapterParsedXmlDataSet.SuraID(count,new String(ch, start, length));
		}else if(this.in_DatabaseID) {
		    chapterParsedXmlDataSet.DatabaseID(count,new String(ch, start, length));		    
		} else if(this.in_SuraName) {
		    chapterParsedXmlDataSet.SuraName(count,new String(ch, start, length));
		}
	}
}
