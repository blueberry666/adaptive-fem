package main.generator;

import main.utils.Rectangle;

public class Part {
	
	public int id;
	public Rectangle rectangle;
	public Edge [] edges = new Edge[4];

	
	public Part(int id){
		this.id = id;
	}
}
