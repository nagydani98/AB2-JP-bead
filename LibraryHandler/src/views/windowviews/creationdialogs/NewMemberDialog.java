package views.windowviews.creationdialogs;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import controllers.MemberController;
import models.Member;
import views.windowviews.AppView;
import views.windowviews.MemberDialog;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

public class NewMemberDialog extends JDialog {
	private JTextField textFieldCode;
	private JTextField textFieldName;
	private JTextField textFieldDate;
	private JTextField textFieldEMail;
	private JTextField textFieldPNum;
	private boolean successful = false;


	/**
	 * Create the dialog.
	 */
	public NewMemberDialog(MemberDialog owner) {
		super(owner, "Új tag felvétele", true);
		setBounds(100, 100, 450, 203);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JLabel lblNv = new JLabel("K\u00F3d:");
		lblNv.setBounds(10, 11, 174, 14);
		getContentPane().add(lblNv);
		
		JLabel lblNewLabel = new JLabel("N\u00E9v:");
		lblNewLabel.setBounds(10, 36, 174, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblSzletsiDtum = new JLabel("Sz\u00FClet\u00E9si d\u00E1tum:");
		lblSzletsiDtum.setBounds(10, 61, 174, 14);
		getContentPane().add(lblSzletsiDtum);
		
		JLabel lblEmailCm = new JLabel("E-Mail c\u00EDm:");
		lblEmailCm.setBounds(10, 86, 174, 14);
		getContentPane().add(lblEmailCm);
		
		JLabel lblTelefonszm = new JLabel("Telefonsz\u00E1m:");
		lblTelefonszm.setBounds(10, 111, 174, 14);
		getContentPane().add(lblTelefonszm);
		
		textFieldCode = new JTextField();
		textFieldCode.setBounds(194, 8, 230, 20);
		getContentPane().add(textFieldCode);
		textFieldCode.setColumns(10);
		
		textFieldName = new JTextField();
		textFieldName.setColumns(10);
		textFieldName.setBounds(194, 33, 230, 20);
		getContentPane().add(textFieldName);
		
		textFieldDate = new JTextField();
		textFieldDate.setText("YYYY.MM.DD");
		textFieldDate.setColumns(10);
		textFieldDate.setBounds(194, 58, 230, 20);
		getContentPane().add(textFieldDate);
		
		textFieldEMail = new JTextField();
		textFieldEMail.setColumns(10);
		textFieldEMail.setBounds(194, 83, 230, 20);
		getContentPane().add(textFieldEMail);
		
		textFieldPNum = new JTextField();
		textFieldPNum.setColumns(10);
		textFieldPNum.setBounds(194, 108, 230, 20);
		getContentPane().add(textFieldPNum);
		
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
				String name = textFieldName.getText();
				Date birthdate = null;
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
					LocalDate date = LocalDate.parse(textFieldDate.getText(), formatter);
					birthdate = new Date(date.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					AppView.showMD("A megadott dátum nem megfelelõ formátumú (YYYY.MM.DD!", 1);
					
				}
				
				String email = textFieldEMail.getText();
				String pnum = textFieldPNum.getText();
				
				if((code != null) && (name != null) && (birthdate != null) && (email != null) && (pnum != null)) {
					Member member = new Member(code, name, email, pnum, birthdate);
					System.out.println(member.toString());
					MemberController.getLoadedMembers().add(member);
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
