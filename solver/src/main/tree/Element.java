package main.tree;
import java.util.HashSet;
import java.util.Set;


public class Element {
	
	public int ID;
	public Set<DOF> dofs = new HashSet<>();
	
	public Element(int id){
		ID = id;
	}

}