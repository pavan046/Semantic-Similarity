package org.knoesis.tags.analysis;

import java.util.List;

import org.knoesis.models.AnnotatedTweet;
/**
 * This class analyzes the specificity of a hashtag by analyzing
 * the extent to which the usage of a hashtag deviates from the 
 * usage of the word without a hash.
 *
 * TODO: 
 * 1. Should get tweets (last 1500) for hashtag
 * 2. Should get tweets (last 1500) to the same hashtag without hash
 * 3. Use Bagofwords to generate term frequency
 * 4. get the cosine similarity between those
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
