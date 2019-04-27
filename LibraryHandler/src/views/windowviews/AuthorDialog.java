package views.windowviews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import controllers.AuthorController;
import controllers.BookController;
import controllers.MainController;
import models.Author;
import models.Book;
import models.GenericTableModel;
import views.windowviews.creationdialogs.NewAuthorDialog;
import views.windowviews.creationdialogs.NewBookDialog;
import views.windowviews.utilitydialogs.ColumnDividerQueryDialog;
import views.windowviews.utilitydialogs.ConfirmDialog;
import views.windowviews.utilitydialogs.DBLoginDialog;
import views.windowviews.utilitydialogs.FileSelectorDialog;

public class AuthorDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable authorTable;
	private static GenericTableModel dialogTableModel;
	private static AuthorController authorController;

	/**
	 * Create the dialog.
	 */
	public AuthorDialog(JFrame owner) {
		super(owner, "Tagok", true);
		
		authorController = new AuthorController();
		Object[] fieldNames = {" ","Kód", "Szerzõ Neve"}; 
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
		
		JLabel lblLoadTitle = new JLabel("Szerz\u0151k bet\u00F6lt\u00E9se");
		lblLoadTitle.setBounds(10, 11, 180, 14);
		loadPanel.add(lblLoadTitle);
		
		JButton btnLoadFromDB = new JButton("Adatb\u00E1zisb\u00F3l");
		btnLoadFromDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DBLoginDialog login = null;
				if(!MainController.isConnectionOpen()) {
					login = new DBLoginDialog(AuthorDialog.this);
					login.setVisible(true);
					login.dispose();
				}
					
					if (login != null && login.isSuccessfulLogin()) {
						authorController.loadAuthorDataFromDB();
						Author.convertAndAppendAuthorss(AuthorController.getLoadedAuthors(), dialogTableModel);
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
						AuthorDialog.this, 
						"Fájl Megnyitása", 
						FileDialog.LOAD, 
						(String)fileFormatComboBox.getSelectedItem());
				if(fileDialog.getFile() != null) {
				if(fileDialog.getSelectedFormat().equals(MainController.availableFileFormatStrings[1])) {
					ColumnDividerQueryDialog dividerSelector = new ColumnDividerQueryDialog(AuthorDialog.this);
					dividerSelector.setVisible(true);
					fileDialog.setColumnDivider(dividerSelector.getColumnDiv());
				}
				
				authorController.loadAuthorsFromFile(fileDialog);
				
				Author.convertAndAppendAuthorss(AuthorController.loadedAuthors, dialogTableModel);
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
		
		JLabel lblExportTitle = new JLabel("Szerz\u0151k export\u00E1l\u00E1sa");
		lblExportTitle.setBounds(10, 11, 180, 14);
		exportPanel.add(lblExportTitle);
		
		JButton buttonUploadToDBOk = new JButton("OK");
		buttonUploadToDBOk.setEnabled(false);
		buttonUploadToDBOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmDialog confirmDialog = new ConfirmDialog(AuthorDialog.this, 
						"Biztosan szeretné exportálni a táblázatban lévõ szerzõket? \n Ha az adatbázisban már van a táblázatban szereplõ kódú tag,\n az nem lesz az adatbázisba illesztve!");
				confirmDialog.setVisible(true);
				if(confirmDialog.getChoice()) {
					for(Author author : AuthorController.getLoadedAuthors()) {
						authorController.insertAuthorIntoDB(author);
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
					DBLoginDialog login = new DBLoginDialog(AuthorDialog.this);
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
								AuthorDialog.this, 
								"Mappa megnyitása", 
								FileDialog.SAVE);
						if(fileDialog.getDirectory() != null) {
						if(exportFileFormatComboBox.getSelectedItem().equals(MainController.availableFileFormatStrings[1])) {
							ColumnDividerQueryDialog dividerSelector = new ColumnDividerQueryDialog(AuthorDialog.this);
							dividerSelector.setVisible(true);
							fileDialog.setColumnDivider(dividerSelector.getColumnDiv());
						}
						fileDialog.setSelectedFormat((String) exportFileFormatComboBox.getSelectedItem());
						authorController.exportAuthorsToFile(fileDialog, dialogTableModel);
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
		
		JScrollPane bookTableScrollPane = new JScrollPane();
		bookTableScrollPane.setBounds(220, 11, 554, 420);
		contentPanel.add(bookTableScrollPane);
		
		authorTable = new JTable(dialogTableModel);
		
		TableColumn tc = null;
		for (int i = 0; i < 6; i++) {
			tc = authorTable.getColumnModel().getColumn(i);
			if(i==0 || i==1){
				tc.setPreferredWidth(30);
			}
			else{
				tc.setPreferredWidth(100);
			}
		}
		
		authorTable.setAutoCreateRowSorter(true);
		TableRowSorter<GenericTableModel> trs = (TableRowSorter<GenericTableModel>)authorTable.getRowSorter();
		trs.setSortable(0, false);
		
		bookTableScrollPane.add(authorTable);
		
		bookTableScrollPane.setViewportView(authorTable);
		
		JButton buttonDel = new JButton("T\u00F6rl\u00E9s");
		buttonDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < dialogTableModel.getRowCount(); i++) {
					if((boolean) dialogTableModel.getValueAt(i, 0)) {
						dialogTableModel.removeRow(i);
						BookController.setLoadedBooks(Book.convertMTM(dialogTableModel));
					}
					
				}
			}
		});
		buttonDel.setBounds(220, 442, 130, 23);
		contentPanel.add(buttonDel);
		
		JButton btnNewBook = new JButton("\u00DAj");
		btnNewBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewAuthorDialog newAuthorDialog = new NewAuthorDialog(AuthorDialog.this);
				newAuthorDialog.setVisible(true);
				if(newAuthorDialog.isSuccessful()) {
					Author.convertAndAppendAuthorss(AuthorController.getLoadedAuthors(), dialogTableModel);
				}
			}
		});
		btnNewBook.setBounds(360, 442, 89, 23);
		contentPanel.add(btnNewBook);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialogTableModel.setEditable(true);
	}

	public static GenericTableModel getDialogTableModel() {
		return dialogTableModel;
	}

	public static void setDialogTableModel(GenericTableModel dialogTableModel) {
		AuthorDialog.dialogTableModel = dialogTableModel;
	}

	public static AuthorController getAuthorController() {
		return authorController;
	}

	public static void setAuthorController(AuthorController authorController) {
		AuthorDialog.authorController = authorController;
	}


	
	

}
