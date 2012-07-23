package org.knoesis.tags.analysis;

import java.util.List;

import org.knoesis.models.AnnotatedTweet;
/**
 * This class analyzes the specificity of a hashtag by analyzing
 * the extent to which the usage of a hashtag deviates from the 
 * usage of the word without a hash.
 * 
 * @author pavan
 *
 */
public class SpecificityAnalyzer implements Analyzer {
	private List<AnnotatedTweet> tweetsOfHashtag;
	private List<AnnotatedTweet> tweetsOfKeyword;
	
	public SpecificityAnalyzer(List<AnnotatedTweet> tweetsOfHashtag, List<AnnotatedTweet> tweetsOfKeyword) {
		this.tweetsOfHashtag = tweetsOfHashtag;
		this.tweetsOfKeyword = tweetsOfKeyword;
	}
	
	@Override
	public void analyze() {
		
	}

}
