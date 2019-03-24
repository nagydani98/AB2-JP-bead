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
			System.out.println("Add meg a felhaszn�l�neved:");
			loginDetails[0] = ConsoleReader.readString();
			if(loginDetails[0].isEmpty()) {
				System.out.println("Nem adt�l meg felhaszn�l�nevet!");
			}
			
		}while(ok);
		
		ok=false;
		do {

			System.out.println("Add meg a jelszavad");
			loginDetails[1] = ConsoleReader.readString();
			
			if(loginDetails[1].isEmpty()) {
				System.out.println("Nem adt�l meg jelsz�t!");
			}
			
		}while(ok);
		
		//System.out.println(loginDetails[0] + loginDetails[1]);
		return loginDetails;
	}
	
	//menu methods
	public void mainConsoleMenu() {
		boolean exit = false;
		do {
		System.out.println("A men�rendszert a megfelel� sz�m bevitel�vel navig�lhatod:");
		System.out.println("1. Tagok\n2. K�nyvek\n3. K�lcs�nz�sek\n4. Szerz�k\n5. Be�ll�t�sok\n6. Kil�p�s");
		
		int menu = ConsoleReader.readIntInRange(1, 6);
		
		switch (menu) {
		case 1:
			memberView.memberConsoleMenu();
			break;

		case 2:
			bookView.bookConsoleMenu();
			break;

		case 3:
	
			break;

		case 4:
	
			break;
			
		case 5:
			
			break;
		case 6:
			exit = true;
			break;

		default:
			break;
		}
		}while(!exit);
	}
}
