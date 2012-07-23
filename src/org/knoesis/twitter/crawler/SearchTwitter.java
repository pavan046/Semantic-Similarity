package org.knoesis.twitter.crawler;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.knoesis.models.AnnotatedTweet;
import org.knoesis.twarql.extractions.Extractor;
import org.knoesis.twarql.extractions.TagExtractor;
import org.knoesis.twarql.extractions.TweetProcessor;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
/**
 * The objective of this class is to get the last 1500 tweets for 
 * a given keyword and support the analysis for consideration of 
 * the hashtag.
 * 
 * @author pavan
 *
 */
public class SearchTwitter {
	/*Defining required classes for init()*/
	private Twitter twitter = null;
	private static TweetProcessor processor = null;

	public SearchTwitter(List<Extractor> extractors) {
		processor = new TweetProcessor(extractors);
	}
	/**
	 * Processes the tweets based on the extractors provided and transforms 
	 * the Tweet to AnnotatedTweet (org.knoesis.model). 
	 * 
	 * TODO: For now the list of extractors are just TagExtractor.
	 * 
	 * @param tag
	 */
	private void getTweets(String tag) {
		Twitter twitter = new TwitterFactory().getInstance("streamingpavan", "Knoesis2009");
		Query query = new Query(tag); 	// Query for the search
		query.setRpp(100);	//default is 15 tweets/search which is set to 100
		QueryResult result = null;
		List<AnnotatedTweet> tweets = new ArrayList<AnnotatedTweet>();
		for(int i=1; i<=15; i++){
			query.setPage(i);
			try {
				result = twitter.search(query);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (result.getTweets().isEmpty())
				break;
			else
				tweets.addAll(processor.process(TweetFactory.Tweet2AnnotatedTweet(result.getTweets())));
		}
		System.out.println(tweets);
	}
	
	public static void main(String[] args) {
		List<Extractor> extractors = new ArrayList<Extractor>();
		extractors.add(new TagExtractor());
		SearchTwitter searchTwitter = new SearchTwitter(extractors);
		searchTwitter.getTweets("obama");
	}

}