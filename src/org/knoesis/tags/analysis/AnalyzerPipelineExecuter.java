package org.knoesis.tags.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.HashTagAnalytics;
import org.knoesis.storage.TagAnalyticsDataStore;
import org.knoesis.twarql.extractions.Extractor;
import org.knoesis.twarql.extractions.TagExtractor;
import org.knoesis.twarql.extractions.TermFrequencyGenerator;
import org.knoesis.twitter.crawler.SearchTwitter;

/**
 * This class needs to take in a hashtag
 * 
 * 1. Get the tweets for the hashtag and the keyword without the hashtag
 * 2. Peform the frequency and Specificity and Consistency Analyzer in a pipeline
 * 			this has to be taken as a List of Analyzers for the constructor (See TweetProcessor)
 * 3. Return a list of results
 * 4. Main function make sure that these results with what u think is necessary for evaluation 
 * 			to be written in a file.
 * @author pavan
 * @author pramod
 */
public class AnalyzerPipelineExecuter {

	List<Analyzer> analyzers = new ArrayList<Analyzer>();
	private Map<String, Double> resultsMap = new HashMap<String, Double>();

	/**
	 * This constructor takes in the list of analyzers like frequency,specificity etc
	 * @param analyzers
	 */
	public AnalyzerPipelineExecuter(List<Analyzer> analyzers){
		this.analyzers = analyzers;
	}

	/**
	 * Transforms the Hashtags to the model HashTagAnalytics and returns 
	 * a list of HashTagAnalytics as a response
	 * @return resultsHashTags
	 */
	public List<HashTagAnalytics> process(List<String> hashTags){
		//Map<String, Double> resultsMap = new HashMap<String, Double>();
		List<HashTagAnalytics> hashTagsAnalytics = new ArrayList<HashTagAnalytics>();
		for (String tag: hashTags){
			hashTagsAnalytics.add(process(tag));
		}
		return hashTagsAnalytics;
	}
	
	public HashTagAnalytics process(String hashTag){
		HashTagAnalytics hashTagAnalytics = new HashTagAnalytics(hashTag);
		for (Analyzer analyzer: analyzers)
			analyzer.analyze(hashTagAnalytics);
		return hashTagAnalytics;
	}

	public Map<String, Double> getResultsMap(){
		return resultsMap;
	}

	/**
	 * For now to print the results just added print statements in every method. 
	 * Should do it in a better way
	 * 
	 * Start the pipeline here and store it into the DB
	 * @param args
	 */
	public static void main(String[] args) {
		
		TagAnalyticsDataStore db = new TagAnalyticsDataStore();
		List<Analyzer> analyzers = new ArrayList<Analyzer>();
		// Adding specificity Analyzer
		analyzers.add(new SpecificityAnalyzer());
		// Adding consistency analyzer
		analyzers.add(new ConsistencyAnalyzer());
		// Adding Frequency Analyzer
		analyzers.add(new FrequencyAnalyzer());
		analyzers.add(new ReTweetCounter());
		analyzers.add(new TagTopicCosineSimilarityAnalyzer());
		analyzers.add(new TagTopicSubsumptionSimilarityCalculator());
		// Calling the pipeline to process.
		AnalyzerPipelineExecuter pipeline = new AnalyzerPipelineExecuter(analyzers);
		Set<String> tags = db.getTopTags(50);
		for (String tag: tags){
			if (tag.equalsIgnoreCase("#sandy"))
				continue;
			HashTagAnalytics tagAnalytics = pipeline.process(tag);
			db.insertTagAnalytics(tagAnalytics, "hurricaneSandy");
		}
	}



}
