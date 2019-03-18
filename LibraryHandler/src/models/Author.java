package models;

import java.util.ArrayList;

public class Author {
	private String name;
	private int authorIDCode;
	private ArrayList<Book> authoredBooks;
	
	public Author(String name, int authorIDCode, ArrayList<Book> authoredBooks) {
		super();
		this.name = name;
		this.authorIDCode = authorIDCode;
		this.authoredBooks = authoredBooks;
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

	public ArrayList<Book> getAuthoredBooks() {
		return authoredBooks;
	}

	public void setAuthoredBooks(ArrayList<Book> authoredBooks) {
		this.authoredBooks = authoredBooks;
	}
	
	
}
