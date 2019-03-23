package views;

import java.sql.Date;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import controllers.MemberController;
import iohandlers.ConsoleReader;
import iohandlers.TextFileHandler;
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
				importExportMemberMenu();
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
			
			if(MemberController.getLoadedMembers().isEmpty()) {
				memberController.loadMemberDataFromDB();
			}
			
			ArrayList<String> read = TextFileHandler.loadTxtFile(csvImportPath);
			for (String string : read) {
				Member mem = Member.memberFromCSVString(string);
				boolean alreadyExists = false;
				
				for (Member member : MemberController.getLoadedMembers()) {
					if(member.getIdCode().equals(mem.getIdCode())) {
						alreadyExists = true;
					}
				}
				
				if(!alreadyExists) {
					MemberController.getLoadedMembers().add(mem);
					memberController.insertMemberIntoDB(mem);
				}
				else {
					System.out.println("A következõ kódú tag már be van töltve:" + mem.getIdCode());
				}
				
			}
			
			
		case 2:
			System.out.println("Írd be az importálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlImportPath = ConsoleReader.readString();
			xmlImportPath+=".xml";
			memberController.loadMembersFromXML(xmlImportPath);
			break;

		case 3:
			
			break;

		case 4:
			System.out.println("Írd be az importálandó fájl nevét és kiterjesztését, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis.txt\" vagy \"C:\\Mappanev\\adatbazis.txt\"");
			String importPath = ConsoleReader.readString();
			
			System.out.println("Írd be a fájlban az adatokat, vagy oszlopokat elválasztó karaktert:");
			String delimiter = ConsoleReader.readString();
			
			if(MemberController.getLoadedMembers().isEmpty()) {
				memberController.loadMemberDataFromDB();
			}
			
			ArrayList<String> readline = TextFileHandler.loadTxtFile(importPath);
			for (String string : readline) {
				Member mem = Member.memberFromFileString(string, delimiter);
				
				boolean alreadyExists = false;
				
				for (Member member : MemberController.getLoadedMembers()) {
					if(member.getIdCode().equals(mem.getIdCode())) {
						alreadyExists = true;
					}
				}
				
				if(!alreadyExists) {
					MemberController.getLoadedMembers().add(mem);
					memberController.insertMemberIntoDB(mem);
				}
				else {
					System.out.println("A következõ kódú tag már be van töltve:" + mem.getIdCode());
				}
				
			}
			break;
		case 5:
			System.out.println("Írd be az írandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String csvExportPath = ConsoleReader.readString();
			csvExportPath+=".csv";
			
			if(MemberController.getLoadedMembers().isEmpty()) {
				memberController.loadMemberDataFromDB();
			}
			
			ArrayList<String> csvLines = new ArrayList<>();
			for (Member member : MemberController.getLoadedMembers()) {
				csvLines.add(member.toCSVString());
			}
			TextFileHandler.writeTxtFile(csvLines, csvExportPath);
			break;
			
		case 6:
			System.out.println("Írd be az exportálandó fájl nevét, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlExportPath = ConsoleReader.readString() + ".xml";
			
			memberController.saveMemberListToXML(xmlExportPath);
			break;
		case 7:
			
			break;
		
		case 8:
			System.out.println("Írd be az importálandó fájl nevét és kiterjesztését, "
					+ "vagy az elérési útvonalát:\nPéldául: \"adatbazis.txt\" vagy \"C:\\Mappanev\\adatbazis.txt\"");
			String exportPath = ConsoleReader.readString();
			
			System.out.println("Írd be a fájlban az adatokat, vagy oszlopokat elválasztó karaktert:");
			String delimiterChars = ConsoleReader.readString();
			
			if(MemberController.getLoadedMembers().isEmpty()) {
				memberController.loadMemberDataFromDB();
			}
			
			ArrayList<String> fileLines = new ArrayList<>();
			for (Member member : MemberController.getLoadedMembers()) {
				fileLines.add(member.toFileString(delimiterChars) + "\n");
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
