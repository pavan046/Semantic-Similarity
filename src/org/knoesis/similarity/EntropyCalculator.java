package org.knoesis.similarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.twarql.extractions.Extractor;
import org.knoesis.twarql.extractions.TagExtractor;
import org.knoesis.twarql.extractions.TermFrequencyGenerator;
import org.knoesis.twitter.crawler.SearchTwitter;

/**
 * The objective of this class is to calculate the entropy 
 * given a list of terms and its occurrances. Also if possible 
 * given the total number of terms of the document.
 * 
 * @author pavan
 *
 */
public class EntropyCalculator {
	private Map<String, Double> termFrequency;
	private int totalNumberOfterms;
	
	public EntropyCalculator(Map<String, Double> termFrequency) {
		this.termFrequency = termFrequency;
		localSetTotalNumberOfTerms();
	}
	/**
	 * Sums and sets the total number of terms to 
	 * calculate the probability.
	 */
	private void localSetTotalNumberOfTerms(){
		int totalTerms = 0; 
		for(String term:termFrequency.keySet())
			totalTerms += termFrequency.get(term);
		this.totalNumberOfterms = totalTerms;	
	}
	/**
	 * Normalizes the termfrequency with the total number of terms occurring in the 
	 * tweets provided. 
	 * @return
	 */
	public double calculate(){
		double entropy = 0d;
		for(String term: termFrequency.keySet()){
			double probability = (double)termFrequency.get(term)/(double)totalNumberOfterms;
			entropy += probability * Math.log(probability)/Math.log(2.0d);
		}
		entropy = -1.0d * entropy;
		return entropy;
	}
	
	public static void main(String[] args) {
		List<Extractor> extractors = new ArrayList<Extractor>();
		extractors.add(new TagExtractor());
		SearchTwitter searchTwitter = new SearchTwitter(extractors);
		// This will get the last 1500 annotated Tweets.
		List<AnnotatedTweet> tweetsOfHashtag = searchTwitter.getTweets("#tcot", true, false);
		TermFrequencyGenerator termFrequencyGenerator = new TermFrequencyGenerator();
		Map<String, Double> termFrequency = termFrequencyGenerator.extractListTweets(tweetsOfHashtag);
		System.out.println(termFrequency);
		EntropyCalculator entropyCalculator = new EntropyCalculator(termFrequency);
		System.out.println(entropyCalculator.calculate());
		
	}
}
