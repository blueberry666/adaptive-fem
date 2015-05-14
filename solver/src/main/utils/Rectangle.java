package main.utils;

import main.generator.Corner;
import main.generator.Direction;

public class Rectangle {
	
	public double x0;
	public double x1;
	public double y0;
	public double y1;
	
	public Rectangle(double x0, double x1, double y0, double y1){
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
	}
	
	public double getWidth(){
		return Math.abs(x1-x0);
	}
	
	public double getHeight(){
		return Math.abs(y1-y0);
	}
	
	public boolean isInRectangle(double x, double y) {
		return (x <= x1 && x >= x0) && (y <= y1 && y >= y0);
	}
	
	public Rectangle[] breakRectangle(){
		Rectangle [] rec = new Rectangle[4];
		double midX = (x0+x1)/2;
		double midY = (y0+y1)/2;
		rec[0] = new Rectangle(x0, midX, y0, midY);
		rec[1] = new Rectangle(midX, x1, y0, midY);
		rec[2] = new Rectangle(midX, x1, midY, y1);
		rec[3] = new Rectangle(x0,midX,midY, y1);
		return rec;

	}
	
	public Rectangle [] breakHorizontal(){
		Rectangle [] rec = new Rectangle[2];
		double midY = (y0+y1)/2;
		rec[0] = new Rectangle(x0, x1, y0, midY);
		rec[1] = new Rectangle(x0, x1, midY, y1);

		return rec;
	}
	
	public Rectangle [] breakVertical(){
		Rectangle [] rec = new Rectangle[2];
		double midX = (x0+x1)/2;
		rec[0] = new Rectangle(x0, midX, y0, y1);
		rec[1] = new Rectangle(midX, x1, y0, y1);

		return rec;
	}
	
	public Point getPoint(Corner c){
		switch(c){
		case LB:
			return new Point(x0,y0);
		case RB:
			return new Point(x1,y0);
		case LT:
			return new Point(x0,y1);
		case RT:
			return new Point(x1,y1);
		}
		return null;
	}
	
	public double getBound(Direction d){
		switch (d) {
		case LEFT:
			return x0;
		case RIGHT:
			return x1;
		case TOP:
			return y1;
		case BOTTOM:
			return y0;

		}
		return 0;
	}
	
	public double getArea(){
		return (x1-x0)*(y1-y0);
	}
	
	

}
