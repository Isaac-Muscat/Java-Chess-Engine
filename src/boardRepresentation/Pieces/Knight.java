package boardRepresentation.Pieces;

import java.util.Collection;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;

public class Knight extends Piece{
	public static final Knight bKnights = new Knight.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BN).build();
	public static final Knight wKnights = new Knight.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WN).build();
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		Knight build() {
			return new Knight(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Knight(Builder builder) {
		super(builder);
	}

	@Override
	public Collection<Move> genPseudoMoves(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Knight getKnights(Color color) {
		if(color==Color.WHITE) {
			return wKnights;
		}
		return bKnights;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=BitboardUtils.SQUARE[57]|BitboardUtils.SQUARE[62];
		} else {
			bitboard|=BitboardUtils.SQUARE[1]|BitboardUtils.SQUARE[6];
		}
		return bitboard;
	}
}
