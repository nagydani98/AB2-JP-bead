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

import controllers.MainController;
import controllers.MemberController;
import models.GenericTableModel;
import models.Member;
import views.windowviews.creationdialogs.NewMemberDialog;
import views.windowviews.utilitydialogs.ColumnDividerQueryDialog;
import views.windowviews.utilitydialogs.ConfirmDialog;
import views.windowviews.utilitydialogs.DBLoginDialog;
import views.windowviews.utilitydialogs.FileSelectorDialog;
import views.windowviews.utilitydialogs.ModifyTableDialog;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.Vector;
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
				DBLoginDialog login = null;
				if(!MainController.isConnectionOpen()) {
					login = new DBLoginDialog(MemberDialog.this);
					login.setVisible(true);
					login.dispose();
				}
				if (login != null && login.isSuccessfulLogin()) {
						MemberDialog.memberController.loadMemberDataFromDB();
						Member.convertAndAppendMembers(MemberController.getLoadedMembers(), dialogTableModel);
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
		for(String formatitem : MainController.availableFileFormatStrings){
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
				if(fileDialog.getFile() != null) {
				if(fileDialog.getSelectedFormat().equals(MainController.availableFileFormatStrings[1])) {
					ColumnDividerQueryDialog dividerSelector = new ColumnDividerQueryDialog(MemberDialog.this);
					dividerSelector.setVisible(true);
					fileDialog.setColumnDivider(dividerSelector.getColumnDiv());
				}
				
				memberController.loadMembersFromFile(fileDialog);
				
				Member.convertAndAppendMembers(MemberController.getLoadedMembers(), dialogTableModel);
				}
			}
		});
		btnFormatOk.setBounds(0, 27, 50, 23);
		formatSelectorPanel.add(btnFormatOk);
		btnFormatOk.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnFormatOk.setActionCommand("OK");
		
		JButton btnFormatSelectionCancel = new JButton("M\u00E9gse");
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
		
		JButton buttonUploadToDBOk = new JButton("OK");
		buttonUploadToDBOk.setEnabled(false);
		buttonUploadToDBOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmDialog confirmDialog = new ConfirmDialog(MemberDialog.this, 
						"Biztosan szeretné exportálni a táblázatban lévõ tagokat? \n Ha az adatbázisban már van a táblázatban szereplõ kódú tag,\n az nem lesz az adatbázisba illesztve!");
				confirmDialog.setVisible(true);
				if(confirmDialog.getChoice()) {
					for(Member member : MemberController.getLoadedMembers()) {
						memberController.insertMemberIntoDB(member);
					}
				}
				
			}
		});
		buttonUploadToDBOk.setFont(new Font("Tahoma", Font.PLAIN, 11));
		buttonUploadToDBOk.setActionCommand("OK");
		buttonUploadToDBOk.setBounds(150, 36, 50, 23);
		exportPanel.add(buttonUploadToDBOk);
		
		JButton btnExportToDB = new JButton("Adatb\u00E1zisba");
		btnExportToDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!MainController.isConnectionOpen()) {
					DBLoginDialog login = new DBLoginDialog(MemberDialog.this);
					login.setVisible(true);
					if(login.isSuccessfulLogin()){
						buttonUploadToDBOk.setEnabled(true);
						login.dispose();
					}
					}
			}
		});
		btnExportToDB.setBounds(10, 36, 130, 23);
		exportPanel.add(btnExportToDB);
		
		JButton btnExportToFile = new JButton("F\u00E1jlba");
		btnExportToFile.setBounds(10, 70, 130, 23);
		exportPanel.add(btnExportToFile);
		
		//file format selector panel
				JPanel exportFormatSelectorPanel = new JPanel();
				exportFormatSelectorPanel.setBounds(10, 100, 130, 50);
				exportPanel.add(exportFormatSelectorPanel);
				exportFormatSelectorPanel.setLayout(null);
				exportFormatSelectorPanel.setVisible(false);
				
				btnExportToFile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						exportFormatSelectorPanel.setVisible(true);
					}
				});
				
				JComboBox<String> exportFileFormatComboBox = new JComboBox();
				exportFileFormatComboBox.setBackground(Color.WHITE);
				exportFileFormatComboBox.setBounds(0, 0, 130, 20);
				exportFormatSelectorPanel.add(exportFileFormatComboBox);
				
				for(String formatitem : MainController.availableFileFormatStrings){
					exportFileFormatComboBox.addItem(formatitem);
				}
				
				JButton btnExportOk = new JButton("OK");
				btnExportOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						FileSelectorDialog fileDialog = new FileSelectorDialog(
								MemberDialog.this, 
								"Mappa megnyitása", 
								FileDialog.SAVE);
						if(fileDialog.getDirectory() != null) {
						if(exportFileFormatComboBox.getSelectedItem().equals(MainController.availableFileFormatStrings[1])) {
							ColumnDividerQueryDialog dividerSelector = new ColumnDividerQueryDialog(MemberDialog.this);
							dividerSelector.setVisible(true);
							fileDialog.setColumnDivider(dividerSelector.getColumnDiv());
						}
						fileDialog.setSelectedFormat((String) exportFileFormatComboBox.getSelectedItem());
						memberController.exportMembersToFile(fileDialog, dialogTableModel);
						}
					}
				});
				btnExportOk.setBounds(0, 27, 50, 23);
				exportFormatSelectorPanel.add(btnExportOk);
				btnExportOk.setFont(new Font("Tahoma", Font.PLAIN, 11));
				btnExportOk.setActionCommand("OK");
				
				JButton btnExportSelectionCancel = new JButton("M\u00E9gse");
				btnExportSelectionCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						exportFormatSelectorPanel.setVisible(false);
					}
				});
				btnExportSelectionCancel.setActionCommand("Cancel");
				btnExportSelectionCancel.setBounds(55, 27, 75, 23);
				exportFormatSelectorPanel.add(btnExportSelectionCancel);
		
		
		
		JButton btnBack = new JButton("Vissza");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnBack.setBounds(10, 447, 89, 23);
		contentPanel.add(btnBack);
		
		JScrollPane memberTableScrollPane = new JScrollPane();
		memberTableScrollPane.setBounds(220, 11, 554, 420);
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
		
		JButton buttonDel = new JButton("T\u00F6rl\u00E9s");
		buttonDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < dialogTableModel.getRowCount(); i++) {
					if((boolean) dialogTableModel.getValueAt(i, 0)) {
						dialogTableModel.removeRow(i);
						MemberController.setLoadedMembers(Member.convertMTM(dialogTableModel));
					}
					
				}
			}
		});
		buttonDel.setBounds(220, 442, 130, 23);
		contentPanel.add(buttonDel);
		
		JButton btnNewMember = new JButton("\u00DAj");
		btnNewMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewMemberDialog newMem = new NewMemberDialog(MemberDialog.this);
				newMem.setVisible(true);
				if(newMem.isSuccessful()) {
					Member.convertAndAppendMembers(MemberController.getLoadedMembers(), dialogTableModel);
				}
			}
		});
		btnNewMember.setBounds(360, 442, 89, 23);
		contentPanel.add(btnNewMember);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialogTableModel.setEditable(true);
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
