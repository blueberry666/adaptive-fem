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
	private int dofID = 0;
	private int elemID = 0;
	
	public DOFGenerator(Collection<Part> grid){
		
		for(Part parent : grid){
			Map<DOF,double[]> dofMap = new HashMap<>();
			for(Corner c : Corner.values()){
				Point wtfPoint = null;
				Direction[] directions = Corner.getDirections(c);
				boolean free = true;
				for(Direction d : directions){
					Neighborhood n = parent.getEdge(d).getNeighborhood(d);
					if(n instanceof Neighborhood.OneEdge){
						Neighborhood.OneEdge oneEdge = (Neighborhood.OneEdge)n;
						Neighborhood.SinglePart singlePart = (Neighborhood.SinglePart)oneEdge.edge.getNeighborhood(d);
						Part sPart = singlePart.part;
						Point[] points = getEdgeVertices(sPart.rectangle, Direction.opposite(d));
						wtfPoint = parent.rectangle.getPoint(c);
						if(wtfPoint.equals(points[0]) || wtfPoint.equals(points[1])){
							for(Point point : points){
								DOF dof = getDof(point);
								getDofArray(dofMap, dof)[c.ordinal()] = 0.5;
							}
							free = false;
							
						}

					}
				}
				if(free){
					DOF dof = getDof(wtfPoint);
					getDofArray(dofMap, dof)[c.ordinal()] = 1;
				}
				
			}
			Element2D elem = new Element2D(getNextElemId());
			elem.rectangle = parent.rectangle;
			for(DOF d : dofMap.keySet()){
				elem.addDof(d);
				elem.localBasisFunctions.put(d, dofMap.get(d));
			}
			elementToPart.put(parent, elem);
		}
			
	}
	
	private DOF getDof(Point p){
		DOF d = DOFtoPoint.get(p);
		if(d == null){
			d = new DOF(getNextDofId());
			DOFtoPoint.put(p, d);
		}
		return d;
		
	}
	
	private double[] getDofArray(Map<DOF, double[]> map, DOF d){
		double [] tab = map.get(d);
		if(tab == null){
			tab = new double[4];
			map.put(d, tab);
		}
		return tab;
		
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
	
	private int getNextDofId(){
		return dofID++;
	}
	
	private int getNextElemId(){
		return elemID++;
	}
}
