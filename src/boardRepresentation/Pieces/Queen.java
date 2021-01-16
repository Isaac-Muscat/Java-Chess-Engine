package boardRepresentation.Pieces;

import java.util.Collection;

import boardRepresentation.Board;
import boardRepresentation.Color;
import boardRepresentation.Moves.Move;
import utilities.BitboardUtils;

public class Queen extends Piece{
	public static final Queen bQueens = new Queen.Builder()
			.setBitboard(initBitboard(Color.BLACK))
			.setColor(Color.BLACK)
			.setPieceEnum(PieceEnum.BQ).build();
	public static final Queen wQueens = new Queen.Builder()
			.setBitboard(initBitboard(Color.WHITE))
			.setColor(Color.WHITE)
			.setPieceEnum(PieceEnum.WQ).build();
	
	private static class Builder extends Piece.Builder<Builder> {
		@Override
		Queen build() {
			return new Queen(this);
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Queen(Builder builder) {
		super(builder);
	}

	@Override
	public Collection<Move> genPseudoMoves(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Queen getQueens(Color color) {
		if(color==Color.WHITE) {
			return wQueens;
		}
		return bQueens;
	}

	public static long initBitboard(Color color) {
		long bitboard=0;
		if(color==Color.WHITE) {
			bitboard|=BitboardUtils.SQUARE[59];
		} else {
			bitboard|=BitboardUtils.SQUARE[3];
		}
		return bitboard;
	}
}