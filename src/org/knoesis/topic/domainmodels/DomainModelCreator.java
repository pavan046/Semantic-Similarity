package org.knoesis.topic.domainmodels;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.knoesis.api.DBpediaPathGenerator;
import org.knoesis.api.WikipediaParser;
import org.knoesis.models.Triple;
import org.knoesis.utils.EntityEncoder;
import org.knoesis.utils.Utils;

import com.hp.hpl.jena.sparql.function.library.date;

/**
 * 
 * @author pramod
 * @author pavan
 * 
 * The objective of this class is to create a domain model give a particular wikipedia concept in the 
 * following two steps
 * 
 * 1. Get related concepts from wikipedia 
 * 2. Generate the shortest path for each related concept from dbpedia. 
 *
 */
public class DomainModelCreator {
	private WikipediaParser parser;
	private String topic;
	private DBpediaPathGenerator pathGenerator;
	
	/**
	 * Contructor
	 * This will set the topic and use wikipedia parser to get all the links in the wikipedia page
	 * of given topic.
	 * @params topic
	 */
	public DomainModelCreator(String topic){
		this.setTopic(topic);
		parser = new WikipediaParser(topic);
		pathGenerator = new DBpediaPathGenerator();
	}

	/**
	 * This method as mentioned in the class is a three step process
	 * 
	 * 1. Gets related wikipedia concepts
	 * 2. For each concept get the shortest path from dbpedia if exists.
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public List<Triple> createModel() throws InterruptedException, IOException{
		List<Triple> triples = new ArrayList<Triple>();
		long count = 0;
		Set<String> links = parser.getLinks();
		for(String link: links){
			String conceptDbpedia = translateConcept(link);
			System.out.println("Dbpedia concept is-------" + conceptDbpedia);
			if(conceptDbpedia!= null){
				List<Triple> pathTriples = pathGenerator.generateTriples(topic, conceptDbpedia);
				
				//List<String> entities = pathGenerator.getAllConnectedEntities(topic, conceptDbpedia);
				
				// Count of number of triples.
				count = count + pathTriples.size();
				if(pathTriples != null){
					System.out.println("No of Triples: " + count);
					triples.addAll(pathTriples);
				}
			}
		}

		return triples;
	}

	/**
	 * The objective of this method is to check whether the wikipedia 
	 * article is a Concept, Template or any of other wikipedia resources
	 * and appropriately re
	 * @param concept
	 * @return translatedConcept
	 */
	public String translateConcept(String concept){
		String dbpediaEncodedEntity = null;
		/**
		 * FIXME: Discuss about what every property of the article is and then 
		 * 		  remove it
		 */
		if(isCategory(concept) || isTemplate(concept) || isWikipedia(concept) || hasNum(concept)){
			return null;
		}
		else{
			 dbpediaEncodedEntity = EntityEncoder.encode(concept);
		}
		return dbpediaEncodedEntity;
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
	
	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}
	
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		System.out.println(new Date() + " Starting the process");
		DomainModelCreator topic_triples = new DomainModelCreator("Hurricane_Sandy");
		List<Triple> triples = topic_triples.createModel();
		for(Triple triple :triples){
			//			buffer.append(triple.toString());
			//			buffer.append("\n");
			System.out.println(triple.toString());
		}
		System.out.println(new Date() + " Process Completed");
	}
	
	}
