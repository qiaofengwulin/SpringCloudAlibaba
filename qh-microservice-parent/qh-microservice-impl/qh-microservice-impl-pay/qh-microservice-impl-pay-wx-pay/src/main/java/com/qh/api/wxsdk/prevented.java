package com.qh.api.wxsdk;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class prevented {

	public static DocumentBuilder preventedXML() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String FEATURE = null;
		try {
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed,
			// almost all XML entity attacks are prevented
			// Xerces 2 only -
			// http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl

			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(FEATURE, true);

			// If you can't completely disable DTDs, then at least do the
			// following:
			// Xerces 1 -
			// http://xerces.apache.org/xerces-j/features.html#external-general-entities

			// Xerces 2 -
			// http://xerces.apache.org/xerces2-j/features.html#external-general-entities

			// JDK7+ - http://xml.org/sax/features/external-general-entities
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(FEATURE, false);

			// Xerces 1 -
			// http://xerces.apache.org/xerces-j/features.html#external-parameter-entities

			// Xerces 2 -
			// http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities

			// JDK7+ - http://xml.org/sax/features/external-parameter-entities
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(FEATURE, false);

			// Disable external DTDs as well
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(FEATURE, false);

			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema,
			// DTD, and Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);

			// And, per Timothy Morgan: "If for some reason support for inline
			// DOCTYPEs are a requirement, then
			// ensure the entity settings are disabled (as shown above) and
			// beware that SSRF attacks
			// (http://cwe.mitre.org/data/definitions/918.html) and denial
			// of service attacks (such as billion laughs or decompression bombs
			// via "jar:") are a risk."

			// remaining parser logic
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			DocumentBuilder safebuilder = dbf.newDocumentBuilder();
			return safebuilder;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static Map<String, String> getPreventedXML(InputStream inStream) {
		DocumentBuilder docu = prevented.preventedXML();
		Document document;
		try {
			document = docu.parse(inStream);
			org.dom4j.io.DOMReader xmlReader = new org.dom4j.io.DOMReader();
			org.dom4j.Document d = xmlReader.read(document);
			Map<String, String> resultMap = WXPayUtil.xmlToMap(d.asXML());
			return resultMap;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
