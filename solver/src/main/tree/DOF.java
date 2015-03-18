package main.tree;
import java.util.HashSet;
import java.util.Set;

public class DOF implements Comparable<DOF>{

	public int ID;
	public Set<Element> elements = new HashSet<>();

	public DOF(int id) {
		ID = id;
	}
	
	


	@Override
	public int compareTo(DOF dof1) {
		if(ID == dof1.ID){
			return 0;
		}
		return ID > dof1.ID?1:-1;
	}

}
