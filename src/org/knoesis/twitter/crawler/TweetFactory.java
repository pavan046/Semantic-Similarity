package org.knoesis.twitter.crawler;

import java.util.ArrayList;
import java.util.List;

import org.knoesis.models.AnnotatedTweet;

import twitter4j.Tweet;

import com.crepezzi.tweetstream4j.types.STweet;

/**
 * Use this class to construct tweets or convert between tweet formats
 * WARNING : We will need to think more about this here. 
 * Maybe only use one type across our code and convert any external type to ours,
 * or maybe subscribe to one of theirs... I don't know. Let's think.
 * 
 * @author PabloMendes
 */
public class TweetFactory {
	
	public static AnnotatedTweet STweet2AnnotatedTweet(STweet st) {
		AnnotatedTweet t = new AnnotatedTweet();
		//TODO PABLO: Pavan, please get this model straightened out.
		t.setStweet(st);
		
//		t.setTruncated(st.getTruncated());
//		t.setGeo(st.getGeo());
//		t.setStatusId(st.getStatusId();
//		t.setSource();st.getSource();
//		st.getInReplyToUserId();
//		st.getInReplyToStatusId();
//		st.getInReplyToScreenName();
//		st.getGeo();
//		st.getFavorited();
//		st.getCreatedAt();
		return t;
	}

	public static AnnotatedTweet Tweet2AnnotatedTweet(Tweet st) {
		AnnotatedTweet t = new AnnotatedTweet();
		t.setTwitter4jTweet(st);
		//TODO there are a bunch of other fields to do
		return t;
	}
	
	public static AnnotatedTweet Tweet2AnnotatedTweet(Tweet st, String searchKeyword) {
		AnnotatedTweet t = new AnnotatedTweet(st, searchKeyword);
		return t;
	}
	// TODO: Logic changed custom to put tweets just which matches the keyword and 
	// 		 does not put tweets which has keyword equivalent hashtags.
	// 		Functionality to differentiate between hashtag search and keyword search
	public static List<AnnotatedTweet> Tweet2AnnotatedTweet(List<Tweet> list, String searchKeyword) {
		List<AnnotatedTweet> converted = new ArrayList<AnnotatedTweet>();
		for (Tweet t: list) {
			AnnotatedTweet aT = Tweet2AnnotatedTweet(t, searchKeyword);
			if(searchKeyword.startsWith("#"))
				converted.add(aT);
			else if(!aT.isContainsHashtagEquivalentToKeyword())
				converted.add(aT);
		}
		return converted;
	}

}
