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
				"\n1. Szerz�k list�z�sa"
				+ "\n2. Szerz� keres�se"
				+ "\n3. Szerz� m�dos�t�sa"
				+ "\n4. Szerz� felv�tele"
				+ "\n5. Szerz�k import�l�sa/export�l�sa"
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
				System.out.println("�rd be a keresett szerz� nev�t");
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
				System.out.println("�rd be a szerz� azonos�t� k�dj�t, amelyiket m�dos�tani szeretn�d:");
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
					System.out.println("A k�vetkez� k�d� szerz� m�r be van t�ltve:" + auth.getAuthorIDCode());
				}
				
			}
			break;
			
		case 2:
			System.out.println("�rd be az import�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlImportPath = ConsoleReader.readString();
			xmlImportPath+=".xml";
			authorController.loadAuthorFromXML(xmlImportPath);
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
					System.out.println("A k�vetkez� k�d� szerz� m�r be van t�ltve:" + auth.getAuthorIDCode());
				}
				
			}
			break;
		case 5:
			System.out.println("�rd be az �rand� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
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
			System.out.println("�rd be az export�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlExportPath = ConsoleReader.readString() + ".xml";
			
			authorController.saveAuthorsToXML(xmlExportPath);
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
			System.out.println("�rd be mit szeretn�l tenni\n"
					+ "1. Szerz� n�v m�dos�t�sa\n"
					+ "2. Kil�p");
			int choice = ConsoleReader.readIntInRange(1, 2);
		switch (choice) {
		case 1:
			System.out.println("Add meg a szerz� �j nev�t:");
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
		System.out.println("Add meg az �j szerz� azonos�t� k�dj�t:");
		int id = ConsoleReader.readInt();
		System.out.println("Add meg az �j szerz� nev�t:");
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
