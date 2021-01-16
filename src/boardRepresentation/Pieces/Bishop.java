package boardRepresentation.Pieces;

import java.util.Collection;

import utilities.BitboardUtils;
import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;

public class Bishop extends Piece{
	public static final Bishop bBishops = new Bishop.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BB).build();
	public static final Bishop wBishops = new Bishop.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WB).build();
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		Bishop build() {
			return new Bishop(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Bishop(Builder builder) {
		super(builder);
	}

	@Override
	public Collection<Move> genPseudoMoves(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Bishop getBishops(Color color) {
		if(color==Color.WHITE) {
			return wBishops;
		}
		return bBishops;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=BitboardUtils.SQUARE[58]|BitboardUtils.SQUARE[61];
		} else {
			bitboard|=BitboardUtils.SQUARE[2]|BitboardUtils.SQUARE[5];
		}
		return bitboard;
	}

}
