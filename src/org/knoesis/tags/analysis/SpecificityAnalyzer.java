package org.knoesis.tags.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.similarity.CosineSimilarityCalculator;
import org.knoesis.twarql.extractions.Extractor;
import org.knoesis.twarql.extractions.TagExtractor;
import org.knoesis.twarql.extractions.TermFrequencyGenerator;
import org.knoesis.twitter.crawler.SearchTwitter;
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
	private double specificityMeasure;
	
	private TermFrequencyGenerator termFreqGenerator = null;
	
	private Map<String, Integer> termFreqForTweetsOfHashtag = null;
	private Map<String, Integer> termFreqForTweetsOfKeyword = null;
	
	public SpecificityAnalyzer(List<AnnotatedTweet> tweetsOfHashtag, List<AnnotatedTweet> tweetsOfKeyword) {
		this.tweetsOfHashtag = tweetsOfHashtag;
		this.tweetsOfKeyword = tweetsOfKeyword;
		termFreqGenerator = new TermFrequencyGenerator();
	}
	
	@Override
	public void analyze() {
		termFreqForTweetsOfHashtag = termFreqGenerator.extractListTweets(tweetsOfHashtag);
		termFreqForTweetsOfKeyword = termFreqGenerator.extractListTweets(tweetsOfKeyword);		
		specificityMeasure =  CosineSimilarityCalculator.calculate(termFreqForTweetsOfHashtag, termFreqForTweetsOfKeyword);				
	}

	public void setSpecificityMeasure(double specificityMeasure) {
		this.specificityMeasure = specificityMeasure;
	}

	public double getSpecificityMeasure() {
		return specificityMeasure;
	}

	public static void main(String[] args) {
		List<Extractor> extractors = new ArrayList<Extractor>();
		extractors.add(new TagExtractor());
		SearchTwitter searchTwitter = new SearchTwitter(extractors);
		// This will get the last 1500 annotated Tweets.
		List<AnnotatedTweet> tweetsOfHashtag = searchTwitter.getTweets("#gop", true);
		List<AnnotatedTweet> tweetsOfKeyword = searchTwitter.getTweets("gop", false);
		
		SpecificityAnalyzer specificity = new SpecificityAnalyzer(tweetsOfHashtag, tweetsOfKeyword);
		specificity.analyze();
		System.out.println("The specificity Measure is :" + specificity.getSpecificityMeasure());
	}
}
