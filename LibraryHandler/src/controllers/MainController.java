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
		String[] loginDetails = consoleView.requestLoginDetails();
		openConnection(loginDetails[0], loginDetails[1]);
		
	}
	
	public static void openConnection(String username, String password) {
		//TODO
	}
	
	public static void closeConnection() {
		//TODO
	}
}
