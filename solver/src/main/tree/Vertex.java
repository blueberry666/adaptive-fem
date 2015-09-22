package main.tree;
import java.util.LinkedList;
import java.util.List;

import main.utils.MatrixUtil;




public class Vertex {
	
	public Vertex parent;
	public List<Vertex> children = new LinkedList<>();
	public double [][] A;
	public double [] b;
	public double [] x;
	public List<DOF> rowDofs = new LinkedList<>();
	public Element element;
	public int eliminatedDofs;
	public List<Element> boundaryElements = new LinkedList<>();
	
	public Vertex(Element e){
		element = e;
	}

	public void generateRandomValues(){
		A = MatrixUtil.generateRandomMatrix(A.length, A.length);
		b = MatrixUtil.generateRandomVector(b.length);

	}
	
	public List<DOF> getNotEliminatedDOFS(){
		return rowDofs.subList(eliminatedDofs, rowDofs.size());
	}

}



