package com.example.myokhttp;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;

class MyContentHandler extends DefaultHandler {
    private StringBuilder id;
    private String nodename;
    private StringBuilder version;
    private StringBuilder name;

    public MyContentHandler() {
        super();
    }

    @Override
    public void startDocument() throws SAXException {//chushihua
        super.startDocument();
        id = new StringBuilder();
        name = new StringBuilder();
        version = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodename = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if("id".equals(nodename)){
            id.append(ch,start,length);
        }else if("name".equals(nodename)){
            name.append(ch,start,length);
        }else if("version".equals(nodename)){
            version.append(ch,start,length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if("app".equals(localName)){
            Log.d("mycontent", "从这开始" + id + name + version);
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
