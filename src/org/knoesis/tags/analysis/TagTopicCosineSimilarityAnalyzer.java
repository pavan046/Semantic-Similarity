package org.knoesis.tags.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.HashTagAnalytics;
import org.knoesis.similarity.CosineSimilarityCalculator;
import org.knoesis.storage.TagAnalyticsDataStore;
import org.knoesis.utils.Utils;

/**
 * Updates the cosine similarity of the hashtag to that of a particular topic 
 * 
 * For now the experiments are done only for the USElections 
 * @author pavan
 *
 */
public class TagTopicCosineSimilarityAnalyzer implements Analyzer {
	private TagAnalyticsDataStore db = new TagAnalyticsDataStore();
	private Map<String, Double> tweetEntitiesFrequecy, wikiEntitiesSimilarityRanking;
	
	public void initializeData(HashTagAnalytics hashTag){
		wikiEntitiesSimilarityRanking = db.getRankedWikiArticles();
		tweetEntitiesFrequecy = hashTag.getEntityFrequency();
	}
	
	@Override
	public void analyze(HashTagAnalytics hashTag) {
		this.initializeData(hashTag);
		//System.out.println(tweetEntitiesFrequecy);
		//System.out.println(wikiEntitiesSimilarityRanking);
		hashTag.setTopicCosineSimilarity(CosineSimilarityCalculator.calculate(
				tweetEntitiesFrequecy, wikiEntitiesSimilarityRanking));
	}

}
