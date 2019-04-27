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
import controllers.LendController;
import models.Book;
import models.Lend;
import views.windowviews.AppView;
import views.windowviews.BookDialog;
import views.windowviews.LendDialog;

public class NewLendDialog extends JDialog {

	private JTextField textFieldMemCode;
	private JTextField textFieldBookCode;
	private JTextField textFieldStartDate;
	private JTextField textFieldEndDate;
	private boolean successful = false;


	/**
	 * Create the dialog.
	 */
	public NewLendDialog(LendDialog owner) {
		super(owner, "Új kölcsönzés felvétele", true);
		setBounds(100, 100, 450, 203);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JLabel lblNv = new JLabel("Tag K\u00F3d:");
		lblNv.setBounds(10, 11, 174, 14);
		getContentPane().add(lblNv);
		
		JLabel lblNewLabel = new JLabel("K\u00F6nyv K\u00F3d:");
		lblNewLabel.setBounds(10, 36, 174, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblSzletsiDtum = new JLabel("K\u00F6lcs\u00F6nz\u00E9s Kezdete:");
		lblSzletsiDtum.setBounds(10, 61, 174, 14);
		getContentPane().add(lblSzletsiDtum);
		
		JLabel lblEmailCm = new JLabel("K\u00F6lcs\u00F6nz\u00E9s V\u00E9ge:");
		lblEmailCm.setBounds(10, 86, 174, 14);
		getContentPane().add(lblEmailCm);
		
		textFieldMemCode = new JTextField();
		textFieldMemCode.setBounds(194, 8, 230, 20);
		getContentPane().add(textFieldMemCode);
		textFieldMemCode.setColumns(10);
		
		textFieldBookCode = new JTextField();
		textFieldBookCode.setColumns(10);
		textFieldBookCode.setBounds(194, 33, 230, 20);
		getContentPane().add(textFieldBookCode);
		
		textFieldStartDate = new JTextField();
		textFieldStartDate.setText("YYYY.MM.DD");
		textFieldStartDate.setColumns(10);
		textFieldStartDate.setBounds(194, 58, 230, 20);
		getContentPane().add(textFieldStartDate);
		
		textFieldEndDate = new JTextField();
		textFieldEndDate.setText("YYYY.MM.DD");
		textFieldEndDate.setColumns(10);
		textFieldEndDate.setBounds(194, 83, 230, 20);
		getContentPane().add(textFieldEndDate);
		
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
				String memcode = textFieldMemCode.getText();
				String bookcode = textFieldBookCode.getText();
				Date startdate = null;
				Date enddate = null;
				int status = 42; //arbitrary number for init
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
					LocalDate date = LocalDate.parse(textFieldStartDate.getText(), formatter);
					startdate = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
					
					LocalDate date2 = LocalDate.parse(textFieldEndDate.getText(), formatter);
					enddate = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					AppView.showMD("A megadott dátum nem megfelelõ formátumú!", 1);
					
				}
				
				if((memcode != null) && (bookcode != null) && (startdate != null) && (enddate != null)) {
					Lend lend = new Lend(memcode, bookcode, startdate, enddate);
					System.out.println(lend.toString());
					LendController.getLoadedLends().add(lend);
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
