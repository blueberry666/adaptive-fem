package main.tree;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Element {
	
	public int ID;
	public Set<DOF> dofs = new HashSet<>();
	
	public Element(int id){
		ID = id;
	}

	public void addDofs(List<DOF> dofsToAdd){
		dofs.addAll(dofsToAdd);
		for(DOF dof : dofsToAdd){
			dof.elements.add(this);
		}
		
	}
	
	public void addDof(DOF dof){
		dofs.add(dof);
		dof.elements.add(this);
	}
}
