package main.generator;

public interface Neighborhood {

	class Empty implements Neighborhood {

		public Empty() {

		}

		@Override
		public String toString() {
			return "Empty";
		}

	}

	class SinglePart implements Neighborhood {

		public Part part;

		public SinglePart(Part p) {
			part = p;
		}

		@Override
		public String toString() {
			return String.format("SinglePart(id=%d)", part.id);
		}

	}

	class OneEdge implements Neighborhood {

		public Edge edge;

		public OneEdge(Edge e) {
			edge = e;
		}

		@Override
		public String toString() {
			return String.format("OneEdge(id=%d)", edge.hashCode());
		}

	}

	class TwoEdge implements Neighborhood {

		public Edge topOrRight;
		public Edge bottomOrLeft;

		public TwoEdge(Edge topRight, Edge bottomLeft) {
			topOrRight = topRight;
			bottomOrLeft = bottomLeft;
		}

		public Edge getEdge(Direction d) {
			switch (d) {
			case TOP:
			case RIGHT:
				return topOrRight;
			case BOTTOM:
			case LEFT:
				return bottomOrLeft;
			}
			return null;

		}

		@Override
		public String toString() {
			return String.format("TwoEdges(topright=%d, leftbottom=%d)",
					topOrRight.hashCode(), bottomOrLeft.hashCode());
		}
	}
}
