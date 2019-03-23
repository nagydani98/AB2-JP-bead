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
				"\n1. Tagok adatainak list�z�sa"
				+ "\n2. Tag keres�se"
				+ "\n3. Tag adatainak m�dos�t�sa"
				+ "\n4. Tag felv�tele"
				+ "\n5. Tagok import�l�sa/export�l�sa"
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
				System.out.println("�rd be a kereset Tag nev�t:");
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
				System.out.println("�rd be a tag azonos�t� k�dj�t, akit m�dos�tani szeretn�l:");
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
			System.out.println("�rd be melyik adat�t szeretn�d m�dos�tani:\n"
			+ "1. Azonos�t� k�d\n"
			+ "2. N�v\n"
			+ "3. Sz�let�si d�tum\n"
			+ "4. Telefonsz�m\n"
			+ "5. E-Mail C�m\n"
			+ "6. Vissza");
			
			int choice = ConsoleReader.readIntInRange(1, 6);
			switch (choice) {
			case 1:
				System.out.println("Add meg az �j k�dot:");
				String id = ConsoleReader.readStringBetweenLength(5, 5);
				toModify.setIdCode(id);
				break;

			case 2:
				System.out.println("Add meg az �j nevet");
				String name = ConsoleReader.readString();
				toModify.setName(name);
				break;
			case 3:
				System.out.println("Add meg az �j sz�let�si d�tumot:");
				Date date = ConsoleReader.readSQLDate();
				toModify.setDateOfBirth(date);
				break;
			case 4:
				System.out.println("Add meg az �j telefonsz�mot");
				String pnum = ConsoleReader.readString();
				toModify.setPhoneNumber(pnum);
				break;
			case 5:
				System.out.println("Add meg az �j E-Mail c�met");
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
					System.out.println("A k�vetkez� k�d� tag m�r be van t�ltve:" + mem.getIdCode());
				}
				
			}
			
			
		case 2:
			System.out.println("�rd be az import�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlImportPath = ConsoleReader.readString();
			xmlImportPath+=".xml";
			memberController.loadMembersFromXML(xmlImportPath);
			break;

		case 3:
			
			break;

		case 4:
			System.out.println("�rd be az import�land� f�jl nev�t �s kiterjeszt�s�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis.txt\" vagy \"C:\\Mappanev\\adatbazis.txt\"");
			String importPath = ConsoleReader.readString();
			
			System.out.println("�rd be a f�jlban az adatokat, vagy oszlopokat elv�laszt� karaktert:");
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
					System.out.println("A k�vetkez� k�d� tag m�r be van t�ltve:" + mem.getIdCode());
				}
				
			}
			break;
		case 5:
			System.out.println("�rd be az �rand� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
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
			System.out.println("�rd be az export�land� f�jl nev�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis\" vagy \"C:\\Mappanev\\adatbazis\"");
			String xmlExportPath = ConsoleReader.readString() + ".xml";
			
			memberController.saveMemberListToXML(xmlExportPath);
			break;
		case 7:
			
			break;
		
		case 8:
			System.out.println("�rd be az import�land� f�jl nev�t �s kiterjeszt�s�t, "
					+ "vagy az el�r�si �tvonal�t:\nP�ld�ul: \"adatbazis.txt\" vagy \"C:\\Mappanev\\adatbazis.txt\"");
			String exportPath = ConsoleReader.readString();
			
			System.out.println("�rd be a f�jlban az adatokat, vagy oszlopokat elv�laszt� karaktert:");
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
		System.out.println("�rd be az �j tag egyedi, 5 karakter hossz� azonos�t�j�t:");
		String id = ConsoleReader.readStringBetweenLength(5, 5);
		
		System.out.println("�rd be az �j tag nev�t:");
		String name = ConsoleReader.readString();
		
		System.out.println("�rd be az �j tag sz�let�si d�tum�t:");
		Date date = ConsoleReader.readSQLDate();
		
		System.out.println("�rd be az �j tag telefonsz�m�t:");
		String pnum = ConsoleReader.readString();
		
		System.out.println("�rd be az �j tag E-Mail c�m�t");
		String email = ConsoleReader.readString();
		
		Member memberToInsert = new Member(id, name, email, pnum, date);
		MemberController.getLoadedMembers().add(memberToInsert);
		
		memberController.insertMemberIntoDB(memberToInsert);
	}
}
