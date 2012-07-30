package org.knoesis.similarity.wikipedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.knoesis.api.WikipediaParser;
/**
 * This class is used for calculating the relatedness between two concepts 
 * based on their relatedness concepts. 
 * @author pavan
 *
 */
public class BiConceptRelatednessCalculator {

	private static String conceptOne, conceptTwo;
	private static int hops;
	private WikipediaParser parser;
	private Logger LOG = Logger.getLogger("BiConceptRelatednessCalculator");
	public BiConceptRelatednessCalculator(String conceptOne, String conceptTwo) {
		this.conceptOne = conceptOne;
		this.conceptTwo = conceptTwo;
		parser = new WikipediaParser();
	}

	public Map<String, Integer> calculate(){
		Map<String, Integer> relatedness = new HashMap<String, Integer>();
		relatedness.put(conceptOne+"--"+conceptTwo, calculateRelatedness(conceptOne, conceptTwo));
		relatedness.put(conceptTwo+"--"+conceptOne, calculateRelatedness(conceptTwo, conceptOne));
		return relatedness;
	}

	private int calculateRelatedness(String c1, String c2) {
		parser.setWikipediaPage(c1);
		Set<String> firstLinks = parser.getLinks();
		if(firstLinks.contains(c2)||firstLinks.contains("Template:"+c2))
			return 2;

		for(String link: firstLinks){
			parser.setWikipediaPage(link);
			if(parser.getLinks().contains(c2) || parser.getLinks().contains("Template:"+c2))
				return 1;
		}
		return 0;
	}

	private int calculateRelatedness(String c1, String c2, Map<String, String> jsonMap) {
		try{
			Set<String> firstLinks = parser.parseJson("prop", jsonMap.get(c1));
			if(firstLinks.contains(c2)||firstLinks.contains("Template:"+c2))
				return 2;

			for(String link: firstLinks){
				parser.setWikipediaPage(link);
				Set<String> secondLinks = parser.parseJson("prop", jsonMap.get(link));
				if(secondLinks.contains(c2) || secondLinks.contains("Template:"+c2))
					return 1;
			}
		}
		catch(NullPointerException e){
			return 0;
		}
		return 0;
	}

	//	private Map<String, Integer> calculateWeights(Map<String, List<String>> secondLinks){
	//		Map<String, Integer> results = new HashMap<String, Integer>();
	//		for(String firstLink: secondLinks.keySet()){
	//			List<String> links = secondLinks.get(firstLink);
	//			if(links.contains(wikipediaTopic))
	//				results.put(firstLink, 3);
	//			else
	//				results.put(firstLink, 2);
	//		}
	//		for(String firstLink: secondLinks.keySet()){
	//			List<String> links = secondLinks.get(firstLink);
	//			for(String secondLink: links){
	//				if(!results.keySet().contains(secondLink))
	//					results.put(secondLink, 1);
	//			}
	//		}
	//		return results;
	//	}

	public static void main(String[] args) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			BufferedReader reader = new BufferedReader(new FileReader(new File("us_1")));
			String line;
			List<String> entities = new ArrayList<String>();
			while((line = reader.readLine()) != null) {
				System.out.println(line);
				WikipediaParser parser = new WikipediaParser(line);
				entities.add(line);	        	
				map.put(line, parser.getLinksJson());
			}
			Map<String, String> temp = new HashMap<String, String>();
			for(String link: map.keySet()){
				Set<String> secondLinks = WikipediaParser.parseJson("prop", map.get(link));
				for(String secondLink: secondLinks){
					System.out.println(link+":"+secondLink);
					temp.put(secondLink, new WikipediaParser(secondLink).getLinksJson());
				}
			}
			map.putAll(temp);
			reader.close();
			Writer write = new FileWriter(new File("us_elections.similarit"));
			Object[] entityArray = entities.toArray();
			for(int i=0; i<entityArray.length; i++){
				String one = (String)entityArray[i];
				for(int j=0; j<entityArray.length; j++){
					String two = (String)entityArray[j];
					if(i==j){
						write.append(one+"--"+two+"\t"+"000"+"\n");
						continue;
					}

					BiConceptRelatednessCalculator wikiCalc = new BiConceptRelatednessCalculator(one, two);
					int relatedLinks = wikiCalc.calculateRelatedness(one, two, map);
					write.append(one+"--"+two+"\t"+relatedLinks+"\n");
				}
			}
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
