package org.knoesis.topic.triples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.knoesis.sparql.api.GetAllTriples;
import org.knoesis.sparql.api.Triple;
import org.knoesis.utils.EntityEncoder;
import org.knoesis.utils.Utils;
import org.knoesis.wikipedia.api.WikipediaParser;

/*
 * This class will give all the triple surrounding a particular topic
 */
public class GetAllTriplesForTopic {
	private WikipediaParser parser;
	private List<String> links;
	private String topic;
	
	private static final String GRAPHURI = "http://dbpedia.org/sparql/";
	
	/*
	 * Contructor
	 * This will set the topic and use wikipedia parser to get all the links in the wikipedia page
	 * of given topic.
	 * @params topic
	 */
	public GetAllTriplesForTopic(String topic){
		this.setTopic(topic);
		parser = new WikipediaParser(topic);
		setLinks(parser.getLinks());
	}
	
	/*
	 * This class will give you all the triples for the topic (which is set in the constructor)
	 */
	public List<Triple> getTriplesForTopic() throws InterruptedException, IOException{
		
		List<Triple> triple_for_topic = new ArrayList<Triple>();
		
		for(String link: links){
			// Excluding all the links starting with Category,Template and Wikipedia
			if(!isCategory(link) && !isTemplate(link) && !isWikipedia(link) && !hasNum(link)){
				
				// Making the link into DBpedia referenceable format.
				String encoded_link = EntityEncoder.encode(link);
				
				// String all Wikipedia concepts for topic into the file
				Utils.writeToFile("Wikipedia_concepts", "http:dbpedia.org/resource/"+encoded_link);
				
				System.out.println("The link is "+ encoded_link);
				
				// This will give you all the triples for the shortest path between the topic
				// and the given link.
				GetAllTriples get_triples = new GetAllTriples(topic, encoded_link, GRAPHURI);
				
				List<String> entities = get_triples.getAllConnectedEntities(topic, encoded_link);
				
				// Writing all entities to a file
				for(String entity: entities){
					Utils.writeToFile("Entities_after_srtst_path", entity);
				}
				
				List<Triple> triples_between_concepts = get_triples.getList_for_triples();
				if(triples_between_concepts!=null)
					for(Triple triple : triples_between_concepts){
						triple_for_topic.add(triple);
				}
				System.out.println("No of triples are " + triple_for_topic.size());
			}
		}
		
		return triple_for_topic;
	}
	
	/*
	 * Checking if it is of format Category:http...... 
	 */
	public boolean isCategory(String entity){
		return entity.matches("Category:(.*)");
	}
	
	/*
	 * Checking if it is a Template
	 */
	public boolean isTemplate(String entity){
		return entity.matches("Template:(.*)");
	}
	
	/*
	 * Checking if it is Wikipedia:...
	 */
	public boolean isWikipedia(String entity){
		return entity.matches("Wikipedia:(.*)");
	}
	
	public boolean hasNum(String entity){
		return entity.matches("(.*)2016");
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}
	

	
	public static void main(String[] args) throws InterruptedException, IOException {
		
//		FileWriter writer = new FileWriter(new File("Triples.n3"));
//		BufferedWriter buffer = new BufferedWriter(writer);
		GetAllTriplesForTopic topic_triples = new GetAllTriplesForTopic("United_States_presidential_election,_2012");
		List<Triple> triples = topic_triples.getTriplesForTopic();
		for(Triple triple :triples){
//			buffer.append(triple.toString());
//			buffer.append("\n");
			
			System.out.println(triple.toString());
		}
	}
	
}
