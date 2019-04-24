package views.windowviews.utilitydialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import models.GenericTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class ModifyTableDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private GenericTableModel genericTableModel;
	
	/**
	 * Create the dialog.
	 */
	public ModifyTableDialog(JDialog owner, GenericTableModel tableModel) {
		super(owner, "Módosítás", true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 230, 434, 31);
			contentPanel.add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setBounds(314, 5, 47, 23);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						
						setVisible(false);
					}
				});
				buttonPane.setLayout(null);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("M\u00E9gse");
				cancelButton.setBounds(366, 5, 63, 23);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 11, 412, 208);
			contentPanel.add(scrollPane);
			
			table = new JTable();
			scrollPane.setViewportView(table);
			genericTableModel.setEditable(true);
			table.setModel(genericTableModel);
		}
		
		
	}

	public GenericTableModel getGenericTableModel() {
		return genericTableModel;
	}

	public void setGenericTableModel(GenericTableModel genericTableModel) {
		this.genericTableModel = genericTableModel;
	}
}
