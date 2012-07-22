package org.knoesis.tags.analysis;

import java.io.File;

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
 */
public class FrequencyAnalyzer implements Analyzer{
	private static int usersMentionHashtag = 0; 
	private static int usersMentionKeyword = 0;
	private static int messagesMentionHashtag = 0;
	private static int messagesMentionKeyword = 0;
	private File tweetJsonFile;
	private String hashTag;
	
	public FrequencyAnalyzer(File tweetJsonFile, String hashTag) {
		this.tweetJsonFile = tweetJsonFile;
		this.hashTag = hashTag;
	}
	@Override
	public void analyze() {
		String user = ""; 
		String tweet = "";
		//Process tweets from the tweetJsonFile; 
		// Get tweets and the users
		
		countUsers(user, tweet);
		countMessages(tweet);
	}
	/**
	 * This function counts the number of distict users mentioning the 
	 * hashtag
	 */
	public void countUsers(String userName, String tweet){
		
	}
	/**
	 * This function counts the messages that mention the hashtag 
	 * and the that mention just the keyword of the hashtag
	 * 
	 */
	public void countMessages(String tweet){
		
	}

}
