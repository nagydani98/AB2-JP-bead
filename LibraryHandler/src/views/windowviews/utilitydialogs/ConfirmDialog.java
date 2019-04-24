package views.windowviews.utilitydialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfirmDialog extends JDialog {
	private boolean choice = false;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public ConfirmDialog(JDialog owner, String message) {
		super(owner, "Figyelem!", true);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 228, 434, 33);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choice = true;
						setVisible(false);
					}
				});
				okButton.setBounds(281, 5, 64, 23);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("M\u00E9gse");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						choice = false;
						setVisible(false);
					}
				});
				cancelButton.setBounds(355, 5, 74, 23);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 414, 206);
		contentPanel.add(textArea);
		textArea.setText(message);
	}

	public boolean getChoice() {
		return choice;
	}

	public void setChoice(boolean choice) {
		this.choice = choice;
	}
	
	
}
