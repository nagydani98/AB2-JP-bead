package views.windowviews;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AuthorDialog extends JDialog {

	/**
	 * Create the dialog.
	 */
	public AuthorDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

}
