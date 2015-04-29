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
}
