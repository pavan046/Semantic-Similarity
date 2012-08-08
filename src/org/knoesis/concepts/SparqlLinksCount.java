package org.knoesis.concepts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;

public class SparqlLinksCount {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		List<String> entities = Arrays.asList(
				"http://dbpedia.org/resource/Justin_Bieber", 
				"http://dbpedia.org/resource/Science", 
				"http://dbpedia.org/resource/Music",
				"http://dbpedia.org/resource/Rock_music",
				"http://dbpedia.org/resource/DBpedia",
				"http://dbpedia.org/resource/Volvo_Ocean_Race",
				"http://dbpedia.org/resource/Google",
				"http://dbpedia.org/resource/Twitter",
				"http://dbpedia.org/resource/Facebook",
				"http://dbpedia.org/resource/Mastodon_(band)",
				"http://dbpedia.org/resource/Tool_(band)",
				"http://dbpedia.org/resource/High_on_Fire",
				"http://dbpedia.org/resource/Spain",
				"http://dbpedia.org/resource/Commissioner",
				"http://dbpedia.org/resource/Cork_GAA",
				"http://dbpedia.org/resource/Dubai",
				"http://dbpedia.org/resource/Newsnight",
				"http://dbpedia.org/resource/Dublin",
				"http://dbpedia.org/resource/European_Union",
				"http://dbpedia.org/resource/China",
				"http://dbpedia.org/resource/Ireland",
				"http://dbpedia.org/resource/D%C3%A1il_%C3%89ireann",
				"http://dbpedia.org/resource/Irish_National_Stud",
				"http://dbpedia.org/resource/Michael_Noonan",
				"http://dbpedia.org/resource/London_Interbank_Offered_Rate",
				"http://dbpedia.org/resource/UBS",
				"http://dbpedia.org/resource/Andy_Griffith",
				"http://dbpedia.org/resource/Moray_Firth",
				"http://dbpedia.org/resource/Obi-Wan_Kenobi",
				"http://dbpedia.org/resource/Athenry",
				"http://dbpedia.org/resource/Real_estate_pricing",
				"http://dbpedia.org/resource/E_11_road_%28United_Arab_Emirates%29",
				"http://dbpedia.org/resource/Hair_dryer",
				"http://dbpedia.org/resource/The_Wire",
				"http://dbpedia.org/resource/Joseph_Stiglitz",
				"http://dbpedia.org/resource/Julian_Assange",
				"http://dbpedia.org/resource/Mary_Mac",
				"http://dbpedia.org/resource/Instagram",
				"http://dbpedia.org/resource/Seanad_%C3%89ireann",
				"http://dbpedia.org/resource/Main_Street",
				"http://dbpedia.org/resource/River_Lee_%28Ireland%29",
				"http://dbpedia.org/resource/Civil_defense",
				"http://dbpedia.org/resource/Queensland",
				"http://dbpedia.org/resource/Norway",
				"http://dbpedia.org/resource/Canada",
				"http://dbpedia.org/resource/Australia",
				"http://dbpedia.org/resource/Netherlands",
				"http://dbpedia.org/resource/Douglas_GAA",
				"http://dbpedia.org/resource/Shopping_mall",
				"http://dbpedia.org/resource/Back_to_the_Future",
				"http://dbpedia.org/resource/Data_Protection_Commissioner",
				"http://dbpedia.org/resource/Eircom",
				"http://dbpedia.org/resource/Akan_language",
				"http://dbpedia.org/resource/Paul_Chambers",
				"http://dbpedia.org/resource/John_Cooper_%28lawyer%29",
				"http://dbpedia.org/resource/Queen%27s_Counsel",
				"http://dbpedia.org/resource/Bilbao",
				"http://dbpedia.org/resource/College_Green",
				"http://dbpedia.org/resource/Joe_Duffy",
				"http://dbpedia.org/resource/Berlin",
				"http://dbpedia.org/resource/Measures_of_national_income_and_output",
				"http://dbpedia.org/resource/International_Monetary_Fund",
				"http://dbpedia.org/resource/IPhone",
				"http://dbpedia.org/resource/Bloomberg_L.P.",
				"http://dbpedia.org/resource/Subscriber_Identity_Module",
				"http://dbpedia.org/resource/Investigative_journalism",
				"http://dbpedia.org/resource/World_War_I",
				"http://dbpedia.org/resource/Left-wing_politics",
				"http://dbpedia.org/resource/Graphene",
				"http://dbpedia.org/resource/International_Financial_Services_Centre",
				"http://dbpedia.org/resource/Spanish_language",
				"http://dbpedia.org/resource/U2",
				"http://dbpedia.org/resource/Carol_Hawkins",
				"http://dbpedia.org/resource/Adam_Clayton",
				"http://dbpedia.org/resource/Chief_technology_officer",
				"http://dbpedia.org/resource/Bridge_%28interpersonal%29",
				"http://dbpedia.org/resource/Enda_Kenny",
				"http://dbpedia.org/resource/Oireachtas",
				"http://dbpedia.org/resource/Linked_Data",
				"http://dbpedia.org/resource/Twitter",
				"http://dbpedia.org/resource/Google",
				"http://dbpedia.org/resource/DBpedia",
				"http://dbpedia.org/resource/YouTube",
				"http://dbpedia.org/resource/Galway_GAA",
				"http://dbpedia.org/resource/Semantic_Web",
				"http://dbpedia.org/resource/Social_network",
				"http://dbpedia.org/resource/Wikipedia",
				"http://dbpedia.org/resource/Spotify",
				"http://dbpedia.org/resource/Wi-Fi",
				"http://dbpedia.org/resource/Galway",
				"http://dbpedia.org/resource/IBM",
				"http://dbpedia.org/resource/Resource_Description_Framework",
				"http://dbpedia.org/resource/Birmingham",
				"http://dbpedia.org/resource/SPARQL",
				"http://dbpedia.org/resource/Italy",
				"http://dbpedia.org/resource/Google%2B",
				"http://dbpedia.org/resource/Dublin",
				"http://dbpedia.org/resource/Israel",
				"http://dbpedia.org/resource/Ars_Technica",
				"http://dbpedia.org/resource/Stuxnet",
				"http://dbpedia.org/resource/Electronic_Sports_World_Cup",
				"http://dbpedia.org/resource/BBC_News",
				"http://dbpedia.org/resource/Black_Sabbath",
				"http://dbpedia.org/resource/BBC",
				"http://dbpedia.org/resource/Internet_Protocol",
				"http://dbpedia.org/resource/Scientific_method",
				"http://dbpedia.org/resource/Gilles_Villeneuve",
				"http://dbpedia.org/resource/Social_influence",
				"http://dbpedia.org/resource/Now_That%27s_What_I_Call_Music%21",
				"http://dbpedia.org/resource/Federal_government_of_the_United_States",
				"http://dbpedia.org/resource/Global_Positioning_System",
				"http://dbpedia.org/resource/Traffic_light",
				"http://dbpedia.org/resource/Amnesty_International",
				"http://dbpedia.org/resource/Human_rights",
				"http://dbpedia.org/resource/Montreal",
				"http://dbpedia.org/resource/Google_Chrome_Extensions",
				"http://dbpedia.org/resource/BBC_World_Service",
				"http://dbpedia.org/resource/BBC_Research",
				"http://dbpedia.org/resource/Institute_of_Electrical_and_Electronics_Engineers",
				"http://dbpedia.org/resource/Fagin",
				"http://dbpedia.org/resource/Nobel_Prize",
				"http://dbpedia.org/resource/W._Wallace_McDowell_Award",
				"http://dbpedia.org/resource/Pi_Day",
				"http://dbpedia.org/resource/Wanna_Bet%3F",
				"http://dbpedia.org/resource/Wiktionary",
				"http://dbpedia.org/resource/User_interface",
				"http://dbpedia.org/resource/VoiD",
				"http://dbpedia.org/resource/Barack_Obama",
				"http://dbpedia.org/resource/Microsoft",
				"http://dbpedia.org/resource/Web_search_engine",
				"http://dbpedia.org/resource/Tag_cloud",
				"http://dbpedia.org/resource/Personalization",
				"http://dbpedia.org/resource/Python_%28programming_language%29",
				"http://dbpedia.org/resource/Natural_language_processing",
				"http://dbpedia.org/resource/Data_mining",
				"http://dbpedia.org/resource/List_of_MLS_drafts",
				"http://dbpedia.org/resource/Facebook",
				"http://dbpedia.org/resource/Web_Ontology_Language",
				"http://dbpedia.org/resource/Discover_%28magazine%29",
				"http://dbpedia.org/resource/Linux",
				"http://dbpedia.org/resource/Bobby_Hutcherson",
				"http://dbpedia.org/resource/Domain_Name_System",
				"http://dbpedia.org/resource/Universal_Product_Code",
				"http://dbpedia.org/resource/Firefox",
				"http://dbpedia.org/resource/Since_We%27ve_Become_Translucent",
				"http://dbpedia.org/resource/Mudhoney",
				"http://dbpedia.org/resource/RDFa",
				"http://dbpedia.org/resource/Bradford_Cox",
				"http://dbpedia.org/resource/Hawaii-Aleutian_Time_Zone",
				"http://dbpedia.org/resource/Namma_Metro",
				"http://dbpedia.org/resource/India",
				"http://dbpedia.org/resource/IPhone",
				"http://dbpedia.org/resource/Accelerometer",
				"http://dbpedia.org/resource/Open_data",
				"http://dbpedia.org/resource/Mobile%2C_Alabama",
				"http://dbpedia.org/resource/Uniform_Resource_Locator",
				"http://dbpedia.org/resource/Text_box",
				"http://dbpedia.org/resource/LaTeX",
				"http://dbpedia.org/resource/Doctor_of_Philosophy",
				"http://dbpedia.org/resource/Information_graphics",
				"http://dbpedia.org/resource/Stanford_University",
				"http://dbpedia.org/resource/Machine_learning",
				"http://dbpedia.org/resource/E-learning",
				"http://dbpedia.org/resource/Apache_Hadoop",
				"http://dbpedia.org/resource/Jello_Biafra",
				"http://dbpedia.org/resource/Pope"
				);

		try {
			Writer write = new FileWriter(new File("SparqlLinksCount"));
			write.append("entity\tCOP\tCIP\tCODP\tCIDP\tCOL\tCIL\n");

			for (Iterator iterator = entities.iterator(); iterator.hasNext();) {
				String entity = (String) iterator.next();

				System.out.println(entity);
				write.append(entity+"\t");

				String sparqlQueryString1= "" +
						"SELECT (count(?p) AS ?c1) WHERE { <"+entity+"> ?p ?o .}";

				Query query = QueryFactory.create(sparqlQueryString1, Syntax.syntaxARQ);
				QueryExecution qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

				ResultSet results = qexec.execSelect();
				//				ResultSetFormatter.out(System.out, results, query); 
				write.append(results.next()+"\t");

				qexec.close() ;
				///

				String sparqlQueryString2= "" +
						"SELECT (count(?p) AS ?c2) WHERE { ?s ?p <"+entity+"> .}";

				query = QueryFactory.create(sparqlQueryString2, Syntax.syntaxARQ);
				qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

				results = qexec.execSelect();
				//				ResultSetFormatter.out(System.out, results, query);
				write.append(results.next()+"\t");

				qexec.close() ;
				///

				String sparqlQueryString3= "" +
						"SELECT (count(DISTINCT ?p) as ?c3) WHERE { <"+entity+"> ?p ?o . }";

				query = QueryFactory.create(sparqlQueryString3, Syntax.syntaxARQ);
				qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

				results = qexec.execSelect();
				//				ResultSetFormatter.out(System.out, results, query); 
				write.append(results.next()+"\t");

				qexec.close() ;
				///

				String sparqlQueryString4= "" +
						"SELECT (count(DISTINCT ?p) as ?c4) WHERE { ?s ?p <"+entity+">. }";

				query = QueryFactory.create(sparqlQueryString4, Syntax.syntaxARQ);
				qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

				results = qexec.execSelect();
				//				ResultSetFormatter.out(System.out, results, query); 
				write.append(results.next()+"\t");

				qexec.close() ;
				///

				String sparqlQueryString5= "" +
						"SELECT (count(distinct ?o) as ?c5) WHERE { <"+entity+"> <http://dbpedia.org/ontology/wikiPageWikiLink> ?o.}";

				query = QueryFactory.create(sparqlQueryString5, Syntax.syntaxARQ);
				qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

				results = qexec.execSelect();
				//				ResultSetFormatter.out(System.out, results, query);  
				write.append(results.next()+"\t");

				qexec.close() ;
				///

				String sparqlQueryString6= "" +
						"SELECT (count(distinct ?s) as ?c6) WHERE { ?s <http://dbpedia.org/ontology/wikiPageWikiLink> <"+entity+">.}";

				query = QueryFactory.create(sparqlQueryString6, Syntax.syntaxARQ);
				qexec = QueryExecutionFactory.sparqlService("http://sparql.sindice.com/sparql", query);

				results = qexec.execSelect();
				//				ResultSetFormatter.out(System.out, results, query);   
				write.append(results.next()+"\n");

				qexec.close() ;

			}
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
