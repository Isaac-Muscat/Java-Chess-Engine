package boardRepresentation;

import boardRepresentation.Pieces.King;

public enum Color {
	WHITE(){
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

		@Override
		public int getDirection() {
			return -1;
		}
	},
	BLACK(){
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
		@Override
		public int getDirection() {
			return 1;
		}
	};
	public abstract King getKing();
	public abstract BoardState inCheckMate();
	public abstract int getStartIndex();
	public abstract Color getOther();
	public abstract int getDirection();
}
