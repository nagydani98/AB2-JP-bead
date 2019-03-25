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

public class Lend {
	private String memberIDCode;
	private String bookIDCode;
	private Date startOfLend;
	private Date endOfLend;
	
	public Lend(String memberIDCode, String bookIDCode, Date startOfLend, Date endOfLend) {
		super();
		this.memberIDCode = memberIDCode;
		this.bookIDCode = bookIDCode;
		this.startOfLend = startOfLend;
		this.endOfLend = endOfLend;
	}
	
	public Lend() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Kölcsönzés adatok: Tag Kód:" + memberIDCode + " Könyv kód:" + bookIDCode + " Kivételi Dátum" + startOfLend
				+ "Lejárati Dátum: " + endOfLend;
	}

	public String toCSVString() {
		return memberIDCode + "," + bookIDCode + "," + startOfLend
				+ "," + endOfLend;
	}
	
	public String toFileString(String delimiter) {
		return memberIDCode + delimiter + bookIDCode + delimiter + startOfLend
				+ delimiter + endOfLend;
	}

	public static Lend lendFromCSVString(String line) {
		Lend ret = new Lend();
		String[] data = line.split(",");
		
		ret.memberIDCode = data[0];
		ret.bookIDCode = data[1];
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(data[2], formatter);
		ret.startOfLend = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
		
		LocalDate date2 = LocalDate.parse(data[3], formatter);
		ret.endOfLend = new Date(date2.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
		return ret;
	}
	
	public static Lend lendFromFileString(String line, String delimiter) {
		Lend ret = new Lend();
		String[] data = line.split(Pattern.quote(delimiter));
		
		ret.memberIDCode = data[0];
		ret.bookIDCode = data[1];
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(data[2], formatter);
		ret.startOfLend = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
		
		LocalDate date2 = LocalDate.parse(data[3], formatter);
		ret.endOfLend = new Date(date2.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
		return ret;
	}

	//methods for xml operations
	public static void convertAndAppendLends(ArrayList<Lend> lendList, Document root) {
		Element lendRootElement = root.createElement("Kolcsonok");	
		for (Lend lend : lendList) {
			
			Element lendElement = root.createElement("Kolcson");
			
			Element memIDElement = root.createElement("TKod");
			memIDElement.setTextContent(lend.memberIDCode);
			lendElement.appendChild(memIDElement);

			Element bookIDElement = root.createElement("KKod");
			bookIDElement.setTextContent(lend.bookIDCode);
			lendElement.appendChild(bookIDElement);
			
			Element dateStartElement = root.createElement("KivDatum");
			dateStartElement.setTextContent(lend.startOfLend.toString());
			lendElement.appendChild(dateStartElement);
			
			Element dateEndElement = root.createElement("LejDatum");
			dateEndElement.setTextContent(lend.endOfLend.toString());
			lendElement.appendChild(dateEndElement);
			
			lendRootElement.appendChild(lendElement);
			}
		root.appendChild(lendRootElement);
	}
	
	public static ArrayList<Lend> lendListFromElementList(ArrayList<Element> elementList) {
		ArrayList<Lend> returnlist = new ArrayList<>();
		
			String[] dataArray = new String[4];
			
			for (int i = 0; i < elementList.size(); i++) {
				Element bookElement = elementList.get(i);
				Lend lend = new Lend();
				ArrayList<Element> authorsElementList = new ArrayList<>();
				
				if(bookElement.getNodeName().equals("Konyv")) {
				
					dataArray[0] = bookElement.getElementsByTagName("TKod").item(0).getTextContent();
					dataArray[1] = bookElement.getElementsByTagName("KKod").item(0).getTextContent();
					dataArray[2] = bookElement.getElementsByTagName("KivDatum").item(0).getTextContent();
					dataArray[3] = bookElement.getElementsByTagName("LejDatum").item(0).getTextContent();
				}
			
				lend.setMemberIDCode(dataArray[0]);
				lend.setBookIDCode(dataArray[1]);
				
			
			
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(dataArray[2], formatter);
				lend.startOfLend = (new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli()));
				
				LocalDate date2 = LocalDate.parse(dataArray[3], formatter);
				lend.endOfLend = (new Date(date2.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli()));
			} catch (DateTimeParseException | NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Olvasott " + lend.toString());
			returnlist.add(lend);
		
			}
		return returnlist;
	}
	
	public String getMemberIDCode() {
		return memberIDCode;
	}

	public void setMemberIDCode(String memberIDCode) {
		this.memberIDCode = memberIDCode;
	}

	public String getBookIDCode() {
		return bookIDCode;
	}

	public void setBookIDCode(String bookIDCode) {
		this.bookIDCode = bookIDCode;
	}

	public Date getStartOfLend() {
		return startOfLend;
	}

	public void setStartOfLend(Date startOfLend) {
		this.startOfLend = startOfLend;
	}

	public Date getEndOfLend() {
		return endOfLend;
	}

	public void setEndOfLend(Date endOfLend) {
		this.endOfLend = endOfLend;
	}
	
	
}
