package views;

import java.sql.Date;

import controllers.MemberController;
import iohandlers.ConsoleReader;
import models.Member;

public class MemberView {
	private MemberController memberController;
	
	public MemberView() {
		// TODO Auto-generated constructor stub
		memberController = new MemberController();
	}
	
	public void memberConsoleMenu() {
		boolean exit = false;
		do {
			System.out.println(
				"\n1. Tagok adatainak listázása"
				+ "\n2. Tag keresése"
				+ "\n3. Tag adatainak módosítása"
				+ "\n4. Tag felvétele"
				+ "\n5. Tagok importálása/exportálása"
				+ "\n6. Vissza");
			int choice = ConsoleReader.readIntInRange(1, 6);
			
			switch (choice) {
			case 1:
				if((MemberController.getLoadedMembers().isEmpty())) {
				memberController.loadMemberDataFromDB();
				}
				for (Member mem : MemberController.getLoadedMembers()) {
					System.out.println(mem.toString());
				}
				break;

			case 2:
				System.out.println("Írd be a kereset Tag nevét:");
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
				}
				break;

			case 3:
				System.out.println("Írd be a tag azonosító kódját, akit módosítani szeretnél:");
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
				}
				break;

			case 4:
				enterNewMember();
				break;
			case 5:
				
				break;
				
			case 6:
				exit = true;
				break;

			default:
				break;
			}
		}while(!exit);
	}
	
	public void modifyMemberMenu(Member toModify) {
		boolean exit = false;
		
		do {
			System.out.println("Írd be melyik adatát szeretnéd módosítani:\n"
			+ "1. Azonosító kód\n"
			+ "2. Név\n"
			+ "3. Születési dátum\n"
			+ "4. Telefonszám\n"
			+ "5. E-Mail Cím\n"
			+ "6. Vissza");
			
			int choice = ConsoleReader.readIntInRange(1, 6);
			switch (choice) {
			case 1:
				System.out.println("Add meg az új kódot:");
				String id = ConsoleReader.readStringBetweenLength(5, 5);
				toModify.setIdCode(id);
				break;

			case 2:
				System.out.println("Add meg az új nevet");
				String name = ConsoleReader.readString();
				toModify.setName(name);
				break;
			case 3:
				System.out.println("Add meg az új születési dátumot:");
				Date date = ConsoleReader.readSQLDate();
				toModify.setDateOfBirth(date);
				break;
			case 4:
				System.out.println("Add meg az új telefonszámot");
				String pnum = ConsoleReader.readString();
				toModify.setPhoneNumber(pnum);
				break;
			case 5:
				System.out.println("Add meg az új E-Mail címet");
				String email = ConsoleReader.readString();
				toModify.setPhoneNumber(email);
				break;
			case 6:
				exit=true;
			break;
			default:
				break;
			}
			
			memberController.updateMemberDataInDB(toModify);
		} while (!exit);
	}
	
	public void importExportMemberMenu() {
		boolean exit = false;
		
		do {
		System.out.println("1. Importálás CSV fájlból\n"+
		"2. Importálás XML fájlból\n"+
		"3. Importálás JSON fájlból\n"+
		"4. Importálás szöveg fájlból, megadott oszlop elválasztó karakterrel\n"+
		"5. Exportálás CSV fájlba"+
		"6. Exportálás XML fájlba"+
		"7. Exportálás JSON fájlba"+
		"8. Exportálás szöveg fájlba, megadott oszlop elválaasztó karakterrel"+
		"9. Vissza"); 
		
		int choice = ConsoleReader.readIntInRange(1, 9);
		
		switch (choice) {
		case 1:
			
		case 2:
			
			break;

		case 3:
			
			break;

		case 4:
			
			break;
		case 5:
			
			break;
			
		case 6:
			
			break;
		case 7:
			
			break;
		
		case 8:
	
			break;

		case 9:
			exit = true;
			break;

		default:
			break;
		}
		}while(exit);
	}
	
	public void enterNewMember() {
		System.out.println("Írd be az új tag egyedi, 5 karakter hosszú azonosítóját:");
		String id = ConsoleReader.readStringBetweenLength(5, 5);
		
		System.out.println("Írd be az új tag nevét:");
		String name = ConsoleReader.readString();
		
		System.out.println("Írd be az új tag születési dátumát:");
		Date date = ConsoleReader.readSQLDate();
		
		System.out.println("Írd be az új tag telefonszámát:");
		String pnum = ConsoleReader.readString();
		
		System.out.println("Írd be az új tag E-Mail címét");
		String email = ConsoleReader.readString();
		
		Member memberToInsert = new Member(id, name, email, pnum, date);
		MemberController.getLoadedMembers().add(memberToInsert);
		
		memberController.insertMemberIntoDB(memberToInsert);
	}
}
