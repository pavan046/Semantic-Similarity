package org.knoesis.tags.analysis;

import java.util.Map;

import org.knoesis.models.HashTagAnalytics;

/**
 * The classes implementing this interface analyzes the hashtags 
 * either for consideration or for deletion from crawling. 
 * 
 *  Example implementations
 *  1. Frequency Analyzer
 *  2. Consistency Analyzer etc. 
 *  
 * @author pavan
 *
 */
public interface Analyzer {
	/**
	 * Analyses and sets the appropriate variable in 
	 * HashTagAnalytics Model.
	 */
	public void analyze(HashTagAnalytics hashTag);

}
