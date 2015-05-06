package main.generator;

public enum Direction {
	
	LEFT,
	TOP,
	RIGHT,
	BOTTOM;

	public static Direction opposite(Direction d){
		switch (d) {
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case TOP:
			return BOTTOM;
		case BOTTOM:
			return TOP;

		}
		return null;
	}
	
	public static Direction next(Direction d){
		switch (d) {
		case LEFT:
			return BOTTOM;
		case RIGHT:
			return TOP;
		case TOP:
			return LEFT;
		case BOTTOM:
			return RIGHT;

		}
		return null;
	}
	
	public static Direction prev(Direction d){
		switch (d) {
		case LEFT:
			return TOP;
		case RIGHT:
			return BOTTOM;
		case TOP:
			return RIGHT;
		case BOTTOM:
			return LEFT;

		}
		return null;
	}
}
