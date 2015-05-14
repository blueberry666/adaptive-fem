package main.productions.productionFactory;

import main.productions.L2ProjectionProduction;
import main.productions.Production;
import main.tree.Vertex;

public class L2ProjectionFactory implements ProductionFactory {

	@Override
	public Production makeProduction(Vertex vertex) {
		return new L2ProjectionProduction(vertex) ;
	}

}
