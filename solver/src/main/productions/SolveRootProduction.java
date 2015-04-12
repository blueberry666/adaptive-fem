package main.productions;

import main.tree.Vertex;
import main.utils.MatrixUtil;

public class SolveRootProduction extends Production {

	public SolveRootProduction(Vertex vert) {
		super(vert);
	}

	@Override
	public void apply() {
		MatrixUtil.printVector(vertex.b);
		System.out.println();
		vertex.x = MatrixUtil.gaussianElimination(vertex.A, vertex.b);

		System.out.println("root x: ");
		MatrixUtil.printVector(vertex.x);
		System.out.println();

	}

}
