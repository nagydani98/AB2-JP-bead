package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Member;

public class MemberController {
	private static ArrayList<Member> loadedMembers = new ArrayList<>();
	private static Statement statement; 
	private static String sqlStatement;
	
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

	public static ArrayList<Member> getLoadedMembers() {
		return loadedMembers;
	}

	public static void setLoadedMembers(ArrayList<Member> loadedMembers) {
		MemberController.loadedMembers = loadedMembers;
	}
	
	
}
