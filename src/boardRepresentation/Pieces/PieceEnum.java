package boardRepresentation.Pieces;

public enum PieceEnum {
	WP(0, "P"),
	WB(1, "B"),
	WN(2, "N"),
	WR(3, "R"),
	WQ(4, "Q"),
	WK(5, "K"),
	BP(6, "p"),
	BB(7, "b"),
	BN(8, "n"),
	BR(9, "r"),
	BQ(10, "q"),
	BK(11, "k");
	
	public static final int BLACK_INDEX_START = 6;
	private final int index;
	private final String symbol;
	
	private PieceEnum(int index, String symbol) {
		this.index = index;
		this.symbol = symbol;
	}
	
	public int getIndex() {
		return this.index;
	}
	public String getSymbol() {
		return this.symbol;
	}
}
