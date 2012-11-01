package org.knoesis.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.knoesis.twarql.extractions.DBpediaSpotlightExtractor;
import org.knoesis.twarql.extractions.Extractor;
import org.knoesis.twarql.extractions.TagExtractor;
import org.knoesis.twitter.crawler.SearchTwitter;

import it.sauronsoftware.cron4j.*;

/**
 * This class is used to schedule a java program 
 */

public class JavaScheduler {
	public static void main(String[] args) {
		
		// List of extrtactors
		List<Extractor> extractors = new ArrayList<Extractor>();
		extractors.add(new TagExtractor());
		extractors.add(new DBpediaSpotlightExtractor());
		final SearchTwitter searchTwitter = new SearchTwitter(extractors);
		
		// Creates a Scheduler instance.
		Scheduler scheduler = new Scheduler();
		
		// Note: You need to specify a pattern like I want to run this every 5 mins/ every 1 hr
		// .... Patterns can be found on http://www.sauronsoftware.it/projects/cron4j/manual.php#p02
		
		// Here we make it run every 30 mins
		scheduler.schedule("*/30 * * * *", new Runnable() {
			public void run() {
				System.out.println("Another minute ticked away..." + new Date() + " system time " + System.currentTimeMillis());
				searchTwitter.getTweets("#apple", "Apple", 100, true);
			}
		});
		
		// Starts the scheduler.
		scheduler.start();
		
		/**
		 * Look at the commented code to see how to stop the scheduler.
		 */
		
//		// Will run for ten minutes.
//		try {
//			Thread.sleep(1000L * 60L * 10L);
//		} catch (InterruptedException e) {
//			;
//		}
//		// Stops the scheduler.
//		scheduler.stop();
	}
}
