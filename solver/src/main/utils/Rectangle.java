package main.utils;

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

}
