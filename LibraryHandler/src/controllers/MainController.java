package controllers;

import java.sql.*;

import views.ConsoleView;

public class MainController {
	private static Connection connection;
	private static Statement statement;
	
	public MainController() {
		//Default constructor, will eventually open the GUI by default
	}
	
	public MainController(boolean operateInConsole) {
		//passing true to this constructor will start the application as a console application
		
		if(operateInConsole) {
			operateInConsole();
		}
		
		else {
			//TODO
		}
	}
	
	public static void operateInConsole(){
		ConsoleView consoleView = new ConsoleView();
		//String[] loginDetails = consoleView.requestLoginDetails(); //loginDetails[0] is the username and loginDetails[1] is the password
		String[] loginDetails = {"System", "Guest123"};
		openConnection(loginDetails[0], loginDetails[1]);
		consoleView.mainConsoleMenu();
		
	}
	
	public static void openConnection(String username, String password) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Connection established");
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
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

	//data loading methods
	
	//getters and setters
	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		MainController.connection = connection;
	}
}
