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
				"\n1. Kölcsönzések adatainak listázása"
				+ "\n2. Kölcsönzés keresés"
				+ "\n3. Kölcsönzés módosítása"
				+ "\n4. Kölcsönzés felvétele"
				+ "\n5. Kölcsönzések importálása/exportálása"
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
				System.out.println("Írd be mi alapján szeretnél keresni:\n"
						+ "1. Tag kód\n"
						+ "2. Könyv kód\n"
						+ "3. Kivételi dátum\n"
						+ "4. Lejárati dátum");
				int searchchoice = ConsoleReader.readInt();
				switch (searchchoice) {
				case 1:
					System.out.println("Írd be a keresett tag kódját:");
					String id = ConsoleReader.readStringBetweenLength(5, 5);
					for (Lend lend : LendController.getLoadedLends()) {
						if(lend.getMemberIDCode().equals(id)) {
							System.out.println("Keresett " + lend.toString());
						}
					}
					break;
				case 2:
					System.out.println("Írd be a keresett könyv kódját:");
					String book = ConsoleReader.readStringBetweenLength(9, 9);
					for (Lend lend : LendController.getLoadedLends()) {
						if(lend.getBookIDCode().equals(book)) {
							System.out.println("Keresett " + lend.toString());
						}
					}
					break;

				case 3:
					System.out.println("Írd be a keresett kivételi dátumot:");
					Date sdate = ConsoleReader.readSQLDate();
					for (Lend lend : LendController.getLoadedLends()) {
						if(lend.getStartOfLend().equals(sdate)) {
							System.out.println("Keresett " + lend.toString());
						}
					}
					break;

				case 4:
					System.out.println("Írd be a keresett lejárati dátumot:");
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
				
				System.out.println("Írd be a keresett tag kódját:");
				String id = ConsoleReader.readStringBetweenLength(5, 5);
				System.out.println("Írd be a keresett könyv kódját:");
				String book = ConsoleReader.readStringBetweenLength(9, 9);
				Lend toModfiy = null;
				for (Lend lend : LendController.getLoadedLends()) {
					if(lend.getMemberIDCode().equals(id) && lend.getBookIDCode().equals(book)) {
						toModfiy = lend;
					}
				}
				
				if(toModfiy !=null) {
					System.out.println("Módosítandó " + toModfiy.toString());
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
			System.out.println("Írd be mit szeretnél módosítani:\n"
			+ "1. Kivételi dátum módosítása\n"
			+ "2. Lejárati dátum módosítása\n"
			+ "3. Vissza");
			
			int choice = ConsoleReader.readIntInRange(1, 3);
			
			switch (choice) {
			case 1:
				System.out.println("Add meg az új kivételi dátumot: ");
				Date sdate = ConsoleReader.readSQLDate();
				toMdify.setStartOfLend(sdate);
				break;
				
			case 2:
				System.out.println("Add meg az új lejárati dátumot: ");
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
		System.out.println("1. Importálás CSV fájlból\n"+
		"2. Importálás XML fájlból\n"+
		"3. Importálás JSON fájlból\n"+
		"4. Importálás szöveg fájlból, megadott oszlop elválasztó karakterrel\n"+
		"5. Exportálás CSV fájlba\n"+
		"6. Exportálás XML fájlba\n"+
		"7. Exportálás JSON fájlba\n"+
		"8. Exportálás szöveg fájlba, megadott oszlop elválaasztó karakterrel\n"+
		"9. Vissza\n"); 
		
		int choice = ConsoleReader.readIntInRange(1, 9);
		
		switch (choice) {
		case 1:
			System.out.println("Írd be az importálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
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
					System.out.println("A következõ kölcsönzés már fel van véve:" + lend.toString());
				}
				
			}
			break;
			
		case 2:
			System.out.println("Írd be az importálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlImportPath = ConsoleReader.readString();
			xmlImportPath+=".xml";
			lendController.loadLendsFromXML(xmlImportPath);
			break;
			
		case 3:
			System.out.println("Ebben a verzióban a JSON kezelés még nincs megvalósítva.");
			break;

		case 4:
			System.out.println("Írd be az importálandó fájl nevét és kiterjesztését, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis.txt\" vagy \"C:\\Mappanev\\adatbazis.txt\"");
			String importPath = ConsoleReader.readString();
			
			System.out.println("Írd be a fájlban az adatokat, vagy oszlopokat elválasztó karaktert:");
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
					System.out.println("A következõ kölcsönzés már fel van véve:" + lend.toString());
				}
				
			}
			break;
		case 5:
			System.out.println("Írd be az írandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
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
			System.out.println("Írd be az exportálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlExportPath = ConsoleReader.readString() + ".xml";
			
			lendController.saveLendsToXML(xmlExportPath);
			break;
		case 7:
			System.out.println("Ebben a verzióban a JSON kezelés még nincs megvalósítva.");
			break;
		
		case 8:
			System.out.println("Írd be az írandó fájl nevét és kiterjesztését, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis.txt\" vagy \"C:\\Mappanev\\adatbazis.txt\"");
			String exportPath = ConsoleReader.readString();
			
			System.out.println("Írd be a fájlban az adatokat, vagy oszlopokat elválasztó karaktert:");
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
		System.out.println("Add meg az új kölcsönzés tag kódját:");
		String memid = ConsoleReader.readStringBetweenLength(5, 5);
		System.out.println("Add meg az új kölcsönzés könyv kódját:");
		String bookid = ConsoleReader.readStringBetweenLength(9, 9);
		System.out.println("Add meg az új kölcsönzés kivételi dátumát:");
		Date sdate = ConsoleReader.readSQLDate();
		System.out.println("Add meg az új kölcsönzés lejárati dátumát:");
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
			System.out.println("Kölcsönzés felvéve!");
		}
		else {
			System.out.println("Valamilyen probléma adódott");
		}
	}
}
