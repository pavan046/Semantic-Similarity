package org.knoesis.twarql.extractions;

import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.lang.MutableString;
import it.unimi.dsi.util.Interval;
import it.unimi.dsi.util.TernaryIntervalSearchTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knoesis.models.AnnotatedTweet;
import org.knoesis.utils.StopWords;
import org.knoesis.utils.Tokenizer;

/**
 * This entity extractor uses a Ternary Search Tree to spot every entity match
 * Operates on surface forms (entity names) only.
 *  
 *
 * @author pavan
 * @author pablomendes (preliminary impl.)
 */
public class TrieExtractor implements Extractor {

	public static int count = 0;

	//private static final Log LOG = LogFactory.getLog(TrieExtractor.class);
	final static Log LOG = LogFactory.getLog(TrieExtractor.class);

	protected TernaryIntervalSearchTree entities;
	private StopWords teststopword = new StopWords();

	public TrieExtractor(String instancesFileName) throws IOException, ClassNotFoundException {
		this(new File(instancesFileName));
	}

	public TrieExtractor(URI instancesFileURI) throws IOException, ClassNotFoundException {
		this(new File(instancesFileURI));
	}

	public TrieExtractor(File instancesFile) throws IOException, ClassNotFoundException {
		this(instancesFile.getName().endsWith(".gz") ? 
				new GZIPInputStream(new FileInputStream(instancesFile)) : 
					new FileInputStream(instancesFile));
	}

	public TrieExtractor(InputStream instancesInputStream) throws IOException, ClassNotFoundException {
		entities = (TernaryIntervalSearchTree) BinIO.loadObject(instancesInputStream);
		if (entities == null) throw new IOException("Can't load null input stream.");
		if (entities.size() == 0) throw new IOException("Found 0 entities inside the entities input stream)");
		LOG.info("Loaded a ternary search tree with "+entities.size()+" entities");
	}

	public Set<String> extract(Object tweet) {
		Set<String> list = new HashSet<String>();
		List<String> ngramTerms = new ArrayList<String>();
		if (tweet == null) // this shouldn't happen
			return list;

		//everything was lowercased in EntityIndexer.
		String tweetContent = ((String) tweet).toLowerCase();
		try {
			/*
			 * Used Punctuations to split hence removed the trimming of punctuations at the end
			 * Since the Intervals are matched with the tweet content I guess its fine to 
			 * split words using punctuations
			 */
			String[] words = Tokenizer.tokenize(tweetContent);
			for (String w: words) {
				if(w==null)
					continue;
				w = w.trim();
				if((w.isEmpty())||(teststopword.isStopword(w)))
					continue;
				//System.out.println("word:"+w);
				Interval ival = entities.getApproximatedInterval(w);
				int maxNgram = 0;
				String ngramEntity = "";
				String entity = "";
				for (int i=ival.left; i<=ival.right; i++) {
					MutableString suggestion = entities.list().get(i);
					entity = suggestion.toString();
					int numberTerms=java.util.regex.Pattern.compile("[\\w]+").split(entity.trim()).length;
					if(numberTerms>=maxNgram && tweetContent.contains(entity) && !ngramTerms.contains(entity)){
						ngramEntity = entity;
						ngramTerms.addAll(Arrays.asList(ngramEntity.split(" ")));
					}
				}
				if(!list.contains(ngramEntity) && !ngramEntity.equals("")){
					if(ngramEntity.equals(w) || tweetContent.matches(".*"+Tokenizer.PUNCTUATION+ngramEntity+Tokenizer.PUNCTUATION+".*"))
						list.add(ngramEntity.toString());
					count++;
				}
			}

		} catch (NullPointerException e) {
			//((Log) LOG).error("Tweet caused NullPointerException: "+tweetContent);
			e.printStackTrace();
		}
		//LOG.info("End Entity Extraction");
		return list;
	}


	public void process(AnnotatedTweet tweet) {
		//FIXME WARNING!!!! This is bad practice. We are resetting the entities in every processor
		//                  So if two processors try to set the same field, one will overrule the other.
		//                  Should have instead an updateEntities() method.
		tweet.setEntities(extract(tweet.getTwitter4jTweet().getText()));
	}


	public void stop() {
	}

	public static void main(String[] args) {
		String testString = "India is the mittromney country Usain Bolt united states. Barack Obama, romney, london," +
				"";
		try {
			//TrieExtractor entityExtractor = new TrieExtractor("dbpediaEntities.Trie");
			DBpediaSpotlightExtractor spotlight = new DBpediaSpotlightExtractor();
			TrieExtractor entityExtractor = new TrieExtractor("dbpedia_entities.trie");
			System.out.println(entityExtractor.extract(testString));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
