package org.knoesis.twarql.storm.spouts;

//JDK Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//Storm Imports
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

//Twitter4J Imports
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/*
* This Storm Spout is responsible for distributing the "tweets" shared by the 
* Twitter Streaming API.
* 
* Note: It is heavily based on Nathan Marz's TwitterSampleSpout found in the 
* "storm-starter" project: https://github.com/nathanmarz/storm-starter 
*/
public class TwarqlSpout implements IRichSpout 
{
	// Declarations
	SpoutOutputCollector collector; // Used to distribute tuples (Edge)
	LinkedBlockingQueue<Status> queue = null; // Queue of Status Objects
	TwitterStream twitterStream; // Twitter Stream
	String twitterUserName; // Twitter Login User Name
	String twitterPassword; // Twitter Login Password
	private Log LOG = LogFactory.getLog(TwarqlSpout.class);
	// Constructor:
	// Sets the Authentication data to access the Twitter Streaming API.
	public TwarqlSpout(String userName, String password)
	{
		this.twitterUserName = userName;
		this.twitterPassword = password;
	}

	@Override
	public void ack(Object arg0) 
	{
		// Nothing Done Here At This Point ....
	}

	@Override
	public void close() 
	{
		// Close the Twitter Stream
		this.twitterStream.shutdown();
	}

	@Override
	public void fail(Object arg0) 
	{
		// Nothing Done Here At This Point ...
	}

	@Override
	public void nextTuple() 
	{
		// Grab a Status off the Queue
		Status status = queue.poll();
		
		// Check to make sure it's Valid (not empty?)
		if (status == null)
		{
			// If Invalid, Let's Wait for 50ms.
			Utils.sleep(50);
		}
		
		// If Valid, Pass the "Tweet" to the Cluster.
		else
		{
			this.collector.emit(new Values(status));
		}
	}

	@Override
	public void open(Map conf, TopologyContext context, 
						SpoutOutputCollector collector) 
	{
		// Instantiate the Queue
		queue = new LinkedBlockingQueue<Status>(1000);
		
		// Specify the Output Collector
		this.collector = collector;
		
		// Instantiate the Twitter Status Listener and set it to add new
		// Status objects to the Queue.
		StatusListener listener = new StatusListener()
		{	
			@Override
			public void onException(Exception e) 
			{
				// Nothing done here, yet...
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice notice) 
			{
				// Nothing done here, yet...
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) 
			{
				// Nothing done here, yet...
			}

			@Override
			public void onStatus(Status status) 
			{
				// Insert the Status into the Queue
				queue.offer(status);
			}

			@Override
			public void onTrackLimitationNotice(int notice) 
			{
				// Nothing done here, yet...
			}
		};
		
		// Start The Stream
		LOG.info("Starting the twitter Stream for the user "+this.twitterUserName);
		TwitterStreamFactory fact = new TwitterStreamFactory(
				new ConfigurationBuilder()
					.setUser(this.twitterUserName)
					.setPassword(this.twitterPassword)
					.build());
	     twitterStream = fact.getInstance();
	     twitterStream.addListener(listener);
	     //twitterStream.sample();
	     
	     // Setup and Execute Stream Filtering
	     FilterQuery filterQuery = new FilterQuery();
	     // Fetch keywords from a file
	  
	     
//	     List<String> keywordList = new ArrayList<String>();
//	     try {
//			Scanner scan = new Scanner(new File("entities"));
//			while(scan.hasNext())
//				keywordList.add(scan.nextLine());
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	     
//		 String[] keywords = (String[]) keywordList.toArray(new String[keywordList.size()]);
//	     String[] keywords = {"twitrist"};
	     String[] keywords = {"#sandy"};
	     filterQuery.track((String[]) keywords);
	     twitterStream.filter(filterQuery);
	     
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) 
	{
		// Declare the Output Label (Single Tweet)
		declarer.declare(new Fields("tweet"));
	}

	@Override
	public boolean isDistributed() 
	{	
		return false;
	}

}

/** TO DO:
//3.Save to DB
//2.Extract Data From Fields
//1.Implementation of Filter/Track Method
**/ 