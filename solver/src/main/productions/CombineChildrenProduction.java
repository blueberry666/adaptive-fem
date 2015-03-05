package main.productions;
import main.tree.DOF;
import main.tree.Vertex;



public class CombineChildrenProduction extends Production {

	public CombineChildrenProduction(Vertex vert) {
		super(vert);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply(Vertex vert) {

		for (Vertex child : vert.children) {
			for (DOF d : child.getNotEliminatedDOFS()) {
				int parentI = vert.rowDofs.indexOf(d);
				int childI = child.rowDofs.indexOf(d);
				for (DOF d2 : child.getNotEliminatedDOFS()) {

					int parentJ = vert.rowDofs.indexOf(d2);

					int childJ = child.rowDofs.indexOf(d2);
					vert.A[parentI][parentJ] += child.A[childI][childJ];

				}
				vert.b[parentI] += child.b[childI];
			}
		}

	}

}
