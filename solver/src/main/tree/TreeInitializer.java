package main.tree;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class TreeInitializer {

	public static void visit(Vertex vertex){
		for(Vertex v : vertex.children){
			visit(v);
		}
		doStuff(vertex);
		
	}
	
	private static void doStuff(Vertex vertex){
		if(vertex.element != null){
			vertex.rowDofs.addAll(vertex.element.dofs);
			vertex.boundaryElements.add(vertex.element);
		}else{
			Set<DOF> dofs = new HashSet<>();
			Set<Element> chlidrenBoundaryElements = new HashSet<>();
			for(Vertex v : vertex.children){
				dofs.addAll(v.getNotEliminatedDOFS());
				chlidrenBoundaryElements.addAll(v.boundaryElements);
			}
			findEliminatedDofs(vertex, dofs, chlidrenBoundaryElements);
			vertex.rowDofs.addAll(dofs);
			findBoundaryElements(vertex);
			
		}
		initializeMatrices(vertex);
	}

	private static void findEliminatedDofs(Vertex vertex, Set<DOF> dofs,
			Set<Element> chlidrenBoundaryElements) {
		Iterator<DOF> it = dofs.iterator();
		while(it.hasNext()){
			DOF d = it.next();
			boolean isInBoundaryElements = true;
			for(Element e : d.elements){
				if(!chlidrenBoundaryElements.contains(e)){
					isInBoundaryElements = false;
					break;
				}
			}
			if(isInBoundaryElements){
				vertex.rowDofs.add(d);
				++vertex.eliminatedDofs;
				it.remove();
			}
		}
	}

	private static void findBoundaryElements(Vertex vertex) {
		Set<Element> boundaryElements = new HashSet<>();
		for (Vertex v : vertex.children) {
			boundaryElements.addAll(v.boundaryElements);
		}

		for (Element e : boundaryElements) {
			boolean isBoundary = false;
			outerLoop: 
			for (DOF d : e.dofs) {
				for (Element el : d.elements) {
					if (!boundaryElements.contains(el)) {
						isBoundary = true;
						break outerLoop;
					}
				}
			}
			if (isBoundary) {
				vertex.boundaryElements.add(e);
			}
		}

	}

	private static void initializeMatrices(Vertex vertex) {
		vertex.A = new double[vertex.rowDofs.size()][vertex.rowDofs.size()];
		vertex.b = new double[vertex.rowDofs.size()];
	}
}