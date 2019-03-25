package views;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import controllers.MainController;
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
		System.out.println("1. Tagok\n2. K�nyvek\n3. K�lcs�nz�sek\n4. Szerz�k\n5. Statisztik�k\n6. Be�ll�t�sok\n7. Kil�p�s");
		
		int menu = ConsoleReader.readIntInRange(1, 7);
		
		switch (menu) {
		case 1:
			memberView.memberConsoleMenu();
			break;

		case 2:
			bookView.bookConsoleMenu();
			break;

		case 3:
			lendView.lendConsoleMenu();
			break;

		case 4:
			authorView.authorConsoleMenu();
			break;
			
		case 5:
			statisticsMenu();
			break;
		case 6:
			System.out.println("Ebben a verzi�ban a be�ll�t�sok m�g nem el�rhet�k!");
			break;
		case 7:
			exit = true;
			break;

		default:
			break;
		}
		}while(!exit);
	}
	
	public void statisticsMenu() {
		try {
		Connection conn = MainController.getConnection();
		CallableStatement callStat = conn.prepareCall("{call tagszam(?)}");
		callStat.registerOutParameter(1, java.sql.Types.NUMERIC);
		callStat.execute();
		float count = callStat.getInt(1);
		System.out.println("Tagok sz�ma: " + count);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
