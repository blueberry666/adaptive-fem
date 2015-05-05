package main.generator;

import main.utils.Rectangle;

public class Generator {

	public Part[] breakHorizontal(Part parent) {
		Rectangle[] broken = parent.rectangle.breakHorizontal();

		Part topPart = new Part(1);
		Part bottomPart = new Part(2);
		topPart.rectangle = broken[1];
		Neighborhood topPartN = doPart(topPart, parent, Direction.TOP);
		Edge top = parent.getEdge(Direction.TOP);
		top.setNeighborhood(Direction.BOTTOM, topPartN);
		topPart.setEdge(Direction.TOP, top);

		bottomPart.rectangle = broken[0];
		Neighborhood bottomPartN = doPart(bottomPart, parent, Direction.BOTTOM);
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

	private void addTwoEdgeNeighborhood(Part parent, Part topPart,
			Part bottomPart, Direction direction) {
		Edge parentEdge = parent.getEdge(direction);
		if (parentEdge.getNeighborhood(direction) instanceof Neighborhood.SinglePart) {
			parentEdge.setNeighborhood(Direction.opposite(direction),
					new Neighborhood.TwoEdge(topPart.getEdge(direction),
							bottomPart.getEdge(direction)));
		}
	}

	private Neighborhood doPart(Part part, Part parent, Direction topBottom) {
		Neighborhood partN = new Neighborhood.SinglePart(part);
		makeSide(part, parent, partN, Direction.LEFT, topBottom);
		makeSide(part, parent, partN, Direction.RIGHT, topBottom);
		return partN;
	}

	private void makeSide(Part part, Part parent, Neighborhood partN,
			Direction leftOrRight, Direction topBottom) {
		Edge newEdge = new Edge();
		Edge parentEdge = parent.getEdge(leftOrRight);

		Neighborhood parentEdgeNeighborhood = parentEdge
				.getNeighborhood(leftOrRight);
		if (parentEdgeNeighborhood instanceof Neighborhood.Empty) {
			newEdge.setNeighborhood(leftOrRight, new Neighborhood.Empty());
		} else if (parentEdgeNeighborhood instanceof Neighborhood.TwoEdge) {
			part.setEdge(leftOrRight,
					((Neighborhood.TwoEdge) parentEdgeNeighborhood)
							.getEdge(topBottom));
			part.getEdge(leftOrRight).setNeighborhood(
					Direction.opposite(leftOrRight),
					new Neighborhood.SinglePart(part));
		} else if (parentEdgeNeighborhood instanceof Neighborhood.OneEdge) {
			throw new RuntimeException(
					"You fucked up! You cannot do that! Only one level of breaking is available!");
		} else {
			newEdge.setNeighborhood(leftOrRight, new Neighborhood.OneEdge(
					parentEdge));
		}
		newEdge.setNeighborhood(Direction.opposite(leftOrRight), partN);
		part.setEdge(leftOrRight, newEdge);
	}

}
