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
				"\n1. Kölcsönzések adatainak listázása"
				+ "\n2. Kölcsönzés keresés"
				+ "\n3. Kölcsönzés módosítása"
				+ "\n4. Kölcsönzés felvétele"
				+ "\n5. Kölcsönzések importálása/exportálása"
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
				/*System.out.println("Írd be a keresett Tag nevét:");
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
				/*System.out.println("Írd be a tag azonosító kódját, akit módosítani szeretnél:");
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
