package org.knoesis.twarql.extractions;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.knoesis.twarql.extractions.Extractor;
import org.knoesis.models.AnnotatedTweet;

/**
 * This extracts hashtags from tweets
 * 
 * @author PabloMendes
 *
 */
public class TagExtractor implements Extractor {

	@Override
	public Set<String> extract(Object text) {
		Set<String> tags = new TreeSet<String>();
		Pattern p = Pattern.compile("(#[a-zA-Z_0-9\\-]+)");
		Matcher m = p.matcher((String)text);  
		while (m.find()) {
			String tag = m.group();
			tags.add(tag);
		}
		return tags;
	}

	@Override
	public void process(AnnotatedTweet tweet) {
		tweet.setHashtags(extract(tweet.getTwitter4jTweet().getText()));
	}

	public static void main(String[] args) {
		TagExtractor extractor = new TagExtractor();
		String[] a = {"@example this is a really silly example of #hashtag1 with no problems",
					  "@example this has no space at the end #hashtag2",
					  "@example this has two of them #hashtag3 #hashtag4 ",
					  "@example this has two of them together #hashtag3#hashtag4 ",
		};
		
		for (String tweet : a) {
			System.out.println(extractor.extract(tweet));
		}
	}
}
