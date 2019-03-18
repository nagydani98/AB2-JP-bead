package models;

import java.sql.Date;
import java.util.ArrayList;

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
