package main.utils;

import main.generator.Part;
import main.scheduler.NotSoDummyNode;
import main.tree.Vertex;

public class ClassyClass {
	
	public Vertex vertex;
	public NotSoDummyNode parent;
	public Part part;
	
	public ClassyClass(Vertex v, NotSoDummyNode n){
		vertex = v;
		parent = n;
	}
	
	public ClassyClass(Vertex v, Part part){
		this.part = part;
		this.vertex = v;
	}

}
