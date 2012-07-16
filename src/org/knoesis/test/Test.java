package org.knoesis.test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.client.utils.URIUtils;

public class Test{
	String mURL = "United_States_presidential_election,_206";
	URI  uri;
	Boolean isCategory = false;
	public void print() throws UnsupportedEncodingException, URISyntaxException{
		//String regex = " \\(.*\\)";
		
		//String brackets_removed = mURL.replaceAll(regex,"");
//		String replaceSpace = mURL.replace(" ", "_");
//		
//		System.out.println(URLEncoder.encode(replaceSpace, "UTF-8"));
		
		
		if(mURL.matches("(.*)_2016"))
			isCategory = true;
		
		System.out.println(isCategory);
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, URISyntaxException {
		Test test = new Test();
		test.print();
	}
}