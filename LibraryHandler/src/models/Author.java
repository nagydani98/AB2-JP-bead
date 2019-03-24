package models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Author {
	private String name;
	private int authorIDCode;
	
	public Author(String name, int authorIDCode) {
		super();
		this.name = name;
		this.authorIDCode = authorIDCode;
	}
	
	public Author() {
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public String toString() {
		return "Szerzõ Adatai: " + name + " " + authorIDCode;
	}
	
	//methods for txt file operations
	
	public String toCSVString() {
		return authorIDCode + "," + name;
	}
	
	public String toFileString(String delimiter) {
		return authorIDCode + delimiter + name;
	}
	
	public static Author authorFromCSVString(String line) {
		Author ret = new Author();
		String[] data = line.split(",");
		
		ret.authorIDCode = Integer.parseInt(data[0]);
		ret.name = data[1];
		return ret;
	}
	
	public static Author authorFromFileString(String line, String delimiter) {
		Author ret = new Author();
		String[] data = line.split(Pattern.quote(delimiter));
		
		ret.authorIDCode = Integer.parseInt(data[0]);
		ret.name = data[1];
		return ret;
	}
	
	//methods for xml operations
	
	public static void convertAndAppendAuthors(Element authorsOfBook, Document doc, ArrayList<Author> authors) {
		for (Author author : authors) {
			Element authorElement = doc.createElement("Szerzo");
			authorsOfBook.appendChild(authorElement);
			
			Element authorCodeElement = doc.createElement("SzKod");
			authorCodeElement.setTextContent("" + author.authorIDCode);
			authorElement.appendChild(authorCodeElement);
			
			Element authorNameElement = doc.createElement("Nev");
			authorNameElement.setTextContent(author.name);
			authorElement.appendChild(authorNameElement);
		}
	}
	
	public static ArrayList<Author> authorListFromElementList(ArrayList<Element> elementList) {
		ArrayList<Author> returnlist = new ArrayList<>();
		
			String[] dataArray = new String[2];
			
			for (int i = 0; i < elementList.size(); i++) {
				Element authorElement = elementList.get(i);
				Author author = new Author();
				
				if(authorElement.getNodeName().equals("Szerzo")) {
				
					dataArray[0] = authorElement.getElementsByTagName("SzKod").item(0).getTextContent();
					dataArray[1] = authorElement.getElementsByTagName("Nev").item(0).getTextContent();
					
				}
			
				
			try {
				author.authorIDCode = Integer.parseInt(dataArray[0]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			author.name = dataArray[1];
			
			returnlist.add(author);
		
			}
		return returnlist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAuthorIDCode() {
		return authorIDCode;
	}

	public void setAuthorIDCode(int authorIDCode) {
		this.authorIDCode = authorIDCode;
	}
	
}
