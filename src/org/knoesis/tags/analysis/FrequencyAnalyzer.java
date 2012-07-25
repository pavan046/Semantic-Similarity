package org.knoesis.tags.analysis;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knoesis.models.AnnotatedTweet;

/**
 * This class analyzes the frequency of hashtags based on the below two attributes
 * 
 * 1. User
 * 2. Messages/Tweets
 * 
 * @author pavan
 *
 *	TODO: Pramod write code to set a file and read every line from the file which will have 
 *		 upto 1500 tweets and perform the following two counts
 *		
 *		1. Number of tweets/Period of time -- Ideally 1500/number of secs or mins 
 *		2. Number of users -- Total number of users who have created the 1500 tweets 
 *		3. We also need to calculate the distribution in the number of users
 *
 */
public class FrequencyAnalyzer implements Analyzer{
	private static double distinctUsersMentionKeyword = 0; 
	private static int messagesMentionKeyword = 0;
	private static double normalizedMessageCount = 0;
	private static Map<String, Integer> twitterUsers = new HashMap<String, Integer>();
	private List<AnnotatedTweet> tweets;
	private String keyword;
	
	public FrequencyAnalyzer(List<AnnotatedTweet> tweets, String keyword) {
		this.tweets = tweets;
		// FIXME: pramod -- Didn't get the use of this?
		this.keyword = keyword;
	}
	@Override
	public void analyze() {
		countMessages();
		countUsers();
	}
	
	/**
	 * This function counts the number of distict users mentioning the 
	 * hashtag and normalizes using the total number of messages fetched.
	 */
	private void countUsers(){
		long noOfTweets = tweets.size();
		for(AnnotatedTweet tweet: tweets){
			// if the user is present then add another post posted by the user
			if(twitterUsers.keySet().contains(tweet.getTwitter4jTweet().getFromUser()))
				twitterUsers.put(tweet.getTwitter4jTweet().getFromUser(), twitterUsers.get(tweet.getTwitter4jTweet().getFromUser())+1);
			else
				twitterUsers.put(tweet.getTwitter4jTweet().getFromUser(), 1);
		}
		distinctUsersMentionKeyword = (double)twitterUsers.keySet().size()/noOfTweets;
		
	}
	
	/**
	 * This function counts the messages that mention the hashtag 
	 * and the that mention just the keyword of the hashtag. And
	 * normalize this with the time period of the first to the last tweet
	 * 
	 * TODO: Might be good to involve the retweets to get to know the number of 
	 * 		 distinct tweets (RTs)
	 * 
	 * TODO: pramod -- Are we doing exactly what it is said above?
	 */
	public void countMessages(){
		int noOfTweets = tweets.size();
		
		// Getting the start and end date of given set of tweets. Assuming they are sorted.
		Date startDate = tweets.get(0).getTwitter4jTweet().getCreatedAt();
		Date endDate = tweets.get(noOfTweets-1).getTwitter4jTweet().getCreatedAt();
		long timeDiffinMilliSecs = startDate.getTime() - endDate.getTime();
		
		long timeDiffinMins = timeDiffinMilliSecs/(1000*60);
		setNormalizedMessageCount((double)noOfTweets/timeDiffinMins);
		
//		System.out.println("The startdate is: " + startDate.toString());
//		System.out.println("The enddate is: " + endDate.toString());

	}
	
	public Map<String, Double> getResults() {
		Map<String, Double> resultsMap = new HashMap<String, Double>();
		resultsMap.put("nMessageCount", getNormalizedMessageCount());
		resultsMap.put("nUserCount", distinctUsersMentionKeyword);
		return resultsMap;
	}
	
	/*
	 * Accessor methods
	 */
	public static void setNormalizedMessageCount(double normalizedMessageCount) {
		FrequencyAnalyzer.normalizedMessageCount = normalizedMessageCount;
	}
	public static double getNormalizedMessageCount() {
		return normalizedMessageCount;
	}

}
