package boardRepresentation;

public enum Color {
	WHITE(-1),
	BLACK(1);
	
	private final int direction;
	private Color(int direction) {
		this.direction = direction;
	}
	
	public int getDirection() {
		return this.direction;
	}
}
