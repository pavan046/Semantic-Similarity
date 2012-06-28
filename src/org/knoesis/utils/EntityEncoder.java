package org.knoesis.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
 * Pramod TODO: I am sure there is a better way to do this.. Check for encoding and decoding of URLs.
 * and use testcases and see what is wrong.. 
 * 
 */
public class EntityEncoder {

	public static String encode(String entity) {
		/**
		 * TODO: For now this is fine but make sure to find a better way to 
		 * encode entities to its appropriate URLs.
		 */
		String encodedReplaceCommas = null;

		try {

			String replaceSpace = entity.replace(" ", "_");
			String encoded = URLEncoder.encode(replaceSpace, "UTF-8");
			//String removingYear = encoded.replaceAll("[1-2][0-9][0-9][0-9]_", "");
			encodedReplaceCommas = encoded.replaceAll("%2C", ",");
			
		} catch(UnsupportedEncodingException e){
			//TODO: Make sure to handle this exception
			e.printStackTrace();
		}

		return encodedReplaceCommas;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String encodedEntity = EntityEncoder.encode("2012 Green National Convention");
		System.out.println(encodedEntity);
	}
}
