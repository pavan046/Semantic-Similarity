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
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;





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
		Statement stmt;
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
	 * Inserting the details of the URL into the table with 
	 * similarity to other highly ranked URLs
	 */
	
	
	public static void main(String[] args) {
		TagAnalyticsDataStore dataStore = new TagAnalyticsDataStore();
		//dataStore.insertURL("http://news.carbon-future.co.uk/archives/42856", "224267191476953089");
		Set<String> tags = new HashSet<String>();
		tags.add("#something");
		tags.add("#obama");
		dataStore.batchInsertTags("11123112341423", tags);
	}


}
