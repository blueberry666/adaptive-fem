package main.productions.productionFactory;

import main.MatrixUtil;
import main.productions.Production;
import main.tree.Vertex;

public class RandomStuffFactory implements ProductionFactory {

	@Override
	public Production makeProduction(Vertex vertex) {
		
		return new Production(vertex) {
			
			@Override
			public void apply() {
				vertex.A = MatrixUtil.generateRandomMatrix(vertex.A.length, vertex.A[0].length);
				vertex.b = MatrixUtil.generateRandomVector(vertex.b.length);
				
			}
		};
	}

}
