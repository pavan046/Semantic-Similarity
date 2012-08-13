package org.knoesis.tags.analysis;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;



/**
 * 
 * @author pavan
 * 
 * The objective of this class is to get info/analysis of 
 * hashtag co-occrance graphs. 
 *
 */
public class HashTagConnectivity {
	private static Graph<String, String> graph;
	private static File popularTagsFile, tweetTagFile;
	private static List<String> popularTags;
	/**
	 * @param populatTagsFileName -- Tags to be considered
	 * @param tweetTagFileName -- <Tweetid> <tag> to generate co-occurrance graph
	 */
	public HashTagConnectivity(String populatTagsFileName, String tweetTagFileName) {
		graph = new UndirectedSparseGraph<String, String>();
		popularTags = new ArrayList<String>();
		popularTagsFile = new File(populatTagsFileName);
		tweetTagFile = new File(tweetTagFileName);
		populatePopularTagsList();
		generateGraphFromFile();
	}
	/**
	 * Builds a list of tags that should only 
	 * be used for the analysis for co-occurrance.
	 */
	private static void populatePopularTagsList(){
		Scanner readPopTagFile;
		try {
			readPopTagFile = new Scanner(popularTagsFile);
			while(readPopTagFile.hasNext()){
				String tag = readPopTagFile.nextLine();
				popularTags.add(tag);
				graph.addVertex(tag);	
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Generates the graph based on the files initialized
	 * in the constuctor
	 */
	private static void generateGraphFromFile() {
		Scanner readFile = null;
		try {
			readFile = new Scanner(tweetTagFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = "";
		boolean first = true;
		Integer count=0;
		while(readFile.hasNext()){
			List<String> tags = new ArrayList<String>();
			if(first){
				line = readFile.nextLine();
				first = false;
			}
			String[] tagsId = line.split("\t");
			String tweetId = tagsId[0];
			tags.add(tagsId[1]);
			boolean flag = true;
			while(flag){
				if(!readFile.hasNext())
					break;
				line = readFile.nextLine();
				tagsId = line.split("\t");
				if(tweetId.equals(tagsId[0])){
					tags.add(tagsId[1]);
				}
				else{
					flag = false;
					if(tags.size()>1){
						for(int i=0; i<tags.size()-1; i++){
							for(int j=i+1; j<tags.size(); j++){
								if(!tags.get(i).equals(tags.get(j)))
									try{
										String tag1 = tags.get(i).toLowerCase();
										String tag2 = tags.get(j).toLowerCase();
										if(!popularTags.contains(tag1) || !popularTags.contains(tag2))
											continue;
										graph.addEdge(tags.get(i).toLowerCase() + "-" + tags.get(j).toLowerCase(), 
												tags.get(i).toLowerCase(), tags.get(j).toLowerCase(), EdgeType.UNDIRECTED);
									}catch (IllegalArgumentException e){
										continue;
									}
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Provides the Global Clustering Co-efficient.
	 * @return
	 */
	public static double getClusteringCoeff(){
		Map<String, Double> metrics = Metrics.clusteringCoefficients(graph);
		Double clusteringCoeff = 0.0d;
		for(String tag: metrics.keySet()){
			clusteringCoeff = metrics.get(tag);
		}
		clusteringCoeff = clusteringCoeff/metrics.size();
		return clusteringCoeff;
	}
	/**
	 * Visualizes the constucted Graph with ISOMLayout
	 */
	public static void graphVisualize(){
		Layout<Integer, String> layout = new CircleLayout(graph);
		layout.setSize(new Dimension(800,800)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Integer,String> vv = 
				new BasicVisualizationServer<Integer,String>(layout);
		vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
		BasicVisualizationServer<Integer,String> bvv = 
	              new BasicVisualizationServer<Integer,String>(layout);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
		
		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv); 
		frame.pack();
		frame.setVisible(true);    
	}
	
	public static int reachability(String tag){
		DijkstraShortestPath shortestPath = new DijkstraShortestPath<String, String>(graph);
		Map<String, Double> distanceMap = shortestPath.getDistanceMap(tag, popularTags);
		System.out.println(distanceMap.keySet().size() + " -- " + popularTags.size());
		int i = 1;
		for(String vertex: popularTags){
			if(!distanceMap.keySet().contains(vertex))
				System.out.println(i+ " -- ERR: " + vertex);
			else
				System.out.println(i+ ": "+ vertex+": "+distanceMap.get(vertex));
			i++;
		}
		return i;
	}
	
	public static void main(String[] args) {
		HashTagConnectivity ds = new HashTagConnectivity("./analysis/clusters/ustags_popular_nocount_400", "./analysis/clusters/ustagsall.txt");
		ds.graphVisualize();
	}
}
