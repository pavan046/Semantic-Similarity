package org.knoesis.twarql.extractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.knoesis.models.AnnotatedTweet;
/**
 * Pipeline class to call functors in sequence to extract entities and URLs from tweets.
 * 
 * @author Pavan
 * @author PabloMendes - modified to use all elements of the list of extractors, not just first.
 */
public class TweetProcessor {
	/**
	 * Method to run the information extraction process on tweets
	 * @param tweets
	 * @param path 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	List<Extractor> extractors = new ArrayList<Extractor>();
	
	public TweetProcessor(List<Extractor> extractors) {
		this.extractors = extractors;
	}
	
	public void addExtractor(Extractor extrinstance) {
		extractors.add(extrinstance);
	}

	//FIXME untested. think more about this.
	public List<AnnotatedTweet> process(Queue<AnnotatedTweet> buffer) {
		List<AnnotatedTweet> result = new ArrayList<AnnotatedTweet>();
		while(!buffer.isEmpty()){
			result.add(process(buffer.poll()));			
		}
		return result;
	}
	
	public List<AnnotatedTweet> process(List<AnnotatedTweet> tweets) {
		List<AnnotatedTweet> result = new ArrayList<AnnotatedTweet>();
		for(AnnotatedTweet tweet: tweets){
			result.add(process(tweet));			
		}
		return result;
	}

	/*
	 * 1.get the tweet content
	 * 2. Store it in the Annotated Tweet
	 * 
	 * 3.Pass it through all extractors. Examples are:
	 *     URLExtractor
	 *     Resolver
	 *                (Store URls in annotated Tweet)
	 *     Entity Extractor
	 *                (Store Entities in the Annotated Tweet)
	 */
	public AnnotatedTweet process(AnnotatedTweet annotatedTweet) {
		for (Extractor e: extractors) {
			e.process(annotatedTweet);
		}
		return annotatedTweet;
	}
	
}