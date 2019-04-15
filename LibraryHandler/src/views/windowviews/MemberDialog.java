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
import views.windowviews.utilitydialogs.DBLoginDialog;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;

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
		loadPanel.setBounds(10, 11, 200, 120);
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
				MemberDialog.memberController.loadMemberDataFromDB();
				Member.convertAndAppendMembers(MemberController.getLoadedMembers(), dialogTableModel);
			}
		});
		btnLoadFromDB.setBounds(10, 36, 130, 23);
		loadPanel.add(btnLoadFromDB);
		
		JButton btnLoadFromFile = new JButton("F\u00E1jlb\u00F3l");
		btnLoadFromFile.setBounds(10, 70, 130, 23);
		loadPanel.add(btnLoadFromFile);
		
		JPanel exportPanel = new JPanel();
		exportPanel.setLayout(null);
		exportPanel.setBounds(10, 142, 200, 120);
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
