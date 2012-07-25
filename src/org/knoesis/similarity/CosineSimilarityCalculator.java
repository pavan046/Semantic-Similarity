package org.knoesis.similarity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class calculates the cosine similarity of two sets of <String, values>.
 * 
 * Cosine Similarity(a,b) = dot(a) * dot(b)/|a| |b|
 * 
 * @author pavan
 *
 */
public class CosineSimilarityCalculator {
	private static Map<String, Integer> docOne, docTwo;
	
	public CosineSimilarityCalculator(Map<String, Integer> docOne, Map<String, Integer> docTwo) {
		this.setDocOne(docOne);
		this.setDocTwo(docTwo);
	}
	
	/**
	 * This method takes in two documents and gives you the 
	 * @param docOne
	 * @param docTwo
	 * @return
	 */
	public static double calculate(Map<String, Integer> docOne, Map<String, Integer> docTwo){
		
		double similarityValue = 0;
		double dotProduct =0;
		
		// Converting all the terms into a set. so that we can use retainAll to get the intersection
		// of two sets
		Set<String> docOneTerms = docOne.keySet();
		Set<String> docTwoTerms = docTwo.keySet();
		docOneTerms.retainAll(docTwoTerms);
		
		Iterator<String> docOneIterator = docOneTerms.iterator();
		
		while(docOneIterator.hasNext()){
			String term = docOneIterator.next();
			double valueForTermInDocOne = docOne.get(term);
			
			// Checking if the second vector has some value associated with that term.
			if(docTwo.get(term) != 0)
			{
				double valueForTermInDoctwo = docTwo.get(term);
				dotProduct = dotProduct + (valueForTermInDocOne * valueForTermInDoctwo);
			}				
		}			
		
		//System.out.println("The dot product is :" + dotProduct);
		
		// Getting the similarity value. i.e., dotProduct / |docOne| |docTwo|
		double lengthOfDocOne = getLength(docOne);
		double lengthOfDoctwo = getLength(docOne);
		double productOfLengths = lengthOfDocOne * lengthOfDoctwo;
		
		//System.out.println("The product of lengths is :" + productOfLengths);
		
		similarityValue = dotProduct / productOfLengths;
		
		return similarityValue;
	}
	
	/**
	 * This method gives you the length of document i.e., |document|
	 * @param doc
	 * @return
	 */
	public static double getLength(Map<String, Integer> doc){
		double length = 0;
		
		Iterator<Integer> mapValuesIterator = doc.values().iterator();
		while(mapValuesIterator.hasNext()){
			double value = mapValuesIterator.next();
			length = length + (value * value);
		}
		return Math.sqrt(length);
	}

	public static void setDocOne(Map<String, Integer> docOne) {
		CosineSimilarityCalculator.docOne = docOne;
	}

	public static void setDocTwo(Map<String, Integer> docTwo) {
		CosineSimilarityCalculator.docTwo = docTwo;
	}
	
	public static void main(String[] args) {
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		Map<String ,Integer> map2 = new HashMap<String, Integer>();
		
		map1.put("pramod", 3);
		map1.put("is", 4);
		map1.put("good", 5);
		
		map2.put("pramod", 3);
		map2.put("is", 4);
		map2.put("good1", 5);
		
		System.out.println(CosineSimilarityCalculator.calculate(map1, map2));
		
		
	}
		
}
