package org.knoesis.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Time;
/**
 * 
 * @author pramod
 *
 *	TODO: Write to files should be done at once and not open and close connections this many times.
 *		  Also, A particular class should be designed for one object with appropriate methods. 
 *		  Explain how sleep and writeToFile come under a single class.
 *		
 *		 If this is being used just for tests its fine.
 */
public class Utils {
	/**
	 * One of the util function to make the process wait for x secs
	 * @param secs
	 */
	public static void sleep(int secs){
		try{
			Thread.sleep(secs*1000);
		} catch (InterruptedException e){
			//FIXME See what exception is being thrown and what to do about it
		}
	}
	/**
	 * The objective of this menthod is to write a given String to a file.
	 * TODO: Not sure how effective this is by opening and closing the file for every 
	 * 		 line you write
	 * @param filename
	 * @param content
	 */
	public static void writeToFile(String filename, String content){
		FileWriter file;
		try {
			file = new FileWriter(filename,true);
			BufferedWriter writer = new BufferedWriter(file);

			writer.append(content);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void wikiToDbpedia(String wikiEntity){
	
	}
	
	public static String dbpediaDecode(String dbpediaEntity){
		String temp = null;
		try {
			temp = dbpediaEntity.replace("http://dbpedia.org/resource/", "");
			String linkSpaceReplaced = temp.replace("_", " ");
			temp = URLDecoder.decode(linkSpaceReplaced,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	public static void main(String[] args) {
		Utils.writeToFile("test", "My name is pramod");
		Utils.writeToFile("test", "This is good");
	}
}
