package org.knoesis.similarity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
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
 * 
 * FIXME: THis code has a pretty big BUG.. Need to check what is wrong and recode
 * 		 I will do it tomorrow
 *
 */
public class CosineSimilarityCalculator {
	
	/**
	 * This method takes in two documents and gives you the 
	 * @param docOne
	 * @param docTwo
	 * @return
	 */
	public static double calculate(Map<String, Double> v1, Map<String, Double> v2){
		
		 Set<String> both = new HashSet<String>(v1.keySet());
         both.retainAll(v2.keySet());
         double sclar = 0, norm1 = 0, norm2 = 0;
         for (String k : both) sclar += v1.get(k) * v2.get(k);
         for (String k : v1.keySet()) norm1 += v1.get(k) * v1.get(k);
         for (String k : v2.keySet()) norm2 += v2.get(k) * v2.get(k);
         return sclar / Math.sqrt(norm1 * norm2);		
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
		
		
		
	}
		
}
