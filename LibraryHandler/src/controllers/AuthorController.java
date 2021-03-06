package controllers;

import java.sql.Date;
import java.sql.PreparedStatement;
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
import models.Lend;
import views.windowviews.utilitydialogs.FileSelectorDialog;

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
			sqlStatement = "Update Szerzo Set Nev = ? Where SzKod = ?";
			statement.executeUpdate(sqlStatement);
			PreparedStatement prepStat = MainController.getConnection().prepareStatement(sqlStatement);
			prepStat.setString(1, author.getName());
			prepStat.setInt(2, author.getAuthorIDCode());
			prepStat.execute();
			MainController.getConnection().commit();
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			try {
				MainController.getConnection().rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
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
					System.out.println("A k�vetkez� k�d� szerz� m�r be van t�ltve:" + author.getAuthorIDCode());
					copy.remove(loaded);
				}
			}
		}
		
		//all remaining books in the copy shouldn't be violating the unique constraints of the "Konyvek" table, so they are safe to insert
		for (Author author : copy) {
			System.out.println("Adatb�zisba felvitt " + author.toString());
			loadedAuthors.add(author);
			insertAuthorIntoDB(author);
		}
	}
	
	public void loadAuthorsFromFile(FileSelectorDialog selector){
		
		if((selector.getFile()) != null) {
			
			if(MainController.availableFileFormatStrings[0].equals(selector.getSelectedFormat())) {
				boolean alreadyExists = false;
				ArrayList<String> read = TextFileHandler.loadTxtFile(selector.getFile());
				for (String string : read) {
					Author author = Author.authorFromCSVString(string);
					for (Author loaded : AuthorController.getLoadedAuthors()) {
						if(loaded.getAuthorIDCode() == author.getAuthorIDCode()) {
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						AuthorController.getLoadedAuthors().add(author);
					}
				}
			}
			if(MainController.availableFileFormatStrings[1].equals(selector.getSelectedFormat())) {
				boolean alreadyExists = false;
				ArrayList<String> read = TextFileHandler.loadTxtFile(selector.getFile());
				for (String string : read) {
					Author author = Author.authorFromFileString(string, "" + selector.getColumnDivider());
					for (Author loaded : AuthorController.getLoadedAuthors()) {
						if(loaded.getAuthorIDCode() == author.getAuthorIDCode()) {
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						AuthorController.getLoadedAuthors().add(author);
					}
				}
			}
			if(MainController.availableFileFormatStrings[2].equals(selector.getSelectedFormat())) {
				
				ArrayList<Element> eList = XMLParser.parseDocument(selector.getFile());
				
				ArrayList<Author> importedAuthorList = Author.authorListFromElementList(eList);
				
				ArrayList<Author> copy = (ArrayList<Author>) loadedAuthors.clone();
				
				for (Author author : importedAuthorList) {
					for (Author loaded : loadedAuthors) {
						if(loaded.getAuthorIDCode() == author.getAuthorIDCode()) {
							System.out.println("A k�vetkez� k�d� szerz� m�r be van t�ltve:" + author.getAuthorIDCode());
							copy.remove(loaded);
						}
					}
				}
				
				//all remaining books in the copy shouldn't be violating the unique constraints of the "Konyvek" table, so they are safe to insert
				for (Author author : copy) {
					loadedAuthors.add(author);
				}
			}
			if(MainController.availableFileFormatStrings[3].equals(selector.getSelectedFormat())) {
				
			}
		}
	}
	
	public void exportAuthorsToFile(FileSelectorDialog selector, GenericTableModel lendTableModel) {
		ArrayList<Author> authorssToExport = Author.convertMTM(lendTableModel);
		
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[0])){
			String path =  selector.getDirectory() + selector.getFile() + ".csv";
			
			ArrayList<String> csvLines = new ArrayList<>();
			for (Author author : authorssToExport) {
				csvLines.add(author.toCSVString());
			}
			TextFileHandler.writeTxtFile(csvLines, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[1])){
			String path =  selector.getDirectory() + selector.getFile() + ".fish";
			
			ArrayList<String> fileLines = new ArrayList<>();
			for (Author author : authorssToExport) {
				fileLines.add(author.toFileString("" + selector.getColumnDivider()) + "\n");
			}
			TextFileHandler.writeTxtFile(fileLines, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[2])){
			String path =  selector.getDirectory() + selector.getFile() + ".xml";
			Document doc = XMLParser.createDocument();
			Author.convertAndAppendAuthors(doc, authorssToExport);
			XMLParser.saveDocument(doc, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[3])){
	
		}
	}


	public static ArrayList<Author> getLoadedAuthors() {
		return loadedAuthors;
	}

	public static void setLoadedAuthors(ArrayList<Author> loadedAuthors) {
		AuthorController.loadedAuthors = loadedAuthors;
	}
	
	
	
}
