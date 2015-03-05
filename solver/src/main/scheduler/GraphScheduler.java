package main.scheduler;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class GraphScheduler {
	
	Set<Node> usedNodes = new HashSet<>();
	
	
		
	public void findNodes(Set<Node> allNodes, Collection<Node> startNodes){
		allNodes.addAll(startNodes);
		for(Node node : startNodes){
			findNodes(allNodes,node.getInNodes());
		}

	}
	
	public List<List<Node>> schedule(Collection<Node> graph){
		List<List<Node>> groups = new LinkedList<>();
		while (!graph.isEmpty()) {
			List<Node> nodes = new LinkedList<>();
			for (Node n : graph) {
				if (canDoStuff(n)) {
					nodes.add(n);
				}

			}
			usedNodes.addAll(nodes);
			graph.removeAll(nodes);
			groups.add(nodes);
		}
		
		return groups;
	}
	
	
	private boolean canDoStuff(Node node){
		if(node.getInNodes().isEmpty()){
			return true;
		}
		for(Node n : node.getInNodes()){
			if(!usedNodes.contains(n)){
				return false;
			}
		}
		return true;
	}

}
