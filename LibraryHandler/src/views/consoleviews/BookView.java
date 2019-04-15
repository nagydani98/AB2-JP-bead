package views.consoleviews;

import java.sql.Date;
import java.util.ArrayList;

import controllers.AuthorController;
import controllers.BookController;
import controllers.MemberController;
import iohandlers.ConsoleReader;
import iohandlers.TextFileHandler;
import models.Author;
import models.Book;
import models.Member;

public class BookView {
	private BookController bookController;
	private AuthorView enterAuth = new AuthorView();
	
	public BookView() {
		// TODO Auto-generated constructor stub
		bookController = new BookController();
	}
	
	public void bookConsoleMenu() {
		boolean exit = false;
		do {
			System.out.println(
				"\n1. Könyvek adatainak listázása"
				+ "\n2. Könyv keresése"
				+ "\n3. Könyv adatainak módosítása"
				+ "\n4. Könyv felvétele"
				+ "\n5. Könyvek importálása/exportálása"
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
				System.out.println("Írd be a keresett Könyv címét:");
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
				System.out.println("Írd be a könyv azonosító kódját, amelyiket módosítani szeretnéd:");
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
			System.out.println("Írd be melyik adatot szeretnéd módosítani:\n"
			+ "1. Cím\n"
			+ "2. Kiadási Dátum\n"
			+ "3. Státusz\n"
			+ "4. ISBN\n"
			+ "5. Szerzõ(k) módosítása\n"
			+ "6. Vissza");
			
			int choice = ConsoleReader.readIntInRange(1, 6);
			switch (choice) {
			case 1:
				System.out.println("Add meg az új címet:");
				String title = ConsoleReader.readString();
				toModify.setTitle(title);
				break;
			case 2:
				System.out.println("Add meg az új kiadási dátumot:");
				Date date = ConsoleReader.readSQLDate();
				toModify.setDateOfRelease(date);
				break;
			case 3:
				System.out.println("Add meg az új státuszt (0 - selejt, 1 - szabad, 2 - kikölcsönzött):");
				int status = ConsoleReader.readIntInRange(0, 2);
				toModify.setStatus(status);
				break;
			case 4:
				System.out.println("Add meg az új ISBN számot:");
				String isbn = ConsoleReader.readString();
				toModify.setISBN(isbn);
				break;
			case 5:
				modifyBookAuthorMenu(toModify);
			break;
			case 6:
				exit=true;
			break;
			default:
				break;
			}
			bookController.updateBookDataInDB(toModify);
		} while (!exit);
	}
	
	public void modifyBookAuthorMenu(Book toModify) {
		boolean exit = false;
		do {
		System.out.println("Írd be mit szeretnél módosítani\n:"
				+ "1. A könyv szerzõ hozzáadása\n"
				+ "2. A könyv szerzõ törlése\n"
				+ "3. Könyv szerzõ adatának módosítása(Belépés a szerzõk menübe)\n"
				+ "4. Vissza\n");
		int choice = ConsoleReader.readIntInRange(1, 4);
		
		switch (choice) {
		case 1:			
			System.out.println("Írd be a könyv egy új szerzõjének az azonosító kódját:");
			int newid = ConsoleReader.readInt();
			if(AuthorController.getLoadedAuthors().isEmpty()) {
				enterAuth.getAuthorController().loadAuthorDataFromDB();
			}
			
			for (Author author : AuthorController.getLoadedAuthors()) {
				if(author.getAuthorIDCode() == newid) {
					toModify.getAuthors().add(author);
				}
			}
			break;

		case 2:			
			System.out.println("Írd be a könyv egy törlendõ szerzõjének az azonosító kódját:");
			int delid = ConsoleReader.readInt();
			if(AuthorController.getLoadedAuthors().isEmpty()) {
				enterAuth.getAuthorController().loadAuthorDataFromDB();
			}
			
			for (Author author : AuthorController.getLoadedAuthors()) {
				if(author.getAuthorIDCode() == delid) {
					toModify.getAuthors().remove(author);
				}
			}
			break;
		case 3:			
			enterAuth.authorConsoleMenu();
			break;
		case 4:			
			exit = true;
			break;
		default:
			break;
		}
		
		}while(!exit);
		
	}
	
	public void bookImportExportMenu() {
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
					System.out.println("A következõ kódú könyv már be van töltve:" + book.getBookIDCode());
				}
				
			}
			break;
			
		case 2:
			System.out.println("Írd be az importálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlImportPath = ConsoleReader.readString();
			xmlImportPath+=".xml";
			bookController.loadBooksFromXML(xmlImportPath);
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
					System.out.println("A következõ kódú könyv már be van töltve:" + importBook.getBookIDCode());
				}
				
			}
			break;
		case 5:
			System.out.println("Írd be az írandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
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
			System.out.println("Írd be az exportálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlExportPath = ConsoleReader.readString() + ".xml";
			
			bookController.saveBooksToXML(xmlExportPath);
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
		System.out.println("Írd be az új könyv példány egyedi, 9 karakter hosszú azonosítóját:");
		String id = ConsoleReader.readStringBetweenLength(9, 9);
		
		System.out.println("Írd be az új könyv címét:");
		String title = ConsoleReader.readString();
		
		System.out.println("Írd be az új könyv kiadási dátumát:");
		Date date = ConsoleReader.readSQLDate();
		
		System.out.println("Írd be az új könyv státuszát(0 - selejt, 1 - szabad, 2 - kikölcsönzött):");
		int status = ConsoleReader.readIntInRange(0, 2);
		
		System.out.println("Írd be az új tkönyv ISBN kódját:");
		String isbn = ConsoleReader.readString();
		
		
		System.out.println("Írd be hány szerzõje van az új könyvnek:");
		int numOfAuthors = ConsoleReader.readInt();

		ArrayList<Author> authors = new ArrayList<>();
		
		
		for (int i = 0; i < numOfAuthors; i++) {
		
		System.out.println(
				"Írd be az új könyv szerzõjének azonosító kódját, vagy írj 0-t, hogy belépj a szerzõk menüjéba, ahol"
				+ "\n megtekintheted a meglévõ szerzõket, vagy újat vehetsz fel, vagy importálhatsz");
		int authID = ConsoleReader.readInt();
		if(authID == 0) {
			enterAuth.authorConsoleMenu();
			System.out.println("Írd be az új könyv szerzõjének azonosító kódját:");
			authID = ConsoleReader.readInt();
		}
			if(AuthorController.getLoadedAuthors().isEmpty()) {
				enterAuth.getAuthorController().loadAuthorDataFromDB();
			}
			
			for(Author author : AuthorController.getLoadedAuthors()) {
				if(author.getAuthorIDCode() == authID) {
					authors.add(author);
				}
			}
		}
		Book book = new Book(id, title, status, isbn, date, authors);
		bookController.insertBookIntoDB(book);
	}
}
