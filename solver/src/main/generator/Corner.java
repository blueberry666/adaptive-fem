package main.generator;

public enum Corner {
	
	LT,
	RT,
	LB,
	RB;
	
	public static Direction[] getDirections(Corner c){
		switch(c){
		case LB:
			return new Direction[]{Direction.BOTTOM,Direction.LEFT};
		case RB:
			return new Direction[]{Direction.BOTTOM,Direction.RIGHT};
		case LT:
			return new Direction[]{Direction.TOP,Direction.LEFT};
		case RT:
			return new Direction[]{Direction.TOP,Direction.RIGHT};
			
		}
		return null;
	}

}
