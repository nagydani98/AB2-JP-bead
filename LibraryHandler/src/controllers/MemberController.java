package controllers;

import java.awt.FileDialog;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import iohandlers.XMLParser;
import models.Member;

public class MemberController {
	private static ArrayList<Member> loadedMembers = new ArrayList<>();
	private static Statement statement; 
	private static String sqlStatement;
	//TODO possibly replace with enum
	public static final String[] availableFileFormats = {"CSV", "Custom Text File", "XML", "JSON"};
	
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
					System.out.println("A következõ kódú tag már be van töltve:" + member.getIdCode());
					copyOfImportedMemberList.remove(member);
				}
				else if(loaded.geteMail().equals(member.geteMail())) {
					System.out.println("A kövektezõ E-Mail cím már regisztrálva van: " + member.geteMail());
					copyOfImportedMemberList.remove(member);
				}
				else if(loaded.getPhoneNumber().equals(member.getPhoneNumber())) {
					System.out.println("A kövektezõ telefonszám már regisztrálva van: " + member.getPhoneNumber());
					copyOfImportedMemberList.remove(member);
				}
			}
		}
		
		//all remaining members in the copy shouldn't be violating the unique constraints of the "Tagok" table, so they are safe to insert
		for (Member member : copyOfImportedMemberList) {
			System.out.println("Adatbázisba felvitt " + member.toString());
			loadedMembers.add(member);
			insertMemberIntoDB(member);
		}
	}
	
	public void loadMembersFromFile(FileDialog selector){
		
	}
	
	public static ArrayList<Member> getLoadedMembers() {
		return loadedMembers;
	}

	public static void setLoadedMembers(ArrayList<Member> loadedMembers) {
		MemberController.loadedMembers = loadedMembers;
	}
	
	
}
