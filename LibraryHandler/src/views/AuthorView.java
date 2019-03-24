package views;

import controllers.AuthorController;
import controllers.BookController;
import iohandlers.ConsoleReader;
import models.Book;

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
				/*if((BookController.getLoadedBooks().isEmpty())) {
				bookController.loadBookDataFromDB();
				}
				for (Book book : BookController.getLoadedBooks()) {
					System.out.println(book.toString());
				}*/
				break;
				 
			case 2:
				/*System.out.println("Írd be a keresett Könyv címét:");
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
				}*/
				break;

			case 3:
				/*System.out.println("Írd be a könyv azonosító kódját, amelyiket módosítani szeretnéd:");
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
				}*/
				break;

			case 4:
				//enterNewBook();
				break;
			case 5:
				//bookImportExportMenu();
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
