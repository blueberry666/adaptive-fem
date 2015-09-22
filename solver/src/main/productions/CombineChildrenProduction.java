package main.productions;
import main.tree.DOF;
import main.tree.Vertex;



public class CombineChildrenProduction extends Production {

	public CombineChildrenProduction(Vertex vert) {
		super(vert);
	}

	@Override
	public void apply() {

		for (Vertex child : vertex.children) {
			for (DOF d : child.getNotEliminatedDOFS()) {
				int parentI = vertex.rowDofs.indexOf(d);
				int childI = child.rowDofs.indexOf(d);
				for (DOF d2 : child.getNotEliminatedDOFS()) {

					int parentJ = vertex.rowDofs.indexOf(d2);

					int childJ = child.rowDofs.indexOf(d2);
					vertex.A[parentI][parentJ] += child.A[childI][childJ];

				}
				vertex.b[parentI] += child.b[childI];
			}
		}

	}

}
