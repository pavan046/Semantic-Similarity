package org.knoesis.twarql.extractions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.URLModel;
import org.knoesis.utils.Tokenizer;
/**
 * The objective of this class is to generate a bag of words give a list of 
 * annotated tweets. The bag of words contain the tfidf of each word
 * except stopwords and urls.
 * 
 * This class is expected to be called after the following two extractors, because
 * it uses the output of these to generate the bag of words.
 * 
 * 1. URLExtractor
 * 2. TagExtractor 
 * 
 * @author pavan
 *
 */
public class TermFrequencyGenerator implements Extractor<Map<String, Integer>> {

	@Override
	public Map<String, Integer> extract(Object tweet) {
		AnnotatedTweet aTweet = (AnnotatedTweet) tweet;
		String tweetText = aTweet.getTwitter4jTweet().getText();
		Map<String, Integer> termFrequency = new HashMap<String, Integer>();
		// Remove URLs
		Set<URLModel> urlModels = aTweet.getUrlModels();
		if (urlModels != null){
			for (URLModel urlModel: urlModels)
				tweetText.replace(urlModel.getShortURL().toString(), ""); 
		}
		// Remove hashtags
		Set<String> hashTags = aTweet.getHashtags();
		if (hashTags != null){
			for (String tag: hashTags)
				tweetText.replace(tag, "");
		}

		// TODO: I am not sure whether stemming is required 
		// FIXME: Tokenizer -- This also cleans urls again overhead work 
		String[] tokens = Tokenizer.tokenize(tweetText.toLowerCase()); 
		for (int i=0; i<tokens.length; i++){
			// If tokens are lesser than 3 chars then ignore
			if (tokens[i].length()<3)
				continue;
			if (termFrequency.containsKey(tokens[i]))
				termFrequency.put(tokens[i], termFrequency.get(tokens[i])+1);
			else
				termFrequency.put(tokens[i], 1);
		}

		return termFrequency;
	}

	@Override
	public void process(AnnotatedTweet tweet) {
		tweet.setBagOfWords(this.extract(tweet));

	}

	/**
	 * This function is specifically to merge the bag of words of a list of 
	 * tweets and provide a single map of words with their frequencies.
	 * 
	 * @param tweets
	 * @return
	 */
	public Map<String, Integer> extractListTweets(List<AnnotatedTweet> tweets){
		Map<String, Integer> termFrequencyMap = new HashMap<String, Integer>();

		for(AnnotatedTweet tweet : tweets){

			Map<String, Integer> tempTermFreqMap = this.extract(tweet);

			for(String term : tempTermFreqMap.keySet()){
				if(termFrequencyMap.containsKey(term)){
					int newCount = termFrequencyMap.get(term) + tempTermFreqMap.get(term);
					termFrequencyMap.put(term, newCount);
				}else
					termFrequencyMap.put(term, 1);
			}			
		}
		return termFrequencyMap;
	}

}
