package org.knoesis.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Temporary class while we're cleaning up the code.
 * Breaks textual content from tweet into a list of tokens
 *  
 * @author PabloMendes
 *
 */
public class Tokenizer {

	public static String PUNCTUATION = "[\"'()<>{}\\[\\]:;,.!?\\\\/&%$*#@\\+\\-=\\s]+";
	
	//TODO This is duplicate code. 
	// The same appears in URLExtractor. 
	// formerly checkurls
	public static String cleanurls(String tweet)
	{
		Pattern p = Pattern.compile("((http|https|ftp)://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[~#&\\n\\-=?\\+\\%/\\.\\w]+)?");
		Matcher m = p.matcher(tweet);
		while (m.find()) {
			String href = m.group();
			tweet = tweet.replace(href, "");
		}
		return tweet;
		
	}
	
	public static String[] tokenize(String tweetContent) {
		tweetContent = cleanurls(tweetContent);
		tweetContent = " "+tweetContent+" ";
		return tweetContent.split(PUNCTUATION);
	}
	
}
