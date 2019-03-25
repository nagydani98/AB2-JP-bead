package views;

import java.util.ArrayList;

import controllers.AuthorController;
import controllers.BookController;
import controllers.MemberController;
import iohandlers.ConsoleReader;
import iohandlers.TextFileHandler;
import models.Author;
import models.Book;
import models.Member;

public class AuthorView {
	private AuthorController authorController;
	
	public AuthorView() {
		// TODO Auto-generated constructor stub
		authorController = new AuthorController();
	}
	
	public void authorConsoleMenu() {
		boolean exit = false;
		do {
			System.out.println(
				"\n1. Szerzõk listázása"
				+ "\n2. Szerzõ keresése"
				+ "\n3. Szerzõ módosítása"
				+ "\n4. Szerzõ felvétele"
				+ "\n5. Szerzõk importálása/exportálása"
				+ "\n6. Vissza");
			int choice = ConsoleReader.readIntInRange(1, 6);
			
			switch (choice) {
			case 1:
				if((AuthorController.getLoadedAuthors().isEmpty())) {
				authorController.loadAuthorDataFromDB();
				}
				for (Author author : AuthorController.getLoadedAuthors()) {
					System.out.println(author.toString());
				}
				break;
				 
			case 2:
				System.out.println("Írd be a keresett szerzõ nevét");
				String searched = ConsoleReader.readString();
				if(!searched.isEmpty()) {
					if((AuthorController.getLoadedAuthors().isEmpty())) {
						authorController.loadAuthorDataFromDB();
					}
					for (Author author : AuthorController.getLoadedAuthors()) {
						if(author.getName().contains(searched)) {
							System.out.println("Keresett :" + author.toString());
						}
					}
				}
				break;

			case 3:
				System.out.println("Írd be a szerzõ azonosító kódját, amelyiket módosítani szeretnéd:");
				int id = ConsoleReader.readInt();
				Author authorToModify = null;
				if(id != 0) {
					if((AuthorController.getLoadedAuthors().isEmpty())) {
						authorController.loadAuthorDataFromDB();
					}
					
					for (Author auth : AuthorController.getLoadedAuthors()) {
						if(auth.getAuthorIDCode() == id) {
							authorToModify = auth;
						}
					}
				}
				if(authorToModify != null) {
					modifyAuthorMenu(authorToModify);
				}

				break;

			case 4:
				enterNewAuthor();
				break;
			case 5:
				importExportAuthorsMenu();
				break;
				
			case 6:
				exit = true;
				break;

			default:
				break;
			}
		}while(!exit);
	}
	
	public void importExportAuthorsMenu() {
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
			
			if((AuthorController.getLoadedAuthors().isEmpty())) {
				authorController.loadAuthorDataFromDB();
			}
			
			ArrayList<String> read = TextFileHandler.loadTxtFile(csvImportPath);
			for (String string : read) {
				Author auth = Author.authorFromCSVString(string);
				boolean alreadyExists = false;
				
				for (Author loaded : AuthorController.getLoadedAuthors()) {
					if(loaded.getAuthorIDCode() == auth.getAuthorIDCode()) {
						alreadyExists = true;
					}
				}
				
				if(!alreadyExists) {
					AuthorController.getLoadedAuthors().add(auth);
					authorController.insertAuthorIntoDB(auth);
				}
				else {
					System.out.println("A következõ kódú szerzõ már be van töltve:" + auth.getAuthorIDCode());
				}
				
			}
			break;
			
		case 2:
			System.out.println("Írd be az importálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlImportPath = ConsoleReader.readString();
			xmlImportPath+=".xml";
			authorController.loadAuthorFromXML(xmlImportPath);
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
			
			if((AuthorController.getLoadedAuthors().isEmpty())) {
				authorController.loadAuthorDataFromDB();
			}
			
			ArrayList<String> readline = TextFileHandler.loadTxtFile(importPath);
			for (String string : readline) {
				Author auth = Author.authorFromFileString(string, delimiter);
				
				boolean alreadyExists = false;
				
				for (Author loaded : AuthorController.getLoadedAuthors()) {
					if(loaded.getAuthorIDCode() == auth.getAuthorIDCode()) {
						alreadyExists = true;
					}
				}
				
				if(!alreadyExists) {
					AuthorController.getLoadedAuthors().add(auth);
					authorController.insertAuthorIntoDB(auth);
				}
				else {
					System.out.println("A következõ kódú szerzõ már be van töltve:" + auth.getAuthorIDCode());
				}
				
			}
			break;
		case 5:
			System.out.println("Írd be az írandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String csvExportPath = ConsoleReader.readString();
			csvExportPath+=".csv";
			
			if((AuthorController.getLoadedAuthors().isEmpty())) {
				authorController.loadAuthorDataFromDB();
			}
			
			ArrayList<String> csvLines = new ArrayList<>();
			for (Author auth : AuthorController.getLoadedAuthors()) {
				csvLines.add(auth.toCSVString());
			}
			TextFileHandler.writeTxtFile(csvLines, csvExportPath);
			break;
			
		case 6:
			System.out.println("Írd be az exportálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlExportPath = ConsoleReader.readString() + ".xml";
			
			authorController.saveAuthorsToXML(xmlExportPath);
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
			
			
			if((AuthorController.getLoadedAuthors().isEmpty())) {
				authorController.loadAuthorDataFromDB();
			}
			
			ArrayList<String> fileLines = new ArrayList<>();
			for (Author auth : AuthorController.getLoadedAuthors()) {
				fileLines.add(auth.toFileString(delimiterChars));
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
	
	public void modifyAuthorMenu(Author toModify) {
		
		boolean exit = false;
		do {
			System.out.println("Írd be mit szeretnél tenni\n"
					+ "1. Szerzõ név módosítása\n"
					+ "2. Kilép");
			int choice = ConsoleReader.readIntInRange(1, 2);
		switch (choice) {
		case 1:
			System.out.println("Add meg a szerzõ új nevét:");
			String newName = ConsoleReader.readString();
			toModify.setName(newName);
			break;
		case 2:
			exit = true;
			break;
		default:
			break;
		}
		}while(!exit);
		authorController.updateAuthorDataInDB(toModify);
	}
	
	public void enterNewAuthor() {
		System.out.println("Add meg az új szerzõ azonosító kódját:");
		int id = ConsoleReader.readInt();
		System.out.println("Add meg az új szerzõ nevét:");
		String name = ConsoleReader.readString();
		
		Author author = new Author(name, id);
		AuthorController.getLoadedAuthors().add(author);
		authorController.insertAuthorIntoDB(author);
	}

	public AuthorController getAuthorController() {
		return authorController;
	}

	public void setAuthorController(AuthorController authorController) {
		this.authorController = authorController;
	}
	
	
	
}
