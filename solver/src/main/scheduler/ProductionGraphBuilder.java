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

public class ProductionGraphBuilder {
	
	private ProductionFactory factory;
	
	public ProductionGraphBuilder(ProductionFactory factory){
		this.factory = factory;
	}

	public Set<NotSoDummyNode> makeGraph(Vertex root) {
		NotSoDummyNode rootNode = makeLeftSideOfGraph(root);
		Set<NotSoDummyNode> leaves = new HashSet<>();
		makeRightSideOfGraph(root, rootNode, leaves);
		return leaves;

	}

	private NotSoDummyNode makeLeftSideOfGraph(Vertex rooty) {
		Queue<ClassyClass> q = new ArrayDeque<>();
		q.add(new ClassyClass(rooty, null));
		NotSoDummyNode dupa = null;
		while (!q.isEmpty()) {
			ClassyClass d = q.poll();
			Vertex root = d.vertex;
			NotSoDummyNode parent = d.parent;

			if (!root.children.isEmpty()) {
				CombineChildrenProduction combineProd = new CombineChildrenProduction(
						root);
				NotSoDummyNode combineNode = new NotSoDummyNode("notroot",
						combineProd);
				Production elimProd;

				elimProd = parent != null ? new PartialEliminationProduction(
						root) : new SolveRootProduction(root);
				NotSoDummyNode elimNode = new NotSoDummyNode("notroot",
						elimProd);
				if(dupa == null){
					dupa = elimNode;
				}

				if (parent != null) {
					parent.addInNode(elimNode);
				}
				elimNode.addInNode(combineNode);

				for (Vertex v : root.children) {
					q.add(new ClassyClass(v, combineNode));

				}

			} else {
				Production p = factory.makeProduction(root);
				NotSoDummyNode node = new NotSoDummyNode("leaf", p);
				parent.addInNode(node);
				if(dupa == null){
					dupa = node;
				}
			}
		}
//		System.out.println("after leftSide");
		return dupa;
	}

	private void makeRightSideOfGraph(Vertex rooty, NotSoDummyNode parenty,
			Set<NotSoDummyNode> leaves) {

		
		ClassyClass c = new ClassyClass(rooty, parenty);
		Queue <ClassyClass> q = new ArrayDeque<>();
		q.add(c);
		while (!q.isEmpty()) {
			ClassyClass cl = q.poll();
			Vertex root = cl.vertex;
			NotSoDummyNode parent = cl.parent;

			BackwardSubstitutionProduction bs = new BackwardSubstitutionProduction(
					root);
			NotSoDummyNode bsNode = new NotSoDummyNode("bs", bs);
			bsNode.addInNode(parent);
			if (root.children.isEmpty()) {
				leaves.add(bsNode);
			}
			for (Vertex v : root.children) {
				q.add(new ClassyClass(v, bsNode));
			}
		}
//		System.out.println("after rightSide");
	}

}

class ClassyClass{
	public Vertex vertex;
	public NotSoDummyNode parent;
	
	public ClassyClass(Vertex v, NotSoDummyNode n){
		vertex = v;
		parent = n;
	}
}
