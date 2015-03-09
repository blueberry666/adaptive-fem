package main.productions;

import java.util.Arrays;

import main.MatrixUtil;
import main.tree.DOF;
import main.tree.Vertex;

public class BackwardSubstitutionProduction extends Production{

	public BackwardSubstitutionProduction(Vertex vert) {
		super(vert);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply(Vertex vert) {
		
		for(Vertex child : vert.children){
			double [] x = new double[vert.A.length];
			int i = 0;
			for(DOF d : child.getNotEliminatedDOFS()){
				int parentI = vert.rowDofs.indexOf(d);
				x[i] = vert.x[parentI];
				++i;
			}

			MatrixUtil.substitute(child.A, child.b, x, child.eliminatedDofs);
			child.x = Arrays.copyOf(child.b, child.b.length);
			
		}
		
	}

}
