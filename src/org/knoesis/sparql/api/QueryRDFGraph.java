package org.knoesis.sparql.api;


import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.resultset.ResultsFormat;

/*
 * This class queries the given RDF dataset with the given SPARQL query
 */
public class QueryRDFGraph {

	private String sparqlEndpoint;
	private String sparqlQuery;
	private com.hp.hpl.jena.query.ResultSet results;
	
	public QueryRDFGraph(String sparqlEndpoint, String sparqlQuery){
		this.sparqlEndpoint = sparqlEndpoint;
		this.sparqlQuery = sparqlQuery;
	}
	
	/*
	 * This method queries the graph using queryEngineHttp.
	 * @output ResultSet
	 */
	public com.hp.hpl.jena.query.ResultSet query(){
		QueryEngineHTTP httpengine = new QueryEngineHTTP(sparqlEndpoint,sparqlQuery);
		results = httpengine.execSelect();		
		return results;
	}
	
	public static void main(String[] args) {
		String graphURI = "http://dbpedia.org/sparql/";
		
		String query1 = "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
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
		"OPTION ( TRANSITIVE, T_DISTINCT, T_SHORTEST_ONLY,t_in(?x), t_out(?y), t_max(10), t_step('path_id') as ?path, t_step(?x) as ?route, t_step('step_no') AS ?jump ). \n"+
		"FILTER ( ?y = <http://dbpedia.org/resource/Mitt_Romney> && ?x = <http://dbpedia.org/resource/Barack_Obama> ) \n"+
		"}";
		
		String query2 = "PREFIX owl: <http://www.w3.org/2002/07/owl#> Select count(?s) where {?s ?p ?o.}";
		QueryRDFGraph querygraph = new QueryRDFGraph(graphURI, query1);
		
		
		// This method is used to format the output as per your requirements.
		ResultSetFormatter.output(querygraph.query(),ResultsFormat.FMT_RS_JSON);
	}
}
