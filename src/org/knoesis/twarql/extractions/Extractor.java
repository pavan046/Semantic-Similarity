package org.knoesis.twarql.extractions;


import java.io.Serializable;
import java.util.Set;

import org.knoesis.models.AnnotatedTweet;

/**
 * This is the most generic interface for any information extraction step.
 * 
 * A few subinterfaces are:
 * 	NamedEntityExtrator
 * 
 * A few example implementors are:
 * 	TrieExtractor
 *  URLExtractor
 *  
 * @author PabloMendes
 * @param <E>
 * 
 * TODO: Some of the extractors that implements this returns only a String not the Set. Need to look 
 * into how I can change it make it more generic.
 */
public interface Extractor<E> extends Serializable{

	/**
	 * Performs information extraction on some raw text.
	 * @param text - input with raw text
	 * @return annotations - extracted information from raw input
	 */
	public E extract(Object text); //TODO This interface needs a refactor. Too specific for entities (Set<String>). What about sentiments?

	/**
	 * Updates the annotated tweets with the extracted information from the tweet text
	 * @param tweet
	 */
	public void process(AnnotatedTweet tweet);

}
