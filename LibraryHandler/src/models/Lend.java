package models;

import java.sql.Date;

public class Lend {
	private String memberIDCode;
	private String bookIDCode;
	private Date startOfLend;
	private Date endOfLend;
	
	public Lend(String memberIDCode, String bookIDCode, Date startOfLend, Date endOfLend) {
		super();
		this.memberIDCode = memberIDCode;
		this.bookIDCode = bookIDCode;
		this.startOfLend = startOfLend;
		this.endOfLend = endOfLend;
	}

	public String getMemberIDCode() {
		return memberIDCode;
	}

	public void setMemberIDCode(String memberIDCode) {
		this.memberIDCode = memberIDCode;
	}

	public String getBookIDCode() {
		return bookIDCode;
	}

	public void setBookIDCode(String bookIDCode) {
		this.bookIDCode = bookIDCode;
	}

	public Date getStartOfLend() {
		return startOfLend;
	}

	public void setStartOfLend(Date startOfLend) {
		this.startOfLend = startOfLend;
	}

	public Date getEndOfLend() {
		return endOfLend;
	}

	public void setEndOfLend(Date endOfLend) {
		this.endOfLend = endOfLend;
	}
	
	
}
