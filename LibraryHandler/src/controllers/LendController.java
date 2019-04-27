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
import models.GenericTableModel;
import models.Lend;
import models.Member;
import views.windowviews.utilitydialogs.FileSelectorDialog;

public class LendController {
	private static ArrayList<Lend> loadedLends = new ArrayList<>();
	private static Statement statement; 
	private static String sqlStatement;
	
	public void loadLendDataFromDB() {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Select * From Kolcson";
			ResultSet rs = statement.executeQuery(sqlStatement);
			
			while(rs.next()) {
				String memid = rs.getString("TKod");
				String bookid = rs.getString("KKod");
				Date start = rs.getDate("KivDatum");
				Date end = rs.getDate("LejDatum");
				
				Lend loaded = new Lend(memid, bookid, start, end);
				loadedLends.add(loaded);
			}
		} catch (SQLException | NumberFormatException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void updateLendDataInDB(Lend lend) {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Update Kolcson Set KivDatum = TO_DATE('"+ lend.getStartOfLend().toString() + "', 'YYYY.MM.DD'), "
					+ "LejDatum = TO_DATE('"+ lend.getEndOfLend().toString() + "', 'YYYY.MM.DD') "
							+ " Where TKod = " + lend.getMemberIDCode() + "AND KKod = " + lend.getBookIDCode();
			statement.executeUpdate(sqlStatement);
			
			MainController.getConnection().commit();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	public void insertIntoIntoDB(Lend lend) {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Insert Into Kolcson Values (" + lend.getMemberIDCode() + ", '" + lend.getBookIDCode() + "', "
					+"TO_DATE('"+ lend.getStartOfLend().toString() + "', 'YYYY.MM.DD'), "
							+ "TO_DATE('"+ lend.getEndOfLend().toString() + "', 'YYYY.MM.DD'))";
			statement.executeUpdate(sqlStatement);
			
			MainController.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			try {
				MainController.getConnection().rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void saveLendsToXML(String path) {
		if((loadedLends.isEmpty())) {
			loadLendDataFromDB();
		}
		
		Document doc = XMLParser.createDocument();
		Lend.convertAndAppendLends(loadedLends, doc);
		XMLParser.saveDocument(doc, path);
		
	}
	
	public void loadLendsFromXML(String path) {
		ArrayList<Element> eList = XMLParser.parseDocument(path);
		
		ArrayList<Lend> importedLends = new ArrayList<>();
		
		if((loadedLends.isEmpty())) {
			loadLendDataFromDB();
		}
		
		ArrayList<Lend> copy = (ArrayList<Lend>) loadedLends.clone();
		
		for (Lend lend : importedLends) {
			for (Lend loaded : loadedLends) {
				if(loaded.getMemberIDCode().equals(lend.getMemberIDCode()) && loaded.getBookIDCode().equals(loaded.getBookIDCode())) {
					System.out.println("A következõ kölcsönzés már be van töltve:" + lend.toString());
					
				}
			}
		}
		
		for (Lend lend : copy) {
			System.out.println("Adatbázisba felvitt " + lend.toString());
			loadedLends.add(lend);
			insertIntoIntoDB(lend);
		}
	}
	
	public void loadLendsFromFile(FileSelectorDialog selector){
		
		if((selector.getFile()) != null) {
			
			if(MainController.availableFileFormatStrings[0].equals(selector.getSelectedFormat())) {
				boolean alreadyExists = false;
				ArrayList<String> read = TextFileHandler.loadTxtFile(selector.getFile());
				for (String string : read) {
					Lend lend = Lend.lendFromCSVString(string);
					for (Lend loaded : LendController.getLoadedLends()) {
						if(loaded.getMemberIDCode().equals(lend.getMemberIDCode()) && loaded.getBookIDCode().equals(loaded.getBookIDCode())) {
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						LendController.getLoadedLends().add(lend);
					}
				}
			}
			if(MainController.availableFileFormatStrings[1].equals(selector.getSelectedFormat())) {
				boolean alreadyExists = false;
				ArrayList<String> read = TextFileHandler.loadTxtFile(selector.getFile());
				for (String string : read) {
					Lend lend = Lend.lendFromFileString(string, "" + selector.getColumnDivider());
					for (Lend loaded : LendController.getLoadedLends()) {
						if(loaded.getMemberIDCode().equals(lend.getMemberIDCode()) && loaded.getBookIDCode().equals(loaded.getBookIDCode())) {
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						LendController.getLoadedLends().add(lend);
					}
				}
			}
			if(MainController.availableFileFormatStrings[2].equals(selector.getSelectedFormat())) {
				
				ArrayList<Element> eList = XMLParser.parseDocument(selector.getFile());
				
				ArrayList<Lend> importedLends = new ArrayList<>();
				
				ArrayList<Lend> copy = (ArrayList<Lend>) loadedLends.clone();
				
				for (Lend lend : importedLends) {
					for (Lend loaded : loadedLends) {
						if(loaded.getMemberIDCode().equals(lend.getMemberIDCode()) && loaded.getBookIDCode().equals(loaded.getBookIDCode())) {
							System.out.println("A következõ kölcsönzés már be van töltve:" + lend.toString());
							
						}
					}
				}
				
				for (Lend lend : copy) {
					loadedLends.add(lend);
				}
			}
			if(MainController.availableFileFormatStrings[3].equals(selector.getSelectedFormat())) {
				
			}
		}
	}
	
	public void exportLendsToFile(FileSelectorDialog selector, GenericTableModel lendTableModel) {
		ArrayList<Lend> lendsToExport = Lend.convertMTM(lendTableModel);
		
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[0])){
			String path =  selector.getDirectory() + selector.getFile() + ".csv";
			
			ArrayList<String> csvLines = new ArrayList<>();
			for (Lend lend : loadedLends) {
				csvLines.add(lend.toCSVString());
			}
			TextFileHandler.writeTxtFile(csvLines, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[1])){
			String path =  selector.getDirectory() + selector.getFile() + ".fish";
			
			ArrayList<String> fileLines = new ArrayList<>();
			for (Lend lend : loadedLends) {
				fileLines.add(lend.toFileString("" + selector.getColumnDivider()) + "\n");
			}
			TextFileHandler.writeTxtFile(fileLines, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[2])){
			String path =  selector.getDirectory() + selector.getFile() + ".xml";
			Document doc = XMLParser.createDocument();
			Lend.convertAndAppendLends(lendsToExport, doc);
			XMLParser.saveDocument(doc, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[3])){
	
		}
	}

	public static ArrayList<Lend> getLoadedLends() {
		return loadedLends;
	}

	public static void setLoadedLends(ArrayList<Lend> loadedLends) {
		LendController.loadedLends = loadedLends;
	}
	
	
}
