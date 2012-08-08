package org.knoesis.concepts;


import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.visualization.VisualizationImageServer;



public class ConceptsGraphCentrality {

	@SuppressWarnings("rawtypes")
	private static DirectedSparseMultigraph graph;
	private static Map<String, MyNode> nodes;
	private Transformer<MyLink, Double> wtTransformer;

	/* Constructor */
	public ConceptsGraphCentrality(){
		graph = new DirectedSparseMultigraph<MyNode, MyLink>();
		nodes = new HashMap<String, MyNode> ();

		this.wtTransformer = new Transformer<MyLink,Double>() {
			public Double transform (MyLink link) {
				return link.weight;
			}
		};
	}

	/* adds a node (from "String" to type "MyNode") to the Set of nodes */
	public void addNode(String nodeName){
		nodes.put(nodeName, new MyNode(nodeName));
	}

	/* adds a weighted edge and eventually new nodes to the graph ("graph") and to the Set of nodes ("nodes") respectively */
	public void addEdge(double weight, String node1, String node2){	
		if (!nodes.containsKey(node1))
			nodes.put(node1, new MyNode(node1));
		if (!nodes.containsKey(node2))
			nodes.put(node2, new MyNode(node2));
		graph.addEdge(new MyLink(weight), nodes.get(node1), nodes.get(node2));
	}


	public void computePageRank(){
		PageRank ranker = new PageRank(this.graph, wtTransformer, 0.15);
		ranker.acceptDisconnectedGraph(true);
		ranker.evaluate();

		for(String nodenames: nodes.keySet()){
			System.out.println("PR Node: "+nodenames+" = "+ranker.getVertexScore(nodes.get(nodenames)));
		}
	}

	public void computeEigenvectorCentrality(){
		EigenvectorCentrality ranker = new EigenvectorCentrality<MyNode, MyLink>(this.graph, wtTransformer);
		ranker.evaluate();

		for(String nodenames: nodes.keySet()){
			System.out.println("EC Node: "+nodenames+" = "+ranker.getVertexScore(nodes.get(nodenames)));
		}
	}

	public void computeDegree(){
		DegreeScorer ranker = new DegreeScorer<MyNode>(this.graph);

		for(String nodenames: nodes.keySet()){
			System.out.println("DC Node: "+nodenames+" = "+ranker.getVertexScore(nodes.get(nodenames)));
		}
	}


	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		ConceptsGraphCentrality graphInstance = new ConceptsGraphCentrality();

		// Add some directed edges along with the vertices to the graph
		//		centr.addEdge(0.2, "1", "2"); 
		//		centr.addEdge(0.1, "2", "3");
		//		centr.addEdge(0.5, "3", "5");

		//		centr.addEdge(0.5,"Josh Homme","United States");
		//		centr.addEdge(0.5,"1890 United States Census","United States");
		//		centr.addEdge(0.5,"Palo Alto, California","United States");

		ConceptsConnectionMatrix matrixClass = new ConceptsConnectionMatrix();

		List<String> entities = Arrays.asList(
				"http://dbpedia.org/resource/Justin_Bieber", 
				"http://dbpedia.org/resource/Science", 
				"http://dbpedia.org/resource/Music",
				"http://dbpedia.org/resource/Rock_music",
				"http://dbpedia.org/resource/Linked_Data",
				"http://dbpedia.org/resource/Twitter",
				"http://dbpedia.org/resource/Google",
				"http://dbpedia.org/resource/DBpedia",
				"http://dbpedia.org/resource/YouTube",
				"http://dbpedia.org/resource/Galway_GAA",
				"http://dbpedia.org/resource/Semantic_Web",
				"http://dbpedia.org/resource/Social_network",
				"http://dbpedia.org/resource/Wikipedia",
				"http://dbpedia.org/resource/Spotify",
				"http://dbpedia.org/resource/Wi-Fi"
				//				"http://dbpedia.org/resource/Galway",
				//				"http://dbpedia.org/resource/IBM",
				//				"http://dbpedia.org/resource/Resource_Description_Framework",
				//				"http://dbpedia.org/resource/Birmingham",
				//				"http://dbpedia.org/resource/SPARQL",
				//				"http://dbpedia.org/resource/Italy",
				//				"http://dbpedia.org/resource/Google%2B",
				//				"http://dbpedia.org/resource/Dublin",
				//				"http://dbpedia.org/resource/Israel",
				//				"http://dbpedia.org/resource/Ars_Technica",
				//				"http://dbpedia.org/resource/Stuxnet",
				//				"http://dbpedia.org/resource/Electronic_Sports_World_Cup",
				//				"http://dbpedia.org/resource/BBC_News",
				//				"http://dbpedia.org/resource/Black_Sabbath",
				//				"http://dbpedia.org/resource/BBC",
				//				"http://dbpedia.org/resource/Internet_Protocol",
				//				"http://dbpedia.org/resource/Scientific_method",
				//				"http://dbpedia.org/resource/Gilles_Villeneuve",
				//				"http://dbpedia.org/resource/Social_influence",
				//				"http://dbpedia.org/resource/Now_That%27s_What_I_Call_Music%21",
				//				"http://dbpedia.org/resource/Federal_government_of_the_United_States",
				//				"http://dbpedia.org/resource/Global_Positioning_System",
				//				"http://dbpedia.org/resource/Traffic_light",
				//				"http://dbpedia.org/resource/Amnesty_International",
				//				"http://dbpedia.org/resource/Human_rights",
				//				"http://dbpedia.org/resource/Montreal",
				//				"http://dbpedia.org/resource/Google_Chrome_Extensions",
				//				"http://dbpedia.org/resource/BBC_World_Service",
				//				"http://dbpedia.org/resource/BBC_Research",
				//				"http://dbpedia.org/resource/Institute_of_Electrical_and_Electronics_Engineers",
				//				"http://dbpedia.org/resource/Fagin",
				//				"http://dbpedia.org/resource/Nobel_Prize",
				//				"http://dbpedia.org/resource/W._Wallace_McDowell_Award",
				//				"http://dbpedia.org/resource/Pi_Day",
				//				"http://dbpedia.org/resource/Wanna_Bet%3F",
				//				"http://dbpedia.org/resource/Wiktionary",
				//				"http://dbpedia.org/resource/User_interface",
				//				"http://dbpedia.org/resource/VoiD",
				//				"http://dbpedia.org/resource/Barack_Obama",
				//				"http://dbpedia.org/resource/Microsoft",
				//				"http://dbpedia.org/resource/Web_search_engine",
				//				"http://dbpedia.org/resource/Tag_cloud",
				//				"http://dbpedia.org/resource/Personalization",
				//				"http://dbpedia.org/resource/Python_%28programming_language%29",
				//				"http://dbpedia.org/resource/Natural_language_processing",
				//				"http://dbpedia.org/resource/Data_mining",
				//				"http://dbpedia.org/resource/List_of_MLS_drafts",
				//				"http://dbpedia.org/resource/Facebook",
				//				"http://dbpedia.org/resource/Web_Ontology_Language",
				//				"http://dbpedia.org/resource/Discover_%28magazine%29",
				//				"http://dbpedia.org/resource/Linux",
				//				"http://dbpedia.org/resource/Bobby_Hutcherson",
				//				"http://dbpedia.org/resource/Domain_Name_System",
				//				"http://dbpedia.org/resource/Universal_Product_Code",
				//				"http://dbpedia.org/resource/Firefox",
				//				"http://dbpedia.org/resource/Since_We%27ve_Become_Translucent",
				//				"http://dbpedia.org/resource/Mudhoney",
				//				"http://dbpedia.org/resource/RDFa",
				//				"http://dbpedia.org/resource/Bradford_Cox",
				//				"http://dbpedia.org/resource/Hawaii-Aleutian_Time_Zone",
				//				"http://dbpedia.org/resource/Namma_Metro",
				//				"http://dbpedia.org/resource/India",
				//				"http://dbpedia.org/resource/IPhone",
				//				"http://dbpedia.org/resource/Accelerometer",
				//				"http://dbpedia.org/resource/Open_data",
				//				"http://dbpedia.org/resource/Mobile%2C_Alabama",
				//				"http://dbpedia.org/resource/Uniform_Resource_Locator",
				//				"http://dbpedia.org/resource/Text_box",
				//				"http://dbpedia.org/resource/LaTeX",
				//				"http://dbpedia.org/resource/Doctor_of_Philosophy",
				//				"http://dbpedia.org/resource/Information_graphics",
				//				"http://dbpedia.org/resource/Stanford_University",
				//				"http://dbpedia.org/resource/Machine_learning",
				//				"http://dbpedia.org/resource/E-learning",
				//				"http://dbpedia.org/resource/Apache_Hadoop",
				//				"http://dbpedia.org/resource/Jello_Biafra",
				//				"http://dbpedia.org/resource/Pope"
				);

		// Generate the connections matrix out of the list of entities 
		Map<String, Map<String,String>> matrix = new HashMap<String, Map<String,String>>();
		matrix = matrixClass.generateConnectionMatrix(entities);

		// Generate a Graph out of the connections matrix ("adjacency matrix")
		for (Iterator iterator = matrix.keySet().iterator(); iterator.hasNext();) {
			String row = (String) iterator.next();
			Map<String,String> cols = matrix.get(row);
			for (Iterator iterator2 = cols.keySet().iterator(); iterator2.hasNext();) {
				String col = (String) iterator2.next();
				String value = cols.get(col);
				//if the value of the connection between two entities is 1 (1 hop) then the weight of the edge is 0.5
				if (value.equals("1")){
					graphInstance.addEdge(0.5, row, col);
					System.out.println("0.5 edge!");
				} 
				//if the value of the connection between two entities is 2 (2 hops) then the weight of the edge is 0.3
				if (value.equals("2")){
					graphInstance.addEdge(0.35, row, col);
				} 
			}
		}

		// Compute Page Rank or other measures on the graph 
		graphInstance.computePageRank();
		graphInstance.computeEigenvectorCentrality();
		graphInstance.computeDegree();


		// Exporting the graph in GraphML format to a file (can then be imported by tools like Gephi etc.)
		GraphMLWriter<MyNode, MyLink> graphWriter =	new GraphMLWriter<MyNode, MyLink>();
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("JUNGgraphExport.graphml")));

			final AbstractLayout layout = new StaticLayout(graph);
			graphWriter.addVertexData("x", null, "0",
					new Transformer<MyNode, String>() {
				public String transform(MyNode v) {
					return Double.toString(layout.getX(v));
				}
			}
					);
			graphWriter.addVertexData("y", null, "0",
					new Transformer<MyNode, String>() {
				public String transform(MyNode v) {
					return Double.toString(layout.getY(v));
				}
			}
					);
			graphWriter.setEdgeDescriptions(new Transformer<MyLink, String>() {
				public String transform(MyLink e) {
					return Double.toString(e.getWeight());
				}
			});
			graphWriter.save(graph, out);

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		//		// Visualise the graph 
		//		VisualizationImageServer vs =
		//				new VisualizationImageServer(
		//						new CircleLayout(graph), new Dimension(550, 550));
		//		JFrame frame = new JFrame();
		//		frame.getContentPane().add(vs);
		//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		frame.pack();
		//		frame.setVisible(true);
	}



}


class MyNode {
	private String id; // good coding practice would have this as private
	public MyNode(String id) {
		this.id = id;
	}
	public String toString() { // Always a good idea for debugging
		return id; // JUNG2 makes good use of these.
	}
}

class MyLink {
	public static int edgeCount = 0;
	public double weight; // should be private for good practice
	int id;
	public MyLink(double weight) {
		this.id = edgeCount++; // This is defined in the outer class.
		this.weight = weight;
	}
	public String toString() { // Always good for debugging
		return "E"+id;
	}
	public double getWeight() {
		return weight;
	}
}
