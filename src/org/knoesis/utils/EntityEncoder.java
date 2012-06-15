package org.knoesis.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
 * This class encodes the raw enitity into dbpedia format
 */
public class EntityEncoder {

	public static String encode(String entity) throws UnsupportedEncodingException{
		
//		String regex = " \\(.*\\)";
//		String brackets_removed = entity.replaceAll(regex,"");
		String replaceSpace = entity.replace(" ", "_");
		String encoded = URLEncoder.encode(replaceSpace, "UTF-8");
		String encoded2 = encoded.replaceAll("%2C", ",");
		
		//Hardcoding-- Replacing number like 2012,2008 etc etc
//		String encoded3 = encoded2.replaceAll(",_[1-2][0-9][0-9][0-9]", "");
		String encoded4 = encoded2.replaceAll("[1-2][0-9][0-9][0-9]_", "");
		return encoded4;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String encodedEntity = EntityEncoder.encode("2012 Green National Convention");
		System.out.println(encodedEntity);
	}
}
