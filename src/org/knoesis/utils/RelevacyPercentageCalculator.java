package org.knoesis.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.knoesis.storage.TagAnalyticsDataStore;


public class RelevacyPercentageCalculator {
	public static void main(String args[]){
		TagAnalyticsDataStore ds = new TagAnalyticsDataStore();
		Map<String, Map<Boolean, Integer>> resultsMap = new HashMap<String, Map<Boolean,Integer>>(); 
		ResultSet results = ds.getRelevancyManualEvalated();
		try {
			while(results.next()){
				String tag = results.getString("hashtag");
				Map<Boolean, Integer> tagResults;
				if(resultsMap.keySet().contains(tag))
					tagResults = resultsMap.get(tag);
				else{
					tagResults = new HashMap<Boolean, Integer>();
					tagResults.put(true, 0);
					tagResults.put(false, 0);
				}
				if(results.getInt("relevant")==1)
					tagResults.put(true, results.getInt("count"));
				else 
					tagResults.put(false, results.getInt("count"));
				resultsMap.put(tag, tagResults);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ds.storeRelevancyPercentage(calculateRelevancyPercentage(resultsMap));
	}
	
	public static Map<String, Double> calculateRelevancyPercentage(Map<String, Map<Boolean, Integer>> tagRelevancy){
		Map<String, Double> results = new HashMap<String, Double>();
		for(String tag: tagRelevancy.keySet()){
			Map<Boolean, Integer> tagResults = tagRelevancy.get(tag);
			double total = tagResults.get(true) + tagResults.get(false);
			results.put(tag, tagResults.get(true)/total);
		}
		return results;
	}
}
