package main.scheduler;

import java.util.*;

public class GraphScheduler {

	Set<Node> usedNodes = new HashSet<>();

	public void findNodes(Set<Node> allNodes,
			Collection<? extends Node> startNodes) {
		Queue<Node> q = new ArrayDeque<>(startNodes);
		allNodes.addAll(startNodes);
		while (!q.isEmpty()) {
			Node node = q.poll();
			for (Node dep : node.getInNodes()) {
				if (allNodes.add(dep)) {
					q.add(dep);
				}
			}
		}
	}

	private Map<Node, List<Node>> buildReverse(Set<Node> nodes) {
		Map<Node, List<Node>> rev = new HashMap<>();
		for (Node node : nodes) {
			rev.put(node, new ArrayList<>(4));
		}
		for (Node node : nodes) {
			for (Node dep : node.getInNodes()) {
				rev.get(dep).add(node);
			}
		}
		return rev;
	}

	private List<Node> findFirstLayer(Set<Node> nodes) {
		List<Node> layer = new ArrayList<>();
		for (Node node : nodes) {
			if (node.getInNodes().isEmpty()) {
				layer.add(node);
			}
		}
		return layer;
	}

	public List<List<Node>> schedule(Collection<? extends Node> starNodes) {
		Set<Node> graph = new HashSet<>();
		findNodes(graph, starNodes);
		Map<Node, List<Node>> rev = buildReverse(graph);
		List<List<Node>> groups = new LinkedList<>();
		List<Node> layer = findFirstLayer(graph);
		graph.removeAll(layer);
		groups.add(layer);
		do {
			List<Node> buffer = new ArrayList<>();
			for (Node node : layer) {
				for (Node next : rev.get(node)) {
					next.getInNodes().remove(node);
					if (next.getInNodes().isEmpty()) {
						buffer.add(next);
					}
				}
			}
			layer = buffer;
			if (graph.size() == layer.size()) {
				graph.clear();
			} else {
				graph.removeAll(layer);
			}
			groups.add(layer);
		} while (!graph.isEmpty());

		return groups;
	}
}