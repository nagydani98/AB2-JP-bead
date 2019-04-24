package models;

import javax.swing.table.DefaultTableModel;

public class GenericTableModel extends DefaultTableModel{
	private boolean editable = false;
	
	public GenericTableModel(Object fildNames[], int rows){
		super(fildNames, rows);
	}
	
	public boolean isCellEditable(int row, int col){
		if(editable) {
			return true;
		}
		else {
			if(col == 0) {
					return true;
			}
			return false;
		}
	}
	
	public Class<?> getColumnClass(int index){
		if(index == 0){
			return Boolean.class;
		}
		else if(index == 1 || index == 5){
			return Integer.class;
		}
		else return (String.class);
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	
}
