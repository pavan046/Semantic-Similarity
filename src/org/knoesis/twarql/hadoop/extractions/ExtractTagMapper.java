package org.knoesis.twarql.hadoop.extractions;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Mapper;
import org.knoesis.twarql.extractions.TagExtractor;

/**
 * Mapper to extract URLs from tweets
 * Expects first field to be tweetId and second field to be tweetContent
 * 
 * @author pmendes 
 */
public class ExtractTagMapper<K extends WritableComparable> extends Mapper<K, Text, LongWritable, Text> {

  private static final Log LOG = LogFactory.getLog(ExtractTagMapper.class);
  private TagExtractor extractor = new TagExtractor();

  private int recordsRead = 0;
  private int recordsWritten = 0;
    
  protected void map(K key, Text value, org.apache.hadoop.mapreduce.Mapper<K,Text,LongWritable,Text>.Context context) throws IOException, InterruptedException {
	  System.setProperty(
			  "javax.xml.parsers.DocumentBuilderFactory",
			  "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl" );
	  recordsRead++;
	  if (recordsRead==1)
		  context.setStatus(" parsing first record ");
	  
	  long tweetId = 0;
	  String date = null;
	  String tweetContent = null;
	  /*
	   * If the line is a continuation of the previous line then it is ignored 
	   */
	  try {
		  String[] line = value.toString().split("\t");
		  tweetId = new Long(line[0]);
		  date = line[1];
		  tweetContent = line[2];
	  } catch (Exception e) {
		  //LOG.debug("Line is not parseable: "+value+" \n"+e.getMessage());
		  return;
	  } 

	  Set<String> tags = extractor.extract(tweetContent);

	  collect(context, tweetId, date, tags);
	  
	  // Every 100 records update application-level status
	  if ((recordsRead%100) == 0) {
		  context.setStatus(" parsed:" + recordsRead +" wrote:" + recordsWritten + " lines");
		  
		  if ((recordsRead%100000) == 0) {
			  LOG.info(" parsed:" + recordsRead +" wrote:" + recordsWritten + " lines");
			  //LOG.info("Time: "+time+" Keywords: "+keywords+" Fixes: "+fixes+" Clicks: "+clicks);
		  }
	  }
	  
  }
  
/**
 *   
 * @param output	is the OutputCollector to dump stuff into
 * @param id		time of the query execution
 * @param tweetContent	the search query keywords
 * @param fixes		the entities and prefix/postfix found within the keywords
 * @param clicks	the URLs that were clicked by the user
 * @throws IOException
 * @throws InterruptedException
 */
  protected void collect(Context output, Long id, String date, Set<String> tags) throws IOException, InterruptedException {	  

	  for (String tag: tags) {
		  recordsWritten++;
		  output.write(
				  new LongWritable(id),
				  new Text(date+"\t"+tag.toLowerCase()+"\twallStreetProtests")
		  );
	  }
		  
	  
  }

}


