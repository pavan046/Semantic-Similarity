package org.knoesis.wikipedia.similarity;

import java.util.Map;

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
	
	public static void calculate(){
		
	}

	public static void setDocOne(Map<String, Integer> docOne) {
		CosineSimilarityCalculator.docOne = docOne;
	}

		public static void setDocTwo(Map<String, Integer> docTwo) {
		CosineSimilarityCalculator.docTwo = docTwo;
	}
	
}
