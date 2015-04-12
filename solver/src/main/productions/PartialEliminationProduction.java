package main.productions;
import main.tree.Vertex;
import main.utils.MatrixUtil;



public class PartialEliminationProduction extends Production {

	public PartialEliminationProduction(Vertex vert) {
		super(vert);
	}

	@Override
	public void apply() {
		MatrixUtil.partiallyEliminate(vertex.A, vertex.b, vertex.eliminatedDofs);

	}

}
