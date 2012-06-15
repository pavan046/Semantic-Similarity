package org.knoesis.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;

public class Utils {
	
	public static void sleep(int secs) throws InterruptedException{
		Thread.sleep(secs*1000);
	}
	
	public static void writeToFile(String filename, String content) throws IOException{
		FileWriter file = new FileWriter(filename,true);
		BufferedWriter writer = new BufferedWriter(file);
		
		writer.append(content);
		writer.newLine();
		writer.close();
	}
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		Utils.writeToFile("test", "My name is pramod");
		Utils.writeToFile("test", "This is good");
	}
}
