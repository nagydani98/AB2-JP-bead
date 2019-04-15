package views.consoleviews;

import java.sql.Date;
import java.util.ArrayList;

import controllers.*;
import iohandlers.ConsoleReader;
import iohandlers.TextFileHandler;
import models.Book;
import models.Lend;
import models.Member;

public class LendView {
	private LendController lendController;
	
	public LendView() {
		// TODO Auto-generated constructor stub
		lendController = new LendController();
	}
	
	public void lendConsoleMenu() {
		boolean exit = false;
		do {
			System.out.println(
				"\n1. K�lcs�nz�sek adatainak list�z�sa"
				+ "\n2. K�lcs�nz�s keres�s"
				+ "\n3. K�lcs�nz�s m�dos�t�sa"
				+ "\n4. K�lcs�nz�s felv�tele"
				+ "\n5. K�lcs�nz�sek import�l�sa/export�l�sa"
				+ "\n6. Vissza");
			int choice = ConsoleReader.readIntInRange(1, 6);
			
			switch (choice) {
			case 1:
				if((LendController.getLoadedLends().isEmpty())) {
				lendController.loadLendDataFromDB();
				}
				for (Lend lend : LendController.getLoadedLends()) {
					System.out.println(lend.toString());
				}
				break;

			case 2:
				
				if((LendController.getLoadedLends().isEmpty())) {
					lendController.loadLendDataFromDB();
					}
				System.out.println("�rd be mi alapj�n szeretn�l keresni:\n"
						+ "1. Tag k�d\n"
						+ "2. K�nyv k�d\n"
						+ "3. Kiv�teli d�tum\n"
						+ "4. Lej�rati d�tum");
				int searchchoice = ConsoleReader.readInt();
				switch (searchchoice) {
				case 1:
					System.out.println("�rd be a keresett tag k�dj�t:");
					String id = ConsoleReader.readStringBetweenLength(5, 5);
					for (Lend lend : LendController.getLoadedLends()) {
						if(lend.getMemberIDCode().equals(id)) {
							System.out.println("Keresett " + lend.toString());
						}
					}
					break;
				case 2:
					System.out.println("�rd be a keresett k�nyv k�dj�t:");
					String book = ConsoleReader.readStringBetweenLength(9, 9);
					for (Lend lend : LendController.getLoadedLends()) {
						if(lend.getBookIDCode().equals(book)) {
							System.out.println("Keresett " + lend.toString());
						}
					}
					break;

				case 3:
					System.out.println("�rd be a keresett kiv�teli d�tumot:");
					Date sdate = ConsoleReader.readSQLDate();
					for (Lend lend : LendController.getLoadedLends()) {
						if(lend.getStartOfLend().equals(sdate)) {
							System.out.println("Keresett " + lend.toString());
						}
					}
					break;

				case 4:
					System.out.println("�rd be a keresett lej�rati d�tumot:");
					Date edate = ConsoleReader.readSQLDate();
					for (Lend lend : LendController.getLoadedLends()) {
						if(lend.getEndOfLend().equals(edate)) {
							System.out.println("Keresett " + lend.toString());
						}
					}
					break;

				default:
					break;
				}
				break;

			case 3:
				if((LendController.getLoadedLends().isEmpty())) {
					lendController.loadLendDataFromDB();
					}
				
				System.out.println("�rd be a keresett tag k�dj�t:");
				String id = ConsoleReader.readStringBetweenLength(5, 5);
				System.out.println("�rd be a keresett k�nyv k�dj�t:");
				String book = ConsoleReader.readStringBetweenLength(9, 9);
				Lend toModfiy = null;
				for (Lend lend : LendController.getLoadedLends()) {
					if(lend.getMemberIDCode().equals(id) && lend.getBookIDCode().equals(book)) {
						toModfiy = lend;
					}
				}
				
				if(toModfiy !=null) {
					System.out.println("M�dos�tand� " + toModfiy.toString());
					modifyLendMenu(toModfiy);
				}
				
				break;

			case 4:
				enterNewLend();
				break;
			case 5:
				lendImportExportMenu();
				break;
				
			case 6:
				exit = true;
				break;

			default:
				break;
			}
		}while(!exit);
	}
	
	public void modifyLendMenu(Lend toMdify) {
		boolean exit = false;
		
		do {
			System.out.println("�rd be mit szeretn�l m�dos�tani:\n"
			+ "1. Kiv�teli d�tum m�dos�t�sa\n"
			+ "2. Lej�rati d�tum m�dos�t�sa\n"
			+ "3. Vissza");
			
			int choice = ConsoleReader.readIntInRange(1, 3);
			
			switch (choice) {
			case 1:
				System.out.println("Add meg az �j kiv�teli d�tumot: ");
				Date sdate = ConsoleReader.readSQLDate();
				toMdify.setStartOfLend(sdate);
				break;
				
			case 2:
				System.out.println("Add meg az �j lej�rati d�tumot: ");
				Date edate = ConsoleReader.readSQLDate();
				toMdify.setStartOfLend(edate);
				break;
			case 3:
				exit=true;
				break;
	
			default:
				break;
			}
		} while (!exit);
		lendController.updateLendDataInDB(toMdify);
	}
	
	public void lendImportExportMenu() {
		boolean exit = false;
		
		do {
		System.out.println("1. Import�l�s CSV f�jlb�l\n"+
		"2. Import�l�s XML f�jlb�l\n"+
		"3. Import�l�s JSON f�jlb�l\n"+
		"4. Import�l�s sz�veg f�jlb�l, megadott oszlop elv�laszt� karakterrel\n"+
		"5. Export�l�s CSV f�jlba\n"+
		"6. Export�l�s XML f�jlba\n"+
		"7. Export�l�s JSON f�jlba\n"+
		"8. Export�l�s sz�veg f�jlba, megadott oszlop elv�laaszt� karakterrel\n"+
		"9. Vissza\n"); 
		
		int choice = ConsoleReader.readIntInRange(1, 9);
		
		switch (choice) {
		case 1:
			System.out.println("�rd be az import�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String csvImportPath = ConsoleReader.readString();
			csvImportPath+=".csv";
			
			if(LendController.getLoadedLends().isEmpty()) {
				lendController.loadLendDataFromDB();
			}
			
			ArrayList<String> read = TextFileHandler.loadTxtFile(csvImportPath);
			for (String string : read) {
				Lend lend = Lend.lendFromCSVString(string);
				boolean alreadyExists = false;
				
				for (Lend iter : LendController.getLoadedLends()) {
					if(iter.getBookIDCode().equals(lend.getBookIDCode()) && iter.getMemberIDCode().equals(lend.getMemberIDCode())) {
						alreadyExists = true;
					}
				}
				
				if(!alreadyExists) {
					LendController.getLoadedLends().add(lend);
					lendController.insertIntoIntoDB(lend);
				}
				else {
					System.out.println("A k�vetkez� k�lcs�nz�s m�r fel van v�ve:" + lend.toString());
				}
				
			}
			break;
			
		case 2:
			System.out.println("�rd be az import�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlImportPath = ConsoleReader.readString();
			xmlImportPath+=".xml";
			lendController.loadLendsFromXML(xmlImportPath);
			break;
			
		case 3:
			System.out.println("Ebben a verzi�ban a JSON kezel�s m�g nincs megval�s�tva.");
			break;

		case 4:
			System.out.println("�rd be az import�land� f�jl nev�t �s kiterjeszt�s�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis.txt\" vagy \"C:\\Mappanev\\adatbazis.txt\"");
			String importPath = ConsoleReader.readString();
			
			System.out.println("�rd be a f�jlban az adatokat, vagy oszlopokat elv�laszt� karaktert:");
			String delimiter = ConsoleReader.readString();
			
			if(LendController.getLoadedLends().isEmpty()) {
				lendController.loadLendDataFromDB();
			}
			
			ArrayList<String> lines = TextFileHandler.loadTxtFile(importPath);
			for (String string : lines) {
				Lend lend = Lend.lendFromFileString(string, delimiter);
				boolean alreadyExists = false;
				
				for (Lend iter : LendController.getLoadedLends()) {
					if(iter.getBookIDCode().equals(lend.getBookIDCode()) && iter.getMemberIDCode().equals(lend.getMemberIDCode())) {
						alreadyExists = true;
					}
				}
				
				if(!alreadyExists) {
					LendController.getLoadedLends().add(lend);
					lendController.insertIntoIntoDB(lend);
				}
				else {
					System.out.println("A k�vetkez� k�lcs�nz�s m�r fel van v�ve:" + lend.toString());
				}
				
			}
			break;
		case 5:
			System.out.println("�rd be az �rand� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String csvExportPath = ConsoleReader.readString();
			csvExportPath+=".csv";
			
			if(LendController.getLoadedLends().isEmpty()) {
				lendController.loadLendDataFromDB();
			}
			
			ArrayList<String> csvLines = new ArrayList<>();
			for (Lend lend : LendController.getLoadedLends()) {
				csvLines.add(lend.toCSVString());
			}
			TextFileHandler.writeTxtFile(csvLines, csvExportPath);
			break;
			
		case 6:
			System.out.println("�rd be az export�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlExportPath = ConsoleReader.readString() + ".xml";
			
			lendController.saveLendsToXML(xmlExportPath);
			break;
		case 7:
			System.out.println("Ebben a verzi�ban a JSON kezel�s m�g nincs megval�s�tva.");
			break;
		
		case 8:
			System.out.println("�rd be az �rand� f�jl nev�t �s kiterjeszt�s�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis.txt\" vagy \"C:\\Mappanev\\adatbazis.txt\"");
			String exportPath = ConsoleReader.readString();
			
			System.out.println("�rd be a f�jlban az adatokat, vagy oszlopokat elv�laszt� karaktert:");
			String delimiterChars = ConsoleReader.readString();
			
			if(LendController.getLoadedLends().isEmpty()) {
				lendController.loadLendDataFromDB();
			}
			
			ArrayList<String> fileLines = new ArrayList<>();
			for (Lend lend : LendController.getLoadedLends()) {
				fileLines.add(lend.toFileString(delimiterChars));
			}
			TextFileHandler.writeTxtFile(fileLines, exportPath);
			break;

		case 9:
			exit = true;
			break;

		default:
			break;
		}
		}while(!exit);
	}
	
	public void enterNewLend() {
		System.out.println("Add meg az �j k�lcs�nz�s tag k�dj�t:");
		String memid = ConsoleReader.readStringBetweenLength(5, 5);
		System.out.println("Add meg az �j k�lcs�nz�s k�nyv k�dj�t:");
		String bookid = ConsoleReader.readStringBetweenLength(9, 9);
		System.out.println("Add meg az �j k�lcs�nz�s kiv�teli d�tum�t:");
		Date sdate = ConsoleReader.readSQLDate();
		System.out.println("Add meg az �j k�lcs�nz�s lej�rati d�tum�t:");
		Date edate = ConsoleReader.readSQLDate();
		
		Lend lend = new Lend(memid, bookid, sdate, edate);
		
		boolean ok1 = false, ok2 = false;
		Book lendedBook = null;
		BookController bookchecker = new BookController();
		bookchecker.loadBookDataFromDB();
		MemberController memberChecker = new MemberController();
		memberChecker.loadMemberDataFromDB();
		
		for (Book book : BookController.getLoadedBooks()) {
			if(book.getBookIDCode().equals(bookid) && book.getStatus() == 1) {
				lendedBook = book;
				ok1 = true;
				break;
			}
		}
		for(Member member : MemberController.getLoadedMembers()) {
			if(member.getIdCode().equals(memid)) {
				ok2 = true;
				break;
			}
		}
		if(ok1 && ok2) {
			lendController.insertIntoIntoDB(lend);
			LendController.getLoadedLends().add(lend);
			lendedBook.setStatus(2);
			bookchecker.updateBookDataInDB(lendedBook);
			System.out.println("K�lcs�nz�s felv�ve!");
		}
		else {
			System.out.println("Valamilyen probl�ma ad�dott");
		}
	}
}
