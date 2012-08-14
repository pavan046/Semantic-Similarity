package org.knoesis.twarql.storm.topologies;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knoesis.twarql.storm.bolts.TagExtractorBolt;
import org.knoesis.twarql.storm.bolts.TweetDBStorageBolt;
import org.knoesis.twarql.storm.spouts.TwarqlSpout;

//Storm Imports
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

/**
 * This class creates a topology for analyzing twitter stream.  
 * This class can be transformed to use the Extactions in Pipeline or in Parallel
 * The arguments needed are
 * 	1. Twitter Username
 *  2. Twitter Password
 *  3. Trie Extractor File
 *  4. Entity URL Tab seperated file
 *  
 * @author pavan
 *
 */
public class TwarqlTopology 
{
	private static Log LOG = LogFactory.getLog(TwarqlTopology.class);
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		if(args.length < 2)
			LOG.error("Arguments <Twitter Username> <Twitter Password>");
		// Twitter Authentication UserName and Password.
		String userName = args[0];
		String password = args[1];
		
		
		
		// Construct the Topology
		LOG.debug("Creating the required Topology");
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout(1, new TwarqlSpout(userName, password));
		builder.setBolt(2, new TagExtractorBolt(), 1).shuffleGrouping(1);
		builder.setBolt(3, new TweetDBStorageBolt(), 1).shuffleGrouping(1);
		boolean localMode = true;
		
		// LocalMode Deployment		
		if (localMode)
		{	
			// Setup the Configuration
			Config conf = new Config();
			conf.setNumWorkers(1);
			
			// Setup the Local Cluster for Testing Purposes
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("TwitrisTopology", conf, builder.createTopology());
			
			// Shut the Cluster Down after X Minutes.
//			Utils.sleep(1 * 50 * 1000);
//			cluster.shutdown();
		}
		
		// Remote Deployment
		else
		{
			Config conf = new Config();
			conf.setNumWorkers(1);
			conf.setMaxSpoutPending(5000);
			conf.setDebug(true);
			try 
			{
				StormSubmitter.submitTopology("TwarqlTopology", conf, builder.createTopology());
			}
			catch (Exception e)
			{
				return;
			}
		}
		
		System.err.println("died");
	}

}