package views.windowviews.utilitydialogs;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;

public class FileSelectorDialog extends FileDialog{
	public FileSelectorDialog(Frame owner, String title, int mode, String selectedFormat) {
		super(owner, title, mode);
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
}
