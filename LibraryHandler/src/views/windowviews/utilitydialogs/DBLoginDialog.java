package views.windowviews.utilitydialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.MainController;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DBLoginDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtJdbcoraclethinlocalhostxe;
	private JTextField txtUsr;
	private JTextField txtPswd;
	private boolean successfulLogin = false;

	/**
	 * Create the dialog.
	 */
	public DBLoginDialog(JDialog owner) {
		super(owner, "Bejelentkezés", true);
		setBounds(100, 100, 450, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblDatabaseURL = new JLabel("Adatb\u00E1zis URL:");
		lblDatabaseURL.setBounds(10, 11, 80, 14);
		contentPanel.add(lblDatabaseURL);
		
		txtJdbcoraclethinlocalhostxe = new JTextField();
		txtJdbcoraclethinlocalhostxe.setText("jdbc:oracle:thin:@localhost:1521:XE");
		txtJdbcoraclethinlocalhostxe.setHorizontalAlignment(SwingConstants.CENTER);
		txtJdbcoraclethinlocalhostxe.setBounds(100, 8, 300, 20);
		contentPanel.add(txtJdbcoraclethinlocalhostxe);
		txtJdbcoraclethinlocalhostxe.setColumns(10);
		
		JLabel lblEnterUsername = new JLabel("Felhaszn\u00E1l\u00F3n\u00E9v:");
		lblEnterUsername.setBounds(10, 42, 80, 14);
		contentPanel.add(lblEnterUsername);
		
		JLabel lblEnterPassword = new JLabel("Jelsz\u00F3:");
		lblEnterPassword.setBounds(10, 73, 80, 14);
		contentPanel.add(lblEnterPassword);
		
		txtUsr = new JTextField();
		txtUsr.setText("System");
		txtUsr.setBounds(100, 39, 300, 20);
		contentPanel.add(txtUsr);
		txtUsr.setColumns(10);
		
		txtPswd = new JTextField();
		txtPswd.setText("Guest123");
		txtPswd.setColumns(10);
		txtPswd.setBounds(100, 70, 300, 20);
		contentPanel.add(txtPswd);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = txtUsr.getText();
				String pswd = txtPswd.getText();
				System.out.println(user + pswd);
				if(MainController.openConnection(user, pswd)) {
					successfulLogin = true;
					setVisible(false);
				}
			}
		});
		btnOk.setBounds(110, 107, 89, 23);
		contentPanel.add(btnOk);
		
		JButton btnMgse = new JButton("M\u00E9gse");
		btnMgse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnMgse.setBounds(209, 107, 89, 23);
		contentPanel.add(btnMgse);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public boolean isSuccessfulLogin() {
		return successfulLogin;
	}

	public void setSuccessfulLogin(boolean successfulLogin) {
		this.successfulLogin = successfulLogin;
	}
	
	
}
