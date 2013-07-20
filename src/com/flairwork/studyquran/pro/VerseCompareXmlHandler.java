package com.flairwork.studyquran.pro;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class VerseCompareXmlHandler extends DefaultHandler {
	 
    // Fields. Change these to match your own tags.
    protected boolean in_verses = false;
    protected boolean in_verse = false;
    protected boolean in_ID = false;
    protected boolean in_TranslationName = false;
    protected boolean in_SuraID = false;
    protected boolean in_VerseID = false;
    protected boolean in_AyahText = false;

    protected int count = 0;
    
    protected VerseCompareParsedXmlDataSet verseCompareParsedXmlDataSet = new VerseCompareParsedXmlDataSet();

    @Override
    public void startDocument() throws SAXException {
            this.verseCompareParsedXmlDataSet = new VerseCompareParsedXmlDataSet();
    }

    @Override
    public void endDocument() throws SAXException {}

    
    //Start Tag: <
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) 
    		throws SAXException {

    	if (localName.equals("verses")) {
                this.in_verses = true;
        }else if (localName.equals("verse")) {
                this.in_verse = true;
        }else if (localName.equals("ID")) {
            this.in_ID = true;         
        }else if (localName.equals("TranslationName")) {
            this.in_TranslationName = true;              
        }else if (localName.equals("SuraID")) {
                this.in_SuraID = true;
        }else if (localName.equals("VerseID")) {
            this.in_VerseID = true;                
        }else if (localName.equals("AyahText")) {
                this.in_AyahText = true;
        }
    }
    
    
    //End Tag: />
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (localName.equals("verses")) {
            this.in_verses = false;
	    } else if (localName.equals("verse")) {
	        this.in_verse = false;
	        count++;
	    } else if (localName.equals("ID")) {
	    	this.in_ID = false;	 
	    } else if (localName.equals("TranslationName")) {
	    	this.in_TranslationName = false;	 	    	
	    } else if (localName.equals("SuraID")) {
	    	this.in_SuraID = false;
	    } else if (localName.equals("VerseID")) {
	    	this.in_VerseID = false;	    	
	    } else if (localName.equals("AyahText")) {
	    	this.in_AyahText = false;
	    }
    }
    
    public VerseCompareParsedXmlDataSet getParsedData() {
        return this.verseCompareParsedXmlDataSet;
    }
    
	@Override
	public void characters(char ch[], int start, int length) {
		if(this.in_ID) {
		    verseCompareParsedXmlDataSet.ID(count,new String(ch, start, length));
		}else if(this.in_TranslationName) {
		    verseCompareParsedXmlDataSet.TranslationName(count,new String(ch, start, length));		    
		}else if(this.in_SuraID) {
		    verseCompareParsedXmlDataSet.SuraID(count,new String(ch, start, length));
		}else if(this.in_VerseID) {
		    verseCompareParsedXmlDataSet.VerseID(count,new String(ch, start, length));		    
		} else if(this.in_AyahText) {
		    verseCompareParsedXmlDataSet.AyahText(count,new String(ch, start, length));
		}
	}
}
