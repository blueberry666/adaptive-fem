package main.utils;

import main.generator.Part;
import main.scheduler.SmartNode;
import main.tree.Vertex;

public class VertexData {
	
	public Vertex vertex;
	public SmartNode parent;
	public Part part;
	
	public VertexData(Vertex v, SmartNode n){
		vertex = v;
		parent = n;
	}
	
	public VertexData(Vertex v, Part part){
		this.part = part;
		this.vertex = v;
	}

}
