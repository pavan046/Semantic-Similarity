package org.knoesis.storage;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.knoesis.models.AnnotatedTweet;
import org.knoesis.models.HashTagAnalytics;
import org.knoesis.twarql.extractions.TagExtractor;
import org.knoesis.utils.Utils;

import twitter4j.Status;
import twitter4j.Tweet;

import com.ibm.icu.util.Calendar;





/**
 * This class connects to the database and queries the necessary 
 * information to the database
 * 
 * TODO: This is a messy code -- make sure to add appropriate comments
 * 	
 * @author pavan
 *
 */
public class TagAnalyticsDataStore implements Serializable{
	private static Connection con = null;
	String serverName = "";
	public TagAnalyticsDataStore() {
		//Set hosts
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", "root");
		connectionProperties.put("password", "root");
		try {
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/tagsim",
					connectionProperties);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Inserting all the tags for a particular tweetId
	 * @param url
	 * @return
	 */
	public void batchInsertTags(String tweetId, Set<String> tags){
		String insertQuery = "Insert into tweetId_hashtag values(?, ?)";
		PreparedStatement prepareStatement;
		try {
			prepareStatement = con.prepareStatement(insertQuery);
			for(String tag: tags){
				prepareStatement.setString(1, tweetId);
				prepareStatement.setString(2, tag.toLowerCase());
				prepareStatement.addBatch();
			}
			prepareStatement.executeBatch();
			prepareStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Inserts the ranked articles into the database with is similarity measure.
	 * 
	 * @param similarArticles
	 * @param eventId
	 */
	public void insertWikiArticles(Map<String, Double> similarArticles,String eventId){
		String insertQuery = "Insert into topic_wikipedia_knowledge values(?, ?, ?, ?, ?)";
		PreparedStatement prepareStatement;
		try {
			prepareStatement = con.prepareStatement(insertQuery);
			Set<String> articles = similarArticles.keySet();
			Calendar cal = Calendar.getInstance();
			java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
			for(String article: articles){
				System.out.println("Inserting Data -- "+article);
				double linkJackardSim = similarArticles.get(article);
				double jackardSim = linkJackardSim % 1;
				prepareStatement.setString(1, eventId);
				prepareStatement.setString(2, article);
				prepareStatement.setDouble(3, linkJackardSim);
				prepareStatement.setDouble(4, jackardSim);
				prepareStatement.setDate(5, sqlDate);
				prepareStatement.addBatch();
			}
			prepareStatement.executeBatch();
			prepareStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get the K top co occurring hashtags in the tweets for an event.
	 * 
	 *  
	 *  FIXME: This has to be linked to 
	 *  		1. Event Id
	 *  		2. Time from last analysis 
	 *  		3. Tags that are already considered.
	 *  
	 * @param limit
	 * @return
	 */
	public static Set<String> getTopTags(int limit){
		String selectQuery = "SELECT hashtag, count(hashtag) as count FROM " +
				" tweetId_hashtag GROUP BY hashtag ORDER BY count DESC LIMIT "+limit;
		Set<String> tags = new HashSet<String>();
		Statement stmt;
		int i=0;
		try {
			stmt = con.createStatement();
			ResultSet results = stmt.executeQuery(selectQuery);
			while(results.next()){
				tags.add(results.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tags;
	}

	/**
	 * Inserts the tweet into the database from streaming API. 
	 * @param tweet
	 * @param eventId
	 */
	public static void insertTweet(Status tweet, String eventId){
		PreparedStatement ps = null;
		double latitude = 10000;
		double longitude = 10000;

		// ==PreparedStatement pstmt;=========== twitterdata table ===============
		String sql = "INSERT IGNORE INTO `twitterdata` "
				+ "(`twitter_ID`, `tweet`, `eventID`, `published_date`, "
				+ "`twitter_author`, `latitude`, `longitude`) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ? );";

		try {
			ps = con.prepareStatement(sql);

			String tweetContent = tweet.getText();
			String twitterId = String.valueOf(tweet.getId());
			String twitter_author = tweet.getUser().getScreenName();
			Date published_date = tweet.getCreatedAt();
			if(tweet.getGeoLocation() != null)
			{
				latitude = tweet.getGeoLocation().getLatitude();
				longitude = tweet.getGeoLocation().getLongitude();
			}			

			ps.setString(1, twitterId);
			ps.setString(2, tweetContent);
			ps.setString(3, eventId);

			// hard fix for now; trying to set time stamp to GMT timezone
			ps.setTimestamp(4, new java.sql.Timestamp(published_date.getTime()
					+ (long) 1000 * 60 * 60 * 5));
			ps.setString(5, twitter_author);
			ps.setFloat(6, (float)latitude);
			ps.setFloat(7, (float)longitude);

			ps.executeUpdate();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Inserts all the analytics values for the hastag
	 * @param tagAnalytics
	 * @param eventId
	 */
	public static void insertTagAnalytics(HashTagAnalytics tagAnalytics, String eventId){
		Calendar cal = Calendar.getInstance();
		String insert = "Insert into hashtag_analytics values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setDouble(1, tagAnalytics.getFrequencyMeasure());
			ps.setDouble(2, tagAnalytics.getSpecificityMeasure());
			ps.setDouble(3, tagAnalytics.getConsistencyMeaure());
			ps.setInt(4, tagAnalytics.getDistinctUsersMentionHashTag());
			ps.setInt(5, tagAnalytics.getNoOfTweets());
			ps.setInt(6, tagAnalytics.getNoOfReTweets());
			ps.setString(7, tagAnalytics.getHashTag());
			ps.setDouble(8, tagAnalytics.getTopicCosineSimilarity());
			ps.setDouble(9, tagAnalytics.getTopicSubsumptionSimilarity());
			ps.setString(10, eventId);
			ps.setDate(11, new java.sql.Date(cal.getTimeInMillis()));
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(tagAnalytics);
			e.printStackTrace();
		}

	}

	/**
	 * This method stores the tweets into DB from search API.
	 * 
	 * The only diff from stream and search API is the datastructure used.
	 * @param tweets
	 * @param eventID
	 */
	public void storeSearchTweetsIntoDB(List<Tweet> tweets, String eventID, String tag){

		// Please replace this with the correct username and password.
		PreparedStatement ps = null;
		double latitude = 10000;
		double longitude = 10000;

		// ==PreparedStatement pstmt;=========== twitterdata table ===============
		String sql = "INSERT IGNORE INTO `twitterdata_tag_analytics` "
				+ "(`twitter_ID`, `tweet`, `eventID`, `published_date`, "
				+ "`twitter_author`, `latitude`, `longitude`, `hashtag`) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? );";

		try {
			ps = con.prepareStatement(sql);

			for(Tweet tweet : tweets){
				String tweetContent = tweet.getText();
				String twitterID = String.valueOf(tweet.getId());
				String twitter_author = tweet.getFromUser();
				Date published_date = tweet.getCreatedAt();
				if(tweet.getGeoLocation() != null)
				{
					latitude = tweet.getGeoLocation().getLatitude();
					longitude = tweet.getGeoLocation().getLongitude();
				}			

				ps.setString(1, twitterID);
				ps.setString(2, tweetContent);
				ps.setString(3, eventID);

				// hard fix for now; trying to set time stamp to GMT timezone
				ps.setTimestamp(4, new java.sql.Timestamp(published_date.getTime()
						+ (long) 1000 * 60 * 60 * 5));
				ps.setString(5, twitter_author);
				ps.setFloat(6, (float)latitude);
				ps.setFloat(7, (float)longitude);
				ps.setString(8, tag);
				ps.addBatch();

			}

			int[] addCount = ps.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void storeEntities(List<AnnotatedTweet> tweets, String tag) {
		String insert = "Insert into entity_tag values(?, ?, ?, ?)";
		Calendar cal = Calendar.getInstance();
		java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
		try {
			PreparedStatement ps = con.prepareStatement(insert);
			for(AnnotatedTweet tweet: tweets){
				for(String entity: tweet.getEntities()){
					entity = Utils.dbpediaDecode(entity);
					ps.setString(1, tag);
					ps.setString(2, ((Long)tweet.getTwitter4jTweet().getId()).toString());
					ps.setString(3, entity);
					ps.setDate(4, date);

					ps.addBatch();
				}
			}
			ps.executeBatch();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Gets the ranked articles prestored in the db
	 * 
	 * FIXME: the topic that is hardcoded should be changed.
	 * @return
	 */
	public Map<String, Double> getRankedWikiArticles() {
		Map<String, Double> articles = new HashMap<String, Double>();
		String selectQuery = "select wiki_article, link_jackard_similarity " +
				"from topic_wikipedia_knowledge where eventID = 'usElections2012'";
		try {
			Statement stmt = con.createStatement();
			ResultSet results = stmt.executeQuery(selectQuery);
			while(results.next()){
				articles.put(results.getString(1), results.getDouble(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return articles;
	}
	/**
	 * A dummy function to get the tweets from the DB and add the 
	 * extracted tags back to the database
	 */
	public void addTags(){
		TagExtractor extract = new TagExtractor();
		String select = "select twitter_ID, tweet from twitterdata";
		String insert = "Insert into tweetId_hashtag values(?, ?)";

		try {
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(select);
			while(result.next()){
				String tweetId = result.getString(1);
				String tweet = result.getString(2);
				Set<String> tags = extract.extract(tweet);
				PreparedStatement ps = con.prepareStatement(insert);
				if (tags == null || tags.isEmpty())
					continue;
				for (String tag: tags){
					ps.setString(1, tweetId);
					ps.setString(2, tag.toLowerCase());
					ps.addBatch();
				}
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResultSet getRelevancyManualEvalated(){
		String selectQuery = "SELECT hashtag, relevant, count(*) as count FROM manual_evaluation GROUP BY hashtag, relevant ";
		ResultSet results = null;
		try{
			Statement stmt = con.createStatement();
			results = stmt.executeQuery(selectQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;

	}

	public void storeRelevancyPercentage(Map<String, Double> relevancy) {
		String insert = "Insert into manual_evaluation_percentage values(?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(insert);
			for(String tag: relevancy.keySet()){
				ps.setString(1, tag);
				ps.setDouble(2, relevancy.get(tag));

				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public static void main(String[] args) {
		TagAnalyticsDataStore dataStore = new TagAnalyticsDataStore();
		//		//dataStore.insertURL("http://news.carbon-future.co.uk/archives/42856", "224267191476953089");
		//		Set<String> tags = new HashSet<String>();
		//		tags.add("#something");
		//		tags.add("#obama");
		//		dataStore.batchInsertTags("11123112341423", tags);
		//		Map<String, Double> relatedArticles = new HashMap<String, Double>();
		//		relatedArticles.put("United States", 3.23412134d);
		//		relatedArticles.put("Barack Obama", 2.234123415345d);
		//		dataStore.insertWikiArticles(relatedArticles, "uselections");
		dataStore.addTags();
	}






}
