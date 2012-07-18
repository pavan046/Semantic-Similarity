package org.knoesis.api;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.knoesis.models.Triple;
import org.knoesis.test.QueryRDFGraph;
import org.knoesis.utils.Utils;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.resultset.ResultSetException;

/*
  */
/**
 * This class will give you all the triples between two concepts, connected by shortest path.
 * Process: First we get the shortest route (route with all the concepts) between two 
 *          concepts and then we find predicates between two consecutive concepts in the route 
 *          and make a triple to store them into RDF Graph 
 * @author pramod
 * @author pavan - refactored class
 * 
 * TODO: This class can be a singleton.
 *
 */
public class DBpediaPathGenerator {
	
	//TODO: Move this to a constatns file or a configuration file
	private final String graphURI = "http://dbpedia.org/sparql/";
	//TODO: Please let me know why are these global variables
	private static QueryEngineHTTP queryEngine; 
	
	/**
	 * This function executes the query on dbpedia sparql endpoing 
	 * and returns the ResultSet of the query
	 * @param query
	 * @return
	 */
	public static ResultSet execDBpediaQuery (String query)throws ResultSetException{
		queryEngine = new QueryEngineHTTP("http://dbpedia.org/sparql",query);
		ResultSet results = queryEngine.execSelect();
		return results;
	}
	
	/**
	 * This method queries dbdpedia/any other sparqlendpoint to get the 
	 * shortest path between the provided two concepts.
	 * @param conceptOne
	 * @param conceptTwo
	 * @return
	 */
	public ResultSet getShortestPath(String conceptOne, String conceptTwo) {
		String queryString = createSparqlQueryForShrtstPath(conceptOne, conceptTwo);
		ResultSet shortestPathResults;		
		
		try {
			shortestPathResults = this.execDBpediaQuery(queryString);
		} catch (ResultSetException e) {
			Utils.sleep(7);
			shortestPathResults = this.execDBpediaQuery(queryString);
			//e.printStackTrace();
		}
		return shortestPathResults;
	}
	
	/**
	 * TODO: I think the below two functions can be done using a single query
	 * 		 Please look into it. 
	 */
	
	/**
	 * Constructs the SPARQL Query to fetch the path with other concepts  from dbpedia for 
	 * two given concepts.
	 * @param conceptOne
	 * @param conceptTwo
	 * @return
	 */
	public String createSparqlQueryForShrtstPath(String conceptOne, String conceptTwo){
		String sparqlQuery = "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		"PREFIX foaf: <http://xmlns.com/foaf/0.1/>"+
		"PREFIX dc: <http://purl.org/dc/elements/1.1/>"+
		"PREFIX : <http://dbpedia.org/resource/>"+
		"PREFIX dbpedia2: <http://dbpedia.org/property/>"+
		"PREFIX dbpedia: <http://dbpedia.org/>"+
		"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n" +
		"SELECT ?route ?y ?jump WHERE \n" +
		"{ \n"+
		"{ \n" +
		"SELECT ?x ?y WHERE \n" + 
		"{ ?x foaf:page ?xpage ; ?predicate ?y . filter( isURI(?y) ) } \n" +
		"} \n" +
		"OPTION ( TRANSITIVE, T_DISTINCT, T_SHORTEST_ONLY,t_in(?x), t_out(?y), t_max(8), t_step('path_id') as ?path, t_step(?x) as ?route, t_step('step_no') AS ?jump ). \n"+
		"FILTER ( ?y = <http://dbpedia.org/resource/"+conceptTwo+"> && ?x = <http://dbpedia.org/resource/"+conceptOne+"> ) \n"+
		"}";
		
		return sparqlQuery;
	}
	
	/**
	 * This method gets the predicate between two concepts connected 
	 * by one edge. The dataset/sparqlendpoint used is dbpedia 
	 * @param conceptOne
	 * @param conceptTwo
	 * @return
	 * 
	 */
	public ResultSet getPredicate(String conceptOne,String conceptTwo) {
		String queryString = createSparqlQueryToGetProperties(conceptOne, conceptTwo);
		ResultSet results = null;
		try {
			queryEngine = new QueryEngineHTTP("http://dbpedia.org/sparql",queryString);
			results = queryEngine.execSelect();
		} catch (ResultSetException e) {
			// TODO Auto-generated catch block
			// Make the system sleep for a couple of secs and then query again... 
			Utils.sleep(20);
			results = (ResultSet)queryEngine.execSelect();
			//e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * This method is used to construct sparql query for getting predicates between two concepts
	 * @params concept1 and concept2
	 * @output sparql query string.
	 */
	public String createSparqlQueryToGetProperties(String conceptOne, String conceptTwo){
		
		// Here isaURI is used to check if the given concept is of the form http:dbpedia.org/../conceptOne
		// or just the conceptOne
		Boolean isaURI = false;
		String sparqlQuery;
		//FIXME: Not the way to do things. Make sure u check this using URL class.
		if(conceptOne.matches("(.*)http://dbpedia.org/resource(.*)"))
			isaURI = true;
		
		if(isaURI){
			sparqlQuery = "SELECT ?predicate WHERE \n" +
			  "{<" + conceptOne+">"+" ?predicate "+ "<"+conceptTwo +">"+". \n" +
			  "}";
		}else{
			String conceptOneURI = "<http://dbpedia.org/resource/" + conceptOne + ">";
			String conceptTwoURI = "<http://dbpedia.org/resource/" + conceptTwo + ">";
			sparqlQuery = "SELECT ?predicate WHERE \n" +
								  "{" + conceptOneURI+" ?predicate "+ conceptTwoURI +". \n" +
								  "}";
		}		
		return sparqlQuery;
	}
	
	
	
	/**
	 * Generates Triples that form the <s, p, o> path from 
	 * conceptOne to conceptTwo.
	 * 
	 * @param conceptOne
	 * @param conceptTwo
	 * @return
	 */
	public List<Triple> generateTriples(String conceptOne, String conceptTwo){
		ResultSet path = getShortestPath(conceptOne, conceptTwo);
		List<Triple> triples = new ArrayList<Triple>();
		List<String> allEntities = new ArrayList<String>();
		if(path != null)
			while(path.hasNext()){
				String entity = path.next().getResource("?route").toString();
				allEntities.add(entity);
			}
				
		for(int i = 0 ;i< allEntities.size()-1;i++ ){
			String sub = "<"+allEntities.get(i)+">";
			String obj = "<"+allEntities.get(i+1)+">";
						
			ResultSet predicates = getPredicate(allEntities.get(i), allEntities.get(i+1));
			while(predicates.hasNext()){
				String predicate ="<"+ predicates.next().getResource("?predicate").toString() +">";
				triples.add(new Triple(sub,predicate,obj));
			}
		}			
		
		return triples;
	}
	
	/**
	 * This is just a test method for generating the entities between 
	 * two entities -- All the entities that form the parth from entityA to entityB
	 * .
	 */
	public List<String> getAllConnectedEntities(String conceptOne, String conceptTwo){
		
		ResultSet results1 = getShortestPath(conceptOne, conceptTwo);
		List<String> entities_list = new ArrayList<String>();
		String concept1URI = "http://dbpedia.org/resource/" + conceptOne;
		String concept2URI = "http://dbpedia.org/resource/" + conceptTwo;
		
		//System.out.println(concept1URI +"--"+ concept2URI);

		if(results1 != null)
			while(results1.hasNext()){
				String entity = results1.next().getResource("?route").toString();
				if(concept1URI.equals(entity) || concept2URI.equals(entity))
					continue;
				else{
					//System.out.println(entity);
					entities_list.add(entity);}	
			}
		
		entities_list.add(concept2URI);
		return entities_list;
	}
	
	public static void main(String[] args) {
		String graphURI = "http://dbpedia.org/sparql/";
		String conceptOne = "United_States_presidential_election,_2012";
		String conceptTwo = "United_States_presidential_election,_2008";
		DBpediaPathGenerator pathGenerator = new DBpediaPathGenerator();
		//List<Triple> triples = pathGenerator.generateTriples(conceptOne, conceptTwo);
		List<Triple> triples = pathGenerator.generateTriples(conceptOne, conceptTwo);
		//ResultSet results = pathGenerator.getShortestPath(conceptOne, conceptTwo);
		//ResultSet results = pathGenerator.getPredicate(conceptOne, conceptTwo);
		System.out.println(triples.toString());
		
		//ResultSetFormatter.outputAsTSV(results);
		
		
	}

}
