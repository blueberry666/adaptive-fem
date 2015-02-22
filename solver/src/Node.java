import java.util.Collection;


public interface Node {

	
	Collection<Node> getInNodes();
	String getName();
}
