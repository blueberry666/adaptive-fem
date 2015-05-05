package main.generator;

import main.utils.Rectangle;

public class Part {
	
	public int id;
	public Rectangle rectangle;
	public Edge [] edges = new Edge[4];

	
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
           return String.format("Part[id=%d](x=[%f, %f], y=[%f, %f])", id, rectangle.x0, 
                         rectangle.x1, rectangle.y0, rectangle.y1);
    }
}
