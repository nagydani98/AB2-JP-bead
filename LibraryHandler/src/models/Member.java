package models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.w3c.dom.*;

public class Member {
	private String memberIDCode;
	private String name;
	private String eMail;
	private String phoneNumber;
	private Date dateOfBirth;
	
	public Member(String idCode, String name, String eMail, String phoneNumber, Date dateOfBirth) {
		super();
		this.memberIDCode = idCode;
		this.name = name;
		this.eMail = eMail;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
	}

	public Member() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Tag adatai: " + memberIDCode + " " + name + " " + eMail + " "
				+ phoneNumber + " " + dateOfBirth;
	}

	
	//methods for working with regular text files
	public String toCSVString() {
		return memberIDCode + "," + name + "," + dateOfBirth + "," + eMail + "," + phoneNumber;
	}
	
	public String toFileString(String delimiter) {
		return memberIDCode + delimiter + name + delimiter + dateOfBirth + delimiter + eMail + delimiter + phoneNumber;
	}
	
	public static Member memberFromCSVString(String line) {
		Member ret = new Member();
		String memData[] = line.split(",");
		
		ret.memberIDCode = memData[0];
		ret.name = memData[1];

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(memData[2], formatter);
		ret.dateOfBirth = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
		
		ret.eMail = memData[3];
		ret.phoneNumber = memData[4];
		return ret;
	}
	
	public static Member memberFromFileString(String line, String delimiter) {
		Member ret = new Member();
		String memData[] = line.split(Pattern.quote(delimiter));
		
		ret.memberIDCode = memData[0];
		ret.name = memData[1];

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(memData[2], formatter);
			ret.dateOfBirth = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
		} catch (DateTimeParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ret.eMail = memData[3];
		ret.phoneNumber = memData[4];
		return ret;
	}
	
	
	//methods for working with xml files
	
	public static ArrayList<Member> memberListFromElementList(ArrayList<Element> elementList) {
		ArrayList<Member> returnlist = new ArrayList<Member>();
		
			String[] dataArray = new String[5];
			
			for (int i = 0; i < elementList.size(); i++) {
				Element memberElement = elementList.get(i);
				
				if(memberElement.getNodeName().equals("Tag")) {
				
					dataArray[0] = memberElement.getElementsByTagName("TKod").item(0).getTextContent();
					dataArray[1] = memberElement.getElementsByTagName("Nev").item(0).getTextContent();
					dataArray[2] = memberElement.getElementsByTagName("SzuletesiIdo").item(0).getTextContent();
					dataArray[3] = memberElement.getElementsByTagName("EMail").item(0).getTextContent();
					dataArray[4] = memberElement.getElementsByTagName("Telefonszam").item(0).getTextContent();
				}
			
			
			Member member = new Member();
			
			member.setIdCode(dataArray[0]);
			member.setName(dataArray[1]);
			
			
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(dataArray[2], formatter);
				member.setDateOfBirth(new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli()));
			} catch (DateTimeParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			member.seteMail(dataArray[3]);
			member.setPhoneNumber(dataArray[4]);
			
			
			System.out.println("Olvasott " + member.toString());
			returnlist.add(member);
		
			}
		return returnlist;
	}
	
	public static void convertAndAppendMembers(ArrayList<Member> members, Document root) {
		Element membersRootElement = root.createElement("Tagok");	
		for (Member member : members) {
				
			System.out.println(member.toString());
			
			Element memberElement = root.createElement("Tag");
			
			Element idElement = root.createElement("TKod");
			idElement.setTextContent(member.memberIDCode);
			memberElement.appendChild(idElement);

			Element nameElement = root.createElement("Nev");
			nameElement.setTextContent(member.name);
			memberElement.appendChild(nameElement);
			
			Element dateElement = root.createElement("SzuletesiIdo");
			dateElement.setTextContent(member.dateOfBirth.toString());
			memberElement.appendChild(dateElement);
			
			Element emailElement = root.createElement("EMail");
			emailElement.setTextContent(member.eMail);
			memberElement.appendChild(emailElement);
			
			Element pnumElement = root.createElement("Telefonszam");
			pnumElement.setTextContent(member.phoneNumber);
			memberElement.appendChild(pnumElement);
			
			membersRootElement.appendChild(memberElement);
			}
		root.appendChild(membersRootElement);
	}
	
	public static void convertAndAppendMembers(ArrayList<Member> members, GenericTableModel mtm) {
		for (Member member : members) {
			mtm.addRow(new Object[]{new Boolean(false), 
					member.getIdCode(), member.getName(), member.getDateOfBirth(), member.geteMail(), member.getPhoneNumber()});
		}
	}
	
	public static ArrayList<Member> convertMTM(GenericTableModel mtm) {
		ArrayList<Member> out = new ArrayList<>();
		for (int i = 0; i < mtm.getRowCount(); i++) {
				String id = (String) mtm.getValueAt(i, 1);
				String name = (String) mtm.getValueAt(i, 2);
				Date date = (Date) mtm.getValueAt(i, 3);
				String mail = (String) mtm.getValueAt(i, 4);
				String phone = (String) mtm.getValueAt(i, 5);
				
				out.add(new Member(id, name, mail, phone, date));
		}
		
		return out;
	}
	
	
	public String getIdCode() {
		return memberIDCode;
	}

	public void setIdCode(String idCode) {
		this.memberIDCode = idCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
