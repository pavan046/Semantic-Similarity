package org.knoesis.tags.analysis;

import java.util.Map;

import org.knoesis.similarity.EntropyCalculator;

/**
 * Analyzes the consistency by calculating the entropy and therefore 
 * determining whether the hashtag is used to indicate a single topic
 * or is distributed in its usage.
 * 
 * TODO: A good analysis is comparing the results of that of dbpedia annotations
 * 		1. Annotate Entites with DBpedia
 * 		2. Mark all the entities related to a given topic using the wikilist generated
 * 		3. Calculate the entropy based on the topic to that of other entities mentioned
 * 		4. If there is a lot of deviation then the entropy value is high.
 * 
 * @author pavan
 *
 */
public class ConsistencyAnalyzer implements Analyzer{
	private Map<String, Integer> termFrequency;
	private EntropyCalculator entropyCalculator;
	double entropyOfHashtag;
	
	public ConsistencyAnalyzer(Map<String, Integer> termFrequency) {
		this.termFrequency = termFrequency;
		this.entropyCalculator = new EntropyCalculator(termFrequency);
	}
	@Override
	public void analyze() {
		entropyOfHashtag = entropyCalculator.calculate();
	}

}
