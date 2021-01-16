package boardRepresentation.Pieces;

import java.util.Collection;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;

public class King extends Piece{
	public static final King bKings = new King.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BK).build();
	public static final King wKings = new King.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WK).build();
	private boolean firstMove = true;
	private boolean qCastleRight = true;
	private boolean kCastleRight = true;
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		King build() {
			return new King(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private King(Builder builder) {
		super(builder);
	}

	@Override
	public Collection<Move> genPseudoMoves(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static King getKings(Color color) {
		if(color==Color.WHITE) {
			return wKings;
		}
		return bKings;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=BitboardUtils.SQUARE[60];
		} else {
			bitboard|=BitboardUtils.SQUARE[4];
		}
		return bitboard;
	}
}
