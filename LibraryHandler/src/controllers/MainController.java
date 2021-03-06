package controllers;

import java.awt.EventQueue;
import java.sql.*;
import java.util.ArrayList;

import views.consoleviews.ConsoleView;
import views.windowviews.AppView;

public class MainController {
	private static Connection connection;
	private static Statement statement;
	public static final String[] availableFileFormatStrings = {"CSV", "Custom Text File", "XML", "JSON"};
	
	public MainController() {
		//Default constructor, opens the GUI by default
		operateInGUI();
	}
	
	public MainController(boolean operateInConsole) {
		//passing true to this constructor will start the application as a console application
		
		if(operateInConsole) {
			operateInConsole();
		}
		
		else {
			operateInGUI();
		}
	}
	
	public static void operateInConsole(){
		ConsoleView consoleView = new ConsoleView();
		String[] loginDetails = consoleView.requestLoginDetails(); //loginDetails[0] is the username and loginDetails[1] is the password
		//String[] loginDetails = {"System", "Guest123"};
		openConnection(loginDetails[0], loginDetails[1]);
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initProcedures();
		consoleView.mainConsoleMenu();
		
	}
	
	public static void operateInGUI(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppView frame = new AppView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static boolean openConnection(String username, String password) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Connection established");
			return true;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return false;
		}
	}
	
	public static boolean openConnection(String url, String username, String password) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Connection established");
			return true;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return false;
		}
	}
	
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isConnectionOpen() {
			if(connection != null) {
				return true;
			}
			else return false;
	}

	//DB setup methods
	public  static void listAllTAblesInLibraryDatabase() {
		try {
			DatabaseMetaData dmd = connection.getMetaData();
		    ResultSet rs1 = dmd.getSchemas();
		    while (rs1.next()) {
		      String ss = rs1.getString(1);
		      String[] types = {"TABLE"};
		      ResultSet rs2 = dmd.getTables(null, ss, "%", types);
		      while (rs2.next())
		        System.out.println(rs2.getString(3) + " " + rs2.getString(4));
		    }
		    connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public static void initProcedures() {
		String sqlProcedureMemberRowCounter = "create or replace procedure tagszam "
				+ "(countNum OUT number) is" 
				+ "begin "
				+ "select count(*) into countNum from Tagok; "
				+ "end;";
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sqlProcedureMemberRowCounter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//getters and setters
	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		MainController.connection = connection;
	}
}
