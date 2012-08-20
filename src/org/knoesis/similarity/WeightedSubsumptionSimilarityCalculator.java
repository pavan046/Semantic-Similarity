package org.knoesis.similarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
	public static double calculate(Map<String, Double> subset, Map<String, Double> set){
		
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
		Set<String> minus = subset.keySet();
		minus.removeAll(intersectionOfSets);
		double minusWeightSum = 0.0d; 
		for(String entity: minus)
			minusWeightSum += subset.get(entity);
		//double minusWeightNormalized = minusWeightSum/minus.size();
		
		weightedSubsumption = 
				intersectionEntitiesWeights/(intersectionEntitiesWeights + minusWeightSum*averageSetValues);
		
		return weightedSubsumption;
	}
	
		
	public static void main(String[] args) {
		
	}
	
}
