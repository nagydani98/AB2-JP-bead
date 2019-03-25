package views;

import controllers.*;
import iohandlers.ConsoleReader;
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
				/*if((MemberController.getLoadedMembers().isEmpty())) {
				memberController.loadMemberDataFromDB();
				}
				for (Member mem : MemberController.getLoadedMembers()) {
					System.out.println(mem.toString());
				}*/
				break;

			case 2:
				/*System.out.println("�rd be a keresett Tag nev�t:");
				String searched = ConsoleReader.readString();
				if(!searched.isEmpty()) {
					if((MemberController.getLoadedMembers().isEmpty())) {
						memberController.loadMemberDataFromDB();
					}
					
					for (Member mem : MemberController.getLoadedMembers()) {
						if(mem.getName().contains(searched)) {
							System.out.println(mem.toString());
						}
							
					}
				}*/
				break;

			case 3:
				/*System.out.println("�rd be a tag azonos�t� k�dj�t, akit m�dos�tani szeretn�l:");
				String id = ConsoleReader.readStringBetweenLength(5, 5);
				Member toModify = null;
				if(!id.isEmpty()) {
					if((MemberController.getLoadedMembers().isEmpty())) {
						memberController.loadMemberDataFromDB();
					}
					
					for (Member mem : MemberController.getLoadedMembers()) {
						if(mem.getIdCode().equals(id)) {
							toModify = mem;
						}
							
					}
				}
				if(toModify != null) {
					modifyMemberMenu(toModify);
				}*/
				break;

			case 4:
				//enterNewMember();
				break;
			case 5:
				//importExportMemberMenu();
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
