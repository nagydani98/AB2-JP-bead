package iohandlers;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import models.Member;

public class XMLParser {

	public static ArrayList<Element> parseDocument(String path) {
		ArrayList<Element> returnlist = new ArrayList<>();
		try {
			
			File file = null;
			
			if((!path.isEmpty()) && path.contains(".xml")) {
			file = new File(path);
			}
			
			if(file.isFile()) {
				
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(file);
				
				Element docelement = doc.getDocumentElement();
				
				for (int i = 0; i < docelement.getChildNodes().getLength(); i++) {
					if(!(docelement.getChildNodes().item(i).getNodeName().equals("#text"))) {
					Element childElement = (Element) docelement.getChildNodes().item(i);
					
					returnlist.add(childElement);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return returnlist;
	}
	
	public static Document createDocument() {
		Document doc = null;
		try {
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			

		} 
		catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}
		
		return doc;
	}

	public static void saveDocument(Document doc, String path) {
		try {
			
			DOMSource source = new DOMSource(doc);
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    
		    
		    
		    StreamResult result = new StreamResult(path);
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.transform(source, result);
		    

		} 
		catch (TransformerException e) {
			
			e.printStackTrace();
		}
	}
	
}
