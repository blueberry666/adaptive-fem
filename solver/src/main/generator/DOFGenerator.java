package main.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.tree.DOF;
import main.tree.Element2D;
import main.utils.Point;
import main.utils.Rectangle;

public class DOFGenerator {
	
	public Map<Part, Element2D> elementToPart = new HashMap<>();
	public Map<Point, DOF> DOFtoPoint = new HashMap<>();
	
	public DOFGenerator(Collection<Part> grid){
		
		for(Part parent : grid){
			for(Corner c : Corner.values()){
				
				Direction[] directions = Corner.getDirections(c);
				for(Direction d : directions){
					Neighborhood n = parent.getEdge(d).getNeighborhood(d);
					if(n instanceof Neighborhood.OneEdge){
						Neighborhood.OneEdge oneEdge = (Neighborhood.OneEdge)n;
						Neighborhood.SinglePart singlePart = (Neighborhood.SinglePart)oneEdge.edge.getNeighborhood(d);
						Part sPart = singlePart.part;
						Point[] points = getEdgeVertices(sPart.rectangle, d);
						Point wtfPoint = parent.rectangle.getPoint(c);
						if(wtfPoint.equals(points[0]) || wtfPoint.equals(points[1])){
							
						}else{
							
						}

					}
				}
				
			}
		}
			
	}

	private Point[] getEdgeVertices(Rectangle r, Direction d){
		switch (d) {
		case LEFT:
			return new Point[] { r.getPoint(Corner.LB), r.getPoint(Corner.LT) };
		case RIGHT:
			return new Point[] { r.getPoint(Corner.RB), r.getPoint(Corner.RT) };		
		case TOP:
			return new Point[] { r.getPoint(Corner.LT), r.getPoint(Corner.RT) };
		case BOTTOM:
			return new Point[] { r.getPoint(Corner.LB), r.getPoint(Corner.RB) };

		}
		
		return null;
	}
}
