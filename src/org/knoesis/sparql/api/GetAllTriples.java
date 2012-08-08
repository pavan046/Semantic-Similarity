		package org.knoesis.sparql.api;

import java.util.ArrayList;
import java.util.List;

import org.knoesis.utils.Utils;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.resultset.ResultSetException;

/*
 * This class will give you all the triples between two concepts, connected by shortest path.
 * Process: First we get the shortest route (route with all the concepts) between two 
 *          concepts and then we find predicates between two consecutive concepts in the route 
 *          and make a triple to store them into RDF Graph
 */
public class GetAllTriples {
	
	private String concept1;
	private String concept2;
	private String graphURI;
	
	private ResultSet shortst_path_results;
	private ResultSet predicates_results;
	private List<Triple> list_for_triples;
	
	public GetAllTriples(String concept1,String concept2,String graphURI) throws InterruptedException{
		this.setConcept1(concept1);
		this.setConcept2(concept2);
		this.setGraphURI(graphURI);	
		getShortstPath();
		getAllTriples();
	}
	
	
	/*
	 * Method to construct the sparql, to find the shortest route between two concepts.
	 * It used transitive closure.
	 */
	public String createSparqlQueryForShrtstPath(String concept1,String concept2){
		String sparql_query = "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
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
		"FILTER ( ?y = <http://dbpedia.org/resource/"+concept2+"> && ?x = <http://dbpedia.org/resource/"+concept1+"> ) \n"+
		"}";
		
		return sparql_query;
	}
	
	/*
	 * This method is used to construct sparql query for getting predicates between two concepts
	 * @params concept1 and concept2
	 * @output sparql query string.
	 */
	public String createSparqlQueryToGetPred(String concept1,String concept2){
		
		Boolean isaURI = false;
		String sparql_query;
		if(concept1.matches("(.*)http://dbpedia.org/resource(.*)"))
			isaURI = true;
		
		if(isaURI){
			sparql_query = "SELECT ?predicate WHERE \n" +
			  "{<" + concept1+">"+" ?predicate "+ "<"+concept2 +">"+". \n" +
			  "}";
		}else{
			String concept1URI = "<http://dbpedia.org/resource/" + concept1 + ">";
			String concept2URI = "<http://dbpedia.org/resource/" + concept2 + ">";
			sparql_query = "SELECT ?predicate WHERE \n" +
								  "{" + concept1URI+" ?predicate "+ concept2URI +". \n" +
								  "}";
		}
		
		//System.out.println(sparql_query);
		return sparql_query;
	}
	
	/*
	 * This methods gives the shortest route between two concepts.
	 */
	public void getShortstPath() throws InterruptedException{
		String sparql_query = createSparqlQueryForShrtstPath(concept1, concept2);
		QueryRDFGraph query = new QueryRDFGraph(graphURI, sparql_query);
		try {
			this.shortst_path_results = query.query();
		} catch (ResultSetException e) {
			// TODO Auto-generated catch block
			Utils.sleep(7);
			this.shortst_path_results = query.query();
			//e.printStackTrace();
		}
		//return results;
	}
	
	/*
	 * Over-loaded method of the above method.
	 */
	public ResultSet getShortestPath(String concept1, String concept2) throws InterruptedException{
		String sparql_query = createSparqlQueryForShrtstPath(concept1, concept2);
		ResultSet results;
		QueryRDFGraph query = new QueryRDFGraph(graphURI, sparql_query);
		try {
			results = query.query();
		} catch (ResultSetException e) {
			// TODO Auto-generated catch block
			Utils.sleep(7);
			results = query.query();
			//e.printStackTrace();
		}
		
		return results;
	}
	/*
	 * This method is used to get the predicates between two concepts.
	 * @params concepts (raw, not URI's)
	 */
	public ResultSet getPredicates(String concept1,String concept2) throws InterruptedException{
		String sparql_query = createSparqlQueryToGetPred(concept1, concept2);
		QueryRDFGraph query2 = new QueryRDFGraph(graphURI, sparql_query);
		ResultSet results = null;
		try {
			results = (ResultSet)query2.query();
		} catch (ResultSetException e) {
			// TODO Auto-generated catch block
			// Make the system sleep for a couple of secs and then query again... 
			Utils.sleep(20);
			results = (ResultSet)query2.query();
			//e.printStackTrace();
		}
		return results;
	}
	
	/*
	 * This method gives all the entities which are in between concept1 and concept2, in the 
	 * shortest path. If there are three paths of same length it gives the entities in all the 
	 * paths.
	 * 
	 * Hardcoded for dbpedia.
	 */
	public List<String> getAllConnectedEntities(String concept1, String concept2) throws InterruptedException{
		
		ResultSet results1 = getShortestPath(concept1, concept2);
		List<String> entities_list = new ArrayList<String>();
		String concept1URI = "http://dbpedia.org/resource/" + getConcept1();
		String concept2URI = "http://dbpedia.org/resource/" + getConcept2();
		
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
	
	/*
	 * This method is used to get all the triples between the concepts based on the shortest path.
	 */
	public void getAllTriples() throws InterruptedException{
		ResultSet results2 = getShortst_path_results();
		list_for_triples = new ArrayList<Triple>();
		List<String> allEntities = new ArrayList<String>();
		if(results2 != null)
			while(results2.hasNext()){
				String entity = results2.next().getResource("?route").toString();
				allEntities.add(entity);
			}
		
		//System.out.println("The entities are" + allEntities);
		
		for(int i = 0 ;i< allEntities.size()-1;i++ ){
			String sub = "<"+allEntities.get(i)+">";
			String obj = "<"+allEntities.get(i+1)+">";
			ResultSet predicates = getPredicates(allEntities.get(i), allEntities.get(i+1));
			while(predicates.hasNext()){
				String predicate ="<"+ predicates.next().getResource("?predicate").toString() +">";
				list_for_triples.add(new Triple(sub,predicate,obj));
			}
		}	
		
		
		//return list_for_triples;
	}
	
	/*
	 * Over-loaded method for the above.
	 */
	public List<Triple> getAllTriples(String concept1,String concept2) throws InterruptedException{
		ResultSet results2 = getShortestPath(concept1,concept2);
		List<Triple> triples = new ArrayList<Triple>();
		List<String> allEntities = new ArrayList<String>();
		if(results2 != null)
			while(results2.hasNext()){
				String entity = results2.next().getResource("?route").toString();
				allEntities.add(entity);
			}
		
		//System.out.println("The entities are" + allEntities);
		
		for(int i = 0 ;i< allEntities.size()-1;i++ ){
			String sub = "<"+allEntities.get(i)+">";
			String obj = "<"+allEntities.get(i+1)+">";
			ResultSet predicates = getPredicates(allEntities.get(i), allEntities.get(i+1));
			while(predicates.hasNext()){
				String predicate ="<"+ predicates.next().getResource("?predicate").toString() +">";
				triples.add(new Triple(sub,predicate,obj));
			}
		}			
		
		return triples;
	}
	
	public static void main(String[] args) throws InterruptedException {
		String graphURI = "http://dbpedia.org/sparql/";
		String concept2 = "United_States_presidential_election,_2008";
		String concept1 = "Mitt_Romney";
		GetAllTriples getshortestroute = new GetAllTriples( concept1,concept2, graphURI);
		
		//getshortestroute.getShortstPath();fa
		List<Triple> triples = getshortestroute.getList_for_triples();	
		
		List<String> entities = getshortestroute.getAllConnectedEntities(concept1,concept2);
		
		System.out.println("The entities "+ entities);
		
		for(Triple triple :triples){
			System.out.println(triple.toString());
		}

	}

	
	// Setter and getter methods
	public void setConcept1(String concept1) {
		this.concept1 = concept1;
	}

	public String getConcept1() {
		return concept1;
	}

	public void setConcept2(String concept2) {
		this.concept2 = concept2;
	}

	public String getConcept2() {
		return concept2;
	}

	public void setGraphURI(String graphURI) {
		this.graphURI = graphURI;
	}

	public String getGraphURI() {
		return graphURI;
	}	
	

	public void setShortst_path_results(ResultSet shortst_path_results) {
		this.shortst_path_results = shortst_path_results;
	}

	public ResultSet getShortst_path_results() {
		return shortst_path_results;
	}

	public void setPredicates_results(ResultSet predicates_results) {
		this.predicates_results = predicates_results;
	}

	public ResultSet getPredicates_results() {
		return predicates_results;
	}

	public void setList_for_triples(List<Triple> list_for_triples) {
		this.list_for_triples = list_for_triples;
	}

	public List<Triple> getList_for_triples() {
		return list_for_triples;
	}

}
