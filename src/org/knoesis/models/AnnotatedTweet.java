package org.knoesis.models;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.crepezzi.tweetstream4j.types.STweet;


import twitter4j.Status;
import twitter4j.Tweet;

/**
 * @author pavan
 * 
 * This class has been a modification from twarql AnnotatedTweet Model.
 * TODO: This is bad practice to copy paste, rather think about merging the two projects 
 * 		will help further.
 *
 */
public class AnnotatedTweet implements Serializable{
	// One of the three would be available.
	private Status tweet; 
	private STweet Stweet;
	private Tweet twitter4jTweet;
	
	
	private Set<String> entities;
	private Set<String> hashtags;
	private Map<String,String> hashtagDef = new HashMap<String,String>();
	private Set<Triple> triples = new HashSet<Triple>();
	
	
	//TODO: Pavan: I am not sure whether this is required.. Yet to be debated
    
	public AnnotatedTweet() {}

	//TODO Move this to factory
	public AnnotatedTweet(Status tweet) {
		this.setStatusTweet(tweet);
	}

	/*
	 * Setter methods
	 */
	
	public void setEntities(Set<String> setEntity){
		entities=setEntity;
	}
	
	/*
	 * getter methods
	 */
	
	
	public Set<String> getEntities(){
		return entities;
	}

		
	private String expandHashtags(String txt) {
		if (hashtagDef.size() != 0) {
			for (String h: hashtags) {
				String def = hashtagDef.get(h);
				txt.replaceAll(h, h+" ("+def+"");
			}
		}
		return txt;
	}
	
	
	public Set<String> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<String> hashtags) {
		this.hashtags = hashtags;
	}

	
	
	@Override
	public String toString() {
		return "TWEET " +
		        "\n\tentities: "+getEntities()+
		        "\n\thashtags: "+getHashtags();
	}

	public Status getStatusTweet() {
		return tweet;
	}

	public void setStatusTweet(Status tweet) {
		this.tweet = tweet;
	}

	public Set<Triple> getTriples() {
		return triples;
	}

	public void setTriples(Set<Triple> triples) {
		this.triples = triples;
	}

	public STweet getStweet() {
		return Stweet;
	}

	public void setStweet(STweet stweet) {
		Stweet = stweet;
	}

	public Tweet getTwitter4jTweet() {
		return twitter4jTweet;
	}

	public void setTwitter4jTweet(Tweet twitter4jTweet) {
		this.twitter4jTweet = twitter4jTweet;
	}

	
}
