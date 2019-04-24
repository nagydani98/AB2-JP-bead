package controllers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import iohandlers.TextFileHandler;
import iohandlers.XMLParser;
import models.Author;
import models.Book;
import models.GenericTableModel;
import models.Member;
import views.windowviews.utilitydialogs.FileSelectorDialog;

public class BookController {
	public static ArrayList<Book> loadedBooks = new ArrayList<>();
	private static Statement statement; 
	private static Statement authorStatement;
	private static String sqlStatement;
	private static String sqlAuthorStatement;
	
	public void loadBookDataFromDB() {
		try {
			statement = MainController.getConnection().createStatement();
			authorStatement = MainController.getConnection().createStatement();
			sqlStatement = "Select * From Konyvek";
			ResultSet rs = statement.executeQuery(sqlStatement);
			ResultSet rsAuthors;
			
			while(rs.next()) {
				String id = rs.getString("KKod");
				String title = rs.getString("Cim");
				Date dateOfRelease = rs.getDate("KiadasDatum");
				int status = rs.getInt("Statusz");
				String isbn = rs.getString("ISBN");
				ArrayList<Author> authorList = new ArrayList<>();
				
				sqlAuthorStatement = "Select SzKod, Nev From Konyvek Natural Join (Select * From Szerezte Natural Join Szerzo) Where KKod ='"
						+ id + "'";
				rsAuthors = authorStatement.executeQuery(sqlAuthorStatement);
				
				while(rsAuthors.next()) {
					int aid = rsAuthors.getInt("SzKod");
					String name = rsAuthors.getString("Nev");
					
					authorList.add(new Author(name, aid));
				}
				
				
				Book book = new Book(id, title, status, isbn, dateOfRelease, authorList);
				loadedBooks.add(book);
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void updateBookDataInDB(Book book) {
		try {
			System.out.println(book.toString());
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Update Konyvek Set Cim = '" + book.getTitle() +
					"', KiadasDatum = TO_DATE('"+ book.getDateOfRelease().toString() + "', 'YYYY.MM.DD'), Statusz = '" 
					+ book.getStatus() + "', ISBN = " + book.getISBN() + " Where KKod = '" + book.getBookIDCode() + "'";
			
			statement.executeUpdate(sqlStatement);
			
			for (Author author : book.getAuthors()) {
				sqlAuthorStatement = "Update Szerzo Set Nev='" + author.getName() + "' Where SzKod = " + author.getAuthorIDCode();
				statement.executeUpdate(sqlAuthorStatement);
				
				String sqlauthored = "Update Szerezte Set SzKod = " + author.getAuthorIDCode() + "Where KKod = '" + book.getBookIDCode() 
				+ "'";
				statement.executeUpdate(sqlauthored);
			}
			
			MainController.getConnection().commit();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			try {
				MainController.getConnection().rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void insertBookIntoDB(Book book) {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Insert Into Konyvek Values ('"+ book.getBookIDCode() +
					"', '" + book.getTitle() +"', TO_DATE('" + book.getDateOfRelease().toString() + "', 'YYYY.MM.DD'),"
							+ " '" + book.getStatus() + "', '" + book.getISBN() + "')";
			statement.executeUpdate(sqlStatement);
			
			for (Author author : book.getAuthors()) {
				sqlAuthorStatement = "Insert Into Szerzo Values(" + author.getAuthorIDCode() + ", '" + author.getName() + "')";
				statement.executeUpdate(sqlAuthorStatement);
				
				String sqlauthored = "Insert Into Szerezte Values ('" + book.getBookIDCode() + "', " + author.getAuthorIDCode() + ")";
				statement.executeUpdate(sqlauthored);
			}
			MainController.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	}

	public void saveBooksToXML(String path) {
		if((loadedBooks.isEmpty())) {
			loadBookDataFromDB();
		}
		
		Document doc = XMLParser.createDocument();
		Book.convertAndAppendBooks(loadedBooks, doc);
		XMLParser.saveDocument(doc, path);
		
	}
	
	public void loadBooksFromXML(String path) {
		ArrayList<Element> eList = XMLParser.parseDocument(path);
		
		ArrayList<Book> importedBookList = Book.bookListFromElementList(eList);
		
		if((loadedBooks.isEmpty())) {
			loadBookDataFromDB();
		}
		
		//to avoid modifying the imported list of members as I am working on them in the loop, I am making a copy of them
		ArrayList<Book> copyOfImportedBooksList = (ArrayList<Book>) importedBookList.clone();
		
		//in these nested loops, I check whether a book from the imported list is already in the loaded books from the db
		//this is a very inefficient way of doing so though
		//i remove all already existing books from the copy of the imported book list
		for (Book book : importedBookList) {
			for (Book loaded : loadedBooks) {
				if(loaded.getBookIDCode().equals(book.getBookIDCode())) {
					System.out.println("A következõ kódú könyv már be van töltve:" + book.getBookIDCode());
					copyOfImportedBooksList.remove(book);
				}
			}
		}
		
		//all remaining books in the copy shouldn't be violating the unique constraints of the "Konyvek" table, so they are safe to insert
		for (Book book : copyOfImportedBooksList) {
			System.out.println("Adatbázisba felvitt " + book.toString());
			loadedBooks.add(book);
			insertBookIntoDB(book);
		}
	}
	
	public void loadBooksFromFile(FileSelectorDialog selector){
		
		if((selector.getFile()) != null) {
			
			if(MainController.availableFileFormatStrings[0].equals(selector.getSelectedFormat())) {
				boolean alreadyExists = false;
				ArrayList<String> read = TextFileHandler.loadTxtFile(selector.getFile());
				for (String string : read) {
					Book newbook = Book.bookFromCSVString(string);
					for (Book book : loadedBooks) {
						if(book.getBookIDCode().equals(newbook.getBookIDCode())) {
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						BookController.getLoadedBooks().add(newbook);
					}
				}
			}
			if(MainController.availableFileFormatStrings[1].equals(selector.getSelectedFormat())) {
				boolean alreadyExists = false;
				ArrayList<String> read = TextFileHandler.loadTxtFile(selector.getFile());
				for (String string : read) {
					Book newbook = Book.bookFromFileString(string, "" + selector.getColumnDivider());
					for (Book book : loadedBooks) {
						if(book.getBookIDCode().equals(newbook.getBookIDCode())) {
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						BookController.getLoadedBooks().add(newbook);
					}
				}
			}
			if(MainController.availableFileFormatStrings[2].equals(selector.getSelectedFormat())) {
				
				ArrayList<Element> eList = XMLParser.parseDocument(selector.getFile());
				
				ArrayList<Book> importedBookList = BookController.getLoadedBooks();
				
				ArrayList<Book> copyOfImportedBooksList = (ArrayList<Book>) importedBookList.clone();
				
				//in these nested loops, I check whether a book from the imported list is already in the loaded books from the db
				//this is a very inefficient way of doing so though
				//i remove all already existing books from the copy of the imported book list
				for (Book book : importedBookList) {
					for (Book loaded : loadedBooks) {
						if(loaded.getBookIDCode().equals(book.getBookIDCode())) {
							System.out.println("A következõ kódú könyv már be van töltve:" + book.getBookIDCode());
							copyOfImportedBooksList.remove(book);
						}
					}
				}
				
				//all remaining books in the copy shouldn't be violating the unique constraints of the "Konyvek" table, so they are safe to insert
				for (Book book : copyOfImportedBooksList) {
					loadedBooks.add(book);
				}
			}
			if(MainController.availableFileFormatStrings[3].equals(selector.getSelectedFormat())) {
				
			}
		}
	}
	
	public void exportBooksToFile(FileSelectorDialog selector, GenericTableModel bookTableModel) {
		ArrayList<Book> booksToExport = Book.convertMTM(bookTableModel); 
		
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[0])){
			String path =  selector.getDirectory() + selector.getFile() + ".csv";
			
			ArrayList<String> csvLines = new ArrayList<>();
			for (Book book : booksToExport) {
				csvLines.add(book.toCSVString());
			}
			TextFileHandler.writeTxtFile(csvLines, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[1])){
			String path =  selector.getDirectory() + selector.getFile() + ".fish";
			
			ArrayList<String> fileLines = new ArrayList<>();
			for (Book book : booksToExport) {
				fileLines.add(book.toFileString("" + selector.getColumnDivider()));
			}
			TextFileHandler.writeTxtFile(fileLines, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[2])){
			String path =  selector.getDirectory() + selector.getFile() + ".xml";
			Document doc = XMLParser.createDocument();
			Book.convertAndAppendBooks(booksToExport, doc);
			XMLParser.saveDocument(doc, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[3])){
	
		}
	}
	
	public static ArrayList<Book> getLoadedBooks() {
		return loadedBooks;
	}

	public static void setLoadedBooks(ArrayList<Book> loadedBooks) {
		BookController.loadedBooks = loadedBooks;
	}
	
	
}
