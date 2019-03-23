package iohandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TextFileHandler {
	
	public static ArrayList<String>  loadTxtFile(String path) {
		ArrayList<String> returnlist = new ArrayList<String>();
		
		if(!path.isEmpty()) {
			File file = new File(path);
			
			if(file.isFile()) {
				try {
					FileReader fileReader = new FileReader(path);
					BufferedReader breader = new BufferedReader(fileReader);
					String readline;
					while((readline = breader.readLine()) != null) {
						returnlist.add(readline);
					}
					breader.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					System.out.println(
							"Nem tallálható a megadott fájl. Biztos jó nevet, vagy elérési útvonalat adtál meg?");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}	
			}
			
		}
		
		
		
		return returnlist;
	}
	
	public static void writeTxtFile(ArrayList<String> linesToWrite, String path) {
		try {
			FileWriter fileWriter = new FileWriter(path);
			BufferedWriter bwriter = new BufferedWriter(fileWriter);
			String fileContent = "";
			for (String string : linesToWrite) {
				fileContent+= string + "\n";
			}
			
			bwriter.write(fileContent);
			
			bwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

