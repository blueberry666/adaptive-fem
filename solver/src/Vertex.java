import java.util.LinkedList;
import java.util.List;




public class Vertex {
	
	public Vertex parent;
	public Vertex[] children;
	public double [][] A;
	public double [] b;
	public double [] x;
	public List<DOF> rowDofs = new LinkedList<>();
	public Element element;
	public int eliminatedDofs;
	public List<Element> boundaryElements = new LinkedList<>();
	
	public Vertex(Vertex parent){
		this.parent = parent;
		
	}

	
	
	public void generateRandomValues(){
		A = MatrixUtil.generateRandomMatrix(A.length, A.length);
		b = MatrixUtil.generateRandomVector(b.length);

	}
	
	
	
	public List<DOF> getNotEliminatedDOFS(){
		return rowDofs.subList(eliminatedDofs, rowDofs.size());
	}
	
	
	

}



