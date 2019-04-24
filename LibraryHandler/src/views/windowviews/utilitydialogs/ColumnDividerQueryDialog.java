package views.windowviews.utilitydialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ColumnDividerQueryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private char columnDiv = '|';
	private JTextField textField;


	/**
	 * Create the dialog.
	 */
	public ColumnDividerQueryDialog(JDialog owner) {
		super(owner, " ", true);
		setBounds(100, 100, 292, 130);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblAddMegAz = new JLabel("Add meg az oszlop elv\u00E1laszt\u00F3 szimb\u00F3lumot:<b>\r\nAlap\u00E9rtelmezett : '|'");
			lblAddMegAz.setBounds(10, 11, 205, 37);
			lblAddMegAz.setHorizontalAlignment(SwingConstants.LEFT);
			contentPanel.add(lblAddMegAz);
		}
		textField = new JTextField();
		textField.setBounds(225, 8, 41, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 59, 276, 32);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						columnDiv = textField.getText().charAt(0);
						setVisible(false);
					}
				});
				okButton.setBounds(154, 5, 47, 23);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("M\u00E9gse");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
					
				});
				cancelButton.setBounds(206, 5, 65, 23);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		
	}

	public char getColumnDiv() {
		return columnDiv;
	}

	public void setColumnDiv(char columnDiv) {
		this.columnDiv = columnDiv;
	}
}
