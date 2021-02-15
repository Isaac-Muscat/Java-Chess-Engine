package boardRepresentation;

import boardRepresentation.Pieces.King;

public enum Color {
	WHITE(-1){
		public Color getOther() {
			return BLACK;
		}
		
		public int getStartIndex() {
			return 0;
		}

		@Override
		public BoardState inCheckMate() {
			return BoardState.WHITE_IN_CHECKMATE;
		}

		@Override
		public King getKing() {
			return King.getKings(WHITE);
		}
	},
	BLACK(1){
		public Color getOther() {
			return WHITE;
		}
		public int getStartIndex() {
			return 6;
		}
		@Override
		public BoardState inCheckMate() {
			return BoardState.BLACK_IN_CHECKMATE;
		}
		@Override
		public King getKing() {
			return King.getKings(BLACK);
		}
	};
	public abstract King getKing();
	public abstract BoardState inCheckMate();
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
