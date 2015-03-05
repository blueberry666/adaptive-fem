package main.scheduler;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class DummyNode implements Node {
	
	private List<Node> inNodes = new LinkedList<>();
	private String name;
	
	public DummyNode(String name){
		this.name = name;
	}

	@Override
	public Collection<Node> getInNodes() {
		return inNodes;
	}

	@Override
	public String getName() {
		return name;
	}
}
