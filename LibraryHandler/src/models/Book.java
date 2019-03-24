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

public class Book {
	private String bookIDCode;
	private String title;
	private int status;
	private String ISBN;
	private Date dateOfRelease;
	private ArrayList<Author> authors;
	

	public Book(String bookIDCode, String title, int status, String iSBN, Date dateOfRelease, ArrayList<Author> authors) {
		super();
		this.bookIDCode = bookIDCode;
		this.title = title;
		this.status = status;
		ISBN = iSBN;
		this.dateOfRelease = dateOfRelease;
		this.authors = authors;
	}
	
	public Book() {
		// TODO Auto-generated constructor stub
	}
	
	
	

	@Override
	public String toString() {
		 String data = "Könyv Adatai: " + bookIDCode + " " + title + " " + status + " " + ISBN
				+ " " + dateOfRelease + " " + authors;
		 
		 return data;
	}
	
	//methods for text file operations
	
	public String toCSVString() {
		String ret =  bookIDCode + "," + title + "," + dateOfRelease + "," + status + "," + ISBN;
		for (Author author : authors) {
			ret+="," + author.toCSVString();
		}
		return ret;
	}
	
	public String toFileString(String delimiter) {
		String ret =  bookIDCode + delimiter + title + delimiter + dateOfRelease + delimiter + status + delimiter + ISBN;
		for (Author author : authors) {
			ret+=delimiter + author.toFileString(delimiter);
		}
		return ret;
	}
	
	public static Book bookFromCSVString(String line) {
		Book ret = new Book();
		String[] data = line.split(",");
		
		ret.bookIDCode = data[0];
		ret.title = data[1];
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(data[2], formatter);
		ret.dateOfRelease = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
		
		
		
		ret.status = Integer.parseInt(data[3]);
		
		ret.ISBN = data[4];
		
		ret.authors = new ArrayList<>();
		
		//starting from the 6th position, or i = 5, the imported text will have the author codes, then the names
		for (int i = 5; i < data.length; i+=2) {
			ret.authors.add(new Author(data[i+1], Integer.parseInt(data[i])));
			
		}
		
		
		return ret;
	}
	
	public static Book bookFromFileString(String line, String delimiter) {
		Book ret = new Book();
		String[] data = line.split(Pattern.quote(delimiter));
		
		ret.bookIDCode = data[0];
		ret.title = data[1];
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(data[2], formatter);
		ret.dateOfRelease = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
		
		
		
		ret.status = Integer.parseInt(data[3]);
		ret.ISBN = data[4];
		
		//starting from the 6th position, or i = 5, the imported text will have the author codes, then the names
		for (int i = 5; i < data.length; i+=2) {
			ret.authors.add(new Author(data[i+1], Integer.parseInt(data[i])));
			
		}
		
		
		return ret;
	}

	//methods for xml operations
	public static void convertAndAppendBooks(ArrayList<Book> bookList, Document root) {
		Element booksRootElement = root.createElement("Konyvek");	
		for (Book book : bookList) {
				
			System.out.println(book.toString());
			
			Element bookElement = root.createElement("Konyv");
			
			Element idElement = root.createElement("KKod");
			idElement.setTextContent(book.bookIDCode);
			bookElement.appendChild(idElement);

			Element titleElement = root.createElement("Cim");
			titleElement.setTextContent(book.title);
			bookElement.appendChild(titleElement);
			
			Element dateElement = root.createElement("KiadasDatum");
			dateElement.setTextContent(book.dateOfRelease.toString());
			bookElement.appendChild(dateElement);
			
			Element statusElement = root.createElement("Statusz");
			statusElement.setTextContent("" + book.status);
			bookElement.appendChild(statusElement);
			
			Element isbnElement = root.createElement("ISBN");
			isbnElement.setTextContent(book.ISBN);
			bookElement.appendChild(isbnElement);
			
			Element authorsElement = root.createElement("Szerzok");
			
			Author.convertAndAppendAuthors(authorsElement, root, book.authors);
			bookElement.appendChild(authorsElement);
			
			booksRootElement.appendChild(bookElement);
			}
		root.appendChild(booksRootElement);
	}
	
	public static ArrayList<Book> bookListFromElementList(ArrayList<Element> elementList) {
		ArrayList<Book> returnlist = new ArrayList<Book>();
		
			String[] dataArray = new String[5];
			Element authorsElement;
			
			for (int i = 0; i < elementList.size(); i++) {
				Element bookElement = elementList.get(i);
				Book book = new Book();
				ArrayList<Element> authorsElementList = new ArrayList<>();
				
				if(bookElement.getNodeName().equals("Konyv")) {
				
					dataArray[0] = bookElement.getElementsByTagName("KKod").item(0).getTextContent();
					dataArray[1] = bookElement.getElementsByTagName("Cim").item(0).getTextContent();
					dataArray[2] = bookElement.getElementsByTagName("KiadasDatum").item(0).getTextContent();
					dataArray[3] = bookElement.getElementsByTagName("Statusz").item(0).getTextContent();
					dataArray[4] = bookElement.getElementsByTagName("ISBN").item(0).getTextContent();
					authorsElement = (Element) bookElement.getElementsByTagName("Szerzok").item(0);
					
					for (int j = 0; j < authorsElement.getChildNodes().getLength(); j++) {
						if(authorsElement.getChildNodes().item(j).getNodeName().equals("Szerzo")) {
							authorsElementList.add((Element) authorsElement.getChildNodes().item(j));
						}
						
					}
					
				}
			
			book.setBookIDCode(dataArray[0]);
			book.setTitle(dataArray[1]);
			
			
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(dataArray[2], formatter);
				book.setDateOfRelease(new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli()));
				
				book.setStatus(Integer.parseInt(dataArray[3]));
			} catch (DateTimeParseException | NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			book.setISBN(dataArray[4]);
			book.setAuthors(Author.authorListFromElementList(authorsElementList));
			
			System.out.println("Olvasott " + book.toString());
			returnlist.add(book);
		
			}
		return returnlist;
	}
	
	public String getBookIDCode() {
		return bookIDCode;
	}

	public void setBookIDCode(String bookIDCode) {
		this.bookIDCode = bookIDCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public Date getDateOfRelease() {
		return dateOfRelease;
	}

	public void setDateOfRelease(Date dateOfRelease) {
		this.dateOfRelease = dateOfRelease;
	}
	
	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<Author> authors) {
		this.authors = authors;
	}
	
}
