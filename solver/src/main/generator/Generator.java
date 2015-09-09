package main.generator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import main.tree.Element2D;
import main.tree.Vertex;
import main.utils.ClassyClass;
import main.utils.Cut;
import main.utils.CutRectangle;

public class Generator {

	private int nextId;
	private Part root;

	public Generator(double x0, double x1, double y0, double y1) {
		Part root = new Part(getNextId());
		root.rectangle = new CutRectangle(Cut.root(x0, x1), Cut.root(y0, y1));
		this.root = root;
		for (Direction d : Direction.values()) {
			Edge e = new Edge();
			e.setNeighborhood(d, new Neighborhood.Empty());
			e.setNeighborhood(Direction.opposite(d),
					new Neighborhood.SinglePart(root));
			root.setEdge(d, e);
		}
	}
	
	public Vertex buildEliminationTree() {
		List<Part> partLeaves = new ArrayList<>();
		getLeaves(root, partLeaves);
		DOFGenerator dofGenerator = new DOFGenerator(partLeaves);
		return elementsToVertex(root, dofGenerator.elementToPart);
	}

	private Vertex elementsToVertex(Part firstPart, Map<Part, Element2D> map) {

		Vertex rootVertex = new Vertex(map.get(firstPart));
		Queue<ClassyClass> q = new ArrayDeque<>();
		
		for (Part p : firstPart.children) {
			q.add(new ClassyClass(rootVertex, p));
		}
		while (!q.isEmpty()){
			ClassyClass classyy = q.poll();
            Part part = classyy.part;
            Vertex parent = classyy.vertex;
            Vertex v = new Vertex(map.get(part));
            parent.children.add(v);

            for (Part p : part.children) {
                   q.add(new ClassyClass(v, p));
            }
     }
		return rootVertex;

	}
	
	public Part[] breakPart(List<Integer> path, BreakType breakType, Part root) {
		Part parent = getPartByPath(path, root);
		if (!parent.children.isEmpty()) {
			throw new RuntimeException("Part already broken!!!");
		}
		Part[] broken = null;
		switch (breakType) {
		case CROSS:
			broken = breakCross(parent);
			break;
		case HORIZONTAL:
			broken = breakHorizontal(parent);
			break;
		case VERTICAL:
			broken = breakVertical(parent);
			break;
		}
		parent.children.addAll(Arrays.asList(broken));
		return broken;
	}

	private Part getPartByPath(List<Integer> path, Part root) {
		if (path.isEmpty()) {
			return root;
		}
		Part part = root;
		for (Integer i : path) {
			part = part.children.get(i);
		}
		return part;
	}

	// topLeft, topRight, bottomLeft, bottomRight
	private Part[] breakCross(Part parent) {
		Part[] brokenH = breakHorizontal(parent);
		List<Part> result = new ArrayList<>();
		result.addAll(Arrays.asList(breakVertical(brokenH[0])));
		result.addAll(Arrays.asList(breakVertical(brokenH[1])));
		return result.toArray(new Part[4]);

	}

	// top bottom
	private Part[] breakHorizontal(Part parent) {
		CutRectangle[] broken = parent.rectangle.breakHorizontal();

		Part topPart = new Part(getNextId());
		Part bottomPart = new Part(getNextId());
		topPart.rectangle = broken[1];
		Neighborhood topPartN = doPart(topPart, parent, Direction.LEFT,
				Direction.TOP);
		Edge top = parent.getEdge(Direction.TOP);
		top.setNeighborhood(Direction.BOTTOM, topPartN);
		topPart.setEdge(Direction.TOP, top);

		bottomPart.rectangle = broken[0];
		Neighborhood bottomPartN = doPart(bottomPart, parent, Direction.LEFT,
				Direction.BOTTOM);
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

	// left right
	private Part[] breakVertical(Part parent) {
		CutRectangle[] broken = parent.rectangle.breakVertical();
		Part leftPart = new Part(getNextId());
		Part rightPart = new Part(getNextId());
		leftPart.rectangle = broken[0];
		rightPart.rectangle = broken[1];

		Neighborhood leftPartN = doPart(leftPart, parent, Direction.TOP,
				Direction.LEFT);
		Edge left = parent.getEdge(Direction.LEFT);
		left.setNeighborhood(Direction.RIGHT, leftPartN);
		leftPart.setEdge(Direction.LEFT, left);

		Neighborhood rightPartN = doPart(rightPart, parent, Direction.TOP,
				Direction.RIGHT);
		Edge right = parent.getEdge(Direction.RIGHT);
		right.setNeighborhood(Direction.LEFT, rightPartN);
		rightPart.setEdge(Direction.RIGHT, right);

		addTwoEdgeNeighborhood(parent, rightPart, leftPart, Direction.TOP);
		addTwoEdgeNeighborhood(parent, rightPart, leftPart, Direction.BOTTOM);

		Edge betweenEdge = new Edge();
		betweenEdge.setNeighborhood(Direction.LEFT, leftPartN);
		betweenEdge.setNeighborhood(Direction.RIGHT, rightPartN);
		leftPart.setEdge(Direction.RIGHT, betweenEdge);
		rightPart.setEdge(Direction.LEFT, betweenEdge);

		return new Part[] { leftPart, rightPart };

	}

	private void addTwoEdgeNeighborhood(Part parent, Part topRight,
			Part bottomLeft, Direction direction) {
		Edge parentEdge = parent.getEdge(direction);
		if (parentEdge.getNeighborhood(direction) instanceof Neighborhood.SinglePart) {
			parentEdge.setNeighborhood(Direction.opposite(direction),
					new Neighborhood.TwoEdge(topRight.getEdge(direction),
							bottomLeft.getEdge(direction)));
		}
	}

	private Neighborhood doPart(Part part, Part parent, Direction d1,
			Direction topBottom) {
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

			edge = ((Neighborhood.TwoEdge) parentEdgeNeighborhood)
					.getEdge(topBottom);
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

	public int getNextId() {
		return nextId++;
	}

	public void getLeaves(Part rooty, List<Part> leaves) {
		Queue <Part> q = new ArrayDeque<>();
		q.add(rooty);
		while(!q.isEmpty()){
			Part root = q.poll();
			if (root.children.isEmpty()) {
				leaves.add(root);
			} else {
				for (Part p : root.children) {
					q.add(p);
				}
			}
		}
		
//		System.out.println("after getLeaves");
		
	}

	public List<Part> getLeaves() {
		List<Part> leaves = new ArrayList<>();
		getLeaves(root, leaves);
		return leaves;

	}
	
	public Part getRoot(){
		return root;
	}
}
