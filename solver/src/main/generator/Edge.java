package main.generator;

public class Edge {
	
	public Neighborhood topOrRight;
	public Neighborhood bottomOrLeft;
	
	
	public Edge(Neighborhood topOrRight, Neighborhood bottomOrLeft) {
		this.topOrRight = topOrRight;
        this.bottomOrLeft = bottomOrLeft;
	}
	
	public Edge(){}

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
			break;
		case TOP:
		case RIGHT:
			topOrRight = n;
		}
	}
	
	@Override
    public String toString() {
           return String.format("Edge[id=%d](topright=%s, botleft=%s)", hashCode(), topOrRight, bottomOrLeft);
    }

}
