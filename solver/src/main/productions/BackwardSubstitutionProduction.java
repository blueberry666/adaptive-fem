package main.productions;

import java.util.Arrays;

import main.MatrixUtil;
import main.tree.DOF;
import main.tree.Vertex;

public class BackwardSubstitutionProduction extends Production{

	public BackwardSubstitutionProduction(Vertex vert) {
		super(vert);
	}

	@Override
	public void apply() {
		
		for(Vertex child : vertex.children){
			double [] x = new double[vertex.A.length];
			int i = 0;
			for(DOF d : child.getNotEliminatedDOFS()){
				int parentI = vertex.rowDofs.indexOf(d);
				x[i] = vertex.x[parentI];
				++i;
			}

			MatrixUtil.substitute(child.A, child.b, x, child.eliminatedDofs);
			child.x = Arrays.copyOf(child.b, child.b.length);
			
		}
		
	}

}
