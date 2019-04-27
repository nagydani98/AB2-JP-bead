package views.windowviews.creationdialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controllers.BookController;
import controllers.MemberController;
import models.Book;
import models.Member;
import views.windowviews.AppView;
import views.windowviews.BookDialog;
import views.windowviews.MemberDialog;

public class NewBookDialog extends JDialog {

	private JTextField textFieldCode;
	private JTextField textFieldTitle;
	private JTextField textFieldDate;
	private JTextField textFieldStatus;
	private JTextField textFieldISBN;
	private boolean successful = false;


	/**
	 * Create the dialog.
	 */
	public NewBookDialog(BookDialog owner) {
		super(owner, "Új könyv felvétele", true);
		setBounds(100, 100, 450, 203);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JLabel lblNv = new JLabel("K\u00F3d:");
		lblNv.setBounds(10, 11, 174, 14);
		getContentPane().add(lblNv);
		
		JLabel lblNewLabel = new JLabel("C\u00EDm");
		lblNewLabel.setBounds(10, 36, 174, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblSzletsiDtum = new JLabel("Kiad\u00E1si D\u00E1tum:");
		lblSzletsiDtum.setBounds(10, 61, 174, 14);
		getContentPane().add(lblSzletsiDtum);
		
		JLabel lblEmailCm = new JLabel("St\u00E1tusz:");
		lblEmailCm.setBounds(10, 86, 174, 14);
		getContentPane().add(lblEmailCm);
		
		JLabel lblTelefonszm = new JLabel("ISBN:");
		lblTelefonszm.setBounds(10, 111, 174, 14);
		getContentPane().add(lblTelefonszm);
		
		textFieldCode = new JTextField();
		textFieldCode.setBounds(194, 8, 230, 20);
		getContentPane().add(textFieldCode);
		textFieldCode.setColumns(10);
		
		textFieldTitle = new JTextField();
		textFieldTitle.setColumns(10);
		textFieldTitle.setBounds(194, 33, 230, 20);
		getContentPane().add(textFieldTitle);
		
		textFieldDate = new JTextField();
		textFieldDate.setText("YYYY.MM.DD");
		textFieldDate.setColumns(10);
		textFieldDate.setBounds(194, 58, 230, 20);
		getContentPane().add(textFieldDate);
		
		textFieldStatus = new JTextField();
		textFieldStatus.setText("1");
		textFieldStatus.setColumns(10);
		textFieldStatus.setBounds(194, 83, 230, 20);
		getContentPane().add(textFieldStatus);
		
		textFieldISBN = new JTextField();
		textFieldISBN.setColumns(10);
		textFieldISBN.setBounds(194, 108, 230, 20);
		getContentPane().add(textFieldISBN);
		
		JButton btnMgse = new JButton("M\u00E9gse");
		btnMgse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnMgse.setBounds(335, 130, 89, 23);
		getContentPane().add(btnMgse);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String code = textFieldCode.getText();
				String title = textFieldTitle.getText();
				Date releasedate = null;
				int status = 42; //arbitrary number for init
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
					LocalDate date = LocalDate.parse(textFieldDate.getText(), formatter);
					releasedate = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
					
					status = Integer.parseInt(textFieldStatus.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					AppView.showMD("A megadott dátum vagy státusz nem megfelelõ formátumú!", 1);
					
				}
				

				String isbn = textFieldISBN.getText();
				
				if((code != null) && (title != null) && (releasedate != null) && (status != 42) && (isbn != null)) {
					Book bok = new Book();
					bok.setBookIDCode(code);
					bok.setTitle(title);
					bok.setDateOfRelease(releasedate);
					bok.setStatus(status);
					bok.setISBN(isbn);
					System.out.println(bok.toString());
					BookController.getLoadedBooks().add(bok);
					successful = true;
					setVisible(false);
				}
			}
		});
		btnOk.setBounds(236, 130, 89, 23);
		getContentPane().add(btnOk);

	}


	public boolean isSuccessful() {
		return successful;
	}


	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	
	
}
