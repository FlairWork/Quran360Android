package com.flairwork.studyquran.pro;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class VerseXmlHandler extends DefaultHandler {
	 
    // Fields. Change these to match your own tags.
    protected boolean in_verses = false;
    protected boolean in_verse = false;
    protected boolean in_id = false;
    protected boolean in_translation_id = false;
    protected boolean in_chapter_id = false;
    protected boolean in_verse_id = false;
    protected boolean in_verse_text = false;
    protected boolean in_arabic_text = false;

    protected int count = 0;
    
    protected VerseParsedXmlDataSet verseParsedXmlDataSet = new VerseParsedXmlDataSet();

    @Override
    public void startDocument() throws SAXException {
            this.verseParsedXmlDataSet = new VerseParsedXmlDataSet();
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
        }else if (localName.equals("id")) {
            this.in_id = true;                
        }else if (localName.equals("translation_id")) {
            this.in_translation_id = true;
        }else if (localName.equals("chapter_id")) {
                this.in_chapter_id = true;
        }else if (localName.equals("verse_id")) {
            this.in_verse_id = true;                
        }else if (localName.equals("verse_text")) {
            this.in_verse_text = true;
        }else if (localName.equals("arabic_text")) {
                this.in_arabic_text = true;
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
	    } else if (localName.equals("id")) {
	    	this.in_id = false;	        
	    } else if (localName.equals("translation_id")) {
	    	this.in_translation_id = false;
	    } else if (localName.equals("chapter_id")) {
	    	this.in_chapter_id = false;
	    } else if (localName.equals("verse_id")) {
	    	this.in_verse_id = false;	    	
	    } else if (localName.equals("verse_text")) {
	    	this.in_verse_text = false;
	    } else if (localName.equals("arabic_text")) {
	    	this.in_arabic_text = false;
	    }
    }
    
    public VerseParsedXmlDataSet getParsedData() {
        return this.verseParsedXmlDataSet;
    }
    
	@Override
	public void characters(char ch[], int start, int length) {
		if(this.in_id) {
		    verseParsedXmlDataSet.ID(count,new String(ch, start, length));
		}else if(this.in_translation_id) {
		    verseParsedXmlDataSet.TranslationID(count,new String(ch, start, length));
		}else if(this.in_chapter_id) {
		    verseParsedXmlDataSet.ChapterID(count,new String(ch, start, length));
		}else if(this.in_verse_id) {
		    verseParsedXmlDataSet.VerseID(count,new String(ch, start, length));		    
		} else if(this.in_verse_text) {
		    verseParsedXmlDataSet.VerseText(count,new String(ch, start, length));
		} else if(this.in_arabic_text) {
		    verseParsedXmlDataSet.ArabicText(count,new String(ch, start, length));
		}
	}
}
