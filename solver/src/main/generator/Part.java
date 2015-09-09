package main.generator;

import java.util.ArrayList;
import java.util.List;

import main.utils.CutRectangle;

public class Part {
	
	public int id;
	public CutRectangle rectangle;
	public Edge [] edges = new Edge[4];
	public List<Part> children = new ArrayList<>();

	
	public Part(int id){
		this.id = id;
	}
	
	public Edge getEdge(Direction d){
		return edges[d.ordinal()];
	}
	
	public void setEdge(Direction d, Edge e){
		edges[d.ordinal()] = e;
	}
	
	@Override
    public String toString() {
           return String.format("Part[id=%d](x=[%f, %f], y=[%f, %f])", id, rectangle.x.left(), 
                         rectangle.x.right(), rectangle.y.left(), rectangle.y.right());
    }
}
