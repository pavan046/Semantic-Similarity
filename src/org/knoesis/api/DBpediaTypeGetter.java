package org.knoesis.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.ResultSet;

/**
 * This class transforms a wikipedia entity to a dbpedia entity 
 * Further queries the dbpedia sparql endpoint to get the type of 
 * the entity.
 * 
 * @author pavan
 *
 */
public class DBpediaTypeGetter {
	/**
	 * This method returns all the types of the wikiEntity from dbpedia.
	 * 
	 * @param wikiEntity
	 * @return
	 */
	public static List<String> getType(String wikiEntity){
		String dbpediaEntity = escapeSpecialCharSparql(wikiEntity);
		String sparqlQuery = "SELECT ?p WHERE { <http://dbpedia.org/resource/"+dbpediaEntity+"> a ?p .}";
		List<String> types = new ArrayList<String>();
		ResultSet results = DBpediaPathGenerator.execDBpediaQuery(sparqlQuery);
		if(results != null)
			while(results.hasNext()){
				String type = results.next().getResource("?p").toString();
				if(type.contains("http://dbpedia.org/ontology"))
					types.add(type);
			}
		
		return types;
	}
	
	/**
	 * transforms an article name of wikipedia to a dbpedia URI
	 * @param wikiEntity
	 * @return
	 */
	public static String escapeSpecialCharSparql(String entity) {
		String encodeEntity = "";
		try{
		String replaceSpace = entity.replace(" ", "_");
		encodeEntity = URLEncoder.encode(replaceSpace, "UTF-8");
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return encodeEntity;
	}
	
	public static void main(String[] args) {
		System.out.println("USE2012: " + DBpediaTypeGetter.getType("United States presidential election, 2012"));
		System.out.println("BO: "+ DBpediaTypeGetter.getType("Barack Obama"));
		System.out.println("MR: " + DBpediaTypeGetter.getType("Mitt Romney"));
		System.out.println("India: " +DBpediaTypeGetter.getType("India"));
	}
}
