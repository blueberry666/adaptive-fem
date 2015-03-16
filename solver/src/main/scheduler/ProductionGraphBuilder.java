package main.scheduler;

import java.util.HashSet;
import java.util.Set;

import main.productions.BackwardSubstitutionProduction;
import main.productions.CombineChildrenProduction;
import main.productions.PartialEliminationProduction;
import main.productions.Production;
import main.productions.SolveRootProduction;
import main.productions.productionFactory.ProductionFactory;
import main.tree.Vertex;

public class ProductionGraphBuilder {
	
	private ProductionFactory factory;
	
	public ProductionGraphBuilder(ProductionFactory factory){
		this.factory = factory;
	}

	public Set<NotSoDummyNode> makeGraph(Vertex root) {
		NotSoDummyNode rootNode = makeLeftSideOfGraph(root, null);
		Set<NotSoDummyNode> leaves = new HashSet<>();
		makeRightSideOfGraph(root, rootNode, leaves);
		return leaves;

	}

	private NotSoDummyNode makeLeftSideOfGraph(Vertex root,
			NotSoDummyNode parent) {

		if(!root.children.isEmpty()){
			CombineChildrenProduction combineProd = new CombineChildrenProduction(
					root);
			NotSoDummyNode combineNode = new NotSoDummyNode("notroot", combineProd);
			Production elimProd;

			elimProd = parent != null ? new PartialEliminationProduction(root)
					: new SolveRootProduction(root);
			NotSoDummyNode elimNode = new NotSoDummyNode("notroot", elimProd);

			if (parent != null) {
				parent.addInNode(elimNode);
			}
			elimNode.addInNode(combineNode);

			for (Vertex v : root.children) {
				makeLeftSideOfGraph(v, combineNode);

			}

			return elimNode;
		}else{
			Production p = factory.makeProduction(root);
			return new NotSoDummyNode("leaf", p);
			
		}
		

	}

	private void makeRightSideOfGraph(Vertex root, NotSoDummyNode parent,
			Set<NotSoDummyNode> leaves) {

		BackwardSubstitutionProduction bs = new BackwardSubstitutionProduction(
				root);
		NotSoDummyNode bsNode = new NotSoDummyNode("bs", bs);
		bsNode.addInNode(parent);

		if (root.children.isEmpty()) {
			leaves.add(bsNode);
		}
		for (Vertex v : root.children) {
			makeRightSideOfGraph(v, bsNode, leaves);
		}
	}

}
