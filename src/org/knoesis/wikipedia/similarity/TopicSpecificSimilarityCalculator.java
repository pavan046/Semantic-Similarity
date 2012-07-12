package org.knoesis.wikipedia.similarity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.knoesis.wikipedia.api.WikipediaParser;
import org.openjena.atlas.logging.Log;

/**
 * This class generates a list of entities with weights 
 * that are related to a particular topic. The entities or the 
 * concepts are taken from wikipedia that can later be 
 * transformed to DBPedia URIs.
 * 
 * TODO: For now the topic calculator takes only 2 hops into consideration
 * 		 Later this has to be changed to provide for more hops. 
 * 		
 * TODO: Also the scalability should be considered. Transform the problem to Map Reduce.
 * 
 * FIXME: Use logging everytime you code.
 * @author pavan
 *
 */
public class TopicSpecificSimilarityCalculator {
	private static String wikipediaTopic;
	private static int hops;
	private WikipediaParser parser;
	private static Logger logger = Logger.getLogger(TopicSpecificSimilarityCalculator.class);

	/**
	 *  
	 * @param wikipediaTopic
	 * @param hops
	 */
	public TopicSpecificSimilarityCalculator(String wikipediaTopic, int hops) {
		this.wikipediaTopic = wikipediaTopic;
		this.hops = hops;
		parser = new WikipediaParser(wikipediaTopic);
	}

	/**
	 * 
	 * @param wikipediaTopic
	 */
	public TopicSpecificSimilarityCalculator(String wikipediaTopic) {
		this.wikipediaTopic = wikipediaTopic;
		this.hops = 2;
		parser = new WikipediaParser(wikipediaTopic);
	}
	/**
	 * FIXME: In this method, the articles retreived can be 
	 * 		 1. Categories -- Ignore
	 * 		 2. Wikipedia  -- Not sure what this is
	 * 		 3. Templates -- Remove Template to compare.
	 * 		 4. Find more
	 * @return
	 */
	public Map<String, Integer> calculate(){
		List<String> firstLinks = parser.getLinks();
		Map<String, List<String>> secondLinks = new HashMap<String, List<String>>();

		for(String link: firstLinks){
			// FIXME: Find a better way to do this, as of now just replacing Templates 
			if(!link.contains("Category") || !link.contains("Wikipedia")){
				logger.info("processing "+link+" article");
				String linkWithoutTemplates = link.replace("Template:", "");
				parser.setWikipediaPage(linkWithoutTemplates);
				secondLinks.put(linkWithoutTemplates, parser.getLinks());
			}
		}
		//System.out.println(secondLinks);

		return calculateWeights(secondLinks);
	}

	private Map<String, Integer> calculateWeights(Map<String, List<String>> secondLinks){
		Map<String, Integer> results = new HashMap<String, Integer>();
		for(String firstLink: secondLinks.keySet()){
			List<String> links = secondLinks.get(firstLink);
			if(links!=null)
				if(links.contains(wikipediaTopic))
					results.put(firstLink, 3);
				else
					results.put(firstLink, 2);
		}
		for(String firstLink: secondLinks.keySet()){
			List<String> links = secondLinks.get(firstLink);
			if(links!=null)
				for(String secondLink: links){
					if(!results.keySet().contains(secondLink))
						results.put(secondLink, 1);
				}
		}
		return results;
	}

	public static void main(String[] args) {
		logger.info("Starting the process for US Elections 2012 " + System.currentTimeMillis());
				
		try {
			Writer write = new FileWriter(new File("obamacare_links.ranking_28th"));
			//United_States_presidential_election,_2012
			TopicSpecificSimilarityCalculator wikiCalc = new TopicSpecificSimilarityCalculator("Patient Protection and Affordable Care Act");
			Map<String, Integer> relatedLinks = wikiCalc.calculate();
			for(String link: relatedLinks.keySet()){
				write.append(link+"\t"+relatedLinks.get(link)+"\n");
			}
			write.close();
		logger.info("Ending the process for US Elections 2012 " + System.currentTimeMillis());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
