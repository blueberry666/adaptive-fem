package main;
import java.util.List;
import java.util.Set;

import main.productions.productionFactory.RandomStuffFactory;
import main.scheduler.GraphScheduler;
import main.scheduler.Node;
import main.scheduler.NotSoDummyNode;
import main.scheduler.ProductionGraphBuilder;
import main.tree.TestTreeBuilder;
import main.tree.TreeInitializer;
import main.tree.Vertex;


public class Application {

	public static void main(String[] args) {

		TestTreeBuilder builder = new TestTreeBuilder();
		Vertex root = builder.buildTree(4);
		TreeInitializer.visit(root);
		builder.printTree("",root);
		
		ProductionGraphBuilder graphBuilder = new ProductionGraphBuilder(new RandomStuffFactory());
		
		
		GraphScheduler scheduler = new GraphScheduler();
		Set<? extends Node> graph = graphBuilder.makeGraph(root);
		List<List<Node>> scheduledNodes = scheduler.schedule(graph);
		Executor executor = new Executor();
		
		for(List<Node> nodes : scheduledNodes){
			System.out.println();
			executor.beginStage(nodes.size());
			for(Node n : nodes){
				System.out.print(n.getName() + "  ");
				executor.submitProduction(((NotSoDummyNode)n).getProduction());
				
			}
			executor.waitForEnd();
		}
		
		executor.shutdown();
		
	}

}
