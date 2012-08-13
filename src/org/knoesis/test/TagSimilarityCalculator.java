package org.knoesis.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.knoesis.api.WikipediaParser;
import org.knoesis.similarity.CosineSimilarityCalculator;
import org.knoesis.twarql.extractions.DBpediaSpotlightExtractor;
import org.knoesis.storage.Database;


import twitter4j.Tweet;

public class TagSimilarityCalculator {
	
	Map<String,Double> entitiesFrequency = new HashMap<String,Double>();
	Map<String,Double> wikipediaEntitiesFreq = new HashMap<String,Double>(); 
	
	// These are used to sort the map based on the values.
	ValueComparator entityFrequencyComparator = new ValueComparator(entitiesFrequency);

	TreeMap<String, Double> sortedEntitesFrequency = new TreeMap<String,Double>(entityFrequencyComparator);
	

	public ResultSet queryDB(Connection connection,String sql){
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void insertToDB(String twitter_ID,Set<String> entities,String tag, Connection conn){

		PreparedStatement ps = null;

		// ==PreparedStatement pstmt;=========== twitterdata table ===============
		String sql = "INSERT IGNORE INTO `continuous_semantics`.`tweetEntities` "
				+ "(`twitter_ID`, `entity`,`hashTag`) "
				+ "VALUES ( ?, ?,?);";

		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for(String entity: entities){
				//System.out.println("The entity is " + entity);
				ps.setString(1, twitter_ID);
				ps.setString(2, entity);
				ps.setString(3, tag);
				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getDisticntEntities(String tag){
		String query = "SELECT twitter_ID,tweet from `twitris_healthcare`.`twitterdata` WHERE eventID like 'Hash_" + tag + "'";
		Connection conn = Database.getConnectionToDB("twitris", "pranyd09!");
		Map<String, Double> entitiesMap = new HashMap<String, Double>();
		DBpediaSpotlightExtractor extractor = new DBpediaSpotlightExtractor();
		ResultSet results = queryDB(conn, query);
		
		try {
			while(results.next()){
				String twitterID = results.getString(1);
				String tweetContent = results.getString(2);
				System.out.println("Tweet content -- " + tweetContent );
				Set<String>	entities = extractor.extract(tweetContent);
				insertToDB(twitterID, entities, "Hash_"+tag, conn);
				// Adding entities into the hashmap
				for(String entity : entities)
				{
					if(entitiesMap.containsKey(entity))
						entitiesMap.put(entity, entitiesMap.get(entity) + 1);
					else
						entitiesMap.put(entity, 1.0);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("No of distinct entities are " + entitiesMap.keySet().size());
	}
	
	/**
	 * This method calculates the cosine similarity of the entities co-occuring with the hashtag and entities in wikipedia page 
	 * of US Elections
	 * 
	 * @param tag
	 * @param takeEntitiesFromDB We use this flag because, for some tags we already extracted entities and stored them into 
	 * 							 database.
	 * @return
	 */
	public double calculate(String tag, boolean takeEntitiesFromDB){
		DBpediaSpotlightExtractor entityExtractor = new DBpediaSpotlightExtractor();
		String queryForTweetsOfATag = "SELECT twitter_ID,tweet from `twitris_healthcare`.`twitterdata` WHERE eventID like " +
				"'usElection2012' && tweet like '%" +"#"+tag+"%' && published_date > '2012-08-02 00:00:00'" +
				"&& published_date < '2012-08-03 00:00:00'";

		String queryForEntities = "Select Entity from `continuous_semantics`.`tweetEntities` WHERE hashTag like '"+tag+"'";

		Connection conn = Database.getConnectionToDB("twitris", "pranyd09!");
		if(takeEntitiesFromDB){
			ResultSet results = queryDB(conn, queryForEntities);
			System.out.println("Quering database");
			try {
				while(results.next()){
					String entity = results.getString(1);
					//System.out.println(entity);
					if(!entity.toLowerCase().matches("(.*)"+tag+"(.*)"))
						if(entitiesFrequency.containsKey(entity))
							entitiesFrequency.put(entity, entitiesFrequency.get(entity) + 1);
						else
							entitiesFrequency.put(entity, 1.0);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sortedEntitesFrequency.putAll(entitiesFrequency);
			System.out.println("Entities Frequency -- "+ sortedEntitesFrequency);

		}
		else{
			ResultSet results = queryDB(conn, queryForTweetsOfATag);
			System.out.println("Quering");
			try {
				if(results != null){
					System.out.println("Has results");
					while(results.next()){
						String twitterID = results.getString(1);
						String tweet = results.getString(2);
						Set<String>	entities = entityExtractor.extract(tweet);

						// Adding entities into the hashmap
						for(String entity : entities)
						{
							if(entitiesFrequency.containsKey(entity))
								entitiesFrequency.put(entity, entitiesFrequency.get(entity) + 1);
							else
								entitiesFrequency.put(entity, 1.0);
						}
						// Inserting into DB
						//insertToDB(twitterID, entities,tag, conn);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//Getting wikipedia links in dbpedia format.
		WikipediaParser wikiParser = new WikipediaParser("United_States_presidential_election,_2012");
		for(String link: wikiParser.getLinks()){
			try {
				String linkSpaceReplaced = link.replace(" ", "_");
				String linkEncoded = URLEncoder.encode(linkSpaceReplaced,"UTF-8");
				String dbpediaStyle = "http://dbpedia.org/resource/" + linkSpaceReplaced ;
				wikipediaEntitiesFreq.put(dbpediaStyle, 1.0);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		System.out.println("WikiPedia Entities -- "+wikipediaEntitiesFreq);

		// Getting the cosine similarity
		return CosineSimilarityCalculator.calculate(entitiesFrequency, wikipediaEntitiesFreq);

	}

	public static void main(String[] args) {
		TagSimilarityCalculator simCal = new TagSimilarityCalculator();
		//simCal.getDisticntEntities("obama");
		System.out.println(simCal.calculate("gop", false));
	}

}
