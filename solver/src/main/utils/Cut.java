package main.utils;


public class Cut {

	public enum Dir {
		LEFT(0), RIGHT(1);

		private int idx;

		Dir(int idx) {
			this.idx = idx;
		}

	}

	public static class Node {
		private Node[] children;

		private double a;
		private double b;
		private Cut edges[];

		private Node(Cut leftEdge, Cut rightEdge) {
			this.edges = new Cut[] { leftEdge, rightEdge };
		}

		public Node[] children() {
			if (children == null) {
				children = split();
			}
			return children;
		}

		public Node child(Dir dir) {
			return children()[dir.idx];
		}

		public Cut edge(Dir dir) {
			return edges[dir.idx];
		}

		public Cut middle() {
			return child(Dir.LEFT).edge(Dir.RIGHT);
		}

		private Node[] split() {
			double s = (a + b) / 2;
			Cut mid = new Cut(s);
			Node left = new Node(edge(Dir.LEFT), mid);
			Node right = new Node(mid, edge(Dir.RIGHT));
			return new Node[]{ left, right };
		}

		public Node leftChild() {
			return child(Dir.LEFT);
		}

		public Node rightChild() {
			return child(Dir.RIGHT);
		}

		public double width() {
			return right() - left();
		}

		public double left() {
			return leftEdge().value;
		}

		public double right() {
			return rightEdge().value;
		}

		public Cut leftEdge() {
			return edge(Dir.LEFT);
		}

		public Cut rightEdge() {
			return edge(Dir.RIGHT);
		}	
	}

	public static Node root(double a, double b) {
		return new Node(new Cut(a), new Cut(b));
	}

	private double value;

	private Cut(double value) {
		this.value = value;
	}

	public double value() {
		return value;
	}

}
