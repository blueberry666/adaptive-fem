package main.generator;

public class Edge {
	
	public Neighborhood topOrRight;
	public Neighborhood bottomOrLeft;
	
	
	public Neighborhood getNeighborhood(Direction d){
		switch (d) {
		case BOTTOM:
		case LEFT:
			return bottomOrLeft;
		case TOP:
		case RIGHT:
			return topOrRight;
		}
		return null;
	}
	
	public void setNeighborhood(Direction d, Neighborhood n){
		switch (d) {
		case BOTTOM:
		case LEFT:
			bottomOrLeft = n;
		case TOP:
		case RIGHT:
			topOrRight = n;
		}
	}

}
