package main.scheduler;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import main.productions.BackwardSubstitutionProduction;
import main.productions.CombineChildrenProduction;
import main.productions.PartialEliminationProduction;
import main.productions.Production;
import main.productions.SolveRootProduction;
import main.productions.productionFactory.ProductionFactory;
import main.tree.Vertex;
import main.utils.VertexData;

public class ProductionGraphBuilder {
	
	private ProductionFactory factory;
	
	public ProductionGraphBuilder(ProductionFactory factory){
		this.factory = factory;
	}

	public Set<SmartNode> makeGraph(Vertex root) {
		SmartNode rootNode = makeLeftSideOfGraph(root);
		Set<SmartNode> leaves = new HashSet<>();
		makeRightSideOfGraph(root, rootNode, leaves);
		return leaves;

	}

	private SmartNode makeLeftSideOfGraph(Vertex root) {
		Queue<VertexData> q = new ArrayDeque<>();
		q.add(new VertexData(root, (SmartNode) null));
		SmartNode rootNode = null;
		while (!q.isEmpty()) {
			VertexData d = q.poll();
			Vertex rootVertex = d.vertex;
			SmartNode parent = d.parent;

			if (!rootVertex.children.isEmpty()) {
				CombineChildrenProduction combineProd = new CombineChildrenProduction(
						rootVertex);
				SmartNode combineNode = new SmartNode("notroot",
						combineProd);
				Production elimProd;

				elimProd = parent != null ? new PartialEliminationProduction(
						rootVertex) : new SolveRootProduction(rootVertex);
				SmartNode elimNode = new SmartNode("notroot",
						elimProd);
				if (rootNode == null) {
					rootNode = elimNode;
				}

				if (parent != null) {
					parent.addInNode(elimNode);
				}
				elimNode.addInNode(combineNode);

				for (Vertex v : rootVertex.children) {
					q.add(new VertexData(v, combineNode));

				}

			} else {
				Production p = factory.makeProduction(rootVertex);
				SmartNode node = new SmartNode("leaf", p);
				parent.addInNode(node);
				if (rootNode == null) {
					rootNode = node;
				}
			}
		}
		return rootNode;
	}

	private void makeRightSideOfGraph(Vertex rooty, SmartNode parenty,
			Set<SmartNode> leaves) {

		
		VertexData c = new VertexData(rooty, parenty);
		Queue <VertexData> q = new ArrayDeque<>();
		q.add(c);
		while (!q.isEmpty()) {
			VertexData cl = q.poll();
			Vertex root = cl.vertex;
			SmartNode parent = cl.parent;

			BackwardSubstitutionProduction bs = new BackwardSubstitutionProduction(
					root);
			SmartNode bsNode = new SmartNode("bs", bs);
			bsNode.addInNode(parent);
			if (root.children.isEmpty()) {
				leaves.add(bsNode);
			}
			for (Vertex v : root.children) {
				q.add(new VertexData(v, bsNode));
			}
		}
	}

}


