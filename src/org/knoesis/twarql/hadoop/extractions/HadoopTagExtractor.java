package org.knoesis.twarql.hadoop.extractions;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Main class to run when running the Tag extractor on Hadoop.
 * Takes in a TSV file where first field is tweetId,  second field is the date and 
 * third to be tweetContent
 * 
 * @author pavan
 */
public class HadoopTagExtractor extends Configured implements Tool {

  //singleton
  private HadoopTagExtractor() {}

  /**
   *   
 * @throws ClassNotFoundException 
 * @throws InterruptedException 
 * @throws IOException 
   */
  public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
	  System.setProperty(
			  "javax.xml.parsers.DocumentBuilderFactory",
			  "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl" );
	Configuration conf = new Configuration();
	String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	
	if (otherArgs.length < 2) {
      System.err.println("Driver <inDir> <outDir>");
      ToolRunner.printGenericCommandUsage(System.err);
      //return -1;
    }
    
    Job job = new Job(conf, "extract-tweet-tag "+otherArgs[1]);
    job.setJarByClass(HadoopTagExtractor.class);
    
    // Inputs
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
       
    // Mapper
    job.setMapperClass(ExtractTagMapper.class);
    job.setMapOutputKeyClass(LongWritable.class);
    job.setMapOutputValueClass(Text.class);
    
    // Reducer
    //job.setReducerClass(QueryFixesReducer.class);
    job.setReducerClass(Reducer.class);
    job.setNumReduceTasks(1);

    // Outputs
    //job.setOutputFormat(SequenceFileOutputFormat.class);
    job.setOutputKeyClass(LongWritable.class);
    job.setOutputValueClass(Text.class);
    //job.setOutputKeyComparatorClass(LongWritable.Comparator.class);
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    
    // run
    //JobClient.runJob(job);
  
    System.exit(job.waitForCompletion(true) ? 0 : 1);
    //return 0;
  }

@Override
public int run(String[] args) throws Exception {
	// TODO Auto-generated method stub
	return 0;
}

  /**
   * Invoke this method to submit the map/reduce job.
   * 
   * @throws IOException
   *           When there is communication problems with the job tracker.
   */
//  @SuppressWarnings("unchecked")
//  public static void main(String[] args) throws Exception {
//    int res = ToolRunner.run(new Configuration(), new QueryFixes(), args);
//    System.exit(res);
//  }
  
}