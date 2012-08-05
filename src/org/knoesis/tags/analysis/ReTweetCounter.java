package org.knoesis.tags.analysis;

import java.util.List;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.HashTagAnalytics;

/**
 * This class is used to count the number of Re-tweets in a given list of tweets
 * @author koneru
 *
 * FIXME: In the package that we are using there is no method to get Retweets, so used regex
 */
public class ReTweetCounter implements Analyzer {

	@Override
	public void analyze(HashTagAnalytics hashTag) {
		hashTag.setNoOfReTweets(countNoOfReTweets(hashTag.getaTweetsOfHashTag()));
	}
	
	private int countNoOfReTweets(List<AnnotatedTweet> tweets){
		int noOfReTweets = 0;
		for(AnnotatedTweet tweet: tweets){
			if(tweet.getTwitter4jTweet().getText().startsWith("RT")){
				noOfReTweets++;
			}
		}
		return noOfReTweets;
	}
	

}
