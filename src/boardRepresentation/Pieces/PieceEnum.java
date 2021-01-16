package boardRepresentation.Pieces;

public enum PieceEnum {
	WP(0),
	WB(1),
	WN(2),
	WR(3),
	WQ(4),
	WK(5),
	BP(6),
	BB(7),
	BN(8),
	BR(9),
	BQ(10),
	BK(11);
	
	private final int index;
	
	private PieceEnum(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
}
