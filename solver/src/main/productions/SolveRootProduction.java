package main.productions;

import main.tree.Vertex;
import main.utils.MatrixUtil;

public class SolveRootProduction extends Production {

	public SolveRootProduction(Vertex vert) {
		super(vert);
	}

	@Override
	public void apply() {
		vertex.x = MatrixUtil.gaussianElimination(vertex.A, vertex.b);

	}

}
