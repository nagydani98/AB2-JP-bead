package controllers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import iohandlers.XMLParser;
import models.Author;
import models.Book;

public class AuthorController {
	public static ArrayList<Author> loadedAuthors = new ArrayList<>();
	private static Statement statement; 
	private static String sqlStatement;
	
	public void loadAuthorDataFromDB() {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Select * From Szerzo";
			ResultSet rs = statement.executeQuery(sqlStatement);
			
			while(rs.next()) {
				String id = rs.getString("SzKod");
				String name = rs.getString("Nev");
				Author author = new Author(name, Integer.parseInt(id));
				loadedAuthors.add(author);
			}
		} catch (SQLException | NumberFormatException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void updateAuthorDataInDB(Author author) {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Update Szerzo Set Nev = '" + author.getName() + "' Where SzKod = " + author.getAuthorIDCode();
			statement.executeUpdate(sqlStatement);
			
			MainController.getConnection().commit();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	public void insertAuthorIntoDB(Author author) {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Insert Into Szerzo Values (" + author.getAuthorIDCode() + ", '" + author.getName() + "')";
			statement.executeUpdate(sqlStatement);
			
			MainController.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	}

	public void saveAuthorsToXML(String path) {
		if((loadedAuthors.isEmpty())) {
			loadAuthorDataFromDB();
		}
		
		Document doc = XMLParser.createDocument();
		Author.convertAndAppendAuthors(doc, loadedAuthors);
		XMLParser.saveDocument(doc, path);
		
	}
	
	public void loadAuthorFromXML(String path) {
		ArrayList<Element> eList = XMLParser.parseDocument(path);
		
		ArrayList<Author> importedAuthorList = Author.authorListFromElementList(eList);
		
		if((loadedAuthors.isEmpty())) {
			loadAuthorDataFromDB();
		}
		
		//to avoid modifying the imported list of authors as I am working on them in the loop, I am making a copy of them
		ArrayList<Author> copy = (ArrayList<Author>) loadedAuthors.clone();
		
		//in these nested loops, I check whether a author from the imported list is already in the loaded authors from the db
		//this is a very inefficient way of doing so though
		//i remove all already existing authors from the copy of the imported author list
		for (Author author : importedAuthorList) {
			for (Author loaded : loadedAuthors) {
				if(loaded.getAuthorIDCode() == author.getAuthorIDCode()) {
					System.out.println("A következõ kódú szerzõ már be van töltve:" + author.getAuthorIDCode());
					copy.remove(loaded);
				}
			}
		}
		
		//all remaining books in the copy shouldn't be violating the unique constraints of the "Konyvek" table, so they are safe to insert
		for (Author author : copy) {
			System.out.println("Adatbázisba felvitt " + author.toString());
			loadedAuthors.add(author);
			insertAuthorIntoDB(author);
		}
	}

	public static ArrayList<Author> getLoadedAuthors() {
		return loadedAuthors;
	}

	public static void setLoadedAuthors(ArrayList<Author> loadedAuthors) {
		AuthorController.loadedAuthors = loadedAuthors;
	}
	
	
	
}
