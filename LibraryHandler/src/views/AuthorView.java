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
				"\n1. Szerz�k list�z�sa"
				+ "\n2. Szerz� keres�se"
				+ "\n3. Szerz� m�dos�t�sa"
				+ "\n4. Szerz� felv�tele"
				+ "\n5. Szerz�k import�l�sa/export�l�sa"
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
				/*System.out.println("�rd be a keresett K�nyv c�m�t:");
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
				/*System.out.println("�rd be a k�nyv azonos�t� k�dj�t, amelyiket m�dos�tani szeretn�d:");
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
