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
public class JakkardCoefficientSimilarityCalculator {
	private List<String> docOne, docTwo;
	
	public JakkardCoefficientSimilarityCalculator(List<String> docOne, List<String> docTwo) {
		this.setDocOne(docOne);
		this.setDocTwo(docTwo);
	}
	
	/**
	 * This method returns the jaccard coefficient of given two lists.
	 * @param docOne
	 * @param docTwo
	 * @return
	 */
	public static double calculate(List<String> docOne, List<String> docTwo){
		
		double jaccardCoeff = 0;
		
		Set<String> docOneSet = new HashSet<String>(docOne);
		Set<String> docTwoSet = new HashSet<String>(docTwo);
		
		Set<String> intersectionOfSets = intersection(docOneSet, docTwoSet);
		Set<String> unionOfSets = union(docOneSet, docTwoSet);
		
		jaccardCoeff = (double)intersectionOfSets.size() / unionOfSets.size();
		
		return jaccardCoeff;
	}
	
	/**
	 * This method gives you the intersection of two sets
	 * @param docOne
	 * @param docTwo
	 * @return
	 */
	public static Set<String> intersection(Set<String> set1,Set<String> set2){	
		Set<String> tmp = new HashSet<String>(set1);
		boolean flag = tmp.retainAll(set2);
		return tmp;
	}
	
	/**
	 * This method gives you the union of two sets
	 * @param docOne
	 * @param docTwo
	 * @return
	 */
	public static Set<String> union(Set<String> setA,Set<String> setB){
		Set<String> tmp = new HashSet<String>(setA);
		tmp.addAll(setB);
		return tmp;
	}

	public void setDocOne(List<String> docOne) {
		this.docOne = docOne;
	}

	public void setDocTwo(List<String> docTwo) {
		this.docTwo = docTwo;
	}
	
	public static void main(String[] args) {
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		
		list1.add("Pramod");
		list1.add("koneru");
		list1.add("asdd");
		
		list2.add("asdd");
		list2.add("Pramod");
		list2.add("asas");
		list2.add("aaas");
		
		JakkardCoefficientSimilarityCalculator test = new JakkardCoefficientSimilarityCalculator(list1, list2);
		System.out.println("The jaccard coeff is :" + test.calculate(list1, list2));
	}
	
}
