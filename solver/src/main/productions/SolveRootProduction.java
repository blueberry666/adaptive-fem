package main.productions;
import main.MatrixUtil;
import main.tree.Vertex;




public class SolveRootProduction extends Production {

	public SolveRootProduction(Vertex vert) {
		super(vert);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply(Vertex vert) {
		// TODO Auto-generated method stubSystem.out.println("root b:");
		MatrixUtil.printVector(vert.b);
		System.out.println();
		vert.x = MatrixUtil.gaussianElimination(vert.A, vert.b);
		
		System.out.println("root x: ");
		MatrixUtil.printVector(vert.x);
		System.out.println();

	}

}
