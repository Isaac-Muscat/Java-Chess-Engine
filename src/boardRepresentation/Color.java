package boardRepresentation;

public enum Color {
	WHITE(-1){
		public Color getOther() {
			return BLACK;
		}
		
		public int getStartIndex() {
			return 0;
		}
	},
	BLACK(1){
		public Color getOther() {
			return WHITE;
		}
		public int getStartIndex() {
			return 6;
		}
	};
	public abstract int getStartIndex();
	public abstract Color getOther();
	private final int direction;
	private Color(int direction) {
		this.direction = direction;
	}
	
	public int getDirection() {
		return this.direction;
	}
}
