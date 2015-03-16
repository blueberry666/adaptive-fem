package main.productions;

import main.MatrixUtil;
import main.tree.Vertex;

public class SolveRootProduction extends Production {

	public SolveRootProduction(Vertex vert) {
		super(vert);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stubSystem.out.println("root b:");
		MatrixUtil.printVector(vertex.b);
		System.out.println();
		vertex.x = MatrixUtil.gaussianElimination(vertex.A, vertex.b);

		System.out.println("root x: ");
		MatrixUtil.printVector(vertex.x);
		System.out.println();

	}

}
