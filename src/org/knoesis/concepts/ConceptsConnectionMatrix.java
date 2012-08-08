package org.knoesis.concepts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

public class ConceptsConnectionMatrix {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConceptsConnectionMatrix test = new ConceptsConnectionMatrix();
//		System.out.println(test.getConnectionType("http://dbpedia.org/resource/RDFa", "http://dbpedia.org/resource/SPARQL"));
		List<String> entities = Arrays.asList(
				"http://dbpedia.org/resource/RDFa", 
				"http://dbpedia.org/resource/SPARQL", 
				"http://dbpedia.org/resource/Galway",
				"http://dbpedia.org/resource/Ireland",
				"http://dbpedia.org/resource/Digital_Enterprise_Research_Institute",
				"http://dbpedia.org/resource/Ferrari",
				"http://dbpedia.org/resource/Modena",
				"http://dbpedia.org/resource/Maranello",
				"http://dbpedia.org/resource/United_States");
		Map<String, Map<String,String>> matrix = new HashMap<String, Map<String,String>>();
		matrix = test.generateConnectionMatrix(entities);
		System.out.println(matrix.toString());
		
	}


	public Map<String, Map<String,String>> generateConnectionMatrix(List<String> entities){

		Map<String, Map<String,String>> matrix = new HashMap<String, Map<String,String>>();

		for (Iterator iterator1 = entities.iterator(); iterator1.hasNext();) {
			String conceptFrom = (String) iterator1.next();

			Map<String, String> innerMatrix = new HashMap<String,String>();
			
			for (Iterator iterator2 = entities.iterator(); iterator2.hasNext();) {
				String conceptTo = (String) iterator2.next();

				if (!conceptFrom.contentEquals(conceptTo)){
					int connectionType = this.getConnectionType(conceptFrom, conceptTo);
					switch (connectionType) {
					case 0:
					break;
					case 1:  innerMatrix.put(conceptTo, "1");
					break;
					case 2:  innerMatrix.put(conceptTo, "2");
					break;
					}
				}
			}
			matrix.put(conceptFrom, innerMatrix);
		}
		return matrix;
	}



	/**
	 * @param conceptFrom : the first DBpedia Resource (source)
	 * @param conceptTo : the second DBpedia Resource (destination)
	 * 
	 * returns: 
	 * "0" for no connection between the 2 concepts
	 * "1" for direct connection 
	 * "2" for 2 hops connection 
	 * 
	 */
	public int getConnectionType(String conceptFrom, String conceptTo){

		String sparqlQueryString1= "" +
				"PREFIX dbont: <http://dbpedia.org/ontology/> "+
				"PREFIX dbp: <http://dbpedia.org/property/>"+
				"   SELECT DISTINCT ?p1" +
				"   WHERE {  "+
				"   	<"+conceptFrom+"> ?p1 <"+conceptTo+"> . "+
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
					"   	<"+conceptFrom+"> ?p1 ?o . "+
					"		?o ?p2 <"+conceptTo+"> ." +
					"   }";

			query = QueryFactory.create(sparqlQueryString2);
			qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

			results = qexec.execSelect();
			//			ResultSetFormatter.out(System.out, results, query);  

			if (results.hasNext()){
				qexec.close() ;
				System.out.println("2 Hops Connection!");
				return 2;
			}else{
				qexec.close() ;
				System.out.println("No Connection...");
				return 0;
			}
		}
		else{
			qexec.close() ;
			System.out.println("Direct connection!");
			return 1;
		}

	}
}
