package org.knoesis.similarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class JaccardCoefficientSimilarityCalculator {
	
	public JaccardCoefficientSimilarityCalculator() {
	}
	
	/**
	 * This method returns the jaccard coefficient of given two lists.
	 * @param docOne
	 * @param docTwo
	 * @return
	 */
	public static double calculate(Set<String> docOne, Set<String> docTwo){
		
		double jaccardCoeff = 0;
		
		Set<String> intersectionOfSets = new HashSet<String>(docOne);
		intersectionOfSets.retainAll(docTwo);
		
		Set<String> unionOfSets = new HashSet<String>(docOne);
		unionOfSets.addAll(docTwo);
		
		jaccardCoeff = (double)intersectionOfSets.size() / unionOfSets.size();
		
		return jaccardCoeff;
	}
	
		
	public static void main(String[] args) {
		Set<String> list1 = new HashSet<String>();
		Set<String> list2 = new HashSet<String>();
		
		list1.add("Pramod");
		list1.add("koneru");
		list1.add("asdd");
		
		list2.add("asdd");
		list2.add("Pramod");
		list2.add("asas");
		list2.add("aaas");
		
		System.out.println("The jaccard coeff is :" + JaccardCoefficientSimilarityCalculator.calculate(list1, list2));
	}
	
}
