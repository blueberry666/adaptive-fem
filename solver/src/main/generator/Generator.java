package main.generator;

import main.utils.Rectangle;

public class Generator {
	
	public Part[] breakHorizontal(Part parent){
		Rectangle[] broken = parent.rectangle.breakHorizontal();
		
		Part topPart = new Part(1);
		Part bottomPart = new Part(2);
		topPart.rectangle = broken[0];
		Neighborhood topPartN = doPart(topPart, parent, Direction.TOP);
		Edge top = parent.getEdge(Direction.TOP);
		top.setNeighborhood(Direction.BOTTOM, topPartN);
		topPart.setEdge(Direction.TOP,top);
		
		bottomPart.rectangle = broken[1];
		Neighborhood bottomPartN = doPart(bottomPart, parent, Direction.BOTTOM);
		Edge bottom = parent.getEdge(Direction.BOTTOM);
		bottom.setNeighborhood(Direction.TOP, bottomPartN);
		bottomPart.setEdge(Direction.BOTTOM,bottom);
		
		Edge parentLeft = parent.getEdge(Direction.LEFT);
		if(!(parentLeft.getNeighborhood(Direction.LEFT) instanceof Neighborhood.Empty)){
			parentLeft.setNeighborhood(
					Direction.RIGHT,
					new Neighborhood.TwoEdge(
							topPart.getEdge(Direction.LEFT), 
							bottomPart.getEdge(Direction.LEFT)));;
		}
		Edge parentRight = parent.edges[Direction.RIGHT.ordinal()];
		if(!(parentRight.getNeighborhood(Direction.RIGHT) instanceof Neighborhood.Empty)){
			parentRight.setNeighborhood(
					Direction.LEFT,
					new Neighborhood.TwoEdge(
							topPart.getEdge(Direction.RIGHT), 
							bottomPart.getEdge(Direction.RIGHT)));
		}

		Edge betweenEdge = new Edge();
		betweenEdge.setNeighborhood(Direction.TOP, topPartN);
		betweenEdge.setNeighborhood(Direction.BOTTOM, bottomPartN);
		topPart.setEdge(Direction.BOTTOM, betweenEdge);
		bottomPart.setEdge(Direction.TOP, betweenEdge);

		return new Part[]{topPart, bottomPart};
	}

	private Neighborhood doPart(Part part, Part parent, Direction topBottom) {
		Neighborhood partN = new Neighborhood.SinglePart(part);
		makeSide(part, parent, partN, Direction.LEFT,topBottom );		
		makeSide(part, parent, partN, Direction.RIGHT, topBottom);
		return partN;
	}

	private void makeSide(Part part, Part parent, Neighborhood partN,
			Direction leftOrRight, Direction topBottom) {
		Edge right = new Edge();
		Edge parentRgiht = parent.getEdge(leftOrRight);

		Neighborhood parentEdgeNeighborhood = parentRgiht.getNeighborhood(leftOrRight);
		if (parentEdgeNeighborhood instanceof Neighborhood.Empty) {
			right.setNeighborhood(leftOrRight, new Neighborhood.Empty());
		} else if (parentEdgeNeighborhood instanceof Neighborhood.TwoEdge) {
			part.setEdge(leftOrRight, ((Neighborhood.TwoEdge) parentEdgeNeighborhood).getEdge(topBottom));
			part.getEdge(leftOrRight).setNeighborhood(
					Direction.opposite(leftOrRight),
					new Neighborhood.SinglePart(part));
		} else if (parentEdgeNeighborhood instanceof Neighborhood.OneEdge) {
			throw new RuntimeException("You fucked up! You cannot do that! Only one level of breaking is available!");
		} else {
			right.setNeighborhood(leftOrRight, new Neighborhood.OneEdge(
					parentRgiht));
		}
		right.setNeighborhood(Direction.opposite(leftOrRight), partN);
		part.setEdge(leftOrRight,right);
	}

	

}
