package views.windowviews;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import controllers.MemberController;
import models.GenericTableModel;
import models.Member;
import views.windowviews.utilitydialogs.DBLoginDialog;
import views.windowviews.utilitydialogs.FileSelectorDialog;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.Color;
import java.awt.FileDialog;

public class MemberDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable memberTable;
	private static GenericTableModel dialogTableModel;
	private static MemberController memberController;

	/**
	 * Create the dialog.
	 */
	public MemberDialog(JFrame owner) {
		super(owner, "Tagok", true);
		
		memberController = new MemberController();
		Object[] fieldNames = {" ","Kód", "Név", "Születési Idõ", "E-Mail Cím", "Telefonszám"}; 
		dialogTableModel = new GenericTableModel(fieldNames, 0);
		
		setBounds(100, 100, 800, 520);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel loadPanel = new JPanel();
		loadPanel.setBounds(10, 11, 200, 150);
		contentPanel.add(loadPanel);
		loadPanel.setLayout(null);
		
		JLabel lblLoadTitle = new JLabel("Tagok bet\u00F6lt\u00E9se");
		lblLoadTitle.setBounds(10, 11, 180, 14);
		loadPanel.add(lblLoadTitle);
		
		JButton btnLoadFromDB = new JButton("Adatb\u00E1zisb\u00F3l");
		btnLoadFromDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DBLoginDialog login = new DBLoginDialog(MemberDialog.this);
				login.setVisible(true);
				if(login.isSuccessfulLogin()){
					MemberDialog.memberController.loadMemberDataFromDB();
					Member.convertAndAppendMembers(MemberController.getLoadedMembers(), dialogTableModel);
					login.dispose();
				}
			}
		});
		btnLoadFromDB.setBounds(10, 36, 130, 23);
		loadPanel.add(btnLoadFromDB);
		
		//file format selector panel
		JPanel formatSelectorPanel = new JPanel();
		formatSelectorPanel.setBounds(10, 100, 130, 50);
		loadPanel.add(formatSelectorPanel);
		formatSelectorPanel.setLayout(null);
		formatSelectorPanel.setVisible(false);
		
		JComboBox<String> fileFormatComboBox = new JComboBox();
		fileFormatComboBox.setBackground(Color.WHITE);
		fileFormatComboBox.setBounds(0, 0, 130, 20);
		formatSelectorPanel.add(fileFormatComboBox);
		for(String formatitem : MemberController.availableFileFormats){
			fileFormatComboBox.addItem(formatitem);
		}
		
		JButton btnFormatOk = new JButton("OK");
		btnFormatOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSelectorDialog fileDialog = new FileSelectorDialog(
						MemberDialog.this, 
						"Fájl Megnyitása", 
						FileDialog.LOAD, 
						(String)fileFormatComboBox.getSelectedItem());
			}
		});
		btnFormatOk.setBounds(0, 27, 50, 23);
		formatSelectorPanel.add(btnFormatOk);
		btnFormatOk.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnFormatOk.setActionCommand("OK");
		
		JButton btnFormatSelectionCancel = new JButton("Cancel");
		btnFormatSelectionCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formatSelectorPanel.setVisible(false);
			}
		});
		btnFormatSelectionCancel.setActionCommand("Cancel");
		btnFormatSelectionCancel.setBounds(55, 27, 75, 23);
		formatSelectorPanel.add(btnFormatSelectionCancel);
		
		JButton btnLoadFromFile = new JButton("F\u00E1jlb\u00F3l");
		btnLoadFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formatSelectorPanel.setVisible(true);
			}
		});
		btnLoadFromFile.setBounds(10, 70, 130, 23);
		loadPanel.add(btnLoadFromFile);
		
		JPanel exportPanel = new JPanel();
		exportPanel.setLayout(null);
		exportPanel.setBounds(10, 176, 200, 150);
		contentPanel.add(exportPanel);
		
		JLabel lblExportTitle = new JLabel("Tagok export\u00E1l\u00E1sa");
		lblExportTitle.setBounds(10, 11, 180, 14);
		exportPanel.add(lblExportTitle);
		
		JButton btnExportToDB = new JButton("Adatb\u00E1zisba");
		btnExportToDB.setBounds(10, 36, 130, 23);
		exportPanel.add(btnExportToDB);
		
		JButton btnExportToFile = new JButton("F\u00E1jlba");
		btnExportToFile.setBounds(10, 70, 130, 23);
		exportPanel.add(btnExportToFile);
		
		JButton btnBack = new JButton("Vissza");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnBack.setBounds(10, 447, 89, 23);
		contentPanel.add(btnBack);
		
		JScrollPane memberTableScrollPane = new JScrollPane();
		memberTableScrollPane.setBounds(220, 11, 554, 459);
		contentPanel.add(memberTableScrollPane);
		
		memberTable = new JTable(dialogTableModel);
		
		TableColumn tc = null;
		for (int i = 0; i < 6; i++) {
			tc = memberTable.getColumnModel().getColumn(i);
			if(i==0 || i==1){
				tc.setPreferredWidth(30);
			}
			else{
				tc.setPreferredWidth(100);
			}
		}
		
		memberTable.setAutoCreateRowSorter(true);
		TableRowSorter<GenericTableModel> trs = (TableRowSorter<GenericTableModel>)memberTable.getRowSorter();
		trs.setSortable(0, false);
		
		memberTableScrollPane.add(memberTable);
		
		memberTableScrollPane.setViewportView(memberTable);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}


	public JTable getMemberTable() {
		return memberTable;
	}

	public void setMemberTable(JTable memberTable) {
		this.memberTable = memberTable;
	}

	public MemberController getMemberController() {
		return memberController;
	}

	public void setMemberController(MemberController memberController) {
		this.memberController = memberController;
	}
}
