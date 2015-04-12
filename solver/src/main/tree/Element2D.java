package main.tree;

import java.util.HashMap;
import java.util.Map;

import main.utils.Rectangle;

public class Element2D extends Element {

	public Rectangle rectangle;
	
	public Map<DOF, double[]> localBasisFunctions = new HashMap<>();

	public Element2D(int id) {
		super(id);
	}

	public void addFunction(DOF dof, double d0, double d1, double d2, double d3){
		
	}
	

}
