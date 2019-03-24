package views;

import java.sql.Date;
import java.util.ArrayList;

import controllers.BookController;
import controllers.MemberController;
import iohandlers.ConsoleReader;
import iohandlers.TextFileHandler;
import models.Book;
import models.Member;

public class BookView {
	private BookController bookController;
	
	public BookView() {
		// TODO Auto-generated constructor stub
		bookController = new BookController();
	}
	
	public void bookConsoleMenu() {
		boolean exit = false;
		do {
			System.out.println(
				"\n1. K�nyvek adatainak list�z�sa"
				+ "\n2. K�nyv keres�se"
				+ "\n3. K�nyv adatainak m�dos�t�sa"
				+ "\n4. K�nyv felv�tele"
				+ "\n5. K�nyvek import�l�sa/export�l�sa"
				+ "\n6. Vissza");
			int choice = ConsoleReader.readIntInRange(1, 6);
			
			switch (choice) {
			case 1:
				if((BookController.getLoadedBooks().isEmpty())) {
				bookController.loadBookDataFromDB();
				}
				for (Book book : BookController.getLoadedBooks()) {
					System.out.println(book.toString());
				}
				break;
				 
			case 2:
				System.out.println("�rd be a keresett K�nyv c�m�t:");
				String searched = ConsoleReader.readString();
				if(!searched.isEmpty()) {
					if((BookController.getLoadedBooks().isEmpty())) {
						bookController.loadBookDataFromDB();
					}
					
					for (Book book : BookController.getLoadedBooks()) {
						if(book.getTitle().contains(searched)) {
							System.out.println(book.toString());
						}
					}
				}
				break;

			case 3:
				System.out.println("�rd be a k�nyv azonos�t� k�dj�t, amelyiket m�dos�tani szeretn�d:");
				String id = ConsoleReader.readStringBetweenLength(9, 9);
				Book bookToModify = null;
				if(!id.isEmpty()) {
					if((BookController.getLoadedBooks().isEmpty())) {
						bookController.loadBookDataFromDB();
					}
					
					for (Book book : BookController.getLoadedBooks()) {
						if(book.getBookIDCode().equals(id)) {
							bookToModify = book;
						}
					}
				}
				if(bookToModify != null) {
					modifyBookMenu(bookToModify);
				}
				break;

			case 4:
				enterNewBook();
				break;
			case 5:
				bookImportExportMenu();
				break;
				
			case 6:
				exit = true;
				break;

			default:
				break;
			}
		}while(!exit);
	}
	
	public void modifyBookMenu(Book toModify) {
		boolean exit = false;
		
		do {
			System.out.println("�rd be melyik adatot szeretn�d m�dos�tani:\n"
			+ "1. Azonos�t� k�d\n"
			+ "2. C�m\n"
			+ "3. Kiad�si D�tum\n"
			+ "4. St�tusz\n"
			+ "5. ISBN\n"
			+ "6. Szerz�(k) m�dos�t�sa\n"
			+ "7. Vissza");
			
			int choice = ConsoleReader.readIntInRange(1, 7);
			switch (choice) {
			case 1:
				System.out.println("Add meg az �j k�dot:");
				String id = ConsoleReader.readStringBetweenLength(5, 5);
				toModify.setBookIDCode(id);
				break;

			case 2:
				System.out.println("Add meg az �j c�met:");
				String title = ConsoleReader.readString();
				toModify.setTitle(title);
				break;
			case 3:
				System.out.println("Add meg az �j kiad�si d�tumot:");
				Date date = ConsoleReader.readSQLDate();
				toModify.setDateOfRelease(date);
				break;
			case 4:
				System.out.println("Add meg az �j st�tuszt (0 - selejt, 1 - szabad, 2 - kik�lcs�nz�tt):");
				int status = ConsoleReader.readIntInRange(0, 2);
				toModify.setStatus(status);
				break;
			case 5:
				System.out.println("Add meg az �j ISBN sz�mot:");
				String isbn = ConsoleReader.readString();
				toModify.setISBN(isbn);
				break;
			case 6:
				modifyBookAuthorMenu();
			break;
			case 7:
				exit=true;
			break;
			default:
				break;
			}
			bookController.updateBookDataInDB(toModify);
		} while (!exit);
	}
	
	public void modifyBookAuthorMenu() {
		System.out.println("�rd be mit szeretn�l m�dos�tani:"
				+ ""
				+ "Ez a men� sajnos m�g nem �zemk�pes ebben a verzi�ban!");
	}
	
	public void bookImportExportMenu() {
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
			
			if((BookController.getLoadedBooks().isEmpty())) {
				bookController.loadBookDataFromDB();
			}
			
			ArrayList<String> read = TextFileHandler.loadTxtFile(csvImportPath);
			for (String string : read) {
				Book book = Book.bookFromCSVString(string);
				boolean alreadyExists = false;
				
				for (Book iter : BookController.getLoadedBooks()) {
					if(iter.getBookIDCode().equals(book.getBookIDCode())) {
						alreadyExists = true;
					}
				}
				
				if(!alreadyExists) {
					BookController.getLoadedBooks().add(book);
					bookController.insertBookIntoDB(book);;
				}
				else {
					System.out.println("A k�vetkez� k�d� k�nyv m�r be van t�ltve:" + book.getBookIDCode());
				}
				
			}
			break;
			
		case 2:
			System.out.println("�rd be az import�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlImportPath = ConsoleReader.readString();
			xmlImportPath+=".xml";
			bookController.loadBooksFromXML(xmlImportPath);
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
			
			if((BookController.getLoadedBooks().isEmpty())) {
				bookController.loadBookDataFromDB();
			}
			
			ArrayList<String> readline = TextFileHandler.loadTxtFile(importPath);
			for (String string : readline) {
				Book importBook = Book.bookFromFileString(string, delimiter);
				
				boolean alreadyExists = false;
				
				for (Book test : BookController.getLoadedBooks()) {
					if(test.getBookIDCode().equals(importBook.getBookIDCode())) {
						alreadyExists = true;
					}
				}
				
				if(!alreadyExists) {
					BookController.getLoadedBooks().add(importBook);
					bookController.insertBookIntoDB(importBook);
				}
				else {
					System.out.println("A k�vetkez� k�d� k�nyv m�r be van t�ltve:" + importBook.getBookIDCode());
				}
				
			}
			break;
		case 5:
			System.out.println("�rd be az �rand� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String csvExportPath = ConsoleReader.readString();
			csvExportPath+=".csv";
			
			if((BookController.getLoadedBooks().isEmpty())) {
				bookController.loadBookDataFromDB();
			}
			
			ArrayList<String> csvLines = new ArrayList<>();
			for (Book book : BookController.getLoadedBooks()) {
				csvLines.add(book.toCSVString());
			}
			TextFileHandler.writeTxtFile(csvLines, csvExportPath);
			break;
			
		case 6:
			System.out.println("�rd be az export�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlExportPath = ConsoleReader.readString() + ".xml";
			
			bookController.saveBooksToXML(xmlExportPath);
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
			
			if((BookController.getLoadedBooks().isEmpty())) {
				bookController.loadBookDataFromDB();
			}
			
			ArrayList<String> fileLines = new ArrayList<>();
			for (Book book : BookController.getLoadedBooks()) {
				fileLines.add(book.toFileString(delimiterChars));
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
	
	public void enterNewBook() {
		System.out.println("�rd be az �j k�nyv p�ld�ny egyedi, 9 karakter hossz� azonos�t�j�t:");
		String id = ConsoleReader.readStringBetweenLength(9, 9);
		
		System.out.println("�rd be az �j k�nyv c�m�t:");
		String title = ConsoleReader.readString();
		
		System.out.println("�rd be az �j k�nyv kiad�si d�tum�t:");
		Date date = ConsoleReader.readSQLDate();
		
		System.out.println("�rd be az �j k�nyv st�tusz�t(0 - selejt, 1 - szabad, 2 - kik�lcs�nz�tt):");
		String status = ConsoleReader.readString();
		
		System.out.println("�rd be az �j tk�nyv ISBN k�dj�t:");
		String isbn = ConsoleReader.readString();
		
		System.out.println("�rd be h�ny szerz�je van az �j k�nyvnek:");
		int numOfAuthors = ConsoleReader.readInt();
		for (int i = 0; i < numOfAuthors; i++) {
			//TODO author reg
			
		}
		//TODO creat new book
	}
}
