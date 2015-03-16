package main.productions.productionFactory;

import main.productions.Production;
import main.tree.Vertex;

public interface ProductionFactory {

	Production makeProduction(Vertex vertex);
}
