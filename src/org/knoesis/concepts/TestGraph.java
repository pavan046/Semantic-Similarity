package org.knoesis.concepts;


import java.awt.Dimension;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.importance.MarkovCentrality;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.scoring.DistanceCentralityScorer;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;


public class TestGraph {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		//		DirectedSparseGraph g = new DirectedSparseGraph();
		//		g.addVertex("Vertex1");
		//		g.addVertex("Vertex2");
		//		g.addVertex("Vertex3");
		//		g.addEdge("Edge1", "Vertex1", "Vertex2");
		//		g.addEdge("Edge2", "Vertex1", "Vertex3");
		//		g.addEdge("Edge3", "Vertex3", "Vertex1");
		//		g.addEdge("Edge4", "Vertex2", "Vertex1");
		//		VisualizationImageServer vs =
		//				new VisualizationImageServer(
		//						new CircleLayout(g), new Dimension(250, 250));
		//
		//		JFrame frame = new JFrame();
		//		frame.getContentPane().add(vs);
		//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		frame.pack();
		//		frame.setVisible(true);
		//
		//		System.out.println(g.inDegree("Vertex1"));
		//		System.out.println(g.outDegree("Vertex1"));

		//		MarkovCentrality ranker = new MarkovCentrality(g);
		//		ranker.evaluate();
		//		ranker.printRankings(true, true);

		DirectedSparseMultigraph g2 = new DirectedSparseMultigraph<MyNode2, MyLink2>();
		// Create some MyNode objects to use as vertices
		MyNode2 n1 = new MyNode2("1"); 
		MyNode2 n2 = new MyNode2("2"); 
		MyNode2 n3 = new MyNode2("3");
		MyNode2 n4 = new MyNode2("4");
		MyNode2 n5 = new MyNode2("5"); // note n1-n5 declared elsewhere.
		// Add some directed edges along with the vertices to the graph
		g2.addEdge(new MyLink2(0.5),n1, n2); 
		g2.addEdge(new MyLink2(0.1),n2, n3);
		g2.addEdge(new MyLink2(0.1), n3, n5);
		g2.addEdge(new MyLink2(0.1), n5, n4); 
		g2.addEdge(new MyLink2(0.5), n4, n2); 
		g2.addEdge(new MyLink2(0.1), n3, n1); 
		g2.addEdge(new MyLink2(0.1), n2, n5);


		Transformer<MyLink2, Double> wtTransformer = new Transformer<MyLink2,Double>() {
			public Double transform(MyLink2 link) {
				return link.weight;
			}
		};
		//		DijkstraShortestPath<MyNode,MyLink> alg = new DijkstraShortestPath(g2, wtTransformer);
		//		List<MyLink> l = alg.getPath(n1, n4);
		//		Number dist = alg.getDistance(n1, n4);
		//		System.out.println("The shortest path from " + n1 + " to " + n4 + " is: ");
		//		System.out.println(l.toString());
		//		System.out.println("and the length of the path is: " + dist);

		Set<MyNode2> set = new HashSet<MyNode2>();
		set.add(n4);

		PageRank ranker = new PageRank(g2, wtTransformer, 0.15);
		ranker.acceptDisconnectedGraph(true);
		ranker.evaluate();
		//		ranker.printRankings();


		System.out.println(ranker.getVertexScore(n1));
		System.out.println(ranker.getVertexScore(n2));
		System.out.println(ranker.getVertexScore(n3));
		System.out.println(ranker.getVertexScore(n4));
		System.out.println(ranker.getVertexScore(n5));



		VisualizationImageServer vs =
				new VisualizationImageServer(
						new CircleLayout(g2), new Dimension(250, 250));

		JFrame frame = new JFrame();
		frame.getContentPane().add(vs);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

}

class MyNode2 {
	private String id; // good coding practice would have this as private
	public MyNode2(String id) {
		this.id = id;
	}
	public String toString() { // Always a good idea for debugging
		return "V"+id; // JUNG2 makes good use of these.
	}
}

class MyLink2 {
	public static int edgeCount = 0;
	double weight; // should be private for good practice
	int id;
	public MyLink2(double weight) {
		this.id = edgeCount++; // This is defined in the outer class.
		this.weight = weight;
	}
	public String toString() { // Always good for debugging
		return "E"+id;
	}
}
