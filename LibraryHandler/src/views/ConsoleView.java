package views;

import iohandlers.*;

public class ConsoleView {
	private static AuthorView authorView;
	private static BookView bookView;
	private static LendView lendView;
	private static MemberView memberView;
	
	public ConsoleView() {
		authorView = new AuthorView();
		bookView = new BookView();
		lendView = new LendView();
		memberView = new MemberView();
	}
	
	//requests username and password, password isn't secure!
	public String[] requestLoginDetails() {
		String[] loginDetails = new String[2];
		boolean ok = false;
		do {
			System.out.println("Please provide your username:");
			loginDetails[0] = ConsoleReader.readString();
			if(loginDetails[0].isEmpty()) {
				System.out.println("You did not provide a username, please try again!");
			}
			
		}while(ok);
		
		ok=false;
		do {

			System.out.println("Please provide your password:");
			loginDetails[1] = ConsoleReader.readString();
			
			if(loginDetails[1].isEmpty()) {
				System.out.println("You did not provide a username, please try again!");
			}
			
		}while(ok);
		
		//System.out.println(loginDetails[0] + loginDetails[1]);
		return loginDetails;
	}
}
