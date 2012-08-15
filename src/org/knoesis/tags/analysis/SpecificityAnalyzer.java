package org.knoesis.tags.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.HashTagAnalytics;
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
	
	@Override
	public void analyze(HashTagAnalytics hashTag) {
		System.out.println(hashTag.getTermFrequencyOfHashTag());
		System.out.println(hashTag.getTermFrequencyOfKeyword());
		hashTag.setSpecificityMeasure(CosineSimilarityCalculator.calculate(
				hashTag.getTermFrequencyOfHashTag(), hashTag.getTermFrequencyOfKeyword()));
	}

	public static void main(String[] args) {
		List<Extractor> extractors = new ArrayList<Extractor>();
		extractors.add(new TagExtractor());
		HashTagAnalytics hashTag = new HashTagAnalytics("#obama");
		SpecificityAnalyzer specificity = new SpecificityAnalyzer();
		specificity.analyze(hashTag);
	}
}
