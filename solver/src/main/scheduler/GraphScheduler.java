package main.scheduler;

import java.util.*;


public class GraphScheduler {
	
	Set<Node> usedNodes = new HashSet<>();
	
	
		
	public void findNodes(Set<Node> allNodes, Collection<? extends Node> startNodes){
        allNodes.addAll(startNodes);
        Queue<Node> q = new LinkedList<Node>(startNodes);
        while(!q.isEmpty()){
            for(Node n : q.poll().getInNodes()){
                if(!allNodes.contains(n)){
                    q.add(n);
                    allNodes.add(n);
                }
            }
        }

	}
	
	public List<List<Node>> schedule(Collection<? extends Node> starNodes){
		Set<Node> graph = new HashSet<>();
		findNodes(graph, starNodes);
        System.out.println("Graph size:" + graph.size());
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
