package main.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.tree.DOF;
import main.tree.Element2D;
import main.utils.CutPoint;
import main.utils.CutRectangle;

public class DOFGenerator {
	
	public Map<Part, Element2D> elementToPart = new HashMap<>();
	public Map<CutPoint, DOF> DOFtoPoint = new HashMap<>();
	private int dofID = 0;
	private int elemID = 0;
	
	public DOFGenerator(Collection<Part> grid){
		
		for(Part parent : grid){
			Map<DOF,double[]> dofMap = new HashMap<>();
			for(Corner c : Corner.values()){
				CutPoint cornerPoint = null;
				Direction[] directions = Corner.getDirections(c);
				boolean free = true;
				for(Direction d : directions){
					Neighborhood n = parent.getEdge(d).getNeighborhood(d);
					cornerPoint = parent.rectangle.getPoint(c);
					if(n instanceof Neighborhood.OneEdge){
						Neighborhood.OneEdge oneEdge = (Neighborhood.OneEdge)n;
						Neighborhood.SinglePart singlePart = (Neighborhood.SinglePart)oneEdge.edge.getNeighborhood(d);
						Part sPart = singlePart.part;
						CutPoint[] points = getEdgeVertices(sPart.rectangle, Direction.opposite(d));
						if(!cornerPoint.equals(points[0]) && !cornerPoint.equals(points[1])){
							for(CutPoint point : points){
								DOF dof = getDof(point);
								getDofArray(dofMap, dof)[c.ordinal()] = 0.5;
							}
							free = false;
							
						}

					}
				}
				if(free){
					DOF dof = getDof(cornerPoint);
					getDofArray(dofMap, dof)[c.ordinal()] = 1;
				}
				
			}
			Element2D elem = new Element2D(getNextElemId());
			elem.rectangle = parent.rectangle.toRectangle();
			for(DOF d : dofMap.keySet()){
				elem.addDof(d);
				elem.localBasisFunctions.put(d, dofMap.get(d));
			}
			elementToPart.put(parent, elem);
		}
			
	}
	
	private DOF getDof(CutPoint p){
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

	private CutPoint[] getEdgeVertices(CutRectangle r, Direction d){
		switch (d) {
		case LEFT:
			return new CutPoint[] { r.getPoint(Corner.LB), r.getPoint(Corner.LT) };
		case RIGHT:
			return new CutPoint[] { r.getPoint(Corner.RB), r.getPoint(Corner.RT) };		
		case TOP:
			return new CutPoint[] { r.getPoint(Corner.LT), r.getPoint(Corner.RT) };
		case BOTTOM:
			return new CutPoint[] { r.getPoint(Corner.LB), r.getPoint(Corner.RB) };

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
