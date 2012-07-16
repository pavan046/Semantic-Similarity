package org.knoesis.wikipedia.similarity;

import java.util.List;

/**
 * This class calculates the similarity using JackardCoefficient.
 * 
 * @author pavan
 *	
 * TODO: Pramod this is a good exercise for u to learn. Make all the similarity 
 * 		 measures as an interface. 
 */
public class JackardCoefficientSimilarityCalculator {
	private List<String> docOne, docTwo;
	
	public JackardCoefficientSimilarityCalculator(List<String> docOne, List<String> docTwo) {
		this.setDocOne(docOne);
		this.setDocTwo(docTwo);
	}
	
	public void calculate(){
		
	}

	public void setDocOne(List<String> docOne) {
		this.docOne = docOne;
	}

	public void setDocTwo(List<String> docTwo) {
		this.docTwo = docTwo;
	}
	
	
}
