package main.generator;

public interface Neighborhood {

	class Empty implements Neighborhood{
		
		public Empty(){
			
		}
	}
	
	class SinglePart implements Neighborhood{
		
		public Part part;
		
		public SinglePart(Part p){
			part = p;
		}
		
	}
	
	class OneEdge implements Neighborhood{
		
		public Edge edge;
		
		public OneEdge(Edge e){
			edge = e;
		}
		
	}
	
	class TwoEdge implements Neighborhood{
		
		public Edge topOrRight;
		public Edge bottomOrLeft;
		
		public TwoEdge(Edge topRight, Edge bottomLeft){
			topOrRight = topRight;
			bottomOrLeft = bottomLeft;
		}
		
		public Edge getEdge(Direction d){
			switch (d) {
			case TOP:
			case RIGHT:
				return topOrRight;
			case BOTTOM:
			case LEFT:
				return bottomOrLeft;
			}
			return null;

		}
	}
}
