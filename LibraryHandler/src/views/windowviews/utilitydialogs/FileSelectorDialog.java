package views.windowviews.utilitydialogs;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;

public class FileSelectorDialog extends FileDialog{
	private String selectedFormat;
	private char columnDivider = '|';
	public FileSelectorDialog(Frame owner, String title, int mode, String selectedFormat) {
		super(owner, title, mode);
		this.selectedFormat = selectedFormat;
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		
		switch (selectedFormat) {
		case "CSV":
			this.setFile("*.csv");
			break;

		case "Custom Text File":
			this.setFile("*.*");
			break;
		case "XML":
			this.setFile("*.xml");
			break;
		case "JSON":
			this.setFile("*.json");
			break;
		default:
			break;
		}
		
		this.setVisible(true);
		
	}
	
	public FileSelectorDialog(Dialog owner, String title, int mode, String selectedFormat) {
		super(owner, title, mode);
		this.selectedFormat = selectedFormat;
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		
		switch (selectedFormat) {
		case "CSV":
			this.setFile("*.csv");
			break;

		case "Custom Text File":
			this.setFile("*.*");
			break;
		case "XML":
			this.setFile("*.xml");
			break;
		case "JSON":
			this.setFile("*.json");
			break;
		default:
			break;
		}
		
		this.setVisible(true);
		
	}
	
	public FileSelectorDialog(Dialog owner, String title, int mode) {
		super(owner, title, mode);
		this.setVisible(true);
	}
	

	public String getSelectedFormat() {
		return selectedFormat;
	}

	public void setSelectedFormat(String selectedFormat) {
		this.selectedFormat = selectedFormat;
	}

	public char getColumnDivider() {
		return columnDivider;
	}

	public void setColumnDivider(char columnDivider) {
		this.columnDivider = columnDivider;
	}
	
	
	
	
}
