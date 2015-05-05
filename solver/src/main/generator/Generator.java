package main.generator;

import main.utils.Rectangle;

public class Generator {
	
	private int nextId;

	public Part[] breakHorizontal(Part parent) {
		Rectangle[] broken = parent.rectangle.breakHorizontal();

		Part topPart = new Part(getNextId());
		Part bottomPart = new Part(getNextId());
		topPart.rectangle = broken[1];
		Neighborhood topPartN = doPart(topPart, parent,Direction.LEFT, Direction.TOP);
		Edge top = parent.getEdge(Direction.TOP);
		top.setNeighborhood(Direction.BOTTOM, topPartN);
		topPart.setEdge(Direction.TOP, top);

		bottomPart.rectangle = broken[0];
		Neighborhood bottomPartN = doPart(bottomPart, parent,Direction.LEFT, Direction.BOTTOM);
		Edge bottom = parent.getEdge(Direction.BOTTOM);
		bottom.setNeighborhood(Direction.TOP, bottomPartN);
		bottomPart.setEdge(Direction.BOTTOM, bottom);

		addTwoEdgeNeighborhood(parent, topPart, bottomPart, Direction.LEFT);
		addTwoEdgeNeighborhood(parent, topPart, bottomPart, Direction.RIGHT);

		Edge betweenEdge = new Edge();
		betweenEdge.setNeighborhood(Direction.TOP, topPartN);
		betweenEdge.setNeighborhood(Direction.BOTTOM, bottomPartN);
		topPart.setEdge(Direction.BOTTOM, betweenEdge);
		bottomPart.setEdge(Direction.TOP, betweenEdge);

		return new Part[] { topPart, bottomPart };
	}
	
	private Part[] breakVertical(Part parent){
		Rectangle [] broken = parent.rectangle.breakVertical();
		Part leftPart = new Part(getNextId());
		Part rightPart = new Part(getNextId());
		leftPart.rectangle = broken[0];
		rightPart.rectangle = broken[1];
		
		Neighborhood leftPartN = doPart(leftPart, parent,Direction.TOP, Direction.LEFT);
		Edge left = parent.getEdge(Direction.LEFT);
		left.setNeighborhood(Direction.RIGHT, leftPartN);
		leftPart.setEdge(Direction.LEFT, left);

		Neighborhood rightPartN = doPart(rightPart, parent,Direction.TOP, Direction.RIGHT);
		Edge right = parent.getEdge(Direction.RIGHT);
		right.setNeighborhood(Direction.LEFT, rightPartN);
		rightPart.setEdge(Direction.RIGHT, right);

		addTwoEdgeNeighborhood(parent, leftPart, rightPart, Direction.TOP);
		addTwoEdgeNeighborhood(parent, leftPart, rightPart, Direction.BOTTOM);

		Edge betweenEdge = new Edge();
		betweenEdge.setNeighborhood(Direction.LEFT, leftPartN);
		betweenEdge.setNeighborhood(Direction.RIGHT, rightPartN);
		leftPart.setEdge(Direction.RIGHT, betweenEdge);
		rightPart.setEdge(Direction.LEFT, betweenEdge);

		return new Part[] { rightPart, leftPart };
		
	}

	private void addTwoEdgeNeighborhood(Part parent, Part topPart,
			Part bottomPart, Direction direction) {
		Edge parentEdge = parent.getEdge(direction);
		if (parentEdge.getNeighborhood(direction) instanceof Neighborhood.SinglePart) {
			parentEdge.setNeighborhood(Direction.opposite(direction),
					new Neighborhood.TwoEdge(topPart.getEdge(direction),
							bottomPart.getEdge(direction)));
		}
	}

	private Neighborhood doPart(Part part, Part parent, Direction d1, Direction topBottom) {
		Neighborhood partN = new Neighborhood.SinglePart(part);
		makeSide(part, parent, partN, d1, topBottom);
		makeSide(part, parent, partN, Direction.opposite(d1), topBottom);
		return partN;
	}
	
	private void makeSide(Part part, Part parent, Neighborhood partN,
			Direction leftOrRight, Direction topBottom) {
		Edge edge = new Edge();
		Edge parentEdge = parent.getEdge(leftOrRight);

		Neighborhood parentEdgeNeighborhood = parentEdge
				.getNeighborhood(leftOrRight);
		if (parentEdgeNeighborhood instanceof Neighborhood.Empty) {
			edge.setNeighborhood(leftOrRight, new Neighborhood.Empty());
		} else if (parentEdgeNeighborhood instanceof Neighborhood.TwoEdge) {

			edge = ((Neighborhood.TwoEdge) parentEdgeNeighborhood).getEdge(topBottom);
		} else if (parentEdgeNeighborhood instanceof Neighborhood.OneEdge) {
			throw new RuntimeException(
					"You fucked up! You cannot do that! Only one level of breaking is available!");
		} else {
			edge.setNeighborhood(leftOrRight, new Neighborhood.OneEdge(
					parentEdge));
		}
		edge.setNeighborhood(Direction.opposite(leftOrRight), partN);
		part.setEdge(leftOrRight, edge);
	}
	
	public int getNextId(){
		return nextId++;
	}

}
