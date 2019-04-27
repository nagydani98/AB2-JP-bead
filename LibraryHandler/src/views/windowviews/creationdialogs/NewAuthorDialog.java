package views.windowviews.creationdialogs;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import controllers.AuthorController;
import controllers.BookController;
import models.Author;
import models.Book;
import views.windowviews.AppView;
import views.windowviews.AuthorDialog;
import views.windowviews.BookDialog;

public class NewAuthorDialog extends JDialog {

	private JTextField textFieldCode;
	private JTextField textFieldName;
	private boolean successful = false;


	/**
	 * Create the dialog.
	 */
	public NewAuthorDialog(AuthorDialog owner) {
		super(owner, "Új szerzõ felvétele", true);
		setBounds(100, 100, 450, 140);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JLabel lblNv = new JLabel("K\u00F3d:");
		lblNv.setBounds(10, 11, 174, 14);
		getContentPane().add(lblNv);
		
		JLabel lblNewLabel = new JLabel("Szerz\u0151 neve");
		lblNewLabel.setBounds(10, 36, 174, 14);
		getContentPane().add(lblNewLabel);
		
		textFieldCode = new JTextField();
		textFieldCode.setBounds(194, 8, 230, 20);
		getContentPane().add(textFieldCode);
		textFieldCode.setColumns(10);
		
		textFieldName = new JTextField();
		textFieldName.setColumns(10);
		textFieldName.setBounds(194, 33, 230, 20);
		getContentPane().add(textFieldName);
		
		JButton btnMgse = new JButton("M\u00E9gse");
		btnMgse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnMgse.setBounds(335, 64, 89, 23);
		getContentPane().add(btnMgse);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int code = -1;
				String name = textFieldName.getText();
				Date releasedate = null;
				try {
					code = Integer.parseInt(textFieldCode.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					AppView.showMD("A megadott kód nem megfelelõ formátumú!", 1);
					
				}
				if((code >= 1) && (name != null)) {
					Author author = new Author(name, code);
					AuthorController.getLoadedAuthors().add(author);
					successful = true;
					setVisible(false);
				}
			}
		});
		btnOk.setBounds(236, 64, 89, 23);
		getContentPane().add(btnOk);

	}


	public boolean isSuccessful() {
		return successful;
	}


	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

}
