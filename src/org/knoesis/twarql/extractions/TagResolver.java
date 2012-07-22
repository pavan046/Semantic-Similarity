package org.knoesis.twarql.extractions;

import java.util.Set;

import org.knoesis.twarql.extractions.Extractor;
import org.knoesis.models.AnnotatedTweet;

/**
 * Extract tags from tweets, then ask some service for the definition.
 * Pick the top K. Maybe K=1, maybe K=2... think about it.
 * 
 * This is a gift for Pavan to implement. :)
 * Very easy:
 *   http://api.tagdef.com/
 *   http://blog.tagal.us/api-documentation/
 * 
 * @author PabloMendes
 *
 */
public class TagResolver implements Extractor {

	TagExtractor extractor = new TagExtractor();
	@Override
	public Set<String> extract(Object text) {
		Set<String> tags = extractor.extract((String)text);
		return resolve(tags);
	}

	@Override
	public void process(AnnotatedTweet tweet) {
		// TODO Auto-generated method stub
		
	}

	private Set<String> resolve(Set<String> tags) {
		// TODO Gift-generated method stub :)
		return null;
	}
}
