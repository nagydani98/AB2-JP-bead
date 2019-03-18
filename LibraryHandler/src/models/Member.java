package models;

import java.sql.Date;

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
