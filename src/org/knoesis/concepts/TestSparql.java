package org.knoesis.concepts;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class TestSparql {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		String concept1 = new String("http://dbpedia.org/resource/RDFa");
		String concept2 = new String("http://dbpedia.org/resource/SPARQL");

		//		String sparqlQueryString1= "" +
		//				"PREFIX dbont: <http://dbpedia.org/ontology/> "+
		//				"PREFIX dbp: <http://dbpedia.org/property/>"+
		//				"   SELECT DISTINCT ?p1 ?p2" +
		//				"   WHERE {  "+
		//				"   	{<http://dbpedia.org/resource/Ferrari> ?p1 <http://dbpedia.org/resource/United_States> . }"+
		//				"		UNION" +
		//				"		{<http://dbpedia.org/resource/United_States> ?p2 <http://dbpedia.org/resource/Ferrari> . }" +
		//				"   }";

		String sparqlQueryString1= "" +
				"PREFIX dbont: <http://dbpedia.org/ontology/> "+
				"PREFIX dbp: <http://dbpedia.org/property/>"+
				"   SELECT DISTINCT ?p1" +
				"   WHERE {  "+
				"   	<"+concept1+"> ?p1 <"+concept2+"> . "+
				"   }";

		Query query = QueryFactory.create(sparqlQueryString1);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

		ResultSet results = qexec.execSelect();
//		ResultSetFormatter.out(System.out, results, query);       

		if(!results.hasNext()){

			System.out.println("No direct connection... checking 2hops connection...");
			
			String sparqlQueryString2= "" +
					"PREFIX dbont: <http://dbpedia.org/ontology/> "+
					"PREFIX dbp: <http://dbpedia.org/property/>"+
					"   SELECT DISTINCT ?o " +
					"   WHERE {  "+
					"   	<"+concept1+"> ?p1 ?o . "+
					"		?o ?p2 <"+concept2+"> ." +
					"   }";

			query = QueryFactory.create(sparqlQueryString2);
			qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

			results = qexec.execSelect();
//			ResultSetFormatter.out(System.out, results, query);  
			
			if (results.hasNext()){
				System.out.println("2 Hops Connection!");
			}else{
				System.out.println("No Connection...");
			}
		}
		else{
			System.out.println("Direct connection!");
		}
		qexec.close() ;

	}

}
