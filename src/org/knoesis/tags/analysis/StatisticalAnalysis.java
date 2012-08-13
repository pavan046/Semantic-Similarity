package org.knoesis.tags.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knoesis.models.HashTagAnalytics;
import org.knoesis.tags.analysis.Analyzer;
import org.knoesis.tags.analysis.AnalyzerPipelineExecuter;
import org.knoesis.tags.analysis.ConsistencyAnalyzer;
import org.knoesis.tags.analysis.FrequencyAnalyzer;
import org.knoesis.tags.analysis.ReTweetCounter;
import org.knoesis.tags.analysis.SpecificityAnalyzer;
import org.knoesis.twarql.extractions.TagExtractor;
import org.knoesis.utils.Utils;

/**
 * This class is used to get the tweets from DB (for elction2012 now), 
 * extract tags and get analytics for each tag.
 * @author koneru
 *
 */
public class StatisticalAnalysis {

	/**
	 * Getting connection for the database
	 * @param username
	 * @param password
	 * @return
	 */
	public Connection getConnectionToDB(String username,String password){
		Connection conn = null;
		System.out.println(new Date() + " Connecting to Database");
		String url = "jdbc:mysql://130.108.5.96/twitris_healthcare?user=" + username + "&password=" + password;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to database");
		return conn;
		
	}
	
	/**
	 * This method get the tweets from the DB (Which we are collecting for election2012), Gets all distinct hashtags and does
	 * analytics on every hashtag.
	 */
	public void analyze(){
		Connection conn = getConnectionToDB("twitris", "pranyd09!");
		Map<String, Integer> hashTagsMap = new HashMap<String, Integer>();
		Map<String, HashTagAnalytics> tagsAnalytics	= new HashMap<String, HashTagAnalytics>();
		TagExtractor tagExtractor = new TagExtractor();
		
		String sql = "SELECT tweet from `twitris_healthcare`.`twitterdata` WHERE eventID like 'ContiSemantics' && published_date > '2012-08-02 00:00:00'";
		
		try {
			Statement selectStatement = conn.createStatement();
			ResultSet result = selectStatement.executeQuery(sql);
			
			while (result.next()) {
				String tweetContent = result.getString(1);
				Set<String> tags = tagExtractor.extract(tweetContent);
				
				Iterator<String> iterator = tags.iterator();
				while(iterator.hasNext()){
					String tag = iterator.next().toLowerCase();
					// Not adding #election2012
					if(!tag.equals("#election2012"))
						if(hashTagsMap.containsKey(tag))
							hashTagsMap.put(tag, hashTagsMap.get(tag) + 1);
						else
							hashTagsMap.put(tag, 1);
				}				
			}
			
			System.out.println(hashTagsMap.toString());

			List<Analyzer> analyzers = new ArrayList<Analyzer>();
			// Adding specificity Analyzer
			analyzers.add(new SpecificityAnalyzer());
			// Adding consistency analyzer
			analyzers.add(new ConsistencyAnalyzer());
			// Adding Frequency Analyzer
			analyzers.add(new FrequencyAnalyzer());
			// Adding retweet Counter
			analyzers.add(new ReTweetCounter());

			// Calling the pipeline to process.
			AnalyzerPipelineExecuter pipeline = new AnalyzerPipelineExecuter(analyzers);
			
			Iterator<String> tagsIterator = hashTagsMap.keySet().iterator();
			while(tagsIterator.hasNext()){
				String hashTag = tagsIterator.next();
				Utils.writeToFile("TagAnalytics.csv",hashTag+ ","+ pipeline.process(hashTag).toString());
				//tagsAnalytics.put(hashTag, pipeline.process(hashTag));
			}
			
			//System.out.println(tagsAnalytics.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		StatisticalAnalysis analyzer = new StatisticalAnalysis();
		analyzer.analyze();
	}
}
