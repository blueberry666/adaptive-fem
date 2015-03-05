package main;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.scheduler.DummyNode;
import main.scheduler.GraphScheduler;
import main.scheduler.Node;
import main.tree.TestTreeBuilder;
import main.tree.TreeInitializer;
import main.tree.Vertex;


public class Application {

	public static void main(String[] args) {

		TestTreeBuilder builder = new TestTreeBuilder();
		Vertex root = builder.buildTree(4);
		TreeInitializer.visit(root);
		builder.printTree("",root);
		
		DummyNode n1 = new DummyNode("1");
		DummyNode n2 = new DummyNode("2");
		DummyNode n3 = new DummyNode("3");
		DummyNode n4 = new DummyNode("4");
		DummyNode n5 = new DummyNode("5");
		DummyNode n6 = new DummyNode("6");
		n2.getInNodes().add(n1);
		n4.getInNodes().addAll(Arrays.asList(n2,n3));
		n5.getInNodes().add(n4);
		n6.getInNodes().add(n4);
		GraphScheduler scheduler = new GraphScheduler();
		Set<Node> graph = new HashSet<>();
		scheduler.findNodes(graph, Arrays.asList(n5,n6));
		List<List<Node>> scheduledNodes = scheduler.schedule(graph);
		for(List<Node> nodes : scheduledNodes){
			System.out.println();
			for(Node n : nodes){
				System.out.print(n.getName() + "  ");
			}
		}
		
	}

}
