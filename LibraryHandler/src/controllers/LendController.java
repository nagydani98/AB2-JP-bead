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
import models.Lend;

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

	public static ArrayList<Lend> getLoadedLends() {
		return loadedLends;
	}

	public static void setLoadedLends(ArrayList<Lend> loadedLends) {
		LendController.loadedLends = loadedLends;
	}
	
	
}
