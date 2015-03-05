package main.productions;
import main.MatrixUtil;
import main.tree.Vertex;



public class PartialEliminationProduction extends Production {

	public PartialEliminationProduction(Vertex vert) {
		super(vert);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply(Vertex vert) {
		MatrixUtil.partiallyEliminate(vert.A, vert.b, vert.eliminatedDofs);

	}

}
