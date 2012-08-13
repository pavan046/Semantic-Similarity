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
	
	
	public static void main(String[] args) {
		TagAnalyticsDataStore dataStore = new TagAnalyticsDataStore();
//		//dataStore.insertURL("http://news.carbon-future.co.uk/archives/42856", "224267191476953089");
//		Set<String> tags = new HashSet<String>();
//		tags.add("#something");
//		tags.add("#obama");
//		dataStore.batchInsertTags("11123112341423", tags);
		Map<String, Double> relatedArticles = new HashMap<String, Double>();
		relatedArticles.put("United States", 3.23412134d);
		relatedArticles.put("Barack Obama", 2.234123415345d);
		dataStore.insertWikiArticles(relatedArticles, "uselections");
	}


}
