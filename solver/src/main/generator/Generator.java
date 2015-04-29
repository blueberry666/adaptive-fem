package main.generator;

import main.generator.Neighborhood.Empty;
import main.generator.Neighborhood.SinglePart;
import main.utils.Rectangle;

public class Generator {
	
	public Part[] breakHorizontal(Part parent){
		Rectangle[] broken = parent.rectangle.breakHorizontal();
		
		Part topPart = new Part(1);
		Part bottomPart = new Part(2);
		topPart.rectangle = broken[0];
		topPart.edges[Direction.TOP.ordinal()] = parent.edges[Direction.TOP.ordinal()];
		Neighborhood topPartN = doPart(topPart, parent, Direction.TOP);
		Edge top = parent.edges[Direction.TOP.ordinal()];
		top.setNeighborhood(Direction.BOTTOM, topPartN);
		topPart.edges[Direction.TOP.ordinal()] = top;
		
		bottomPart.rectangle = broken[1];
		bottomPart.edges[Direction.BOTTOM.ordinal()] = parent.edges[Direction.BOTTOM.ordinal()];
		Neighborhood bottomPartN = doPart(bottomPart, parent, Direction.BOTTOM);
		Edge bottom = parent.edges[Direction.BOTTOM.ordinal()];
		bottom.setNeighborhood(Direction.TOP, bottomPartN);
		bottomPart.edges[Direction.BOTTOM.ordinal()] = bottom;
		
		Edge parentLeft = parent.edges[Direction.LEFT.ordinal()];
		if(!(parentLeft.getNeighborhood(Direction.LEFT) instanceof Neighborhood.Empty)){
			parentLeft.setNeighborhood(
					Direction.RIGHT,
					new Neighborhood.TwoEdge(
							topPart.edges[Direction.LEFT.ordinal()], 
							bottomPart.edges[Direction.LEFT.ordinal()]));
		}
		Edge parentRight = parent.edges[Direction.RIGHT.ordinal()];
		if(!(parentRight.getNeighborhood(Direction.RIGHT) instanceof Neighborhood.Empty)){
			parentRight.setNeighborhood(
					Direction.LEFT,
					new Neighborhood.TwoEdge(
							topPart.edges[Direction.RIGHT.ordinal()], 
							bottomPart.edges[Direction.RIGHT.ordinal()]));
		}
		
		Edge betweenEdge = new Edge();
		betweenEdge.setNeighborhood(Direction.TOP, topPartN);
		betweenEdge.setNeighborhood(Direction.BOTTOM, bottomPartN);
		topPart.edges[Direction.BOTTOM.ordinal()] = betweenEdge;
		bottomPart.edges[Direction.TOP.ordinal()] = betweenEdge;
		
		return new Part[]{topPart, bottomPart};
	}

	private Neighborhood doPart(Part part, Part parent, Direction topBottom) {
		Neighborhood partN = new Neighborhood.SinglePart(part);
		makeSide(part, parent, partN, Direction.LEFT,topBottom );		
		makeSide(part, parent, partN, Direction.RIGHT, topBottom);
		return partN;
	}

	private void makeSide(Part part, Part parent, Neighborhood partN, Direction leftOrRight, Direction topBottom) {
		Edge right = new Edge();
		Edge parentRgiht = parent.edges[leftOrRight.ordinal()];

		if(parentRgiht.getNeighborhood(leftOrRight) instanceof Neighborhood.Empty){
			right.setNeighborhood(leftOrRight, new Neighborhood.Empty());
		}else if(parentRgiht.getNeighborhood(leftOrRight) instanceof Neighborhood.TwoEdge){
			part.edges[leftOrRight.ordinal()] = ((Neighborhood.TwoEdge)parentRgiht.getNeighborhood(leftOrRight)).getEdge(topBottom);
		}
		else{
			right.setNeighborhood(leftOrRight, new Neighborhood.OneEdge(parentRgiht));
		}
		right.setNeighborhood(Direction.opposite(leftOrRight), partN);
		part.edges[leftOrRight.ordinal()] = right;
	}

	

}
