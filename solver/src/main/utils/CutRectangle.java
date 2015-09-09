package main.utils;

import main.generator.Corner;
import main.generator.Direction;

public class CutRectangle {

	public Cut.Node x;
	public Cut.Node y;

	public CutRectangle(Cut.Node x, Cut.Node y) {
		this.x = x;
		this.y = y;
	}

	public double getWidth() {
		return x.width();
	}

	public double getHeight() {
		return y.width();
	}

	public boolean isInRectangle(double x, double y) {
		return toRectangle().isInRectangle(x, y);
	}

	public CutRectangle[] breakRectangle() {
		CutRectangle[] rec = new CutRectangle[4];
		rec[0] = new CutRectangle(x.leftChild(), y.leftChild());
		rec[1] = new CutRectangle(x.rightChild(), y.leftChild());
		rec[2] = new CutRectangle(x.rightChild(), y.rightChild());
		rec[3] = new CutRectangle(x.leftChild(), y.rightChild());
		return rec;

	}

	public CutRectangle[] breakHorizontal() {
		CutRectangle[] rec = new CutRectangle[2];
		rec[0] = new CutRectangle(x, y.leftChild());
		rec[1] = new CutRectangle(x, y.rightChild());

		return rec;
	}

	public CutRectangle[] breakVertical() {
		CutRectangle[] rec = new CutRectangle[2];
		rec[0] = new CutRectangle(x.leftChild(), y);
		rec[1] = new CutRectangle(x.rightChild(), y);

		return rec;
	}

	public CutPoint getPoint(Corner c) {
		switch (c) {
		case LB:
			return new CutPoint(x.leftEdge(), y.leftEdge());
		case RB:
			return new CutPoint(x.rightEdge(), y.leftEdge());
		case LT:
			return new CutPoint(x.leftEdge(), y.rightEdge());
		case RT:
			return new CutPoint(x.rightEdge(), y.rightEdge());
		}
		return null;
	}

	public Cut getBound(Direction d) {
		switch (d) {
		case LEFT:
			return x.leftEdge();
		case RIGHT:
			return x.rightEdge();
		case TOP:
			return y.rightEdge();
		case BOTTOM:
			return y.leftEdge();
		}
		return null;
	}

	public double getArea() {
		return getWidth() * getHeight();
	}

	public Rectangle toRectangle() {
		double x0 = this.x.left();
		double x1 = this.x.right();
		double y0 = this.y.left();
		double y1 = this.y.right();
		return new Rectangle(x0, x1, y0, y1);
	}

}
