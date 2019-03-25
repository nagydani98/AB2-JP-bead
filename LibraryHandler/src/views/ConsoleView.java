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
			System.out.println("Add meg a felhasználóneved:");
			loginDetails[0] = ConsoleReader.readString();
			if(loginDetails[0].isEmpty()) {
				System.out.println("Nem adtál meg felhasználónevet!");
			}
			
		}while(ok);
		
		ok=false;
		do {

			System.out.println("Add meg a jelszavad");
			loginDetails[1] = ConsoleReader.readString();
			
			if(loginDetails[1].isEmpty()) {
				System.out.println("Nem adtál meg jelszót!");
			}
			
		}while(ok);
		
		//System.out.println(loginDetails[0] + loginDetails[1]);
		return loginDetails;
	}
	
	//menu methods
	public void mainConsoleMenu() {
		boolean exit = false;
		do {
		System.out.println("A menürendszert a megfelelõ szám bevitelével navigálhatod:");
		System.out.println("1. Tagok\n2. Könyvek\n3. Kölcsönzések\n4. Szerzõk\n5. Statisztikák\n6. Beállítások\n7. Kilépés");
		
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
			System.out.println("Ebben a verzióban a beállítások még nem elérhetõk!");
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
		System.out.println("Tagok száma: " + count);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
