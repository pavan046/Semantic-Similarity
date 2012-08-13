package org.knoesis.twarql.storm.bolts;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.knoesis.storage.TagAnalyticsDataStore;
import org.knoesis.twarql.extractions.Extractor;
import org.knoesis.twarql.extractions.TagExtractor;


import twitter4j.Status;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
/**
 * This Bolt extracts hastags and emits a stream of triples with the tweetid.
 * Normally used when you need to parallellize the Extractors (Not in pipeline)
 * @author pavan
 *
 * TODO: Would be better if the schema for the triple are taken from a config file or an ontology/schema reader.
 */
public class TagAnalyticsBolt implements IRichBolt{
	private OutputCollector _collector;
	private static Extractor hashTagExtractor = new TagExtractor();
	private TagAnalyticsDataStore dataStore = new TagAnalyticsDataStore();
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("id", "triples"));
	}

	@Override
	public void cleanup() {
			
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		Status status = (Status)input.getValue(0);
		Set<String> tags = (Set<String>) hashTagExtractor.extract(status.getText());
		String tweetId = Long.toString(status.getId());
		dataStore.batchInsertTags(tweetId, tags);
	}

}
