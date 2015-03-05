package main.tree;
import java.util.Arrays;
import java.util.List;

public class TestTreeBuilder {
	
	Element [][] elements;

	
	public Vertex buildTree(int size){
		DOF [][] dofs = makeDOFArray(size +1);
		elements = makeElementArray(dofs);
		return makeTree(0, size-1, 0, size-1);
	}
	
	public void printTree(String cus, Vertex v){
		System.out.println(cus + "children size: " + v.children.size());
		System.out.print(cus + "DOFS: ");
		for(DOF d : v.rowDofs){
			System.out.print(d.ID + " ");
		}
		System.out.println();
		System.out.println(cus + "eliminated count :" + v.eliminatedDofs);
		for(Vertex v1 : v.children){
			printTree(cus+"        ", v1);
		}
		
	}

	public DOF[][] makeDOFArray(int size) {
		DOF[][] result = new DOF[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				result[i][j] = new DOF(i * (size) + j);
			}
		}

		return result;
	}

	public Element[][] makeElementArray(DOF[][] dofs) {
		int size = dofs.length - 1;
		Element[][] elements = new Element[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				Element e = new Element(i * size + j);
				List<DOF> list = Arrays.asList(dofs[i][j], dofs[i + 1][j],
						dofs[i + 1][j + 1], dofs[i][j + 1]);
				e.dofs.addAll(list);
				for (DOF d : list) {
					d.elements.add(e);
				}

				elements[i][j] = e;
			}
		}
		return elements;
	}

	public Vertex makeTree(int x1, int x2, int y1, int y2) {
		int midX = x1 + x2;
		int midY = y1 + y2;
		if(x2-x1>0){
			Vertex v = new Vertex(null);
			v.children.add(makeTree(x1, midX / 2, y1, midY / 2));
			v.children.add(makeTree(x1, midX / 2, midY / 2 + 1, y2));
			v.children.add(makeTree(midX / 2 + 1, x2, y1, midY / 2));
			v.children.add(makeTree(midX / 2 + 1, x2, midY / 2 + 1, y2));
			return v;
			
		}else{
			return new Vertex(elements[x1][y1]);
		}
		
	}
	
	
}
