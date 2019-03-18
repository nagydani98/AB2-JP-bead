package iohandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader {
	
	
	//will read and Integer value
	public static int readInt() {
		int number = 0;
		boolean ok = false;
		InputStreamReader inputStream = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(inputStream);
		
		do {
			try {
				number = Integer.valueOf(reader.readLine());
				ok = true;
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("Invalid input, input was not an Integer");
			}
		} while (ok);
		return number;
	}
	
	/*will read an Integer value greater or equal than the floor 
	and less or equal than the ceiling given*/
	public static int readIntInRange(int floor, int ceiling) { 
		
		int number = 0;
		boolean unsuccessful = true;
		
		do {
			
				number = readInt();
				if(number <= ceiling && number >= floor)
					unsuccessful = false;
				else System.out.println("Invalid input, Integer was not in the required range");
				
		} while (unsuccessful);
		return number;
	}
	
	//will read a line and return it as a String value
	public static String readString() {
		InputStreamReader inputStream = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(inputStream);
		
		String returnValue = "";
		try {
			returnValue = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnValue;
	}
	
	//will read a line and check if it's below or equal to the required maximum length
	public static String readStringWithLength(int maxLength) {
		String returnValue = "";
		boolean ok = false;
		
		do {
			returnValue = readString();
			
			if(returnValue.length() <= maxLength) {
				ok = true;
			}
			else {
				System.out.println("Input is longer than what is required");
			}
				
		} while (ok);
		
		return returnValue;
	}
}
