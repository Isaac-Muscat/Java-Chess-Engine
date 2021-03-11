package boardRepresentation.Pieces;

public enum PieceEnum {
	WP(0, "P", 100),
	WB(1, "B", 305),
	WN(2, "N", 300),
	WR(3, "R", 500),
	WQ(4, "Q", 900),
	WK(5, "K", 10000000),
	BP(6, "p", -100),
	BB(7, "b", -305),
	BN(8, "n", -300),
	BR(9, "r", -500),
	BQ(10, "q", -900),
	BK(11, "k", -10000000);
	
	public static final int BLACK_INDEX_START = 6;
	private final int index;
	private final String symbol;
	private final int value;
	
	private PieceEnum(int index, String symbol, int value) {
		this.index = index;
		this.symbol = symbol;
		this.value = value;
	}
	
	public int getIndex() {
		return this.index;
	}
	public int getValue() {
		return value;
	}
	public String getSymbol() {
		return this.symbol;
	}
	
	public static PieceEnum getPieceByString (String symbol) {
		for(PieceEnum piece: PieceEnum.values()) {
			if(symbol.equals(piece.getSymbol()))return piece;
		}
		return null;
	}
}
