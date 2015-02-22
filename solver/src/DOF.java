import java.util.HashSet;
import java.util.Set;

public class DOF {

	public int ID;
	public Set<Element> elements = new HashSet<>();

	public DOF(int id) {
		ID = id;
	}

}
