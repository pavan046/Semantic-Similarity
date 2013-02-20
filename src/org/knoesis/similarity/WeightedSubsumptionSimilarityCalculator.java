package org.knoesis.similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.knoesis.storage.TagAnalyticsDataStore;

/**
 * This class calculates the similarity using JackardCoefficient.
 * 
 * @author pavan
 *	
 * TODO: Pramod this is a good exercise for u to learn. Make all the similarity 
 * 		 measures as an interface. 
 */
public class WeightedSubsumptionSimilarityCalculator {
	
	/**
	 * This method returns the weighted .
	 * @param subset
	 * @param set
	 * @return
	 */
	public static Map<Integer, Double> calculate(Map<String, Double> subset, Map<String, Double> set){
		Map<Integer, Double> subsumptionSimilarities = new HashMap<Integer, Double>();
		double weightedSubsumption = 0;
		//Get the instersection of sets 
		Set<String> intersectionOfSets = new HashSet<String>(subset.keySet());
		intersectionOfSets.retainAll(set.keySet());
		
		double averageSetValues = 0.0d;
		for(String entity: set.keySet()){
			averageSetValues += set.get(entity);
		}
		averageSetValues = averageSetValues/set.keySet().size();
		
		//Multiply the weights of the intersection elements
		double intersectionEntitiesWeights = 0.0d;
		for(String entity: intersectionOfSets)
			intersectionEntitiesWeights += subset.get(entity)*set.get(entity);
		
		//Divide this by A-(A Intersection B)/Total number of elements of A
		Set<String> minus = new HashSet<String>(subset.keySet());
		minus.removeAll(intersectionOfSets);
		double minusWeightSum = 0.0d; 
		for(String entity: minus)
			minusWeightSum += subset.get(entity);
		//double minusWeightNormalized = minusWeightSum/minus.size();
		System.out.println("Intersection Size: "+intersectionOfSets.size());
		System.out.println("Minus Size: "+minus.size());
		System.out.println("Tagset Size: "+subset.size());
		//System.out.println("Minus:" + minusWeightSum);
		 
		subsumptionSimilarities.put(1, intersectionEntitiesWeights/ (intersectionEntitiesWeights + minusWeightSum*averageSetValues));
		subsumptionSimilarities.put(2, (double)intersectionOfSets.size()/minus.size());
		subsumptionSimilarities.put(3, (double)intersectionOfSets.size()/subset.size());
		//return weightedSubsumption;
		return subsumptionSimilarities;
	}
	
	public static double calculateWeightedSubsumtion(Map<String, Double> subset, Map<String, Double> set){
		Map<Integer, Double> results = WeightedSubsumptionSimilarityCalculator.calculate(subset, set);
		return results.get(1);
	}
	
		
	public static void main(String[] args) {
		TagAnalyticsDataStore ds = new TagAnalyticsDataStore();
		Set<String> tags = ds.getTagsfromTagAnalytics();
		Map<String, Double> wikiRelatedConcepts = ds.getRankedWikiArticles();
		//System.out.println(wikiRelatedConcepts);
		for(String tag: tags){
			System.out.println(tag);
			//tag="#akin";
			Map<String, Double> tagEntityFrequecy = ds.getEntitiesFrequencyForHashtags(tag);
			//System.out.println(tagEntityFrequecy);
			//Double weightedSusumption = calculate(tagEntityFrequecy, wikiRelatedConcepts);
			//ds.storeWeightedSubsumptionScore(tag, weightedSusumption);
		}
	}
	
}
