package main.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.productions.Production;

public class NotSoDummyNode implements Node {
	
	
	private Production production;
	private List<Node> inNodes = new ArrayList<>();
	private String name;
	
	public NotSoDummyNode(String name, Production prod){
		this.name = name;
		this.production = prod;
	}

	@Override
	public Collection<Node> getInNodes() {
		return inNodes;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void addInNode(Node node){
		inNodes.add(node);
	}

	public Production getProduction() {
		return production;
	}



}
