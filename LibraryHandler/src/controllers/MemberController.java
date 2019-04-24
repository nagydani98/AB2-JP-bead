package controllers;

import java.awt.FileDialog;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.FileHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import iohandlers.TextFileHandler;
import iohandlers.XMLParser;
import models.GenericTableModel;
import models.Member;
import views.windowviews.utilitydialogs.FileSelectorDialog;

public class MemberController {
	private static ArrayList<Member> loadedMembers = new ArrayList<>();
	private static Statement statement; 
	private static String sqlStatement;
	//TODO possibly replace with enum
	
	public MemberController() {
		// TODO Auto-generated constructor stub
	}
	
	public void loadMemberDataFromDB() {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Select * From Tagok";
			ResultSet rs = statement.executeQuery(sqlStatement);
			
			while(rs.next()) {
				String id = rs.getString("TKod");
				String name = rs.getString("Nev");
				Date dateOfBirth = rs.getDate("SzuletesiIdo");
				String email = rs.getString("EMail");
				String phone = rs.getString("Telefonszam");
				
				Member member = new Member(id, name, email, phone, dateOfBirth);
				loadedMembers.add(member);
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			
		}
	}
	
	public void updateMemberDataInDB(Member member) {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Update Tagok Set TKod = '" + member.getIdCode() + "', Nev = '" + member.getName() +
					"', SzuletesiIdo = TO_DATE('"+ member.getDateOfBirth().toString() + "', 'YYYY.MM.DD'), EMail = '" 
					+ member.geteMail() + "', Telefonszam = " + member.getPhoneNumber() + "Where TKod = '" + member.getIdCode() + "'";
			statement.executeUpdate(sqlStatement);
			
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
	
	public void insertMemberIntoDB(Member member) {
		try {
			statement = MainController.getConnection().createStatement();
			sqlStatement = "Insert Into Tagok Values ('"+ member.getIdCode() +
					"', '" + member.getName() +"', TO_DATE('" + member.getDateOfBirth().toString() + "', 'YYYY.MM.DD'),"
							+ " '" + member.geteMail() + "', '" + member.getPhoneNumber() + "')";
			statement.executeUpdate(sqlStatement);
			//MainController.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	}

	
	//methods for xml operations
	public void saveMemberListToXML(String path) {
		if(MemberController.loadedMembers.isEmpty()) {
			loadMemberDataFromDB();
		}
		Document doc = XMLParser.createDocument();
		Member.convertAndAppendMembers(loadedMembers, doc);
		XMLParser.saveDocument(doc, path);
	}
	
	public void loadMembersFromXML(String path) {
		ArrayList<Element> eList = XMLParser.parseDocument(path);
		
		ArrayList<Member> importedMemberList = Member.memberListFromElementList(eList);
		
		if(MemberController.loadedMembers.isEmpty()) {
			loadMemberDataFromDB();
		}
		
		//to avoid modifying the imported list of members as I am working on them in the loop, I am making a copy of them
		ArrayList<Member> copyOfImportedMemberList = (ArrayList<Member>) importedMemberList.clone();
		
		//in these nested loops, I check whether a member from the imported list is already in the loaded members from the db
		//this is a very inefficient way of doing so though
		//i remove all already existing members from the copy of the imported member list
		for (Member member : importedMemberList) {
			for (Member loaded : loadedMembers) {
				if(loaded.getIdCode().equals(member.getIdCode())) {
					System.out.println("A k�vetkez� k�d� tag m�r be van t�ltve:" + member.getIdCode());
					copyOfImportedMemberList.remove(member);
				}
				else if(loaded.geteMail().equals(member.geteMail())) {
					System.out.println("A k�vektez� E-Mail c�m m�r regisztr�lva van: " + member.geteMail());
					copyOfImportedMemberList.remove(member);
				}
				else if(loaded.getPhoneNumber().equals(member.getPhoneNumber())) {
					System.out.println("A k�vektez� telefonsz�m m�r regisztr�lva van: " + member.getPhoneNumber());
					copyOfImportedMemberList.remove(member);
				}
			}
		}
		
		//all remaining members in the copy shouldn't be violating the unique constraints of the "Tagok" table, so they are safe to insert
		for (Member member : copyOfImportedMemberList) {
			System.out.println("Adatb�zisba felvitt " + member.toString());
			loadedMembers.add(member);
			insertMemberIntoDB(member);
		}
	}
	
	public void loadMembersFromFile(FileSelectorDialog selector){
		
		if((selector.getFile()) != null) {
			
			if(MainController.availableFileFormatStrings[0].equals(selector.getSelectedFormat())) {
				boolean alreadyExists = false;
				ArrayList<String> read = TextFileHandler.loadTxtFile(selector.getFile());
				for (String string : read) {
					Member mem = Member.memberFromCSVString(string);
					for (Member member : MemberController.getLoadedMembers()) {
						if(member.getIdCode().equals(mem.getIdCode())) {
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						MemberController.getLoadedMembers().add(mem);
					}
				}
			}
			if(MainController.availableFileFormatStrings[1].equals(selector.getSelectedFormat())) {
				boolean alreadyExists = false;
				ArrayList<String> read = TextFileHandler.loadTxtFile(selector.getFile());
				for (String string : read) {
					Member mem = Member.memberFromFileString(string, "" + selector.getColumnDivider());
					for (Member member : MemberController.getLoadedMembers()) {
						if(member.getIdCode().equals(mem.getIdCode())) {
							alreadyExists = true;
						}
					}
					
					if(!alreadyExists) {
						MemberController.getLoadedMembers().add(mem);
					}
				}
			}
			if(MainController.availableFileFormatStrings[2].equals(selector.getSelectedFormat())) {
				
				ArrayList<Element> eList = XMLParser.parseDocument(selector.getFile());
				
				ArrayList<Member> importedMemberList = Member.memberListFromElementList(eList);
				
				ArrayList<Member> copyOfImportedMemberList = (ArrayList<Member>) importedMemberList.clone();
				
				//in these nested loops, I check whether a member from the imported list is already in the loaded members from the db
				//this is a very inefficient way of doing so though
				//i remove all already existing members from the copy of the imported member list
				for (Member member : importedMemberList) {
					for (Member loaded : loadedMembers) {
						if(loaded.getIdCode().equals(member.getIdCode())) {
							copyOfImportedMemberList.remove(member);
						}
						else if(loaded.geteMail().equals(member.geteMail())) {
							copyOfImportedMemberList.remove(member);
						}
						else if(loaded.getPhoneNumber().equals(member.getPhoneNumber())) {
							copyOfImportedMemberList.remove(member);
						}
					}
				}
				
				//all remaining members in the copy shouldn't be violating the unique constraints of the "Tagok" table, so they are safe to insert
				for (Member member : copyOfImportedMemberList) {
					loadedMembers.add(member);
				
			}
			}
			if(MainController.availableFileFormatStrings[3].equals(selector.getSelectedFormat())) {
				
			}
		}
	}
	
	public void exportMembersToFile(FileSelectorDialog selector, GenericTableModel memberTableModel) {
		ArrayList<Member> membersToExport = Member.convertMTM(memberTableModel);
		
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[0])){
			String path =  selector.getDirectory() + selector.getFile() + ".csv";
			
			ArrayList<String> csvLines = new ArrayList<>();
			for (Member member : membersToExport) {
				csvLines.add(member.toCSVString());
			}
			TextFileHandler.writeTxtFile(csvLines, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[1])){
			String path =  selector.getDirectory() + selector.getFile() + ".fish";
			
			ArrayList<String> fileLines = new ArrayList<>();
			for (Member member : membersToExport) {
				fileLines.add(member.toFileString("" + selector.getColumnDivider()) + "\n");
			}
			TextFileHandler.writeTxtFile(fileLines, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[2])){
			String path =  selector.getDirectory() + selector.getFile() + ".xml";
			Document doc = XMLParser.createDocument();
			Member.convertAndAppendMembers(membersToExport, doc);
			XMLParser.saveDocument(doc, path);
		}
		if(selector.getSelectedFormat().equals(MainController.availableFileFormatStrings[3])){
	
		}
	}
	
	public static ArrayList<Member> getLoadedMembers() {
		return loadedMembers;
	}

	public static void setLoadedMembers(ArrayList<Member> loadedMembers) {
		MemberController.loadedMembers = loadedMembers;
	}
	
	
}
