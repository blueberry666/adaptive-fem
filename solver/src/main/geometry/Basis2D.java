package main.geometry;

import java.util.Collection;
import java.util.Map;

import main.tree.DOF;
import main.tree.Element2D;
import main.utils.Rectangle;


public class Basis2D {

	public static ShapeFunction[] shapeFunctions = ShapeFunction.values();
	
	
	public static double normalize(double x0, double x1, double x){
		return (x-x0)/(x1-x0);
	}
	
	private static double evaluate(double x, double y, Element2D element, Map<DOF, Double> map){
		double ret = 0;
		for(DOF dof: element.dofs){
			double [] functions = element.localBasisFunctions.get(dof);
			double bi = 0;
			for(int i=0;i<3;++i){
				Rectangle r = element.rectangle;
				//a_i*P_i
				bi += shapeFunctions[i].eval(normalize(r.x0,r.x1, x), normalize(r.y0, r.y1, y))*functions[i];
			}
			
			ret += map.get(dof)*bi;
		}
		
		return ret;
		
	}
	
	private static Element2D findElementForPoint(double x, double y, Collection<Element2D> elemList){
		
		for(Element2D e : elemList){
			if(e.rectangle.isInRectangle(x, y)){
				return e;
			}
		}
		return null;
	}
	
	public static double evaluate(double x, double y, Collection<Element2D> elemList, Map<DOF,Double> dofMap){
		return evaluate(x, y, findElementForPoint(x, y, elemList), dofMap);
	}
	
	
}
